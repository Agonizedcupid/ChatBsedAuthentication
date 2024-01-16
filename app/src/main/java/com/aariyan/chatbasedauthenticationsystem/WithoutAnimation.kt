package com.aariyan.chatbasedauthenticationsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aariyan.chatbasedauthenticationsystem.ui.theme.ChatBasedAuthenticationSystemTheme

//@Composable
//fun ChatBasedAuth() {
//    val viewModel = viewModel<ChatViewModel>()
//    val chatState by viewModel.chatState.collectAsState()
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        when (chatState) {
//            is ChatUiState.Loading -> LoadingScreen()
//            is ChatUiState.Greeting -> GreetingScreen { viewModel.handleUserInput("Start") }
//            is ChatUiState.AskIfAccountExists -> AskIfAccountExistsScreen(viewModel::handleUserInput)
//            is ChatUiState.WaitForPhoneNumber -> WaitForPhoneNumberScreen(viewModel::handleUserInput)
//            is ChatUiState.AskForPassword -> AskForPasswordScreen(viewModel::handleUserInput)
//            is ChatUiState.PasswordRetry -> PasswordRetryScreen(viewModel::handleUserInput)
//            is ChatUiState.PasswordReset -> PasswordResetScreen(viewModel::handleUserInput)
//            is ChatUiState.WaitForOTP -> WaitForOTPScreen(viewModel::handleUserInput)
//            is ChatUiState.AccountCreation -> AccountCreationScreen(viewModel::handleUserInput)
//            is ChatUiState.AskForName -> AskForNameScreen(viewModel::handleUserInput)
//            is ChatUiState.AskForGender -> AskForGenderScreen(viewModel::handleUserInput)
//            is ChatUiState.AskForClass -> AskForClassScreen(viewModel::handleUserInput)
//            is ChatUiState.AskForDivisionOrSection -> AskForDivisionOrSectionScreen(viewModel::handleUserInput)
//            is ChatUiState.AskForBatch -> AskForBatchScreen(viewModel::handleUserInput)
//            is ChatUiState.SetPassword -> SetPasswordScreen(viewModel::handleUserInput)
//            is ChatUiState.AuthenticationComplete -> AuthenticationCompleteScreen()
//            is ChatUiState.Error -> ErrorScreen((chatState as ChatUiState.Error).exception.message)
//            is ChatUiState.Success -> SuccessScreen((chatState as ChatUiState.Success).message)
//        }
//    }
//}

@Composable
fun LoadingScreen() {
//    Column{
//        BubbleWithIcon(text = "আসসালামু আলাইকুম, শুভ সকাল!"
//            , isUserMessage  = false)
//    }
}

@Preview
@Composable
fun PreviewLoadingScreen() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            Column (modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(15.dp)){
                AskIfAccountExistsScreen()
            }

        }
    }
}

@Composable
fun PasswordRetryScreen() {
//    Column {
//        Text("Please retry your password:")
//        var password by remember { mutableStateOf("") }
//        TextField(value = password, onValueChange = { password = it })
//        Button(onClick = { onRetry(password) }) {
//            Text("Retry")
//        }
//    }
}

@Composable
fun PasswordResetScreen() {
//    Column {
//        Text("Enter your new password:")
//        var newPassword by remember { mutableStateOf("") }
//        TextField(value = newPassword, onValueChange = { newPassword = it })
//        Button(onClick = { onReset(newPassword) }) {
//            Text("Reset Password")
//        }
//    }
}

@Composable
fun WaitForOTPScreen() {
//    Column {
//        Text("Enter OTP:")
//        var otp by remember { mutableStateOf("") }
//        TextField(value = otp, onValueChange = { otp = it })
//        Button(onClick = { onOTPSubmitted(otp) }) {
//            Text("Submit OTP")
//        }
//    }
}

@Composable
fun AccountCreationScreen() {
//    Column {
//        Text("Let's create your account.")
//        Button(onClick = { onContinue("Continue") }) {
//            Text("Continue")
//        }
//    }
}

