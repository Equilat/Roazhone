package com.example.roazhone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.UndergroundParkingDetails;

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
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);

        return new ParkAndRideViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkAndRideViewHolder vh, int i) {
        ParkAndRideDetails upd = parkingList.get(i);
        vh.vName.setText(upd.getNomParking());
        if(upd.getStatus().equals("FERME")) {
            vh.vRoom.setText(R.string.parking_ferme);
        }
        else if(upd.getPlacesLibres() == 0) {
            vh.vRoom.setText(R.string.parking_complet);
        }
        else {
            vh.vRoom.setText(upd.getPlacesLibres().toString());
        }
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