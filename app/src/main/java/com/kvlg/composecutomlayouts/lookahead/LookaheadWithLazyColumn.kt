package com.kvlg.composecutomlayouts.lookahead

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kvlg.composecutomlayouts.R

@Preview
@Composable
fun LookaheadWithLazyColumn() {
    LazyColumn {
        items(10, key = { it }) {
            val index = it % 4
            var expanded by rememberSaveable { mutableStateOf(false) }
            AnimatedVisibility(
                remember { MutableTransitionState(false) }.apply { targetState = true },
                enter = slideInHorizontally { 20 } + fadeIn()
            ) {
                Surface(shape = RoundedCornerShape(10.dp),
                    color = pastelColors[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expanded = !expanded
                        }) {
                    LookaheadScope {
                        val title = remember {
                            movableContentOf {
                                Text(
                                    names[index],
                                    Modifier
                                        .padding(20.dp)
                                        .animateBounds(Modifier),
                                    Color.Black
                                )
                            }
                        }
                        val image = remember {
                            if (index < 3) {
                                movableContentOf {
                                    Image(
                                        painter = painterResource(res[index]),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .animateBounds(
                                                if (expanded)
                                                    Modifier.fillMaxWidth()
                                                else
                                                    Modifier.size(80.dp),
                                                spring(stiffness = Spring.StiffnessLow)
                                            )
                                            .clip(RoundedCornerShape(5.dp)),
                                        contentScale = if (expanded) {
                                            ContentScale.FillWidth
                                        } else {
                                            ContentScale.Crop
                                        }
                                    )
                                }
                            } else {
                                movableContentOf {
                                    Box(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .animateBounds(
                                                if (expanded) Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(1f)
                                                else Modifier.size(80.dp),
                                                spring(stiffness = Spring.StiffnessLow)
                                            )
                                            .background(
                                                Color.LightGray, RoundedCornerShape(5.dp)
                                            ),
                                    )
                                }
                            }
                        }
                        if (expanded) {
                            Column {
                                title()
                                image()
                            }
                        } else {
                            Row {
                                image()
                                title()
                            }
                        }
                    }
                }
            }
        }
    }
}

val names = listOf("YT", "Pepper", "Waffle", "Who?")
val res = listOf(
    R.drawable.yt_profile,
    R.drawable.circus,
    R.drawable.android,
)
val pastelColors = listOf(
    Color(0xFFffd7d7),
    Color(0xFFffe9d6),
    Color(0xFFfffbd0),
    Color(0xFFe3ffd9),
    Color(0xFFd0fff8)
)
