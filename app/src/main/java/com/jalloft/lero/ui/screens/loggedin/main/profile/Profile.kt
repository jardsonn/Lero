package com.jalloft.lero.ui.screens.loggedin.main.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.jalloft.lero.R
import com.jalloft.lero.data.domain.User
import com.jalloft.lero.ui.components.ImageProfileEmpty
import com.jalloft.lero.ui.components.PhotoItem
import com.jalloft.lero.ui.screens.viewmodel.LeroViewModel
import com.jalloft.lero.util.DateUtil
import com.valentinilk.shimmer.shimmer


@Composable
fun ProfileScreen(
    onBackSignIn: () -> Unit,
    onPreferences: () -> Unit,
    leroViewModel: LeroViewModel,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        if (leroViewModel.isLoadingCurrentUser) {
            LoadingContent()
        } else if (leroViewModel.currentUser != null) {
            leroViewModel.currentUser?.let { currentUser ->
                ProfileContent(user = currentUser, onPreferences,
                    onEditProfile = { leroViewModel.signOut() })
            }
        } else {
            onBackSignIn()
        }

    }
}

@Composable
fun LoadingContent() {
    Column {
        ShimmerBox(
            modifier = Modifier
                .width(100.dp)
                .height(24.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(3) {
                ShimmerBox(
                    modifier = Modifier
                        .height(100.dp)
                        .weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(2) {
                ShimmerBox(
                    modifier = Modifier
                        .height(100.dp)
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun ShimmerBox(modifier: Modifier) {
    Box(
        modifier = modifier
            .shimmer()
            .padding(horizontal = 16.dp)
            .background(
                MaterialTheme.colorScheme.onBackground.copy(.2f),
                RoundedCornerShape(10.dp)
            )

    )
}

@Composable
fun ProfileContent(user: User, onPreferences: () -> Unit, onEditProfile: () -> Unit) {
    Column {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSecondary
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                val request = ImageRequest.Builder(LocalContext.current)
                    .data(user.profilePhoto?.url)
                    .build()

                SubcomposeAsyncImage(
                    model = request,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    when (val state = painter.state) {
                        is AsyncImagePainter.State.Loading -> {
                            ImageProfileEmpty(modifier = Modifier.shimmer())
                        }

                        is AsyncImagePainter.State.Success -> {
                            Image(
                                painter = state.painter,
                                contentDescription = user.name,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )
                        }

                        is AsyncImagePainter.State.Error -> {
                            ImageProfileEmpty()
                        }

                        AsyncImagePainter.State.Empty -> {}
                    }
                }

                Text(
                    text = "${user.name.orEmpty()}, ${DateUtil.calculateAge(user.dateOfBirth?.toDate())}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileOption(
                        modifier = Modifier.weight(1f),
                        icon = painterResource(id = R.drawable.ic_preferences),
                        text = stringResource(R.string.preferences),
                        onClick = onPreferences
                    )

                    ProfileOption(
                        modifier = Modifier.weight(1f),
                        icon = painterResource(id = R.drawable.ic_edit_profile),
                        text = stringResource(R.string.edit_profile),
                        onClick = onEditProfile
                    )

                    ProfileOption(
                        modifier = Modifier.weight(1f),
                        icon = painterResource(id = R.drawable.ic_dating_profile),
                        text = stringResource(R.string.dating_profile),
                        onClick = {

                        }
                    )
                }
            }
        }
        if (user.photos.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 78.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_empty_photos),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )
                    Text(
                        text = stringResource(R.string.no_photos_yet),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(user.photos.size) { index ->
                    val item = user.photos[index]
                    PhotoItem(
                        url = item.url,
                        modifier = Modifier
                            .height(120.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(5.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileOption(modifier: Modifier, icon: Painter, text: String, onClick: () -> Unit) {
    Surface(
        modifier = modifier,
        color = Color.Transparent,
        shape = RoundedCornerShape(10.dp),
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground.copy(.1f))
            ) {
                Icon(painter = icon, contentDescription = text, modifier = Modifier.padding(8.dp))
            }

            Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

