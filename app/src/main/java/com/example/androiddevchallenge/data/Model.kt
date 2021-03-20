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
package com.example.androiddevchallenge.data

import com.google.gson.annotations.SerializedName

data class CurrentWeatherData(val weather: WeatherData, val location: UserLocation)

data class GeographicLocation(
    val distance: Int,
    val title: String,
    val woeid: Int
)

data class UserLocation(
    val title: String,
    val woeid: Int,
    val lat: Double,
    val lon: Double,
    val image: List<String>
)

data class WeatherDayData(
    val id: Long,
    val weather_state_name: String,
    val weather_state_abbr: String,
    val wind_direction_compass: String,
    val created: String,
    val applicable_date: String,
    val min_temp: Double,
    val max_temp: Double,
    val the_temp: Double,
    val wind_speed: Double,
    val wind_direction: Double,
    val air_pressure: Double,
    val humidity: Double,
    val visibility: Double,
    val predictability: Double,
)

data class WeatherData(
    val consolidated_weather: List<WeatherDayData>,
    val time: String,
    val sun_rise: String,
    val sun_set: String,
    val timezone_name: String,
    val title: String,
    val location_type: String,
    val woeid: String,
    val latt_long: String,
    val timezone: String,
)

/*

{
	"consolidated_weather": [{
			"id": 5027643989688320,
			"weather_state_name": "Heavy Cloud",
			"weather_state_abbr": "hc",
			"wind_direction_compass": "ESE",
			"created": "2021-03-17T18:20:43.389353Z",
			"applicable_date": "2021-03-17",
			"min_temp": 1.855,
			"max_temp": 8.475,
			"the_temp": 7.875,
			"wind_speed": 3.726657016882738,
			"wind_direction": 109.93354757997469,
			"air_pressure": 1022,
			"humidity": 72,
			"visibility": 13.22899268273284,
			"predictability": 71
		},
		{},
		{},
		{},
		{},
		{}
	],
	"time": "2021-03-17T15:00:31.994012-04:00",
	"sun_rise": "2021-03-17T07:03:55.312738-04:00",
	"sun_set": "2021-03-17T19:05:15.297886-04:00",
	"timezone_name": "LMT",
	"parent": {},
	"sources": [],
	"title": "New York",
	"location_type": "City",
	"woeid": 2459115,
	"latt_long": "40.71455,-74.007118",
	"timezone": "US/Eastern"
}
*/

data class UrbanSearch(val _embedded: Embedded) {

    data class Embedded(
        @SerializedName("location:nearest-urban-areas")
        val nearestUrbanAreas: List<NearestUrbanAreas>
    )

    data class NearestUrbanAreas(
        val _links: Links
    )

    data class Links(
        @SerializedName("location:nearest-urban-area")
        val urbanArea: UrbanArea
    )

    data class UrbanArea(
        val href: String,
        val name: String
    )
}

data class UrbanImage(val photos: List<Photo>) {

    data class Photo(
        val image: Image
    )

    data class Image(
        val mobile: String,
        val web: String
    )
}
