package com.example.roazhone;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ParkingAdapter extends  RecyclerView.Adapter<ParkingViewHolder>{

    private List<UndergroundParkingRecord> contactList;

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
