package com.jalloft.lero.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jalloft.lero.R

@SuppressLint("ModifierParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NormalTextField(
    value: String,
    label: String,
    placeholder: String,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    closeKeyboad: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    var nextChanged by remember {
        mutableStateOf(false)
    }

    var hasFocus by remember {
        mutableStateOf(false)
    }

    val isError = nextChanged && errorMessage != null

    Column(modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
        )
        BasicTextField(
            value = value,
            onValueChange,
            Modifier
                .padding(top = 4.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.hasFocus) {
                        hasFocus = true
                    }
                    if (hasFocus && !focusState.hasFocus) {
                        nextChanged = true
                        hasFocus = false
                    }
                },
            enabled,
            readOnly,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            keyboardOptions,
            keyboardActions,
            singleLine,
            maxLines,
            minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                            )
                        )
                    }
                    it()
                }
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = if (!isError) MaterialTheme.colorScheme.onBackground.copy(.2f) else MaterialTheme.colorScheme.tertiary.copy(
                        0.5f
                    ),
                    thickness = 2.dp
                )
            }
        }

        AnimatedVisibility(visible = isError) {
            Text(
                text = errorMessage.orEmpty(),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.tertiary.copy(
                        0.8f
                    )
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }

    LaunchedEffect(key1 = closeKeyboad, block = {
        if (closeKeyboad) {
            keyboard?.hide()
            nextChanged = true
        }
    })

}

@Composable
fun SelectableTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    maxLines: Int = 1,
    onInformation: (() -> Unit)? = null,
    onOpenOptios: () -> Unit,
    hasSelected: Boolean
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(end = 4.dp)
            )
            if (onInformation != null) {
                IconButton(onClick = onInformation) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = stringResource(R.string.information_gender_button)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .clickable { onOpenOptios() }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val color = MaterialTheme.colorScheme.onBackground.copy(
                if (!hasSelected) .5f else 1f
            )
            Text(
                text = text,
                maxLines = maxLines,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 4.dp),
                overflow = TextOverflow.Ellipsis,
            )

            Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null, tint = color)
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground.copy(.2f),
            thickness = 2.dp
        )
    }
}


@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    placeholder: String
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground.copy(.05f), CircleShape),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(.5f),
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(20.dp)
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .weight(1f)
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                    )
                }
                it()
            }
            if (value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(.5f),
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickable { onValueChange("") }
                )
            }
        }
    }
}