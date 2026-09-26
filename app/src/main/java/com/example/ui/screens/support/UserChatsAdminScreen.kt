package com.example.ui.screens.support

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.SupportChatRepository
import com.example.data.repository.UserChatConversation
import com.example.ui.components.LuxuryCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAmber
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextGoldSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UserChatsAdminScreen(
  supportChatRepository: SupportChatRepository
) {
  val context = LocalContext.current
  val messagesFlow by supportChatRepository.messagesFlow.collectAsState()

  val conversations = remember(messagesFlow) {
    supportChatRepository.getAllConversations()
  }

  var selectedConversation by remember { mutableStateOf<UserChatConversation?>(null) }
  var replyText by remember { mutableStateOf("") }

  if (selectedConversation != null) {
    val conv = selectedConversation!!
    val threadMessages = remember(messagesFlow, conv.userId) {
      supportChatRepository.getMessagesForUser(conv.userId, conv.userName, conv.userEmail)
    }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(DeepBlack)
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      // Header with back button
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(SurfaceDark)
          .border(1.dp, CardBorderGold, RoundedCornerShape(12.dp))
          .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color(0xFF2E2713))
              .clickable { selectedConversation = null },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
              tint = GoldPrimary,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "REPLYING TO USER CHAT",
              color = GoldPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold
            )
            Text(
              text = conv.userName,
              color = TextWhite,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
        Text(
          text = conv.userEmail,
          color = TextGoldSecondary,
          fontSize = 12.sp
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(threadMessages, key = { it.id }) { msg ->
          ChatMessageItem(msg = msg)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = replyText,
          onValueChange = { replyText = it },
          placeholder = {
            Text(
              text = "Send manager reply to ${conv.userName}...",
              color = TextMuted,
              fontSize = 13.5.sp
            )
          },
          modifier = Modifier.weight(1f),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite,
            focusedContainerColor = SurfaceDark,
            unfocusedContainerColor = SurfaceDark,
            focusedBorderColor = GoldPrimary,
            unfocusedBorderColor = CardBorderGold
          ),
          shape = RoundedCornerShape(14.dp)
        )

        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(if (replyText.isNotBlank()) GoldPrimary else Color(0xFF2E2713))
            .clickable(enabled = replyText.isNotBlank()) {
              supportChatRepository.sendManagerReply(
                targetUserId = conv.userId,
                targetUserName = conv.userName,
                targetUserEmail = conv.userEmail,
                text = replyText
              )
              replyText = ""
              Toast.makeText(context, "Reply sent to user!", Toast.LENGTH_SHORT).show()
            },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send",
            tint = if (replyText.isNotBlank()) DeepBlack else TextMuted,
            modifier = Modifier.size(24.dp)
          )
        }
      }
    }
    return
  }

  // List of all Conversations
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(DeepBlack)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(4.dp))
    }

    item {
      LuxuryCard(
        backgroundColor = SurfaceDark,
        borderColor = GoldPrimary,
        borderWidth = 1.5.dp,
        contentPadding = 18.dp
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "MANAGER EXCLUSIVE SECTION",
              color = GoldPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "USER SUPPORT CHATS (${conversations.size})",
              color = TextWhite,
              fontSize = 20.sp,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "All live messages and queries received from CA users. Tap any conversation to reply.",
              color = TextGoldSecondary,
              fontSize = 12.5.sp,
              lineHeight = 16.sp
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Box(
            modifier = Modifier
              .size(52.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0xFF2E2713))
              .border(1.dp, GoldPrimary, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Chat,
              contentDescription = null,
              tint = GoldAmber,
              modifier = Modifier.size(30.dp)
            )
          }
        }
      }
    }

    item {
      SectionHeader(title = "USER CONVERSATIONS (${conversations.size})")
    }

    if (conversations.isEmpty()) {
      item {
        LuxuryCard(
          backgroundColor = SurfaceDark,
          borderColor = CardBorderGold,
          contentPadding = 24.dp
        ) {
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "No user chats received yet.",
              color = TextWhite,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    } else {
      items(conversations, key = { it.userId }) { conv ->
        val timeStr = remember(conv.lastMessageTimestamp) {
          val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
          sdf.format(Date(conv.lastMessageTimestamp))
        }

        LuxuryCard(
          backgroundColor = SurfaceDark,
          borderColor = if (conv.hasUnreadFromUser) GoldPrimary else CardBorderGold,
          contentPadding = 14.dp,
          onClick = { selectedConversation = conv }
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              modifier = Modifier.weight(1f),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(if (conv.hasUnreadFromUser) GoldPrimary else Color(0xFF2E2713)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = null,
                  tint = if (conv.hasUnreadFromUser) DeepBlack else GoldLight,
                  modifier = Modifier.size(24.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = conv.userName,
                    color = TextWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                  )
                  Text(
                    text = timeStr,
                    color = TextMuted,
                    fontSize = 11.sp
                  )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = conv.userEmail,
                  color = TextGoldSecondary,
                  fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = conv.lastMessageText,
                  color = if (conv.hasUnreadFromUser) TextWhite else TextMuted,
                  fontSize = 13.sp,
                  fontWeight = if (conv.hasUnreadFromUser) FontWeight.Bold else FontWeight.Normal,
                  maxLines = 1
                )
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}
