package com.gkhnakbs.weatherappwithwidget.domain.worker

/**
 * Created by Gökhan Akbaş on 27/10/2025.
 */

// UpdateWeatherWorker.kt
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gkhnakbs.weatherappwithwidget.domain.repository.WeatherRepositoryForWidget
import com.gkhnakbs.weatherappwithwidget.domain.state.WeatherWidgetState
import com.gkhnakbs.weatherappwithwidget.domain.state.WeatherWidgetStateDefinition
import com.gkhnakbs.weatherappwithwidget.presentation.ui.widget.WeatherWidget
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltWorker
class UpdateWeatherWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: WeatherRepositoryForWidget,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(WeatherWidget::class.java)

        if (glanceIds.isEmpty()) {
            return Result.success()
        }

        glanceIds.forEach { glanceId ->
            updateAppWidgetState(context, WeatherWidgetStateDefinition, glanceId) { prefs ->
                prefs.toMutablePreferences().apply {
                    this[WeatherWidgetState.isLoading] = true
                    this[WeatherWidgetState.location] = "Loading..."
                }
            }
            WeatherWidget().update(context, glanceId)
        }

        val result = repository.getWeatherData(39.960108, 32.791761)

        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        if (result == null) {
            glanceIds.forEach { glanceId ->
                updateAppWidgetState(context, WeatherWidgetStateDefinition, glanceId) { prefs ->
                    prefs.toMutablePreferences().apply {
                        this[WeatherWidgetState.isLoading] = false
                        this[WeatherWidgetState.temperature] = "Error"
                        this[WeatherWidgetState.location] = "Data couldn't fetched"
                        this[WeatherWidgetState.humidity] = ""
                        this[WeatherWidgetState.apparentTemperature] = ""
                        this[WeatherWidgetState.lastUpdate] = "Last: $currentTime"
                    }
                }
                WeatherWidget().update(context, glanceId)
            }
            return Result.retry()
        }

        glanceIds.forEach { glanceId ->
            updateAppWidgetState(context, WeatherWidgetStateDefinition, glanceId) { prefs ->
                prefs.toMutablePreferences().apply {
                    this[WeatherWidgetState.isLoading] = false
                    this[WeatherWidgetState.temperature] = "${result.current?.temperature2m}${result.currentUnits?.temperature2m ?: "°C"}"
                    this[WeatherWidgetState.humidity] = "${result.current?.relativeHumidity2m}${result.currentUnits?.relativeHumidity2m ?: "%"}"
                    this[WeatherWidgetState.location] = result.timezone?.split("/")?.lastOrNull() ?: "Bilinmeyen" // Timezone'dan şehir adını al
                    this[WeatherWidgetState.apparentTemperature] = "Feeling: ${result.current?.apparentTemperature}${result.currentUnits?.apparentTemperature ?: "°C"}"
                    this[WeatherWidgetState.lastUpdate] = "Last: $currentTime"
                }
            }
            WeatherWidget().update(context, glanceId)
        }

        return Result.success()
    }
}