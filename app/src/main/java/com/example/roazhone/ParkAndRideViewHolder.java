package com.example.roazhone;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.model.ParkAndRideDetails;

import java.util.List;


public class ParkAndRideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected TextView vName;
    protected TextView vRoom;
    protected TextView vDistance;
    private final List<ParkAndRideDetails> parkAndRideDetailsList;

    public ParkAndRideViewHolder(View v, List<ParkAndRideDetails> parkAndRideDetailsList) {
        super(v);
        v.setOnClickListener(this);
        vName =  v.findViewById(R.id.parkingName);
        vRoom = v.findViewById(R.id.parkingRoom);
        vDistance = v.findViewById(R.id.parkingDistance);
        this.parkAndRideDetailsList = parkAndRideDetailsList;
    }

    @Override
    public void onClick(View v) {
        int itemPosition = this.getAdapterPosition();
        ParkAndRideDetails parkAndRideDetails = parkAndRideDetailsList.get(itemPosition);
        HomeFragmentDirections.ActionHomeFragmentToInfosParkingFragment action = HomeFragmentDirections.actionHomeFragmentToInfosParkingFragment();
        action.setParkAndRideDetails(parkAndRideDetails);
        Navigation.findNavController(v).navigate(action);
    }
}
