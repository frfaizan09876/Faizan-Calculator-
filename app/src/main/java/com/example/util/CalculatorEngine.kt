package com.example.util

import com.example.data.model.CalculatorItem
import com.example.data.model.DepreciationScheduleRow
import com.example.data.model.IncomeTaxSlabComparison
import com.example.data.model.ModuleType
import com.example.data.model.ResultRowItem
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.pow

object CalculatorEngine {

  fun formatRupee(value: Double): String {
    if (value.isNaN() || value.isInfinite()) return "₹0.00"
    val isNegative = value < 0
    val absValue = abs(value)
    val parts = String.format("%.2f", absValue).split(".")
    val integerPart = parts[0]
    val decimalPart = parts[1]

    val result = StringBuilder()
    val len = integerPart.length
    if (len <= 3) {
      result.append(integerPart)
    } else {
      val lastThree = integerPart.substring(len - 3)
      var remaining = integerPart.substring(0, len - 3)
      while (remaining.length > 2) {
        result.insert(0, "," + remaining.substring(remaining.length - 2))
        remaining = remaining.substring(0, remaining.length - 2)
      }
      if (remaining.isNotEmpty()) {
        result.insert(0, remaining)
      }
      result.append(",").append(lastThree)
    }
    val formatted = "₹$result.$decimalPart"
    return if (isNegative) "-$formatted" else formatted
  }

  fun formatDecimal(value: Double, decimals: Int = 2): String {
    if (value.isNaN() || value.isInfinite()) return "0.00"
    val pattern = if (decimals == 2) "#,##0.00" else "#,##0.0000"
    return DecimalFormat(pattern).format(value)
  }

