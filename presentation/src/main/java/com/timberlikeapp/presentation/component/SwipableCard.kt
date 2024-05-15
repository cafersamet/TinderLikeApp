package com.timberlikeapp.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun SwipableCard(
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var offset by remember { mutableFloatStateOf(0f) }
    var dismissRight by remember { mutableStateOf(false) }
    var dismissLeft by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val width = displayMetrics.widthPixels

    val swipeThreshold = width / 2

    LaunchedEffect(dismissRight) {
        if (dismissRight) {
            onSwipeRight.invoke()
        }
    }

    LaunchedEffect(dismissLeft) {
        if (dismissLeft) {
            onSwipeLeft.invoke()
        }
    }
    if (dismissRight.not() && dismissLeft.not()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(offset.roundToInt(), 0)
                }
                .draggable(
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        if (offset > swipeThreshold) {
                            dismissRight = true
                        } else if (offset < -swipeThreshold) {
                            dismissLeft = true
                        }
                        offset = 0f
                    },
                    state = rememberDraggableState { delta ->
                        offset += delta
                    }
                )
                .rotate(
                    animateFloatAsState(
                        targetValue = offset / 30,
                        label = ""
                    ).value
                )
        ) {
            content()
        }
    }
}