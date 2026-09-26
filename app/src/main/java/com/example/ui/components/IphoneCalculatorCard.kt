package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextGoldSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.text.DecimalFormat

@Composable
fun IphoneCalculatorFullScreenModal(
  onDismiss: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(
      usePlatformDefaultWidth = false,
      dismissOnBackPress = true,
      dismissOnClickOutside = false
    )
  ) {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = DeepBlack
    ) {
      var displayText by remember { mutableStateOf("0") }
      var expressionText by remember { mutableStateOf("") }
      var firstOperand by remember { mutableStateOf<Double?>(null) }
      var operator by remember { mutableStateOf<String?>(null) }
      var clearOnNextDigit by remember { mutableStateOf(false) }

      val df = remember { DecimalFormat("#,###.########") }

      fun onButtonPress(btn: String) {
        when (btn) {
          "AC" -> {
            displayText = "0"
            expressionText = ""
            firstOperand = null
            operator = null
            clearOnNextDigit = false
          }
          "+/-" -> {
            val current = displayText.replace(",", "").toDoubleOrNull() ?: 0.0
            if (current != 0.0) {
              displayText = df.format(-current)
            }
          }
          "%" -> {
            val current = displayText.replace(",", "").toDoubleOrNull() ?: 0.0
            val res = current / 100.0
            displayText = df.format(res)
            expressionText = "$current% = ${df.format(res)}"
          }
          "÷", "×", "-", "+" -> {
            val current = displayText.replace(",", "").toDoubleOrNull() ?: 0.0
            firstOperand = current
            operator = btn
            expressionText = "${df.format(current)} $btn"
            clearOnNextDigit = true
          }
          "=" -> {
            val second = displayText.replace(",", "").toDoubleOrNull() ?: 0.0
            val first = firstOperand
            val op = operator
            if (first != null && op != null) {
              val res = when (op) {
                "+" -> first + second
                "-" -> first - second
                "×" -> first * second
                "÷" -> if (second != 0.0) first / second else Double.NaN
                else -> second
              }
              expressionText = "${df.format(first)} $op ${df.format(second)} ="
              displayText = if (res.isNaN()) "Error" else df.format(res)
              firstOperand = null
              operator = null
              clearOnNextDigit = true
            }
          }
          "." -> {
            if (clearOnNextDigit) {
              displayText = "0."
              clearOnNextDigit = false
            } else if (!displayText.contains(".")) {
              displayText += "."
            }
          }
          else -> {
            // Digit
            if (displayText == "0" || clearOnNextDigit || displayText == "Error") {
              displayText = btn
              clearOnNextDigit = false
            } else if (displayText.replace(",", "").length < 14) {
              val clean = (displayText + btn).replace(",", "")
              val num = clean.toDoubleOrNull()
              if (num != null && !displayText.endsWith(".")) {
                displayText = df.format(num)
              } else {
                displayText += btn
              }
            }
          }
        }
      }

      Column(
        modifier = Modifier
          .fillMaxSize()
          .background(DeepBlack)
          .padding(horizontal = 20.dp, vertical = 12.dp)
      ) {
        // Top Header bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF2B200A)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Calculate,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "iPHONE CALCULATOR",
                color = GoldPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
              )
              Text(
                text = "Full Screen Apple iOS Interface",
                color = TextGoldSecondary,
                fontSize = 12.sp
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(SurfaceDark)
              .clickable { onDismiss() }
              .padding(10.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close Calculator",
              tint = TextWhite,
              modifier = Modifier.size(22.dp)
            )
          }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Expression line
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
          contentAlignment = Alignment.CenterEnd
        ) {
          Text(
            text = expressionText,
            color = GoldPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace
          )
        }

        // Giant Main Display Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
          contentAlignment = Alignment.CenterEnd
        ) {
          Text(
            text = displayText,
            color = TextWhite,
            fontSize = if (displayText.length > 9) 38.sp else 54.sp,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Keypad Grid
        val buttonRows = listOf(
          listOf("AC", "+/-", "%", "÷"),
          listOf("7", "8", "9", "×"),
          listOf("4", "5", "6", "-"),
          listOf("1", "2", "3", "+"),
          listOf("0", ".", "=")
        )

        buttonRows.forEach { row ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            row.forEach { btnText ->
              val isZero = (btnText == "0")
              val isOp = btnText in listOf("÷", "×", "-", "+", "=")
              val isTop = btnText in listOf("AC", "+/-", "%")
              val isSelectedOp = (operator == btnText)

              val bgColor = when {
                isSelectedOp -> TextWhite
                isOp -> Color(0xFFFF9F0A) // Apple Orange
                isTop -> Color(0xFFA5A5A5) // Apple Silver
                else -> Color(0xFF333333)  // Apple Dark Gray
              }
              val textColor = when {
                isSelectedOp -> Color(0xFFFF9F0A)
                isOp || isTop -> DeepBlack
                else -> TextWhite
              }

              Box(
                modifier = Modifier
                  .weight(if (isZero) 2.1f else 1f)
                  .height(68.dp)
                  .clip(if (isZero) RoundedCornerShape(34.dp) else CircleShape)
                  .background(bgColor)
                  .clickable { onButtonPress(btnText) },
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = btnText,
                  color = textColor,
                  fontSize = 24.sp,
                  fontWeight = if (isOp || isTop) FontWeight.ExtraBold else FontWeight.SemiBold
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}

@Composable
fun IphoneCalculatorCard() {
  var displayText by remember { mutableStateOf("0") }
  var firstOperand by remember { mutableStateOf<Double?>(null) }
  var operator by remember { mutableStateOf<String?>(null) }
  var clearOnNextDigit by remember { mutableStateOf(false) }

  val df = remember { DecimalFormat("#,###.########") }

  fun onButtonPress(btn: String) {
    when (btn) {
      "AC" -> {
        displayText = "0"
        firstOperand = null
        operator = null
        clearOnNextDigit = false
      }
      "+/-" -> {
        val current = displayText.replace(",", "").toDoubleOrNull() ?: 0.0
        if (current != 0.0) {
          displayText = df.format(-current)
        }
      }
      "%" -> {
        val current = displayText.replace(",", "").toDoubleOrNull() ?: 0.0
        displayText = df.format(current / 100.0)
      }
      "÷", "×", "-", "+" -> {
        firstOperand = displayText.replace(",", "").toDoubleOrNull()
        operator = btn
        clearOnNextDigit = true
      }
      "=" -> {
        val second = displayText.replace(",", "").toDoubleOrNull() ?: 0.0
        val first = firstOperand
        val op = operator
        if (first != null && op != null) {
          val res = when (op) {
            "+" -> first + second
            "-" -> first - second
            "×" -> first * second
            "÷" -> if (second != 0.0) first / second else Double.NaN
            else -> second
          }
          displayText = if (res.isNaN()) "Error" else df.format(res)
          firstOperand = null
          operator = null
          clearOnNextDigit = true
        }
      }
      "." -> {
        if (clearOnNextDigit) {
          displayText = "0."
          clearOnNextDigit = false
        } else if (!displayText.contains(".")) {
          displayText += "."
        }
      }
      else -> {
        // Digit
        if (displayText == "0" || clearOnNextDigit || displayText == "Error") {
          displayText = btn
          clearOnNextDigit = false
        } else if (displayText.replace(",", "").length < 12) {
          val clean = (displayText + btn).replace(",", "")
          val num = clean.toDoubleOrNull()
          if (num != null && !displayText.endsWith(".")) {
            displayText = df.format(num)
          } else {
            displayText += btn
          }
        }
      }
    }
  }

  LuxuryCard(
    backgroundColor = Color(0xFF141414),
    borderColor = CardBorderGold,
    borderWidth = 1.dp,
    contentPadding = 16.dp
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "iPHONE STYLE INSTANT CALCULATOR",
          color = GoldPrimary,
          fontSize = 11.sp,
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 0.8.sp
        )
        Text(
          text = if (operator != null) "OP: $operator" else "READY",
          color = TextMuted,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Display Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(64.dp)
          .background(Color(0xFF0C0C0C), RoundedCornerShape(12.dp))
          .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterEnd
      ) {
        Text(
          text = displayText,
          color = TextWhite,
          fontSize = 34.sp,
          fontWeight = FontWeight.Light,
          fontFamily = FontFamily.Monospace,
          textAlign = TextAlign.End,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Calculator Keypad
      val buttonRows = listOf(
        listOf("AC", "+/-", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
      )

      buttonRows.forEach { row ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          row.forEach { btnText ->
            val isZero = (btnText == "0")
            val isOp = btnText in listOf("÷", "×", "-", "+", "=")
            val isTop = btnText in listOf("AC", "+/-", "%")

            val bgColor = when {
              isOp -> Color(0xFFFF9F0A) // Apple Orange
              isTop -> Color(0xFFA5A5A5) // Apple Silver
              else -> Color(0xFF333333)  // Apple Dark Gray
            }
            val textColor = when {
              isOp || isTop -> DeepBlack
              else -> TextWhite
            }

            Box(
              modifier = Modifier
                .weight(if (isZero) 2.1f else 1f)
                .height(54.dp)
                .clip(if (isZero) RoundedCornerShape(27.dp) else CircleShape)
                .background(bgColor)
                .clickable { onButtonPress(btnText) },
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = btnText,
                color = textColor,
                fontSize = 20.sp,
                fontWeight = if (isOp) FontWeight.ExtraBold else FontWeight.Medium
              )
            }
          }
        }
      }
    }
  }
}
