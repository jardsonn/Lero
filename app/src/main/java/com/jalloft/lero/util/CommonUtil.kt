package com.jalloft.lero.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.jalloft.lero.BuildConfig
import com.jalloft.lero.R
import java.io.File

/**
 * Created by Jardson Costa on 05/02/2024.
 */
object CommonUtil {

    fun getImageBitmap(context: Context, @DrawableRes resId: Int): ImageBitmap? {
        return ContextCompat.getDrawable(context, resId)?.toBitmap()?.asImageBitmap()
    }

    fun Context.findActivity(): Activity {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("Permissions should be called in the context of an Activity")
    }

    fun createPhotoFile(context: Context): Uri {
        val tempFile = File.createTempFile(
            "IMG_${System.currentTimeMillis()}", ".jpg", context.cacheDir
        ).apply {
            createNewFile()
        }
        return FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.provider",
            tempFile
        )
    }
}