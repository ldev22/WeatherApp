package com.wungatech.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wungatech.weatherapp.Helpers.Helper;
import com.wungatech.weatherapp.Models.ForecastResponse;
import com.wungatech.weatherapp.Models.WeatherResponse;

import java.util.Calendar;

public class DisplayOffline extends AppCompatActivity {
    WeatherResponse weatherResponse;
    ForecastResponse forecastResponse;
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
    RelativeLayout topHalf;
    TextView mainWeather;
    TextView mainWeatherTemp;
    LinearLayout bottomHalf;
    TextView currentDailyTemp;
    TextView minTemp;
    TextView maxTemp;
    Helper helper = new Helper();
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nav_other, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home:
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                return true;
            case R.id.nav_view_favourites:
                //Open maps to show all favourites
                Intent fav = new Intent(this, FavouritesActivity.class);
                startActivity(fav);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_offline);
        //Log.i("Enter Offline Activity", "We are in the offline activity");
        Intent intent = getIntent();
        String lat = intent.getStringExtra("lat");
        String lon = intent.getStringExtra("lon");
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
        try{
            Gson gson = new Gson();
            SQLiteDatabase favourites = this.openOrCreateDatabase("Favourites", MODE_PRIVATE, null);
            Cursor c1 = favourites.rawQuery("SELECT * FROM weather WHERE lat LIKE ? AND lon LIKE ?", new String[]{lat, lon});

            int jsonDataIndex = c1.getColumnIndex("jsonData");
            c1.moveToFirst();
            String jsonData;
            do{
                jsonData = c1.getString(jsonDataIndex);
                Log.i("Show me JSON:", jsonData);
            }while(c1.moveToNext());
            c1.close();
            WeatherResponse weatherResponse = gson.fromJson(jsonData, WeatherResponse.class);
            mainWeather.setText(weatherResponse.weather.get(0).main.toUpperCase());
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

            Cursor c = favourites.rawQuery("SELECT * FROM favourites WHERE lat=? AND lon=?", new String[]{lat,lon});
            int jsonDataIndexForeCast = c.getColumnIndex("jsonData");
            c.moveToFirst();
            String jsonDataForecast;
            do{
                jsonDataForecast = c.getString(jsonDataIndex);
            }while(c.moveToNext());
            c.close();
            ForecastResponse forecastResponse = gson.fromJson(jsonDataForecast, ForecastResponse.class);
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            //the reason we are doing this is to fit with the design of the app
            day1.setText(helper.convertStringtoDay(day + 1));
            day2.setText(helper.convertStringtoDay(day + 2));
            day3.setText(helper.convertStringtoDay(day + 3));
            day4.setText(helper.convertStringtoDay(day + 4));
            day5.setText(helper.convertStringtoDay(day + 5));
            icon1.setBackgroundResource(helper.getWeatherIcon(forecastResponse.forecast.get(1).weather.get(0).main));
            icon2.setBackgroundResource(helper.getWeatherIcon(forecastResponse.forecast.get(2).weather.get(0).main));
            icon3.setBackgroundResource(helper.getWeatherIcon(forecastResponse.forecast.get(3).weather.get(0).main));
            icon4.setBackgroundResource(helper.getWeatherIcon(forecastResponse.forecast.get(4).weather.get(0).main));
            icon5.setBackgroundResource(helper.getWeatherIcon(forecastResponse.forecast.get(5).weather.get(0).main));
            forecast1.setText(String.valueOf((int)forecastResponse.forecast.get(1).main.temp) + "\u00B0");
            forecast2.setText(String.valueOf((int)forecastResponse.forecast.get(2).main.temp) + "\u00B0");
            forecast3.setText(String.valueOf((int)forecastResponse.forecast.get(3).main.temp) + "\u00B0");
            forecast4.setText(String.valueOf((int)forecastResponse.forecast.get(4).main.temp) + "\u00B0");
            forecast5.setText(String.valueOf((int)forecastResponse.forecast.get(5).main.temp) + "\u00B0");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}