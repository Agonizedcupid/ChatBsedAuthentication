package com.aariyan.chatbasedauthenticationsystem

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aariyan.chatbasedauthenticationsystem.presentation.component.AskAccountExistOrNotScreen
import com.aariyan.chatbasedauthenticationsystem.presentation.component.AskForBatchSelectionScreen
import com.aariyan.chatbasedauthenticationsystem.presentation.component.AskForClassNSectionScreen
import com.aariyan.chatbasedauthenticationsystem.presentation.component.AskForConfirmRegPasswordScreen
import com.aariyan.chatbasedauthenticationsystem.presentation.component.AskForLogInPasswordScreenComponent
import com.aariyan.chatbasedauthenticationsystem.presentation.component.AskForNameScreen
import com.aariyan.chatbasedauthenticationsystem.presentation.component.AskForRegPasswordScreen
import com.aariyan.chatbasedauthenticationsystem.presentation.component.LogInPasswordEnteringScreen
import com.aariyan.chatbasedauthenticationsystem.presentation.component.OTPValidationCompletedScreen
import com.aariyan.chatbasedauthenticationsystem.presentation.component.SendOTPScreen
import com.aariyan.chatbasedauthenticationsystem.presentation.component.UnregisterUserPhoneNumberScreen
import com.aariyan.chatbasedauthenticationsystem.ui.theme.ChatBasedAuthenticationSystemTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthAppBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularShape(
                    modifier = Modifier,
                    strokeColor = Color.Transparent,
                    centeredIcon = R.drawable.utkorsho_icon,
                    backgroundColor = Color(0xffE5F4F0),
                    iconTint = Color(0xff52B69A), whenClick = {}
                )
                Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
                Text(
                    text = "উৎকর্ষ সহযোগী",
                    color = Color(0xff52B69A),
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Handle click */ }) {
                Icon(Icons.Filled.Refresh, contentDescription = "More", tint = Color(0xff52B69A))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(Color(0xffEEF8F5))
    )
}

@Preview
@Composable
fun PreviewAppBar() {
    ChatBasedAuthenticationSystemTheme {
        AuthAppBar()
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                topBar = { AuthAppBar() }
            ) { innerPadding ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    ChatBased()
                }

            }

        }
    }
}

@Preview
@Composable
fun PreviewFullScreen() {
    ChatBasedAuthenticationSystemTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            ChatBased()
        }
    }
}

