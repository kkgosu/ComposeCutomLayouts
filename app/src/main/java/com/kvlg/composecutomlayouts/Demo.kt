package com.kvlg.composecutomlayouts

import androidx.compose.runtime.Composable

sealed class Demo(val title: String) {
    override fun toString() = title
}

class ComposableDemo(title: String, val content: @Composable (onNavigateUp: () -> Unit) -> Unit) :
    Demo(title)

class DemoCategory(title: String, val demos: List<Demo>) : Demo(title)
