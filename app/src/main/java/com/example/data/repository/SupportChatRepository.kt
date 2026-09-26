package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.example.data.model.UserProfile
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.UUID

data class SupportChatMessage(
  val id: String,
  val userId: String,
  val userName: String,
  val userEmail: String,
  val senderType: String, // "USER" or "MANAGER"
  val senderName: String,
  val text: String,
  val timestampMillis: Long
)

data class UserChatConversation(
  val userId: String,
  val userName: String,
  val userEmail: String,
  val lastMessageText: String,
  val lastMessageTimestamp: Long,
  val messageCount: Int,
  val hasUnreadFromUser: Boolean
)

class SupportChatRepository(private val context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences("ca_support_chat_prefs", Context.MODE_PRIVATE)

  private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
  private val listType = Types.newParameterizedType(List::class.java, SupportChatMessage::class.java)
  private val listAdapter = moshi.adapter<List<SupportChatMessage>>(listType)

  private val _messagesFlow = MutableStateFlow<List<SupportChatMessage>>(loadMessagesFromPrefs())
  val messagesFlow: StateFlow<List<SupportChatMessage>> = _messagesFlow.asStateFlow()

  private val _customLogoBase64Flow = MutableStateFlow<String?>(prefs.getString("custom_logo_base64", null))
  val customLogoBase64Flow: StateFlow<String?> = _customLogoBase64Flow.asStateFlow()

  private val _customAppNameFlow = MutableStateFlow<String?>(prefs.getString("custom_app_name", null))
  val customAppNameFlow: StateFlow<String?> = _customAppNameFlow.asStateFlow()

  private val _customBoxTitlesFlow = MutableStateFlow<Map<String, String>>(loadCustomBoxTitles())
  val customBoxTitlesFlow: StateFlow<Map<String, String>> = _customBoxTitlesFlow.asStateFlow()

  private fun loadCustomBoxTitles(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    prefs.all.forEach { (key, value) ->
      if (key.startsWith("box_title_") && value is String) {
        val boxKey = key.removePrefix("box_title_")
        map[boxKey] = value
      }
    }
    return map
  }

  fun saveCustomBoxTitle(boxKey: String, newTitle: String) {
    val trimmed = newTitle.trim()
    if (trimmed.isBlank()) {
      prefs.edit().remove("box_title_$boxKey").apply()
    } else {
      prefs.edit().putString("box_title_$boxKey", trimmed).apply()
    }
    _customBoxTitlesFlow.value = loadCustomBoxTitles()
  }

  fun clearCustomBoxTitle(boxKey: String) {
    prefs.edit().remove("box_title_$boxKey").apply()
    _customBoxTitlesFlow.value = loadCustomBoxTitles()
  }

  fun clearAllCustomBoxTitles() {
    val keysToRemove = prefs.all.keys.filter { it.startsWith("box_title_") }
    val editor = prefs.edit()
    keysToRemove.forEach { editor.remove(it) }
    editor.apply()
    _customBoxTitlesFlow.value = emptyMap()
  }

  init {
    if (loadMessagesFromPrefs().isEmpty()) {
      // Create initial demo inquiry from demo student so manager sees sample chat
      val demoMsg = SupportChatMessage(
        id = UUID.randomUUID().toString(),
        userId = "demo_student",
        userName = "CA Student Member",
        userEmail = "ca.student@icai.org",
        senderType = "USER",
        senderName = "CA Student Member",
        text = "Hello Manager sir! Could you please guide me on how to calculate Section 44ADA presumptive tax?",
        timestampMillis = System.currentTimeMillis() - 3600000L * 3
      )
      val managerReply = SupportChatMessage(
        id = UUID.randomUUID().toString(),
        userId = "demo_student",
        userName = "CA Student Member",
        userEmail = "ca.student@icai.org",
        senderType = "MANAGER",
        senderName = "CA Muhammad Faizan (Manager / Admin)",
        text = "Welcome! In Section 44ADA for professionals, 50% of gross receipts is considered taxable income. You can use our Tax calculator module on Home!",
        timestampMillis = System.currentTimeMillis() - 3600000L * 2
      )
      saveMessagesToPrefs(listOf(demoMsg, managerReply))
    }
  }

  private fun loadMessagesFromPrefs(): List<SupportChatMessage> {
    val json = prefs.getString("all_support_messages", null) ?: return emptyList()
    return try {
      listAdapter.fromJson(json) ?: emptyList()
    } catch (e: Exception) {
      emptyList()
    }
  }

  private fun saveMessagesToPrefs(messages: List<SupportChatMessage>) {
    val json = listAdapter.toJson(messages)
    prefs.edit().putString("all_support_messages", json).apply()
    _messagesFlow.value = messages
  }

  fun getMessagesForUser(userId: String, userName: String, userEmail: String): List<SupportChatMessage> {
    val all = _messagesFlow.value.filter { it.userId == userId }.sortedBy { it.timestampMillis }
    if (all.isEmpty()) {
      // Include an automatic greeting from Manager
      return listOf(
        SupportChatMessage(
          id = "welcome_$userId",
          userId = userId,
          userName = userName,
          userEmail = userEmail,
          senderType = "MANAGER",
          senderName = "CA Muhammad Faizan (Manager / Admin)",
          text = "Hello $userName! Welcome to CA Utility Official Support. Send your queries here and our Manager will reply directly.",
          timestampMillis = System.currentTimeMillis()
        )
      )
    }
    return all
  }

  fun getAllConversations(): List<UserChatConversation> {
    val all = _messagesFlow.value
    val grouped = all.groupBy { it.userId }
    return grouped.map { (userId, msgs) ->
      val sorted = msgs.sortedBy { it.timestampMillis }
      val lastMsg = sorted.last()
      val lastFromUser = sorted.lastOrNull { it.senderType == "USER" }
      UserChatConversation(
        userId = userId,
        userName = lastMsg.userName,
        userEmail = lastMsg.userEmail,
        lastMessageText = lastMsg.text,
        lastMessageTimestamp = lastMsg.timestampMillis,
        messageCount = sorted.size,
        hasUnreadFromUser = lastMsg.senderType == "USER"
      )
    }.sortedByDescending { it.lastMessageTimestamp }
  }

  fun sendUserMessage(user: UserProfile, text: String) {
    if (text.isBlank()) return
    val newMsg = SupportChatMessage(
      id = UUID.randomUUID().toString(),
      userId = user.id,
      userName = user.fullName,
      userEmail = user.email,
      senderType = "USER",
      senderName = user.fullName,
      text = text.trim(),
      timestampMillis = System.currentTimeMillis()
    )
    saveMessagesToPrefs(_messagesFlow.value + newMsg)
  }

  fun sendManagerReply(targetUserId: String, targetUserName: String, targetUserEmail: String, text: String) {
    if (text.isBlank()) return
    val newMsg = SupportChatMessage(
      id = UUID.randomUUID().toString(),
      userId = targetUserId,
      userName = targetUserName,
      userEmail = targetUserEmail,
      senderType = "MANAGER",
      senderName = "CA Muhammad Faizan (Manager / Admin)",
      text = text.trim(),
      timestampMillis = System.currentTimeMillis()
    )
    saveMessagesToPrefs(_messagesFlow.value + newMsg)
  }

  fun saveCustomLogoFromUri(uri: Uri): Boolean {
    return try {
      val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
      if (inputStream != null) {
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        if (bitmap != null) {
          // Scale bitmap so base64 string is compact
          val maxDim = 320
          val scaled = if (bitmap.width > maxDim || bitmap.height > maxDim) {
            val ratio = maxDim.toFloat() / Math.max(bitmap.width, bitmap.height)
            Bitmap.createScaledBitmap(bitmap, (bitmap.width * ratio).toInt(), (bitmap.height * ratio).toInt(), true)
          } else {
            bitmap
          }
          val stream = ByteArrayOutputStream()
          scaled.compress(Bitmap.CompressFormat.PNG, 85, stream)
          val bytes = stream.toByteArray()
          val base64 = Base64.encodeToString(bytes, Base64.DEFAULT)
          prefs.edit().putString("custom_logo_base64", base64).apply()
          _customLogoBase64Flow.value = base64
          true
        } else {
          false
        }
      } else {
        false
      }
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }

  fun clearCustomLogo() {
    prefs.edit().remove("custom_logo_base64").apply()
    _customLogoBase64Flow.value = null
  }

  fun saveCustomAppName(name: String) {
    val trimmed = name.trim()
    if (trimmed.isBlank()) {
      clearCustomAppName()
    } else {
      prefs.edit().putString("custom_app_name", trimmed).apply()
      _customAppNameFlow.value = trimmed
    }
  }

  fun clearCustomAppName() {
    prefs.edit().remove("custom_app_name").apply()
    _customAppNameFlow.value = null
  }

  fun getDecodedCustomLogoBitmap(base64: String?): Bitmap? {
    if (base64.isNullOrBlank()) return null
    return try {
      val bytes = Base64.decode(base64, Base64.DEFAULT)
      BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } catch (e: Exception) {
      null
    }
  }
}
