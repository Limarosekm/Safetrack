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
        GeoPoint landslidePoint4 = new GeoPoint(30.22, 73.26);
        GeoPoint landslidePoint5 = new GeoPoint(37.05, 80.50);
        GeoPoint landslidePoint6 = new GeoPoint(21.95, 82.70);
        GeoPoint landslidePoint7 = new GeoPoint(11.27, 76.00);
        GeoPoint landslidePoint8 = new GeoPoint(11.45, 77.2);
        GeoPoint landslidePoint9 = new GeoPoint(11.14, 75.53);
        GeoPoint landslidePoint10 = new GeoPoint(10.08, 77.05);
        GeoPoint landslidePoint11 = new GeoPoint(11.14, 105.4);
        GeoPoint landslidePoint12 = new GeoPoint(27.08, 93.60);
        GeoPoint landslidePoint13 = new GeoPoint(15.31, 75.10);
        GeoPoint landslidePoint14 = new GeoPoint(13.55, 74.9);
        GeoPoint landslidePoint15 = new GeoPoint(25.12, 91.47);
        GeoPoint landslidePoint16 = new GeoPoint(30.10, 38.48);
        GeoPoint landslidePoint17 = new GeoPoint(25.37, 91.52);
        GeoPoint landslidePoint18 = new GeoPoint(11.17, 75.53);
        GeoPoint landslidePoint19 = new GeoPoint(26.49, 88.13);

        // Create and add markers for landslide-prone areas
        addMarker(landslidePoint1, "Landslide-Prone Area 1", Color.GREEN);
        addMarker(landslidePoint2, "Landslide-Prone Area 2", Color.GREEN);
        addMarker(landslidePoint3, "Landslide-Prone Area 3", Color.GREEN);
        addMarker(landslidePoint4, "Landslide-Prone Area 4", Color.GREEN);
        addMarker(landslidePoint5, "Landslide-Prone Area 6", Color.GREEN);
        addMarker(landslidePoint6, "Landslide-Prone Area 7", Color.GREEN);
        addMarker(landslidePoint7, "Landslide-Prone Area 8", Color.GREEN);
        addMarker(landslidePoint8, "Landslide-Prone Area 9", Color.GREEN);
        addMarker(landslidePoint9, "Landslide-Prone Area 10", Color.GREEN);
        addMarker(landslidePoint10, "Landslide-Prone Area 11", Color.GREEN);
        addMarker(landslidePoint11, "Landslide-Prone Area 12", Color.GREEN);
        addMarker(landslidePoint12, "Landslide-Prone Area 13", Color.GREEN);
        addMarker(landslidePoint13, "Landslide-Prone Area 13", Color.GREEN);
        addMarker(landslidePoint14, "Landslide-Prone Area 14", Color.GREEN);
        addMarker(landslidePoint15, "Landslide-Prone Area 15", Color.GREEN);
        addMarker(landslidePoint16, "Landslide-Prone Area 16", Color.GREEN);
        addMarker(landslidePoint17, "Landslide-Prone Area 17", Color.GREEN);
        addMarker(landslidePoint18, "Landslide-Prone Area 18", Color.GREEN);
        addMarker(landslidePoint19, "Landslide-Prone Area 19", Color.GREEN);
    }

    private void addFloodMarkers() {
        // Example coordinates for flood-prone areas in Kerala
        GeoPoint floodPoint1 = new GeoPoint(10.31, 76.32);
        GeoPoint floodPoint2 = new GeoPoint(10.27, 76.41);
        GeoPoint floodPoint3 = new GeoPoint(27.56, 78.52);
        GeoPoint floodPoint4 = new GeoPoint(26.25, 80.20);
        GeoPoint floodPoint5 = new GeoPoint(21.36, 86.33);
        GeoPoint floodPoint6 = new GeoPoint(15.42, 79.01);
        GeoPoint floodPoint7 = new GeoPoint(20.20, 87.01);
        GeoPoint floodPoint8 = new GeoPoint(28.89, 76.60);
        GeoPoint floodPoint9 = new GeoPoint(28.99, 77.01);
        GeoPoint floodPoint10 = new GeoPoint(26.58, 85.50);
        GeoPoint floodPoint11= new GeoPoint(22.10, 71.15);
        GeoPoint floodPoint12 = new GeoPoint(24.00, 90.00);
        GeoPoint floodPoint13 = new GeoPoint(10.31, 76.32);
        GeoPoint floodPoint14 = new GeoPoint(10.10, 76.35);
        GeoPoint floodPoint15 = new GeoPoint(10.30, 76.33);
        GeoPoint floodPoint16 = new GeoPoint(10.08, 76.49);
        GeoPoint floodPoint17 = new GeoPoint(9.37, 76.60);
        GeoPoint floodPoint18 = new GeoPoint(10.78, 76.65);
        GeoPoint floodPoint19 = new GeoPoint(13.05, 80.16);
        GeoPoint floodPoint20 = new GeoPoint(10.38, 7);


        // Create and add markers for flood-prone areas
        addMarker(floodPoint1, "Flood-Prone Area 1", Color.BLUE);
        addMarker(floodPoint2, "Flood-Prone Area 2", Color.BLUE);
        addMarker(floodPoint3, "Flood-Prone Area 3", Color.BLUE);
        addMarker(floodPoint4, "Flood-Prone Area 4", Color.BLUE);
        addMarker(floodPoint5, "Flood-Prone Area 5", Color.BLUE);
        addMarker(floodPoint6, "Flood-Prone Area 6", Color.BLUE);
        addMarker(floodPoint7, "Flood-Prone Area 7", Color.BLUE);
        addMarker(floodPoint8, "Flood-Prone Area 8", Color.BLUE);
        addMarker(floodPoint9, "Flood-Prone Area 9", Color.BLUE);
        addMarker(floodPoint10, "Flood-Prone Area 10", Color.BLUE);
        addMarker(floodPoint11, "Flood-Prone Area 11", Color.BLUE);
        addMarker(floodPoint12, "Flood-Prone Area 12", Color.BLUE);
        addMarker(floodPoint13, "Flood-Prone Area 13", Color.BLUE);
        addMarker(floodPoint14, "Flood-Prone Area 14", Color.BLUE);
        addMarker(floodPoint15, "Flood-Prone Area 15", Color.BLUE);
        addMarker(floodPoint16, "Flood-Prone Area 16", Color.BLUE);
        addMarker(floodPoint17, "Flood-Prone Area 17", Color.BLUE);
        addMarker(floodPoint18, "Flood-Prone Area 18", Color.BLUE);
        addMarker(floodPoint19, "Flood-Prone Area 19", Color.BLUE);
        addMarker(floodPoint20, "Flood-Prone Area 20", Color.BLUE);
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
        Call<WeatherResponse> call = weatherService.getThreeDayWeather(city, "79504015e37345ffad674240240510", 3); // Replace YOUR_API_KEY with your actual API key

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
                .append("Condition: ").append(weatherResponse.getCurrent().getCondition().getText()).append("\n\n")
                .append("Humidity: ").append(weatherResponse.getCurrent().getHumidity()).append("%\n\n"); // Correct line
        // Add humidity
        // Add humidity

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
