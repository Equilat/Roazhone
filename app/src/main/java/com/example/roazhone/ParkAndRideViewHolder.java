package com.example.roazhone;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.model.ParkAndRideDetails;

import java.util.List;


public class ParkAndRideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected ImageView vFavoris;
    protected TextView vName;
    protected TextView vRoom;
    private final List<ParkAndRideDetails> parkAndRideDetailsList;

    public ParkAndRideViewHolder(View v, List<ParkAndRideDetails> parkAndRideDetailsList) {
        super(v);
        v.setOnClickListener(this);
        vName =  v.findViewById(R.id.parkingName);
        vRoom = v.findViewById(R.id.parkingRoom);
        vFavoris = v.findViewById(R.id.parkingFavoris);
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
