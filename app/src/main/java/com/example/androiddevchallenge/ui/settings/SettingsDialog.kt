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

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.LocationRequest.LocationError
import com.example.androiddevchallenge.LocationRequest.LocationResponse
import com.example.androiddevchallenge.LocationRequest.PermissionDenied
import com.example.androiddevchallenge.LocationRequest.PermissionDeniedPermanently
import com.example.androiddevchallenge.LocationRequest.ShowRational
import com.example.androiddevchallenge.LocationStatus.Error
import com.example.androiddevchallenge.LocationStatus.Loaded
import com.example.androiddevchallenge.LocationStatus.Loading
import com.example.androiddevchallenge.PermissionHelper
import com.example.androiddevchallenge.WeatherViewModel
import com.example.androiddevchallenge.showToast
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge4.R.string

@ExperimentalAnimationApi
@Composable
fun SettingsDialog() {

    val viewModel: WeatherViewModel = viewModel()
    val openSettings = remember { viewModel.openSettings }

    val dialogBackgroundColor: Color by animateColorAsState(
        targetValue = if (openSettings.value) MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f) else Color.Transparent,
        animationSpec = tween(300, easing = LinearEasing)
    )

    val dialogScale: Float by animateFloatAsState(
        if (openSettings.value) 1f else 0f,
        // tween(300, easing = LinearEasing)
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dialogBackgroundColor)
            .padding(52.dp),
        contentAlignment = Alignment.Center,
    ) {

        val locationHelper = PermissionHelper.current
        val openDeniedDialog = remember { mutableStateOf(false) }

        if (viewModel.closeApp.value) {
            locationHelper.finish()
        }

//        ImageBlur(
//            modifier = Modifier.size(260.dp), //.alpha(0.6F),
//            image = ImageBitmap.imageResource(id = R.drawable.default_img ),
//            radius = 25F,
//            context = LocalContext.current
//        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = dialogScale
                    scaleY = dialogScale
                    alpha = 0.9f
                },
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp),
            //  contentColor = Color.White
        ) {

            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primary)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Current location",
                        color = MaterialTheme.colors.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {

                    when (viewModel.userLocation) {
                        is Loaded -> {
                            Text(text = "Location: ${(viewModel.userLocation as Loaded).data.title}")
                        }
                        is Loading -> {
                            Text(text = "Loading..")
                        }
                        is Error -> {
                            Text(text = "Error, location could not be found")
                        }
                        else -> Text(text = "Your location is not set, please click the button below to use your last known location.")
                    }

                    val context = LocalContext.current

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow),
                        onClick = {

                            Log.v("eltonkolaxxx", "---> onclick location")
                            locationHelper.listenForLocation { response ->
                                when (response) {
                                    is LocationResponse -> viewModel.setLocation(response)
                                    is LocationError -> {
                                        context.showToast(string.preferences_location_error)
                                    }
                                    is PermissionDenied -> {
                                        context.showToast(string.preferences_location_denied)
                                    }
                                    is PermissionDeniedPermanently -> {
                                        context.showToast(string.preferences_location_denied_permanent)
                                        viewModel.finish()
                                    }
                                    is ShowRational -> {
                                        openDeniedDialog.value = true
                                    }
                                }
                            }
                        }
                    ) {
                        Text(text = stringResource(id = string.preferences_location))
                    }

                    if (!viewModel.firstTime.collectAsState(initial = true).value) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            shape = RoundedCornerShape(50),
                            onClick = {
                                openSettings.value = false
                            }
                        ) {
                            Text(text = stringResource(id = string.preferences_close))
                        }
                    }
                }
            }

            if (openDeniedDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                    },
                    title = {
                        Text(text = stringResource(id = string.preferences_rational_title))
                    },
                    text = {
                        Text(text = stringResource(id = string.preferences_rational_msg))
                    },
                    confirmButton = {
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                viewModel.refresh()
                            }
                        ) {
                            Text(text = stringResource(id = string.preferences_rational_ok))
                        }
                    }
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview("SettingsDialogPreview", widthDp = 360, heightDp = 640)
@Composable
fun SettingsDialogPreview() {
    MyTheme {
        SettingsDialog()
    }
}
