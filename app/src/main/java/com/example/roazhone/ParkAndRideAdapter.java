package com.example.roazhone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.model.ParkAndRideDetails;

import java.util.ArrayList;
import java.util.List;

public class ParkAndRideAdapter extends  RecyclerView.Adapter<ParkAndRideViewHolder>{

    private List<ParkAndRideDetails> parkingList;
    private Context context;

    public ParkAndRideAdapter(Context context) {
        this.context = context;
        parkingList = new ArrayList<>();
    }

    public ParkAndRideAdapter(Context context, List<ParkAndRideDetails> parkingList) {
        this.context = context;
        this.parkingList = parkingList;
    }

    @NonNull
    @Override
    public ParkAndRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);

        return new ParkAndRideViewHolder(itemView, parkingList);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkAndRideViewHolder vh, int i) {
        ParkAndRideDetails upd = parkingList.get(i);
        vh.vName.setText(upd.getNomParking());
        if(upd.getStatus().equals("FERME")) {
            vh.vRoom.setText(R.string.parking_ferme_short);
        }
        else if(upd.getPlacesLibres() == 0) {
            vh.vRoom.setText(R.string.parking_complet_short);
        }
        else {
            vh.vRoom.setText(upd.getPlacesLibres().toString()+context.getString(R.string.places_dispos));
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