package com.gkhnakbs.weatherappwithwidget.domain.receivers

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.gkhnakbs.weatherappwithwidget.domain.worker.UpdateWeatherWorker
import com.gkhnakbs.weatherappwithwidget.presentation.ui.widget.WeatherWidget
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

/**
 * Created by Gökhan Akbaş on 27/10/2025.
 */

@AndroidEntryPoint
class WeatherWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = WeatherWidget()

    private val periodicWorkerTAG = "WeatherWidgetUpdateWork"

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        val workRequest = PeriodicWorkRequestBuilder<UpdateWeatherWorker>(
            45, TimeUnit.MINUTES,// En az 15 dakika
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            periodicWorkerTAG,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WorkManager.getInstance(context).cancelUniqueWork(periodicWorkerTAG)
    }
}