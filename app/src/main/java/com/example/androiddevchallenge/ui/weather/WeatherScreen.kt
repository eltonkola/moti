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
package com.example.androiddevchallenge.ui.weather

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue.Collapsed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.data.CurrentWeatherData
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge4.R
import com.skydoves.landscapist.coil.CoilImage
import org.joda.time.DateTime

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun WeatherScreen(data: CurrentWeatherData, lastUpdate: DateTime, useCelcius: Boolean, modifier: Modifier = Modifier) {

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(Collapsed)
    )

    BottomSheetScaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            NextDaysView(bottomSheetScaffoldState.bottomSheetState, data, lastUpdate, useCelcius)
        },
        sheetPeekHeight = 252.dp,
        sheetShape = RectangleShape,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.5.dp,
        sheetGesturesEnabled = true,
        sheetContentColor = Color.White,

    ) {

        Box(modifier = modifier) {

            val currentImageIndex = remember { mutableStateOf(0) }

            CoilImage(
                modifier = Modifier.fillMaxSize(),
                imageModel = data.location.image[currentImageIndex.value],
                contentScale = ContentScale.Crop,
                circularRevealedEnabled = true,
                // placeHolder = ImageBitmap.imageResource(R.drawable.default_img),
                error = ImageBitmap.imageResource(R.drawable.default_img)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colors.primary,
                                MaterialTheme.colors.primaryVariant
                            )
                        ),
                        alpha = 0.6f
                    ).pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (data.location.image.size > 1) {
                                    if (data.location.image.size - 1 == currentImageIndex.value) {
                                        currentImageIndex.value = 0
                                    } else {
                                        currentImageIndex.value += 1
                                    }
                                }
                            },
                        )
                    }
            )

            TodaysDataView(
                data,
                useCelcius,
                modifier = Modifier.padding(top = 60.dp)
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview("AppBarScreenPreview", widthDp = 360, heightDp = 640)
@Composable
fun WeatherScreenPreview() {
    MyTheme() {
        //  WeatherScreen(mockViewModel(LocalContext.current))
    }
}
