package com.aariyan.chatbasedauthenticationsystem

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ResponseType {
    YES, NO
}

class ChatViewModel : ViewModel() {

    private val _chatState = MutableStateFlow<ChatUiState>(ChatUiState.Greeting)
    val chatState: StateFlow<ChatUiState> = _chatState

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    // Flag to indicate if the typing indicator should be shown
    private val _showTypingIndicator = MutableStateFlow(false)
    val showTypingIndicator: StateFlow<Boolean> = _showTypingIndicator

    private val _otpTimerFinished = MutableStateFlow(false)
    val otpTimerFinished: StateFlow<Boolean> = _otpTimerFinished

    private val _otpTimerValue = MutableStateFlow(20) // 120 seconds for 2 minutes
    val otpTimerValue: StateFlow<Int> = _otpTimerValue


    fun sendOtp() {
        startOtpTimer()
    }

    private fun startOtpTimer() {
        viewModelScope.launch {
            for (time in 20 downTo 0) {
                Log.d("OtpTimer", "Time left: $time seconds")
                _otpTimerValue.value = time
                delay(1000)
            }
            _otpTimerFinished.value = true
        }
    }

    fun resendOtp() {
        // Logic to resend OTP
        _otpTimerFinished.value = false // Reset the timer
        startOtpTimer() // Start the timer again
    }


    val showTextField: StateFlow<Boolean> = chatState.map { state ->
        when (state) {
            is ChatUiState.WaitForPhoneNumber,
            is ChatUiState.AskForPassword,
            is ChatUiState.WaitForPhoneNumberForUnRegisterUser,
            is ChatUiState.SendOTP
            -> true

            else -> false
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)


    // Handle user input based on the current state
    fun handleUserInput(input: String, responseType: ResponseType = ResponseType.YES) {
        //appendSystemMessage("adfgsdfsdfgsdfg", false)
        when (val currentState = _chatState.value) {
            ChatUiState.Greeting -> {
                appendSystemMessage("তোমার কি উৎকর্ষ একাউন্ট আছে?", isUserMessage = false)
                _messages.value += Message(input, true)
                //_chatState.value = ChatUiState.AskIfAccountExists
                if (responseType == ResponseType.YES) {
                    _chatState.value = ChatUiState.WaitForPhoneNumber("")
                } else {
                    _chatState.value = ChatUiState.WaitForPhoneNumberForUnRegisterUser("")
                }
            }

            ChatUiState.AskIfAccountExists -> {
                processAccountExistenceResponse(input)
            }

            is ChatUiState.WaitForPhoneNumber -> {
                val text = buildAnnotatedString {
                    append("আচ্ছা!  যে নম্বর দিয়ে একাউন্টটি খুলেছ সে")

                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xff52B69A)
                        )
                    ) {
                        append(" ফোন নম্বরটি ")
                    }

                    append("দাও প্লিজ... ")
                }
                appendSystemMessage(text.toString(), isUserMessage = false)
                _messages.value += Message(input, true)

                val phoneNumberExists = checkPhoneNumberExists(input)

                if (phoneNumberExists) {
                    _chatState.value = ChatUiState.AskForPassword("")
                    //appendSystemMessage("Please enter your password:", isUserMessage = false)

                } else {
                    // If phone number does not exist, start account creation
                    _chatState.value = ChatUiState.AccountCreation
                    appendSystemMessage(
                        "Let's create your account. What is your name?",
                        isUserMessage = false
                    )

                }
            }

