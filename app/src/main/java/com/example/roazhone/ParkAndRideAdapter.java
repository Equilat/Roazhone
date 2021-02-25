package com.example.roazhone;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.model.ParkAndRideDetails;

import java.util.ArrayList;
import java.util.List;

public class ParkAndRideAdapter extends  RecyclerView.Adapter<ParkAndRideViewHolder>{

    private List<ParkAndRideDetails> parkingList;

    public ParkAndRideAdapter() {
        parkingList = new ArrayList<>();
    }

    public ParkAndRideAdapter(List<ParkAndRideDetails> parkingList) {
        this.parkingList = parkingList;
    }

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

    public void setParkings(List<ParkAndRideDetails> parkingList) {
        this.parkingList = parkingList;
        notifyDataSetChanged();
    }
}