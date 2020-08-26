package com.wungatech.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wungatech.weatherapp.Adapter.PlaceAdapter;
import com.wungatech.weatherapp.Helpers.Helper;
import com.wungatech.weatherapp.Models.Place;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FavouritesActivity extends AppCompatActivity implements PlaceAdapter.OnPlaceListener {
    private RecyclerView rv;
    private List<Place> places = new ArrayList<>();
    private String latForMap;
    private String lonForMap;
    Helper helper = new Helper();
    Place place;
    SQLiteDatabase favourites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        try{
            favourites = this.openOrCreateDatabase("Favourites", MODE_PRIVATE, null);
            Cursor c = favourites.rawQuery("SELECT * FROM favourites", null);
            if (c != null){
                int latIndex = c.getColumnIndex("lat");
                int lonIndex = c.getColumnIndex("lon");
                c.moveToFirst();
                do{
                    try{
                        place = new Place();
                        place.lat = c.getString(latIndex);
                        place.lon = c.getString(lonIndex);
                        place.placeName = helper.convertToAddress(place.lat, place.lon, this);
                        //Log.i("Fetched Place is:", place.placeName);
                        latForMap = place.lat;
                        lonForMap = place.lon;
                        places.add(place);
                    }catch(Exception e){
                        Log.i("Error", e.getLocalizedMessage());
                    }
                }while(c.moveToNext());
                Log.i("Places:", places.toString());
                PlaceAdapter mAdapter = new PlaceAdapter(this, places, this);
                mAdapter.notifyDataSetChanged();
                rv = findViewById(R.id.favouriteList);
                rv.setHasFixedSize(true);
                rv.setLayoutManager(new LinearLayoutManager(this));
                rv.setAdapter(mAdapter);
            }else{
                Toast.makeText(this, "Please connect to the internet and download weather", Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            favourites.close();
        }
    }

    public void onPlaceClick(int position) {
        Place selected = places.get(position);
        Bundle b = new Bundle();
        b.putSerializable("SelectedPlace", selected);
        //send place to the map activity
        Intent gotToMap = new Intent(this, ShowFavouriteActivity.class);
        gotToMap.putExtra("lon", lonForMap);
        gotToMap.putExtra("lat", latForMap);
        startActivity(gotToMap);
    }
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
}