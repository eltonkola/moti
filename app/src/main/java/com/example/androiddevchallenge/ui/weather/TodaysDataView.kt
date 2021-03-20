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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.data.CurrentWeatherData
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.utils.formatToTwoDecimals
import com.example.androiddevchallenge.utils.toIcon
import com.example.androiddevchallenge.utils.toTime
import com.example.androiddevchallenge.utils.toTodayDate
import com.example.androiddevchallenge4.R
import kotlin.math.roundToLong

@ExperimentalAnimationApi
@Composable
fun TodaysDataView(data: CurrentWeatherData, useCelcius: Boolean, modifier: Modifier = Modifier) {

    val today = data.weather.consolidated_weather.first()

    Box(modifier = modifier) {

        Column(
            modifier = Modifier
                .padding(top = 52.dp, start = 16.dp, end = 16.dp)
        ) {

            Text(
                text = today.applicable_date.toTodayDate("Today, "),
                style = MaterialTheme.typography.h4,
                modifier = Modifier,
                color = Color.White
            )
            Text(
                text = data.location.title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier,
                color = Color.White
            )

            Text(
                text = "The suns rises at ${data.weather.sun_rise.toTime()} and sets at ${data.weather.sun_set.toTime()}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier,
                fontSize = 12.sp,
                color = Color.White
            )

            Row(
                modifier = Modifier.padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Column() {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Max",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.width(36.dp),
                            color = Color.White
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_max),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = "${today.max_temp.formatToTwoDecimals(useCelcius)}°",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(start = 2.dp),
                            color = Color.White
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Min",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.width(36.dp),
                            color = Color.White
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_min),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = "${today.min_temp.formatToTwoDecimals(useCelcius)}°",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(start = 2.dp),
                            color = Color.White
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                ) {}

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = today.weather_state_abbr.toIcon()),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(42.dp),
                        )
                        val unit = if (useCelcius) "C" else "F"
                        Text(
                            text = "${today.the_temp.formatToTwoDecimals(useCelcius)}°$unit",
                            style = MaterialTheme.typography.h1,
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.White
                        )
                    }
                    Text(
                        text = today.weather_state_name,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier,
                        color = Color.White
                    )
                    Text(
                        text = "Humidity: ${today.humidity}%",
                        style = MaterialTheme.typography.body1,
                        fontSize = 12.sp,
                        modifier = Modifier,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Wind speed : ${today.wind_speed.roundToLong()} mph, direction: ${today.wind_direction_compass}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier,
                fontSize = 12.sp,
                color = Color.White
            )
            Text(
                text = "Air pressure: ${today.air_pressure} mbar",
                style = MaterialTheme.typography.body1,
                modifier = Modifier,
                fontSize = 12.sp,
                color = Color.White
            )

            Text(
                text = "Visibility: ${today.visibility.formatToTwoDecimals(true)} miles",
                style = MaterialTheme.typography.body1,
                modifier = Modifier,
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview("AppBarScreenPreview", widthDp = 360, heightDp = 640)
@Composable
fun TodaysDataViewPreview() {
    MyTheme() {
        // TODO -pass mocked data
        // TodaysDataView(mockViewModel(LocalContext.current))
    }
}
