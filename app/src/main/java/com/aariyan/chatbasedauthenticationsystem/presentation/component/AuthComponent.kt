package com.aariyan.chatbasedauthenticationsystem.presentation.component

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aariyan.chatbasedauthenticationsystem.ChatViewModel
import com.aariyan.chatbasedauthenticationsystem.HandleDividerSelection
import com.aariyan.chatbasedauthenticationsystem.PasswordText
import com.aariyan.chatbasedauthenticationsystem.SectionSelectionComponent
import com.aariyan.chatbasedauthenticationsystem.SelectableItem
import com.aariyan.chatbasedauthenticationsystem.presentation.component.dummy.FilledButton
import com.aariyan.chatbasedauthenticationsystem.ui.theme.ChatBasedAuthenticationSystemTheme
import java.util.Calendar
import java.util.concurrent.ConcurrentLinkedDeque

@Composable
fun AskAccountExistOrNotComponent(yesBtn: () -> Unit, noBtn: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 15.dp)
            .fillMaxWidth(),
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
            FilledButton(
                modifier = Modifier.weight(1f),
                whenClicked = { yesBtn() },
                buttonTitle = "হ্যাঁ",
                fillColor = Color.White,
                buttonTitleColor = Color(0xff52B69A),
                titleFontSize = 16.sp
            )
            Spacer(modifier = Modifier.padding(start = 25.dp))
            FilledButton(
                modifier = Modifier.weight(1f),
                whenClicked = { noBtn() },
                buttonTitle = "না",
                fillColor = Color.White,
                buttonTitleColor = Color(0xff52B69A),
                titleFontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
fun PreviewGreetingComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            Column() {
                AskAccountExistOrNotComponent(yesBtn = { }, noBtn = {})
            }
        }
    }
}

@Composable
fun LogInPhoneNumberEnteringComponent() {
    val text = buildAnnotatedString {
        append("আচ্ছা!  যে নম্বর দিয়ে একাউন্টটি খুলেছ সে")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xff52B69A))) {
            append(" ফোন নম্বরটি ")
        }

        append("দাও প্লিজ...")
    }
    Text(
        modifier = Modifier.padding(start = 15.dp),
        text = text,
        style = TextStyle(color = Color(0xff71828A))
    )
}

@Preview
@Composable
fun PreviewLogInPhoneNumberEnteringComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            LogInPhoneNumberEnteringComponent()
        }
    }
}

@Composable
fun AskForLogInPasswordScreenComponent() {
    val text = buildAnnotatedString {
        append("ধন্যবাদ, তোমার ফোন নম্বরটির বিপরীতে আমরা একটি একাউন্ট সনাক্ত করতে পেরেছি। এখন")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xff52B69A))) {
            append(" পাসওয়ার্ড ")
        }

        append("দিয়ে তোমার একাউন্টে প্রবেশ করতে পারো...")
    }

    Text(
        modifier = Modifier.padding(start = 15.dp),
        text = text,
        style = TextStyle(color = Color(0xff71828A))
    )
}

@Preview
@Composable
fun PreviewAskForLogInPasswordScreenComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            AskForLogInPasswordScreenComponent()
        }
    }
}

@Composable
fun ConfirmRegPasswordComponent(selectedPosition: Int, password: String, studentName: String) {
    Column(
        modifier = Modifier.padding(start = 15.dp)
    ) {
        Text(
            text = "ধন্যবাদ, $studentName তোমার পাসওয়ার্ড টি পুনরায় দাও",
            style = TextStyle(color = Color(0xff71828A), fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))
        HandleDividerSelection(selectedPosition = selectedPosition)
        PasswordText(password = password)

        Row(
            modifier = Modifier.padding(bottom = 10.dp, top = 5.dp)
        ) {
            Icon(Icons.Default.Info, tint = Color.Red, contentDescription = "")
            Spacer(modifier = Modifier.padding(start = 10.dp))
            Text(text = "পাসওয়ার্ডটি কোথাও ভুল হচ্ছে!", color = Color.Red)
        }

        ElevatedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(), onClick = {
        }) {
            Text(
                text = "নতুন করে পাসওয়ার্ড সেট করো",
                style = TextStyle(
                    color = Color(0xff52B69A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewConfirmPasswordComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            ConfirmRegPasswordComponent(3, "", "HADY")
        }
    }
}

@Composable
fun SetRegPasswordComponent(selectedPosition: Int, password: String, studentName: String) {
    Column(
        modifier = Modifier.padding(start = 15.dp)
    ) {
        Text(
            text = "ধন্যবাদ, $studentName তোমার পাসওয়ার্ড সেট করো",
            style = TextStyle(color = Color(0xff71828A), fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))
        HandleDividerSelection(selectedPosition = selectedPosition)
        PasswordText(password = password)
    }
}

@Preview
@Composable
fun PreviewSetRegPasswordComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            SetRegPasswordComponent(3, "", "HADY")
        }
    }
}

