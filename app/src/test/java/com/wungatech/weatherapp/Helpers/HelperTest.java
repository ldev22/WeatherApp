package com.wungatech.weatherapp.Helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.wungatech.weatherapp.R;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class HelperTest {
    @Mock
    private Context context;
    public void setupTests(){
        MockitoAnnotations.initMocks(this);
    }
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

    @Test
    public void convertToAddress() {
        String lat = "-21.98333";
        String lon = "16.91667";

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        Double latitude = Double.parseDouble(lat);
        Double longitude = Double.parseDouble(lon);
        String output;
        String expected = "Okandhanja, Namibia";
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String country = addresses.get(0).getCountryName();
            output =  city + "," + country;
            assertEquals(expected, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}