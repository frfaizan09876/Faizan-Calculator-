package com.example.ui.screens.about

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ExamTipCard
import com.example.ui.components.LuxuryCard
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.GoldAmber
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextGoldSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun AboutScreen() {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(DeepBlack)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(4.dp))
      // App Identity Banner
      LuxuryCard(
        backgroundColor = SurfaceDark,
        borderColor = GoldPrimary,
        borderWidth = 1.5.dp,
        contentPadding = 20.dp
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0xFF262113)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.School,
              contentDescription = null,
              tint = GoldPrimary,
              modifier = Modifier.size(32.dp)
            )
          }
          Spacer(modifier = Modifier.width(16.dp))
          Column {
            Text(
              text = "FAIZAN RAZA",
              color = GoldPrimary,
              fontSize = 18.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "CA Calculator & Audit System",
              color = TextWhite,
              fontSize = 16.sp,
              fontWeight = FontWeight.ExtraBold
            )
            Text(
              text = "Founder & Chief Developer • Version 1.0",
              color = TextGoldSecondary,
              fontSize = 12.sp
            )
          }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = "CA Calculator is an official, all-in-one financial, accounting, tax, and costing utility designed under the direction of FAIZAN RAZA for Indian commerce students, CA Foundation, CA Intermediate, and CA Final aspirants under the Institute of Chartered Accountants of India (ICAI).",
          color = TextWhite,
          fontSize = 13.5.sp,
          lineHeight = 19.sp
        )
      }
    }

    item {
      Text(
        text = "ICAI SYLLABUS ALIGNMENT",
        color = GoldPrimary,
        fontSize = 15.sp,
        fontWeight = FontWeight.ExtraBold
      )
    }

    item {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SyllabusFeatureCard(
          level = "CA FOUNDATION",
          subjects = "Paper 1 (Accounting) & Paper 3 (Quantitative Aptitude)",
          coverage = "Simple & Compound Interest, Depreciation (SLM & WDV schedule), Ratio Analysis, Discount & Markup calculations."
        )
        SyllabusFeatureCard(
          level = "CA INTERMEDIATE",
          subjects = "Paper 3 (Taxation) & Paper 4 (Cost & FM)",
          coverage = "Income Tax (AY 2025-26 Old vs New regime u/s 115BAC), TDS u/s 194C/J/I, GSTR-3B, Cost Sheet, Marginal Costing, CVP, NPV, IRR & Payback."
        )
        SyllabusFeatureCard(
          level = "CA FINAL / ARTICLESHEEP",
          subjects = "Paper 1 (FR) & Paper 4 (Direct Tax / Indirect Tax)",
          coverage = "Advanced Financial Break-Even, WDV schedule verification, GSTR-3B ITC carry forward, and client tax regime planning."
        )
      }
    }

    item {
      Text(
        text = "AY 2025-26 TAX REGIME QUICK REFERENCE",
        color = GoldPrimary,
        fontSize = 15.sp,
        fontWeight = FontWeight.ExtraBold
      )
    }

    item {
      LuxuryCard(
        backgroundColor = SurfaceDark,
        borderColor = CardBorderGold,
        contentPadding = 16.dp
      ) {
        Text(
          text = "NEW TAX REGIME (DEFAULT u/s 115BAC)",
          color = Color.White,
          fontSize = 14.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "• Standard Deduction of ₹75,000 for salaried taxpayers.\n" +
            "• Rebate u/s 87A makes tax NIL up to ₹7,00,000 taxable income.\n" +
            "• Slabs: 0-3L Nil | 3-7L 5% | 7-10L 10% | 10-12L 15% | 12-15L 20% | >15L 30%.\n" +
            "• Health & Education Cess: 4% applicable on total tax liability.",
          color = TextGoldSecondary,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = "OLD TAX REGIME (OPTIONAL)",
          color = Color.White,
          fontSize = 14.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "• Allows deductions under Chapter VI-A (80C, 80D), HRA, LTA & ₹50,000 standard deduction.\n" +
            "• Slabs: 0-2.5L Nil | 2.5-5L 5% | 5-10L 20% | >10L 30%.\n" +
            "• Rebate u/s 87A up to ₹5,00,000 taxable income.",
          color = TextGoldSecondary,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )
      }
    }

    item {
      ExamTipCard(
        tipText = "CA Exam Strategy: Always state your formula clearly, define variables, and round decimal values to 2 places unless instructed otherwise in the ICAI question paper."
      )
    }

    item {
      LuxuryCard(
        backgroundColor = Color(0xFF161512),
        borderColor = CardBorderGold,
        contentPadding = 14.dp
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "DISCLAIMER: This application is a mathematical and professional study utility. All calculations are strictly based on standard ICAI formulas and statutory Indian tax laws for educational and reference use.",
            color = TextMuted,
            fontSize = 11.sp,
            lineHeight = 15.sp
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
fun SyllabusFeatureCard(
  level: String,
  subjects: String,
  coverage: String
) {
  LuxuryCard(
    backgroundColor = SurfaceDark,
    borderColor = CardBorderGold,
    contentPadding = 14.dp
  ) {
    Row(verticalAlignment = Alignment.Top) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0xFF2E2713)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Star,
          contentDescription = null,
          tint = GoldAmber,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = level,
          color = GoldPrimary,
          fontSize = 14.sp,
          fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = subjects,
          color = TextWhite,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = coverage,
          color = TextGoldSecondary,
          fontSize = 12.sp,
          lineHeight = 17.sp
        )
      }
    }
  }
}
