package com.example.ui.screens.tools

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CalculationRecord
import com.example.data.model.CalculatorItem
import com.example.data.model.ModuleType
import com.example.ui.components.ExamTipCard
import com.example.ui.components.FormulaBadge
import com.example.ui.components.GoldButton
import com.example.ui.components.LuxuryCard
import com.example.ui.components.SecondaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.GoldAmber
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RubyRed
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextGoldSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.util.CalculatorEngine
import com.example.util.ReportExporter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ToolsScreen(
  historyList: List<CalculationRecord>,
  onDeleteRecord: (String) -> Unit,
  onClearAllHistory: () -> Unit
) {
  val context = LocalContext.current
  var activeTab by remember { mutableStateOf(0) } // 0=Saved History, 1=Formula Cheat Sheet, 2=GST Splitter

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DeepBlack)
  ) {
    // Top utility tabs
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(SurfaceDark)
        .padding(horizontal = 10.dp, vertical = 5.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      listOf("Saved History (${historyList.size})", "Formula Cheat Sheet", "GST Invoice Splitter").forEachIndexed { index, title ->
        val isSelected = activeTab == index
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) GoldPrimary else CardBackground)
            .clickable { activeTab = index }
            .padding(vertical = 6.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = title,
            color = if (isSelected) DeepBlack else TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 10.5.sp
          )
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 10.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(4.dp))
      }

      when (activeTab) {
        0 -> {
          // SAVED CALCULATIONS MANAGER
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Saved CA Calculations",
                  color = TextWhite,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.ExtraBold
                )
                Text(
                  text = "Persistent storage across sessions",
                  color = TextGoldSecondary,
                  fontSize = 11.sp
                )
              }
              if (historyList.isNotEmpty()) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF331C1F))
                    .border(1.dp, RubyRed, RoundedCornerShape(6.dp))
                    .clickable { onClearAllHistory() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(
                    text = "CLEAR ALL",
                    color = RubyRed,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold
                  )
                }
              }
            }
          }

          if (historyList.isEmpty()) {
            item {
              LuxuryCard(backgroundColor = SurfaceDark, contentPadding = 24.dp) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(36.dp)
                  )
                  Spacer(modifier = Modifier.width(16.dp))
                  Column {
                    Text(
                      text = "No Saved History Found",
                      color = TextWhite,
                      fontSize = 16.sp,
                      fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = "Perform any calculation in the Calculator tab and it will be saved here automatically.",
                      color = TextGoldSecondary,
                      fontSize = 13.sp,
                      lineHeight = 18.sp
                    )
                  }
                }
              }
            }
          } else {
            items(historyList) { record ->
              HistoryRecordItemCard(
                record = record,
                onShareText = { ReportExporter.shareTextReport(context, record) },
                onSharePdf = { ReportExporter.generateAndSharePdfReport(context, record) },
                onDelete = { onDeleteRecord(record.id) }
              )
            }
          }
        }

        1 -> {
          // FORMULA CHEAT SHEET
          item {
            Text(
              text = "ICAI EXAM FORMULA CHEAT SHEET",
              color = GoldPrimary,
              fontSize = 18.sp,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "All 18 mathematical, accounting, financial, tax & costing formulas with ICAI tips",
              color = TextGoldSecondary,
              fontSize = 13.sp
            )
          }

          items(CalculatorEngine.allCalculators) { tool ->
            FormulaReferenceCard(tool = tool)
          }
        }

        2 -> {
          // GST INVOICE SPLITTER
          item {
            GstInvoiceSplitterCard()
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(30.dp))
      }
    }
  }
}

