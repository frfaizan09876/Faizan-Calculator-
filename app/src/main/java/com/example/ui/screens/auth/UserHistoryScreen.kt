package com.example.ui.screens.auth

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserAuthLog
import com.example.data.model.UserProfile
import com.example.data.repository.AuthRepository
import com.example.ui.components.CaTextField
import com.example.ui.components.LuxuryCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAmber
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RubyRed
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextGoldSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UserHistoryScreen(
  authRepository: AuthRepository,
  allUsers: List<UserProfile>
) {
  val context = LocalContext.current
  val authLogs by authRepository.authLogsFlow.collectAsState()

  var selectedSubTab by remember { mutableStateOf(0) } // 0 = ACTIVITY LOGS, 1 = ALL REGISTERED USERS
  var searchQuery by remember { mutableStateOf("") }

  val filteredLogs = remember(authLogs, searchQuery) {
    if (searchQuery.isBlank()) {
      authLogs
    } else {
      authLogs.filter { log ->
        log.fullName.contains(searchQuery, ignoreCase = true) ||
          log.email.contains(searchQuery, ignoreCase = true) ||
          log.eventType.contains(searchQuery, ignoreCase = true) ||
          log.icaiRegNo.contains(searchQuery, ignoreCase = true) ||
          log.firmOrCollege.contains(searchQuery, ignoreCase = true)
      }
    }
  }

  val filteredUsers = remember(allUsers, searchQuery) {
    if (searchQuery.isBlank()) {
      allUsers
    } else {
      allUsers.filter { user ->
        user.fullName.contains(searchQuery, ignoreCase = true) ||
          user.email.contains(searchQuery, ignoreCase = true) ||
          user.icaiRegNo.contains(searchQuery, ignoreCase = true) ||
          user.firmOrCollege.contains(searchQuery, ignoreCase = true)
      }
    }
  }

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

    // Manager Admin Header Banner
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
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "MANAGER EXCLUSIVE SECTION",
                color = GoldPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "USER HISTORY & CREDENTIALS",
              color = TextWhite,
              fontSize = 20.sp,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Complete real-time audit trail of all CA signups, logins, passwords, ICAI codes & email addresses.",
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
              imageVector = Icons.Default.History,
              contentDescription = null,
              tint = GoldAmber,
              modifier = Modifier.size(30.dp)
            )
          }
        }
      }
    }

    // Stats Overview Row
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Card 1: Total Users
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceDark)
            .border(1.dp, CardBorderGold, RoundedCornerShape(14.dp))
            .padding(14.dp)
        ) {
          Column {
            Text(
              text = "TOTAL CA USERS",
              color = TextMuted,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "${allUsers.size}",
              color = GoldPrimary,
              fontSize = 26.sp,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Registered Accounts",
              color = TextGoldSecondary,
              fontSize = 11.sp
            )
          }
        }

        // Card 2: Total Auth Logs
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceDark)
            .border(1.dp, CardBorderGold, RoundedCornerShape(14.dp))
            .padding(14.dp)
        ) {
          Column {
            Text(
              text = "TOTAL AUTH LOGS",
              color = TextMuted,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "${authLogs.size}",
              color = EmeraldGreen,
              fontSize = 26.sp,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Logins & Signups",
              color = TextGoldSecondary,
              fontSize = 11.sp
            )
          }
        }
      }
    }

    // Sub Tabs & Search
    item {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceDark, RoundedCornerShape(12.dp))
            .border(1.dp, CardBorderGold, RoundedCornerShape(12.dp))
            .padding(4.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("LOGIN & SIGNUP HISTORY", "ALL REGISTERED USERS (${allUsers.size})").forEachIndexed { idx, title ->
            val isSelected = selectedSubTab == idx
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) GoldPrimary else Color.Transparent)
                .clickable { selectedSubTab = idx }
                .padding(vertical = 10.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = title,
                color = if (isSelected) DeepBlack else TextWhite,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }
          }
        }

        CaTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          label = "Search by Email, CA Code, Name or Event",
          placeholder = "e.g. gmail.com, CRO, LOGIN, FCA..."
        )
      }
    }

    if (selectedSubTab == 0) {
      // TAB 0: LOGIN & SIGNUP ACTIVITY LOGS
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          SectionHeader(title = "USER LOGIN & SIGNUP ACTIVITY LOGS")
          if (authLogs.isNotEmpty()) {
            Text(
              text = "CLEAR LOGS",
              color = RubyRed,
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable {
                  authRepository.clearAuthLogs()
                  Toast.makeText(context, "Auth History Cleared", Toast.LENGTH_SHORT).show()
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }

      if (filteredLogs.isEmpty()) {
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
              Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(36.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "No user login or signup activity logs found",
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      } else {
        items(filteredLogs, key = { it.id }) { log ->
          UserAuthLogItemCard(log = log)
        }
      }
    } else {
      // TAB 1: ALL REGISTERED USERS LIST
      item {
        SectionHeader(title = "ALL REGISTERED CA USERS (${filteredUsers.size})")
      }

      if (filteredUsers.isEmpty()) {
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
                text = "No registered users match your search",
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      } else {
        items(filteredUsers, key = { it.id }) { user ->
          RegisteredUserAdminCard(user = user)
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(28.dp))
    }
  }
}

@Composable
fun UserAuthLogItemCard(log: UserAuthLog) {
  val isSignup = log.eventType.equals("SIGNUP", ignoreCase = true)
  val badgeBg = if (isSignup) EmeraldGreen else GoldPrimary
  val badgeText = if (isSignup) "NEW SIGNUP" else "LOGIN EVENT"

  val timeString = remember(log.timestampMillis) {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    sdf.format(Date(log.timestampMillis))
  }

  LuxuryCard(
    backgroundColor = SurfaceDark,
    borderColor = if (isSignup) EmeraldGreen.copy(alpha = 0.5f) else CardBorderGold,
    contentPadding = 14.dp
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Top row: Badge & timestamp
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(badgeBg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = badgeText,
            color = DeepBlack,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }

        Text(
          text = timeString,
          color = TextMuted,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
      }

      // User details
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = null,
          tint = GoldPrimary,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = log.fullName,
          color = TextWhite,
          fontSize = 15.sp,
          fontWeight = FontWeight.ExtraBold
        )
      }

      // Email address and password row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Email: ${log.email}",
          color = TextGoldSecondary,
          fontSize = 12.5.sp,
          modifier = Modifier.weight(1f)
        )

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF2B2516))
            .border(1.dp, CardBorderGold, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null,
              tint = GoldLight,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Pass: ${log.passwordUsed}",
              color = GoldLight,
              fontSize = 11.5.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      // CA Stage & ICAI Code
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(CardBackground)
          .padding(8.dp)
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
          Text(
            text = "CA Code / Reg No: ${log.icaiRegNo}",
            color = TextWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Stage: ${log.caStage} • ${log.firmOrCollege}",
            color = TextMuted,
            fontSize = 11.sp
          )
        }
      }
    }
  }
}

