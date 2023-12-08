package com.kvlg.composecutomlayouts.subcompose

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SubcomposeButton(isLoading: Boolean, modifier: Modifier = Modifier) {
    SubcomposeLayout(modifier = modifier.border(1.dp, Color.Red), measurePolicy = { constraints ->
        var width = 0
        var height = 0
        subcompose("Idle") { IdleButton() }.map {
            val placeable = it.measure(constraints)
            width = placeable.width
            height = placeable.height
        }
        val loadingPlaceable = subcompose("Loading") {
            LoadingButton(isLoading = isLoading, width = width, height = height)
        }.map { it.measure(constraints) }
        layout(width, height) {
            loadingPlaceable.forEach {
                it.placeRelative(
                    x = (width - it.width) / 2,
                    y = (height - it.height) / 2
                )
            }
        }
    })
}

@Composable
@Preview
fun LoadingButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    width: Int = 0,
    height: Int = 0
) {
    val widthInDp = with(LocalDensity.current) { width.toDp() }
    val heightInDp = with(LocalDensity.current) { height.toDp() }
    val shapeAnimation by animateIntAsState(
        targetValue = if (isLoading) 50 else 16,
        animationSpec = tween(durationMillis = 300), label = ""
    )
    val widthAnimation by animateDpAsState(
        targetValue = if (isLoading) 44.dp else widthInDp,
        animationSpec = tween(durationMillis = 300), label = ""
    )
    val heightAnimation by animateDpAsState(
        targetValue = if (isLoading) 44.dp else heightInDp,
        animationSpec = tween(durationMillis = 300), label = ""
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(shapeAnimation))
            .width(widthAnimation)
            .height(heightAnimation)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Crossfade(targetState = isLoading, label = "") {
            if (it) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(44.dp)
                        .padding(4.dp), color = Color.White, strokeWidth = 4.dp
                )
            } else {
                IdleButton()
            }
        }
    }
}

@Preview
@Composable
fun IdleButton(modifier: Modifier = Modifier) {
    Text(
        text = "Load...",
        color = Color.White,
        fontSize = 44.sp,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(vertical = 4.dp, horizontal = 32.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Preview
@Composable
fun LoadingSubcomposeButtonDemo() {
    var isLoading by remember { mutableStateOf(false) }
    val onClick = { isLoading = !isLoading }
    Box(modifier = Modifier.fillMaxSize()) {
        SubcomposeButton(
            isLoading = isLoading,
            modifier = Modifier
                .align(Alignment.Center)
                .clickable(onClick = onClick)
        )
    }
}
