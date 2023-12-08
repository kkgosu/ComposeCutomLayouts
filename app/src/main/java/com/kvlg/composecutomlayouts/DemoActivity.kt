package com.kvlg.composecutomlayouts

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.fragment.app.FragmentActivity

class DemoActivity : FragmentActivity() {
    private lateinit var hostView: View
    private lateinit var focusManager: FocusManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootDemo = AllDemosCategory
        ComposeView(this).also {
            setContentView(it)
        }.setContent {
            hostView = LocalView.current
            focusManager = LocalFocusManager.current
            val navigator = rememberSaveable(
                saver = Navigator.Saver(rootDemo, onBackPressedDispatcher)
            ) {
                Navigator(rootDemo, onBackPressedDispatcher )
            }

            DemoTheme(window) {
                DemoApp(
                    currentDemo = navigator.currentDemo,
                    onNavigateToDemo = { demo ->
                        navigator.navigateTo(demo)
                    },
                    onNavigateUp = {
                        onBackPressed()
                    },
                )
            }

        }
    }
}

@Composable
private fun DemoTheme(
    window: Window,
    content: @Composable () -> Unit
) {
    val colorScheme = darkColorScheme()
    SideEffect {
        window.statusBarColor = colorScheme.background.toArgb()
        window.navigationBarColor = colorScheme.background.toArgb()
    }
    MaterialTheme(colorScheme = colorScheme, content = content)
}

private class Navigator private constructor(
    private val backDispatcher: OnBackPressedDispatcher,
    private val rootDemo: Demo,
    initialDemo: Demo,
    private val backStack: MutableList<Demo>
) {
    constructor(
        rootDemo: Demo,
        backDispatcher: OnBackPressedDispatcher,
    ) : this(backDispatcher, rootDemo, rootDemo, mutableListOf<Demo>())

    private val onBackPressed = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            popBackStack()
        }
    }.apply {
        isEnabled = !isRoot
        backDispatcher.addCallback(this)
    }

    private var _currentDemo by mutableStateOf(initialDemo)
    var currentDemo: Demo
        get() = _currentDemo
        private set(value) {
            _currentDemo = value
            onBackPressed.isEnabled = !isRoot
        }

    val isRoot: Boolean get() = backStack.isEmpty()

    fun navigateTo(demo: Demo) {
        backStack.add(currentDemo)
        currentDemo = demo

    }

    private fun popBackStack() {
        currentDemo = backStack.removeAt(backStack.lastIndex)
    }

    companion object {
        fun Saver(
            rootDemo: Demo,
            backDispatcher: OnBackPressedDispatcher
        ): Saver<Navigator, *> = listSaver<Navigator, String>(
            save = { navigator ->
                (navigator.backStack + navigator.currentDemo).map { it.title }
            },
            restore = { restored ->
                require(restored.isNotEmpty())
                val backStack = restored.mapTo(mutableListOf()) {
                    requireNotNull(findDemo(rootDemo, it, exact = true))
                }
                val initial = backStack.removeAt(backStack.lastIndex)
                Navigator(backDispatcher, rootDemo, initial, backStack)
            }
        )

        fun findDemo(demo: Demo, title: String, exact: Boolean = false): Demo? {
            if (exact) {
                if (demo.title == title) {
                    return demo
                }
            } else {
                if (demo.title.contains(title)) {
                    return demo
                }
            }
            if (demo is DemoCategory) {
                demo.demos.forEach { child ->
                    findDemo(child, title, exact)
                        ?.let { return it }
                }
            }
            return null
        }
    }
}