@Composable
fun AskForNameScreen() {
//    Column {
//        Text("What's your name?")
//        var name by remember { mutableStateOf("") }
//        TextField(value = name, onValueChange = { name = it })
//        Button(onClick = { onNameEntered(name) }) {
//            Text("Submit Name")
//        }
//    }
}

@Composable
fun AskForGenderScreen() {
//    Column {
//        Text("Select your gender:")
//        Button(onClick = { onGenderSelected("Male") }) {
//            Text("Male")
//        }
//        Button(onClick = { onGenderSelected("Female") }) {
//            Text("Female")
//        }
//    }
}

@Composable
fun AskForClassScreen() {
//    Column {
//        Text("What's your class?")
//        // Example: Simple buttons for class selection
//        Button(onClick = { onClassSelected("Class 10") }) {
//            Text("Class 10")
//        }
//    }
}

@Composable
fun AskForDivisionOrSectionScreen() {
//    Column {
//        Text("Select your division or section:")
//        Button(onClick = { onDivisionOrSectionSelected("Science") }) {
//            Text("Science")
//        }
//        Button(onClick = { onDivisionOrSectionSelected("Commerce") }) {
//            Text("Commerce")
//        }
//        Button(onClick = { onDivisionOrSectionSelected("Arts") }) {
//            Text("Arts")
//        }
//    }
}

@Composable
fun AskForBatchScreen() {
//    Column {
//        Text("What's your batch year?")
//        var batch by remember { mutableStateOf("") }
//        TextField(value = batch, onValueChange = { batch = it })
//        Button(onClick = { onBatchSelected(batch) }) {
//            Text("Submit Batch")
//        }
//    }
}

@Composable
fun SetPasswordScreen() {
//    Column {
//        Text("Set your password:")
//        var password by remember { mutableStateOf("") }
//        TextField(value = password, onValueChange = { password = it })
//        Button(onClick = { onPasswordSet(password) }) {
//            Text("Set Password")
//        }
//    }
}

@Composable
fun AuthenticationCompleteScreen() {
    Text("You are successfully authenticated. Welcome!")
}

@Composable
fun GreetingScreen(yesBtn: () -> Unit, noBtn: () -> Unit) {
    BubbleWithIcon(isUserMessage = false) {
        GreetingComponent(yesBtn = { yesBtn() }, noBtn = {noBtn()})
    }

}

@Composable
fun UnregisterUserPhoneNumberScree() {
    BubbleWithIcon(isUserMessage = false) {
        UnregisterUserWaitingNumberScreenComponent()
    }
}

@Composable
fun SendOTPScreen(viewModel: ChatViewModel) {
    BubbleWithIcon(isUserMessage = false) {
        SendOTPScreenComponent("+8801732394777", viewModel)
    }
}

@Composable
fun OTPValidationCompletedScreen(onStart: ()-> Unit) {
    BubbleWithIcon(isUserMessage = false) {
        OTPValidationCompletedComponent(onStart = {onStart()})
    }
}


@Composable
fun AskIfAccountExistsScreen() {
//    BubbleWithIcon(isUserMessage = false) {
//        AskIfAccountExistsScreenComponent()
//    }
}



@Composable
fun WaitForPhoneNumberScreen() {
    BubbleWithIcon(isUserMessage = false) {
        AskIfAccountExistsScreenComponent()
    }
}


@Composable
fun AskForPasswordScreen() {
    BubbleWithIcon(isUserMessage = false) {
        AskForPasswordScreenComponent()
    }
}


@Composable
fun ErrorScreen(errorMessage: String?) {
    Column {
        Text("An error occurred", color = Color.Red)
        errorMessage?.let {
            Text(it, color = Color.Red)
        }
    }
}


@Composable
fun SuccessScreen(successMessage: String) {
    Text("Success: $successMessage", color = Color.Green)
}




