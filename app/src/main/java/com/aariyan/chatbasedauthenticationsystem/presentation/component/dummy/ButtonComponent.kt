package com.aariyan.chatbasedauthenticationsystem.presentation.component.dummy

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilledButton(
    buttonTitle: String,
    modifier: Modifier,
    borderCornerRadius: Dp = 8.dp,
    fillColor: Color,
    buttonTitleColor: Color,
    titleFontSize: TextUnit = 13.sp,
    elevation: Dp = 2.dp,
    whenClicked: ()-> Unit
) {
    Button(
        onClick = {
            whenClicked()
        },
        modifier = modifier
            .height(48.dp),
        shape = RoundedCornerShape(borderCornerRadius),
        colors = ButtonDefaults.buttonColors(fillColor),
        elevation = ButtonDefaults.buttonElevation(elevation)
    ) {
        Text(text = buttonTitle, style = TextStyle(fontSize = titleFontSize, color = buttonTitleColor))
    }
}