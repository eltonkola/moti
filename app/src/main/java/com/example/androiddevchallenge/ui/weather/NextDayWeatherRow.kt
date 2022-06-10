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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.data.WeatherDayData
import com.example.androiddevchallenge.utils.formatToTwoDecimals
import com.example.androiddevchallenge.utils.toFullDayDateName
import com.example.androiddevchallenge.utils.toIcon

@ExperimentalAnimationApi
@Composable
fun NextDayWeatherRow(day: WeatherDayData, min: Int, max: Int, useCelcius: Boolean, modifier: Modifier = Modifier) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Column(modifier = Modifier.width(70.dp)) {

            Text(
                text = day.applicable_date.toFullDayDateName().toUpperCase(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 2.dp),
                color = Color.White
            )

            Text(
                text = "${day.predictability}%",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 2.dp),
                color = Color.White
            )
        }

        Icon(
            painter = painterResource(id = day.weather_state_abbr.toIcon()),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .padding(start = 6.dp, end = 6.dp)
                .size(36.dp),
        )

        val aaPad = getpaddingStart(min, max, day.min_temp.toInt(), day.max_temp.toInt())
        Row(
            modifier = Modifier.height(22.dp).weight(1F).padding(
                start = (if(aaPad> 0) aaPad else 0).dp
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = "${day.min_temp.formatToTwoDecimals(useCelcius)}°",
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 2.dp),
                color = Color.White
            )

            Button(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp).width(getWidthBar(min, max, day.min_temp.toInt(), day.max_temp.toInt()).dp),
                shape = RoundedCornerShape(50),
                onClick = {},
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {}

            Text(
                text = "${day.max_temp.formatToTwoDecimals(useCelcius)}°",
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 2.dp),
                color = Color.White
            )
        }
    }
}

const val maxWidth = 90

private fun getWidthBar(min: Int, max: Int, myMin: Int, myMax: Int): Int {
    val sa = maxWidth / (max - min)
    return sa * myMax - myMin
}

private fun getpaddingStart(min: Int, max: Int, myMin: Int, myMax: Int): Int {
    val sa = maxWidth / (max - min)
    val res = sa * myMin - min
    return res + 4
}
