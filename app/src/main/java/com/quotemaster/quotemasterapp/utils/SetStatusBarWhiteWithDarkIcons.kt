package com.quotemaster.quotemasterapp.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun SetStatusBarWhiteWithDarkIcons() {
    val view = LocalView.current
    val context = view.context
    val activity = context.findActivity()

    SideEffect {
        val window = activity.window
        window.statusBarColor = Color.Black.toArgb()

        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = false
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("No Activity found")
}