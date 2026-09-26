package com.example.data.model

enum class ModuleType(val title: String, val badgeText: String, val subtitle: String) {
  ACCOUNTING("Accounting", "7 Tools", "Interest, Depreciation, Ratios, GST & P&L"),
  FINANCE("Finance", "5 Tools", "EMI, NPV, IRR, Payback & Financial BEP"),
  TAX("Tax (India)", "3 Tools", "Old vs New Income Tax Slab, TDS & GST GSTR-3B"),
  COSTING("Costing", "3 Tools", "CA Cost Sheet, Marginal Costing & CVP BEP")
}

data class CalculatorItem(
  val id: String,
  val title: String,
  val subtitle: String,
  val formulaBadge: String,
  val moduleType: ModuleType,
  val description: String,
  val examTip: String = ""
)

data class CalculationRecord(
  val id: String,
  val calculatorId: String,
  val calculatorTitle: String,
  val moduleTitle: String,
  val inputSummary: String,
  val resultSummary: String,
  val timestampMillis: Long,
  val detailedRows: List<ResultRowItem>,
  val userId: String? = null,
  val userEmail: String? = null
)

data class ResultRowItem(
  val label: String,
  val value: String,
  val isHighlighted: Boolean = false,
  val isPositive: Boolean = true,
  val footnote: String = ""
)

data class IncomeTaxSlabComparison(
  val grossIncome: Double,
  val totalDeductionsOld: Double,
  val oldRegimeTaxableIncome: Double,
  val oldRegimeTax: Double,
  val oldRegimeCess: Double,
  val oldRegimeTotalTax: Double,
  val newRegimeStandardDeduction: Double,
  val newRegimeTaxableIncome: Double,
  val newRegimeTax: Double,
  val newRegimeCess: Double,
  val newRegimeTotalTax: Double,
  val recommendedRegime: String,
  val taxSavings: Double
)

data class DepreciationScheduleRow(
  val year: Int,
  val openingWdv: Double,
  val depreciationAmount: Double,
  val closingWdv: Double
)
