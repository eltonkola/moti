/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.utils

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme

class DrawerShape(private val top: Dp) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0F, top.value)
            // lineTo(size.width, top.value)

            quadraticBezierTo(size.width / 2, -top.value, size.width, top.value)
            lineTo(size.width, size.height)
            lineTo(0F, size.height)
            lineTo(0F, top.value)
            close()
        }
        return Outline.Generic(path)
    }
}

@ExperimentalAnimationApi
@Preview("ShapePreview", widthDp = 360, heightDp = 640)
@Composable
fun ShapePreview() {
    MyTheme {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Green).padding(40.dp),
        ) {
            Box(
                modifier = Modifier.size(200.dp).clip(
                    DrawerShape(
                        100
                            .dp
                    )
                ).background(Color.Red),
            )
        }
    }
}
