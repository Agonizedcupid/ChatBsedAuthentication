package com.aariyan.chatbasedauthenticationsystem

import android.text.TextPaint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.aariyan.chatbasedauthenticationsystem.ui.theme.ChatBasedAuthenticationSystemTheme


@Composable
fun Bubble(
    content: @Composable () -> Unit,
    isSender: Boolean,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSender) Color(0xff52B69A) else Color(0xffEEF8F5)
    val arrowPosition = if (isSender) ArrowPosition.RIGHT else ArrowPosition.LEFT

    val density = LocalDensity.current
   // Log.d("CHECK_MESSAGE_STATE", "2. ${message} --- ${isSender}")

    Box(
        modifier = modifier
            .fillMaxWidth(if (isSender) 0.85f else 1f) // Adjust as needed
            .wrapContentWidth(
                align = if (isSender) Alignment.End else Alignment.Start
            )
            .background(backgroundColor, chatBubbleShape(density, arrowPosition))
            .padding(start = 15.dp, top = 16.dp, bottom = 16.dp, end = 15.dp)
    )  {
        //Text(modifier = Modifier.padding(end = 10.dp, start = 10.dp),text = message, color = Color(0xff71828A))
        content()
    }
}

fun chatBubbleShape(density: Density, arrowPosition: ArrowPosition) = GenericShape { size, _ ->
    with(density) {
        val arrowWidth = 15.dp.toPx()
        val arrowHeight = 15.dp.toPx()
        val arrowOffsetY = 20.dp.toPx()
        val cornerRad = 8.dp.toPx()

        when (arrowPosition) {
            ArrowPosition.LEFT -> {
                moveTo(arrowWidth + cornerRad, 0f)
                lineTo(size.width - cornerRad, 0f)
                arcTo(Rect(size.width - cornerRad, 0f, size.width, cornerRad * 2), 270f, 90f, false)
                lineTo(size.width, size.height - cornerRad)
                arcTo(Rect(size.width - cornerRad, size.height - cornerRad * 2, size.width, size.height), 0f, 90f, false)
                lineTo(arrowWidth + cornerRad, size.height)
                arcTo(Rect(arrowWidth, size.height - cornerRad * 2, arrowWidth + cornerRad * 2, size.height), 90f, 90f, false)
                lineTo(arrowWidth, arrowOffsetY + arrowHeight)
                lineTo(0f, arrowOffsetY + arrowHeight / 2)
                lineTo(arrowWidth, arrowOffsetY)
                lineTo(arrowWidth, cornerRad)
                arcTo(Rect(arrowWidth, 0f, arrowWidth + cornerRad * 2, cornerRad * 2), 180f, 90f, false)
            }
            ArrowPosition.RIGHT -> {
                // Start slightly down from the top left corner to leave space for the arc
                moveTo(0f, cornerRad)
                // Arc for the top left corner
                arcTo(Rect(0f, 0f, cornerRad * 2, cornerRad * 2), 180f, 90f, false)

                // Top side
                lineTo(size.width - cornerRad - arrowWidth, 0f)
                // Arc for the top right corner before the arrow
                arcTo(Rect(size.width - cornerRad * 2 - arrowWidth, 0f, size.width - arrowWidth, cornerRad * 2), 270f, 90f, false)

                // Right side with arrow
                lineTo(size.width - arrowWidth, arrowOffsetY)
                lineTo(size.width, arrowOffsetY + arrowHeight / 2)
                lineTo(size.width - arrowWidth, arrowOffsetY + arrowHeight)

                // Bottom right corner after the arrow
                lineTo(size.width - arrowWidth, size.height - cornerRad)
                arcTo(Rect(size.width - cornerRad * 2 - arrowWidth, size.height - cornerRad * 2, size.width - arrowWidth, size.height), 0f, 90f, false)

                // Bottom side
                lineTo(cornerRad, size.height)
                arcTo(Rect(0f, size.height - cornerRad * 2, cornerRad * 2, size.height), 90f, 90f, false)

                // Left side
                lineTo(0f, cornerRad)
            }





            ArrowPosition.TOP -> {
                val arrowOffsetX = size.width / 2 - arrowWidth / 2

                // Top left corner
                moveTo(cornerRad, arrowHeight)
                arcTo(Rect(0f, arrowHeight, cornerRad * 2, arrowHeight + cornerRad * 2), 180f, 90f, false)

                // Top arrow
                lineTo(arrowOffsetX, arrowHeight)
                lineTo(arrowOffsetX + arrowWidth / 2, 0f)
                lineTo(arrowOffsetX + arrowWidth, arrowHeight)

                // Top right corner
                lineTo(size.width - cornerRad, arrowHeight)
                arcTo(Rect(size.width - cornerRad * 2, arrowHeight, size.width, arrowHeight + cornerRad * 2), 270f, 90f, false)

                // Right side
                lineTo(size.width, size.height - cornerRad)
                arcTo(Rect(size.width - cornerRad * 2, size.height - cornerRad * 2, size.width, size.height), 0f, 90f, false)

                // Bottom right corner
                lineTo(cornerRad, size.height)
                arcTo(Rect(0f, size.height - cornerRad * 2, cornerRad * 2, size.height), 90f, 90f, false)

                // Left side
                lineTo(0f, arrowHeight + cornerRad)
                arcTo(Rect(0f, arrowHeight, cornerRad * 2, arrowHeight + cornerRad * 2), 180f, -90f, false)
            }
        }
        // Closing the path
        close()
    }
}


enum class ArrowPosition {
    LEFT, RIGHT, TOP
}




@Preview
@Composable
fun PreviewBubble() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            Bubble(content = {Text(text = "fgsdfgsdfg")}, isSender = true)
        }
    }
}
