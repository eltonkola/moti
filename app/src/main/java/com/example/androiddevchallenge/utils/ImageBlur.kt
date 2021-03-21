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

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.DefaultTintBlendMode

// to used, was experimenting wit some ui stuff
@Composable
fun ImageBlur(
    modifier: Modifier,
    context: Context,
    radius: Float,
    image: ImageBitmap,
) {
    Canvas(modifier = modifier) {

        val bitmap = Bitmap.createScaledBitmap(image.asAndroidBitmap(), this.size.width.toInt(), this.size.height.toInt(), false)
        val blurred = bitmap.blur(context, radius)?.asImageBitmap()

        blurred?.let {
            drawImage(
                image = it,
                topLeft = Offset.Zero,
                alpha = 1F,
                style = Fill,
                blendMode = DefaultTintBlendMode
            )
        }
    }
}

fun Bitmap.blur(context: Context, radius: Float): Bitmap? {
    val bitmap = copy(config, true)

    RenderScript.create(context).apply {
        val input = Allocation.createFromBitmap(this, this@blur)
        val output = Allocation.createFromBitmap(this, this@blur)

        ScriptIntrinsicBlur.create(this, Element.U8_4(this)).apply {
            setInput(input)
            // Set the radius of the Blur. Supported range 0 < radius <= 25
            setRadius(radius)
            forEach(output)

            output.copyTo(bitmap)
            destroy()
        }
    }
    return bitmap
}
