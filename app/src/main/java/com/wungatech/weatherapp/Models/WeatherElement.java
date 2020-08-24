package com.wungatech.weatherapp.Models;

import com.google.gson.annotations.SerializedName;

public class WeatherElement {
    @SerializedName("dt_txt")
    public String dt_txt;
    @SerializedName("weather")
    public Weather weather;
    @SerializedName("main")
    public Main main;

    public WeatherElement(String dt_txt, Weather weather, Main main) {
        this.dt_txt = dt_txt;
        this.weather = weather;
        this.main = main;
    }
}
