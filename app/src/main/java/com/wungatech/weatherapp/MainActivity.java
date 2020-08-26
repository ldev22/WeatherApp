package com.wungatech.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wungatech.weatherapp.Contracts.WeatherService;
import com.wungatech.weatherapp.Helpers.Helper;
import com.wungatech.weatherapp.Models.ForecastResponse;
import com.wungatech.weatherapp.Models.WeatherResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
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
    ForecastResponse forecastResponse;
    SQLiteDatabase favourites;
    WeatherResponse weatherResponse;
    Helper helper = new Helper();
    Gson gson;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Log.i("Permission Check", "We have permission");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isNetworkConnected()){
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
                    Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, API_KEY, "metric", "6");
                    call.enqueue(new Callback<WeatherResponse>() {
                        public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                            if (response.code() == 200) {
                                weatherResponse = response.body();
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
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    });
                    Retrofit retrofit2 = new Retrofit.Builder()
                            .baseUrl(BaseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    WeatherService service2 = retrofit2.create(WeatherService.class);
                    Call<ForecastResponse> forecast = service2.getForecastWeatherData(lat, lon, API_KEY, "metric", "6");

                    forecast.enqueue(new Callback<ForecastResponse>() {
                        public void onResponse(@NonNull Call<ForecastResponse> call, @NonNull Response<ForecastResponse> response) {
                            if (response.code() == 200) {
                                forecastResponse = response.body();
                                //String dateString = );
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
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.i("Error is: ", t.getMessage());
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
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }else{
            try{
                favourites = this.openOrCreateDatabase("Favourites", MODE_PRIVATE, null);
                Cursor c = favourites.rawQuery("SELECT * FROM favourites LIMIT 1",null);
                if(c != null){
                    int lat = c.getColumnIndex("lat");
                    int lon = c.getColumnIndex("lon");
                    c.moveToFirst();
                    String latit = "";
                    String longit = "";
                    do{
                        latit = c.getString(lat);
                        longit = c.getString(lon);
                    }while(c.moveToNext());
                    //launch offline weather
                    Intent offline = new Intent(this, DisplayOffline.class);
                    offline.putExtra("lon", longit);
                    offline.putExtra("lat", latit);
                    startActivity(offline);
                }else{
                    Toast.makeText(this, "Please connect to the internet and download weather", Toast.LENGTH_LONG).show();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

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
        gson = new Gson();
        try{
            favourites = this.openOrCreateDatabase("Favourites", MODE_PRIVATE, null);
            favourites.execSQL("CREATE TABLE IF " +
                    "NOT EXISTS weather (lat VARCHAR, lon VARCHAR, jsonData TEXT, updated DATETIME)");
            favourites.execSQL("CREATE TABLE IF " +
                    "NOT EXISTS favourites (lat VARCHAR, lon VARCHAR, jsonData TEXT, updated DATETIME)");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nav_add_favourites:
                Log.i("Get the Location:", "Location to be found.");
                Log.i("lon", lon);
                Log.i("lat", lat);
                String gsonForecast = gson.toJson(forecastResponse);
                String gsonWeather = gson.toJson(weatherResponse);
                try{
                    favourites.execSQL("INSERT INTO favourites(lat,lon,jsonData, updated) " +
                            "VALUES('"+lat+"', '"+lon+"', '"+ gsonForecast +"', 'NOW()')");
                    favourites.execSQL("INSERT INTO weather(lat,lon,jsonData,updated) " +
                            "VALUES('"+lat+"', '"+lon+"', '"+ gsonWeather +"', 'NOW()')");
                    Toast.makeText(this, "Successfully added as favourite", Toast.LENGTH_LONG).show();
                }catch(SQLException e){
                    Toast.makeText(this, "Offline storage failed", Toast.LENGTH_LONG).show();
                }
                //save the url as a database item
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}