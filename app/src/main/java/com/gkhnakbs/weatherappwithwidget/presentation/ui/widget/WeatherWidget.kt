package com.gkhnakbs.weatherappwithwidget.presentation.ui.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.color.ColorProvider
import androidx.glance.layout.size
import com.gkhnakbs.weatherappwithwidget.MainActivity
import com.gkhnakbs.weatherappwithwidget.domain.callbacks.RefreshWeatherActionCallback
import com.gkhnakbs.weatherappwithwidget.domain.model.size.WidgetSize
import com.gkhnakbs.weatherappwithwidget.domain.state.WeatherWidgetState
import com.gkhnakbs.weatherappwithwidget.domain.state.WeatherWidgetStateDefinition

/**
 * Created by Gökhan Akbaş on 27/10/2025.
 */

class WeatherWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*>
        get() = WeatherWidgetStateDefinition

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(
            DpSize(55.dp, 180.dp),   // SMALL
            DpSize(170.dp, 180.dp),  // MEDIUM
            DpSize(250.dp, 180.dp)   // LARGE
        )
    )

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        provideContent {
            val size = LocalSize.current

            val prefs = currentState<Preferences>()

            val widgetSize = WidgetSize.getWidgetSize(size)

            val uiState = WeatherWidgetUiState(
                isLoading = prefs[WeatherWidgetState.isLoading] ?: false,
                temperature = prefs[WeatherWidgetState.temperature] ?: "--",
                humidity = prefs[WeatherWidgetState.humidity] ?: "",
                location = prefs[WeatherWidgetState.location] ?: "Loading...",
                apparentTemperature = prefs[WeatherWidgetState.apparentTemperature] ?: "",
                lastUpdate = prefs[WeatherWidgetState.lastUpdate] ?: ""
            )

            if (uiState.isLoading) {
                Box(
                    modifier = GlanceModifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Boyuta göre doğru arayüzü seç
                when (widgetSize) {
                    WidgetSize.SMALL -> WeatherSmall(uiState, context)
                    WidgetSize.MEDIUM -> WeatherMedium(uiState, context)
                    WidgetSize.LARGE -> WeatherLarge(uiState, context)
                }
            }
        }
    }
}


@Composable
fun WeatherLarge(
    uiState: WeatherWidgetUiState,
    context: Context
) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFF1E88E5))
            .cornerRadius(16.dp)
            .padding(16.dp)
            .clickable(
                actionStartActivity(
                    Intent(context, MainActivity::class.java)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.location,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(day = Color.White, night = Color.White)
                    )
                )

                Spacer(modifier = GlanceModifier.width(8.dp))

                Text(
                    text = "🔄",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = ColorProvider(day = Color.White, night = Color.White)
                    ),
                    modifier = GlanceModifier
                        .clickable(actionRunCallback<RefreshWeatherActionCallback>())
                )
            }

            Spacer(modifier = GlanceModifier.height(12.dp))

            Text(
                text = uiState.temperature,
                style = TextStyle(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(day = Color.White, night = Color.White)
                )
            )

            Spacer(modifier = GlanceModifier.height(4.dp))

            if (uiState.apparentTemperature.isNotEmpty()) {
                Text(
                    text = uiState.apparentTemperature,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = ColorProvider(day = Color.White.copy(alpha = 0.9f), night = Color.White.copy(alpha = 0.9f))
                    )
                )
            }

            Spacer(modifier = GlanceModifier.height(12.dp))

            if (uiState.humidity.isNotEmpty()) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "💧 Humidity: ${uiState.humidity}",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = ColorProvider(day = Color.White, night = Color.White)
                        )
                    )
                }
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            if (uiState.lastUpdate.isNotEmpty()) {
                Text(
                    text = uiState.lastUpdate,
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = ColorProvider(day = Color.White.copy(0.7f), night = Color.White.copy(0.7f))
                    )
                )
            }
        }
    }
}

@Composable
fun WeatherMedium(
    uiState: WeatherWidgetUiState,
    context: Context
) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFF1976D2))
            .cornerRadius(16.dp)
            .padding(12.dp)
            .clickable(
                actionStartActivity(
                    Intent(context, MainActivity::class.java)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.location,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(day = Color.White, night = Color.White)
                    )
                )

                Spacer(modifier = GlanceModifier.width(6.dp))

                Text(
                    text = "🔄",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = ColorProvider(day = Color.White, night = Color.White)
                    ),
                    modifier = GlanceModifier
                        .size(40.dp)
                        .clickable(actionRunCallback<RefreshWeatherActionCallback>())
                )
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            Text(
                text = uiState.temperature,
                style = TextStyle(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(day = Color.White, night = Color.White)
                )
            )

            Spacer(modifier = GlanceModifier.height(8.dp))

            if (uiState.humidity.isNotEmpty()) {
                Text(
                    text = "💧 ${uiState.humidity}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = ColorProvider(day = Color.White, night = Color.White)
                    )
                )
            }
        }
    }
}

@Composable
fun WeatherSmall(
    uiState: WeatherWidgetUiState,
    context: Context
) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFF1565C0))
            .cornerRadius(16.dp)
            .padding(8.dp)
            .clickable(
                actionStartActivity(
                    Intent(context, MainActivity::class.java)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.temperature,
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(day = Color.White, night = Color.White)
                )
            )

            Spacer(modifier = GlanceModifier.height(4.dp))

            Text(
                text = uiState.location.take(8),
                style = TextStyle(
                    fontSize = 11.sp,
                    color = ColorProvider(day = Color.White, night = Color.White)
                )
            )
        }
    }
}

