package com.gkhnakbs.weatherappwithwidget.domain.callbacks



import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.gkhnakbs.weatherappwithwidget.domain.di.WeatherRepositoryEntryPoint
import com.gkhnakbs.weatherappwithwidget.domain.state.WeatherWidgetState
import com.gkhnakbs.weatherappwithwidget.domain.state.WeatherWidgetStateDefinition
import com.gkhnakbs.weatherappwithwidget.presentation.ui.widget.WeatherWidget
import dagger.hilt.android.EntryPointAccessors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Gökhan Akbaş on 27/10/2025.
 */
class RefreshWeatherActionCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, WeatherWidgetStateDefinition, glanceId) { prefs ->
            prefs.toMutablePreferences().apply {
                this[WeatherWidgetState.isLoading] = true
            }
        }
        WeatherWidget().update(context, glanceId)

        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WeatherRepositoryEntryPoint::class.java
        )
        val repository = hiltEntryPoint.weatherRepository()

        try {
            val data = repository.getWeatherData(39.960108, 32.791761)

            data?.let {
                updateAppWidgetState(context, WeatherWidgetStateDefinition, glanceId) { prefs ->
                    prefs.toMutablePreferences().apply {
                        this[WeatherWidgetState.isLoading] = false
                        this[WeatherWidgetState.temperature] = "${data.current?.temperature2m}${data.currentUnits?.temperature2m ?: "°C"}"
                        this[WeatherWidgetState.humidity] = "${data.current?.relativeHumidity2m}${data.currentUnits?.relativeHumidity2m ?: "%"}"
                        this[WeatherWidgetState.location] = data.timezone?.split("/")?.lastOrNull() ?: "Unknown"
                        this[WeatherWidgetState.lastUpdate] = "Last Updated: ${getCurrentTime()}"
                        this[WeatherWidgetState.apparentTemperature] =
                            "Feeling: ${data.current?.apparentTemperature}${data.currentUnits?.apparentTemperature ?: "°C"}"
                    }
                }
            }

        } catch (e: Exception) {
            updateAppWidgetState(context, WeatherWidgetStateDefinition, glanceId) { prefs ->
                prefs.toMutablePreferences().apply {
                    this[WeatherWidgetState.isLoading] = false
                    this[WeatherWidgetState.temperature] = "Error!"
                    this[WeatherWidgetState.humidity] = "Try again"
                    this[WeatherWidgetState.lastUpdate] = "Last Updated: ${getCurrentTime()}"
                    this[WeatherWidgetState.apparentTemperature] = ""
                    this[WeatherWidgetState.location] = "Connection Error"
                }
            }
            Log.e("WeatherWidgetRefresh","Error: ${e.message}", e)
        }
        finally {
            WeatherWidget().update(context, glanceId)
        }
    }

    private fun getCurrentTime(): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    }
}