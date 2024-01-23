package com.aariyan.chatbasedauthenticationsystem

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aariyan.chatbasedauthenticationsystem.domain.ViewModelHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    private val _selectedGender = MutableStateFlow("")
    val selectedGender = _selectedGender.asStateFlow()

    private val _selectedClass = MutableStateFlow("")
    private val _selectedSection = MutableStateFlow("বিজ্ঞান")

    private val _selectedBatchYear = MutableStateFlow("")

    fun updateBatchYear(newYear: String) {
        _selectedBatchYear.value = newYear
    }

    fun updateGender(newGender: String) {
        _selectedGender.value = newGender
        // Additional logic can be added here if needed
    }

    fun updateClass(newClass: String) {
        _selectedClass.value = newClass
    }

    fun updateSection(newSection: String) {
        _selectedSection.value = newSection
    }


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

    fun isValidName(name: String): Boolean {
        // Check if the name is not empty
        if (name.isBlank()) return false

        // Check if the name contains only letters and spaces
        if (!name.matches(Regex("^[A-Za-z\\s]+\$"))) return false

        // Check for any specific length requirements (optional)
        // Example: Name should be at least 2 characters long
        if (name.length < 2) return false

        return true
    }


    val showTextField: StateFlow<Boolean> = chatState.map { state ->
        when (state) {
            is ChatUiState.WaitForPhoneNumber,
            is ChatUiState.AskForPassword,
            is ChatUiState.WaitForPhoneNumberForUnRegisterUser,
            is ChatUiState.SendOTP,
            is ChatUiState.AskForName,
            is ChatUiState.AskForRegistrationPassword,
            is ChatUiState.AskForConfirmRegistrationPassword
            -> true

            else -> false
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val helper = ViewModelHelper()
    // Handle user userInput based on the current state
    fun handleUserInput(userInput: String, responseType: ResponseType = ResponseType.YES) {
        //appendSystemMessage("adfgsdfsdfgsdfg", false)
        when (val currentState = _chatState.value) {
            // First Screen
            ChatUiState.Greeting -> {
                val (newState, messages) = helper.handleGreetingState(userInput, responseType)
                _messages.value += messages
                _chatState.value = newState
            }
            // When ask for the Phone number for log in
            is ChatUiState.WaitForPhoneNumber -> {
                val (newState, messages) = helper.handleWaitForPhoneNumber(userInput)
                _messages.value += messages
                _chatState.value = newState
            }

            // When user enter password for the LogIn
            is ChatUiState.AskForPassword -> {
                val (newState, messages) = helper.handleAskForPassword(userInput)
                _messages.value += messages
                _chatState.value = newState
            }
            // Registration Process:
            is ChatUiState.WaitForPhoneNumberForUnRegisterUser -> {
                val (newState, messages) = helper.handleWaitForPhoneNumberForUnRegisterUser(userInput = userInput)
                _messages.value += messages
                _chatState.value = newState
            }
            is ChatUiState.SendOTP -> {
                val (newState, messages) = helper.handleOTP(userInput)
                _messages.value += messages
                _chatState.value = newState
            }
            is ChatUiState.OTPValidationCompleted -> {
                val (newState, messages) = helper.handleOTPValidationCompleted(userInput = userInput)
                _messages.value += messages
                _chatState.value = newState
            }
/////////////////////////////// Information //////////////////////////////////////////////////

            is ChatUiState.AskForName -> {
                val (newState, messages) = helper.handleNameNGender(userInput = userInput)
                _messages.value += messages
                _chatState.value = newState
            }
            is ChatUiState.AskForClassNSection -> {
                val (newState, messages) = helper.handleClassNSection(userInput = userInput)
                _messages.value += messages
                _chatState.value = newState
            }

            is ChatUiState.AskForBatchSection -> {
                val (newState, messages) = helper.handleBatchSection(userInput = userInput)
                _messages.value += messages
                _chatState.value = newState
            }

            is ChatUiState.AskForRegistrationPassword -> {
                val (newState, messages) = helper.handleRegistrationPassword(userInput = userInput)
                _messages.value += messages
                _chatState.value = newState
            }

            is ChatUiState.AskForConfirmRegistrationPassword -> {
                val (newState, messages) = helper.handleConfirmRegistrationPassword(userInput = userInput)
                _messages.value += messages
                _chatState.value = newState
            }











            ChatUiState.AskIfAccountExists -> {
                processAccountExistenceResponse(userInput)
            }

            ChatUiState.AccountCreation -> {
                // Start account creation process
                _chatState.value = ChatUiState.AskForName("What is your name?")
                appendSystemMessage("What is your name?", isUserMessage = false)

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
                // Assuming a function verifyOTP(userInput) exists
                val otpVerified = verifyOTP(userInput)
                if (otpVerified) {
                    // Proceed to set a new password or complete account creation
                    _chatState.value = ChatUiState.SetPassword
                } else {
                    _chatState.value = ChatUiState.Error(Exception("Invalid OTP"))
                }
            }

            ChatUiState.AskForGender -> {
                processGenderSelection(userInput)
            }

            ChatUiState.AskForClass -> {
                // Process class selection
                // Special handling for SSC or HSC
                if (userInput == "SSC" || userInput == "HSC") {
                    _chatState.value = ChatUiState.AskForDivisionOrSection
                } else {
                    // Proceed to ask for batch
                    _chatState.value = ChatUiState.AskForBatch
                }
            }

            ChatUiState.AskForDivisionOrSection -> {
                // Process division or section selection
                processDivisionOrSectionSelection(userInput)
                //_chatState.value = ChatUiState.AskForBatch
            }

            ChatUiState.AskForBatch -> {
                processBatchSelection(userInput)

            }

            ChatUiState.SetPassword -> {
                processPasswordSetting(userInput)
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
                _messages.value += Message(userInput, true)
                _chatState.value = ChatUiState.Greeting
            }

            is ChatUiState.Success -> {
                Log.d("REMAINING_OPERATION", "SUCCESS")
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
            // If the userInput is invalid, prompt the user again
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
            // If the userInput is invalid, prompt the user again
            _chatState.value =
                ChatUiState.Error(Exception("Invalid selection. Please enter a valid division or section."))
        }
    }

    // Placeholder for division or section validation
    private fun validateDivisionOrSection(selection: String): Boolean {
        // Implement actual logic to validate the division or section
        // This might include checking if the userInput matches predefined options
        val validDivisions = listOf("Science", "Commerce", "Arts")
        return selection in validDivisions
    }

    private fun processAccountExistenceResponse(userInput: String) {
        if (userInput.trim().equals("yes", ignoreCase = true)) {
            _chatState.value = ChatUiState.WaitForPhoneNumber("Please enter your phone number:")
            appendSystemMessage("Please enter your phone number:", isUserMessage = false)
        } else if (userInput.trim().equals("no", ignoreCase = true)) {
            _chatState.value = ChatUiState.AccountCreation
            appendSystemMessage("Start the Account Creation", isUserMessage = false)
        } else {
            // Handle invalid userInput
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

    private fun processNameuserInput(name: String, userInput: String) {


        appendSystemMessage("তোমার নাম এবং জেন্ডার সম্পর্কে জানতে চাচ্ছিলাম", isUserMessage = false)
        _messages.value += Message(userInput, true)
        //appendSystemMessage("ধন্যবাদ, তোমার নাম এবং জেন্ডার জানানোর জন্য", isUserMessage = false)
        //appendSystemMessage("তোমার ক্লাস সিলেক্ট করো", isUserMessage = false)

        _chatState.value = ChatUiState.AskForClassNSection("")

//        if (isValidName(name)) {
//            // If the name is valid, proceed with the next step of account creation
//            // For example, asking for gender
//            //_chatState.value = ChatUiState.AskForGender
//            _messages.value += Message(userInput, true)
//            appendSystemMessage("ধন্যবাদ, তোমার নাম এবং জেন্ডার জানানোর জন্য", isUserMessage = false)
//        } else {
//            // If the name is invalid, prompt the user to enter a valid name again
////            _chatState.value =
////                ChatUiState.Error(Exception("Invalid name. Please enter a valid name."))
//            appendSystemMessage("Invalid name. Please enter a valid name.", isUserMessage = false)
//        }
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
            // If the gender userInput is invalid, prompt the user again
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
        // For example, check if the userInput is either "Male" or "Female"
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
    data class AskForRegistrationPassword(val message: String) : ChatUiState
    data class AskForConfirmRegistrationPassword(val message: String) : ChatUiState
    object PasswordRetry : ChatUiState
    object PasswordReset : ChatUiState
    object WaitForOTP : ChatUiState
    object SendOTP : ChatUiState
    object AccountCreation : ChatUiState
    object OTPValidationCompleted : ChatUiState
    data class AskForName(val message: String) : ChatUiState
    data class AskForClassNSection(val message: String) : ChatUiState
    data class AskForBatchSection(val message: String) : ChatUiState
    object AskForGender : ChatUiState
    object AskForClass : ChatUiState
    object AskForDivisionOrSection : ChatUiState
    object AskForBatch : ChatUiState
    object SetPassword : ChatUiState
    object AuthenticationComplete : ChatUiState
    data class Error(val exception: Throwable) : ChatUiState
    data class Success(val message: String) : ChatUiState

}
