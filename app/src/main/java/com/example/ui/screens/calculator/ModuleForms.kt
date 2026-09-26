package com.example.ui.screens.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DepreciationScheduleRow
import com.example.data.model.IncomeTaxSlabComparison
import com.example.data.model.ResultRowItem
import com.example.ui.components.CaTextField
import com.example.ui.components.ExamTipCard
import com.example.ui.components.FormulaBadge
import com.example.ui.components.GoldButton
import com.example.ui.components.LuxuryCard
import com.example.ui.components.ResultRowCard
import com.example.ui.components.SecondaryButton
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
import com.example.util.CalculatorEngine

@Composable
fun DepreciationScheduleTable(schedule: List<DepreciationScheduleRow>) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(SurfaceDark)
      .border(1.dp, CardBorderGold, RoundedCornerShape(12.dp))
      .padding(12.dp)
  ) {
    Text(
      text = "WDV DEPRECIATION SCHEDULE (AS 10)",
      color = GoldPrimary,
      fontSize = 13.sp,
      fontWeight = FontWeight.ExtraBold
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF23252E))
        .padding(vertical = 8.dp, horizontal = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text("Yr", color = TextGoldSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f))
      Text("Opening WDV", color = TextGoldSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
      Text("Depreciation", color = TextGoldSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.4f))
      Text("Closing WDV", color = TextGoldSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
    }
    schedule.forEach { row ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(row.year.toString(), color = TextWhite, fontSize = 12.sp, modifier = Modifier.weight(0.6f))
        Text(CalculatorEngine.formatRupee(row.openingWdv), color = TextWhite, fontSize = 12.sp, modifier = Modifier.weight(1.5f))
        Text("- " + CalculatorEngine.formatRupee(row.depreciationAmount), color = RubyRed, fontSize = 12.sp, modifier = Modifier.weight(1.4f))
        Text(CalculatorEngine.formatRupee(row.closingWdv), color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
      }
    }
  }
}

@Composable
fun IncomeTaxComparisonTable(comparison: IncomeTaxSlabComparison) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(SurfaceDark)
      .border(1.5.dp, GoldPrimary, RoundedCornerShape(14.dp))
      .padding(16.dp)
  ) {
    Text(
      text = "TAX REGIME COMPARISON (AY 2025-26)",
      color = GoldPrimary,
      fontSize = 14.sp,
      fontWeight = FontWeight.ExtraBold
    )
    Spacer(modifier = Modifier.height(12.dp))

    // Recommended regime banner
    Surface(
      color = Color(0xFF1F3A2D),
      shape = RoundedCornerShape(10.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text("RECOMMENDED REGIME:", color = TextGoldSecondary, fontSize = 11.sp)
          Text(comparison.recommendedRegime, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
        }
        Column(horizontalAlignment = Alignment.End) {
          Text("NET TAX SAVING:", color = TextGoldSecondary, fontSize = 11.sp)
          Text(CalculatorEngine.formatRupee(comparison.taxSavings), color = EmeraldGreen, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
        }
      }
    }
    Spacer(modifier = Modifier.height(14.dp))

    // Table headers
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF262113))
        .padding(vertical = 8.dp, horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text("Particulars", color = TextGoldSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.6f))
      Text("Old Regime", color = TextGoldSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
      Text("New Regime", color = TextGoldSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
    }

    val rows = listOf(
      Triple("Gross Income", CalculatorEngine.formatRupee(comparison.grossIncome), CalculatorEngine.formatRupee(comparison.grossIncome)),
      Triple("Total Deductions", "- " + CalculatorEngine.formatRupee(comparison.totalDeductionsOld), "- " + CalculatorEngine.formatRupee(comparison.newRegimeStandardDeduction)),
      Triple("Net Taxable Income", CalculatorEngine.formatRupee(comparison.oldRegimeTaxableIncome), CalculatorEngine.formatRupee(comparison.newRegimeTaxableIncome)),
      Triple("Income Tax (Slab Rate)", CalculatorEngine.formatRupee(comparison.oldRegimeTax), CalculatorEngine.formatRupee(comparison.newRegimeTax)),
      Triple("4% Health & Ed. Cess", "+ " + CalculatorEngine.formatRupee(comparison.oldRegimeCess), "+ " + CalculatorEngine.formatRupee(comparison.newRegimeCess)),
      Triple("TOTAL TAX PAYABLE", CalculatorEngine.formatRupee(comparison.oldRegimeTotalTax), CalculatorEngine.formatRupee(comparison.newRegimeTotalTax))
    )

    rows.forEachIndexed { index, triple ->
      val isTotal = index == rows.lastIndex
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = if (isTotal) 10.dp else 6.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(triple.first, color = if (isTotal) TextWhite else TextGoldSecondary, fontSize = if (isTotal) 13.sp else 12.sp, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal, modifier = Modifier.weight(1.6f))
        Text(triple.second, color = if (isTotal) RubyRed else TextWhite, fontSize = if (isTotal) 13.sp else 12.sp, fontWeight = if (isTotal) FontWeight.ExtraBold else FontWeight.Normal, modifier = Modifier.weight(1.2f))
        Text(triple.third, color = if (isTotal) GoldLight else TextWhite, fontSize = if (isTotal) 13.sp else 12.sp, fontWeight = if (isTotal) FontWeight.ExtraBold else FontWeight.Normal, modifier = Modifier.weight(1.2f))
      }
    }
  }
}

@Composable
fun RadioOptionRow(
  label: String,
  selected: Boolean,
  onClick: () -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick() }
      .padding(horizontal = 4.dp, vertical = 4.dp)
  ) {
    RadioButton(
      selected = selected,
      onClick = onClick,
      colors = RadioButtonDefaults.colors(
        selectedColor = GoldPrimary,
        unselectedColor = TextMuted
      )
    )
    Text(text = label, color = if (selected) TextWhite else TextGoldSecondary, fontSize = 13.sp)
  }
}
