package com.wungatech.weatherapp.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wungatech.weatherapp.R;

public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView placeName;
    PlaceAdapter.OnPlaceListener onPlaceListener;
    PlaceViewHolder(View itemView, PlaceAdapter.OnPlaceListener onPlaceListener) {
        super(itemView);
        placeName = itemView.findViewById(R.id.place_name);
        this.onPlaceListener = onPlaceListener;
        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        onPlaceListener.onPlaceClick(getAdapterPosition());
    }
}
