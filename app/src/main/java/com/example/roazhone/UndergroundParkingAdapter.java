package com.example.roazhone;

import android.content.Context;
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
    private Context context;

    public UndergroundParkingAdapter(Context context) {
        this.context = context;
        parkingList = new ArrayList<>();
    }

    public UndergroundParkingAdapter(Context context, List<UndergroundParkingDetails> parkingList) {
        this.context = context;
        this.parkingList = parkingList;
    }

    @NonNull
    @Override
    public UndergroundParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);

        return new UndergroundParkingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UndergroundParkingViewHolder vh, int i) {
        UndergroundParkingDetails upd = parkingList.get(i);
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

    public void setParkings(List<UndergroundParkingDetails> parkingList) {
        this.parkingList = parkingList;
        notifyDataSetChanged();
    }
}
