package com.example.roazhone;

import android.view.View;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


public class ParkAndRideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected TextView vName;
    protected TextView vRoom;

    public ParkAndRideViewHolder(View v) {
        super(v);
        v.setOnClickListener(this);
        vName =  v.findViewById(R.id.parkingName);
        vRoom = v.findViewById(R.id.parkingRoom);
    }

    @Override
    public void onClick(View v) {
        System.out.println("here");
        int itemPosition = this.getAdapterPosition();
        Navigation.findNavController(v).navigate(R.id.actionHomeFragmentToInfosParkingFragment);
    }
}
