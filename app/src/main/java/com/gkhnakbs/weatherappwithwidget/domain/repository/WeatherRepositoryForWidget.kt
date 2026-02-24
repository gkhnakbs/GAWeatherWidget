package com.gkhnakbs.weatherappwithwidget.domain.repository

import com.gkhnakbs.weatherappwithwidget.data.api.WeatherApiService
import com.gkhnakbs.weatherappwithwidget.domain.model.data.WeatherResponse
import javax.inject.Inject

/**
 * Created by Gökhan Akbaş on 27/10/2025.
 */

class WeatherRepositoryForWidget @Inject constructor(
    private val apiService: WeatherApiService
) {
    suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherResponse? {
        return try {
            apiService.getWeatherData(
                latitude = latitude,
                longitude = longitude
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}