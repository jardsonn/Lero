package com.jalloft.lero.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.jalloft.lero.R
import com.valentinilk.shimmer.shimmer


@Composable
fun PhotoItem(
    url: String?,
    modifier: Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    onRemove: (() -> Unit)? = null
) {
    val request = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .build()

    Box(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.onBackground.copy(.05f),
                RoundedCornerShape(5.dp)
            )
    ) {

        SubcomposeAsyncImage(
            model = request, contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
        ) {
            when (val state = painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    Surface(
                        color = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground.copy(.1f),
                        shape = CircleShape,
                        modifier = Modifier.shimmer()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(16.dp),
                        )
                    }
                }

                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = state.painter,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape)
                    )
                }

                is AsyncImagePainter.State.Error -> {
                    Surface(
                        color = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground.copy(.5f),
                        shape = CircleShape,
                        modifier = Modifier.shimmer()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(16.dp),
                        )
                    }
                }

                AsyncImagePainter.State.Empty -> {}
            }
        }

        if (onRemove != null){
            IconButton(
                onClick = { onRemove() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp)
                    .padding(4.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    containerColor = MaterialTheme.colorScheme.onBackground.copy(.5f),
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Clear,
                    contentDescription = "Remove photo",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        }

}

@Composable
fun ImageProfileEmpty(
    modifier: Modifier = Modifier,
    painter: Painter = painterResource(id = R.drawable.ic_profile),
    size: Dp = 100.dp
) {
    Surface(
        color = MaterialTheme.colorScheme.onBackground.copy(.2f),
        contentColor = MaterialTheme.colorScheme.onBackground.copy(.5f),
        shape = CircleShape,
        modifier = modifier.size(size)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .padding(16.dp),
        )
    }
}
