package com.jalloft.lero.ui.screens.loggedin.registration

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.Photo
import com.jalloft.lero.ui.components.PhotoItem
import com.jalloft.lero.ui.components.RegisterScaffold
import com.jalloft.lero.ui.screens.viewmodel.LeroViewModel
import com.jalloft.lero.util.CommonUtil
import com.jalloft.lero.util.UserFields
import kotlinx.coroutines.launch

sealed interface PhotoOption {
    data object Idle : PhotoOption
    data object Profile : PhotoOption
    data object Colletion : PhotoOption

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPhotoScreen(
    onBack: (() -> Unit)?,
    onNext: () -> Unit,
    leroViewModel: LeroViewModel,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val context = LocalContext.current
    val user = leroViewModel.currentUser

    var photoOptionSelected by remember { mutableStateOf<PhotoOption>(PhotoOption.Idle) }
    var showPhotoDialog by remember { mutableStateOf(false) }

    var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }

    val selectedPhotos = remember { mutableStateListOf<Photo>() }
    var currentProfilePhoto by remember { mutableStateOf<Photo?>(null) }

    LaunchedEffect(key1 = user, block = {
        if (user != null) {
            if (currentProfilePhoto == null) {
                currentProfilePhoto = user.profilePhoto
            }

            val photos = user.photos
            if (!photos.isNullOrEmpty()) {
                selectedPhotos.addAll(photos)
            }
        }
    })

    LaunchedEffect(
        key1 = leroViewModel.isLoadedProfilePhoto,
        key2 = leroViewModel.isLoadedCollectionPhoto,
        block = {
            if (leroViewModel.isLoadedProfilePhoto && leroViewModel.isLoadedCollectionPhoto) {
//            val photos = mapOf(UserFields.PHOTOS to leroViewModel.successCollectionPhoto)
                val photos = mapOf(
                    UserFields.PROFILE_PHOTO to leroViewModel.successProfilePhoto,
                    UserFields.PHOTOS to leroViewModel.successCollectionPhoto
                )

                leroViewModel.updateOrEdit(context, photos)
            }
        })


    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> selectedPhotos.addAll(uris.map { Photo(url = it.toString()) }) }
    )

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> currentProfilePhoto = Photo(url = uri.toString()) }
    )


    val photoTakeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            if (isSuccess) {
                if (photoOptionSelected == PhotoOption.Profile) {
                    currentProfilePhoto = Photo(url = tempPhotoUri.toString())
                } else {
                    selectedPhotos.add(Photo(url = tempPhotoUri.toString()))
                }

                tempPhotoUri = Uri.EMPTY
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                tempPhotoUri = CommonUtil.createPhotoFile(context)
                photoTakeLauncher.launch(tempPhotoUri)
            }
        }
    )


    LaunchedEffect(key1 = leroViewModel.isSuccessUpdateOrEdit, block = {
        if (leroViewModel.isSuccessUpdateOrEdit) {
            onNext()
            leroViewModel.clear()
        }
    })

    RegisterScaffold(
        title = "Fotos",
        subtitle = "Suas fotos ajudam a mostrar quem você realmente é.",
        onBack = onBack,
        textButton = stringResource(R.string.advance),
        onSubmit = {
            val listForSave = selectedPhotos.filter { it.name == null }
            if (currentProfilePhoto?.name != null && listForSave.isEmpty()){
                onNext()
                leroViewModel.clear()
                return@RegisterScaffold
            }

            if (currentProfilePhoto?.name == null) {
                leroViewModel.saveProfilePhoto(context, currentProfilePhoto)
            }
            leroViewModel.savePhotos(context, listForSave)
        },
        onSkip = onNext,
        errorMessage = leroViewModel.erroUpdateOrEdit,
        isLoading = leroViewModel.isLoadingUpdateOrEdit
    ) {
        Text(
            text = stringResource(R.string.profile),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        if (currentProfilePhoto != null) {
            currentProfilePhoto?.let { photo ->
                PhotoItem(
                    url = photo.url,
                    modifier = Modifier.size(70.dp),
                    onRemove = {
                        currentProfilePhoto = null
                    })
            }
        } else {
            AddPhotoButton(
                onClick = {
                    showPhotoDialog = true
                    photoOptionSelected = PhotoOption.Profile
                },
            )
        }


        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Minhas fotos",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.FixedSize(72.dp), content = {
                item {
                    AddPhotoButton(
                        onClick = {
                            showPhotoDialog = true
                            photoOptionSelected = PhotoOption.Colletion
                        },
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                items(selectedPhotos.size) { index ->
                    val item = selectedPhotos[index]
                    PhotoItem(
                        url = item.url,
                        modifier = Modifier
                            .size(70.dp)
                            .padding(end = 4.dp),
                        onRemove = { selectedPhotos.remove(item) })
                }
            },
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

    }

    if (showPhotoDialog) {
        ModalBottomSheet(
            onDismissRequest = { showPhotoDialog = false },
            containerColor = MaterialTheme.colorScheme.background,
            sheetState = sheetState,
            modifier = Modifier
                .heightIn(max = 200.dp)
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Escolha um opção", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showPhotoDialog = false
                                    }
                                }
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Open camera",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Tirar foto",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showPhotoDialog = false
                                    }
                                }
                            if (photoOptionSelected == PhotoOption.Profile) {
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            } else {
                                multiplePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }

                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_gallery),
                        contentDescription = "Open gallery",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Escolher na galeria",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}



@Composable
fun AddPhotoButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground.copy(.05f),
            contentColor = MaterialTheme.colorScheme.onBackground.copy(.5f),
        ),
        modifier = modifier.size(70.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.AddCircle,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
}
