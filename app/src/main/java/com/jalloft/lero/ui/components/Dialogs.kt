package com.jalloft.lero.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.jalloft.lero.R


@Composable
fun DialogInformation(
    onDismissRequest: () -> Unit,
    confirmButton: () -> Unit,
    title: String,
    text: String
) {
    AlertDialog(
        modifier = Modifier.padding(horizontal = 16.dp),
        containerColor = MaterialTheme.colorScheme.background,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = confirmButton)
            { Text(text = stringResource(R.string.entendi)) }
        },
        title = {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.titleMedium
            )
        }, text = {
            Text(text = text)
        },
        shape = RoundedCornerShape(20.dp)
    )
}