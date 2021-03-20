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

class WeatherRepository() {

    private val service = WeatherClient<DemoRemoteService>().create(DemoRemoteService::class.java)

    suspend fun getLocation(lat: Double, lon: Double): UserLocation {
        val locations = service.getLocation("$lat,$lon")
        val place = locations.minBy { it.distance }!!

        val normalizedName = place.title.replace(" ", "-")
        val mocks = (0..10).map {
            val y = 2340 + it
            "https://loremflickr.com/1080/$y/$normalizedName"
        }

        val images = try {
            val urban = service.getLocationImageName("$lat,$lon")
            val picture = service.getLocationImage("${urban._embedded.nearestUrbanAreas.first()._links.urbanArea.href}images")

            val real = picture.photos.map { it.image.mobile }

            mutableListOf<String>().apply {
                addAll(real)
                addAll(mocks)
            }.toList()
        } catch (e: Exception) {
            e.printStackTrace()
            mocks
        }

        return UserLocation(place.title, place.woeid, lat, lon, images)
    }

    suspend fun getWeather(location: UserLocation): WeatherData {
        return service.getWeather(location.woeid.toString())
    }
}