@Composable
fun ChatBased() {
    val viewModel = viewModel<ChatViewModel>()
    val chatState by viewModel.chatState.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val showTextField by viewModel.showTextField.collectAsState()
    var textInput by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("ছেলে") }
    var selectedClass by remember { mutableStateOf("") }
    var selectedSection by remember { mutableStateOf("") }
    var selectedBatchYear by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 15.dp)
        ) {

            items(messages) { message ->
                ChatBubble(message.text, isUserMessage = message.isUserMessage)
            }

            item {
                when (chatState) {
                    is ChatUiState.Loading -> LoadingScreen()
                    // Completed
                    is ChatUiState.Greeting -> AskAccountExistOrNotScreen(
                        yesBtn = {
                            viewModel.handleUserInput(
                                "জি, আমার একাউন্ট আছে!",
                                responseType = ResponseType.YES
                            )
                        },
                        noBtn = {
                            viewModel.handleUserInput(
                                "দুঃখিত, আমার কোনো একাউন্ট নেই!",
                                responseType = ResponseType.NO
                            )
                        }
                    )

                    // Completed
                    is ChatUiState.AskForName -> AskForNameScreen(
                        textInput,
                        selectedGender,
                        onGenderSelected = { newGender ->
                            selectedGender = newGender
                            viewModel.updateGender(newGender)
                        }, onSubmitClick = {
                            if (!viewModel.isValidName(textInput)) {
                                Toast.makeText(context, "নামটি বৈধ নয়", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                viewModel.handleUserInput("আমার নাম: ${textInput}, এবং জেন্ডার:  ${selectedGender}")
                            }
                        }
                    )

                    is ChatUiState.AskIfAccountExists -> AskIfAccountExistsScreen()
                    // Completed
                    is ChatUiState.WaitForPhoneNumber -> WaitForPhoneNumberScreen()
                    // Completed, TODO: AskForPassword should be renamed to AskForLogInPassword
                    is ChatUiState.AskForPassword -> LogInPasswordEnteringScreen()

                    is ChatUiState.PasswordRetry -> PasswordRetryScreen()
                    is ChatUiState.PasswordReset -> PasswordResetScreen()
                    is ChatUiState.WaitForOTP -> WaitForOTPScreen()
                    is ChatUiState.AccountCreation -> AccountCreationScreen()
                    is ChatUiState.AskForGender -> AskForGenderScreen()
                    is ChatUiState.AskForClass -> AskForClassScreen()
                    is ChatUiState.AskForDivisionOrSection -> AskForDivisionOrSectionScreen()
                    is ChatUiState.AskForBatch -> AskForBatchScreen()
                    is ChatUiState.SetPassword -> SetPasswordScreen()


                    is ChatUiState.AuthenticationComplete -> AuthenticationCompleteScreen()
                    is ChatUiState.Error -> ErrorScreen((chatState as ChatUiState.Error).exception.message)
                    is ChatUiState.Success -> SuccessScreen((chatState as ChatUiState.Success).message)


                    // Completed
                    is ChatUiState.WaitForPhoneNumberForUnRegisterUser -> UnregisterUserPhoneNumberScreen()

                    // Completed
                    is ChatUiState.SendOTP -> SendOTPScreen(viewModel, "")

                    // Completed
                    ChatUiState.OTPValidationCompleted -> OTPValidationCompletedScreen(
                        onStart = {
                            viewModel.handleUserInput(userInput = "ঠিক আছে, শুরু করা যাক...")
                        }
                    )
                    // Completed
                    is ChatUiState.AskForClassNSection -> AskForClassNSectionScreen(
                        selectedPosition = 1,
                        selectedOption = selectedSection,
                        selectedClass = selectedClass,
                        onClassSelected = {newClass->
                            selectedClass = newClass
                            viewModel.updateClass(newClass = newClass)
                        },
                        onOptionSelected = {newOption->
                            selectedSection = newOption
                            viewModel.updateSection(newSection = newOption)
                        }, submitInfo = {
                            viewModel.handleUserInput("আমি ${selectedClass} শ্রেণীতে, ${selectedSection} বিভাগে অধ্যায়নরত আছি")
                        }
                    )

                    // Completed
                    is ChatUiState.AskForBatchSection -> AskForBatchSelectionScreen(
                        selectedPosition = 2,
                        selectedBatchYear = selectedBatchYear,
                        onBatchYearSelected = {newYear->
                            selectedBatchYear = newYear
                            viewModel.updateBatchYear(newYear = newYear)
                        },
                        onSubmitClick = {
                            viewModel.handleUserInput("আমি ${selectedBatchYear} ব্যাচের একজন ছাত্র/ছাত্রী")
                        }
                    )

                    // Completed
                    is ChatUiState.AskForRegistrationPassword-> AskForRegPasswordScreen(
                        selectedPosition = 3,
                        password = textInput
                    )
                    //Completed
                    is ChatUiState.AskForConfirmRegistrationPassword-> AskForConfirmRegPasswordScreen(
                        selectedPosition = 3,
                        password = textInput
                    )
                }
            }
        }


        if (showTextField) {
            //Spacer(modifier = Modifier.weight(1f))
            Divider(color = Color(0xffF0F7FC), thickness = 1.dp)
            Row(
                modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(Color(0xffE5F4F0), RoundedCornerShape(4.dp))
                        .padding(10.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.feather_icon),
                        contentDescription = "",
                        tint = Color(0xff52B69A)
                    )
                }

                Spacer(modifier = Modifier.padding(start = 5.dp))

                if (chatState is ChatUiState.WaitForPhoneNumber ||
                    chatState is ChatUiState.WaitForPhoneNumberForUnRegisterUser ||
                    chatState is ChatUiState.SendOTP
                ) {
                    CustomEditText(
                        modifier = Modifier.weight(1f),
                        value = textInput,
                        placeholder = "এখানে লিখো ...",
                        inputType = KeyBoardInputType.NUMBER,
                        onValueChange = { newValue ->
                            textInput = newValue
                        }
                    )
                } else if (chatState is ChatUiState.AskForPassword) {
                    CustomEditText(
                        modifier = Modifier.weight(1f),
                        value = textInput,
                        placeholder = "এখানে লিখো ...",
                        inputType = KeyBoardInputType.PASSWORD,
                        onValueChange = { newValue ->
                            textInput = newValue
                        }
                    )
                } else if (chatState is ChatUiState.AskForName) {
                    CustomEditText(
                        modifier = Modifier.weight(1f),
                        value = textInput,
                        placeholder = "এখানে লিখো ...",
                        inputType = KeyBoardInputType.TEXT,
                        onValueChange = { newValue ->
                            textInput = newValue
                        }
                    )
                }

                CircularShape(
                    modifier = Modifier,
                    strokeColor = Color.Transparent,
                    centeredIcon = R.drawable.send_icon,
                    backgroundColor = Color(0xffE5F4F0),
                    iconTint = Color(0xff52B69A), whenClick = {

                        if (chatState is ChatUiState.WaitForPhoneNumber) {
                            if (!isValidPhoneNumber(textInput.trim())) {
                                viewModel.handleUserInput(textInput)
                                textInput = ""
                            } else {
                                Toast.makeText(context, "ফোন নম্বর বৈধ নয়", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        } else if (chatState is ChatUiState.AskForPassword) {
                            viewModel.handleUserInput(textInput)
                        } else if (chatState is ChatUiState.WaitForPhoneNumberForUnRegisterUser) {
                            if (!isValidPhoneNumber(textInput.trim())) {
                                viewModel.handleUserInput(textInput)
                                textInput = ""
                                viewModel.sendOtp()
                            } else {
                                Toast.makeText(context, "ফোন নম্বর বৈধ নয়", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else if (chatState is ChatUiState.SendOTP) {
                            viewModel.handleUserInput(textInput)
                        } else if (chatState is ChatUiState.AskForName) {
//                            if (!viewModel.isValidName(textInput)) {
//                                Toast.makeText(context, "নামটি বৈধ নয়", Toast.LENGTH_SHORT)
//                                    .show()
//                            } else {
//                                viewModel.handleUserInput("আমার নাম: ${textInput}, এবং জেন্ডার:  ${selectedGender}")
//                            }
                        } else {
                            viewModel.handleUserInput("")
                        }
                    }
                )

            }
        }
    }
}


//@Composable
//fun ChatBased() {
//    val viewModel = viewModel<ChatViewModel>()
//    val chatState by viewModel.chatState.collectAsState()
//    val messages by viewModel.messages.collectAsState()
//    val showTextField by viewModel.showTextField.collectAsState()
//    var textInput by remember { mutableStateOf("") }
//
//    val context = LocalContext.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(start = 15.dp, end = 15.dp, top = 10.dp)
//    ) {
//
//        //MessagesList(messages = messages)
//        Log.d("CHECK_SIZE", messages.size.toString())
//        LazyColumn() {
//            items(messages) { message ->
//                ChatBubble(message.text, isUserMessage = message.isUserMessage)
//            }
//        }
//
//        when (chatState) {
//            is ChatUiState.Loading -> LoadingScreen()
//            is ChatUiState.Greeting -> GreetingScreen(
//                yesBtn = {
//                    viewModel.handleUserInput(
//                        "জি, আমার একাউন্ট আছে!",
//                        responseType = ResponseType.YES
//                    )
//                },
//                noBtn = {
//                    viewModel.handleUserInput(
//                        "দুঃখিত, আমার কোনো একাউন্ট নেই!",
//                        responseType = ResponseType.NO
//                    )
//                }
//            )
//
//            is ChatUiState.AskForName -> AskForNameScreen()
//            is ChatUiState.AskIfAccountExists -> AskIfAccountExistsScreen()
//            is ChatUiState.WaitForPhoneNumber -> WaitForPhoneNumberScreen()
//            is ChatUiState.AskForPassword -> AskForPasswordScreen()
//            is ChatUiState.PasswordRetry -> PasswordRetryScreen()
//            is ChatUiState.PasswordReset -> PasswordResetScreen()
//            is ChatUiState.WaitForOTP -> WaitForOTPScreen()
//            is ChatUiState.AccountCreation -> AccountCreationScreen()
//            is ChatUiState.AskForGender -> AskForGenderScreen()
//            is ChatUiState.AskForClass -> AskForClassScreen()
//            is ChatUiState.AskForDivisionOrSection -> AskForDivisionOrSectionScreen()
//            is ChatUiState.AskForBatch -> AskForBatchScreen()
//            is ChatUiState.SetPassword -> SetPasswordScreen()
//            is ChatUiState.AuthenticationComplete -> AuthenticationCompleteScreen()
//            is ChatUiState.Error -> ErrorScreen((chatState as ChatUiState.Error).exception.message)
//            is ChatUiState.Success -> SuccessScreen((chatState as ChatUiState.Success).message)
//            is ChatUiState.WaitForPhoneNumberForUnRegisterUser -> UnregisterUserPhoneNumberScree()
//            is ChatUiState.SendOTP -> SendOTPScreen(viewModel)
//            ChatUiState.OTPValidationCompleted -> OTPValidationCompletedScreen(
//                onStart = {
//                    viewModel.handleUserInput(input = "ঠিক আছে, শুরু করা যাক...")
//                }
//            )
//        }
//
//        //Spacer(modifier = Modifier.weight(1f))
//
//        if (showTextField) {
//            //Spacer(modifier = Modifier.weight(1f))
//            Divider(color = Color(0xffF0F7FC), thickness = 1.dp)
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(45.dp)
//                        .background(Color(0xffE5F4F0), RoundedCornerShape(4.dp))
//                        .padding(10.dp)
//                ) {
//                    Icon(
//                        painterResource(id = R.drawable.feather_icon),
//                        contentDescription = "",
//                        tint = Color(0xff52B69A)
//                    )
//                }
//
//                Spacer(modifier = Modifier.padding(start = 5.dp))
//
//                if (chatState is ChatUiState.WaitForPhoneNumber ||
//                    chatState is ChatUiState.WaitForPhoneNumberForUnRegisterUser ||
//                    chatState is ChatUiState.SendOTP
//                ) {
//                    CustomEditText(
//                        modifier = Modifier.weight(1f),
//                        value = textInput,
//                        placeholder = "এখানে লিখো ...",
//                        inputType = KeyBoardInputType.NUMBER,
//                        onValueChange = { newValue ->
//                            textInput = newValue
//                        }
//                    )
//                } else if (chatState is ChatUiState.AskForPassword) {
//                    CustomEditText(
//                        modifier = Modifier.weight(1f),
//                        value = textInput,
//                        placeholder = "এখানে লিখো ...",
//                        inputType = KeyBoardInputType.PASSWORD,
//                        onValueChange = { newValue ->
//                            textInput = newValue
//                        }
//                    )
//                }
//
//                CircularShape(
//                    modifier = Modifier,
//                    strokeColor = Color.Transparent,
//                    centeredIcon = R.drawable.send_icon,
//                    backgroundColor = Color(0xffE5F4F0),
//                    iconTint = Color(0xff52B69A), whenClick = {
//
//                        if (chatState is ChatUiState.WaitForPhoneNumber) {
//                            if (!isValidPhoneNumber(textInput.trim())) {
//                                viewModel.handleUserInput(textInput)
//                                textInput = ""
//                            } else {
//                                Toast.makeText(context, "ফোন নম্বর বৈধ নয়", Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//
//                        } else if (chatState is ChatUiState.AskForPassword) {
//                            viewModel.handleUserInput(textInput)
//                        } else if (chatState is ChatUiState.WaitForPhoneNumberForUnRegisterUser) {
//                            if (!isValidPhoneNumber(textInput.trim())) {
//                                viewModel.handleUserInput(textInput)
//                                textInput = ""
//                                viewModel.sendOtp()
//                            } else {
//                                Toast.makeText(context, "ফোন নম্বর বৈধ নয়", Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//                        } else if (chatState is ChatUiState.SendOTP) {
//                            viewModel.handleUserInput(textInput)
//
//                        }
//                    }
//                )
//
//            }
//        }
//
//    }
//}

fun isValidPhoneNumber(number: String): Boolean {
    val pattern = "^01[3-9]\\d{8}$".toRegex()
    return number.matches(pattern)
}


/**
 * Animation Based Code, Working super fine
 */


@Composable
fun MessagesList(messages: List<Message>) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Track the size of the messages list
    val listSize by remember { derivedStateOf { messages.size } }

    // Scroll to the bottom when a new message is added
    LaunchedEffect(listSize) {
        coroutineScope.launch {
            scrollState.scrollToItem(index = max(0, listSize - 1))
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), state = scrollState) {
        items(messages) { message ->
            ChatBubble(message.text, isUserMessage = message.isUserMessage)
        }
    }
}


@Composable
fun ChatBubble(text: String, isUserMessage: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
    ) {
        //Bubble(message = text, isSender = false)
        BubbleWithIcon(isUserMessage = isUserMessage) {
            //Bubble(content = { Text(text = text) }, isSender = isUserMessage)
            Text(modifier = Modifier.padding(start = 5.dp, end = 5.dp),text = text, style = TextStyle(color = Color(0xff71828A)))
        }
    }
}

