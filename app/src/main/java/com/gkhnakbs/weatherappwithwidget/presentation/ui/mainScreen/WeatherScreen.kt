package com.gkhnakbs.weatherappwithwidget.presentation.ui.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Created by Gökhan Akbaş on 19/11/2025.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val isRefreshing = uiState is WeatherUiState.Loading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E88E5))
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.fetchWeather() },
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            when (uiState) {
                is WeatherUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }

                is WeatherUiState.Idle -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }

                is WeatherUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 32.dp)
                        ) {
                            Text(
                                text = uiState.weatherData.timezone ?: "Unknown Location",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${uiState.weatherData.latitude?.let { "%.2f".format(it) }}°, ${
                                    uiState.weatherData.longitude?.let {
                                        "%.2f".format(
                                            it
                                        )
                                    }
                                }°",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            Text(
                                text = "${
                                    uiState.weatherData.current?.temperature2m?.let {
                                        "%.1f".format(
                                            it
                                        )
                                    } ?: "--"
                                }°",
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.White
                            )
                            Text(
                                text = "Hissedilen: ${
                                    uiState.weatherData.current?.apparentTemperature?.let {
                                        "%.1f".format(
                                            it
                                        )
                                    } ?: "--"
                                }°",
                                fontSize = 18.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                WeatherInfoItem(
                                    label = "Humidity",
                                    value = "${uiState.weatherData.current?.relativeHumidity2m ?: "--"}%",
                                    modifier = Modifier.weight(1f)
                                )
                                WeatherInfoItem(
                                    label = "Rain",
                                    value = "${uiState.weatherData.current?.rain ?: "0"} mm",
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                WeatherInfoItem(
                                    label = "Time Period",
                                    value = uiState.weatherData.timezoneAbbreviation ?: "--",
                                    modifier = Modifier.weight(1f)
                                )
                                WeatherInfoItem(
                                    label = "Height",
                                    value = "${uiState.weatherData.elevation?.let { "%.0f".format(it) } ?: "--"} m",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Text(
                            text = "Last Update: ${uiState.weatherData.current?.time ?: "--"}",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }

                is WeatherUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.message,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = { viewModel.fetchWeather() }) {
                            Text("Try again")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}