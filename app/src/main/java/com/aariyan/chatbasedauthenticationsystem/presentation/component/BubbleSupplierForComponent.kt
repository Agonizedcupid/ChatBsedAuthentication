package com.aariyan.chatbasedauthenticationsystem.presentation.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.aariyan.chatbasedauthenticationsystem.ASkForNameComponent
import com.aariyan.chatbasedauthenticationsystem.BubbleWithIcon
import com.aariyan.chatbasedauthenticationsystem.ChatViewModel
import com.aariyan.chatbasedauthenticationsystem.ui.theme.ChatBasedAuthenticationSystemTheme

@Composable
fun AskAccountExistOrNotScreen(yesBtn: () -> Unit, noBtn: () -> Unit) {
    BubbleWithIcon(isUserMessage = false) {
        AskAccountExistOrNotComponent(yesBtn = { yesBtn() }, noBtn = { noBtn() })
    }

}
@Preview
@Composable
fun PreviewAskAccountExistOrNotScreen() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            AskAccountExistOrNotScreen(yesBtn = { }, noBtn = {})
        }
    }
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun LogInPhoneNumberEnteringScreen() {
    BubbleWithIcon(isUserMessage = false) {
        LogInPhoneNumberEnteringComponent()
    }

}
@Preview
@Composable
fun PreviewLogInPhoneNumberEnteringScreen() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            LogInPhoneNumberEnteringScreen()
        }
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun LogInPasswordEnteringScreen() {
    BubbleWithIcon(isUserMessage = false) {
        AskForLogInPasswordScreenComponent()
    }
}
@Preview
@Composable
fun PreviewLogInPhonePasswordEnteringScreen() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            LogInPasswordEnteringScreen()
        }
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun AskForConfirmRegPasswordScreen(
    selectedPosition: Int,
    password: String,
) {
    BubbleWithIcon(isUserMessage = false) {
        ConfirmRegPasswordComponent(
            selectedPosition = selectedPosition,
            password = password,
            "HADY"
        )
    }
}
@Preview
@Composable
fun PreviewAskForConfirmRegPasswordScreen() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            AskForConfirmRegPasswordScreen(selectedPosition = 3, password = "fdsdfgsdfgsdf")
        }
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun AskForRegPasswordScreen(
    selectedPosition: Int,
    password: String,
) {
    BubbleWithIcon(isUserMessage = false) {
        SetRegPasswordComponent(
            selectedPosition = selectedPosition,
            password = password,
            "HADY"
        )
    }
}

@Preview
@Composable
fun PreviewAskForRegPasswordScreen() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            AskForRegPasswordScreen(selectedPosition = 3, password = "fdsdfgsdfgsdf")
        }
    }
}
////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun AskForBatchSelectionScreen(
    selectedPosition: Int,
    selectedBatchYear: String,
    onBatchYearSelected: (String) ->
    Unit, onSubmitClick: ()-> Unit
) {
    BubbleWithIcon(isUserMessage = false) {
        BatchSelectionComponent(
            selectedPosition = selectedPosition,
            selectedBatchYear = selectedBatchYear,
            onBatchYearSelected = onBatchYearSelected,
            onSubmitClick = onSubmitClick
        )
    }
}
@Preview
@Composable
fun PreviewAskForBatchSelectionScreen() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            AskForBatchSelectionScreen(
                selectedPosition = 2,
                selectedBatchYear = "",
                onBatchYearSelected = {}
            ) {

            }
        }
    }
}
/////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun AskForClassNSectionScreen(
    selectedPosition: Int, selectedOption: String, onOptionSelected: (String) -> Unit,
    selectedClass: String, onClassSelected: (String) -> Unit, submitInfo: ()-> Unit
) {
    BubbleWithIcon(isUserMessage = false) {
        ClassNSectionComponent(
            selectedPosition = selectedPosition,
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected,
            selectedClass = selectedClass,
            onClassSelected = onClassSelected,
            submitInfo = submitInfo
        )
    }
}
@Preview
@Composable
fun PreviewAskForClassNSectionScreen() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            AskForClassNSectionScreen(
                selectedPosition = 1,
                selectedOption = "",
                selectedClass = "",
                onClassSelected = {},
                onOptionSelected = {}, submitInfo = {}
            )
        }
    }
}
/////////////////////////////////////////////////////////////////////////
@Composable
fun OTPValidationCompletedScreen(onStart: () -> Unit) {
    BubbleWithIcon(isUserMessage = false) {
        OTPValidationCompletedComponent(onStart = { onStart() })
    }
}

@Preview
@Composable
fun PreviewOTPValidationCompletedScreen() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            OTPValidationCompletedScreen(onStart = {})
        }
    }
}
///////////////////////////////////////////////////////////////

@Composable
fun SendOTPScreen(viewModel: ChatViewModel, phoneNumber: String) {
    BubbleWithIcon(isUserMessage = false) {
        SendOTPScreenComponent(phoneNumber, viewModel)
    }
}
////////////////////////////////////////////////////////////////////////////////
@Composable
fun UnregisterUserPhoneNumberScreen() {
    BubbleWithIcon(isUserMessage = false) {
        UnregisterUserWaitingNumberScreenComponent()
    }
}
@Preview
@Composable
fun PreviewUnregisterUserPhoneNumberScree() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            UnregisterUserPhoneNumberScreen()
        }
    }
}
//////////////////////////////////////////////////////////
@Composable
fun AskForNameScreen(
    textInput: String, selectedGender: String,
    onGenderSelected: (String) -> Unit,
    onSubmitClick: () -> Unit
) {
    BubbleWithIcon(isUserMessage = false) {
        NameGenderComponent(0,textInput, selectedGender, onGenderSelected, onSubmitClick)
    }
}
@Preview
@Composable
fun PreviewAskForNameScreen() {
    BoxWithConstraints {
        ChatBasedAuthenticationSystemTheme {
            AskForNameScreen(textInput = "dfgdfgsdfg", selectedGender = "", onGenderSelected = {}, onSubmitClick = {})
        }
    }
}

