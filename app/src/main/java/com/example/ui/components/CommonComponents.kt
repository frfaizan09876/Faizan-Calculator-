package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldAmber
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMuted
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RubyRed
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextGoldSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun LuxuryCard(
  modifier: Modifier = Modifier,
  borderWidth: Dp = 1.dp,
  borderColor: Color = CardBorderGold,
  backgroundColor: Color = CardBackground,
  contentPadding: Dp = 11.dp,
  onClick: (() -> Unit)? = null,
  content: @Composable ColumnScope.() -> Unit
) {
  val shape = RoundedCornerShape(12.dp)
  Card(
    modifier = modifier
      .fillMaxWidth()
      .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
    shape = shape,
    colors = CardDefaults.cardColors(containerColor = backgroundColor),
    border = BorderStroke(borderWidth, borderColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(contentPadding),
      content = content
    )
  }
}

@Composable
fun GoldButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  enabled: Boolean = true
) {
  Button(
    onClick = onClick,
    modifier = modifier
      .fillMaxWidth()
      .height(42.dp),
    shape = RoundedCornerShape(10.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = GoldPrimary,
      contentColor = DeepBlack,
      disabledContainerColor = GoldMuted,
      disabledContentColor = TextWhite.copy(alpha = 0.6f)
    ),
    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
  ) {
    if (icon != null) {
      Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
      Spacer(modifier = Modifier.width(6.dp))
    }
    Text(
      text = text.uppercase(),
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      letterSpacing = 0.5.sp
    )
  }
}

@Composable
fun SecondaryButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null
) {
  OutlinedButton(
    onClick = onClick,
    modifier = modifier
      .fillMaxWidth()
      .height(42.dp),
    shape = RoundedCornerShape(10.dp),
    border = BorderStroke(1.dp, GoldPrimary),
    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
  ) {
    if (icon != null) {
      Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
      Spacer(modifier = Modifier.width(6.dp))
    }
    Text(
      text = text.uppercase(),
      fontWeight = FontWeight.Bold,
      fontSize = 12.5.sp,
      letterSpacing = 0.5.sp
    )
  }
}

@Composable
fun CaTextField(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  modifier: Modifier = Modifier,
  placeholder: String = "",
  prefixText: String = "",
  suffixText: String = "",
  keyboardType: KeyboardType = KeyboardType.Decimal,
  imeAction: ImeAction = ImeAction.Next,
  onAction: () -> Unit = {},
  isError: Boolean = false,
  errorMessage: String = "",
  visualTransformation: VisualTransformation = VisualTransformation.None,
  trailingIcon: @Composable (() -> Unit)? = null
) {
  Column(modifier = modifier.fillMaxWidth()) {
    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      label = {
        Text(
          text = label,
          color = if (isError) RubyRed else TextGoldSecondary,
          fontWeight = FontWeight.Medium
        )
      },
      placeholder = if (placeholder.isNotEmpty()) {
        { Text(placeholder, color = TextMuted) }
      } else null,
      prefix = if (prefixText.isNotEmpty()) {
        { Text(prefixText, color = GoldLight, fontWeight = FontWeight.SemiBold) }
      } else null,
      suffix = if (suffixText.isNotEmpty()) {
        { Text(suffixText, color = GoldLight, fontWeight = FontWeight.SemiBold) }
      } else null,
      trailingIcon = trailingIcon,
      visualTransformation = visualTransformation,
      singleLine = true,
      modifier = Modifier.fillMaxWidth(),
      keyboardOptions = KeyboardOptions(
        keyboardType = keyboardType,
        imeAction = imeAction
      ),
      keyboardActions = KeyboardActions(
        onNext = { onAction() },
        onDone = { onAction() }
      ),
      isError = isError,
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = GoldPrimary,
        unfocusedBorderColor = CardBorderGold,
        errorBorderColor = RubyRed,
        focusedTextColor = TextWhite,
        unfocusedTextColor = TextWhite,
        cursorColor = GoldPrimary,
        focusedContainerColor = SurfaceDark,
        unfocusedContainerColor = SurfaceDark
      ),
      shape = RoundedCornerShape(12.dp)
    )
    AnimatedVisibility(visible = isError && errorMessage.isNotEmpty()) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp, start = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Warning,
          contentDescription = "Error",
          tint = RubyRed,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = errorMessage,
          color = RubyRed,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}

@Composable
fun ResultRowCard(
  label: String,
  value: String,
  isHighlighted: Boolean = false,
  isPositive: Boolean = true,
  footnote: String = ""
) {
  val bgColor = if (isHighlighted) {
    if (isPositive) Color(0xFF1B2E24) else Color(0xFF332023)
  } else {
    SurfaceDark
  }
  val borderColor = if (isHighlighted) {
    if (isPositive) EmeraldGreen.copy(alpha = 0.5f) else RubyRed.copy(alpha = 0.5f)
  } else {
    CardBorderGold.copy(alpha = 0.5f)
  }
  val valueColor = if (isHighlighted) {
    if (isPositive) EmeraldGreen else RubyRed
  } else {
    GoldLight
  }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(bgColor)
      .border(1.dp, borderColor, RoundedCornerShape(10.dp))
      .padding(horizontal = 14.dp, vertical = 12.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = label,
          color = if (isHighlighted) TextWhite else TextGoldSecondary,
          fontSize = if (isHighlighted) 15.sp else 14.sp,
          fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium,
          modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = value,
          color = valueColor,
          fontSize = if (isHighlighted) 17.sp else 15.sp,
          fontWeight = FontWeight.ExtraBold,
          textAlign = TextAlign.End
        )
      }
      if (footnote.isNotEmpty()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = footnote,
          color = TextMuted,
          fontSize = 11.5.sp,
          fontWeight = FontWeight.Normal
        )
      }
    }
  }
}

@Composable
fun FormulaBadge(
  formulaText: String,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(50),
    color = Color(0xFF262113),
    border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.6f)),
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Default.Calculate,
        contentDescription = null,
        tint = GoldAmber,
        modifier = Modifier.size(14.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = formulaText,
        color = GoldLight,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
fun ExamTipCard(
  tipText: String,
  modifier: Modifier = Modifier
) {
  if (tipText.isEmpty()) return
  LuxuryCard(
    modifier = modifier,
    backgroundColor = Color(0xFF1B1B18),
    borderColor = GoldPrimary.copy(alpha = 0.4f),
    contentPadding = 12.dp
  ) {
    Row(verticalAlignment = Alignment.Top) {
      Icon(
        imageVector = Icons.Default.Info,
        contentDescription = "Exam Tip",
        tint = GoldAmber,
        modifier = Modifier
          .size(18.dp)
          .padding(top = 2.dp)
      )
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(
          text = "ICAI EXAM & PRACTICAL NOTE",
          color = GoldPrimary,
          fontSize = 11.sp,
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
          text = tipText,
          color = TextGoldSecondary,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )
      }
    }
  }
}

@Composable
fun SectionHeader(
  title: String,
  subtitle: String = "",
  actionText: String = "",
  onActionClick: (() -> Unit)? = null
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        color = TextWhite,
        fontSize = 18.sp,
        fontWeight = FontWeight.ExtraBold
      )
      if (subtitle.isNotEmpty()) {
        Text(
          text = subtitle,
          color = TextGoldSecondary,
          fontSize = 13.sp
        )
      }
    }
    if (actionText.isNotEmpty() && onActionClick != null) {
      Text(
        text = actionText,
        color = GoldPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .clickable { onActionClick() }
          .padding(4.dp)
      )
    }
  }
}
