package com.kvlg.composecutomlayouts.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

@Composable
fun SimpleLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = { measurables, constrains ->
            // this: MeasureScope
            layout(500, 500) {
                // this: PlaceableScope
            }
        }
    )
}

@Composable
@Preview
fun SimpleLayout1_1(modifier: Modifier = Modifier) {
    Layout(modifier = modifier, content = {
        Text(text = "Top left")
        Text(text = "Top right")
        Text(text = "Bottom left")
        Text(text = "Bottom right")
    }, measurePolicy = { measurables, constrains ->
        val placeables = measurables.map { it.measure(constrains) }
        layout(constrains.maxWidth, constrains.maxHeight) {
            placeables.forEachIndexed { index, it ->
                when (index) {
                    0 -> it.place(0, 0)
                    1 -> it.place(constrains.maxWidth - it.width, 0)
                    2 -> it.place(0, constrains.maxHeight - it.height)
                    3 -> it.place(constrains.maxWidth - it.width, constrains.maxHeight - it.height)
                }
            }
        }
    })
}

@Composable
@Preview
fun SimpleLayout1_2(modifier: Modifier = Modifier) {
    Layout(modifier = modifier, content = {
        Text(text = "Top left", modifier = Modifier.layoutId(0))
        Text(text = "Bottom left", modifier = Modifier.layoutId(2))
        Text(text = "Bottom right", modifier = Modifier.layoutId(3))
        Text(text = "Top right", modifier = Modifier.layoutId(1))
    }, measurePolicy = { measurables, constrains ->
        val placeables = measurables.sortedBy { it.layoutId as Int }.map { it.measure(constrains) }
        layout(constrains.maxWidth, constrains.maxHeight) {
            placeables.forEachIndexed { index, it ->
                when (index) {
                    0 -> it.place(0, 0)
                    1 -> it.place(constrains.maxWidth - it.width, 0)
                    2 -> it.place(0, constrains.maxHeight - it.height)
                    3 -> it.place(constrains.maxWidth - it.width, constrains.maxHeight - it.height)
                }
            }
        }
    })
}

@Composable
fun Timeline1(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        layout(constraints.maxWidth, constraints.maxHeight) {
            var x = 0
            var y = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = x, y = y)
                x += placeable.width
                y += placeable.height
            }
        }
    }
}

@Composable
fun Timeline2(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        layout(constraints.maxWidth, constraints.maxHeight) {
            var x = 0
            var y = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = x, y = y)
                val layoutId = (placeable as? Measurable)?.layoutId
                when (layoutId) {
                    "parallel" -> {}
                    "end" -> x = 0
                    null -> x += placeable.width
                }
                y += placeable.height
            }
        }
    }
}

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        layout(constraints.maxWidth, constraints.maxHeight) {
            var x = 0
            var y = 0
            placeables.forEach { placeable ->
                val position = ((placeable as? Measurable)?.parentData as? PositionParentData)?.position
                placeable.placeRelative(x = x, y = y) // Place children
                when (position) {
                    Position.PARALLEL -> {}
                    Position.END -> x = 0
                    null -> x += placeable.width
                }
                y += placeable.height + 8.dp.roundToPx()
            }
        }
    }
}

enum class Position {
    END, PARALLEL
}

class PositionParentData(
    val position: Position
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any =
        this@PositionParentData
}

fun Modifier.position(position: Position) = this then PositionParentData(position)

@Preview
@Composable
fun Timeline2Demo() {
    val color1 = Color(0xFF7e57c2)
    val color2 = Color(0xFF8e24aa)
    val color3 = Color(0xFFe91e63)
    val shape = RoundedCornerShape(16.dp)
    Timeline {
        Box(
            modifier = Modifier
                .size(50.dp, 18.dp)
                .clip(shape)
                .background(color1)
        )
        Box(
            modifier = Modifier
                .size(164.dp, 18.dp)
                .clip(shape)
                .background(color1)
                .layoutId("parallel")
        )
        Box(
            modifier = Modifier
                .size(72.dp, 18.dp)
                .clip(shape)
                .background(color1)
        )
        Box(
            modifier = Modifier
                .size(26.dp, 18.dp)
                .clip(shape)
                .background(color1)
        )
        Box(
            modifier = Modifier
                .size(72.dp, 18.dp)
                .clip(shape)
                .background(color1)
                .layoutId("parallel")
        )
        Box(
            modifier = Modifier
                .size(50.dp, 18.dp)
                .clip(shape)
                .background(color1)
                .layoutId("end")
        )
        Box(
            modifier = Modifier
                .size(28.dp, 18.dp)
                .clip(shape)
                .background(color2)
        )
        Box(
            modifier = Modifier
                .size(150.dp, 18.dp)
                .clip(shape)
                .background(color2)
        )
        Box(
            modifier = Modifier
                .size(50.dp, 18.dp)
                .clip(shape)
                .background(color2)
                .layoutId("parallel")
        )
        Box(
            modifier = Modifier
                .size(75.dp, 18.dp)
                .clip(shape)
                .background(color2)
                .layoutId("end")
        )
        Box(
            modifier = Modifier
                .size(80.dp, 18.dp)
                .clip(shape)
                .background(color3)
        )
        Box(
            modifier = Modifier
                .size(78.dp, 18.dp)
                .clip(shape)
                .background(color3)
                .layoutId("parallel")
        )
        Box(
            modifier = Modifier
                .size(50.dp, 18.dp)
                .clip(shape)
                .background(color3)
        )
        Box(
            modifier = Modifier
                .size(75.dp, 18.dp)
                .clip(shape)
                .background(color3)
        )
    }
}

@Preview
@Composable
fun TimelineLayoutDemo() {
    Timeline {
        val color1 = Color(0xFF7e57c2)
        val color2 = Color(0xFF8e24aa)
        val color3 = Color(0xFFe91e63)
        Box(
            modifier = Modifier
                .size(50.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color1)
        )
        Box(
            modifier = Modifier
                .size(164.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color1)
                .position(Position.PARALLEL)
        )
        Box(
            modifier = Modifier
                .size(72.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color1)
        )
        Box(
            modifier = Modifier
                .size(26.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color1)
        )
        Box(
            modifier = Modifier
                .size(72.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color1)
                .position(Position.PARALLEL)
        )
        Box(
            modifier = Modifier
                .size(50.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color1)
                .position(Position.END)
        )
        Box(
            modifier = Modifier
                .size(28.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color2)
        )
        Box(
            modifier = Modifier
                .size(150.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color2)
        )
        Box(
            modifier = Modifier
                .size(50.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color2)
                .position(Position.PARALLEL)
        )
        Box(
            modifier = Modifier
                .size(75.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color2)
                .position(Position.END)
        )
        Box(
            modifier = Modifier
                .size(80.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color3)
        )
        Box(
            modifier = Modifier
                .size(78.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color3)
                .position(Position.PARALLEL)
        )
        Box(
            modifier = Modifier
                .size(50.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color3)
        )
        Box(
            modifier = Modifier
                .size(75.dp, 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color3)
        )
    }
}