package com.wungatech.weatherapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ForecastResponse {
    @SerializedName("list")
    public ArrayList<WeatherElement> weatherElement = new ArrayList<WeatherElement>();
    @SerializedName("main")
    public Main main;
    @SerializedName("dt")
    public float dt;
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("cod")
    public float cod;
}