@Composable
fun BatchSelectionComponent(
    selectedPosition: Int,
    selectedBatchYear: String,
    onBatchYearSelected: (String) -> Unit, onSubmitClick: () -> Unit
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val items =
        (currentYear..currentYear + 3).map { "SSC ${it.toString()}" } // List of years as strings
    //var selectedItem by remember { mutableStateOf<String?>("SSC 2026") }


    Column(modifier = Modifier.padding(start = 15.dp)) {
        Text(
            text = "ধন্যবাদ, Mehedi তোমার ব্যাচ সিলেক্ট করো",
            style = TextStyle(color = Color(0xff71828A), fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))
        HandleDividerSelection(selectedPosition = selectedPosition)

        Spacer(modifier = Modifier.padding(top = 8.dp))
        items.forEach { item ->
            SelectableItem(
                text = item,
                isSelected = selectedBatchYear == item,
                onSelect = { onBatchYearSelected(item) },
            )
        }


        ElevatedButton(modifier = Modifier
            .fillMaxWidth(), onClick = {
            onSubmitClick()
        }) {
            Text(
                text = "Submit",
                style = TextStyle(
                    color = Color(0xff52B69A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewBatchSelectionComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            BatchSelectionComponent(
                selectedPosition = 2,
                selectedBatchYear = "",
                onBatchYearSelected = {}
            ) {

            }
        }
    }
}

@Composable
fun ClassNSectionComponent(
    selectedPosition: Int, selectedOption: String, onOptionSelected: (String) -> Unit,
    selectedClass: String, onClassSelected: (String) -> Unit, submitInfo: () -> Unit
) {
    val items = listOf("৬ষ্ট", "৭ম", "৮ম", "এসএসসি", "এইচএসসি")

    Column(
        modifier = Modifier.padding(start = 15.dp)
    ) {
        Text(
            text = "অভিনন্দন Abdullah, নিচে ক্লাস সিলেক্ট করো",
            style = TextStyle(color = Color(0xff71828A), fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))
        HandleDividerSelection(selectedPosition = selectedPosition)

        Spacer(modifier = Modifier.padding(top = 8.dp))
        items.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowItems.forEach { item ->
                    SelectableItem(
                        text = item,
                        isSelected = selectedClass == item,
                        onSelect = { onClassSelected(item) },
                        modifier = Modifier.weight(1f) // Equal weight for each item
                    )
                }
            }
        }


        // Conditionally display the button
        if (selectedClass == "এসএসসি" || selectedClass == "এইচএসসি") {
            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = "তোমার বিভাগ নির্বাচন করো", color = Color(0xff71828A)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionSelectionComponent(selectedOption, onOptionSelected)
            }

        }

        ElevatedButton(modifier = Modifier
            .fillMaxWidth(), onClick = {
            submitInfo()
        }) {
            Text(
                text = "সাবমিট",
                style = TextStyle(
                    color = Color(0xff52B69A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewClassNSectionComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            ClassNSectionComponent(
                selectedPosition = 1,
                selectedOption = "",
                selectedClass = "",
                onClassSelected = {},
                onOptionSelected = {}, submitInfo = {}
            )
        }
    }
}

@Composable
fun OTPValidationCompletedComponent(onStart: () -> Unit) {

    val text =
        "ধন্যবাদ, নিবন্ধনের প্রথম ধাপ সম্পন্ন হয়েছে, দ্বিতীয় ধাপে তোমার প্রোফাইল আপডেট করতে কিছু তথ্য প্রয়োজন"

    Column(
        modifier = Modifier
            //.padding(15.dp)
            .fillMaxWidth()
            .background(Color(0xffEEF8F5), RoundedCornerShape(8.dp))
            .padding(start = 15.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = text,
            style = TextStyle(color = Color(0xff71828A), fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.padding(top = 25.dp))


        ElevatedButton(modifier = Modifier.fillMaxWidth(), onClick = {
            onStart()
        }) {
            Text(
                text = "ঠিক আছে, শুরু করা যাক...",
                style = TextStyle(
                    color = Color(0xff52B69A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

    }
}

@Preview
@Composable
fun PreviewOTPValidationCompletedComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            OTPValidationCompletedComponent(onStart = {})
        }
    }
}

@Composable
fun SendOTPScreenComponent(phoneNumber: String, viewModel: ChatViewModel) {
    val otpTimerFinished by viewModel.otpTimerFinished.collectAsState()
    val timeLeft = viewModel.otpTimerValue.collectAsState()

    val text = "ধন্যবাদ! $phoneNumber এই নম্বরে প্রেরিত কোডটি মেয়াদ উত্তীর্ণ হওয়ার আগে লিখো।"

    Column(
        modifier = Modifier
            //.padding(15.dp)
            .fillMaxWidth()
            .background(Color(0xffEEF8F5), RoundedCornerShape(8.dp))
            .padding(5.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = text,
            style = TextStyle(color = Color(0xff71828A), fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.padding(top = 25.dp))

        Text(
            text = "কোডটির মেয়াদ ${timeLeft.value / 60}:${timeLeft.value % 60}",
            style = TextStyle(color = Color.Red, fontSize = 16.sp)
        )

        if (otpTimerFinished) {
            ElevatedButton(modifier = Modifier.fillMaxWidth(), onClick = {
                viewModel.resendOtp()
            }) {
                Text(
                    text = "নতুন কোড চাই",
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

@Composable
@Preview
fun PreviewSendOTPScreenComponent() {
    val viewModel = viewModel<ChatViewModel>()

    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            SendOTPScreenComponent("dfgdgfsdfg", viewModel)
        }
    }
}

@Composable
fun UnregisterUserWaitingNumberScreenComponent() {
    val text = buildAnnotatedString {
        append("ধন্যবাদ, একাউন্টে প্রবেশ করতে তোমার")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xff52B69A))) {
            append(" ফোন নম্বরটি ")
        }

        append("দাও প্লিজ...")
    }

    Text(
        modifier = Modifier.padding(start = 15.dp),
        text = text,
        style = TextStyle(color = Color(0xff71828A))
    )
}

@Preview
@Composable
fun PreviewUnregisterUserWaitingNumberScreenComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            UnregisterUserWaitingNumberScreenComponent()
        }
    }
}

@Composable
fun NameGenderComponent(
    selectedPosition: Int,
    textInput: String,
    selectedGender: String,
    onGenderSelected: (String) -> Unit,
    onSubmitClick: ()-> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xffEEF8F5), RoundedCornerShape(8.dp))
            .padding(start = 15.dp),
    ){
        Text(
            text = "তোমার নাম লিখো - ইংরেজিতে",
            style = TextStyle(color = Color(0xff71828A), fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))
        HandleDividerSelection(selectedPosition = selectedPosition)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
            text = textInput
        )

        Row(
            //modifier = Modifier.padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "জেন্ডার:")
            RadioBtnComponent(selectedOption = selectedGender, onOptionSelected = onGenderSelected)
        }

        ElevatedButton(modifier = Modifier
            .fillMaxWidth(),
            onClick = {
                onSubmitClick()
            }) {
            Text(
                text = "সাবমিট",
                style = TextStyle(
                    color = Color(0xff52B69A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun RadioBtnComponent(selectedOption: String, onOptionSelected: (String) -> Unit) {
    val options = listOf("ছেলে", "মেয়ে")

    options.forEach { option ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            RadioButton(
                selected = option == selectedOption,
                onClick = { onOptionSelected(option) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xff52B69A),
                    unselectedColor = Color.Gray
                )
                // Optionally, add colors or other styling
            )
            Text(text = option, modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Preview
@Composable
fun PreviewNameGenderComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            NameGenderComponent(
                0,
                "",
                "",
                onGenderSelected = {},
                onSubmitClick = {}
            )
        }
    }
}
