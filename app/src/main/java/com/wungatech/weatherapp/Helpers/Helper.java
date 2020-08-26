package com.wungatech.weatherapp.Helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.wungatech.weatherapp.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

//utility class that is used by Activities
public class Helper {
    public String convertStringtoDay(int val){
        if(val == 8){
            val = 1;
        }else if(val == 9){
            val = 2;
        }else if(val == 10){
            val = 3;
        }else if(val == 11){
            val = 4;
        }

        if(val == 1){
            return "Sunday";
        }else if(val == 2){
            return "Monday";
        }else if(val == 3){
            return "Tuesday";
        }else if(val == 4){
            return "Wednesday";
        }else if(val == 5){
            return "Thursday";
        }else if(val == 6){
            return "Friday";
        }else{
            return "Saturday";
        }
    }

    public int getWeatherIcon(String icon){
        if(icon.contains("Rain") || icon.contains("Thunderstorms")){
            return R.drawable.rain;
        }else if(icon.contains("Clouds") || icon.contains("Mist")){
            return R.drawable.partlysunny;
        }else{
            return R.drawable.clear;
        }
    }

    public String convertToAddress(String lat, String lon, Context context){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        Double latitude = Double.parseDouble(lat);
        Double longitude = Double.parseDouble(lon);
        String output;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String country = addresses.get(0).getCountryName();
            output = address + "," + city + "," + country;
            return output;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