            is ChatUiState.AskForPassword -> {
                val text = buildAnnotatedString {
                    append("ধন্যবাদ, তোমার ফোন নম্বরটির বিপরীতে আমরা একটি একাউন্ট সনাক্ত করতে পেরেছি। এখন")

                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xff52B69A)
                        )
                    ) {
                        append(" পাসওয়ার্ড ")
                    }

                    append("দিয়ে তোমার একাউন্টে প্রবেশ করতে পারো...")
                }

                appendSystemMessage(text.toString(), isUserMessage = false)
                _messages.value += Message(input, true)

                val passwordCorrect = verifyPassword(input)
                if (passwordCorrect) {
                    _chatState.value = ChatUiState.AuthenticationComplete
                    appendSystemMessage(
                        "Authentication successful! Welcome to the application.",
                        isUserMessage = false
                    )

                } else {
                    _chatState.value = ChatUiState.PasswordRetry
                    appendSystemMessage(
                        "Incorrect password, please try again:",
                        isUserMessage = false
                    )

                    // Implement logic for password retry and reset
                }
            }

            ChatUiState.AccountCreation -> {
                // Start account creation process
                _chatState.value = ChatUiState.AskForName("What is your name?")
                appendSystemMessage("What is your name?", isUserMessage = false)

            }

            is ChatUiState.AskForName -> {
                processNameInput(input)
            }

            is ChatUiState.PasswordRetry -> {
                processPasswordRetry()
            }

            ChatUiState.PasswordReset -> {
                // Handle password reset logic
                // Possibly involve sending OTP
                _chatState.value = ChatUiState.WaitForOTP
            }

            ChatUiState.WaitForOTP -> {
                // Verify OTP
                // Assuming a function verifyOTP(input) exists
                val otpVerified = verifyOTP(input)
                if (otpVerified) {
                    // Proceed to set a new password or complete account creation
                    _chatState.value = ChatUiState.SetPassword
                } else {
                    _chatState.value = ChatUiState.Error(Exception("Invalid OTP"))
                }
            }

            ChatUiState.AskForGender -> {
                processGenderSelection(input)
            }

            ChatUiState.AskForClass -> {
                // Process class selection
                // Special handling for SSC or HSC
                if (input == "SSC" || input == "HSC") {
                    _chatState.value = ChatUiState.AskForDivisionOrSection
                } else {
                    // Proceed to ask for batch
                    _chatState.value = ChatUiState.AskForBatch
                }
            }

            ChatUiState.AskForDivisionOrSection -> {
                // Process division or section selection
                processDivisionOrSectionSelection(input)
                //_chatState.value = ChatUiState.AskForBatch
            }

            ChatUiState.AskForBatch -> {
                processBatchSelection(input)

            }

            ChatUiState.SetPassword -> {
                processPasswordSetting(input)
            }

            ChatUiState.AuthenticationComplete -> {
                processAuthenticationComplete()
            }

            is ChatUiState.Error -> {
                // You might want to log the error or inform the user
                val errorMessage =
                    (currentState as ChatUiState.Error).exception.message ?: "An error occurred"
                Log.d("REMAINING_OPERATION", errorMessage)
                // Update the UI to show the error message
            }

            ChatUiState.Loading -> {
                appendSystemMessage("আসসালামু আলাইকুম, শুভ সকাল!", isUserMessage = false)
                _messages.value += Message(input, true)
                _chatState.value = ChatUiState.Greeting
            }

            is ChatUiState.Success -> {
                Log.d("REMAINING_OPERATION", "SUCCESS")
            }

            is ChatUiState.WaitForPhoneNumberForUnRegisterUser -> {
                val text = buildAnnotatedString {
                    append("ধন্যবাদ, একাউন্টে প্রবেশ করতে তোমার")

                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xff52B69A)
                        )
                    ) {
                        append(" ফোন নম্বরটি ")
                    }

                    append("দাও প্লিজ...")
                }
                appendSystemMessage(text.toString(), isUserMessage = false)
                _messages.value += Message(input, true)

                val phoneNumberExists = !checkPhoneNumberExists(input)

                if (phoneNumberExists) {
                    _chatState.value = ChatUiState.AskForPassword("")
                    //appendSystemMessage("Please enter your password:", isUserMessage = false)

                } else {
                    // If phone number does not exist, start account creation
                    _chatState.value = ChatUiState.SendOTP
                    //appendSystemMessage("Let's create your account. What is your name?", isUserMessage = false)

                }
            }

            is ChatUiState.SendOTP -> {
                val text = "ধন্যবাদ! এই নম্বরে প্রেরিত কোডটি মেয়াদ উত্তীর্ণ হওয়ার আগে লিখো।"
                appendSystemMessage(text.toString(), isUserMessage = false)
                _messages.value += Message(input, true)
                _chatState.value = ChatUiState.OTPValidationCompleted
            }

            is ChatUiState.OTPValidationCompleted -> {
                val text = "ধন্যবাদ, নিবন্ধনের প্রথম ধাপ সম্পন্ন হয়েছে, দ্বিতীয় ধাপে তোমার প্রোফাইল আপডেট করতে কিছু তথ্য প্রয়োজন"
                appendSystemMessage(text.toString(), isUserMessage = false)
                _messages.value += Message(input, true)
            }
        }
    }

    private fun appendSystemMessage(message: String, isUserMessage: Boolean) {
        _messages.value += Message(
            message,
            isUserMessage = isUserMessage
        ) // Add system message to chat history
    }

    private fun processAuthenticationComplete() {
        // This is where you would typically update the UI to reflect successful authentication
        // or redirect the user to the main part of your application
        // For now, we'll just set a message indicating success

        val successMessage = "Authentication successful! Welcome to the application."
        _chatState.value = ChatUiState.Success(successMessage)
    }

    private fun processPasswordSetting(password: String) {
        if (validatePassword(password)) {
            // Implement logic to securely store the password
            // Typically involves a network request to save the password for the new account
            _chatState.value = ChatUiState.AuthenticationComplete
        } else {
            // If the password is not strong enough, prompt the user again
            _chatState.value =
                ChatUiState.Error(Exception("Password not strong enough. Please choose a stronger password."))
        }
    }

    // Placeholder for password validation
    private fun validatePassword(password: String): Boolean {
        // Implement actual logic to validate the password's strength
        // Example: check length, presence of numbers/special characters, etc.
        return password.length >= 8 // Simple length check as an example
    }

    private fun processBatchSelection(batch: String) {
        if (validateBatch(batch)) {
            // If the batch is valid, proceed to set the password
            _chatState.value = ChatUiState.SetPassword
        } else {
            // If the input is invalid, prompt the user again
            _chatState.value =
                ChatUiState.Error(Exception("Invalid batch. Please enter a valid batch."))
        }
    }

    // Placeholder for batch validation
    private fun validateBatch(batch: String): Boolean {
        // Implement actual logic to validate the batch
        // This might include checking if the batch matches a certain format or is within a range
        return batch.matches(Regex("\\d{4}")) // Example: validating a 4-digit batch number
    }

    private fun processDivisionOrSectionSelection(selection: String) {
        if (validateDivisionOrSection(selection)) {
            // If the division or section is valid, proceed with the next step
            _chatState.value = ChatUiState.AskForBatch
        } else {
            // If the input is invalid, prompt the user again
            _chatState.value =
                ChatUiState.Error(Exception("Invalid selection. Please enter a valid division or section."))
        }
    }

    // Placeholder for division or section validation
    private fun validateDivisionOrSection(selection: String): Boolean {
        // Implement actual logic to validate the division or section
        // This might include checking if the input matches predefined options
        val validDivisions = listOf("Science", "Commerce", "Arts")
        return selection in validDivisions
    }

    private fun processAccountExistenceResponse(input: String) {
        if (input.trim().equals("yes", ignoreCase = true)) {
            _chatState.value = ChatUiState.WaitForPhoneNumber("Please enter your phone number:")
            appendSystemMessage("Please enter your phone number:", isUserMessage = false)
        } else if (input.trim().equals("no", ignoreCase = true)) {
            _chatState.value = ChatUiState.AccountCreation
            appendSystemMessage("Start the Account Creation", isUserMessage = false)
        } else {
            // Handle invalid input
            _chatState.value =
                ChatUiState.Error(IllegalArgumentException("Please answer with 'yes' or 'no'"))
            appendSystemMessage("Please answer with 'yes' or 'no'", isUserMessage = false)
        }
    }

    private fun checkPhoneNumberExists(phoneNumber: String): Boolean {
        // Implement the logic to check if the phone number exists
        // This could involve a network request to your backend
        appendSystemMessage("আপনার ফোন নম্বর চেক করা হচ্ছে ...........", isUserMessage = false)
        return true // Placeholder
    }

    private fun verifyPassword(password: String): Boolean {
        // Implement the logic to verify the password
        // This could involve a network request to your backend
        appendSystemMessage("আপনার পাসওয়ার্ড যাচাই করা হচ্ছে ..........", isUserMessage = false)
        return true // Placeholder
    }

    private fun verifyOTP(otp: String): Boolean {
        // Implement OTP verification logic
        appendSystemMessage("Verify OTP", isUserMessage = false)
        return true // Placeholder
    }

    private var passwordRetryCount = 0
    private val maxRetryCount = 3 // Maximum allowed retry attempts

    private fun processPasswordRetry() {
        passwordRetryCount++
        if (passwordRetryCount >= maxRetryCount) {
            // If the maximum retry count is reached, initiate password reset
            _chatState.value = ChatUiState.PasswordReset
            appendSystemMessage("Reset Password", isUserMessage = false)
            passwordRetryCount = 0 // Reset the counter
        } else {
            // If the maximum retry count has not been reached, ask for the password again
            _chatState.value = ChatUiState.AskForPassword("Incorrect password, please try again:")
            appendSystemMessage("Incorrect password, please try again:", isUserMessage = false)
        }
    }

    private fun processNameInput(name: String) {
        if (validateName(name)) {
            // If the name is valid, proceed with the next step of account creation
            // For example, asking for gender
            _chatState.value = ChatUiState.AskForGender
            appendSystemMessage("Ask for Gender", isUserMessage = false)
        } else {
            // If the name is invalid, prompt the user to enter a valid name again
            _chatState.value =
                ChatUiState.Error(Exception("Invalid name. Please enter a valid name."))
            appendSystemMessage("Invalid name. Please enter a valid name.", isUserMessage = false)
        }
    }

    // Placeholder for name validation
    private fun validateName(name: String): Boolean {
        // Implement actual logic to validate the name
        // For example, check if the name is not empty and meets certain criteria
        appendSystemMessage("Validate name", isUserMessage = false)
        return name.isNotBlank() // Simple validation placeholder
    }

    private fun processGenderSelection(gender: String) {
        if (validateGender(gender)) {
            // If the gender is valid, proceed with the next step
            // For example, asking for the class
            _chatState.value = ChatUiState.AskForClass
            appendSystemMessage("Ask For Class", isUserMessage = false)
        } else {
            // If the gender input is invalid, prompt the user again
            _chatState.value =
                ChatUiState.Error(Exception("Invalid gender. Please enter 'Male' or 'Female'."))
            appendSystemMessage(
                "Invalid gender. Please enter 'Male' or 'Female'.",
                isUserMessage = false
            )
        }
    }

    // Placeholder for gender validation
    private fun validateGender(gender: String): Boolean {
        // Implement actual logic to validate the gender
        // For example, check if the input is either "Male" or "Female"
        appendSystemMessage("Validate Gender", isUserMessage = false)
        return gender.equals("male", ignoreCase = true) || gender.equals(
            "female",
            ignoreCase = true
        )
    }
}

sealed interface ChatUiState {
    object Loading : ChatUiState
    object Greeting : ChatUiState
    object AskIfAccountExists : ChatUiState
    data class WaitForPhoneNumber(val message: String) : ChatUiState
    data class WaitForPhoneNumberForUnRegisterUser(val message: String) : ChatUiState
    data class AskForPassword(val message: String) : ChatUiState
    object PasswordRetry : ChatUiState
    object PasswordReset : ChatUiState
    object WaitForOTP : ChatUiState
    object SendOTP : ChatUiState
    object AccountCreation : ChatUiState
    object OTPValidationCompleted: ChatUiState
    data class AskForName(val message: String) : ChatUiState
    object AskForGender : ChatUiState
    object AskForClass : ChatUiState
    object AskForDivisionOrSection : ChatUiState
    object AskForBatch : ChatUiState
    object SetPassword : ChatUiState
    object AuthenticationComplete : ChatUiState
    data class Error(val exception: Throwable) : ChatUiState
    data class Success(val message: String) : ChatUiState

}
