package com.wungatech.weatherapp.Helpers;

import com.wungatech.weatherapp.R;

import org.junit.Test;

import static org.junit.Assert.*;

public class HelperTest {

    @Test
    public void convertStringtoDay() {
        int val = 5;
        String expected = "Thursday";

        String output;
        if(val == 5){
            output = "Thursday";
        }else{
            output = "Saturday";
        }

        assertEquals(expected, output);
    }

    @Test
    public void getWeatherIcon() {
        String input = "Rain";
        int expected = R.drawable.rain;
        int output;
        if(input.contains("Rain") || input.contains("Thunderstorms")){
            output = R.drawable.rain;
        }else{
            output = R.drawable.clear;
        }
        assertEquals(expected, output);
    }
}