@Composable
fun RegisteredUserAdminCard(user: UserProfile) {
  val createdString = remember(user.createdAtMillis) {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    sdf.format(Date(user.createdAtMillis))
  }

  LuxuryCard(
    backgroundColor = SurfaceDark,
    borderColor = if (user.isManager) GoldPrimary else CardBorderGold,
    contentPadding = 14.dp
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(Color(android.graphics.Color.parseColor(user.avatarColorHex)))
              .border(1.5.dp, GoldPrimary, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = user.initials,
              color = DeepBlack,
              fontSize = 14.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = user.fullName,
                color = TextWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold
              )
              if (user.isManager) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(GoldPrimary)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "ADMIN",
                    color = DeepBlack,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold
                  )
                }
              }
            }
            Text(
              text = user.email,
              color = GoldLight,
              fontSize = 12.5.sp
            )
          }
        }
      }

      // Password and Reg no
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF2B2516))
            .border(1.dp, CardBorderGold, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null,
              tint = GoldLight,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Password: ${user.lastKnownPassword.ifEmpty { "••••••••" }}",
              color = GoldLight,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
        }

        Text(
          text = "Reg: ${user.icaiRegNo}",
          color = TextWhite,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(CardBackground)
          .padding(8.dp)
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
          Text(
            text = "Stage: ${user.caStage}",
            color = TextGoldSecondary,
            fontSize = 11.5.sp
          )
          Text(
            text = "Firm / Office: ${user.firmOrCollege}",
            color = TextMuted,
            fontSize = 11.sp
          )
          Text(
            text = "Joined / Created: $createdString",
            color = TextMuted,
            fontSize = 10.5.sp
          )
        }
      }
    }
  }
}
