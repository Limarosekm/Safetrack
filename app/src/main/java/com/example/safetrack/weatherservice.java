
package com.example.safetrack;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface weatherservice {

    @GET("forecast.json")
    Call<WeatherResponse> getThreeDayWeather(
            @Query("q") String cityName,
            @Query("key") String apiKey,
            @Query("days") int days
    );
}

