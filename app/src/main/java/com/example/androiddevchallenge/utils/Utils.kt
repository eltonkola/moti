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

import com.example.androiddevchallenge4.R
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.DateTimeFormatterBuilder
import org.joda.time.format.PeriodFormatterBuilder
import java.math.BigDecimal
import java.math.RoundingMode

fun String.toTodayDate(prefix: String = ""): String {

    val monthAndYear = DateTimeFormatterBuilder()
        .appendLiteral(prefix)
        .appendDayOfMonth(2)
        .appendLiteral(' ')
        .appendMonthOfYearText()
        .appendLiteral(' ')
        .appendYear(2, 2)
        .toFormatter()

    val date = DateTime(this)

    return monthAndYear.print(date)
}

fun String.toDayName(): String {

    val monthAndYear = DateTimeFormatterBuilder()
        .appendDayOfWeekShortText()
        .toFormatter()

    val date = DateTime(this)

    return monthAndYear.print(date)
}

fun String.toFullDayDateName(): String {

    val monthAndYear = DateTimeFormatterBuilder()
        .appendDayOfWeekShortText()
        .appendLiteral(' ')
        .appendDayOfMonth(2)
        .toFormatter()

    val date = DateTime(this)

    return monthAndYear.print(date)
}

fun String.toTime(): String {
    val monthAndYear = DateTimeFormatterBuilder()
        .appendHourOfDay(2)
        .appendLiteral(':')
        .appendMinuteOfHour(2)
        .toFormatter()

    val date = DateTime(this)

    return monthAndYear.print(date)
}

fun Double.formatToTwoDecimals(celcius: Boolean): String {
    return BigDecimal(if (celcius) this else this.toF()).setScale(2, RoundingMode.HALF_EVEN).toString()
}

fun Double.toF(): Double {
    return ((this * 9) / 5) + 32
}

fun DateTime.toTimeAgo(): String {

    val now = DateTime()
    val period = Period(this, now)

    val formatter = PeriodFormatterBuilder()
        .appendHours().appendSuffix(" hours ")
        .appendMinutes().appendSuffix(" minutes ")
        .appendSeconds().appendSuffix(" seconds ago")
        .printZeroNever()
        .toFormatter()

    return formatter.print(period)
}

fun String.toIcon(): Int {
    return when (this) {
        "c" -> R.drawable.ic_c
        "h" -> R.drawable.ic_h
        "hc" -> R.drawable.ic_hc
        "hr" -> R.drawable.ic_hr
        "lc" -> R.drawable.ic_lc
        "lr" -> R.drawable.ic_lr
        "s" -> R.drawable.ic_s
        "sl" -> R.drawable.ic_sl
        "sn" -> R.drawable.ic_sn
        else -> R.drawable.ic_t
    }
}
