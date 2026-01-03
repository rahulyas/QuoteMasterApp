package com.quotemaster.quotemasterapp.presentation.component

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ViewCapture(
    modifier: Modifier = Modifier,
    onViewReady: (View) -> Unit,
    content: @Composable () -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            FrameLayout(context).apply {
                id = View.generateViewId()
                onViewReady(this)
            }
        },
        update = { frameLayout ->
            frameLayout.removeAllViews()
            ComposeView(frameLayout.context).apply {
                setContent { content() }
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                frameLayout.addView(this)
            }
        }
    )
}