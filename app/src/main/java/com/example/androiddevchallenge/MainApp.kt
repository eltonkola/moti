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
package com.example.androiddevchallenge

import android.app.Application
import com.example.androiddevchallenge.data.PreferencesStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ActivityComponent

@HiltAndroidApp
class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}

@Module
@InstallIn(ActivityComponent::class)
abstract class AnalyticsModule {

//    @Provides
//    fun providePreferencesStore(context: Context): PreferencesStore{
//        return PreferencesStore(context)
//    }

    @Binds
    abstract fun bindPreferencesStore(preferencesStore: PreferencesStore): PreferencesStore
}
