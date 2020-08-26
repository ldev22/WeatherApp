package com.wungatech.weatherapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {
    @SerializedName("list")
    public List<Forecast> forecast;
    @SerializedName("city")
    public City city;
}