@Composable
fun BubbleWithIcon(isUserMessage: Boolean, content: @Composable () -> Unit) {
    Row(
        //verticalAlignment = Alignment.CenterVertically
    ) {

        if (!isUserMessage) {
            SenderIdentity(isUserMessage = isUserMessage)
        }
        Bubble(content, isSender = isUserMessage)
        //content()
        if (isUserMessage) {
            SenderIdentity(isUserMessage = isUserMessage)
        }

    }
}

data class Message(val text: String, val isUserMessage: Boolean, val isTyping: Boolean = false)

@Composable
fun SenderIdentity(isUserMessage: Boolean) {
    if (isUserMessage) {
        Spacer(modifier = Modifier.padding(start = 5.dp))
        CircularShape(
            modifier = Modifier,
            strokeColor = Color.Transparent,
            centeredIcon = R.drawable.utkorsho_icon,
            backgroundColor = Color(0xffE5F4F0),
            iconTint = Color(0xff52B69A), whenClick = {}
        )
    } else {
        CircularShape(
            modifier = Modifier,
            strokeColor = Color.Transparent,
            centeredIcon = R.drawable.utkorsho_icon,
            backgroundColor = Color(0xffE5F4F0),
            iconTint = Color(0xff52B69A), whenClick = {}
        )
        Spacer(modifier = Modifier.padding(start = 5.dp))
    }
}


