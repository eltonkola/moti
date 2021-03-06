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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.CurrentWeather
import com.example.androiddevchallenge.CurrentWeather.Ready
import com.example.androiddevchallenge.WeatherViewModel
import com.example.androiddevchallenge.ui.settings.SettingsScreen
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.weather.WeatherScreen

@ExperimentalMaterialApi
@ExperimentalUnsignedTypes
@ExperimentalAnimationApi
@Composable
fun WeatherAppScreen() {

    val viewModel: WeatherViewModel = viewModel()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // based on the state of the weather information, we will show an empty, loading, or the weather view
        when (viewModel.weather) {
            is Ready -> {
                val useCelcius = viewModel.useCelsius.collectAsState(initial = true)

                WeatherScreen((viewModel.weather as Ready).data, (viewModel.weather as Ready).lastUpdate, useCelcius.value)
            }
            is CurrentWeather.Loading -> {
                LoadingScreen()
            }
            else -> {
                EmptyScreen()
            }
        }
        // actionbar and settings dialog, will be on top of all
        if (viewModel.weather !is CurrentWeather.Loading) {
            SettingsScreen(modifier = Modifier.fillMaxSize())
        }
    }
}

@ExperimentalUnsignedTypes
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    // LocalContext.current
    MyTheme {
        WeatherAppScreen()
    }
}
