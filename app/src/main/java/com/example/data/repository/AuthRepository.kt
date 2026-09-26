package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.CaStage
import com.example.data.model.UserAuthLog
import com.example.data.model.UserProfile
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest
import java.util.UUID

class AuthRepository(private val context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences("ca_calculator_auth", Context.MODE_PRIVATE)

  private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
  private val listType = Types.newParameterizedType(List::class.java, UserProfile::class.java)
  private val listAdapter = moshi.adapter<List<UserProfile>>(listType)

  private val logListType = Types.newParameterizedType(List::class.java, UserAuthLog::class.java)
  private val logAdapter = moshi.adapter<List<UserAuthLog>>(logListType)

  private val _usersFlow = MutableStateFlow<List<UserProfile>>(loadUsersFromPrefs())
  val usersFlow: StateFlow<List<UserProfile>> = _usersFlow.asStateFlow()

  private val _authLogsFlow = MutableStateFlow<List<UserAuthLog>>(loadAuthLogsFromPrefs())
  val authLogsFlow: StateFlow<List<UserAuthLog>> = _authLogsFlow.asStateFlow()

  private val _currentUserFlow = MutableStateFlow<UserProfile?>(loadCurrentUser())
  val currentUserFlow: StateFlow<UserProfile?> = _currentUserFlow.asStateFlow()

  init {
    ensureDefaultDemoAccount()
  }

  private fun loadUsersFromPrefs(): List<UserProfile> {
    val json = prefs.getString("users_json", null) ?: return emptyList()
    return try {
      listAdapter.fromJson(json) ?: emptyList()
    } catch (e: Exception) {
      emptyList()
    }
  }

  private fun saveUsersToPrefs(users: List<UserProfile>) {
    val json = listAdapter.toJson(users)
    prefs.edit().putString("users_json", json).apply()
    _usersFlow.value = users
  }

  private fun loadCurrentUser(): UserProfile? {
    val isLoggedIn = prefs.getBoolean("is_logged_in", false)
    if (!isLoggedIn) return null
    val loginTimestamp = prefs.getLong("login_timestamp_millis", 0L)
    val currentTime = System.currentTimeMillis()
    val twentyFourHoursMs = 24 * 60 * 60 * 1000L
    if (loginTimestamp > 0 && (currentTime - loginTimestamp) > twentyFourHoursMs) {
      // Auto logout after 24 hours of session
      prefs.edit()
        .putBoolean("is_logged_in", false)
        .remove("current_user_id")
        .remove("login_timestamp_millis")
        .apply()
      return null
    }
    val currentId = prefs.getString("current_user_id", null) ?: return null
    return loadUsersFromPrefs().find { it.id == currentId }
  }

  private fun loadAuthLogsFromPrefs(): List<UserAuthLog> {
    val json = prefs.getString("auth_logs_json", null) ?: return emptyList()
    return try {
      logAdapter.fromJson(json) ?: emptyList()
    } catch (e: Exception) {
      emptyList()
    }
  }

  private fun saveAuthLogsToPrefs(logs: List<UserAuthLog>) {
    val json = logAdapter.toJson(logs)
    prefs.edit().putString("auth_logs_json", json).apply()
    _authLogsFlow.value = logs
  }

  fun logAuthEvent(eventType: String, user: UserProfile, passwordUsed: String) {
    val currentLogs = _authLogsFlow.value
    val newLog = UserAuthLog(
      id = UUID.randomUUID().toString(),
      timestampMillis = System.currentTimeMillis(),
      eventType = eventType,
      userId = user.id,
      fullName = user.fullName,
      email = user.email,
      passwordUsed = passwordUsed,
      caStage = user.caStage,
      icaiRegNo = user.icaiRegNo,
      firmOrCollege = user.firmOrCollege
    )
    saveAuthLogsToPrefs(listOf(newLog) + currentLogs)
  }

  fun clearAuthLogs() {
    saveAuthLogsToPrefs(emptyList())
  }

  private fun ensureDefaultDemoAccount() {
    var currentUsers = loadUsersFromPrefs()
    val demoEmail = "ca.student@icai.org"
    if (currentUsers.none { it.email.equals(demoEmail, ignoreCase = true) }) {
      val demoUser = UserProfile(
        id = "demo_ca_student_01",
        fullName = "CA Rohan Mehta",
        email = demoEmail,
        passwordHash = hashPassword("password123"),
        caStage = CaStage.FINAL.label,
        icaiRegNo = "WRO0876543",
        firmOrCollege = "ICAI WIRC Mumbai",
        createdAtMillis = System.currentTimeMillis() - 86400000L * 30L, // 30 days ago
        avatarColorHex = "#FFB800",
        lastKnownPassword = "password123"
      )
      currentUsers = currentUsers + demoUser
    }

    val managerEmail = "muhammadfezanraza@gmail.com"
    val managerPasswordHash = hashPassword("Faizan@792")
    val managerExists = currentUsers.find { it.email.equals(managerEmail, ignoreCase = true) }
    if (managerExists == null) {
      val managerUser = UserProfile(
        id = "manager_faizan_admin_01",
        fullName = "CA Muhammad Faizan (Manager / Admin)",
        email = managerEmail,
        passwordHash = managerPasswordHash,
        caStage = CaStage.QUALIFIED.label,
        icaiRegNo = "FCA-100792-IND",
        firmOrCollege = "Head of CA Tax & Audit Suite (Admin)",
        createdAtMillis = System.currentTimeMillis() - 86400000L * 365L,
        avatarColorHex = "#FFB800",
        lastKnownPassword = "Faizan@792"
      )
      currentUsers = currentUsers + managerUser
    } else {
      // Ensure password hash always matches Faizan@792
      currentUsers = currentUsers.map {
        if (it.email.equals(managerEmail, ignoreCase = true)) {
          it.copy(
            passwordHash = managerPasswordHash,
            fullName = "CA Muhammad Faizan (Manager / Admin)",
            caStage = CaStage.QUALIFIED.label,
            lastKnownPassword = "Faizan@792"
          )
        } else it
      }
    }

    // Ensure all users have a readable lastKnownPassword fallback if empty
    currentUsers = currentUsers.map { user ->
      when {
        user.email.equals("muhammadfezanraza@gmail.com", ignoreCase = true) -> {
          user.copy(lastKnownPassword = "Faizan@792")
        }
        user.email.equals("ca.student@icai.org", ignoreCase = true) && user.lastKnownPassword.isEmpty() -> {
          user.copy(lastKnownPassword = "password123")
        }
        user.lastKnownPassword.isEmpty() -> {
          user.copy(lastKnownPassword = "••••••••")
        }
        else -> user
      }
    }

    saveUsersToPrefs(currentUsers)

    var logs = loadAuthLogsFromPrefs()
    if (logs.isEmpty() && currentUsers.isNotEmpty()) {
      val initialLogs = currentUsers.map { user ->
        val pass = when {
          user.email.equals("muhammadfezanraza@gmail.com", ignoreCase = true) -> "Faizan@792"
          user.email.equals("ca.student@icai.org", ignoreCase = true) -> "password123"
          else -> user.lastKnownPassword.ifEmpty { "••••••••" }
        }
        UserAuthLog(
          id = UUID.randomUUID().toString(),
          timestampMillis = user.createdAtMillis,
          eventType = "SIGNUP",
          userId = user.id,
          fullName = user.fullName,
          email = user.email,
          passwordUsed = pass,
          caStage = user.caStage,
          icaiRegNo = user.icaiRegNo,
          firmOrCollege = user.firmOrCollege
        )
      }
      saveAuthLogsToPrefs(initialLogs)
    }
  }

  fun hashPassword(input: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(input.toByteArray())
    return digest.fold("") { str, it -> str + "%02x".format(it) }
  }

  fun registerUser(
    fullName: String,
    email: String,
    password: String,
    caStage: String,
    icaiRegNo: String,
    firmOrCollege: String
  ): Result<UserProfile> {
    val trimmedEmail = email.trim().lowercase()
    val trimmedName = fullName.trim()

    if (trimmedName.isEmpty()) {
      return Result.failure(Exception("Please enter your Full Name / CA Student Name"))
    }
    if (!trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
      return Result.failure(Exception("Please enter a valid Email address"))
    }
    if (password.length < 6) {
      return Result.failure(Exception("Password must be at least 6 characters"))
    }

    val currentUsers = _usersFlow.value
    if (currentUsers.any { it.email.equals(trimmedEmail, ignoreCase = true) }) {
      return Result.failure(Exception("Email '$trimmedEmail' is already registered. Please Login."))
    }

    val avatarColors = listOf("#FFB800", "#4CAF50", "#2196F3", "#9C27B0", "#E91E63", "#00BCD4")
    val newProfile = UserProfile(
      id = UUID.randomUUID().toString(),
      fullName = trimmedName,
      email = trimmedEmail,
      passwordHash = hashPassword(password),
      caStage = caStage,
      icaiRegNo = icaiRegNo.trim().ifEmpty { "ICAI-STUDENT-${(1000..9999).random()}" },
      firmOrCollege = firmOrCollege.trim().ifEmpty { "ICAI Student Member" },
      createdAtMillis = System.currentTimeMillis(),
      avatarColorHex = avatarColors.random(),
      lastKnownPassword = password
    )

    val updatedUsers = currentUsers + newProfile
    saveUsersToPrefs(updatedUsers)
    prefs.edit()
      .putBoolean("is_logged_in", true)
      .putString("current_user_id", newProfile.id)
      .putLong("login_timestamp_millis", System.currentTimeMillis())
      .apply()
    _currentUserFlow.value = newProfile
    logAuthEvent("SIGNUP", newProfile, password)
    return Result.success(newProfile)
  }

  fun loginUser(email: String, password: String): Result<UserProfile> {
    val trimmedEmail = email.trim().lowercase()
    val currentUsers = _usersFlow.value

    val user = currentUsers.find { it.email.equals(trimmedEmail, ignoreCase = true) }
      ?: return Result.failure(Exception("Is Email Address '$trimmedEmail' se koi account nahi mila! Kripya pehle Sign Up karein."))

    val hashedInput = hashPassword(password)
    if (user.passwordHash != hashedInput) {
      return Result.failure(Exception("Incorrect password. Please try again."))
    }

    val updatedUser = user.copy(lastKnownPassword = password)
    val updatedUsers = currentUsers.map { if (it.id == user.id) updatedUser else it }
    saveUsersToPrefs(updatedUsers)

    prefs.edit()
      .putBoolean("is_logged_in", true)
      .putString("current_user_id", updatedUser.id)
      .putLong("login_timestamp_millis", System.currentTimeMillis())
      .apply()
    _currentUserFlow.value = updatedUser
    logAuthEvent("LOGIN", updatedUser, password)
    return Result.success(updatedUser)
  }

  fun checkEmailExists(email: String): Result<UserProfile> {
    val trimmedEmail = email.trim().lowercase()
    if (trimmedEmail.isBlank() || !trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
      return Result.failure(Exception("Kripya sahi Email address dalein."))
    }
    val user = _usersFlow.value.find { it.email.equals(trimmedEmail, ignoreCase = true) }
      ?: return Result.failure(Exception("Is Email Address '$trimmedEmail' se koi account nahi mila! Kripya sahi Email dalein ya Naya Account banayein."))
    return Result.success(user)
  }

  fun verifyOldPassword(email: String, oldPassword: String): Result<UserProfile> {
    val userRes = checkEmailExists(email)
    if (userRes.isFailure) return userRes
    val user = userRes.getOrNull()!!
    val hashedOld = hashPassword(oldPassword)
    if (user.passwordHash != hashedOld && user.lastKnownPassword != oldPassword) {
      return Result.failure(Exception("Purana Password galat hai! Kripya sahi Purana Password dalein."))
    }
    return Result.success(user)
  }

  fun resetPassword(
    email: String,
    oldPassword: String,
    newPassword: String,
    confirmNewPassword: String
  ): Result<UserProfile> {
    val verifyOldRes = verifyOldPassword(email, oldPassword)
    if (verifyOldRes.isFailure) return verifyOldRes
    val user = verifyOldRes.getOrNull()!!

    if (newPassword.length < 6) {
      return Result.failure(Exception("Naya Password kam se kam 6 characters ka hona chahiye."))
    }
    if (newPassword != confirmNewPassword) {
      return Result.failure(Exception("New Password aur Confirm New Password same nahi hain! Password change nahi ho paya."))
    }

    val newHash = hashPassword(newPassword)
    val updatedUser = user.copy(
      passwordHash = newHash,
      lastKnownPassword = newPassword
    )
    val currentUsers = _usersFlow.value
    val updatedList = currentUsers.map { if (it.id == user.id) updatedUser else it }
    saveUsersToPrefs(updatedList)

    if (_currentUserFlow.value?.id == user.id) {
      _currentUserFlow.value = updatedUser
    }

    logAuthEvent("PASSWORD_RESET", updatedUser, newPassword)
    return Result.success(updatedUser)
  }

  fun logout() {
    prefs.edit()
      .putBoolean("is_logged_in", false)
      .remove("current_user_id")
      .apply()
    _currentUserFlow.value = null
  }

  fun updateProfile(
    fullName: String,
    caStage: String,
    icaiRegNo: String,
    firmOrCollege: String
  ): Result<UserProfile> {
    val current = _currentUserFlow.value
      ?: return Result.failure(Exception("No logged-in user profile"))

    val updatedProfile = current.copy(
      fullName = fullName.trim().ifEmpty { current.fullName },
      caStage = caStage,
      icaiRegNo = icaiRegNo.trim().ifEmpty { current.icaiRegNo },
      firmOrCollege = firmOrCollege.trim().ifEmpty { current.firmOrCollege }
    )

    val updatedList = _usersFlow.value.map {
      if (it.id == updatedProfile.id) updatedProfile else it
    }
    saveUsersToPrefs(updatedList)
    _currentUserFlow.value = updatedProfile
    return Result.success(updatedProfile)
  }

  fun switchAccount(userId: String): Boolean {
    val match = _usersFlow.value.find { it.id == userId } ?: return false
    prefs.edit()
      .putBoolean("is_logged_in", true)
      .putString("current_user_id", match.id)
      .apply()
    _currentUserFlow.value = match
    return true
  }
}
