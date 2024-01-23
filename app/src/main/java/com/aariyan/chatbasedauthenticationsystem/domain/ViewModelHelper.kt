package com.aariyan.chatbasedauthenticationsystem.domain
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.aariyan.chatbasedauthenticationsystem.ChatUiState
import com.aariyan.chatbasedauthenticationsystem.Message
import com.aariyan.chatbasedauthenticationsystem.ResponseType
import kotlinx.coroutines.flow.MutableStateFlow
class ViewModelHelper {
    fun handleGreetingState(
        userInput: String,
        responseType: ResponseType = ResponseType.YES
    ): Pair<ChatUiState, List<Message>> {

        val botMessage = Message("তোমার কি উৎকর্ষ একাউন্ট আছে?", isUserMessage = false)
        val userResponse = Message(userInput, true)

        val newState = if (responseType == ResponseType.YES) {
            ChatUiState.WaitForPhoneNumber("")
        } else {
            ChatUiState.WaitForPhoneNumberForUnRegisterUser("")
        }

        return Pair(newState, listOf(botMessage, userResponse))
    }

    fun handleWaitForPhoneNumber(
        userInput: String
    ): Pair<ChatUiState, List<Message>> {
        val messages = mutableListOf<Message>()

        // Create the annotated string
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
        messages.add(Message(text.toString(), isUserMessage = false))
        messages.add(Message(userInput, true))

        // Check if phone number exists
        val phoneNumberExists = checkPhoneNumberExists(userInput)

        // Determine the next state based on whether the phone number exists
        val nextState = if (phoneNumberExists) {
            ChatUiState.AskForPassword("")
        } else {
            ChatUiState.AccountCreation
        }

        return Pair(nextState, messages)
    }

    fun handleAskForPassword(
        userInput: String
    ): Pair<ChatUiState, List<Message>> {
        val messages = mutableListOf<Message>()

        // Create the annotated string
        val text = "ধন্যবাদ, তোমার ফোন নম্বরটির বিপরীতে আমরা একটি একাউন্ট সনাক্ত করতে পেরেছি। এখন পাসওয়ার্ড দিয়ে তোমার একাউন্টে প্রবেশ করতে পারো..."
        messages.add(Message(text, isUserMessage = false))
        messages.add(Message(userInput, true))

        // Add message before checking password
        messages.add(Message("তোমার পাসওয়ার্ড চেক করা হচ্ছে...", isUserMessage = false))

        // Verify the password
        val passwordCorrect = verifyPassword(userInput)

        // Determine the next state based on password correctness
        val nextState = if (passwordCorrect) {
            messages.add(Message("Authentication successful! Welcome to the application.", isUserMessage = false))
            ChatUiState.AuthenticationComplete
        } else {
            messages.add(Message("Incorrect password, please try again:", isUserMessage = false))
            ChatUiState.PasswordRetry
        }

        return Pair(nextState, messages)
    }

    fun handleWaitForPhoneNumberForUnRegisterUser(
        userInput: String
    ): Pair<ChatUiState, List<Message>> {
        val message = mutableListOf<Message>()

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
        message.add(Message(text.toString(), isUserMessage = false))
        message.add(Message(userInput, isUserMessage = true))

        val nextState = if (!checkPhoneNumberExists("")) {
            ChatUiState.AskForPassword("")
        } else {
            ChatUiState.SendOTP
        }
        return Pair(nextState, message)
    }

    fun handleOTP(userInput: String): Pair<ChatUiState, List<Message>> {
        val message = mutableListOf<Message>()
        val text = "ধন্যবাদ! এই নম্বরে প্রেরিত কোডটি মেয়াদ উত্তীর্ণ হওয়ার আগে লিখো।"
        message.add(Message(text, false))
        message.add(Message(userInput, true))
        val nextState = ChatUiState.OTPValidationCompleted

        return Pair(nextState, message)
    }

    fun handleOTPValidationCompleted(userInput: String): Pair<ChatUiState, List<Message>> {
        val message = mutableListOf<Message>()
        val text =
            "ধন্যবাদ, নিবন্ধনের প্রথম ধাপ সম্পন্ন হয়েছে, দ্বিতীয় ধাপে তোমার প্রোফাইল আপডেট করতে কিছু তথ্য প্রয়োজন"

        message.add(Message(text, false))
        message.add(Message(userInput, true))
        val nextState = ChatUiState.AskForName("")

        return Pair(nextState, message)
    }

    fun handleNameNGender(userInput: String): Pair<ChatUiState, List<Message>> {
        val message = mutableListOf<Message>()
        val text = "তোমার নাম এবং জেন্ডার সম্পর্কে জানতে চাচ্ছিলাম"

        message.add(Message(text, false))
        message.add(Message(userInput, true))
        val nextState = ChatUiState.AskForClassNSection("")

        return Pair(nextState, message)
    }

    fun handleClassNSection(userInput: String): Pair<ChatUiState, List<Message>> {
        val message = mutableListOf<Message>()
        val text = "তোমার ক্লাস সিলেক্ট করো"

        message.add(Message(text, false))
        message.add(Message(userInput, true))
        val nextState = ChatUiState.AskForBatchSection("")

        return Pair(nextState, message)
    }

    fun handleBatchSection(userInput: String): Pair<ChatUiState, List<Message>> {
        val message = mutableListOf<Message>()
        val text = "তোমার ব্যাচ সিলেক্ট করো"

        message.add(Message(text, false))
        message.add(Message(userInput, true))
        val nextState = ChatUiState.AskForRegistrationPassword("")

        return Pair(nextState, message)
    }

    fun handleRegistrationPassword(userInput: String): Pair<ChatUiState, List<Message>> {
        val message = mutableListOf<Message>()
        val text = "একটা পাসওয়ার্ড সেট করো"

        message.add(Message(text, false))
        message.add(Message(userInput, true))
        val nextState = ChatUiState.AskForConfirmRegistrationPassword("")

        return Pair(nextState, message)
    }

    fun handleConfirmRegistrationPassword(userInput: String): Pair<ChatUiState, List<Message>> {
        val message = mutableListOf<Message>()
        val text = "তোমার পাসওয়ার্ড টি নিশ্চিত করো"

        message.add(Message(text, false))
        message.add(Message(userInput, true))
        val nextState = ChatUiState.AskForConfirmRegistrationPassword("")

        return Pair(nextState, message)
    }


    private fun verifyPassword(password: String): Boolean {
        // Implement the logic to verify the password
        return true // Placeholder
    }

    private fun checkPhoneNumberExists(phoneNumber: String): Boolean {
        // Implement the logic to check if the phone number exists
        return true // Placeholder
    }
}