//@Preview
//@Composable
//fun PreviewBubbleWithIcon() {
//    BoxWithConstraints {
//        ChatBasedAuthenticationSystemTheme {
//            BubbleWithIcon(content = { Text(text = "Hello World") }, isUserMessage = true)
//        }
//    }
//}

@Preview
@Composable
fun PreviewChatBubble() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            ChatBubble(text = "Hello How are you", isUserMessage = true)
        }
    }
}

//
//@Composable
//fun ChatBasedAuthentication() {
//    var textInput by remember { mutableStateOf("") }
//    var messages = remember { mutableStateOf(listOf(Message("Hello, what's your first name?", false))) }
//    val askingForFirstName = remember { mutableStateOf(true) }
//    var showTypingIndicator = remember { mutableStateOf(false) }
//    val coroutineScope = rememberCoroutineScope()
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        MessagesList(messages = messages.value)
//
//        if (showTypingIndicator.value) {
//            TypingIndicator()
//        }
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//        ) {
//            TextField(
//                value = textInput,
//                onValueChange = { textInput = it },
//                modifier = Modifier.weight(1f),
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
//                keyboardActions = KeyboardActions(onSend = {
//                    if (textInput.isNotEmpty()) {
//                        messages.value = messages.value + Message("You: ${textInput}", true)
//                        showTypingIndicator.value = true
//                        coroutineScope.launch {
//                            simulateTypingResponse(
//                                askingForFirstName,
//                                messages,
//                                textInput,
//                                showTypingIndicator,
//                            )
//                            textInput = ""
//                        }
//                    }
//                })
//            )
//
//            Button(
//                onClick = {
//                    if (textInput.isNotEmpty()) {
//                        messages.value = messages.value + Message("You: $textInput", true)
//                        showTypingIndicator.value = true
//                        coroutineScope.launch {
//                            simulateTypingResponse(askingForFirstName, messages, textInput, showTypingIndicator)
//                            textInput = ""
//                        }
//                    }
//                }
//            ) {
//                Text("Send")
//            }
//        }
//    }
//}
suspend fun simulateTypingResponse(
    askingForFirstName: MutableState<Boolean>,
    messages: MutableState<List<Message>>,
    inputText: String,
    showTypingIndicator: MutableState<Boolean>
) {
    delay(1000) // Simulate typing delay

//    val responseText = if (askingForFirstName.value) {
//        askingForFirstName.value = false
//        "Nice to meet you, $inputText! What's your last name?"
//    } else {
//        "Thanks, $inputText! Authentication complete."
//    }

    // Add an initial 'typing' message
    var typingMessage = Message("", false, isTyping = true)
    messages.value = messages.value + typingMessage

    for (char in inputText) {
        typingMessage = typingMessage.copy(text = typingMessage.text + char)
        // Update the last message in the list to show the typing effect
        messages.value = messages.value.dropLast(1) + typingMessage
        delay(50) // Delay between each character
    }

    // Replace the 'typing' message with the complete response
    messages.value = messages.value.dropLast(1) + typingMessage.copy(isTyping = false)

    showTypingIndicator.value = false
}

