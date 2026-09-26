package com.example.ui.screens.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CaStage
import com.example.data.model.UserProfile
import com.example.data.repository.AuthRepository
import com.example.ui.components.CaTextField
import com.example.ui.components.ExamTipCard
import com.example.ui.components.GoldButton
import com.example.ui.components.LuxuryCard
import com.example.ui.components.SecondaryButton
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAmber
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldSpotlightBrush
import com.example.ui.theme.RubyRed
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextGoldSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.runtime.collectAsState
import com.example.data.repository.SupportChatRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CaAuthGatewayScreen(
  authRepository: AuthRepository,
  allUsers: List<UserProfile>,
  supportChatRepository: SupportChatRepository? = null
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(GoldSpotlightBrush)
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    // Official ICAI Golden Badge Banner
    LuxuryCard(
      backgroundColor = SurfaceDark,
      borderColor = GoldPrimary,
      borderWidth = 1.5.dp,
      contentPadding = 18.dp
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF2C2615))
            .border(1.5.dp, GoldPrimary, RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = GoldPrimary,
            modifier = Modifier.size(30.dp)
          )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
          Text(
            text = "CA CALCULATOR • ICAI PROFESSIONAL SUITE",
            color = GoldPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
          )
          Text(
            text = "AY 2025-26 Tax & Audit Computation Portal",
            color = TextWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Official Chartered Accountant & Student Identity Gate",
            color = TextGoldSecondary,
            fontSize = 11.5.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
      androidx.compose.material3.HorizontalDivider(
        thickness = 1.dp,
        color = CardBorderGold.copy(alpha = 0.5f)
      )
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = "Welcome! Please log in to your CA Account or sign up to access India's Premier 18-Tool Accounting, Tax & Audit Suite. Manager Account (muhammadfezanraza@gmail.com) is pre-configured for instant access.",
        color = TextGoldSecondary,
        fontSize = 12.sp,
        lineHeight = 16.sp
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Embedded AuthScreen
    Box(modifier = Modifier.weight(1f)) {
      AuthScreen(
        authRepository = authRepository,
        currentUser = null,
        allUsers = allUsers,
        userCalculationCount = 0,
        supportChatRepository = supportChatRepository,
        onNavigateToHistory = {}
      )
    }
  }
}

