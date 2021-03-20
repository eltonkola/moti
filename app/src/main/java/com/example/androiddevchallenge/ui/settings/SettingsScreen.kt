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
package com.example.androiddevchallenge.ui.settings

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.WeatherViewModel
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge4.R

@ExperimentalAnimationApi
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {

    val viewModel: WeatherViewModel = viewModel()

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(top = 56.dp, start = 16.dp, end = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu_icon),
                contentDescription = "Settings",
                tint = Color.White,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .clickable {
                        viewModel.openSettings.value = true
                    }
            )
            Row(
                modifier = Modifier.height(10.dp).weight(1f),
            ) {}
            Row(
                modifier = Modifier,
            ) {

                val celcius = viewModel.useCelsius.collectAsState(initial = true)

                Text(
                    text = "C°",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.clickable { viewModel.useCelsius(true) },
                    color = if (celcius.value) Color.White else Color.LightGray
                )
                Text(
                    text = " / ",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier,
                    color = Color.LightGray
                )
                Text(
                    text = "F°",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.clickable { viewModel.useCelsius(false) },
                    color = if (!celcius.value) Color.White else Color.LightGray
                )
            }
        }
        SettingsDialog()
    }
}

@ExperimentalAnimationApi
@Preview("AppBarScreenPreview", widthDp = 360, heightDp = 640)
@Composable
fun AppBarScreenPreview() {
    MyTheme() {
        SettingsScreen()
    }
}
