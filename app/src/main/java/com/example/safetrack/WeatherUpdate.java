package com.example.safetrack;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WeatherUpdate extends AppCompatActivity {

    private TextView weatherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_update);

        weatherInfo = findViewById(R.id.weather_info);

        // Get the WeatherResponse object from the Intent
        WeatherResponse weatherResponse = (WeatherResponse) getIntent().getSerializableExtra("weatherData");
        if (weatherResponse != null) {
            displayWeatherData(weatherResponse);
        }
    }

    private void displayWeatherData(WeatherResponse weatherResponse) {
        StringBuilder forecastInfo = new StringBuilder();

        forecastInfo.append("Location: ").append(weatherResponse.getLocation().getName()).append("\n")
                .append("Current Temperature: ").append(weatherResponse.getCurrent().getTempC()).append("°C\n")
                .append("Condition: ").append(weatherResponse.getCurrent().getCondition().getText()).append("\n\n");

        forecastInfo.append("3-Day Forecast:\n");
        for (WeatherResponse.Forecast.ForecastDay day : weatherResponse.getForecast().getForecastday()) {
            forecastInfo.append(day.getDate()).append(": ")
                    .append("Max Temp: ").append(day.getDay().getMaxtempC()).append("°C, ")
                    .append("Min Temp: ").append(day.getDay().getMintempC()).append("°C, ")
                    .append("Condition: ").append(day.getDay().getCondition().getText()).append("\n");
        }

        weatherInfo.setText(forecastInfo.toString());
    }
}
