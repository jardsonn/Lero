package com.jalloft.lero.ui.screens.loggedin.registration

import android.Manifest
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalloft.lero.R
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.screens.viewmodel.LeroViewModel
import com.jalloft.lero.util.MANDATORY_DATA_SAVED
import com.jalloft.lero.util.PermissionUtil
import com.jalloft.lero.util.UserFields
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.launch


@Composable
fun LocalizationScreen(
    onBack: (() -> Unit)?,
    onDone: () -> Unit,
    leroViewModel: LeroViewModel,
) {
    val context = LocalContext.current
    val user = leroViewModel.currentUser
    var address by remember { mutableStateOf<Address?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = leroViewModel.isSuccessUpdateOrEdit, block = {
        if (leroViewModel.isSuccessUpdateOrEdit) {
            onDone()
            Hawk.put(MANDATORY_DATA_SAVED, true)
            leroViewModel.clear()
        }
    })


    LaunchedEffect(key1 = user) {
        if (user?.location != null) {
            with(user.location) {
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

    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            leroViewModel.isLoadingUpdateOrEdit = false
            if (isGranted) {
                scope.launch {
                    val localization = PermissionUtil.getLocation(context)
                    val updates = mapOf(UserFields.LOCATION to localization)
                    leroViewModel.updateOrEdit(context, updates)
                }
            } else {
                Toast.makeText(
                    context,
                    "Permissão negada. Acesso à sua localização é obrigatório para continuar.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )


    RegisterScaffold(
        onBack = onBack,
        textButton = if (address == null) "Conceder" else "Atualizar",
        textSecundaryButton = if (address == null) "" else "Continuar em ${address?.subAdminArea}",
        onSecundarySubmit = if (address != null) ({
            onDone()
            Hawk.put(MANDATORY_DATA_SAVED, true)
            leroViewModel.clear()
        }) else null,
        onSubmit = {
//            Hawk.put(MANDATORY_DATA_SAVED, true)
            PermissionUtil.requestPerimission(
                context,
                onPermissionGranted = {
                    scope.launch {
                        leroViewModel.isLoadingUpdateOrEdit = true
                        val localization = PermissionUtil.getLocation(context)
                        val updates = mapOf(UserFields.LOCATION to localization)
                        leroViewModel.updateOrEdit(context, updates)
                    }
                },
                onRequestPermission = {
                    locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                },
                onShowRequestPermissionRationale = {
                    Toast.makeText(context, "Conceda permissão pela as config", Toast.LENGTH_SHORT)
                        .show()
                })

        },
        errorMessage = leroViewModel.erroUpdateOrEdit,
        isLoading = leroViewModel.isLoadingUpdateOrEdit
    ) {

        Image(
            painter = painterResource(id = R.drawable.illustration_navigation),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentScale = ContentScale.FillWidth
        )

        Text(
            text = "Localização",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        )

        Text(
            text = "Precisamos de sua permissão para acessar sua localização. Com acesso à sua localização, podemos mostrar perfis de usuários próximos, permitindo que você se conecte com pessoas que estão perto de você. ",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground.copy(.5f),
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}


