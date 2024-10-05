package com.example.safetrack;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private EditText searchInput;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize OSMDroid configuration with application context
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_main);

        // Set up the MapView
        mapView = findViewById(R.id.map);
        searchInput = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);

        mapView.setMultiTouchControls(true); // Enables zooming and other controls
        mapView.getController().setZoom(9.0); // Set default zoom level
        mapView.getController().setCenter(new GeoPoint(10.52, 76.21)); // Center map on Thrissur

        // Set click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = searchInput.getText().toString().trim();
                if (!location.isEmpty()) {
                    searchLocation(location);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Add markers for landslide and flood-prone areas
        addLandslideMarkers();
        addFloodMarkers();
    }

    private void addLandslideMarkers() {
        // Example coordinates for landslide-prone areas in Kerala
        GeoPoint landslidePoint1 = new GeoPoint(9.96, 76.59);
        GeoPoint landslidePoint2 = new GeoPoint(10.13, 76.59);
        GeoPoint landslidePoint3 = new GeoPoint(11.23, 75.84);

        // Create and add markers for landslide-prone areas
        addMarker(landslidePoint1, "Landslide-Prone Area 1", Color.GREEN);
        addMarker(landslidePoint2, "Landslide-Prone Area 2", Color.GREEN);
        addMarker(landslidePoint3, "Landslide-Prone Area 3", Color.GREEN);
    }

    private void addFloodMarkers() {
        // Example coordinates for flood-prone areas in Kerala
        GeoPoint floodPoint1 = new GeoPoint(9.90, 76.27);
        GeoPoint floodPoint2 = new GeoPoint(10.08, 76.49);
        GeoPoint floodPoint3 = new GeoPoint(8.57, 76.59);

        // Create and add markers for flood-prone areas
        addMarker(floodPoint1, "Flood-Prone Area 1", Color.BLUE);
        addMarker(floodPoint2, "Flood-Prone Area 2", Color.BLUE);
        addMarker(floodPoint3, "Flood-Prone Area 3", Color.BLUE);
    }

    private void addMarker(GeoPoint point, String title, int color) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setTitle(title);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM); // Position the marker

        // Create a colored marker programmatically
        Bitmap markerIcon = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(markerIcon);
        Paint paint = new Paint();
        paint.setColor(color); // Set marker color to the specified color
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(10, 10, 10, paint); // Draw the circle

        marker.setIcon(new BitmapDrawable(getResources(), markerIcon)); // Set the created bitmap as marker icon

        mapView.getOverlays().add(marker); // Add marker to the map's overlays
        mapView.invalidate(); // Refresh the map to show the new marker
    }

    private void searchLocation(String location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocationName(location, 1);
            if (addresses != null && !addresses.isEmpty()) {
                android.location.Address address = addresses.get(0);
                GeoPoint geoPoint = new GeoPoint(address.getLatitude(), address.getLongitude());

                // Move the map view to the searched location
                mapView.getController().animateTo(geoPoint);
                mapView.getController().setZoom(15.0); // Adjust the zoom level as needed

                // Optionally add a marker at the searched location
                addMarker(geoPoint, location, Color.RED);

                // Fetch and show weather info
                fetchWeatherData(address.getLocality());
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error fetching location", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void fetchWeatherData(String city) {
        // Use Retrofit to fetch weather data
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherservice weatherService = retrofit.create(weatherservice.class);
        Call<WeatherResponse> call = weatherService.getThreeDayWeather(city, "YOUR_API_KEY", 3); // Replace YOUR_API_KEY with your actual API key

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    showWeatherDialog(weatherResponse);
                } else {
                    Toast.makeText(MainActivity.this, "Weather data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch weather data", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void showWeatherDialog(WeatherResponse weatherResponse) {
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

        new AlertDialog.Builder(this)
                .setTitle("Weather Information")
                .setMessage(forecastInfo.toString())
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume(); // Needed for map to resume
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause(); // Needed for map to pause
    }
}
