package com.kvlg.composecutomlayouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DemoApp(
    currentDemo: Demo,
    onNavigateToDemo: (Demo) -> Unit,
    onNavigateUp: () -> Unit,
) {
    Scaffold { innerPadding ->
        val modifier = Modifier
            .consumeWindowInsets(innerPadding)
            .padding(innerPadding)
        DemoContent(
            modifier,
            currentDemo,
            onNavigateToDemo,
            onNavigateUp
        )
    }
}

@Composable
private fun DemoContent(
    modifier: Modifier,
    currentDemo: Demo,
    onNavigate: (Demo) -> Unit,
    onNavigateUp: () -> Unit
) {
    DisplayDemo(currentDemo, onNavigate, onNavigateUp)
}

@Composable
fun Material2LegacyTheme(content: @Composable () -> Unit) {
    val material2Colors =
        if (isSystemInDarkTheme()) {
            androidx.compose.material.darkColors()
        } else {
            androidx.compose.material.lightColors()
        }
    androidx.compose.material.MaterialTheme(
        colors = material2Colors,
        content = {
            CompositionLocalProvider(
                LocalContentColor provides androidx.compose.material.MaterialTheme.colors.onSurface,
                content = content
            )
        }
    )
}

@Composable
private fun DisplayDemo(demo: Demo, onNavigate: (Demo) -> Unit, onNavigateUp: () -> Unit) {
    when (demo) {
        is ComposableDemo -> Material2LegacyTheme { demo.content(onNavigateUp) }
        is DemoCategory -> DisplayDemoCategory(demo, onNavigate)
    }
}

@Composable
private fun DisplayDemoCategory(category: DemoCategory, onNavigate: (Demo) -> Unit) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        category.demos.forEach { demo ->
            ListItem(onClick = { onNavigate(demo) }) {
                Text(
                    modifier = Modifier
                        .height(56.dp)
                        .wrapContentSize(Alignment.Center),
                    text = demo.title
                )
            }
        }
    }
}

@Composable
internal fun ListItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)
) {
    Box(
        modifier
            .heightIn(min = 48.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        contentAlignment = Alignment.CenterStart
    ) { content() }
}