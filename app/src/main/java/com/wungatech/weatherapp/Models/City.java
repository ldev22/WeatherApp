package com.wungatech.weatherapp.Models;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("name")
    public String name;
    public String country;
    public Coord coord;
}
