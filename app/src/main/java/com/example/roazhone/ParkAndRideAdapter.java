package com.example.roazhone;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.model.ParkAndRideRecord;

import java.util.List;

public class ParkAndRideAdapter extends  RecyclerView.Adapter<ParkAndRideViewHolder>{

    private List<ParkAndRideRecord> parkingList;

    @NonNull
    @Override
    public ParkAndRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ParkAndRideViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }
}