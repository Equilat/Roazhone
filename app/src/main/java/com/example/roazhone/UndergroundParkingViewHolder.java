package com.example.roazhone;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.model.UndergroundParkingDetails;

import java.util.List;


public class UndergroundParkingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected ImageView vFavoris;
    protected TextView vName;
    protected TextView vRoom;
    private List<UndergroundParkingDetails> undergroundParkingDetailsList;

    public UndergroundParkingViewHolder(View v, List<UndergroundParkingDetails> undergroundParkingDetailsList) {
        super(v);
        v.setOnClickListener(this);
        vName =  v.findViewById(R.id.parkingName);
        vRoom = v.findViewById(R.id.parkingRoom);
        vFavoris = v.findViewById(R.id.parkingFavoris);
        this.undergroundParkingDetailsList = undergroundParkingDetailsList;
    }

    @Override
    public void onClick(View v) {
        int itemPosition = this.getAdapterPosition();
        UndergroundParkingDetails undergroundParkingDetails = undergroundParkingDetailsList.get(itemPosition);
        HomeFragmentDirections.ActionHomeFragmentToInfosParkingFragment action = HomeFragmentDirections.actionHomeFragmentToInfosParkingFragment();
        action.setUndergroundParkingDetails(undergroundParkingDetails);
        Navigation.findNavController(v).navigate(action);
    }


}
