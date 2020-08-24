package com.wungatech.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wungatech.weatherapp.Contracts.WeatherService;
import com.wungatech.weatherapp.Models.ForecastResponse;
import com.wungatech.weatherapp.Models.Weather;
import com.wungatech.weatherapp.Models.WeatherElement;
import com.wungatech.weatherapp.Models.WeatherResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    //public static String BaseUrl = "https://api.openweathermap.org/data/2.5/forecast?units=metric&cnt=6";
    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String AppId = "651e65c83907e8834259263057d957f5";
    public static String lat;
    public static String lon;
    RelativeLayout topHalf;
    TextView mainWeather;
    TextView mainWeatherTemp;
    LinearLayout bottomHalf;
    TextView currentDailyTemp;
    TextView minTemp;
    TextView maxTemp;
    String API_KEY = "651e65c83907e8834259263057d957f5";
    LinearLayout weatherForecast;
    TextView forecast1;
    TextView forecast2;
    TextView forecast3;
    TextView forecast4;
    TextView forecast5;
    ImageView icon1;
    ImageView icon2;
    ImageView icon3;
    ImageView icon4;
    ImageView icon5;
    TextView day1;
    TextView day2;
    TextView day3;
    TextView day4;
    TextView day5;
    ScrollView parent;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findUserWeather();
        topHalf = (RelativeLayout) findViewById(R.id.weatherBackground);
        mainWeatherTemp = (TextView) findViewById(R.id.mainWeatherTemp);
        mainWeather = (TextView) findViewById(R.id.mainWeather);
        bottomHalf = (LinearLayout) findViewById(R.id.weatherData);
        currentDailyTemp = (TextView) findViewById(R.id.currentDailyTemp);
        minTemp = (TextView) findViewById(R.id.minDailyTemp);
        maxTemp = (TextView) findViewById(R.id.maxDailyTemp);
        weatherForecast = (LinearLayout) findViewById(R.id.weatherForecast);
        forecast1 = (TextView)findViewById(R.id.forecastTemp1);
        forecast2 = (TextView)findViewById(R.id.forecastTemp2);
        forecast3 = (TextView)findViewById(R.id.forecastTemp3);
        forecast4 = (TextView)findViewById(R.id.forecastTemp4);
        forecast5 = (TextView)findViewById(R.id.forecastTemp5);
        icon1 = (ImageView)findViewById(R.id.iconWeather1);
        icon2 = (ImageView)findViewById(R.id.iconWeather2);
        icon3 = (ImageView)findViewById(R.id.iconWeather3);
        icon4 = (ImageView)findViewById(R.id.iconWeather4);
        icon5 = (ImageView)findViewById(R.id.iconWeather5);
        day1 = (TextView)findViewById(R.id.dayOWeek1);
        day2 = (TextView)findViewById(R.id.dayOWeek2);
        day3 = (TextView)findViewById(R.id.dayOWeek3);
        day4 = (TextView)findViewById(R.id.dayOWeek4);
        day5 = (TextView)findViewById(R.id.dayOWeek5);
        parent = (ScrollView) findViewById(R.id.partentBackground);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }

    public void findUserWeather() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //final Location[] currentLocation = new Location[1];
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Log.i("Current Location", location.toString());
                //System.out.println("Location: " + location.toString());
                lat = String.valueOf(location.getLatitude());
                lon = String.valueOf(location.getLongitude());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BaseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                WeatherService service = retrofit.create(WeatherService.class);
                Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, API_KEY, "metric");
                call.enqueue(new Callback<WeatherResponse>() {
                    public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                        if (response.code() == 200) {
                            WeatherResponse weatherResponse = response.body();
                            Log.i("Main Weather", weatherResponse.weather.get(0).main);
                            mainWeather.setText(weatherResponse.weather.get(0).main);
                            mainWeatherTemp.setText(String.valueOf((int)weatherResponse.main.temp + "\u00B0"));
                            minTemp.setText(String.valueOf((int)weatherResponse.main.temp_min + "\u00B0"));
                            currentDailyTemp.setText(String.valueOf((int)weatherResponse.main.temp + "\u00B0"));
                            if(weatherResponse.weather.get(0).main.contains("Clouds") || weatherResponse.weather.get(0).main.contains("Mist")){
                                topHalf.setBackgroundResource(R.drawable.forest_cloudy);
                                bottomHalf.setBackgroundResource(R.color.colorForestCloudy);
                                parent.setBackgroundResource(R.color.colorForestCloudy);
                            }else if(weatherResponse.weather.get(0).main.contains("Rain") || weatherResponse.weather.get(0).main.contains("Thunderstorm")){
                                topHalf.setBackgroundResource(R.drawable.forest_rainy);
                                bottomHalf.setBackgroundResource(R.color.colorForestRainy);
                                parent.setBackgroundResource(R.color.colorForestRainy);
                            }else{
                                topHalf.setBackgroundResource(R.drawable.forest_sunny);
                                bottomHalf.setBackgroundResource(R.color.colorForestSunny);
                                parent.setBackgroundResource(R.color.colorForestSunny);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });

                Call<ForecastResponse> forecast = service.getForecastWeatherData(lat, lon, API_KEY, "metric", "6");
                forecast.enqueue(new Callback<ForecastResponse>() {
                    public void onResponse(@NonNull Call<ForecastResponse> call, @NonNull Response<ForecastResponse> response) {
                        Log.i("Response Code", String.valueOf(response.code()));
                        if (response.code() == 200) {
                            ForecastResponse weatherResponse = response.body();
                            Log.i("Day String", weatherResponse.weatherElement.get(1).dt_txt);
                            //the reason we are doing this is to fit with the design of the app
                            day1.setText(convertStringtoDay(weatherResponse.weatherElement.get(1).dt_txt));
                            day2.setText(convertStringtoDay(weatherResponse.weatherElement.get(2).dt_txt));
                            day3.setText(convertStringtoDay(weatherResponse.weatherElement.get(3).dt_txt));
                            day4.setText(convertStringtoDay(weatherResponse.weatherElement.get(4).dt_txt));
                            day5.setText(convertStringtoDay(weatherResponse.weatherElement.get(5).dt_txt));
                            icon1.setImageResource(getWeatherIcon(weatherResponse.weatherElement.get(1).weather.icon));
                            icon2.setImageResource(getWeatherIcon(weatherResponse.weatherElement.get(2).weather.icon));
                            icon3.setImageResource(getWeatherIcon(weatherResponse.weatherElement.get(3).weather.icon));
                            icon4.setImageResource(getWeatherIcon(weatherResponse.weatherElement.get(4).weather.icon));
                            icon5.setImageResource(getWeatherIcon(weatherResponse.weatherElement.get(5).weather.icon));
                            forecast1.setText(String.valueOf(weatherResponse.weatherElement.get(1).main.temp) + "\u00B0");
                            forecast2.setText(String.valueOf(weatherResponse.weatherElement.get(2).main.temp) + "\u00B0");
                            forecast3.setText(String.valueOf(weatherResponse.weatherElement.get(3).main.temp) + "\u00B0");
                            forecast4.setText(String.valueOf(weatherResponse.weatherElement.get(4).main.temp) + "\u00B0");
                            forecast5.setText(String.valueOf(weatherResponse.weatherElement.get(5).main.temp) + "\u00B0");
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        //return currentLocation[0];
    }

    private String convertStringtoDay(String name){
        try{
            DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            Date date = format.parse(name);
            DateFormat formatter = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            String day = formatter.format(date);
            return day;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getWeatherIcon(String icon){
        if(icon.contains("w01d")){
            return R.drawable.w01d;
        }else if(icon.contains("w01n")){
            return R.drawable.w01n;
        }else if(icon.contains("w02d")){
            return R.drawable.w02d;
        }else if(icon.contains("w02n")){
            return R.drawable.w02n;
        }else if(icon.contains("w03d")){
            return R.drawable.w03d;
        }else if(icon.contains("w03n")){
            return R.drawable.w03n;
        }else if(icon.contains("w04d")){
            return R.drawable.w04d;
        }else if(icon.contains("w09d")){
            return R.drawable.w09d;
        }else if(icon.contains("w09n")){
            return R.drawable.w09n;
        }else if(icon.contains("w10d")){
            return R.drawable.w10d;
        }else if(icon.contains("w10n")){
            return R.drawable.w10n;
        }else if(icon.contains("w11d")){
            return R.drawable.w11d;
        }else if(icon.contains("w11n")){
            return R.drawable.w11n;
        }else if(icon.contains("w13d")){
            return R.drawable.w13d;
        }else if(icon.contains("w13n")){
            return R.drawable.w13n;
        }else if(icon.contains("w50d")){
            return R.drawable.w50d;
        }else if(icon.contains("w50n")){
            return R.drawable.w50n;
        }else{
            return R.drawable.unknown;
        }
    }
}