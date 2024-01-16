package com.aariyan.chatbasedauthenticationsystem

import android.text.InputType
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aariyan.chatbasedauthenticationsystem.ui.theme.ChatBasedAuthenticationSystemTheme

@Composable
fun GreetingComponent(yesBtn: ()-> Unit, noBtn: ()-> Unit) {
    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .background(Color(0xffEEF8F5), RoundedCornerShape(8.dp)),
            //.padding(16.dp),
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
            style = TextStyle(color = Color(0xff52B69A), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        )

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp), horizontalArrangement = Arrangement.SpaceBetween){
            ElevatedButton(modifier = Modifier.weight(1f), onClick = { yesBtn() }) {
                Text(text = "হ্যাঁ", style = TextStyle(color = Color(0xff52B69A), fontSize = 16.sp, fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.padding(start = 25.dp))
            ElevatedButton(modifier = Modifier.weight(1f), onClick = { noBtn() }) {
                Text(text = "না", style = TextStyle(color = Color(0xff52B69A), fontSize = 16.sp, fontWeight = FontWeight.Bold))
            }
        }
    }
}


@Composable
fun AskIfAccountExistsScreenComponent() {
    val text = buildAnnotatedString {
        append("আচ্ছা!  যে নম্বর দিয়ে একাউন্টটি খুলেছ সে")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xff52B69A))) {
            append(" ফোন নম্বরটি ")
        }

        append("দাও প্লিজ...")
    }

    Text(text = text, style = TextStyle(color = Color(0xff71828A)))
}

@Composable
fun AskForPasswordScreenComponent() {
    val text = buildAnnotatedString {
        append("ধন্যবাদ, তোমার ফোন নম্বরটির বিপরীতে আমরা একটি একাউন্ট সনাক্ত করতে পেরেছি। এখন")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xff52B69A))) {
            append(" পাসওয়ার্ড ")
        }

        append("দিয়ে তোমার একাউন্টে প্রবেশ করতে পারো...")
    }

    Text(text = text, style = TextStyle(color = Color(0xff71828A)))
}


@Preview
@Composable
fun PreviewGreetingComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                AskIfAccountExistsScreenComponent()
            }
        }
    }
}

enum class KeyBoardInputType {
    TEXT, PHONE, PASSWORD, EMAIL, NUMBER
}
@Composable
fun CustomEditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    inputType: KeyBoardInputType = KeyBoardInputType.TEXT
) {

    val keyboardType = when(inputType) {
        KeyBoardInputType.TEXT -> KeyboardType.Text
        KeyBoardInputType.PHONE -> KeyboardType.Phone
        KeyBoardInputType.PASSWORD -> KeyboardType.Password
        KeyBoardInputType.EMAIL -> KeyboardType.Email
        KeyBoardInputType.NUMBER -> KeyboardType.Number
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier
            .padding(16.dp)
            //.background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color(0xff172B4D), fontSize = 16.sp),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.padding(10.dp)
            ) {
                if (value.isEmpty()) {
                    Text(placeholder, style = TextStyle(color = Color(0xff172B4D)))
                }
                innerTextField() // This will display the actual text field
            }
        }
    )
}

@Preview
@Composable
fun PreviewCustomEditText() {
    ChatBasedAuthenticationSystemTheme {
        CustomEditText(value = "dfgsdfg", onValueChange = {})
    }
}