//
//
//@Composable
//fun TypingIndicator() {
//    Row(
//        modifier = Modifier.padding(8.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // Pen Icon
//        Icon(
//            imageVector = Icons.Filled.Create,
//            contentDescription = "Writing",
//            modifier = Modifier
//                .size(24.dp)
//                .padding(end = 8.dp)
//        )
//
//        val infiniteTransition = rememberInfiniteTransition(label = "")
//        for (i in 0..2) {
//            val offset by infiniteTransition.animateFloat(
//                initialValue = 0f,
//                targetValue = 8f,
//                animationSpec = infiniteRepeatable(
//                    animation = tween(500, easing = FastOutSlowInEasing),
//                    repeatMode = RepeatMode.Reverse,
//                    initialStartOffset = StartOffset(i * 150)
//                ), label = ""
//            )
//
//            Box(
//                modifier = Modifier
//                    .size(8.dp)
//                    .offset { IntOffset(0, -offset.roundToInt()) }
//                    .background(Color.Gray, CircleShape)
//                    .padding(horizontal = 1.dp)
//            )
//        }
//    }
//}
@Preview
@Composable
fun DefaultPreview() {
    //ChatBasedAuthentication()
}

/**
 * Animation Done
 */


@Composable
fun CircularShape(
    modifier: Modifier,
    circleSize: Dp = 50.dp,
    strokeColor: Color,
    strokeWidth: Dp = 0.5.dp,
    centeredIcon: Int,
    backgroundColor: Color,
    iconTint: Color,
    whenClick: () -> Unit
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(circleSize) // Size of the circle
            //.background(backgroundColor, shape = CircleShape)
            .border(
                width = strokeWidth,
                color = strokeColor,
                shape = RoundedCornerShape(size = 100.dp)
            )
            .width(circleSize)
            .height(circleSize)
            .background(color = backgroundColor, shape = RoundedCornerShape(size = 100.dp))
            .clickable(true, onClick = whenClick)
    ) {
        Icon(
            painterResource(id = centeredIcon), // Replace with your desired icon
            contentDescription = "Icon in the middle",
            tint = iconTint // Icon color
        )

    }
}

@Preview
@Composable
fun PreviewCircle() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            CircularShape(
                modifier = Modifier,
                strokeColor = Color.Transparent,
                centeredIcon = R.drawable.utkorsho_icon,
                backgroundColor = Color(0xffE5F4F0),
                iconTint = Color(0xff52B69A), whenClick = {}
            )
        }
    }
}