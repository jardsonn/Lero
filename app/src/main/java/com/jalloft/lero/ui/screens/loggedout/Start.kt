package com.jalloft.lero.ui.screens.loggedout

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.jalloft.lero.R
import com.jalloft.lero.ui.screens.loggedout.viewmodel.LoggedOutViewModel
import com.jalloft.lero.util.CommonUtil
import timber.log.Timber


@Composable
fun StartScreen(
    onBack: () -> Unit,
    onSignInWithNumber: () -> Unit,
    onAuthenticated: (FirebaseUser?) -> Unit,
    viewModel: LoggedOutViewModel
) {

    val context = LocalContext.current

//    var isSignInLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel.signInWithGoogleSuccess, block = {
        if (viewModel.signInWithGoogleSuccess != null) {
            Timber.i("SignInWithGoogle::Success autenticado")
            onAuthenticated(viewModel.signInWithGoogleSuccess)
        }
    })

    val googleLoginLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            println("O codigo é esse: ${it.resultCode} e o outro é esse ${Activity.RESULT_OK}")
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                viewModel.handleGoogleSignInResult(context, task)
            }
        }
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .navigationBarsPadding()
        .drawBehind {
            CommonUtil
                .getImageBitmap(context, R.drawable.image_background_top_sart)
                ?.let {
                    drawImage(
                        image = it,
                        topLeft = Offset(-50.dp.toPx(), -20.dp.toPx())
                    )
                }
            CommonUtil
                .getImageBitmap(context, R.drawable.image_background_top_end)
                ?.let {
                    drawImage(
                        image = it,
                        topLeft = Offset((size.width - it.width + 38.dp.toPx()), 50.dp.toPx())
                    )
                }
        }) {

        Icon(
            painter = painterResource(id = R.drawable.lero_logo),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 48.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SignInButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.entrar_com_o_google).uppercase(),
                icon = painterResource(id = R.drawable.ic_google),
                enabled = !viewModel.signInWithGoogleLoading,
                onClick = {
                    val signInOptions = GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, signInOptions)
                    val signInIntent = googleSignInClient.signInIntent
                    googleLoginLauncher.launch(signInIntent)
                }
            )

            SignInButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.entrar_com_o_facebook).uppercase(),
                icon = painterResource(id = R.drawable.ic_facebook),
                enabled = !viewModel.signInWithGoogleLoading,
                onClick = {}
            )

            SignInButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.entrar_com_o_celular).uppercase(),
                icon = painterResource(id = R.drawable.ic_sms),
                enabled = !viewModel.signInWithGoogleLoading,
                onClick = onSignInWithNumber
            )


        }

    }
}

@Composable
fun SignInButton(modifier: Modifier, text: String, icon: Painter, enabled: Boolean = true, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = 52.dp),
        enabled = enabled,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(.2f)),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Icon(
            painter = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )
    }
}