@Composable
fun HistoryRecordItemCard(
  record: CalculationRecord,
  onShareText: () -> Unit,
  onSharePdf: () -> Unit,
  onDelete: () -> Unit
) {
  val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(record.timestampMillis))
  var isExpanded by remember { mutableStateOf(false) }

  LuxuryCard(
    backgroundColor = SurfaceDark,
    borderColor = CardBorderGold,
    contentPadding = 10.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Top
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = record.calculatorTitle,
            color = TextWhite,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = record.moduleTitle,
            color = GoldLight,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(Color(0xFF2C2615))
              .padding(horizontal = 5.dp, vertical = 1.dp)
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = record.resultSummary,
          color = GoldPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = dateStr,
          color = TextMuted,
          fontSize = 9.5.sp
        )
      }

      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF2E2713))
            .clickable { onShareText() }
            .padding(6.dp)
        ) {
          Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = GoldLight, modifier = Modifier.size(15.dp))
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(GoldPrimary)
            .clickable { onSharePdf() }
            .padding(6.dp)
        ) {
          Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = "PDF", tint = DeepBlack, modifier = Modifier.size(15.dp))
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF331C1F))
            .clickable { onDelete() }
            .padding(6.dp)
        ) {
          Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = RubyRed, modifier = Modifier.size(15.dp))
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { isExpanded = !isExpanded }
        .padding(vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (isExpanded) "Hide Full Calculation Breakdown" else "Show Full Calculation Breakdown",
        color = GoldPrimary,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
      )
      Icon(
        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        contentDescription = null,
        tint = GoldPrimary,
        modifier = Modifier.size(16.dp)
      )
    }

    AnimatedVisibility(visible = isExpanded) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF13141A))
          .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text("INPUTS: ${record.inputSummary}", color = TextGoldSecondary, fontSize = 11.5.sp)
        Spacer(modifier = Modifier.height(4.dp))
        record.detailedRows.forEach { row ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(row.label, color = TextWhite, fontSize = 12.sp, modifier = Modifier.weight(1f))
            Text(row.value, color = if (row.isHighlighted) GoldAmber else GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun FormulaReferenceCard(tool: CalculatorItem) {
  var isExpanded by remember { mutableStateOf(false) }
  LuxuryCard(
    backgroundColor = SurfaceDark,
    borderColor = CardBorderGold,
    contentPadding = 14.dp,
    onClick = { isExpanded = !isExpanded }
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = tool.title,
            color = TextWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = tool.moduleType.title,
            color = GoldPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        FormulaBadge(formulaText = tool.formulaBadge)
      }
      Icon(
        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        contentDescription = null,
        tint = GoldPrimary
      )
    }

    AnimatedVisibility(visible = isExpanded) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 10.dp)
      ) {
        Text("CONCEPT & APPLICATION:", color = GoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(tool.description, color = TextWhite, fontSize = 13.sp, lineHeight = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        ExamTipCard(tipText = tool.examTip)
      }
    }
  }
}

@Composable
fun GstInvoiceSplitterCard() {
  var invoiceTotalStr by remember { mutableStateOf("118000") }
  var selectedRate by remember { mutableStateOf(18.0) }

  val total = invoiceTotalStr.toDoubleOrNull() ?: 0.0
  val baseValue = total / (1.0 + (selectedRate / 100.0))
  val gstAmt = total - baseValue
  val cgst = gstAmt / 2.0
  val sgst = gstAmt / 2.0

  LuxuryCard(
    backgroundColor = SurfaceDark,
    borderColor = GoldPrimary,
    contentPadding = 18.dp
  ) {
    Text(
      text = "GST INVOICE TAX SPLITTER UTILITY",
      color = GoldPrimary,
      fontSize = 15.sp,
      fontWeight = FontWeight.ExtraBold
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "Enter any total invoice amount (inclusive of GST) to instantly split taxable base value, CGST & SGST / IGST.",
      color = TextGoldSecondary,
      fontSize = 13.sp
    )
    Spacer(modifier = Modifier.height(14.dp))

    com.example.ui.components.CaTextField(
      value = invoiceTotalStr,
      onValueChange = { invoiceTotalStr = it },
      label = "Total Invoice Value (inclusive of GST)",
      prefixText = "₹ "
    )
    Spacer(modifier = Modifier.height(12.dp))

    Text("Applicable GST Rate", color = TextGoldSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(6.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      listOf(5.0, 12.0, 18.0, 28.0).forEach { rate ->
        com.example.ui.screens.calculator.RadioOptionRow(
          label = "${rate.toInt()}%",
          selected = selectedRate == rate,
          onClick = { selectedRate = rate }
        )
      }
    }
    Spacer(modifier = Modifier.height(16.dp))

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      com.example.ui.components.ResultRowCard(label = "Base Taxable Value", value = CalculatorEngine.formatRupee(baseValue), isHighlighted = false)
      com.example.ui.components.ResultRowCard(label = "CGST (${selectedRate / 2}%)", value = CalculatorEngine.formatRupee(cgst), isHighlighted = false)
      com.example.ui.components.ResultRowCard(label = "SGST / UTGST (${selectedRate / 2}%)", value = CalculatorEngine.formatRupee(sgst), isHighlighted = false)
      com.example.ui.components.ResultRowCard(label = "Total GST Component", value = CalculatorEngine.formatRupee(gstAmt), isHighlighted = true, isPositive = false)
      com.example.ui.components.ResultRowCard(label = "Total Invoice Value", value = CalculatorEngine.formatRupee(total), isHighlighted = true, isPositive = true)
    }
  }
}
