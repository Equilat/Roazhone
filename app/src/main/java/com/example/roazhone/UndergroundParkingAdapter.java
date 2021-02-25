package com.example.roazhone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.model.UndergroundParkingDetails;

import java.util.ArrayList;
import java.util.List;

public class UndergroundParkingAdapter extends  RecyclerView.Adapter<UndergroundParkingViewHolder>{

    private List<UndergroundParkingDetails> parkingList;

    public UndergroundParkingAdapter() {
        parkingList = new ArrayList<>();
    }

    public UndergroundParkingAdapter(List<UndergroundParkingDetails> parkingList) {
        this.parkingList = parkingList;
    }

    @NonNull
    @Override
    public UndergroundParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("here");
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);

        return new UndergroundParkingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UndergroundParkingViewHolder vh, int i) {
        System.out.println("here");
        UndergroundParkingDetails upd = parkingList.get(i);
        vh.vName.setText(upd.getNomParking());
        if(upd.getStatus().equals("FERME")) {
            vh.vRoom.setText(R.string.parking_ferme);
        }
        else if(upd.getPlacesLibres() == 0) {
            vh.vRoom.setText(R.string.parking_complet);
        }
        else {
            vh.vRoom.setText(upd.getPlacesLibres());
        }
    }


    @Override
    public int getItemCount() {
        return parkingList.size();
    }

    public void setParkings(List<UndergroundParkingDetails> parkingList) {
        this.parkingList = parkingList;
        notifyDataSetChanged();
    }
}