  val allCalculators = listOf(
    // ----------------- ACCOUNTING MODULE -----------------
    CalculatorItem(
      id = "simple_interest",
      title = "Simple Interest (SI)",
      subtitle = "Basic linear interest calculation",
      formulaBadge = "SI = (P × R × T) / 100",
      moduleType = ModuleType.ACCOUNTING,
      description = "Calculates simple interest earned or payable on a principal sum over a fixed period.",
      examTip = "CA Foundation Accountancy basic formula. Always verify if time T is in years or months."
    ),
    CalculatorItem(
      id = "compound_interest",
      title = "Compound Interest (CI)",
      subtitle = "Compounded growth calculation",
      formulaBadge = "A = P(1 + r/n)ⁿᵗ",
      moduleType = ModuleType.ACCOUNTING,
      description = "Calculates accumulated value with compounding at annual, semi-annual, quarterly, or monthly intervals.",
      examTip = "In CA Inter Financial Management, compounding frequency 'n' changes effective annual rate (EAR)."
    ),
    CalculatorItem(
      id = "depreciation",
      title = "Depreciation (SLM & WDV)",
      subtitle = "Straight Line & Written Down Value",
      formulaBadge = "SLM = (C - S)/N | WDV Rate = (1 - (S/C)^(1/N))",
      moduleType = ModuleType.ACCOUNTING,
      description = "Calculates annual depreciation under AS 10 (Property, Plant & Equipment) with WDV schedule.",
      examTip = "ICAI Accounting Standard 10: WDV method applies higher depreciation charge in earlier years."
    ),
    CalculatorItem(
      id = "ratio_analysis",
      title = "Ratio Analysis",
      subtitle = "Current, Quick & Debt-Equity Ratios",
      formulaBadge = "CR = CA/CL | QR = (CA-Inv)/CL | DER = Debt/Equity",
      moduleType = ModuleType.ACCOUNTING,
      description = "Evaluates liquidity and capital structure solvency of an entity.",
      examTip = "Standard benchmarks: Current Ratio 2:1, Quick Ratio 1:1, Debt-Equity 2:1."
    ),
    CalculatorItem(
      id = "profit_loss",
      title = "Profit & Loss Calculator",
      subtitle = "Gross profit, margin & markup",
      formulaBadge = "Profit % = (P / CP) × 100",
      moduleType = ModuleType.ACCOUNTING,
      description = "Determines profitability, markup on cost, and margin on selling price per unit or total.",
      examTip = "Don't confuse Markup on Cost with Margin on Selling Price! (1/4 on cost = 1/5 on sales)."
    ),
    CalculatorItem(
      id = "discount_calc",
      title = "Discount Calculator",
      subtitle = "Trade discount & cash discount",
      formulaBadge = "Net Price = List Price - Discount",
      moduleType = ModuleType.ACCOUNTING,
      description = "Calculates net price after single or chain (successive) discounts.",
      examTip = "Trade discount is deducted from invoice price before recording in sales register."
    ),
    CalculatorItem(
      id = "gst_calc",
      title = "GST Calculator",
      subtitle = "Add GST or Remove GST (Inclusive/Exclusive)",
      formulaBadge = "GST = Base × Rate% | Base = Total / (1 + Rate%)",
      moduleType = ModuleType.ACCOUNTING,
      description = "Calculates CGST (50%) + SGST (50%) or IGST (100%) for Indian GST slabs (5%, 12%, 18%, 28%).",
      examTip = "For inclusive GST: Tax = Invoice Value × Rate / (100 + Rate)."
    ),

    // ----------------- FINANCE MODULE -----------------
    CalculatorItem(
      id = "emi_calc",
      title = "EMI Calculator",
      subtitle = "Equated Monthly Installment",
      formulaBadge = "EMI = P × i × (1+i)ⁿ / ((1+i)ⁿ - 1)",
      moduleType = ModuleType.FINANCE,
      description = "Calculates monthly EMI, total interest payable, and total cash outflow for term loans.",
      examTip = "Used in CA Final & Inter FM for debt service coverage ratio (DSCR) and project finance."
    ),
    CalculatorItem(
      id = "npv_calc",
      title = "Net Present Value (NPV)",
      subtitle = "Capital Budgeting tool",
      formulaBadge = "NPV = Σ [ CFt / (1+r)ᵗ ] - I₀",
      moduleType = ModuleType.FINANCE,
      description = "Discounts future net cash inflows at cost of capital to determine project acceptability.",
      examTip = "Accept project if NPV > 0. NPV represents net wealth addition to shareholders."
    ),
    CalculatorItem(
      id = "irr_calc",
      title = "Internal Rate of Return (IRR)",
      subtitle = "Discount rate where NPV = 0",
      formulaBadge = "Σ [ CFt / (1+IRR)ᵗ ] - I₀ = 0",
      moduleType = ModuleType.FINANCE,
      description = "Iteratively solves for the exact rate of return where Present Value of inflows equals Initial Outflow.",
      examTip = "If IRR > Cost of Capital (WACC), project should be accepted."
    ),
    CalculatorItem(
      id = "payback_period",
      title = "Payback Period",
      subtitle = "Time to recover initial investment",
      formulaBadge = "Payback = Initial Investment / Annual Cash Flow",
      moduleType = ModuleType.FINANCE,
      description = "Calculates exact period in years & months required to recoup initial cash outlay.",
      examTip = "Payback ignores time value of money unless Discounted Payback method is used."
    ),
    CalculatorItem(
      id = "fin_bep",
      title = "Financial BEP",
      subtitle = "Break-even EBIT for EPS = 0",
      formulaBadge = "Financial BEP = Interest + Pref. Div / (1 - t)",
      moduleType = ModuleType.FINANCE,
      description = "Determines the EBIT level required to cover all fixed financial charges (Interest & Preference dividend).",
      examTip = "At Financial BEP, Earnings Per Share (EPS) is exactly zero."
    ),

    // ----------------- TAX MODULE (INDIA) -----------------
    CalculatorItem(
      id = "income_tax",
      title = "Income Tax Calculator (AY 2025-26)",
      subtitle = "Old Regime vs New Regime Comparison",
      formulaBadge = "Rebate u/s 87A | Std Ded ₹75,000 | 4% Cess",
      moduleType = ModuleType.TAX,
      description = "Comprehensive Indian Income Tax calculator comparing Old and New regimes for FY 2024-25 / AY 2025-26.",
      examTip = "New regime default u/s 115BAC: Nil tax up to ₹7,00,000 taxable income after ₹75,000 std deduction."
    ),
    CalculatorItem(
      id = "tds_calc",
      title = "TDS Calculator",
      subtitle = "Tax Deducted at Source (194C, 194J, 194I, etc.)",
      formulaBadge = "TDS = Amount × Rate% (20% if no PAN)",
      moduleType = ModuleType.TAX,
      description = "Estimates statutory TDS deduction for contractors, professionals, rent, interest, and commission.",
      examTip = "Section 206AA: If deductee fails to furnish PAN, TDS is deducted at 20% or slab rate whichever is higher."
    ),
    CalculatorItem(
      id = "gst_return",
      title = "GST Basic Return (GSTR-3B)",
      subtitle = "Output Tax vs Eligible ITC",
      formulaBadge = "Net GST Payable = Output GST - Input Tax Credit",
      moduleType = ModuleType.TAX,
      description = "Estimates monthly GSTR-3B cash payable liability or excess Input Tax Credit (ITC) carry forward.",
      examTip = "Section 49 of CGST Act: ITC of IGST must be utilized first before CGST/SGST."
    ),

    // ----------------- COSTING MODULE -----------------
    CalculatorItem(
      id = "cost_sheet",
      title = "CA Cost Sheet",
      subtitle = "Prime Cost, Works Cost & COP",
      formulaBadge = "Prime Cost + Factory + Admin + Selling = Total Cost",
      moduleType = ModuleType.COSTING,
      description = "Standard ICAI Cost Sheet format from Direct Material to Selling Price with desired profit margin.",
      examTip = "Admin overheads related to production are added to Factory Cost to arrive at Cost of Production."
    ),
    CalculatorItem(
      id = "marginal_costing",
      title = "Marginal Costing (CVP)",
      subtitle = "P/V Ratio, Contribution & Margin of Safety",
      formulaBadge = "P/V Ratio = (Contribution / Sales) × 100",
      moduleType = ModuleType.COSTING,
      description = "Core Cost-Volume-Profit analysis for short-term decision making, profit planning, and safety margin.",
      examTip = "Margin of Safety (MoS) = Profit / (P/V Ratio). Higher MoS indicates strong operating resilience."
    ),
    CalculatorItem(
      id = "break_even_analysis",
      title = "Break-even Analysis",
      subtitle = "Unit BEP, Revenue BEP & Target Profit Units",
      formulaBadge = "BEP Units = Fixed Cost / Contribution per unit",
      moduleType = ModuleType.COSTING,
      description = "Calculates exact production units and sales volume needed to break even or achieve target profit.",
      examTip = "Contribution per unit remains constant; Profit increases by Contribution per unit after BEP."
    )
  )

