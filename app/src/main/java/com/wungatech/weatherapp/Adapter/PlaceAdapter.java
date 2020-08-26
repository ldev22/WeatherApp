package com.wungatech.weatherapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wungatech.weatherapp.Models.Place;
import com.wungatech.weatherapp.R;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceViewHolder> {
    private Context context;
    private List<Place> places;
    private OnPlaceListener onPlaceListener;
    public PlaceAdapter(Context context, List<Place> places, OnPlaceListener onPlaceListener) {
        this.context = context;
        this.places = places;
        this.onPlaceListener = onPlaceListener;
    }
    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_place, parent, false);
        return new PlaceViewHolder(view, onPlaceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place placeObject = places.get(position);
        //int imageRes = getResourceId(context, songObject.getImagePath(), context.getPackageName());
        //holder.songImage.setImageResource(imageRes);
        holder.placeName.setText(placeObject.placeName);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
    public void setClickListener(OnPlaceListener itemClickListener) {
        this.onPlaceListener = itemClickListener;
    }
    public interface OnPlaceListener{
        void onPlaceClick(int position);
    }
}
