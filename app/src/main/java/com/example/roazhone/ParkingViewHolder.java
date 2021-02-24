package com.example.roazhone;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class ParkingViewHolder extends RecyclerView.ViewHolder {

    protected TextView vName;
    protected TextView vRoom;

    public ParkingViewHolder(View v) {
        super(v);
        vName =  v.findViewById(R.id.parkingName);
        vRoom = v.findViewById(R.id.parkingRoom);
    }
}