  // -----------------------------------------------------------------------------------------------
  // 1. SIMPLE INTEREST
  // -----------------------------------------------------------------------------------------------
  fun calculateSimpleInterest(principal: Double, rate: Double, timeYears: Double): List<ResultRowItem> {
    val si = (principal * rate * timeYears) / 100.0
    val totalAmount = principal + si
    return listOf(
      ResultRowItem("Principal Amount (P)", formatRupee(principal), false),
      ResultRowItem("Rate of Interest (R)", "${formatDecimal(rate, 2)}% p.a.", false),
      ResultRowItem("Time Period (T)", "${formatDecimal(timeYears, 2)} Years", false),
      ResultRowItem("Total Simple Interest Earned", formatRupee(si), true, isPositive = true),
      ResultRowItem("Maturity / Total Amount", formatRupee(totalAmount), true, isPositive = true)
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 2. COMPOUND INTEREST
  // -----------------------------------------------------------------------------------------------
  fun calculateCompoundInterest(
    principal: Double,
    rateAnnual: Double,
    timeYears: Double,
    compoundingFrequency: Int // 1=Annually, 2=Semi, 4=Quarterly, 12=Monthly, 365=Daily
  ): List<ResultRowItem> {
    val r = rateAnnual / 100.0
    val n = compoundingFrequency.toDouble()
    val amount = principal * (1.0 + (r / n)).pow(n * timeYears)
    val ci = amount - principal
    val freqName = when (compoundingFrequency) {
      1 -> "Annually"
      2 -> "Semi-Annually"
      4 -> "Quarterly"
      12 -> "Monthly"
      365 -> "Daily"
      else -> "Custom ($compoundingFrequency times/yr)"
    }
    return listOf(
      ResultRowItem("Principal Amount (P)", formatRupee(principal)),
      ResultRowItem("Annual Rate (r)", "${formatDecimal(rateAnnual, 2)}%"),
      ResultRowItem("Compounding Frequency", freqName),
      ResultRowItem("Time Period (t)", "${formatDecimal(timeYears, 2)} Years"),
      ResultRowItem("Total Compound Interest (CI)", formatRupee(ci), true, isPositive = true),
      ResultRowItem("Final Compound Value (A)", formatRupee(amount), true, isPositive = true)
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 3. DEPRECIATION (SLM & WDV)
  // -----------------------------------------------------------------------------------------------
  fun calculateDepreciation(
    cost: Double,
    salvageValue: Double,
    usefulLifeYears: Int
  ): Pair<List<ResultRowItem>, List<DepreciationScheduleRow>> {
    val slmAnnual = (cost - salvageValue) / usefulLifeYears.toDouble()
    val slmRate = (slmAnnual / cost) * 100.0

    // WDV rate formula: 1 - (S/C)^(1/N)
    val wdvRateDecimal = if (cost > 0 && salvageValue > 0) {
      1.0 - (salvageValue / cost).pow(1.0 / usefulLifeYears.toDouble())
    } else {
      slmAnnual / cost
    }
    val wdvRatePercent = wdvRateDecimal * 100.0

    val schedule = mutableListOf<DepreciationScheduleRow>()
    var openWdv = cost
    for (y in 1..usefulLifeYears) {
      val dep = openWdv * wdvRateDecimal
      val closing = openWdv - dep
      schedule.add(
        DepreciationScheduleRow(
          year = y,
          openingWdv = openWdv,
          depreciationAmount = dep,
          closingWdv = closing
        )
      )
      openWdv = closing
    }

    val rows = listOf(
      ResultRowItem("Original Asset Cost", formatRupee(cost)),
      ResultRowItem("Estimated Salvage Value", formatRupee(salvageValue)),
      ResultRowItem("Useful Life", "$usefulLifeYears Years"),
      ResultRowItem("SLM Annual Depreciation", formatRupee(slmAnnual), true, isPositive = false),
      ResultRowItem("SLM Depreciation Rate", "${formatDecimal(slmRate, 2)}% p.a."),
      ResultRowItem("WDV Depreciation Rate (AS 10)", "${formatDecimal(wdvRatePercent, 2)}% p.a.", true),
      ResultRowItem("Year 1 WDV Depreciation", formatRupee(cost * wdvRateDecimal), false, isPositive = false)
    )
    return Pair(rows, schedule)
  }

  // -----------------------------------------------------------------------------------------------
  // 4. RATIO ANALYSIS
  // -----------------------------------------------------------------------------------------------
  fun calculateRatios(
    currentAssets: Double,
    currentLiabilities: Double,
    inventory: Double,
    totalDebt: Double,
    shareholderEquity: Double
  ): List<ResultRowItem> {
    val currentRatio = if (currentLiabilities > 0) currentAssets / currentLiabilities else 0.0
    val quickAssets = currentAssets - inventory
    val quickRatio = if (currentLiabilities > 0) quickAssets / currentLiabilities else 0.0
    val debtEquityRatio = if (shareholderEquity > 0) totalDebt / shareholderEquity else 0.0

    val crNote = if (currentRatio >= 2.0) "Optimal (Benchmark ≥ 2:1)" else "Low Liquidity Cushion"
    val qrNote = if (quickRatio >= 1.0) "Optimal Liquid Health (≥ 1:1)" else "Check inventory lockup"
    val derNote = if (debtEquityRatio <= 2.0) "Prudent Solvency Level (≤ 2:1)" else "High Financial Leverage"

    return listOf(
      ResultRowItem("Current Ratio (CA / CL)", "${formatDecimal(currentRatio, 2)} : 1", true, isPositive = currentRatio >= 2.0, footnote = crNote),
      ResultRowItem("Quick Ratio ((CA-Inv) / CL)", "${formatDecimal(quickRatio, 2)} : 1", true, isPositive = quickRatio >= 1.0, footnote = qrNote),
      ResultRowItem("Debt-Equity Ratio (Debt / Equity)", "${formatDecimal(debtEquityRatio, 2)} : 1", true, isPositive = debtEquityRatio <= 2.0, footnote = derNote),
      ResultRowItem("Net Working Capital", formatRupee(currentAssets - currentLiabilities), false, isPositive = (currentAssets - currentLiabilities) >= 0)
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 5. PROFIT & LOSS CALCULATOR
  // -----------------------------------------------------------------------------------------------
  fun calculateProfitLoss(costPrice: Double, sellingPrice: Double, quantity: Int): List<ResultRowItem> {
    val unitProfit = sellingPrice - costPrice
    val totalProfit = unitProfit * quantity
    val profitPercent = if (costPrice > 0) (unitProfit / costPrice) * 100.0 else 0.0
    val marginOnSales = if (sellingPrice > 0) (unitProfit / sellingPrice) * 100.0 else 0.0
    val isGain = unitProfit >= 0

    return listOf(
      ResultRowItem("Total Cost Price (CP)", formatRupee(costPrice * quantity)),
      ResultRowItem("Total Selling Revenue (SP)", formatRupee(sellingPrice * quantity)),
      ResultRowItem(if (isGain) "Net Profit Amount" else "Net Loss Amount", formatRupee(abs(totalProfit)), true, isPositive = isGain),
      ResultRowItem(if (isGain) "Profit % on Cost (Markup)" else "Loss % on Cost", "${formatDecimal(abs(profitPercent), 2)}%", true, isPositive = isGain),
      ResultRowItem("Margin % on Selling Price", "${formatDecimal(marginOnSales, 2)}%", false, isPositive = isGain)
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 6. DISCOUNT CALCULATOR
  // -----------------------------------------------------------------------------------------------
  fun calculateDiscount(originalPrice: Double, discountPercent: Double, secondDiscountPercent: Double = 0.0): List<ResultRowItem> {
    val firstDiscAmount = originalPrice * (discountPercent / 100.0)
    val priceAfterFirst = originalPrice - firstDiscAmount
    val secondDiscAmount = priceAfterFirst * (secondDiscountPercent / 100.0)
    val finalPrice = priceAfterFirst - secondDiscAmount
    val totalDiscount = firstDiscAmount + secondDiscAmount
    val effectiveRate = if (originalPrice > 0) (totalDiscount / originalPrice) * 100.0 else 0.0

    return listOf(
      ResultRowItem("Original / List Price", formatRupee(originalPrice)),
      ResultRowItem("First Discount (${formatDecimal(discountPercent, 1)}%)", "- " + formatRupee(firstDiscAmount), false, isPositive = true),
      if (secondDiscountPercent > 0.0) {
        ResultRowItem("Second Chain Discount (${formatDecimal(secondDiscountPercent, 1)}%)", "- " + formatRupee(secondDiscAmount), false, isPositive = true)
      } else {
        ResultRowItem("Chain Discount", "None (Single Discount)")
      },
      ResultRowItem("Total Discount Amount Saved", formatRupee(totalDiscount), true, isPositive = true),
      ResultRowItem("Effective Discount Rate", "${formatDecimal(effectiveRate, 2)}%", false),
      ResultRowItem("Final Payable Net Price", formatRupee(finalPrice), true, isPositive = false)
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 7. GST CALCULATOR (ADD / REMOVE + CGST/SGST vs IGST)
  // -----------------------------------------------------------------------------------------------
  fun calculateGst(
    amount: Double,
    gstRate: Double,
    isInclusive: Boolean,
    isInterState: Boolean // Inter-State = IGST 100%; Intra-State = CGST 50% + SGST 50%
  ): List<ResultRowItem> {
    val baseValue: Double
    val totalGst: Double
    val totalInvoice: Double

    if (isInclusive) {
      // Amount includes GST
      baseValue = amount / (1.0 + (gstRate / 100.0))
      totalGst = amount - baseValue
      totalInvoice = amount
    } else {
      // Amount is exclusive base taxable value
      baseValue = amount
      totalGst = amount * (gstRate / 100.0)
      totalInvoice = baseValue + totalGst
    }

    val rows = mutableListOf<ResultRowItem>()
    rows.add(ResultRowItem("Base Taxable Value", formatRupee(baseValue)))
    rows.add(ResultRowItem("GST Slab Rate", "${formatDecimal(gstRate, 1)}%"))

    if (isInterState) {
      rows.add(ResultRowItem("IGST (${formatDecimal(gstRate, 1)}%)", formatRupee(totalGst), true, isPositive = false, footnote = "Inter-State Supply"))
    } else {
      val cgst = totalGst / 2.0
      val sgst = totalGst / 2.0
      rows.add(ResultRowItem("CGST (${formatDecimal(gstRate / 2.0, 1)}%)", formatRupee(cgst), false, isPositive = false))
      rows.add(ResultRowItem("SGST / UTGST (${formatDecimal(gstRate / 2.0, 1)}%)", formatRupee(sgst), false, isPositive = false))
      rows.add(ResultRowItem("Total GST (CGST + SGST)", formatRupee(totalGst), true, isPositive = false, footnote = "Intra-State Supply"))
    }

    rows.add(ResultRowItem("Final Invoice Value (Total)", formatRupee(totalInvoice), true, isPositive = true))
    return rows
  }

  // -----------------------------------------------------------------------------------------------
  // 8. EMI CALCULATOR
  // -----------------------------------------------------------------------------------------------
  fun calculateEmi(loanAmount: Double, rateAnnual: Double, tenureMonths: Int): List<ResultRowItem> {
    val monthlyRate = (rateAnnual / 12.0) / 100.0
    val emi = if (monthlyRate > 0) {
      (loanAmount * monthlyRate * (1.0 + monthlyRate).pow(tenureMonths)) /
        ((1.0 + monthlyRate).pow(tenureMonths) - 1.0)
    } else {
      loanAmount / tenureMonths.toDouble()
    }
    val totalPayment = emi * tenureMonths
    val totalInterest = totalPayment - loanAmount
    val interestShare = if (totalPayment > 0) (totalInterest / totalPayment) * 100.0 else 0.0

    return listOf(
      ResultRowItem("Loan Principal Amount", formatRupee(loanAmount)),
      ResultRowItem("Monthly EMI Installment", formatRupee(emi), true, isPositive = false),
      ResultRowItem("Total Interest Payable", formatRupee(totalInterest), true, isPositive = false, footnote = "${formatDecimal(interestShare, 1)}% of total payment"),
      ResultRowItem("Total Payment (Principal + Interest)", formatRupee(totalPayment), true, isPositive = false),
      ResultRowItem("Loan Tenure", "$tenureMonths Months (${formatDecimal(tenureMonths / 12.0, 1)} Years)")
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 9. NET PRESENT VALUE (NPV)
  // -----------------------------------------------------------------------------------------------
  fun calculateNpv(initialOutflow: Double, discountRatePercent: Double, inflows: List<Double>): List<ResultRowItem> {
    val r = discountRatePercent / 100.0
    var pvOfInflows = 0.0
    for ((index, cf) in inflows.withIndex()) {
      val t = index + 1
      pvOfInflows += cf / (1.0 + r).pow(t)
    }
    val npv = pvOfInflows - initialOutflow
    val isAccepted = npv >= 0

    return listOf(
      ResultRowItem("Initial Capital Outflow (I₀)", "- " + formatRupee(initialOutflow)),
      ResultRowItem("Discount Rate / WACC", "${formatDecimal(discountRatePercent, 2)}%"),
      ResultRowItem("Present Value of All Inflows", formatRupee(pvOfInflows)),
      ResultRowItem("Net Present Value (NPV)", formatRupee(npv), true, isPositive = isAccepted, footnote = if (isAccepted) "ACCEPT: Value Accretive Project" else "REJECT: Negative NPV"),
      ResultRowItem("Profitability Index (PI)", formatDecimal(if (initialOutflow > 0) pvOfInflows / initialOutflow else 0.0, 2), false, isPositive = isAccepted)
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 10. INTERNAL RATE OF RETURN (IRR)
  // -----------------------------------------------------------------------------------------------
  fun calculateIrr(initialOutflow: Double, inflows: List<Double>): List<ResultRowItem> {
    // Bisection method between 0% and 100%
    var low = 0.0
    var high = 2.0 // up to 200%
    var irr = 0.0
    var found = false

    fun npvAt(rate: Double): Double {
      var pv = 0.0
      for ((index, cf) in inflows.withIndex()) {
        val t = index + 1
        pv += cf / (1.0 + rate).pow(t)
      }
      return pv - initialOutflow
    }

    for (i in 0 until 100) {
      val mid = (low + high) / 2.0
      val npvMid = npvAt(mid)
      if (abs(npvMid) < 0.001) {
        irr = mid
        found = true
        break
      }
      if (npvMid > 0) {
        low = mid
      } else {
        high = mid
      }
      irr = mid
    }

    val irrPercent = irr * 100.0
    return listOf(
      ResultRowItem("Initial Cash Outflow", "- " + formatRupee(initialOutflow)),
      ResultRowItem("Number of Cash Inflow Periods", "${inflows.size} Years"),
      ResultRowItem("Internal Rate of Return (IRR)", "${formatDecimal(irrPercent, 2)}% p.a.", true, isPositive = irrPercent >= 10.0, footnote = "Exact discount rate where NPV = ₹0"),
      ResultRowItem("CA Exam Recommendation", if (irrPercent >= 12.0) "Accept if WACC ≤ ${formatDecimal(irrPercent, 1)}%" else "Caution: Low IRR Project", false)
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 11. PAYBACK PERIOD
  // -----------------------------------------------------------------------------------------------
  fun calculatePaybackPeriod(initialInvestment: Double, annualCashFlow: Double): List<ResultRowItem> {
    val paybackYears = if (annualCashFlow > 0) initialInvestment / annualCashFlow else 0.0
    val fullYears = paybackYears.toInt()
    val remainingMonths = ((paybackYears - fullYears) * 12.0).toInt()

    return listOf(
      ResultRowItem("Initial Investment", formatRupee(initialInvestment)),
      ResultRowItem("Annual Even Cash Inflow", formatRupee(annualCashFlow)),
      ResultRowItem("Exact Payback Period", "${formatDecimal(paybackYears, 2)} Years", true, isPositive = true),
      ResultRowItem("Payback in Years & Months", "$fullYears Years, $remainingMonths Months", true, isPositive = true, footnote = "Time taken to recover capital outlay"),
      ResultRowItem("Annual ROI (Unadjusted)", "${formatDecimal(if (initialInvestment > 0) (annualCashFlow / initialInvestment) * 100.0 else 0.0, 2)}%", false)
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 12. FINANCIAL BREAK-EVEN POINT (EBIT for EPS = 0)
  // -----------------------------------------------------------------------------------------------
  fun calculateFinancialBep(interestCharge: Double, prefDividend: Double, taxRatePercent: Double): List<ResultRowItem> {
    val taxDecimal = taxRatePercent / 100.0
    val finBep = interestCharge + (prefDividend / (1.0 - taxDecimal))
    return listOf(
      ResultRowItem("Fixed Interest Charge (I)", formatRupee(interestCharge)),
      ResultRowItem("Preference Dividend (PD)", formatRupee(prefDividend)),
      ResultRowItem("Corporate Tax Rate (t)", "${formatDecimal(taxRatePercent, 1)}%"),
      ResultRowItem("Financial Break-Even EBIT", formatRupee(finBep), true, isPositive = true, footnote = "EBIT required for EPS = ₹0.00"),
      ResultRowItem("Gross Tax-Equivalent Pref. Div", formatRupee(if (taxDecimal < 1.0) prefDividend / (1.0 - taxDecimal) else 0.0), false)
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 13. INDIAN INCOME TAX (OLD vs NEW REGIME FY 2024-25 / AY 2025-26)
  // -----------------------------------------------------------------------------------------------
  fun calculateIncomeTaxComparison(
    grossSalary: Double,
    otherIncome: Double,
    deductions80C: Double,
    deductions80D: Double,
    hraAndOtherExemptions: Double,
    isSeniorCitizen: Boolean
  ): IncomeTaxSlabComparison {
    val totalIncome = grossSalary + otherIncome

    // ---- OLD REGIME ----
    // Old regime standard deduction ₹50,000 for salaried
    val oldStdDed = if (grossSalary > 0) 50000.0 else 0.0
    val totalOldDeductions = oldStdDed + deductions80C + deductions80D + hraAndOtherExemptions
    val oldTaxable = (totalIncome - totalOldDeductions).coerceAtLeast(0.0)

    var oldTax = 0.0
    val basicExemptionOld = if (isSeniorCitizen) 300000.0 else 250000.0
    if (oldTaxable > basicExemptionOld) {
      if (oldTaxable <= 500000.0) {
        oldTax += (oldTaxable - basicExemptionOld) * 0.05
      } else if (oldTaxable <= 1000000.0) {
        oldTax += (500000.0 - basicExemptionOld) * 0.05
        oldTax += (oldTaxable - 500000.0) * 0.20
      } else {
        oldTax += (500000.0 - basicExemptionOld) * 0.05
        oldTax += 500000.0 * 0.20
        oldTax += (oldTaxable - 1000000.0) * 0.30
      }
    }
    // Rebate u/s 87A for Old Regime if taxable income <= 5,00,000
    if (oldTaxable <= 500000.0) {
      oldTax = 0.0
    }
    val oldCess = oldTax * 0.04
    val oldTotalTax = oldTax + oldCess

    // ---- NEW REGIME (AY 2025-26 u/s 115BAC) ----
    // Standard deduction under New Regime is ₹75,000 for salaried
    val newStdDed = if (grossSalary > 0) 75000.0 else 0.0
    val newTaxable = (totalIncome - newStdDed).coerceAtLeast(0.0)

    var newTax = 0.0
    // Slabs: 0-3L Nil, 3-7L 5%, 7-10L 10%, 10-12L 15%, 12-15L 20%, >15L 30%
    if (newTaxable > 300000.0) {
      val slab1 = (newTaxable.coerceAtMost(700000.0) - 300000.0).coerceAtLeast(0.0)
      newTax += slab1 * 0.05

      if (newTaxable > 700000.0) {
        val slab2 = (newTaxable.coerceAtMost(1000000.0) - 700000.0).coerceAtLeast(0.0)
        newTax += slab2 * 0.10
      }
      if (newTaxable > 1000000.0) {
        val slab3 = (newTaxable.coerceAtMost(1200000.0) - 1000000.0).coerceAtLeast(0.0)
        newTax += slab3 * 0.15
      }
      if (newTaxable > 1200000.0) {
        val slab4 = (newTaxable.coerceAtMost(1500000.0) - 1200000.0).coerceAtLeast(0.0)
        newTax += slab4 * 0.20
      }
      if (newTaxable > 1500000.0) {
        val slab5 = (newTaxable - 1500000.0)
        newTax += slab5 * 0.30
      }
    }
    // Rebate u/s 87A for New Regime if taxable income <= ₹7,00,000
    if (newTaxable <= 700000.0) {
      newTax = 0.0
    }
    val newCess = newTax * 0.04
    val newTotalTax = newTax + newCess

    val recommended = if (newTotalTax <= oldTotalTax) "New Tax Regime (Default)" else "Old Tax Regime"
    val savings = abs(oldTotalTax - newTotalTax)

    return IncomeTaxSlabComparison(
      grossIncome = totalIncome,
      totalDeductionsOld = totalOldDeductions,
      oldRegimeTaxableIncome = oldTaxable,
      oldRegimeTax = oldTax,
      oldRegimeCess = oldCess,
      oldRegimeTotalTax = oldTotalTax,
      newRegimeStandardDeduction = newStdDed,
      newRegimeTaxableIncome = newTaxable,
      newRegimeTax = newTax,
      newRegimeCess = newCess,
      newRegimeTotalTax = newTotalTax,
      recommendedRegime = recommended,
      taxSavings = savings
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 14. TDS CALCULATOR
  // -----------------------------------------------------------------------------------------------
  fun calculateTds(
    sectionCode: String,
    paymentAmount: Double,
    hasPan: Boolean
  ): List<ResultRowItem> {
    val rate = when {
      !hasPan -> 20.0
      sectionCode == "194C_INDIV" -> 1.0 // Contractor individual/HUF
      sectionCode == "194C_COMP" -> 2.0 // Contractor company
      sectionCode == "194J_TECH" -> 2.0 // Technical services
      sectionCode == "194J_PROF" -> 10.0 // Professional services
      sectionCode == "194I_PLANT" -> 2.0 // Rent on plant & machinery
      sectionCode == "194I_LAND" -> 10.0 // Rent on land & building
      sectionCode == "194A" -> 10.0 // Interest other than securities
      else -> 10.0
    }

    val tdsAmount = paymentAmount * (rate / 100.0)
    val netPayable = paymentAmount - tdsAmount
    val sectionLabel = when (sectionCode) {
      "194C_INDIV" -> "194C - Contractor (Individual / HUF)"
      "194C_COMP" -> "194C - Contractor (Company / Firm)"
      "194J_TECH" -> "194J - Technical Services / Royalty"
      "194J_PROF" -> "194J - Professional Fees / Directors"
      "194I_PLANT" -> "194I(a) - Rent on Plant & Machinery"
      "194I_LAND" -> "194I(b) - Rent on Land, Building & Furniture"
      "194A" -> "194A - Interest (Non-Security)"
      else -> "Standard Slab / Custom"
    }

    return listOf(
      ResultRowItem("Gross Payment Amount", formatRupee(paymentAmount)),
      ResultRowItem("TDS Section", sectionLabel),
      ResultRowItem("PAN Status", if (hasPan) "Furnished (Normal Rate)" else "Not Furnished (20% u/s 206AA)", isHighlighted = !hasPan),
      ResultRowItem("TDS Deduction Rate", "${formatDecimal(rate, 1)}%"),
      ResultRowItem("Total TDS Amount to Deduct", "- " + formatRupee(tdsAmount), true, isPositive = false),
      ResultRowItem("Net Payment Payable to Party", formatRupee(netPayable), true, isPositive = true)
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 15. GST BASIC RETURN ESTIMATION (GSTR-3B)
  // -----------------------------------------------------------------------------------------------
  fun calculateGstr3b(
    outwardTaxableSales: Double,
    outwardGstRate: Double,
    inwardTaxablePurchases: Double,
    inwardGstRate: Double,
    reverseChargeLiability: Double = 0.0
  ): List<ResultRowItem> {
    val outputTax = outwardTaxableSales * (outwardGstRate / 100.0) + reverseChargeLiability
    val eligibleItc = inwardTaxablePurchases * (inwardGstRate / 100.0)
    val netPayable = outputTax - eligibleItc
    val isCashPayable = netPayable >= 0

    return listOf(
      ResultRowItem("Outward Taxable Sales", formatRupee(outwardTaxableSales)),
      ResultRowItem("Total Output GST Liability", formatRupee(outputTax), false, isPositive = false),
      ResultRowItem("Inward Taxable Purchases", formatRupee(inwardTaxablePurchases)),
      ResultRowItem("Eligible Input Tax Credit (ITC)", formatRupee(eligibleItc), false, isPositive = true),
      if (isCashPayable) {
        ResultRowItem("Net GST Payable in Cash (GSTR-3B)", formatRupee(netPayable), true, isPositive = false, footnote = "Challan deposit required by 20th of subsequent month")
      } else {
        ResultRowItem("Excess ITC Carried Forward", formatRupee(abs(netPayable)), true, isPositive = true, footnote = "Available for adjustment in next tax period")
      }
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 16. CA COST SHEET
  // -----------------------------------------------------------------------------------------------
  fun calculateCostSheet(
    directMaterial: Double,
    directLabour: Double,
    directExpenses: Double,
    factoryOverheads: Double,
    adminOverheads: Double,
    sellingOverheads: Double,
    desiredProfitPercent: Double,
    isProfitOnSales: Boolean // if true, profit % is on Sales; otherwise on Total Cost
  ): List<ResultRowItem> {
    val primeCost = directMaterial + directLabour + directExpenses
    val factoryCost = primeCost + factoryOverheads
    val costOfProduction = factoryCost + adminOverheads
    val totalCost = costOfProduction + sellingOverheads

    val profitAmount: Double
    val sellingPrice: Double
    if (isProfitOnSales) {
      // Selling Price = Total Cost / (1 - profit%)
      val marginDecimal = (desiredProfitPercent / 100.0).coerceAtMost(0.95)
      sellingPrice = totalCost / (1.0 - marginDecimal)
      profitAmount = sellingPrice - totalCost
    } else {
      // Profit on Total Cost
      profitAmount = totalCost * (desiredProfitPercent / 100.0)
      sellingPrice = totalCost + profitAmount
    }

    return listOf(
      ResultRowItem("1. Direct Materials Consumed", formatRupee(directMaterial)),
      ResultRowItem("2. Direct Labour Cost", formatRupee(directLabour)),
      ResultRowItem("3. Direct Expenses", formatRupee(directExpenses)),
      ResultRowItem("PRIME COST (1 + 2 + 3)", formatRupee(primeCost), true, isPositive = true),
      ResultRowItem("4. Factory / Works Overheads", "+ " + formatRupee(factoryOverheads)),
      ResultRowItem("FACTORY / WORKS COST", formatRupee(factoryCost), true, isPositive = true),
      ResultRowItem("5. Administrative Overheads", "+ " + formatRupee(adminOverheads)),
      ResultRowItem("COST OF PRODUCTION (COP)", formatRupee(costOfProduction), true, isPositive = true),
      ResultRowItem("6. Selling & Distribution Overheads", "+ " + formatRupee(sellingOverheads)),
      ResultRowItem("TOTAL COST / COST OF SALES", formatRupee(totalCost), true, isPositive = true),
      ResultRowItem("Profit Margin (${formatDecimal(desiredProfitPercent, 1)}% ${if (isProfitOnSales) "on Sales" else "on Cost"})", "+ " + formatRupee(profitAmount), true, isPositive = true),
      ResultRowItem("FINAL SELLING PRICE / SALES", formatRupee(sellingPrice), true, isPositive = true, footnote = "ICAI standard Cost Sheet layout")
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 17. MARGINAL COSTING (CVP ANALYSIS)
  // -----------------------------------------------------------------------------------------------
  fun calculateMarginalCosting(
    totalSales: Double,
    variableCost: Double,
    fixedCost: Double
  ): List<ResultRowItem> {
    val contribution = totalSales - variableCost
    val pvRatio = if (totalSales > 0) (contribution / totalSales) * 100.0 else 0.0
    val bepSalesRs = if (pvRatio > 0) fixedCost / (pvRatio / 100.0) else 0.0
    val marginOfSafetyRs = totalSales - bepSalesRs
    val mosPercent = if (totalSales > 0) (marginOfSafetyRs / totalSales) * 100.0 else 0.0
    val netProfit = contribution - fixedCost

    return listOf(
      ResultRowItem("Total Sales Value", formatRupee(totalSales)),
      ResultRowItem("Total Contribution (Sales - VC)", formatRupee(contribution), true, isPositive = true),
      ResultRowItem("P/V Ratio (Profit Volume Ratio)", "${formatDecimal(pvRatio, 2)}%", true, isPositive = pvRatio >= 20.0, footnote = "(Contribution / Sales) × 100"),
      ResultRowItem("Fixed Cost (FC)", "- " + formatRupee(fixedCost)),
      ResultRowItem("Net Profit / Operating Profit", formatRupee(netProfit), true, isPositive = netProfit >= 0),
      ResultRowItem("Break-Even Sales (₹)", formatRupee(bepSalesRs), true, isPositive = true, footnote = "Sales required for zero profit"),
      ResultRowItem("Margin of Safety (MoS - ₹)", formatRupee(marginOfSafetyRs.coerceAtLeast(0.0)), false, isPositive = marginOfSafetyRs >= 0),
      ResultRowItem("Margin of Safety Ratio (%)", "${formatDecimal(mosPercent.coerceAtLeast(0.0), 2)}%", true, isPositive = mosPercent >= 20.0)
    )
  }

  // -----------------------------------------------------------------------------------------------
  // 18. BREAK-EVEN ANALYSIS (UNITS & TARGET PROFIT)
  // -----------------------------------------------------------------------------------------------
  fun calculateBreakEvenAnalysis(
    fixedCost: Double,
    sellingPricePerUnit: Double,
    variableCostPerUnit: Double,
    targetProfit: Double = 0.0
  ): List<ResultRowItem> {
    val contributionPerUnit = sellingPricePerUnit - variableCostPerUnit
    val bepUnits = if (contributionPerUnit > 0) fixedCost / contributionPerUnit else 0.0
    val bepRevenue = bepUnits * sellingPricePerUnit
    val targetUnits = if (contributionPerUnit > 0) (fixedCost + targetProfit) / contributionPerUnit else 0.0
    val targetRevenue = targetUnits * sellingPricePerUnit

    return listOf(
      ResultRowItem("Contribution per Unit", formatRupee(contributionPerUnit), true, isPositive = contributionPerUnit > 0),
      ResultRowItem("P/V Ratio", "${formatDecimal(if (sellingPricePerUnit > 0) (contributionPerUnit / sellingPricePerUnit) * 100.0 else 0.0, 2)}%"),
      ResultRowItem("Break-Even Point (Units)", "${formatDecimal(bepUnits, 0)} Units", true, isPositive = true),
      ResultRowItem("Break-Even Revenue (₹)", formatRupee(bepRevenue), true, isPositive = true),
      if (targetProfit > 0) {
        ResultRowItem("Units for Target Profit of ${formatRupee(targetProfit)}", "${formatDecimal(targetUnits, 0)} Units", true, isPositive = true)
      } else {
        ResultRowItem("Target Profit Goal", "₹0 (Break-Even Default)")
      },
      if (targetProfit > 0) {
        ResultRowItem("Revenue Required for Target Profit", formatRupee(targetRevenue), true, isPositive = true)
      } else {
        ResultRowItem("Status", "Balanced at BEP")
      }
    )
  }
}
