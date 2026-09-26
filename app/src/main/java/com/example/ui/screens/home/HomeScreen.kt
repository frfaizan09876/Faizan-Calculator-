package com.example.ui.screens.home

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CalculationRecord
import com.example.data.model.CalculatorItem
import com.example.data.model.ModuleType
import com.example.data.model.UserProfile
import com.example.data.repository.SupportChatRepository
import com.example.ui.components.ExamTipCard
import com.example.ui.components.FormulaBadge
import com.example.ui.components.IphoneCalculatorCard
import com.example.ui.components.IphoneCalculatorFullScreenModal
import com.example.ui.components.LuxuryCard
import com.example.ui.components.SectionHeader
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
import com.example.util.CalculatorEngine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
  currentUser: UserProfile? = null,
  supportChatRepository: SupportChatRepository? = null,
  onNavigateToCalculator: (ModuleType, String?) -> Unit,
  onNavigateToTools: () -> Unit,
  onNavigateToAccount: () -> Unit = {},
  onNavigateToSupportChat: () -> Unit = {},
  recentHistory: List<CalculationRecord>
) {
  val context = LocalContext.current
  val customLogoBase64 by supportChatRepository?.customLogoBase64Flow?.collectAsState() ?: remember { mutableStateOf<String?>(null) }
  val customAppName by supportChatRepository?.customAppNameFlow?.collectAsState() ?: remember { mutableStateOf<String?>(null) }
  val customBoxTitles by supportChatRepository?.customBoxTitlesFlow?.collectAsState() ?: remember { mutableStateOf<Map<String, String>>(emptyMap()) }

  var showAppNameDialog by remember { mutableStateOf(false) }
  var appNameInput by remember { mutableStateOf("") }

  var showBoxRenameDialog by remember { mutableStateOf(false) }
  var editingBoxKey by remember { mutableStateOf<String?>(null) }
  var editingBoxLabel by remember { mutableStateOf("") }
  var editingBoxInput by remember { mutableStateOf("") }

  val logoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    if (uri != null && supportChatRepository != null) {
      val success = supportChatRepository.saveCustomLogoFromUri(uri)
      if (success) {
        Toast.makeText(context, "Brand Logo Updated!", Toast.LENGTH_SHORT).show()
      } else {
        Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
      }
    }
  }

  var searchQuery by remember { mutableStateOf("") }
  var showIphoneCalc by remember { mutableStateOf(false) }
  val filteredTools = remember(searchQuery) {
    if (searchQuery.isBlank()) {
      emptyList()
    } else {
      CalculatorEngine.allCalculators.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
          it.subtitle.contains(searchQuery, ignoreCase = true) ||
          it.description.contains(searchQuery, ignoreCase = true) ||
          it.formulaBadge.contains(searchQuery, ignoreCase = true)
      }
    }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(GoldSpotlightBrush),
    contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 40.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    // 1. TOP BAR: LOGO (WITH MANAGER GALLERY OPTION) & SUPPORT TEAM BUTTON
    item {
      Spacer(modifier = Modifier.height(2.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(SurfaceDark)
          .border(1.dp, CardBorderGold, RoundedCornerShape(12.dp))
          .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Left side: Custom Logo or Default Brand Emblem
        Row(verticalAlignment = Alignment.CenterVertically) {
          val customBitmap = remember(customLogoBase64) {
            supportChatRepository?.getDecodedCustomLogoBitmap(customLogoBase64)?.asImageBitmap()
          }

          if (customBitmap != null) {
            Image(
              bitmap = customBitmap,
              contentDescription = "Brand Logo",
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .border(1.5.dp, GoldPrimary, CircleShape)
            )
          } else {
            Image(
              painter = painterResource(id = R.drawable.ca_logo),
              contentDescription = "Brand Logo",
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .border(1.5.dp, GoldPrimary, CircleShape)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = customAppName ?: "CA UTILITY INDIA",
              color = TextWhite,
              fontSize = 14.5.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.3.sp
            )
            if (currentUser?.isManager == true) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                // CHANGE LOGO (MANAGER ONLY)
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(GoldPrimary)
                    .clickable { logoPickerLauncher.launch("image/*") }
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.AddPhotoAlternate,
                      contentDescription = null,
                      tint = DeepBlack,
                      modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                      text = "LOGO",
                      color = DeepBlack,
                      fontSize = 8.5.sp,
                      fontWeight = FontWeight.ExtraBold
                    )
                  }
                }

                // CHANGE APP NAME (MANAGER ONLY)
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(GoldLight)
                    .clickable {
                      appNameInput = customAppName ?: "CA UTILITY INDIA"
                      showAppNameDialog = true
                    }
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Edit,
                      contentDescription = null,
                      tint = DeepBlack,
                      modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                      text = "RENAME APP",
                      color = DeepBlack,
                      fontSize = 8.5.sp,
                      fontWeight = FontWeight.ExtraBold
                    )
                  }
                }

                if (customLogoBase64 != null) {
                  Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove Logo",
                    tint = TextMuted,
                    modifier = Modifier
                      .size(14.dp)
                      .clickable {
                        supportChatRepository?.clearCustomLogo()
                        Toast.makeText(context, "Default Logo Restored", Toast.LENGTH_SHORT).show()
                      }
                  )
                }
              }
            } else {
              Text(
                text = "Official ICAI Formulas",
                color = TextGoldSecondary,
                fontSize = 10.5.sp
              )
            }
          }
        }

        // Right side: SUPPORT TEAM BUTTON
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(GoldPrimary)
            .clickable { onNavigateToSupportChat() }
            .padding(horizontal = 8.dp, vertical = 5.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.SupportAgent,
              contentDescription = null,
              tint = DeepBlack,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "SUPPORT TEAM",
              color = DeepBlack,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
        }
      }
    }

    // 2. Premium Hero Card
    item {
      val heroTitle = customBoxTitles["hero_title"] ?: "CA Calculator"
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
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "INDIA'S PREMIER CA UTILITY",
              color = GoldPrimary,
              fontSize = 9.5.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = heroTitle,
                color = TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
              )
              if (currentUser?.isManager == true) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(GoldPrimary)
                    .clickable {
                      editingBoxKey = "hero_title"
                      editingBoxLabel = "Hero Card Title"
                      editingBoxInput = heroTitle
                      showBoxRenameDialog = true
                    }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Rename Hero Card",
                    tint = DeepBlack,
                    modifier = Modifier.size(11.dp)
                  )
                }
              }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "All-in-one Accounting, Finance, Tax & Costing suite with 100% accurate ICAI formulas.",
              color = TextGoldSecondary,
              fontSize = 11.5.sp,
              lineHeight = 15.sp
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFF2E2713)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Calculate,
              contentDescription = null,
              tint = GoldAmber,
              modifier = Modifier.size(22.dp)
            )
          }
        }
      }
    }

    // CA Student & Member Login / ID Badge Banner
    item {
      val idbadgeTitle = customBoxTitles["idbadge_title"] ?: "CA Member / Student Login & Digital ID"
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF1E2028))
          .border(1.dp, CardBorderGold, RoundedCornerShape(12.dp))
          .clickable { onNavigateToAccount() }
          .padding(horizontal = 10.dp, vertical = 6.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2E2713))
                .border(1.dp, GoldPrimary, RoundedCornerShape(8.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = idbadgeTitle,
                  color = TextWhite,
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.ExtraBold
                )
                if (currentUser?.isManager == true) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(4.dp))
                      .background(GoldPrimary)
                      .clickable {
                        editingBoxKey = "idbadge_title"
                        editingBoxLabel = "Digital ID Banner"
                        editingBoxInput = idbadgeTitle
                        showBoxRenameDialog = true
                      }
                      .padding(horizontal = 4.dp, vertical = 2.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Edit,
                      contentDescription = "Rename ID Banner",
                      tint = DeepBlack,
                      modifier = Modifier.size(10.dp)
                    )
                  }
                }
              }
              Text(
                text = "Sync calculations, ICAI stage & profile badge",
                color = GoldLight,
                fontSize = 10.5.sp
              )
            }
          }
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = GoldPrimary,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }

    // Quick Search across all 18 tools
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search 18 CA Calculators (e.g. GST, EMI, IRR, SLM)...", color = TextMuted) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GoldPrimary) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = GoldPrimary,
          unfocusedBorderColor = CardBorderGold,
          focusedTextColor = TextWhite,
          unfocusedTextColor = TextWhite,
          cursorColor = GoldPrimary,
          focusedContainerColor = SurfaceDark,
          unfocusedContainerColor = SurfaceDark
        ),
        shape = RoundedCornerShape(12.dp)
      )
    }

    // Search Results (if searching)
    if (searchQuery.isNotBlank()) {
      item {
        Text(
          text = "Search Results (${filteredTools.size})",
          color = GoldPrimary,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
      }
      if (filteredTools.isEmpty()) {
        item {
          LuxuryCard(contentPadding = 16.dp) {
            Text(
              text = "No matching CA calculator found for \"$searchQuery\".",
              color = TextGoldSecondary,
              fontSize = 14.sp
            )
          }
        }
      } else {
        items(filteredTools) { tool ->
          CalculatorListItem(
            item = tool,
            onClick = { onNavigateToCalculator(tool.moduleType, tool.id) }
          )
        }
      }
    }

    // Module Categories Spotlight
    item {
      SectionHeader(
        title = "Core CA Modules",
        subtitle = "18 comprehensive calculators organized by discipline"
      )
    }

    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // iPhone Calculator Button (Placed right above Accounting Module Box)
        val iphoneTitle = customBoxTitles["iphone_title"] ?: "iPHONE INSTANT CALCULATOR"
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
              Brush.horizontalGradient(
                colors = listOf(Color(0xFFFF9F0A), Color(0xFFFFB340))
              )
            )
            .clickable { showIphoneCalc = true }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(DeepBlack),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Calculate,
                  contentDescription = null,
                  tint = GoldPrimary,
                  modifier = Modifier.size(16.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = iphoneTitle,
                    color = DeepBlack,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.3.sp
                  )
                  if (currentUser?.isManager == true) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(DeepBlack)
                        .clickable {
                          editingBoxKey = "iphone_title"
                          editingBoxLabel = "iPhone Calculator Box"
                          editingBoxInput = iphoneTitle
                          showBoxRenameDialog = true
                        }
                        .padding(horizontal = 3.dp, vertical = 1.dp)
                    ) {
                      Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Rename iPhone Box",
                        tint = GoldPrimary,
                        modifier = Modifier.size(10.dp)
                      )
                    }
                  }
                }
                Text(
                  text = "Tap to open Full Screen Apple iOS Calculator",
                  color = Color(0xFF2B1A00),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(DeepBlack)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "FULL SCREEN",
                color = GoldPrimary,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }
          }
        }

        if (showIphoneCalc) {
          IphoneCalculatorFullScreenModal(
            onDismiss = { showIphoneCalc = false }
          )
        }

        ModuleSpotlightCard(
          title = customBoxTitles["module_acc"] ?: "Accounting Module",
          badge = "7 Calculators",
          subtitle = "SI, Compound Interest, SLM & WDV Depreciation, Ratios, P&L, Discount & GST",
          icon = Icons.Default.AccountBalanceWallet,
          isManager = currentUser?.isManager == true,
          onRenameBox = {
            editingBoxKey = "module_acc"
            editingBoxLabel = "Accounting Module Box"
            editingBoxInput = customBoxTitles["module_acc"] ?: "Accounting Module"
            showBoxRenameDialog = true
          },
          onClick = { onNavigateToCalculator(ModuleType.ACCOUNTING, null) }
        )
        ModuleSpotlightCard(
          title = customBoxTitles["module_fin"] ?: "Finance Module",
          badge = "5 Calculators",
          subtitle = "EMI, Net Present Value (NPV), IRR (Newton-Raphson), Payback Period & BEP",
          icon = Icons.Default.TrendingUp,
          isManager = currentUser?.isManager == true,
          onRenameBox = {
            editingBoxKey = "module_fin"
            editingBoxLabel = "Finance Module Box"
            editingBoxInput = customBoxTitles["module_fin"] ?: "Finance Module"
            showBoxRenameDialog = true
          },
          onClick = { onNavigateToCalculator(ModuleType.FINANCE, null) }
        )
        ModuleSpotlightCard(
          title = customBoxTitles["module_tax"] ?: "Tax Module (India)",
          badge = "3 Calculators",
          subtitle = "AY 2025-26 Old vs New Regime Income Tax, TDS u/s 194C/J/I & GST GSTR-3B",
          icon = Icons.Default.AccountBalance,
          isManager = currentUser?.isManager == true,
          onRenameBox = {
            editingBoxKey = "module_tax"
            editingBoxLabel = "Tax Module Box"
            editingBoxInput = customBoxTitles["module_tax"] ?: "Tax Module (India)"
            showBoxRenameDialog = true
          },
          onClick = { onNavigateToCalculator(ModuleType.TAX, null) }
        )
        ModuleSpotlightCard(
          title = customBoxTitles["module_cost"] ?: "Costing Module",
          badge = "3 Calculators",
          subtitle = "ICAI Cost Sheet, Marginal Costing (P/V Ratio, MoS) & Break-Even Analysis",
          icon = Icons.Default.Receipt,
          isManager = currentUser?.isManager == true,
          onRenameBox = {
            editingBoxKey = "module_cost"
            editingBoxLabel = "Costing Module Box"
            editingBoxInput = customBoxTitles["module_cost"] ?: "Costing Module"
            showBoxRenameDialog = true
          },
          onClick = { onNavigateToCalculator(ModuleType.COSTING, null) }
        )
      }
    }

    // Recent Calculation History
    item {
      SectionHeader(
        title = "Recent Calculations",
        subtitle = "Saved in persistent local storage",
        actionText = if (recentHistory.isNotEmpty()) "View All" else "",
        onActionClick = { onNavigateToTools() }
      )
    }

    if (recentHistory.isEmpty()) {
      item {
        LuxuryCard(backgroundColor = SurfaceDark, contentPadding = 20.dp) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.History,
              contentDescription = null,
              tint = GoldPrimary,
              modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
              Text(
                text = "No Saved Calculations Yet",
                color = TextWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Each calculation is automatically saved so you can export to PDF or share anytime.",
                color = TextGoldSecondary,
                fontSize = 13.sp
              )
            }
          }
        }
      }
    } else {
      items(recentHistory.take(3)) { record ->
        HistoryPreviewCard(record = record, onClick = { onNavigateToTools() })
      }
    }

    item {
      ExamTipCard(
        tipText = "ICAI Standard Suggestion: In Financial Management and Costing questions, always present your working notes and formulas before substituting values to secure step-marking."
      )
    }

    item {
      Spacer(modifier = Modifier.height(20.dp))
    }
  }

  // MANAGER ONLY: DIALOG TO CHANGE APP NAME
  if (showAppNameDialog) {
    AlertDialog(
      onDismissRequest = { showAppNameDialog = false },
      title = {
        Text(
          text = "Change App Name (Manager Only)",
          color = GoldPrimary,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
      },
      text = {
        Column {
          Text(
            text = "Enter custom application title / App ka naya name likhein:",
            color = TextGoldSecondary,
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = appNameInput,
            onValueChange = { appNameInput = it },
            label = { Text("Application Title", color = TextGoldSecondary) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = GoldPrimary,
              unfocusedBorderColor = CardBorderGold,
              focusedTextColor = TextWhite,
              unfocusedTextColor = TextWhite,
              focusedContainerColor = SurfaceDark,
              unfocusedContainerColor = SurfaceDark
            ),
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (appNameInput.isNotBlank()) {
              supportChatRepository?.saveCustomAppName(appNameInput)
              Toast.makeText(context, "App Name Updated Across Portal!", Toast.LENGTH_SHORT).show()
            }
            showAppNameDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DeepBlack)
        ) {
          Text("SAVE / UPDATE", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(
          onClick = {
            supportChatRepository?.clearCustomAppName()
            Toast.makeText(context, "Reset to Default Name", Toast.LENGTH_SHORT).show()
            showAppNameDialog = false
          }
        ) {
          Text("RESET DEFAULT", color = TextMuted, fontSize = 12.sp)
        }
      },
      containerColor = SurfaceDark,
      iconContentColor = GoldPrimary
    )
  }

  // MANAGER ONLY: DIALOG TO RENAME ANY BOX / CARD TITLE
  if (showBoxRenameDialog && editingBoxKey != null) {
    AlertDialog(
      onDismissRequest = { showBoxRenameDialog = false },
      title = {
        Text(
          text = "Rename Box Title (Manager Only)",
          color = GoldPrimary,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
      },
      text = {
        Column {
          Text(
            text = "Enter custom title for $editingBoxLabel:",
            color = TextGoldSecondary,
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = editingBoxInput,
            onValueChange = { editingBoxInput = it },
            label = { Text("Box Title", color = TextGoldSecondary) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = GoldPrimary,
              unfocusedBorderColor = CardBorderGold,
              focusedTextColor = TextWhite,
              unfocusedTextColor = TextWhite,
              focusedContainerColor = SurfaceDark,
              unfocusedContainerColor = SurfaceDark
            ),
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (editingBoxInput.isNotBlank()) {
              supportChatRepository?.saveCustomBoxTitle(editingBoxKey!!, editingBoxInput)
              Toast.makeText(context, "Box Title Updated!", Toast.LENGTH_SHORT).show()
            }
            showBoxRenameDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = DeepBlack)
        ) {
          Text("SAVE / UPDATE", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(
          onClick = {
            supportChatRepository?.clearCustomBoxTitle(editingBoxKey!!)
            Toast.makeText(context, "Reset Box Title to Default", Toast.LENGTH_SHORT).show()
            showBoxRenameDialog = false
          }
        ) {
          Text("RESET DEFAULT", color = TextMuted, fontSize = 12.sp)
        }
      },
      containerColor = SurfaceDark,
      iconContentColor = GoldPrimary
    )
  }
}

@Composable
fun ModuleSpotlightCard(
  title: String,
  badge: String,
  subtitle: String,
  icon: ImageVector,
  isManager: Boolean = false,
  onRenameBox: (() -> Unit)? = null,
  onClick: () -> Unit
) {
  LuxuryCard(
    onClick = onClick,
    backgroundColor = CardBackground,
    borderColor = CardBorderGold,
    contentPadding = 10.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF262113)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = GoldPrimary,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.width(10.dp))
      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = title,
              color = TextWhite,
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Bold
            )
            if (isManager && onRenameBox != null) {
              Spacer(modifier = Modifier.width(4.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(GoldPrimary)
                  .clickable { onRenameBox() }
                  .padding(horizontal = 3.dp, vertical = 1.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Edit,
                  contentDescription = "Rename Box",
                  tint = DeepBlack,
                  modifier = Modifier.size(10.dp)
                )
              }
            }
          }
          Text(
            text = badge,
            color = GoldPrimary,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(Color(0xFF332A15))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = subtitle,
          color = TextGoldSecondary,
          fontSize = 11.sp,
          lineHeight = 14.sp
        )
      }
      Spacer(modifier = Modifier.width(6.dp))
      Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = null,
        tint = GoldPrimary,
        modifier = Modifier.size(18.dp)
      )
    }
  }
}

@Composable
fun CalculatorListItem(
  item: CalculatorItem,
  onClick: () -> Unit
) {
  LuxuryCard(
    onClick = onClick,
    backgroundColor = SurfaceDark,
    borderColor = CardBorderGold,
    contentPadding = 14.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = item.title,
          color = TextWhite,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = item.subtitle,
          color = TextGoldSecondary,
          fontSize = 12.5.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        FormulaBadge(formulaText = item.formulaBadge)
      }
      Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = null,
        tint = GoldPrimary
      )
    }
  }
}

@Composable
fun HistoryPreviewCard(
  record: CalculationRecord,
  onClick: () -> Unit
) {
  val timeStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(record.timestampMillis))
  LuxuryCard(
    onClick = onClick,
    backgroundColor = SurfaceDark,
    borderColor = CardBorderGold,
    contentPadding = 14.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = record.calculatorTitle,
            color = TextWhite,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = record.moduleTitle,
            color = GoldLight,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(Color(0xFF2C2615))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = record.resultSummary,
          color = GoldPrimary,
          fontSize = 13.5.sp,
          fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = timeStr,
          color = TextMuted,
          fontSize = 11.sp
        )
      }
      Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = null,
        tint = TextGoldSecondary
      )
    }
  }
}
