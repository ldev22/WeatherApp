package com.wungatech.weatherapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowFavouriteActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private Double lon;
    private Double lat;
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_add_favourites:
                item.setTitle("Home");
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                return true;
            case R.id.nav_view_favourites:
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
        setContentView(R.layout.activity_show_favourite);
        try{
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            Intent intent = getIntent();
            lon = Double.parseDouble(intent.getStringExtra("lon"));
            lat = Double.parseDouble(intent.getStringExtra("lat"));
            Toast.makeText(this, "Click on marker to view location weather and forecast", Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Log.i("Error", e.getLocalizedMessage());
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng place = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(place).title("Check weather."));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //pass lat lon to DisplayOffline
        Intent offlineWeather = new Intent(this, DisplayOffline.class);
        offlineWeather.putExtra("lat", String.valueOf(lat));
        offlineWeather.putExtra("lon",String.valueOf(lon));
        startActivity(offlineWeather);
        return true;
    }
}