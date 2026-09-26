package com.example.ui.screens.calculator

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CalculationRecord
import com.example.data.model.CalculatorItem
import com.example.data.model.DepreciationScheduleRow
import com.example.data.model.IncomeTaxSlabComparison
import com.example.data.model.ModuleType
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
import com.example.ui.theme.GoldAmber
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMuted
import com.example.ui.theme.GoldPrimary
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
fun CalculatorScreen(
  initialModule: ModuleType = ModuleType.ACCOUNTING,
  initialCalculatorId: String? = null,
  historyList: List<CalculationRecord> = emptyList(),
  onDeleteRecord: ((String) -> Unit)? = null,
  onSaveRecord: (String, String, String, String, String, List<ResultRowItem>) -> CalculationRecord
) {
  val context = LocalContext.current
  var selectedModule by remember { mutableStateOf(initialModule) }
  val moduleTools = remember(selectedModule) {
    CalculatorEngine.allCalculators.filter { it.moduleType == selectedModule }
  }

  var selectedTool by remember(selectedModule) {
    val found = initialCalculatorId?.let { id ->
      moduleTools.find { it.id == id }
    }
    mutableStateOf(found ?: moduleTools.first())
  }

  // Update selectedTool if initialCalculatorId changes
  LaunchedEffect(initialModule, initialCalculatorId) {
    selectedModule = initialModule
    initialCalculatorId?.let { id ->
      val match = CalculatorEngine.allCalculators.find { it.id == id }
      if (match != null) {
        selectedTool = match
      }
    }
  }

  // Input states for all tools with realistic default CA demo values
  var pVal by remember(selectedTool.id) { mutableStateOf("100000") }
  var rVal by remember(selectedTool.id) { mutableStateOf("12.0") }
  var tVal by remember(selectedTool.id) { mutableStateOf("3") }
  var compoundFreq by remember(selectedTool.id) { mutableStateOf(1) } // 1=Annual, 2=Semi, 4=Quarter, 12=Month

  var costVal by remember(selectedTool.id) { mutableStateOf("500000") }
  var salvageVal by remember(selectedTool.id) { mutableStateOf("50000") }
  var lifeVal by remember(selectedTool.id) { mutableStateOf("5") }

  var currAssets by remember(selectedTool.id) { mutableStateOf("450000") }
  var currLiab by remember(selectedTool.id) { mutableStateOf("200000") }
  var invVal by remember(selectedTool.id) { mutableStateOf("150000") }
  var debtVal by remember(selectedTool.id) { mutableStateOf("600000") }
  var equityVal by remember(selectedTool.id) { mutableStateOf("400000") }

  var cpVal by remember(selectedTool.id) { mutableStateOf("1200") }
  var spVal by remember(selectedTool.id) { mutableStateOf("1600") }
  var qtyVal by remember(selectedTool.id) { mutableStateOf("250") }

  var origPrice by remember(selectedTool.id) { mutableStateOf("10000") }
  var disc1 by remember(selectedTool.id) { mutableStateOf("10.0") }
  var disc2 by remember(selectedTool.id) { mutableStateOf("5.0") }

  var gstAmount by remember(selectedTool.id) { mutableStateOf("25000") }
  var gstRate by remember(selectedTool.id) { mutableStateOf(18.0) }
  var isGstInclusive by remember(selectedTool.id) { mutableStateOf(false) }
  var isInterState by remember(selectedTool.id) { mutableStateOf(false) }

  var loanAmount by remember(selectedTool.id) { mutableStateOf("1500000") }
  var tenureMonths by remember(selectedTool.id) { mutableStateOf("36") }

  var npvInit by remember(selectedTool.id) { mutableStateOf("1000000") }
  var npvRate by remember(selectedTool.id) { mutableStateOf("12.0") }
  var npvInflowStr by remember(selectedTool.id) { mutableStateOf("300000, 350000, 400000, 450000") }

  var irrInit by remember(selectedTool.id) { mutableStateOf("500000") }
  var irrInflowStr by remember(selectedTool.id) { mutableStateOf("150000, 180000, 220000, 250000") }

  var paybackInit by remember(selectedTool.id) { mutableStateOf("1200000") }
  var paybackAnnual by remember(selectedTool.id) { mutableStateOf("350000") }

  var finInterest by remember(selectedTool.id) { mutableStateOf("200000") }
  var finPrefDiv by remember(selectedTool.id) { mutableStateOf("100000") }
  var finTaxRate by remember(selectedTool.id) { mutableStateOf("30.0") }

  // Tax inputs
  var salaryIncome by remember(selectedTool.id) { mutableStateOf("1250000") }
  var otherIncome by remember(selectedTool.id) { mutableStateOf("85000") }
  var ded80c by remember(selectedTool.id) { mutableStateOf("150000") }
  var ded80d by remember(selectedTool.id) { mutableStateOf("25000") }
  var hraExempt by remember(selectedTool.id) { mutableStateOf("120000") }
  var isSenior by remember(selectedTool.id) { mutableStateOf(false) }

  var tdsSection by remember(selectedTool.id) { mutableStateOf("194J_PROF") }
  var tdsAmount by remember(selectedTool.id) { mutableStateOf("150000") }
  var tdsHasPan by remember(selectedTool.id) { mutableStateOf(true) }

  var gstrOutSales by remember(selectedTool.id) { mutableStateOf("850000") }
  var gstrOutRate by remember(selectedTool.id) { mutableStateOf("18.0") }
  var gstrInPurch by remember(selectedTool.id) { mutableStateOf("420000") }
  var gstrInRate by remember(selectedTool.id) { mutableStateOf("18.0") }

  // Costing inputs
  var costMat by remember(selectedTool.id) { mutableStateOf("450000") }
  var costLab by remember(selectedTool.id) { mutableStateOf("250000") }
  var costExp by remember(selectedTool.id) { mutableStateOf("50000") }
  var costFact by remember(selectedTool.id) { mutableStateOf("120000") }
  var costAdmin by remember(selectedTool.id) { mutableStateOf("80000") }
  var costSell by remember(selectedTool.id) { mutableStateOf("60000") }
  var costProfitMargin by remember(selectedTool.id) { mutableStateOf("20.0") }
  var isProfitOnSales by remember(selectedTool.id) { mutableStateOf(true) }

  var margSales by remember(selectedTool.id) { mutableStateOf("1000000") }
  var margVC by remember(selectedTool.id) { mutableStateOf("600000") }
  var margFC by remember(selectedTool.id) { mutableStateOf("250000") }

  var bepFC by remember(selectedTool.id) { mutableStateOf("300000") }
  var bepSP by remember(selectedTool.id) { mutableStateOf("500") }
  var bepVC by remember(selectedTool.id) { mutableStateOf("300") }
  var bepTargetProfit by remember(selectedTool.id) { mutableStateOf("100000") }

  // Result state
  var calculatedRows by remember(selectedTool.id) { mutableStateOf<List<ResultRowItem>>(emptyList()) }
  var depSchedule by remember(selectedTool.id) { mutableStateOf<List<DepreciationScheduleRow>?>(null) }
  var taxComparison by remember(selectedTool.id) { mutableStateOf<IncomeTaxSlabComparison?>(null) }
  var currentRecord by remember(selectedTool.id) { mutableStateOf<CalculationRecord?>(null) }

  // Perform Calculation function
  fun runCalculation() {
    depSchedule = null
    taxComparison = null

    val rows: List<ResultRowItem>
    var inputSummaryStr = ""
    var resultSummaryStr = ""

    when (selectedTool.id) {
      "simple_interest" -> {
        val p = pVal.toDoubleOrNull() ?: 0.0
        val r = rVal.toDoubleOrNull() ?: 0.0
        val t = tVal.toDoubleOrNull() ?: 0.0
        rows = CalculatorEngine.calculateSimpleInterest(p, r, t)
        inputSummaryStr = "Principal: ${CalculatorEngine.formatRupee(p)} | Rate: $r% | Time: $t Yrs"
        val siRow = rows.find { it.label.contains("Interest") }
        resultSummaryStr = "Interest: ${siRow?.value ?: "₹0.00"}"
      }
      "compound_interest" -> {
        val p = pVal.toDoubleOrNull() ?: 0.0
        val r = rVal.toDoubleOrNull() ?: 0.0
        val t = tVal.toDoubleOrNull() ?: 0.0
        rows = CalculatorEngine.calculateCompoundInterest(p, r, t, compoundFreq)
        inputSummaryStr = "Principal: ${CalculatorEngine.formatRupee(p)} | Rate: $r% | Time: $t Yrs | Freq: $compoundFreq/yr"
        val ciRow = rows.find { it.label.contains("Interest") }
        resultSummaryStr = "Compound Interest: ${ciRow?.value ?: "₹0.00"}"
      }
      "depreciation" -> {
        val c = costVal.toDoubleOrNull() ?: 0.0
        val s = salvageVal.toDoubleOrNull() ?: 0.0
        val n = (lifeVal.toIntOrNull() ?: 1).coerceAtLeast(1)
        val resultPair = CalculatorEngine.calculateDepreciation(c, s, n)
        rows = resultPair.first
        depSchedule = resultPair.second
        inputSummaryStr = "Cost: ${CalculatorEngine.formatRupee(c)} | Salvage: ${CalculatorEngine.formatRupee(s)} | Life: $n Yrs"
        val slmRow = rows.find { it.label.contains("SLM Annual") }
        resultSummaryStr = "SLM Annual Dep: ${slmRow?.value ?: "₹0.00"}"
      }
      "ratio_analysis" -> {
        val ca = currAssets.toDoubleOrNull() ?: 0.0
        val cl = currLiab.toDoubleOrNull() ?: 0.0
        val inv = invVal.toDoubleOrNull() ?: 0.0
        val debt = debtVal.toDoubleOrNull() ?: 0.0
        val eq = equityVal.toDoubleOrNull() ?: 0.0
        rows = CalculatorEngine.calculateRatios(ca, cl, inv, debt, eq)
        inputSummaryStr = "CA: ${CalculatorEngine.formatRupee(ca)} | CL: ${CalculatorEngine.formatRupee(cl)} | Inv: ${CalculatorEngine.formatRupee(inv)}"
        val crRow = rows.find { it.label.contains("Current Ratio") }
        resultSummaryStr = "Current Ratio: ${crRow?.value ?: "0:1"}"
      }
      "profit_loss" -> {
        val cp = cpVal.toDoubleOrNull() ?: 0.0
        val sp = spVal.toDoubleOrNull() ?: 0.0
        val qty = (qtyVal.toIntOrNull() ?: 1).coerceAtLeast(1)
        rows = CalculatorEngine.calculateProfitLoss(cp, sp, qty)
        inputSummaryStr = "CP: ${CalculatorEngine.formatRupee(cp)} | SP: ${CalculatorEngine.formatRupee(sp)} | Units: $qty"
        val pRow = rows.find { it.label.contains("Profit Amount") || it.label.contains("Loss") }
        resultSummaryStr = "${pRow?.label ?: "Result"}: ${pRow?.value ?: "₹0.00"}"
      }
      "discount_calc" -> {
        val orig = origPrice.toDoubleOrNull() ?: 0.0
        val d1 = disc1.toDoubleOrNull() ?: 0.0
        val d2 = disc2.toDoubleOrNull() ?: 0.0
        rows = CalculatorEngine.calculateDiscount(orig, d1, d2)
        inputSummaryStr = "Original: ${CalculatorEngine.formatRupee(orig)} | Disc 1: $d1% | Disc 2: $d2%"
        val finalRow = rows.find { it.label.contains("Payable") }
        resultSummaryStr = "Net Payable: ${finalRow?.value ?: "₹0.00"}"
      }
      "gst_calc" -> {
        val amt = gstAmount.toDoubleOrNull() ?: 0.0
        rows = CalculatorEngine.calculateGst(amt, gstRate, isGstInclusive, isInterState)
        val modeStr = if (isGstInclusive) "Remove GST (Inclusive)" else "Add GST (Exclusive)"
        inputSummaryStr = "Amount: ${CalculatorEngine.formatRupee(amt)} | Slab: $gstRate% | $modeStr"
        val totalRow = rows.find { it.label.contains("Final Invoice") }
        resultSummaryStr = "Final Invoice: ${totalRow?.value ?: "₹0.00"}"
      }
      "emi_calc" -> {
        val p = loanAmount.toDoubleOrNull() ?: 0.0
        val r = rVal.toDoubleOrNull() ?: 0.0
        val months = (tenureMonths.toIntOrNull() ?: 12).coerceAtLeast(1)
        rows = CalculatorEngine.calculateEmi(p, r, months)
        inputSummaryStr = "Loan: ${CalculatorEngine.formatRupee(p)} | Rate: $r% | Tenure: $months Mos"
        val emiRow = rows.find { it.label.contains("EMI") }
        resultSummaryStr = "Monthly EMI: ${emiRow?.value ?: "₹0.00"}"
      }
      "npv_calc" -> {
        val init = npvInit.toDoubleOrNull() ?: 0.0
        val r = (npvRate.toDoubleOrNull() ?: 12.0)
        val inflows = npvInflowStr.split(",").mapNotNull { it.trim().toDoubleOrNull() }
        rows = CalculatorEngine.calculateNpv(init, r, inflows)
        inputSummaryStr = "Initial Outflow: ${CalculatorEngine.formatRupee(init)} | Discount: $r% | ${inflows.size} Inflows"
        val npvRow = rows.find { it.label.contains("Net Present Value") }
        resultSummaryStr = "NPV: ${npvRow?.value ?: "₹0.00"}"
      }
      "irr_calc" -> {
        val init = irrInit.toDoubleOrNull() ?: 0.0
        val inflows = irrInflowStr.split(",").mapNotNull { it.trim().toDoubleOrNull() }
        rows = CalculatorEngine.calculateIrr(init, inflows)
        inputSummaryStr = "Initial Outflow: ${CalculatorEngine.formatRupee(init)} | ${inflows.size} Cash Inflow Periods"
        val irrRow = rows.find { it.label.contains("Internal Rate of Return") }
        resultSummaryStr = "IRR: ${irrRow?.value ?: "0.00%"}"
      }
      "payback_period" -> {
        val init = paybackInit.toDoubleOrNull() ?: 0.0
        val annual = paybackAnnual.toDoubleOrNull() ?: 0.0
        rows = CalculatorEngine.calculatePaybackPeriod(init, annual)
        inputSummaryStr = "Investment: ${CalculatorEngine.formatRupee(init)} | Annual Inflow: ${CalculatorEngine.formatRupee(annual)}"
        val pbRow = rows.find { it.label.contains("Exact Payback") }
        resultSummaryStr = "Payback: ${pbRow?.value ?: "0.00 Yrs"}"
      }
      "fin_bep" -> {
        val interest = finInterest.toDoubleOrNull() ?: 0.0
        val pref = finPrefDiv.toDoubleOrNull() ?: 0.0
        val tax = finTaxRate.toDoubleOrNull() ?: 0.0
        rows = CalculatorEngine.calculateFinancialBep(interest, pref, tax)
        inputSummaryStr = "Interest: ${CalculatorEngine.formatRupee(interest)} | Pref Div: ${CalculatorEngine.formatRupee(pref)} | Tax: $tax%"
        val bepRow = rows.find { it.label.contains("Break-Even EBIT") }
        resultSummaryStr = "Financial BEP EBIT: ${bepRow?.value ?: "₹0.00"}"
      }
      "income_tax" -> {
        val gross = salaryIncome.toDoubleOrNull() ?: 0.0
        val other = otherIncome.toDoubleOrNull() ?: 0.0
        val c80 = ded80c.toDoubleOrNull() ?: 0.0
        val d80 = ded80d.toDoubleOrNull() ?: 0.0
        val hra = hraExempt.toDoubleOrNull() ?: 0.0
        val comp = CalculatorEngine.calculateIncomeTaxComparison(gross, other, c80, d80, hra, isSenior)
        taxComparison = comp
        rows = listOf(
          ResultRowItem("Recommended Regime", comp.recommendedRegime, true, isPositive = true),
          ResultRowItem("Old Regime Total Tax", CalculatorEngine.formatRupee(comp.oldRegimeTotalTax)),
          ResultRowItem("New Regime Total Tax (u/s 115BAC)", CalculatorEngine.formatRupee(comp.newRegimeTotalTax)),
          ResultRowItem("Net Tax Savings via ${comp.recommendedRegime}", CalculatorEngine.formatRupee(comp.taxSavings), true, isPositive = true)
        )
        inputSummaryStr = "Gross: ${CalculatorEngine.formatRupee(gross)} | Other: ${CalculatorEngine.formatRupee(other)} | 80C: ${CalculatorEngine.formatRupee(c80)}"
        resultSummaryStr = "${comp.recommendedRegime} (Save ${CalculatorEngine.formatRupee(comp.taxSavings)})"
      }
      "tds_calc" -> {
        val amt = tdsAmount.toDoubleOrNull() ?: 0.0
        rows = CalculatorEngine.calculateTds(tdsSection, amt, tdsHasPan)
        inputSummaryStr = "Payment: ${CalculatorEngine.formatRupee(amt)} | Sec: $tdsSection | PAN: ${if (tdsHasPan) "Yes" else "No"}"
        val netRow = rows.find { it.label.contains("Net Payment") }
        resultSummaryStr = "Net Payable: ${netRow?.value ?: "₹0.00"}"
      }
      "gst_return" -> {
        val outSales = gstrOutSales.toDoubleOrNull() ?: 0.0
        val outRate = gstrOutRate.toDoubleOrNull() ?: 0.0
        val inPurch = gstrInPurch.toDoubleOrNull() ?: 0.0
        val inRate = gstrInRate.toDoubleOrNull() ?: 0.0
        rows = CalculatorEngine.calculateGstr3b(outSales, outRate, inPurch, inRate)
        inputSummaryStr = "Outward Sales: ${CalculatorEngine.formatRupee(outSales)} | Inward Purchases: ${CalculatorEngine.formatRupee(inPurch)}"
        val netRow = rows.find { it.label.contains("Net GST Payable") || it.label.contains("Carried Forward") }
        resultSummaryStr = "${netRow?.label ?: "Net GST"}: ${netRow?.value ?: "₹0.00"}"
      }
      "cost_sheet" -> {
        val mat = costMat.toDoubleOrNull() ?: 0.0
        val lab = costLab.toDoubleOrNull() ?: 0.0
        val exp = costExp.toDoubleOrNull() ?: 0.0
        val fact = costFact.toDoubleOrNull() ?: 0.0
        val adm = costAdmin.toDoubleOrNull() ?: 0.0
        val sell = costSell.toDoubleOrNull() ?: 0.0
        val margin = costProfitMargin.toDoubleOrNull() ?: 0.0
        rows = CalculatorEngine.calculateCostSheet(mat, lab, exp, fact, adm, sell, margin, isProfitOnSales)
        inputSummaryStr = "Material: ${CalculatorEngine.formatRupee(mat)} | Labour: ${CalculatorEngine.formatRupee(lab)} | Overheads: ${CalculatorEngine.formatRupee(fact + adm + sell)}"
        val spRow = rows.find { it.label.contains("FINAL SELLING PRICE") }
        resultSummaryStr = "Selling Price: ${spRow?.value ?: "₹0.00"}"
      }
      "marginal_costing" -> {
        val s = margSales.toDoubleOrNull() ?: 0.0
        val vc = margVC.toDoubleOrNull() ?: 0.0
        val fc = margFC.toDoubleOrNull() ?: 0.0
        rows = CalculatorEngine.calculateMarginalCosting(s, vc, fc)
        inputSummaryStr = "Sales: ${CalculatorEngine.formatRupee(s)} | VC: ${CalculatorEngine.formatRupee(vc)} | FC: ${CalculatorEngine.formatRupee(fc)}"
        val pvRow = rows.find { it.label.contains("P/V Ratio") }
        resultSummaryStr = "P/V Ratio: ${pvRow?.value ?: "0.00%"}"
      }
      "break_even_analysis" -> {
        val fc = bepFC.toDoubleOrNull() ?: 0.0
        val sp = bepSP.toDoubleOrNull() ?: 0.0
        val vc = bepVC.toDoubleOrNull() ?: 0.0
        val tp = bepTargetProfit.toDoubleOrNull() ?: 0.0
        rows = CalculatorEngine.calculateBreakEvenAnalysis(fc, sp, vc, tp)
        inputSummaryStr = "FC: ${CalculatorEngine.formatRupee(fc)} | SP: ₹$sp/unit | VC: ₹$vc/unit"
        val bepRow = rows.find { it.label.contains("Break-Even Point (Units)") }
        resultSummaryStr = "BEP: ${bepRow?.value ?: "0 Units"}"
      }
      else -> {
        rows = emptyList()
      }
    }

    calculatedRows = rows
    if (rows.isNotEmpty()) {
      val record = onSaveRecord(
        selectedTool.id,
        selectedTool.title,
        selectedModule.title,
        inputSummaryStr,
        resultSummaryStr,
        rows
      )
      currentRecord = record
    }
  }

  // Trigger initial calculation on load
  LaunchedEffect(selectedTool.id) {
    runCalculation()
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DeepBlack)
  ) {
    // 1. Top Category Module Tabs
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(SurfaceDark)
        .padding(horizontal = 8.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      ModuleType.entries.forEach { module ->
        val isSelected = selectedModule == module
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) GoldPrimary else CardBackground)
            .clickable { selectedModule = module }
            .padding(vertical = 5.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = module.title,
            color = if (isSelected) DeepBlack else TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          )
        }
      }
    }

    // 2. Horizontal scrolling sub-tool chips for the active module
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(SurfaceDark)
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 10.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      moduleTools.forEach { tool ->
        val isActive = selectedTool.id == tool.id
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isActive) Color(0xFF332A15) else Color(0xFF23252E))
            .border(
              1.dp,
              if (isActive) GoldPrimary else Color.Transparent,
              RoundedCornerShape(16.dp)
            )
            .clickable { selectedTool = tool }
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = tool.title,
            color = if (isActive) GoldPrimary else TextGoldSecondary,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
          )
        }
      }
    }

    // 3. Scrollable calculator form and results
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 10.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(4.dp))
        // Calculator Header Banner
        LuxuryCard(
          backgroundColor = CardBackground,
          borderColor = CardBorderGold,
          contentPadding = 10.dp
        ) {
          Text(
            text = selectedTool.title,
            color = TextWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = selectedTool.description,
            color = TextGoldSecondary,
            fontSize = 11.5.sp,
            lineHeight = 16.sp
          )
          Spacer(modifier = Modifier.height(6.dp))
          FormulaBadge(formulaText = selectedTool.formulaBadge)
        }
      }

      // 4. DYNAMIC FORM FIELDS BASED ON SELECTED TOOL ID
      item {
        LuxuryCard(
          backgroundColor = SurfaceDark,
          borderColor = CardBorderGold,
          contentPadding = 16.dp
        ) {
          Text(
            text = "ENTER INPUT PARAMETERS",
            color = GoldPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold
          )
          Spacer(modifier = Modifier.height(14.dp))

          when (selectedTool.id) {
            "simple_interest" -> {
              CaTextField(value = pVal, onValueChange = { pVal = it }, label = "Principal Amount (₹)", prefixText = "₹ ")
              Spacer(modifier = Modifier.height(12.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = rVal, onValueChange = { rVal = it }, label = "Rate % p.a.", suffixText = "%", modifier = Modifier.weight(1f))
                CaTextField(value = tVal, onValueChange = { tVal = it }, label = "Time (Years)", suffixText = "Yrs", modifier = Modifier.weight(1f))
              }
            }
            "compound_interest" -> {
              CaTextField(value = pVal, onValueChange = { pVal = it }, label = "Principal Amount (₹)", prefixText = "₹ ")
              Spacer(modifier = Modifier.height(12.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = rVal, onValueChange = { rVal = it }, label = "Annual Rate %", suffixText = "%", modifier = Modifier.weight(1f))
                CaTextField(value = tVal, onValueChange = { tVal = it }, label = "Time (Years)", suffixText = "Yrs", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              Text("Compounding Frequency", color = TextGoldSecondary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Spacer(modifier = Modifier.height(6.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(1 to "Annual", 2 to "Semi", 4 to "Quarter", 12 to "Monthly").forEach { (valFreq, label) ->
                  RadioOptionRow(label = label, selected = compoundFreq == valFreq, onClick = { compoundFreq = valFreq })
                }
              }
            }
            "depreciation" -> {
              CaTextField(value = costVal, onValueChange = { costVal = it }, label = "Original Asset Cost (₹)", prefixText = "₹ ")
              Spacer(modifier = Modifier.height(12.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = salvageVal, onValueChange = { salvageVal = it }, label = "Salvage Value (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = lifeVal, onValueChange = { lifeVal = it }, label = "Useful Life", suffixText = "Yrs", modifier = Modifier.weight(1f))
              }
            }
            "ratio_analysis" -> {
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = currAssets, onValueChange = { currAssets = it }, label = "Current Assets (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = currLiab, onValueChange = { currLiab = it }, label = "Current Liabilities", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = invVal, onValueChange = { invVal = it }, label = "Inventory & Prepaids", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = debtVal, onValueChange = { debtVal = it }, label = "Total Debt", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              CaTextField(value = equityVal, onValueChange = { equityVal = it }, label = "Shareholders' Equity / Net Worth", prefixText = "₹ ")
            }
            "profit_loss" -> {
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = cpVal, onValueChange = { cpVal = it }, label = "Cost Price per Unit", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = spVal, onValueChange = { spVal = it }, label = "Selling Price per Unit", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              CaTextField(value = qtyVal, onValueChange = { qtyVal = it }, label = "Total Quantity / Units Sold", suffixText = "Units")
            }
            "discount_calc" -> {
              CaTextField(value = origPrice, onValueChange = { origPrice = it }, label = "Original List Price (₹)", prefixText = "₹ ")
              Spacer(modifier = Modifier.height(12.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = disc1, onValueChange = { disc1 = it }, label = "First Discount %", suffixText = "%", modifier = Modifier.weight(1f))
                CaTextField(value = disc2, onValueChange = { disc2 = it }, label = "Chain Discount 2 %", suffixText = "%", modifier = Modifier.weight(1f))
              }
            }
            "gst_calc" -> {
              CaTextField(value = gstAmount, onValueChange = { gstAmount = it }, label = "Amount (₹)", prefixText = "₹ ")
              Spacer(modifier = Modifier.height(12.dp))
              Text("GST Slab Rate", color = TextGoldSecondary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Spacer(modifier = Modifier.height(6.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(5.0, 12.0, 18.0, 28.0).forEach { rate ->
                  RadioOptionRow(label = "${rate.toInt()}%", selected = gstRate == rate, onClick = { gstRate = rate })
                }
              }
              Spacer(modifier = Modifier.height(12.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Amount Includes GST (Remove GST)", color = TextWhite, fontSize = 13.5.sp)
                Switch(
                  checked = isGstInclusive,
                  onCheckedChange = { isGstInclusive = it },
                  colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary, checkedTrackColor = GoldMuted)
                )
              }
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Inter-State Supply (IGST vs CGST+SGST)", color = TextWhite, fontSize = 13.5.sp)
                Switch(
                  checked = isInterState,
                  onCheckedChange = { isInterState = it },
                  colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary, checkedTrackColor = GoldMuted)
                )
              }
            }
            "emi_calc" -> {
              CaTextField(value = loanAmount, onValueChange = { loanAmount = it }, label = "Loan Amount (₹)", prefixText = "₹ ")
              Spacer(modifier = Modifier.height(12.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = rVal, onValueChange = { rVal = it }, label = "Interest Rate % p.a.", suffixText = "%", modifier = Modifier.weight(1f))
                CaTextField(value = tenureMonths, onValueChange = { tenureMonths = it }, label = "Tenure (Months)", suffixText = "Mos", modifier = Modifier.weight(1f))
              }
            }
            "npv_calc" -> {
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = npvInit, onValueChange = { npvInit = it }, label = "Initial Outflow (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = npvRate, onValueChange = { npvRate = it }, label = "Discount Rate %", suffixText = "%", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              CaTextField(
                value = npvInflowStr,
                onValueChange = { npvInflowStr = it },
                label = "Annual Net Cash Inflows (comma separated)",
                placeholder = "e.g. 300000, 350000, 400000"
              )
            }
            "irr_calc" -> {
              CaTextField(value = irrInit, onValueChange = { irrInit = it }, label = "Initial Investment Outflow (₹)", prefixText = "₹ ")
              Spacer(modifier = Modifier.height(12.dp))
              CaTextField(
                value = irrInflowStr,
                onValueChange = { irrInflowStr = it },
                label = "Annual Net Cash Inflows (comma separated)",
                placeholder = "e.g. 150000, 180000, 220000"
              )
            }
            "payback_period" -> {
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = paybackInit, onValueChange = { paybackInit = it }, label = "Initial Investment (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = paybackAnnual, onValueChange = { paybackAnnual = it }, label = "Annual Cash Inflow (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
            }
            "fin_bep" -> {
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = finInterest, onValueChange = { finInterest = it }, label = "Fixed Interest (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = finPrefDiv, onValueChange = { finPrefDiv = it }, label = "Preference Div (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              CaTextField(value = finTaxRate, onValueChange = { finTaxRate = it }, label = "Corporate Tax Rate %", suffixText = "%")
            }
            "income_tax" -> {
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = salaryIncome, onValueChange = { salaryIncome = it }, label = "Gross Salary / Income (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = otherIncome, onValueChange = { otherIncome = it }, label = "Other Income (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              Text("Deductions for Old Regime Comparison", color = GoldPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.height(6.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = ded80c, onValueChange = { ded80c = it }, label = "Section 80C (max 1.5L)", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = ded80d, onValueChange = { ded80d = it }, label = "Section 80D (Health)", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = hraExempt, onValueChange = { hraExempt = it }, label = "HRA / LTA / Other Exemptions", prefixText = "₹ ", modifier = Modifier.weight(1f))
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp)
                ) {
                  Text("Senior Citizen?", color = TextWhite, fontSize = 13.sp, modifier = Modifier.weight(1f))
                  Switch(
                    checked = isSenior,
                    onCheckedChange = { isSenior = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary, checkedTrackColor = GoldMuted)
                  )
                }
              }
            }
            "tds_calc" -> {
              CaTextField(value = tdsAmount, onValueChange = { tdsAmount = it }, label = "Gross Payment Amount (₹)", prefixText = "₹ ")
              Spacer(modifier = Modifier.height(12.dp))
              Text("Select TDS Section Nature", color = TextGoldSecondary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Spacer(modifier = Modifier.height(6.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                  "194C_INDIV" to "194C (1%)",
                  "194C_COMP" to "194C (2%)",
                  "194J_PROF" to "194J (10%)",
                  "194I_LAND" to "194I (10%)"
                ).forEach { (code, label) ->
                  RadioOptionRow(label = label, selected = tdsSection == code, onClick = { tdsSection = code })
                }
              }
              Spacer(modifier = Modifier.height(8.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("PAN Furnished by Deductee?", color = TextWhite, fontSize = 13.5.sp)
                Switch(
                  checked = tdsHasPan,
                  onCheckedChange = { tdsHasPan = it },
                  colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary, checkedTrackColor = GoldMuted)
                )
              }
            }
            "gst_return" -> {
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = gstrOutSales, onValueChange = { gstrOutSales = it }, label = "Outward Taxable Sales (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = gstrOutRate, onValueChange = { gstrOutRate = it }, label = "Output GST %", suffixText = "%", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = gstrInPurch, onValueChange = { gstrInPurch = it }, label = "Inward Purchases (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = gstrInRate, onValueChange = { gstrInRate = it }, label = "ITC GST %", suffixText = "%", modifier = Modifier.weight(1f))
              }
            }
            "cost_sheet" -> {
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = costMat, onValueChange = { costMat = it }, label = "Direct Material Consumed", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = costLab, onValueChange = { costLab = it }, label = "Direct Labour Cost", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = costExp, onValueChange = { costExp = it }, label = "Direct Expenses", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = costFact, onValueChange = { costFact = it }, label = "Factory Overheads", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = costAdmin, onValueChange = { costAdmin = it }, label = "Admin Overheads", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = costSell, onValueChange = { costSell = it }, label = "Selling Overheads", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                CaTextField(value = costProfitMargin, onValueChange = { costProfitMargin = it }, label = "Profit Margin %", suffixText = "%", modifier = Modifier.weight(1f))
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                  Text("Margin on Sales?", color = TextWhite, fontSize = 13.sp, modifier = Modifier.weight(1f))
                  Switch(
                    checked = isProfitOnSales,
                    onCheckedChange = { isProfitOnSales = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary, checkedTrackColor = GoldMuted)
                  )
                }
              }
            }
            "marginal_costing" -> {
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = margSales, onValueChange = { margSales = it }, label = "Total Sales Value (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = margVC, onValueChange = { margVC = it }, label = "Variable Cost (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              CaTextField(value = margFC, onValueChange = { margFC = it }, label = "Fixed Cost (₹)", prefixText = "₹ ")
            }
            "break_even_analysis" -> {
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = bepFC, onValueChange = { bepFC = it }, label = "Fixed Cost (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = bepSP, onValueChange = { bepSP = it }, label = "Selling Price / Unit", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
              Spacer(modifier = Modifier.height(12.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CaTextField(value = bepVC, onValueChange = { bepVC = it }, label = "Variable Cost / Unit", prefixText = "₹ ", modifier = Modifier.weight(1f))
                CaTextField(value = bepTargetProfit, onValueChange = { bepTargetProfit = it }, label = "Target Profit Goal (₹)", prefixText = "₹ ", modifier = Modifier.weight(1f))
              }
            }
            else -> {
              Text("Inputs for ${selectedTool.title}", color = TextGoldSecondary)
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          // 5. Calculate & Reset Buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            SecondaryButton(
              text = "Reset",
              onClick = {
                // reset values to realistic default demo values
                runCalculation()
              },
              icon = Icons.Default.Refresh,
              modifier = Modifier.weight(0.4f)
            )
            GoldButton(
              text = "Calculate",
              onClick = { runCalculation() },
              icon = Icons.Default.Calculate,
              modifier = Modifier.weight(0.6f)
            )
          }
        }
      }

      // 6. RESULT SECTION
      if (calculatedRows.isNotEmpty()) {
        item {
          LuxuryCard(
            backgroundColor = CardBackground,
            borderColor = GoldPrimary,
            borderWidth = 1.5.dp,
            contentPadding = 18.dp
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "CALCULATION RESULT",
                color = GoldPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.8.sp
              )
              Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2E2713))
                    .clickable {
                      currentRecord?.let { rec ->
                        ReportExporter.shareTextReport(context, rec)
                      }
                    }
                    .padding(8.dp)
                ) {
                  Icon(imageVector = Icons.Default.Share, contentDescription = "Share Text", tint = GoldLight, modifier = Modifier.size(18.dp))
                }

                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(GoldPrimary)
                    .clickable {
                      currentRecord?.let { rec ->
                        ReportExporter.generateAndSharePdfReport(context, rec)
                      } ?: Toast
                        .makeText(context, "Please calculate first", Toast.LENGTH_SHORT)
                        .show()
                    }
                    .padding(8.dp)
                ) {
                  Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = "PDF Report", tint = DeepBlack, modifier = Modifier.size(18.dp))
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              calculatedRows.forEach { rowItem ->
                ResultRowCard(
                  label = rowItem.label,
                  value = rowItem.value,
                  isHighlighted = rowItem.isHighlighted,
                  isPositive = rowItem.isPositive,
                  footnote = rowItem.footnote
                )
              }
            }
          }
        }

        // 7. WDV Schedule Table (for Depreciation)
        depSchedule?.let { schedule ->
          item {
            DepreciationScheduleTable(schedule = schedule)
          }
        }

        // 8. Income Tax Regime Comparison Table
        taxComparison?.let { comparison ->
          item {
            IncomeTaxComparisonTable(comparison = comparison)
          }
        }
      }

      // 9. Exam Tip Box
      item {
        ExamTipCard(tipText = selectedTool.examTip)
      }

      // 10. HISTORY SPECIFIC TO THIS CALCULATOR
      item {
        val calculatorSpecificHistory = historyList.filter { it.calculatorId == selectedTool.id }
        
        LuxuryCard(
          backgroundColor = CardBackground,
          borderColor = CardBorderGold,
          borderWidth = 1.dp,
          contentPadding = 10.dp
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
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
                  modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "${selectedTool.title.uppercase()} HISTORY (${calculatorSpecificHistory.size})",
                  color = GoldPrimary,
                  fontSize = 11.5.sp,
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
                  text = if (calculatorSpecificHistory.isEmpty()) "NO LOGS YET" else "PAST LOGS",
                  color = GoldAmber,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (calculatorSpecificHistory.isEmpty()) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(SurfaceDark, RoundedCornerShape(10.dp))
                  .padding(16.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "No calculations recorded for ${selectedTool.title} yet.\nWhen you tap 'Calculate', history will automatically appear here.",
                  color = TextGoldSecondary,
                  fontSize = 12.5.sp,
                  textAlign = TextAlign.Center,
                  lineHeight = 17.sp
                )
              }
            } else {
              Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                calculatorSpecificHistory.forEach { rec ->
                  CalculatorHistoryItemCard(
                    record = rec,
                    onDelete = onDeleteRecord?.let { del -> { del(rec.id) } },
                    onShare = {
                      ReportExporter.shareTextReport(context, rec)
                    },
                    onExportPdf = {
                      ReportExporter.generateAndSharePdfReport(context, rec)
                    }
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
}

@Composable
fun CalculatorHistoryItemCard(
  record: CalculationRecord,
  onDelete: (() -> Unit)?,
  onShare: () -> Unit,
  onExportPdf: () -> Unit
) {
  val dateStr = remember(record.timestampMillis) {
    SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(record.timestampMillis))
  }

  LuxuryCard(
    backgroundColor = SurfaceDark,
    borderColor = CardBorderGold,
    borderWidth = 1.dp,
    contentPadding = 14.dp
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = GoldLight,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = record.userEmail ?: "CA User",
            color = GoldLight,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = dateStr,
            color = TextMuted,
            fontSize = 11.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "INPUTS: ${record.inputSummary}",
        color = TextGoldSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "RESULT: ${record.resultSummary}",
        color = GoldPrimary,
        fontSize = 13.5.sp,
        fontWeight = FontWeight.ExtraBold
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF2E2713))
            .clickable { onShare() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = GoldLight, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Share", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(GoldPrimary)
            .clickable { onExportPdf() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = "PDF", tint = DeepBlack, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "PDF", color = DeepBlack, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
          }
        }

        if (onDelete != null) {
          Spacer(modifier = Modifier.width(8.dp))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFF331111))
              .clickable { onDelete() }
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFF6B6B), modifier = Modifier.size(13.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "Delete", color = Color(0xFFFF6B6B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
