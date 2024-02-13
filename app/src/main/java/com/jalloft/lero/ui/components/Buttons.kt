package com.jalloft.lero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp


@Composable
fun LeroButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = CircleShape,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier.semantics { role = Role.Button },
        onClick = onClick,
        shape = shape,
        enabled = enabled,
        color = Color.Transparent
    ) {
        val gradient = Brush.horizontalGradient(
            if (enabled) listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary,
            ) else listOf(
                MaterialTheme.colorScheme.primary.copy(.05f),
                MaterialTheme.colorScheme.primary.copy(.05f),
            )
        )

        ProvideTextStyle(
            value = MaterialTheme.typography.labelLarge.copy(
                color = if (enabled) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary.copy(
                    .1f
                )
            )
        ) {
            Row(
                Modifier
                    .defaultMinSize(minHeight = 52.dp)
                    .background(gradient, shape = shape)
                    .then(modifier),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content

            )
        }
    }
}


