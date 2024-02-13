package com.jalloft.lero.ui.screens.loggedout

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.enums.CountryPhoneNumber
import com.jalloft.lero.ui.components.AnimatedComponent
import com.jalloft.lero.ui.components.LeroButton
import com.jalloft.lero.ui.screens.loggedout.viewmodel.LoggedOutViewModel
import com.jalloft.lero.util.CountryCodeDetector
import timber.log.Timber


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInWithPhoneScreen(
    viewModel: LoggedOutViewModel,
    onBack: () -> Unit,
    onAuthenticated: () -> Unit,
    onVerifyNumber: (String, String) -> Unit,
) {

    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current

    var number by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf(CountryCodeDetector(context).getCountryCode()) }
    var showCountryCodePage by remember { mutableStateOf(false) }

    BackHandler(showCountryCodePage) { showCountryCodePage = false }

    LaunchedEffect(key1 = viewModel.signInWithPhoneSuccess, block = {
        if (viewModel.signInWithPhoneSuccess == null) {
            Timber.i("SignInWithPhone::Success autenticado")
            onAuthenticated()
        } else {
            Timber.i("SignInWithPhone::Success confirmar codigo - ${viewModel.signInWithPhoneSuccess?.first}")
            viewModel.signInWithPhoneSuccess?.first?.let { onVerifyNumber(it, countryCode.code+number) }
        }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Column {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.meu_numero),
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
                )

                val lineColor =
                    if (viewModel.signInWithPhoneFailure != null) MaterialTheme.colorScheme.tertiary.copy(.1f) else MaterialTheme.colorScheme.onBackground
                        .copy(.1f)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .widthIn(max = 110.dp)
                            .wrapContentWidth()
                            .clickable { showCountryCodePage = true }
                    ) {
                        Row {
                            Text(
                                text = "${countryCode.countryCode} ${countryCode.code}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 12.dp)
                            )
                            Icon(
                                imageVector = Icons.Rounded.ArrowDropDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground.copy(.5f)
                            )
                        }
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 2.dp,
                            color = lineColor
                        )
                    }
                    BasicTextField(
                        value = number,
                        onValueChange = { value -> if (value.isDigitsOnly()) number = value },
                        modifier = Modifier.weight(1f),
                        textStyle = MaterialTheme.typography.titleMedium,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboard?.hide()
                                viewModel.signInWithPhone(context, countryCode.code+number)
                            }
                        ),
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                if (number.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.numero_de_telefone),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                                    )
                                }
                                it()
                            }
                            Divider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 2.dp,
                                color = lineColor
                            )
                        }
                    }
                }

                AnimatedVisibility(visible = !viewModel.signInWithPhoneFailure.isNullOrEmpty()) {
                    Text(
                        text = viewModel.signInWithPhoneFailure.orEmpty(),
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Text(
                    text = stringResource(R.string.sms_billing_notice),
                    color = MaterialTheme.colorScheme.onBackground.copy(.8f),
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                LeroButton(
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        if (viewModel.signInWithPhoneLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(end = 16.dp, top = 8.dp),
                                strokeCap = StrokeCap.Round,
                                color = MaterialTheme.colorScheme.primary.copy(.2f)
                            )
                        }
                        Text(text = stringResource(R.string.continuar))
                    },
                    enabled = !viewModel.signInWithPhoneLoading && number.isNotEmpty(),
                    onClick = {
                        keyboard?.hide()
                        viewModel.signInWithPhone(context, countryCode.code+number)
                    },
                )
            }
        }

        AnimatedComponent(visible = showCountryCodePage) {
            CountryList(
                modifier = Modifier.fillMaxSize(),
                onCountry = {
                    countryCode = it
                    showCountryCodePage = false
                },
                onClose = { showCountryCodePage = false }
            )
        }
    }
}

@Composable
fun CountryList(modifier: Modifier, onCountry: (CountryPhoneNumber) -> Unit, onClose: () -> Unit) {
    val context = LocalContext.current
    Column(modifier = modifier.background(color = MaterialTheme.colorScheme.background)) {
        var query by remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.fechar_lista_de_paises)
                )
            }
            BasicTextField(
                value = query,
                onValueChange = { query = it },
                textStyle = MaterialTheme.typography.titleSmall
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        if (query.isEmpty()) {
                            Text(
                                text = stringResource(R.string.search_country),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                            )
                        }
                        it()
                    }
                }

            }
        }

        LazyColumn(
            modifier = modifier.padding(horizontal = 16.dp),
            content = {
                val list =
                    if (query.isEmpty()) CountryPhoneNumber.entries else CountryPhoneNumber.entries.filter {
                        context.getString(it.countryNameResId).startsWith(query, true)
                    }
                items(list.size) {
                    val country = list[it]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCountry(country)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = country.countryNameResId),
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp),
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground.copy(.7f)
                        )
                        Text(
                            text = country.code,
                        )


                    }
                }
            })
    }
}

