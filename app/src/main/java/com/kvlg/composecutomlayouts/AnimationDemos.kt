package com.kvlg.composecutomlayouts

import com.kvlg.composecutomlayouts.layout.SimpleLayout1_1
import com.kvlg.composecutomlayouts.layout.TimelineLayoutDemo
import com.kvlg.composecutomlayouts.lookahead.AnimateBoundsModifierDemo
import com.kvlg.composecutomlayouts.lookahead.LookaheadLayoutWithAlignmentLinesDemo
import com.kvlg.composecutomlayouts.lookahead.LookaheadWithFlowRowDemo
import com.kvlg.composecutomlayouts.lookahead.LookaheadWithLazyColumn
import com.kvlg.composecutomlayouts.lookahead.LookaheadWithPopularBoxWithConstraintsUsage
import com.kvlg.composecutomlayouts.lookahead.ScreenSizeChangeDemo
import com.kvlg.composecutomlayouts.lookahead.SharedElementExplorationDemo
import com.kvlg.composecutomlayouts.subcompose.LoadingSubcomposeButtonDemo
import com.kvlg.composecutomlayouts.subcompose.SubcomposeColumnDemo
import com.kvlg.composecutomlayouts.subcompose.SubcomposeSimpleBoxWithConstraintsDemo

val AnimationDemos = DemoCategory(
    "Animation",
    listOf(
        DemoCategory(
            "Layout Demos",
            listOf(
                ComposableDemo("Simple Corner Layout") { SimpleLayout1_1() },
                ComposableDemo("Timeline") { TimelineLayoutDemo() },
            )
        ),
        DemoCategory(
            "Subcompose Layout",
            listOf(
                ComposableDemo("Subcompose Simple BoxWithConstrains") { SubcomposeSimpleBoxWithConstraintsDemo() },
                ComposableDemo("Subcompose Column") { SubcomposeColumnDemo() },
                ComposableDemo("Subcompose Loading Button") { LoadingSubcomposeButtonDemo() }
            )
        ),
        DemoCategory(
            "Lookahead Animation Demos",
            listOf(
                ComposableDemo("AnimateBoundsModifier") {
                    AnimateBoundsModifierDemo()
                },
                ComposableDemo("Screen Size Change Demo") { ScreenSizeChangeDemo() },
                ComposableDemo("Lookahead With Alignment Lines") {
                    LookaheadLayoutWithAlignmentLinesDemo()
                },
                ComposableDemo("Lookahead With PopularBoxWithConstraintsUsage") { LookaheadWithPopularBoxWithConstraintsUsage() },
                ComposableDemo("Lookahead With LazyColumn") { LookaheadWithLazyColumn() },
                ComposableDemo("Lookahead With Flow Row") { LookaheadWithFlowRowDemo() },
                ComposableDemo("Shared Element") {
                    SharedElementExplorationDemo()
                },
            )
        ),
    )
)
