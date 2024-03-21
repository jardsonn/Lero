package com.jalloft.lero.ui.screens.loggedin.main.profile

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.Choice
import com.jalloft.lero.data.domain.Range
import com.jalloft.lero.data.domain.enums.SexualGender
import com.jalloft.lero.ui.components.LeroCommunAppBar
import com.jalloft.lero.ui.components.SliderTrack
import com.jalloft.lero.ui.components.toSliderState
import com.jalloft.lero.ui.screens.viewmodel.LeroViewModel
import com.jalloft.lero.util.DateUtil

const val MIN_AGE = 18
const val MAX_AGE = 99
const val DEFAULT_MAX_AGE = 65

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    viewModel: LeroViewModel,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var address by remember { mutableStateOf<Address?>(null) }
    val context = LocalContext.current
    val user = viewModel.currentUser

    val distances by viewModel.distances.collectAsState(initial = listOf())

    var currentPreferences by remember { mutableStateOf(user?.datingPreferences) }

//    Timber.i("LISTA DE DISTANCIAS = ${distances.value.size}")

    var showDialogDistanceFromYou by remember { mutableStateOf(false) }
    var showDialogGender by remember { mutableStateOf(false) }
    var showDialogAgeRange by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = currentPreferences) {
        val preferences = currentPreferences
        if (preferences?.location?.preference != null) {
            with(preferences.location.preference) {
                val geocoder = Geocoder(context)
                if (longitude != null && latitude != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocation(latitude, longitude, 1) {
                            address = it[0]
                        }
                    } else {
                        try {
                            address = geocoder.getFromLocation(latitude, longitude, 1)?.get(0)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        LeroCommunAppBar(
            text = stringResource(id = R.string.preferences),
            onBack = onBack,
            actions = {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.preferences_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(.5f)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = stringResource(R.string.basics_informations),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(16.dp))
            OptionColumun(
                icon = painterResource(id = R.drawable.ic_localization),
                text = stringResource(R.string.dating_location),
                subtitle = address?.subAdminArea ?: stringResource(R.string.undefined),
                onClick = {}
            )
            OptionColumun(
                icon = painterResource(id = R.drawable.ic_distance),
                text = stringResource(R.string.distance_from_you),
                subtitle = "${currentPreferences?.distance?.preference} km",
                onClick = { showDialogDistanceFromYou = true }
            )
            OptionColumun(
                icon = painterResource(id = R.drawable.ic_gender),
                text = stringResource(id = R.string.gender),
                subtitle = currentPreferences?.gender?.preference?.joinToString(", ") {
                    context.getString(it.nameResId)
                }?.ifEmpty { null } ?: stringResource(R.string.undefined),
                onClick = { showDialogGender = true }
            )
            OptionColumun(
                icon = painterResource(id = R.drawable.ic_age_range),
                text = stringResource(R.string.age_range),
                subtitle = currentPreferences?.ageRange?.preference?.toString()
                    ?: stringResource(R.string.undefined),
                onClick = { showDialogAgeRange = true }
            )
            OptionColumun(
                icon = painterResource(id = R.drawable.ic_looking_for),
                text = stringResource(R.string.looking_for),
                subtitle = currentPreferences?.lookingFor?.preference?.joinToString { ", " }
                    ?: stringResource(R.string.undefined),
                onClick = {}
            )

        }
    }

    if (showDialogAgeRange) {
        var strongPreferenceChecked by remember {
            mutableStateOf(
                currentPreferences?.gender?.isStrong ?: false
            )
        }

        var selectedItem by remember {
            val age = DateUtil.calculateAge(user?.dateOfBirth?.toDate())?.plus(5)
            mutableStateOf(
                Range(MIN_AGE, maxOf(age ?: DEFAULT_MAX_AGE, MAX_AGE))
            )
        }
        PreferencesModalBottomSheet(
            onDismissRequest = { showDialogAgeRange = false },
            title = stringResource(R.string.age_range),
            subtitle = "Qual a idade perfeita para você",
            strongPreferenceChecked,
            onStrongPreference = { strongPreferenceChecked = it },
            onSave = { showDialogAgeRange = false },
            content = {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NumberPicker(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        value = (selectedItem.min ?: MIN_AGE),
                        range = MIN_AGE..MAX_AGE,
                        onValueChange = {
                            selectedItem = selectedItem.copy(min = it)
                        }
                    )

                    NumberPicker(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        value = (selectedItem.max ?: MAX_AGE),
                        range = (selectedItem.min ?: MIN_AGE)..MAX_AGE,
                        onValueChange = {
                            selectedItem = selectedItem.copy(max = it)
                        }
                    )

                }


//                RangeSlider(
//                    value = (selectedItem.min ?: MIN_AGE).toFloat()..(selectedItem.max ?: DEFAULT_MAX_AGE).toFloat(),
//                    onValueChange = {
//                        selectedItem = Range(it.start.toInt(), it.endInclusive.toInt())
//                    },
//                    valueRange = MIN_AGE.toFloat() .. MAX_AGE.toFloat(),
//                    steps = MAX_AGE,
//                    track = { sliderState ->
//                        SliderTrack(sliderState = sliderState.toSliderState())
//                    }
//                )
            })

    }

    if (showDialogGender) {
        var strongPreferenceChecked by remember {
            mutableStateOf(
                currentPreferences?.gender?.isStrong ?: false
            )
        }
        val selectedItem = remember {
            mutableStateListOf<SexualGender>().apply {
                addAll(
                    currentPreferences?.gender?.preference ?: listOf()
                )
            }
        }
        PreferencesModalBottomSheet(
            onDismissRequest = { showDialogGender = false },
            title = stringResource(R.string.gender),
            subtitle = "Escolha quem você deseja namorar",
            if (selectedItem.isEmpty()) null else strongPreferenceChecked,
            onStrongPreference = { strongPreferenceChecked = it },
            onSave = {
                currentPreferences = currentPreferences?.copy(
                    gender = Choice(
                        selectedItem,
                        strongPreferenceChecked
                    )
                )
                showDialogGender = false
            },
            content = {
                LazyColumn {
                    val list = SexualGender.entries
                    items(list.size) { index ->
                        PreferenceItemCheckable(
                            text = stringResource(id = list[index].nameResId),
                            isSelected = selectedItem.contains(list[index]),
                            onSelectItem = {
                                if (selectedItem.contains(list[index])) {
                                    selectedItem.remove(list[index])
                                } else {
                                    selectedItem.add(list[index])
                                }
                            }
                        )
                    }
                }
            })
    }


    if (showDialogDistanceFromYou) {
        var strongPreferenceChecked by remember {
            mutableStateOf(
                currentPreferences?.distance?.isStrong ?: false
            )
        }
        var selectedItem by remember { mutableStateOf(currentPreferences?.distance?.preference) }
        PreferencesModalBottomSheet(
            onDismissRequest = { showDialogDistanceFromYou = false },
            title = stringResource(R.string.distance_from_you),
            subtitle = "Escolha a distância até ${address?.adminArea}",
            strongPreferenceChecked,
            onStrongPreference = { strongPreferenceChecked = it },
            onSave = {
                currentPreferences = currentPreferences?.copy(
                    distance = Choice(
                        selectedItem,
                        strongPreferenceChecked
                    )
                )
                showDialogDistanceFromYou = false
            },
            content = {
                LazyColumn {
                    items(distances.size) { index ->
                        PreferenceItem(
                            text = "${distances[index]} km",
                            isSelected = selectedItem == distances[index],
                            onSelectItem = { selectedItem = distances[index] }
                        )
                    }
                }
            }
        )

    }
}

@Composable
fun PreferenceItem(text: String, isSelected: Boolean, onSelectItem: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectItem() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp, 8.dp)
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PreferenceItemCheckable(text: String, isSelected: Boolean, onSelectItem: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp, 8.dp)
        )

        Checkbox(checked = isSelected, onCheckedChange = { onSelectItem() })
    }
}


@Composable
fun OptionColumun(icon: Painter, text: String, subtitle: String, onClick: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(painter = icon, contentDescription = null)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesModalBottomSheet(
    onDismissRequest: () -> Unit,
    title: String,
    subtitle: String,
    strongPreference: Boolean?,
    onStrongPreference: (Boolean) -> Unit,
    onSave: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        shape = BottomSheetDefaults.HiddenShape,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            LeroCommunAppBar(
                text = title,
                onBack = onDismissRequest,
                actions = {
                    TextButton(onClick = onSave) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.size(12.dp))
            PreferenceModalFooter(
                strongPreference,
                onStrongPreference = onStrongPreference
            )
            Spacer(modifier = Modifier.size(12.dp))
            content()
        }

    }
}

@Composable
fun PreferenceModalFooter(
    strongPreference: Boolean?,
    onStrongPreference: (Boolean) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.onBackground.copy(.05f),
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Essa é um preferência forte",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Switch(
                checked = strongPreference ?: false,
                onCheckedChange = onStrongPreference,
                enabled = strongPreference != null
            )
        }
    }
}

