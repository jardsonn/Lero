package com.jalloft.lero.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalloft.lero.R


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OptionsListScaffold(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    onBack: () -> Unit,
    onSave: (() -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {

    Column(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .pointerInteropFilter { false }
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
            if (onSave != null) {
                TextButton(onClick = onSave) {
                    Text(text = stringResource(R.string.salvar))
                }
            }
        }


        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            subtitle?.let {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        MaterialTheme.colorScheme.onBackground.copy(.5f)
                    ),
                )
            }

            LazyColumn(content = content, modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
        }
    }
}


@Composable
fun ItemOption(
    modifier: Modifier,
    isChecked: Boolean,
    checkBoxShape: Shape = CircleShape,
    name: String,
    onClick: () -> Unit
) {
    Box(modifier = modifier
        .clickable { onClick() }
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp
            )

            Surface(
                modifier = Modifier.size(24.dp),
                shape = checkBoxShape,
                color = MaterialTheme.colorScheme.background,
                border = BorderStroke(
                    2.dp,
                    if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
                        .5f
                    )
                )
            ) {
                if (isChecked) {
                    Icon(
                        imageVector = if (checkBoxShape == CircleShape) Icons.Rounded.CheckCircle else Icons.Rounded.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }

}
