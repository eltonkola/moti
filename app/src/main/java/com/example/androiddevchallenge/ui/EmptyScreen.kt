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
package com.example.androiddevchallenge.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.WeatherViewModel
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge4.R.drawable
@ExperimentalAnimationApi
@Composable
fun EmptyScreen(modifier: Modifier = Modifier) {

    val viewModel: WeatherViewModel = viewModel()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {



//        CoilImage(
//            modifier = Modifier.fillMaxSize(),
//            imageModel = "",
//            contentScale = ContentScale.Crop,
//            circularRevealedEnabled = true,
//            placeHolder = ImageBitmap.imageResource(drawable.default_img),
//            error = ImageBitmap.imageResource(drawable.default_img)
//        )
    }
}

@ExperimentalAnimationApi
@Preview("EmptyScreenPreview", widthDp = 360, heightDp = 640)
@Composable
fun EmptyScreenPreview() {
    MyTheme() {
        EmptyScreen()
    }
}
