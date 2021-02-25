package com.example.roazhone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.model.UndergroundParkingDetails;

import java.util.List;

public class ParkingAdapter extends  RecyclerView.Adapter<ParkingViewHolder>{

    private List<UndergroundParkingDetails> parkingList;

    public ParkingAdapter(List<UndergroundParkingDetails> parkingList) {
        this.parkingList = parkingList;
    }

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);

        return new ParkingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder vh, int i) {
        UndergroundParkingDetails upd = parkingList.get(i);
        vh.vName.setText(upd.getKey());
        if(upd.getStatus().equals("FERME")) {
            vh.vRoom.setText(R.string.parking_ferme);
        }
        else if(upd.getFree() == 0) {
            vh.vRoom.setText(R.string.parking_complet);
        }
        else {
            vh.vRoom.setText(upd.getFree());
        }
    }


    @Override
    public int getItemCount() {
        return parkingList.size();
    }
}
