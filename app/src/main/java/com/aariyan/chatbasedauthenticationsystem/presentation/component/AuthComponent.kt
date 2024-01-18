package com.aariyan.chatbasedauthenticationsystem.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aariyan.chatbasedauthenticationsystem.ui.theme.ChatBasedAuthenticationSystemTheme
import java.util.concurrent.ConcurrentLinkedDeque

@Composable
fun GreetingComponent(yesBtn: () -> Unit, noBtn: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .background(Color(0xffEEF8F5), RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "আমি উৎকর্ষ সহযোগী বলছি! :)",
            style = TextStyle(color = Color(0xff71828A), fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.padding(top = 25.dp))
        Text(
            text = "এপটি লগ-ইন করা পর্যন্ত আমি তোমাকে সাহায্য করবো। আশা করি আমাকেও তুমি তথ্য দিয়ে সহযোগিতা করবে।",
            style = TextStyle(color = Color(0xff71828A), fontSize = 16.sp)
        )

        Text(
            text = "তোমার কি উৎকর্ষ একাউন্ট আছে?",
            style = TextStyle(
                color = Color(0xff52B69A),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ElevatedButton(modifier = Modifier.weight(1f), onClick = { yesBtn() }) {
                Text(
                    text = "হ্যাঁ",
                    style = TextStyle(
                        color = Color(0xff52B69A),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.padding(start = 25.dp))
            ElevatedButton(modifier = Modifier.weight(1f), onClick = { noBtn() }) {
                Text(
                    text = "না",
                    style = TextStyle(
                        color = Color(0xff52B69A),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewGreetingComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            Column(modifier = Modifier.fillMaxSize()) {
                GreetingComponent(yesBtn = { }, noBtn = {})
            }
        }
    }
}