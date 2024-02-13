package com.jalloft.lero.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 6,
    isError: Boolean = false,
    onOtpTextChange: (String, Boolean) -> Unit,
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpCount) {
            throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
        }
    }

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    BasicTextField(
        modifier = modifier.focusRequester(focusRequester),
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount && it.text.isDigitsOnly()) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
                if (it.text.length == otpCount){
                    keyboard?.hide()
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(otpCount) { index ->
                    CharView(index = index, text = otpText, isError)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        delay(100)
        keyboard?.show()
    }
}

@Composable
private fun CharView(
    index: Int,
    text: String,
    isError: Boolean = false
) {
    val char = when {
        index == text.length -> "|"
        index > text.length -> ""
        else -> text[index].toString()
    }

    val isFocused = text.length == index
    val hasDigit = char.trim().isNotEmpty() && char.trim().isDigitsOnly()

    val bgColor =
        if (isError) MaterialTheme.colorScheme.tertiary.copy(.05f) else if (isFocused) MaterialTheme.colorScheme.primary.copy(
            .05f
        ) else MaterialTheme.colorScheme.background

    val borderColor = if (isError) MaterialTheme.colorScheme.tertiary.copy(.5f)
    else if (isFocused || hasDigit) MaterialTheme.colorScheme.primary.copy(.5f) else MaterialTheme.colorScheme.onBackground.copy(
        .1f
    )
    Text(
        modifier = Modifier
            .width(48.dp)
            .background(
                bgColor,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                1.dp, borderColor, RoundedCornerShape(8.dp)
            )
            .padding(2.dp),
        text = char,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center
    )
}