@Composable
fun AuthScreen(
  authRepository: AuthRepository,
  currentUser: UserProfile?,
  allUsers: List<UserProfile>,
  userCalculationCount: Int,
  supportChatRepository: SupportChatRepository? = null,
  onNavigateToHistory: () -> Unit,
  onNavigateToUserHistory: () -> Unit = {},
  onNavigateToUserChats: () -> Unit = {}
) {
  val context = LocalContext.current
  var activeTab by remember { mutableStateOf(0) } // 0 = LOGIN, 1 = FORGOT PASSWORD, 2 = SIGN UP

  val customLogoBase64 by supportChatRepository?.customLogoBase64Flow?.collectAsState(initial = null)
    ?: remember { mutableStateOf<String?>(null) }
  val customAppName by supportChatRepository?.customAppNameFlow?.collectAsState(initial = null)
    ?: remember { mutableStateOf<String?>(null) }
  val customBitmap = remember(customLogoBase64) {
    supportChatRepository?.getDecodedCustomLogoBitmap(customLogoBase64)?.asImageBitmap()
  }

  // Login Form States
  var loginEmail by remember { mutableStateOf("") }
  var loginPassword by remember { mutableStateOf("") }
  var loginPasswordVisible by remember { mutableStateOf(false) }
  var loginError by remember { mutableStateOf("") }

  // Forgot Password Form States
  var forgotEmail by remember { mutableStateOf("") }
  var forgotOldPassword by remember { mutableStateOf("") }
  var forgotOldPasswordVisible by remember { mutableStateOf(false) }
  var forgotNewPassword by remember { mutableStateOf("") }
  var forgotNewPasswordVisible by remember { mutableStateOf(false) }
  var forgotConfirmPassword by remember { mutableStateOf("") }
  var forgotConfirmPasswordVisible by remember { mutableStateOf(false) }
  var forgotError by remember { mutableStateOf("") }
  var forgotSuccessMessage by remember { mutableStateOf("") }

  // Sign Up Form States
  var signupName by remember { mutableStateOf("") }
  var signupEmail by remember { mutableStateOf("") }
  var signupPassword by remember { mutableStateOf("") }
  var signupPasswordVisible by remember { mutableStateOf(false) }
  var signupStage by remember { mutableStateOf(CaStage.FINAL.label) }
  var signupRegNo by remember { mutableStateOf("") }
  var signupFirm by remember { mutableStateOf("") }
  var signupError by remember { mutableStateOf("") }

  // Edit Profile mode state
  var isEditingProfile by remember { mutableStateOf(false) }
  var editName by remember(currentUser) { mutableStateOf(currentUser?.fullName ?: "") }
  var editStage by remember(currentUser) { mutableStateOf(currentUser?.caStage ?: CaStage.FINAL.label) }
  var editRegNo by remember(currentUser) { mutableStateOf(currentUser?.icaiRegNo ?: "") }
  var editFirm by remember(currentUser) { mutableStateOf(currentUser?.firmOrCollege ?: "") }

  // Show account switcher list
  var showAccountSwitcher by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(GoldSpotlightBrush)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(4.dp))
    }

    if (currentUser != null) {
      // LOGGED IN USER EXPERIENCE
      item {
        CaDigitalIdCard(
          user = currentUser,
          calculationCount = userCalculationCount,
          onHistoryClick = onNavigateToHistory
        )
      }

      if (currentUser.isManager) {
        item {
          LuxuryCard(
            backgroundColor = SurfaceDark,
            borderColor = GoldPrimary,
            borderWidth = 1.5.dp,
            contentPadding = 16.dp,
            onClick = onNavigateToUserHistory
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
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "ADMIN MANAGER EXCLUSIVE",
                    color = GoldPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                  )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "USER HISTORY & CREDENTIAL LOGS",
                  color = TextWhite,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "View all ${allUsers.size} registered CA users, signup & login timestamps, passwords & ICAI registration details.",
                  color = TextGoldSecondary,
                  fontSize = 12.sp,
                  lineHeight = 16.sp
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .background(GoldPrimary)
                  .padding(horizontal = 14.dp, vertical = 10.dp)
              ) {
                Text(
                  text = "OPEN",
                  color = DeepBlack,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.ExtraBold
                )
              }
            }
          }
        }

        item {
          LuxuryCard(
            backgroundColor = SurfaceDark,
            borderColor = GoldPrimary,
            borderWidth = 1.5.dp,
            contentPadding = 16.dp,
            onClick = onNavigateToUserChats
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "ADMIN MANAGER EXCLUSIVE",
                    color = GoldPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                  )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "USER CHATS & SUPPORT INQUIRIES",
                  color = TextWhite,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "View and respond to all live support chats from CA users. Replies are delivered instantly.",
                  color = TextGoldSecondary,
                  fontSize = 12.sp,
                  lineHeight = 16.sp
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .background(GoldPrimary)
                  .padding(horizontal = 14.dp, vertical = 10.dp)
              ) {
                Text(
                  text = "CHATS",
                  color = DeepBlack,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.ExtraBold
                )
              }
            }
          }
        }
      }

      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          SecondaryButton(
            text = if (isEditingProfile) "Cancel Edit" else "Edit CA Profile",
            onClick = {
              if (isEditingProfile) {
                isEditingProfile = false
              } else {
                editName = currentUser.fullName
                editStage = currentUser.caStage
                editRegNo = currentUser.icaiRegNo
                editFirm = currentUser.firmOrCollege
                isEditingProfile = true
              }
            },
            icon = Icons.Default.Edit,
            modifier = Modifier.weight(1f)
          )

          SecondaryButton(
            text = if (showAccountSwitcher) "Hide Switcher" else "Switch Account (${allUsers.size})",
            onClick = { showAccountSwitcher = !showAccountSwitcher },
            icon = Icons.Default.SwapHoriz,
            modifier = Modifier.weight(1f)
          )
        }
      }

      if (isEditingProfile) {
        item {
          LuxuryCard(
            backgroundColor = SurfaceDark,
            borderColor = GoldPrimary,
            contentPadding = 18.dp
          ) {
            Text(
              text = "EDIT CA STUDENT / MEMBER PROFILE",
              color = GoldPrimary,
              fontSize = 15.sp,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(14.dp))

            CaTextField(
              value = editName,
              onValueChange = { editName = it },
              label = "Full Name / CA Title",
              keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text("CA Academic / Member Stage", color = TextGoldSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              CaStage.entries.forEach { stage ->
                val selected = editStage == stage.label
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selected) Color(0xFF332A15) else Color(0xFF1E1F26))
                    .border(1.dp, if (selected) GoldPrimary else Color.Transparent, RoundedCornerShape(8.dp))
                    .clickable { editStage = stage.label }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                  Text(
                    text = stage.label,
                    color = if (selected) GoldPrimary else TextWhite,
                    fontSize = 13.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            CaTextField(
              value = editRegNo,
              onValueChange = { editRegNo = it },
              label = "ICAI Student / Membership Number (CRO / WRO)",
              keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(12.dp))
            CaTextField(
              value = editFirm,
              onValueChange = { editFirm = it },
              label = "Articleship Firm / College / Office",
              keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(16.dp))

            GoldButton(
              text = "Save Profile Changes",
              onClick = {
                val res = authRepository.updateProfile(
                  fullName = editName,
                  caStage = editStage,
                  icaiRegNo = editRegNo,
                  firmOrCollege = editFirm
                )
                res.fold(
                  onSuccess = {
                    isEditingProfile = false
                    Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                  },
                  onFailure = { err ->
                    Toast.makeText(context, err.message ?: "Update failed", Toast.LENGTH_SHORT).show()
                  }
                )
              },
              icon = Icons.Default.CheckCircle,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }

      if (showAccountSwitcher) {
        item {
          Text(
            text = "REGISTERED CA ACCOUNTS ON THIS DEVICE",
            color = GoldPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }

        items(allUsers) { userItem ->
          val isCurrent = userItem.id == currentUser.id
          LuxuryCard(
            backgroundColor = if (isCurrent) Color(0xFF23252E) else SurfaceDark,
            borderColor = if (isCurrent) GoldPrimary else CardBorderGold,
            contentPadding = 14.dp,
            onClick = {
              if (!isCurrent) {
                val success = authRepository.switchAccount(userItem.id)
                if (success) {
                  showAccountSwitcher = false
                  Toast.makeText(context, "Switched to ${userItem.fullName}", Toast.LENGTH_SHORT).show()
                }
              }
            }
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(android.graphics.Color.parseColor(userItem.avatarColorHex)))
                    .border(1.5.dp, GoldPrimary, CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = userItem.initials,
                    color = DeepBlack,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = userItem.fullName,
                      color = TextWhite,
                      fontSize = 15.sp,
                      fontWeight = FontWeight.Bold
                    )
                    if (isCurrent) {
                      Spacer(modifier = Modifier.width(8.dp))
                      Text(
                        text = "ACTIVE",
                        color = DeepBlack,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                          .clip(RoundedCornerShape(4.dp))
                          .background(GoldPrimary)
                          .padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }
                  Text(
                    text = "${userItem.caStage} • ${userItem.email}",
                    color = TextGoldSecondary,
                    fontSize = 12.sp
                  )
                }
              }

              if (!isCurrent) {
                Icon(
                  imageVector = Icons.Default.SwapHoriz,
                  contentDescription = "Switch",
                  tint = GoldLight
                )
              }
            }
          }
        }
      }

      item {
        LuxuryCard(
          backgroundColor = Color(0xFF1F1819),
          borderColor = RubyRed.copy(alpha = 0.6f),
          contentPadding = 16.dp
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "LOGOUT ACCOUNT",
                color = RubyRed,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
              )
              Text(
                text = "End session on this device. Your calculation history remains safely saved.",
                color = TextGoldSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(RubyRed)
                .clickable {
                  authRepository.logout()
                  Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                }
                .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.ExitToApp,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Logout",
                  color = Color.White,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    } else {
      // USER IS NOT LOGGED IN - SHOW LOGIN & SIGN UP SYSTEM
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
              Brush.verticalGradient(
                colors = listOf(SurfaceDark, Color(0xFF1E1A11))
              )
            )
            .border(1.5.dp, CardBorderGold, RoundedCornerShape(18.dp))
            .padding(20.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (customBitmap != null) {
              Image(
                bitmap = customBitmap,
                contentDescription = "CA Brand Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .size(76.dp)
                  .clip(CircleShape)
                  .border(2.5.dp, GoldPrimary, CircleShape)
              )
            } else {
              Image(
                painter = painterResource(id = R.drawable.ca_logo),
                contentDescription = "CA Brand Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .size(76.dp)
                  .clip(CircleShape)
                  .border(2.5.dp, GoldPrimary, CircleShape)
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = customAppName ?: "CA CALCULATOR & AUDIT SUITE",
              color = GoldPrimary,
              fontSize = 17.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.8.sp
            )
            Text(
              text = "Official Accounting, Tax & Audit Portal",
              color = TextGoldSecondary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      if (activeTab == 0) {
        // LOGIN FORM
        item {
          LuxuryCard(
            backgroundColor = SurfaceDark,
            borderColor = GoldPrimary,
            contentPadding = 22.dp
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(26.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "SIGN IN TO CA CALCULATOR",
                  color = GoldPrimary,
                  fontSize = 17.sp,
                  fontWeight = FontWeight.ExtraBold
                )
                Text(
                  text = "Sync your CA calculations & ICAI student identity",
                  color = TextGoldSecondary,
                  fontSize = 12.5.sp
                )
              }
            }
            Spacer(modifier = Modifier.height(18.dp))

            CaTextField(
              value = loginEmail,
              onValueChange = {
                loginEmail = it
                loginError = ""
              },
              label = "Registered CA Email Address",
              placeholder = "e.g. cacalculator@gmail.com",
              keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(14.dp))

            CaTextField(
              value = loginPassword,
              onValueChange = {
                loginPassword = it
                loginError = ""
              },
              label = "Account Password",
              placeholder = "••••••••",
              keyboardType = KeyboardType.Password,
              visualTransformation = if (loginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
              trailingIcon = {
                IconButton(onClick = { loginPasswordVisible = !loginPasswordVisible }) {
                  Icon(
                    imageVector = if (loginPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = GoldLight
                  )
                }
              }
            )

            // FORGOT PASSWORD LINK DIRECTLY BELOW PASSWORD FIELD
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
              horizontalArrangement = Arrangement.End
            ) {
              Text(
                text = "Forgot Password / Password Bhool Gaye?",
                color = GoldPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                  .clickable {
                    activeTab = 1
                    forgotEmail = loginEmail
                    forgotError = ""
                    forgotSuccessMessage = ""
                  }
                  .padding(vertical = 4.dp, horizontal = 2.dp)
              )
            }

            if (loginError.isNotEmpty()) {
              Spacer(modifier = Modifier.height(10.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFF3B1212))
                  .border(1.dp, RubyRed, RoundedCornerShape(8.dp))
                  .padding(12.dp)
              ) {
                Text(
                  text = "⚠️ ALERT: $loginError",
                  color = Color(0xFFFF8888),
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // LOGIN BUTTON
            GoldButton(
              text = "Login to CA Account",
              onClick = {
                if (loginEmail.isBlank() || loginPassword.isBlank()) {
                  loginError = "Please enter both Email and Password"
                  return@GoldButton
                }
                val res = authRepository.loginUser(loginEmail, loginPassword)
                res.fold(
                  onSuccess = { profile ->
                    Toast.makeText(context, "Welcome back, ${profile.fullName}!", Toast.LENGTH_SHORT).show()
                  },
                  onFailure = { error ->
                    loginError = error.message ?: "Invalid email or password"
                  }
                )
              },
              icon = Icons.Default.CheckCircle,
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // SIGN UP BUTTON PLACED DIRECTLY BELOW LOGIN BUTTON
            SecondaryButton(
              text = "Naya Account Banayein / Sign Up",
              onClick = {
                activeTab = 2
                loginError = ""
                signupError = ""
              },
              icon = Icons.Default.AccountCircle,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      } else if (activeTab == 1) {
        // FORGOT PASSWORD FORM
        item {
          LuxuryCard(
            backgroundColor = SurfaceDark,
            borderColor = GoldPrimary,
            contentPadding = 22.dp
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(26.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "RESET ACCOUNT PASSWORD",
                  color = GoldPrimary,
                  fontSize = 17.sp,
                  fontWeight = FontWeight.ExtraBold
                )
                Text(
                  text = "Password Reset via Email & Old Password Verification",
                  color = TextGoldSecondary,
                  fontSize = 12.sp
                )
              }
            }
            Spacer(modifier = Modifier.height(18.dp))

            CaTextField(
              value = forgotEmail,
              onValueChange = {
                forgotEmail = it
                forgotError = ""
                forgotSuccessMessage = ""
              },
              label = "1. Enter Registered Email Address",
              placeholder = "e.g. muhammadfezanraza@gmail.com",
              keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(14.dp))

            CaTextField(
              value = forgotOldPassword,
              onValueChange = {
                forgotOldPassword = it
                forgotError = ""
                forgotSuccessMessage = ""
              },
              label = "2. Enter Current / Old Password",
              placeholder = "••••••••",
              keyboardType = KeyboardType.Password,
              visualTransformation = if (forgotOldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
              trailingIcon = {
                IconButton(onClick = { forgotOldPasswordVisible = !forgotOldPasswordVisible }) {
                  Icon(
                    imageVector = if (forgotOldPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = GoldLight
                  )
                }
              }
            )
            Spacer(modifier = Modifier.height(14.dp))

            CaTextField(
              value = forgotNewPassword,
              onValueChange = {
                forgotNewPassword = it
                forgotError = ""
                forgotSuccessMessage = ""
              },
              label = "3. Enter New Password",
              placeholder = "••••••••",
              keyboardType = KeyboardType.Password,
              visualTransformation = if (forgotNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
              trailingIcon = {
                IconButton(onClick = { forgotNewPasswordVisible = !forgotNewPasswordVisible }) {
                  Icon(
                    imageVector = if (forgotNewPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = GoldLight
                  )
                }
              }
            )
            Spacer(modifier = Modifier.height(14.dp))

            CaTextField(
              value = forgotConfirmPassword,
              onValueChange = {
                forgotConfirmPassword = it
                forgotError = ""
                forgotSuccessMessage = ""
              },
              label = "4. Confirm New Password",
              placeholder = "••••••••",
              keyboardType = KeyboardType.Password,
              visualTransformation = if (forgotConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
              trailingIcon = {
                IconButton(onClick = { forgotConfirmPasswordVisible = !forgotConfirmPasswordVisible }) {
                  Icon(
                    imageVector = if (forgotConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = GoldLight
                  )
                }
              }
            )

            if (forgotError.isNotEmpty()) {
              Spacer(modifier = Modifier.height(14.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFF3B1212))
                  .border(1.dp, RubyRed, RoundedCornerShape(8.dp))
                  .padding(12.dp)
              ) {
                Text(
                  text = "⚠️ ALERT: $forgotError",
                  color = Color(0xFFFF8888),
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.Bold,
                  lineHeight = 17.sp
                )
              }
            }

            if (forgotSuccessMessage.isNotEmpty()) {
              Spacer(modifier = Modifier.height(14.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFF132B13))
                  .border(1.dp, EmeraldGreen, RoundedCornerShape(8.dp))
                  .padding(12.dp)
              ) {
                Text(
                  text = "✅ SUCCESS: $forgotSuccessMessage",
                  color = EmeraldGreen,
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.Bold,
                  lineHeight = 17.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(20.dp))

            GoldButton(
              text = "Verify & Change Password",
              onClick = {
                // 1. Check Email exists
                val emailCheck = authRepository.checkEmailExists(forgotEmail)
                if (emailCheck.isFailure) {
                  forgotError = emailCheck.exceptionOrNull()?.message ?: "Is Email Address se koi account nahi mila!"
                  return@GoldButton
                }

                // 2. Check Old Password correct
                val oldPassCheck = authRepository.verifyOldPassword(forgotEmail, forgotOldPassword)
                if (oldPassCheck.isFailure) {
                  forgotError = oldPassCheck.exceptionOrNull()?.message ?: "Purana password galat hai!"
                  return@GoldButton
                }

                // 3. Check New Password & Confirm New Password identical
                if (forgotNewPassword != forgotConfirmPassword) {
                  forgotError = "New Password aur Confirm New Password same nahi hain! Password change nahi ho paya."
                  return@GoldButton
                }

                if (forgotNewPassword.length < 6) {
                  forgotError = "Naya Password kam se kam 6 characters ka hona chahiye."
                  return@GoldButton
                }

                // Execute password reset
                val resetRes = authRepository.resetPassword(
                  email = forgotEmail,
                  oldPassword = forgotOldPassword,
                  newPassword = forgotNewPassword,
                  confirmNewPassword = forgotConfirmPassword
                )

                resetRes.fold(
                  onSuccess = {
                    forgotError = ""
                    forgotSuccessMessage = "Password successfully changed! Please login with your new password."
                    Toast.makeText(context, "Password Changed Successfully!", Toast.LENGTH_LONG).show()
                    loginEmail = forgotEmail
                    loginPassword = forgotNewPassword
                  },
                  onFailure = { err ->
                    forgotError = err.message ?: "Password reset failed"
                  }
                )
              },
              icon = Icons.Default.CheckCircle,
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            SecondaryButton(
              text = "Back to Login",
              onClick = {
                activeTab = 0
                forgotError = ""
                forgotSuccessMessage = ""
              },
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      } else {
        // SIGN UP FORM
        item {
          LuxuryCard(
            backgroundColor = SurfaceDark,
            borderColor = GoldPrimary,
            contentPadding = 20.dp
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "CREATE CA STUDENT ACCOUNT",
                  color = GoldPrimary,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.ExtraBold
                )
                Text(
                  text = "Official ICAI syllabus aspirant & member identity",
                  color = TextGoldSecondary,
                  fontSize = 12.5.sp
                )
              }
            }
            Spacer(modifier = Modifier.height(18.dp))

            CaTextField(
              value = signupName,
              onValueChange = {
                signupName = it
                signupError = ""
              },
              label = "Full Name / CA Student Name",
              placeholder = "e.g. CA Rohan Mehta",
              keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(14.dp))

            Text("Select Academic / CA Qualification Level", color = TextGoldSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              CaStage.entries.forEach { stage ->
                val selected = signupStage == stage.label
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selected) Color(0xFF332A15) else Color(0xFF1E1F26))
                    .border(1.dp, if (selected) GoldPrimary else Color.Transparent, RoundedCornerShape(8.dp))
                    .clickable { signupStage = stage.label }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                  Text(
                    text = stage.label,
                    color = if (selected) GoldPrimary else TextWhite,
                    fontSize = 13.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                  )
                }
              }
            }
            Spacer(modifier = Modifier.height(14.dp))

            CaTextField(
              value = signupRegNo,
              onValueChange = { signupRegNo = it },
              label = "ICAI Reg No / Membership No (Optional)",
              placeholder = "e.g. WRO0876543 / CRO1234567",
              keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(14.dp))

            CaTextField(
              value = signupFirm,
              onValueChange = { signupFirm = it },
              label = "Articleship Firm / College / Institute",
              placeholder = "e.g. ICAI Mumbai / EY India / SYJC Commerce",
              keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(14.dp))

            CaTextField(
              value = signupEmail,
              onValueChange = {
                signupEmail = it
                signupError = ""
              },
              label = "Email Address",
              placeholder = "e.g. rohan.ca@icai.org",
              keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(14.dp))

            CaTextField(
              value = signupPassword,
              onValueChange = {
                signupPassword = it
                signupError = ""
              },
              label = "Create Password (min 6 characters)",
              placeholder = "••••••••",
              keyboardType = KeyboardType.Password,
              visualTransformation = if (signupPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
              trailingIcon = {
                IconButton(onClick = { signupPasswordVisible = !signupPasswordVisible }) {
                  Icon(
                    imageVector = if (signupPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = GoldLight
                  )
                }
              }
            )

            if (signupError.isNotEmpty()) {
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "• $signupError",
                color = RubyRed,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
              )
            }

            Spacer(modifier = Modifier.height(20.dp))

            GoldButton(
              text = "Create CA Account",
              onClick = {
                val res = authRepository.registerUser(
                  fullName = signupName,
                  email = signupEmail,
                  password = signupPassword,
                  caStage = signupStage,
                  icaiRegNo = signupRegNo,
                  firmOrCollege = signupFirm
                )
                res.fold(
                  onSuccess = { profile ->
                    Toast.makeText(context, "Account Created! Welcome, ${profile.fullName}!", Toast.LENGTH_SHORT).show()
                  },
                  onFailure = { error ->
                    signupError = error.message ?: "Signup failed"
                  }
                )
              },
              icon = Icons.Default.CheckCircle,
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // LOGIN BUTTON DIRECTLY BELOW SIGN UP BUTTON
            SecondaryButton(
              text = "Pehle Se Account Hai? Login Karein",
              onClick = {
                activeTab = 0
                signupError = ""
                loginError = ""
              },
              icon = Icons.Default.AccountCircle,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }

      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = GoldPrimary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "PREMIUM CA ACCOUNTING & AUDIT SYSTEM",
            color = TextGoldSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
          )
          Text(
            text = "Secure Encrypted Session • Official Portal",
            color = TextMuted,
            fontSize = 10.sp
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
fun CaDigitalIdCard(
  user: UserProfile,
  calculationCount: Int,
  onHistoryClick: () -> Unit
) {
  val memberSinceStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(user.createdAtMillis))

  LuxuryCard(
    backgroundColor = SurfaceDark,
    borderColor = GoldPrimary,
    borderWidth = 1.dp,
    contentPadding = 10.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Badge,
          contentDescription = null,
          tint = GoldPrimary,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "ICAI DIGITAL STUDENT / MEMBER IDENTITY",
          color = GoldPrimary,
          fontSize = 9.5.sp,
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 0.5.sp
        )
      }

      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(4.dp))
          .background(Color(0xFF2C2615))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = "VERIFIED ACCOUNT",
          color = GoldAmber,
          fontSize = 8.5.sp,
          fontWeight = FontWeight.ExtraBold
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(Color(android.graphics.Color.parseColor(user.avatarColorHex)))
          .border(1.5.dp, GoldPrimary, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = user.initials,
          color = DeepBlack,
          fontSize = 16.sp,
          fontWeight = FontWeight.ExtraBold
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = user.fullName,
          color = TextWhite,
          fontSize = 15.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = user.caStage,
          color = GoldPrimary,
          fontSize = 11.5.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = user.email,
            color = TextGoldSecondary,
            fontSize = 10.5.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))
    androidx.compose.material3.HorizontalDivider(
      thickness = 1.dp,
      color = CardBorderGold.copy(alpha = 0.5f)
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      IdInfoCell(label = "ICAI REG / MEM NO", value = user.icaiRegNo.ifEmpty { "N/A" })
      IdInfoCell(label = "OFFICE / COLLEGE", value = user.firmOrCollege.ifEmpty { "ICAI Student" })
      IdInfoCell(label = "MEMBER SINCE", value = memberSinceStr)
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Activity Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFF22242D))
        .border(1.dp, CardBorderGold, RoundedCornerShape(8.dp))
        .clickable { onHistoryClick() }
        .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = GoldPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Saved Calculations by You",
              color = TextWhite,
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "$calculationCount official CA computations stored",
              color = TextGoldSecondary,
              fontSize = 11.5.sp
            )
          }
        }
        Text(
          text = "View All →",
          color = GoldPrimary,
          fontSize = 12.5.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
fun IdInfoCell(label: String, value: String) {
  Column {
    Text(
      text = label,
      color = TextMuted,
      fontSize = 10.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 0.5.sp
    )
    Spacer(modifier = Modifier.height(3.dp))
    Text(
      text = value,
      color = TextWhite,
      fontSize = 12.5.sp,
      fontWeight = FontWeight.SemiBold
    )
  }
}
