package com.example.roazhone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.model.UndergroundParkingDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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


        return new UndergroundParkingViewHolder(itemView, parkingList);
    }

    @Override
    public void onBindViewHolder(@NonNull UndergroundParkingViewHolder vh, int i) {
        UndergroundParkingDetails upd = parkingList.get(i);
        vh.vName.setText(upd.getNomParking());
        if(upd.getStatus().equals("FERME")) {
            vh.vRoom.setText(R.string.parking_ferme_short);
            vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_red));
        }
        else if(upd.getPlacesLibres() == 0) {
            vh.vRoom.setText(R.string.parking_complet_short);
            vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_red));
        }
        else if(upd.getPlacesLibres() <= upd.getPlacesMax()*0.1) {
            vh.vRoom.setText(upd.getPlacesLibres().toString()+context.getString(R.string.places_dispos));
            vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_orange));
        }
        else {
            vh.vRoom.setText(upd.getPlacesLibres().toString()+context.getString(R.string.places_dispos));
            vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_green));
        }

        List<Double> coord = upd.getGeo();
        double lat = coord.get(0);
        double lon = coord.get(1);
        ImageButton gMapsLink = (ImageButton) vh.itemView.findViewById(R.id.itinaryButton);
        gMapsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lon);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });
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
