package com.aariyan.chatbasedauthenticationsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.util.Calendar

@Composable
fun GreetingComponent(yesBtn: () -> Unit, noBtn: () -> Unit) {
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
fun UnregisterUserWaitingNumberScreenComponent() {
    val text = buildAnnotatedString {
        append("ধন্যবাদ, একাউন্টে প্রবেশ করতে তোমার")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xff52B69A))) {
            append(" ফোন নম্বরটি ")
        }

        append("দাও প্লিজ...")
    }

    Text(text = text, style = TextStyle(color = Color(0xff71828A)))
}

@Composable
fun SendOTPScreenComponent(phoneNumber: String, viewModel: ChatViewModel) {
    val otpTimerFinished by viewModel.otpTimerFinished.collectAsState()
    val timeLeft = viewModel.otpTimerValue.collectAsState()

    val text = "ধন্যবাদ! ${phoneNumber} এই নম্বরে প্রেরিত কোডটি মেয়াদ উত্তীর্ণ হওয়ার আগে লিখো।"

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
fun OTPValidationCompletedComponent(onStart: () -> Unit) {

    val text =
        "ধন্যবাদ, নিবন্ধনের প্রথম ধাপ সম্পন্ন হয়েছে, দ্বিতীয় ধাপে তোমার প্রোফাইল আপডেট করতে কিছু তথ্য প্রয়োজন"

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

//@Preview
//@Composable
//fun PreviewSendOTPScreenComponent() {
//    ChatBasedAuthenticationSystemTheme {
//        OTPValidationCompletedComponent(onStart = {})
//    }
//}

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


//@Preview
//@Composable
//fun PreviewGreetingComponent() {
//    BoxWithConstraints {
//        ChatBasedAuthenticationSystemTheme {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.White)
//            ) {
//                AskIfAccountExistsScreenComponent()
//            }
//        }
//    }
//}

@Composable
fun AuthInfoPreviewInLastSteps() {
    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .background(Color(0xffEEF8F5), RoundedCornerShape(8.dp)),
        //.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Mehedi, তোমার প্রদত্ত তথ্যগুলো",
            style = TextStyle(color = Color(0xff71828A), fontSize = 16.sp)
        )

        Text(
            text = "সঠিক কিনা আর একবার দেখে নাও ",
            style = TextStyle(color = Color(0xff52B69A), fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.padding(top = 25.dp))


        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp)) {
            Text(text = "নামঃ ")
            Text(text = "Mehedi Hassan", color = Color(0xff52B69A))
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp)) {
            Text(text = "জেন্ডারঃ ")
            Text(text = "ছেলে", color = Color(0xff52B69A))
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp)) {
            Text(text = "শ্রেণীঃ ")
            Text(text = "এইচএসসি", color = Color(0xff52B69A))
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp)) {
            Text(text = "বিভাগঃ ")
            Text(text = "বিজ্ঞান", color = Color(0xff52B69A))
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp)) {
            Text(text = "ব্যাচঃ ")
            Text(text = "এইচএসসি ২০২৫", color = Color(0xff52B69A))
        }


        ElevatedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 15.dp), onClick = { }) {
            Text(
                text = "সব ঠিক আছে",
                style = TextStyle(
                    color = Color(0xff52B69A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        ElevatedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp), onClick = { }) {
            Text(
                text = "পরিবর্তন করতে চাই",
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
fun RoundedDivider(modifier: Modifier = Modifier, color: Color = Color(0xffCCEAE1)) {
    Box(
        modifier = modifier
            .height(4.dp)
            .background(color, RoundedCornerShape(50))
    )
}


@Composable
fun ASkForNameComponent(
    textInput: String,
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(top = 15.dp)
            .fillMaxWidth()
            .background(Color(0xffEEF8F5), RoundedCornerShape(8.dp)),
    ) {
        NameGenderComponent(0, textInput = textInput, selectedGender, onGenderSelected)
        //ClassSectionSelectionComponent(selectedPosition = 1)
        //BatchSelectionComponent(2)
        //SetPasswordComponent(selectedPosition = 3)
        //ConfirmPasswordComponent(selectedPosition = 3)
    }
}

@Composable
fun SetPasswordComponent(selectedPosition: Int) {
    Column {
        Text(
            text = "ধন্যবাদ, Mehedi তোমার পাসওয়ার্ড সেট করো",
            style = TextStyle(color = Color(0xff71828A), fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))
        HandleDividerSelection(selectedPosition = selectedPosition)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            text = "ABCDfgdf"
        )

    }
}

@Composable
fun ConfirmPasswordComponent(selectedPosition: Int) {
    Column {
        Text(
            text = "ধন্যবাদ, Mehedi তোমার পাসওয়ার্ড টি পুনরায় দাও",
            style = TextStyle(color = Color(0xff71828A), fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))
        HandleDividerSelection(selectedPosition = selectedPosition)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            text = "ABCDfgdf"
        )

        Row(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
        ) {
            Icon(Icons.Default.Info, tint = Color.Red, contentDescription = "")
            Spacer(modifier = Modifier.padding(start = 10.dp))
            Text(text = "পাসওয়ার্ডটি কোথাও ভুল হচ্ছে!", color = Color.Red)
        }

        ElevatedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp), onClick = {
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

@Composable
fun BatchSelectionComponent(selectedPosition: Int) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val items =
        (currentYear..currentYear + 3).map { "SSC ${it.toString()}" } // List of years as strings
    var selectedItem by remember { mutableStateOf<String?>("SSC 2026") }


    Column {
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
                isSelected = selectedItem == item,
                onSelect = { selectedItem = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
            )
        }


        ElevatedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp), onClick = {
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

@Composable
fun ClassSectionSelectionComponent(selectedPosition: Int, selectedOption: String, onOptionSelected: (String) -> Unit,
                                   selectedClass: String, onClassSelected: (String) -> Unit, submitInfo: ()-> Unit) {
    val items = listOf("৬ষ্ট", "৭ম", "৮ম", "এসএসসি", "এইচএসসি")
    //var selectedItem by remember { mutableStateOf<String?>("এইচএসসি") }

    Column {
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
                    //.padding(horizontal = 8.dp),
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

@Composable
@Preview
fun PreviewClassSection() {
    ChatBasedAuthenticationSystemTheme {
        //ClassSectionSelectionComponent(1)
    }
}

@Composable
fun SelectableItem(
    text: String,
    isSelected: Boolean,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color(0xFFCCEAE1) else Color.White
    val textColor = if (isSelected) Color(0xff52B69A) else Color.Black
    val borderColor = if (isSelected) Color(0xff52B69A) else Color.White

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(4.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable { onSelect(text) }
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text, color = textColor)
    }
}

@Composable
fun NameGenderComponent(
    selectedPosition: Int,
    textInput: String,
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    Column {
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
fun SectionSelectionComponent(selectedOption: String, onOptionSelected: (String) -> Unit) {
    //var selectedOption by remember { mutableStateOf("বিজ্ঞান") }
    val options = listOf("বিজ্ঞান", "ব্যবসায় শিক্ষা", "মানবিক")

    LazyRow {
        items(options) { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xff52B69A),
                        unselectedColor = Color.White
                    )
                )
                Text(text = option)
            }
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


@Composable
fun HandleDividerSelection(selectedPosition: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0 until 4) {
            RoundedDivider(
                color = if (i == selectedPosition) Color(0xff52B69A) else Color(0xffCCEAE1),
                modifier = Modifier.weight(1f)
            )

            if (i < 3) { // Avoid adding a spacer after the last divider
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewGreetingComponent() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            //ASkForNameComponent("")
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////

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

    val keyboardType = when (inputType) {
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

//@Preview
//@Composable
//fun PreviewCustomEditText() {
//    ChatBasedAuthenticationSystemTheme {
//        CustomEditText(value = "dfgsdfg", onValueChange = {})
//    }
//}
