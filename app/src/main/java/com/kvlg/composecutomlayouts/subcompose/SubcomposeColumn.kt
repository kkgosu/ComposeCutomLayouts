package com.kvlg.composecutomlayouts.subcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/**
 * Column, который приводит ширину детей к размеру наиболее широкого из них
 *
 * @author Konstantin Koval on 14.04.2023
 */
@Composable
fun SubcomposeColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        var subcomposeIndex = 0
        var placeables: List<Placeable> = subcompose(subcomposeIndex++, content).map {
            it.measure(constraints)
        }
        val columnSize =
            placeables.fold(IntSize.Zero) { currentMax: IntSize, placeable: Placeable ->
                IntSize(
                    width = maxOf(currentMax.width, placeable.width),
                    height = currentMax.height + placeable.height
                )
            }

        // Снова измеряем каждый элемент, используя ширину самого длинного элемента,
        // используя ее как минимальную ширину
        if (placeables.isNotEmpty() && placeables.size > 1) {
            placeables = subcompose(subcomposeIndex, content).map { measurable: Measurable ->
                measurable.measure(Constraints(columnSize.width, constraints.maxWidth))
            }
        }

        layout(columnSize.width, columnSize.height) {
            var yPos = 0
            placeables.forEach { placeable: Placeable ->
                placeable.placeRelative(0, yPos)
                yPos += placeable.height
            }
        }
    }
}

@Preview
@Composable
fun SubcomposeColumnDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SubcomposeColumn {
            Text(text = "smol", modifier = Modifier.background(Color.Red), color = Color.White)
            Text(
                text = "Lorem Ipsum is simply dummy text",
                modifier = Modifier.background(Color.Blue),
                color = Color.White
            )
        }
        SubcomposeColumn {
            Text(text = "smol", modifier = Modifier.background(Color.Red), color = Color.White)
            Text(
                text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                modifier = Modifier.background(Color.Blue),
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun SubcomposeSimpleBoxWithConstraintsDemo() {
    BoxWithConstraints {
        val rectangleHeight = 50.dp
        if (maxHeight < rectangleHeight * 2) {
            Box(
                Modifier
                    .size(50.dp, rectangleHeight)
                    .background(Color.Blue)
            )
        } else {
            Column {
                Box(
                    Modifier
                        .size(50.dp, rectangleHeight)
                        .background(Color.Blue)
                )
                Box(
                    Modifier
                        .size(50.dp, rectangleHeight)
                        .background(Color.Gray)
                )
            }
        }
    }
}