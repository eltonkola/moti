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
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue.Expanded
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign.Start
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.WeatherViewModel
import com.example.androiddevchallenge.data.CurrentWeatherData
import com.example.androiddevchallenge.utils.DrawerShape
import com.example.androiddevchallenge.utils.formatToTwoDecimals
import com.example.androiddevchallenge.utils.toDayName
import com.example.androiddevchallenge.utils.toTimeAgo
import com.example.androiddevchallenge4.R
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.LineChartData.Point
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.YAxisDrawer
import org.joda.time.DateTime

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun NextDaysView(
    bottomSheetState: BottomSheetState,
    data: CurrentWeatherData,
    lastUpdate: DateTime,
    useCelcius: Boolean,
    modifier: Modifier = Modifier
) {

    val arrowRotation = if (bottomSheetState.progress.from == Expanded) {
        if (bottomSheetState.progress.fraction == 1.0f) {
            180f
        } else {
            180F - bottomSheetState.progress.fraction * 180F
        }
    } else {
        if (bottomSheetState.progress.fraction == 1.0f) {
            0F
        } else {
            bottomSheetState.progress.fraction * 180F
        }
    }

    val topDrawerArch = if (bottomSheetState.progress.from == Expanded) {
        if (bottomSheetState.progress.fraction == 1.0f) {
            0F
        } else {
            bottomSheetState.progress.fraction * 90F
        }
    } else {
        if (bottomSheetState.progress.fraction == 1.0f) {
            90f
        } else {
            90F - bottomSheetState.progress.fraction * 90F
        }
    }

    val viewModel: WeatherViewModel = viewModel()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(600.dp)
            .clip(DrawerShape(topDrawerArch.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.2f),
                        Color.DarkGray.copy(alpha = 0.8f),
                        Color.Black
                    )
                ),
                alpha = 0.8f
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_up),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .rotate(arrowRotation),
        )

        val emptyY = object : YAxisDrawer {
            override fun drawAxisLabels(
                drawScope: DrawScope,
                canvas: Canvas,
                drawableArea: Rect,
                minValue: Float,
                maxValue: Float
            ) {
            }

            override fun drawAxisLine(
                drawScope: DrawScope,
                canvas: Canvas,
                drawableArea: Rect
            ) {
            }
        }

        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(end = 6.dp)
                .offset(x = (-20).dp),
            lineChartData = LineChartData(
                data.weather.consolidated_weather.map {
                    Point(
                        it.the_temp.formatToTwoDecimals(useCelcius).toFloat(),
                        it.applicable_date.toDayName()
                    )
                }
            ),
            pointDrawer = FilledCircularPointDrawer(diameter = 6.dp, color = Color.White),
            lineDrawer = SolidLineDrawer(thickness = 1.dp, color = Color.White),
            xAxisDrawer = SimpleXAxisDrawer(10.sp, Color.White, 1, 0.1.dp, Color.White),
            yAxisDrawer = emptyY,
            horizontalOffset = 2f,
            animation = tween(2000),
        )

        Text(
            text = "Next 5 days predictions:",
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 12.dp, top = 20.dp),
            color = Color.White
        )
        Divider(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .height(0.5.dp)
                .background(Color.DarkGray)
        )

        val min = data.weather.consolidated_weather.minOf { it.min_temp }
        val max = data.weather.consolidated_weather.maxOf { it.max_temp }

        data.weather.consolidated_weather.forEachIndexed { index, weatherDayData ->
            if (index > 0) {
                NextDayWeatherRow(weatherDayData, min.toInt(), max.toInt(), useCelcius)
                Divider(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .height(0.5.dp)
                        .background(Color.DarkGray)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = "Last update ${lastUpdate.toTimeAgo()}",
                style = MaterialTheme.typography.h5,
                modifier = Modifier,
                fontSize = 12.sp,
                textAlign = Start,
                color = Color.White
            )

            Divider(modifier = Modifier.height(10.dp).weight(1f))

            val versionName = LocalContext.current.packageManager
                .getPackageInfo(LocalContext.current.packageName, 0).versionName

            Text(
                text = "V $versionName",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.clickable { viewModel.reload() },
                fontSize = 12.sp,
                textAlign = Start,
                color = Color.White
            )
        }
    }
}
