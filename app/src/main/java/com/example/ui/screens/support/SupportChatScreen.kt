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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.model.UserProfile
import com.example.data.repository.SupportChatMessage
import com.example.data.repository.SupportChatRepository
import com.example.ui.components.LuxuryCard
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAmber
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextGoldSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SupportChatScreen(
  currentUser: UserProfile?,
  supportChatRepository: SupportChatRepository,
  onBack: () -> Unit
) {
  val context = LocalContext.current
  val messagesFlow by supportChatRepository.messagesFlow.collectAsState()

  val userId = currentUser?.id ?: "guest_user"
  val userName = currentUser?.fullName ?: "CA Visitor"
  val userEmail = currentUser?.email ?: "guest@ca.utility"

  val messages = remember(messagesFlow, userId) {
    supportChatRepository.getMessagesForUser(userId, userName, userEmail)
  }

  var messageText by remember { mutableStateOf("") }
  val listState = rememberLazyListState()

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DeepBlack)
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    // Top Bar
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
            .clickable { onBack() },
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
            text = "DIRECT MANAGER SUPPORT",
            color = GoldPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
          )
          Text(
            text = "Chat with CA Muhammad Faizan",
            color = TextWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(EmeraldGreen)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "ONLINE",
          color = EmeraldGreen,
          fontSize = 11.sp,
          fontWeight = FontWeight.ExtraBold
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Messages List
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(messages, key = { it.id }) { msg ->
        ChatMessageItem(msg = msg)
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Message Input Field
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      OutlinedTextField(
        value = messageText,
        onValueChange = { messageText = it },
        placeholder = {
          Text(
            text = "Ask anything from Manager sir...",
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
          .background(if (messageText.isNotBlank()) GoldPrimary else Color(0xFF2E2713))
          .clickable(enabled = messageText.isNotBlank()) {
            if (currentUser != null) {
              supportChatRepository.sendUserMessage(currentUser, messageText)
            } else {
              val guestProfile = UserProfile(
                id = userId,
                fullName = userName,
                email = userEmail,
                passwordHash = "",
                caStage = "COMMERCE",
                icaiRegNo = "GUEST",
                firmOrCollege = "CA Student Visitor",
                createdAtMillis = System.currentTimeMillis()
              )
              supportChatRepository.sendUserMessage(guestProfile, messageText)
            }
            messageText = ""
            Toast.makeText(context, "Message sent to Manager", Toast.LENGTH_SHORT).show()
          },
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = "Send",
          tint = if (messageText.isNotBlank()) DeepBlack else TextMuted,
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }
}

@Composable
fun ChatMessageItem(msg: SupportChatMessage) {
  val isManager = msg.senderType == "MANAGER"
  val alignment = if (isManager) Alignment.CenterHorizontally else Alignment.End
  val bg = if (isManager) Color(0xFF231E12) else Color(0xFF1B2A1E)
  val border = if (isManager) GoldPrimary else EmeraldGreen

  val timeStr = remember(msg.timestampMillis) {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    sdf.format(Date(msg.timestampMillis))
  }

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = if (isManager) Alignment.Start else Alignment.End
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth(0.85f)
        .clip(RoundedCornerShape(12.dp))
        .background(bg)
        .border(1.dp, border.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
        .padding(12.dp)
    ) {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = msg.senderName,
            color = if (isManager) GoldPrimary else EmeraldGreen,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.ExtraBold
          )
          Text(
            text = timeStr,
            color = TextMuted,
            fontSize = 10.sp
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = msg.text,
          color = TextWhite,
          fontSize = 14.sp,
          lineHeight = 19.sp
        )
      }
    }
  }
}
