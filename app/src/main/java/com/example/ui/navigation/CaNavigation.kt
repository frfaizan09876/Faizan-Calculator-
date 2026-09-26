package com.example.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ModuleType
import com.example.data.model.UserProfile
import com.example.data.repository.AuthRepository
import com.example.data.repository.HistoryRepository
import com.example.data.repository.SupportChatRepository
import com.example.ui.screens.about.AboutScreen
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.auth.CaAuthGatewayScreen
import com.example.ui.screens.auth.UserHistoryScreen
import com.example.ui.screens.calculator.CalculatorScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.support.SupportChatScreen
import com.example.ui.screens.support.UserChatsAdminScreen
import com.example.ui.screens.tools.ToolsScreen
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.GoldAmber
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldSpotlightBrush
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextGoldSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

enum class BottomTab(val label: String, val icon: ImageVector) {
  HOME("Home", Icons.Default.Home),
  CALCULATOR("Calculator", Icons.Default.Calculate),
  TOOLS("Tools", Icons.Default.Widgets),
  ABOUT("About", Icons.Default.Info),
  ACCOUNT("Account", Icons.Default.AccountCircle),
  USER_HISTORY("User History", Icons.Default.Security),
  USER_CHATS("User Chats", Icons.Default.Chat)
}

@Composable
fun CaAppHost(
  historyRepository: HistoryRepository,
  authRepository: AuthRepository
) {
  val context = LocalContext.current
  val supportChatRepository = remember { SupportChatRepository(context) }

  var activeTab by remember { mutableStateOf(BottomTab.HOME) }
  var showSupportChat by remember { mutableStateOf(false) }
  var selectedModule by remember { mutableStateOf(ModuleType.ACCOUNTING) }
  var selectedCalculatorId by remember { mutableStateOf<String?>(null) }

  val historyList by historyRepository.historyFlow.collectAsState()
  val currentUser by authRepository.currentUserFlow.collectAsState()
  val allUsers by authRepository.usersFlow.collectAsState()

  val userHistoryList = remember(historyList, currentUser) {
    historyRepository.getHistoryForUser(currentUser?.id)
  }

  if (currentUser == null) {
    CaAuthGatewayScreen(
      authRepository = authRepository,
      allUsers = allUsers,
      supportChatRepository = supportChatRepository
    )
    return
  }

  Scaffold(
    topBar = {
      CaTopBar(
        title = "CA Calculator",
        subtitle = when (activeTab) {
          BottomTab.HOME -> if (showSupportChat) "Direct Manager Support Chat" else "India's Premier Accounting & Tax Tool"
          BottomTab.CALCULATOR -> "${selectedModule.title} Module"
          BottomTab.TOOLS -> "Saved History & ICAI Cheat Sheet"
          BottomTab.ABOUT -> "AY 2025-26 & ICAI Syllabus"
          BottomTab.ACCOUNT -> if (currentUser != null) "CA Member ID & Profile" else "CA Login & Account Setup"
          BottomTab.USER_HISTORY -> "Admin Audit Trail & User Credentials"
          BottomTab.USER_CHATS -> "Live Support Messages from Users"
        },
        savedCount = userHistoryList.size,
        currentUser = currentUser,
        onAccountClick = { activeTab = BottomTab.ACCOUNT; showSupportChat = false },
        onHistoryClick = { activeTab = BottomTab.TOOLS; showSupportChat = false }
      )
    },
    bottomBar = {
      CaBottomNavigationBar(
        currentTab = activeTab,
        isManager = currentUser?.isManager == true,
        onTabSelected = {
          activeTab = it
          showSupportChat = false
        }
      )
    },
    containerColor = DeepBlack
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(GoldSpotlightBrush)
        .padding(paddingValues)
    ) {
      if (showSupportChat) {
        SupportChatScreen(
          currentUser = currentUser,
          supportChatRepository = supportChatRepository,
          onBack = { showSupportChat = false }
        )
      } else {
        Crossfade(targetState = activeTab, label = "tabCrossfade") { tab ->
          when (tab) {
            BottomTab.HOME -> {
              HomeScreen(
                currentUser = currentUser,
                supportChatRepository = supportChatRepository,
                onNavigateToCalculator = { module, calcId ->
                  selectedModule = module
                  selectedCalculatorId = calcId
                  activeTab = BottomTab.CALCULATOR
                },
                onNavigateToTools = {
                  activeTab = BottomTab.TOOLS
                },
                onNavigateToAccount = {
                  activeTab = BottomTab.ACCOUNT
                },
                onNavigateToSupportChat = {
                  showSupportChat = true
                },
                recentHistory = userHistoryList
              )
            }
            BottomTab.CALCULATOR -> {
              CalculatorScreen(
                initialModule = selectedModule,
                initialCalculatorId = selectedCalculatorId,
                historyList = userHistoryList,
                onDeleteRecord = { id -> historyRepository.deleteRecord(id) },
                onSaveRecord = { id, title, moduleName, inSum, outSum, rows ->
                  historyRepository.saveCalculation(
                    calculatorId = id,
                    calculatorTitle = title,
                    moduleTitle = moduleName,
                    inputSummary = inSum,
                    resultSummary = outSum,
                    detailedRows = rows,
                    userId = currentUser?.id,
                    userEmail = currentUser?.email
                  )
                }
              )
            }
            BottomTab.TOOLS -> {
              ToolsScreen(
                historyList = userHistoryList,
                onDeleteRecord = { id -> historyRepository.deleteRecord(id) },
                onClearAllHistory = { historyRepository.clearAllHistory() }
              )
            }
            BottomTab.ABOUT -> {
              AboutScreen()
            }
            BottomTab.ACCOUNT -> {
              AuthScreen(
                authRepository = authRepository,
                currentUser = currentUser,
                allUsers = allUsers,
                userCalculationCount = userHistoryList.size,
                supportChatRepository = supportChatRepository,
                onNavigateToHistory = { activeTab = BottomTab.TOOLS },
                onNavigateToUserHistory = { activeTab = BottomTab.USER_HISTORY },
                onNavigateToUserChats = { activeTab = BottomTab.USER_CHATS }
              )
            }
            BottomTab.USER_HISTORY -> {
              if (currentUser?.isManager == true) {
                UserHistoryScreen(
                  authRepository = authRepository,
                  allUsers = allUsers
                )
              } else {
                activeTab = BottomTab.ACCOUNT
              }
            }
            BottomTab.USER_CHATS -> {
              if (currentUser?.isManager == true) {
                UserChatsAdminScreen(
                  supportChatRepository = supportChatRepository
                )
              } else {
                activeTab = BottomTab.ACCOUNT
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun CaTopBar(
  title: String,
  subtitle: String,
  savedCount: Int,
  currentUser: UserProfile?,
  onAccountClick: () -> Unit,
  onHistoryClick: () -> Unit
) {
  Surface(
    color = SurfaceDark,
    shadowElevation = 8.dp,
    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold.copy(alpha = 0.6f)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 10.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2E2713))
            .border(1.dp, GoldPrimary, RoundedCornerShape(8.dp)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "₹",
            color = GoldAmber,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.3.sp
          )
          Text(
            text = subtitle,
            color = TextGoldSecondary,
            fontSize = 10.5.sp
          )
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // User Profile / Login badge (Compact)
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF23252E))
            .border(1.dp, CardBorderGold, RoundedCornerShape(8.dp))
            .clickable { onAccountClick() }
            .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            if (currentUser != null) {
              Box(
                modifier = Modifier
                  .size(16.dp)
                  .clip(CircleShape)
                  .background(GoldPrimary),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = currentUser.initials.take(1),
                  color = DeepBlack,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.ExtraBold
                )
              }
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = currentUser.fullName.split(" ").firstOrNull() ?: "CA",
                color = TextWhite,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold
              )
            } else {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = "CA Login",
                color = GoldLight,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Saved calculations badge ("History" - Compact)
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF23252E))
            .border(1.dp, CardBorderGold, RoundedCornerShape(8.dp))
            .clickable { onHistoryClick() }
            .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "History",
              color = TextGoldSecondary,
              fontSize = 10.5.sp,
              fontWeight = FontWeight.Medium
            )
            if (savedCount > 0) {
              Spacer(modifier = Modifier.width(4.dp))
              Box(
                modifier = Modifier
                  .clip(CircleShape)
                  .background(GoldPrimary)
                  .padding(horizontal = 5.dp, vertical = 1.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "$savedCount",
                  color = DeepBlack,
                  fontSize = 9.5.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun CaBottomNavigationBar(
  currentTab: BottomTab,
  isManager: Boolean = false,
  onTabSelected: (BottomTab) -> Unit
) {
  val tabsToShow = remember(isManager) {
    if (isManager) {
      BottomTab.entries
    } else {
      BottomTab.entries.filter { it != BottomTab.USER_HISTORY && it != BottomTab.USER_CHATS }
    }
  }

  Surface(
    color = SurfaceDark,
    shadowElevation = 12.dp,
    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold.copy(alpha = 0.6f)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .height(56.dp)
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      tabsToShow.forEach { tab ->
        val isSelected = currentTab == tab
        Column(
          modifier = Modifier
            .widthIn(min = 68.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFF2E2713) else Color.Transparent)
            .clickable { onTabSelected(tab) }
            .padding(vertical = 4.dp, horizontal = 8.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(
            imageVector = tab.icon,
            contentDescription = tab.label,
            tint = if (isSelected) GoldPrimary else TextMuted,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = tab.label,
            color = if (isSelected) GoldPrimary else TextMuted,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
          )
        }
      }
    }
  }
}
