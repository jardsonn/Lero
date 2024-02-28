package com.jalloft.lero.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalloft.lero.R


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScaffold(
    title: String? = null,
    subtitle: String? = null,
    textButton: String = stringResource(R.string.advance),
    textSecundaryButton: String = "",
    errorMessage: String? = null,
    isLoading: Boolean = false,
    enabledSubmitButton: Boolean = true,
    onBack: (() -> Unit)?,
    onSubmit: (() -> Unit)? = null,
    onSecundarySubmit: (() -> Unit)? = null,
    onSkip: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { false }
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack, enabled = !isLoading) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (onSkip != null) {
                TextButton(onClick = onSkip, enabled = !isLoading) {
                    Text(text = stringResource(R.string.pular).uppercase())
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            if (!title.isNullOrEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                )
            }
            if (!subtitle.isNullOrEmpty()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            content()
            AnimatedVisibility(visible = !errorMessage.isNullOrEmpty()) {
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = errorMessage.orEmpty(),
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            if (onSubmit != null) {
                Spacer(modifier = Modifier.size(32.dp))
                LeroButton(
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(end = 16.dp, top = 8.dp),
                                strokeCap = StrokeCap.Round,
                                color = MaterialTheme.colorScheme.primary.copy(.2f)
                            )
                        }
                        Text(text = textButton)
                    },
                    enabled = enabledSubmitButton && !isLoading,
                    onClick = onSubmit,
                )
            }

            if (onSecundarySubmit != null) {
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedButton(
                    onClick = onSecundarySubmit,
                    enabled = enabledSubmitButton && !isLoading,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                ) {
                    Text(text = textSecundaryButton)
                }
            }

        }
    }
}