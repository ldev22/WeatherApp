package com.wungatech.weatherapp.Contracts;

import com.wungatech.weatherapp.Models.ForecastResponse;
import com.wungatech.weatherapp.Models.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("data/2.5/weather?")
    Call<WeatherResponse> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id, @Query("units") String units, @Query("cnt") String cnt);

    @GET("data/2.5/forecast?")
    Call<ForecastResponse> getForecastWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id, @Query("units") String units, @Query("cnt") String cnt);
}
