package com.jalloft.lero.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.jalloft.lero.R

/**
 * Created by Jardson Costa on 05/02/2024.
 */
object CommonUtil {

    fun getImageBitmap(context: Context, @DrawableRes resId: Int): ImageBitmap?{
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
}