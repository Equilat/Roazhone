package com.example.roazhone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.model.ParkAndRideDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParkAndRideAdapter extends  RecyclerView.Adapter<ParkAndRideViewHolder>{

    private Set<String> parkingsFavoris;
    private List<ParkAndRideDetails> parkingList;
    private Context context;
    private View itemView;
    boolean isLoading;

    private OnFavorisClicked listener;

    interface OnFavorisClicked {
        void onClickFavoris(String id, String key);
    }

    public ParkAndRideAdapter(Context context) {
        this.context = context;
        parkingList = new ArrayList<>();
    }

    public ParkAndRideAdapter(Context context, List<ParkAndRideDetails> parkingList) {
        this.context = context;
        this.parkingList = parkingList;
    }

    public ParkAndRideAdapter(Context context, Set<String> parkingsFavoris, OnFavorisClicked listener) {
        this.context = context;
        this.parkingsFavoris = parkingsFavoris;
        this.listener = listener;
        parkingList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ParkAndRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);

        return new ParkAndRideViewHolder(itemView, parkingList);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkAndRideViewHolder vh, int i) {
        ParkAndRideDetails upd = parkingList.get(i);
        vh.vName.setText(upd.getNomParking());

        if(isLoading) {
            vh.vDistance.setText(itemView.getResources().getString(R.string.calcul_en_cours));
        }
        else if(upd.getUserDistance() == null) {
            vh.vDistance.setVisibility(View.INVISIBLE);
        }

        if(upd.getUserDistance() != null) {
            String text = String.format(context.getString(R.string.distance_kilometres), upd.getUserDistance().toString());
            vh.vDistance.setText(text);
            isLoading = false;
        }

        updateFavorisStar(parkingsFavoris, upd.getId(), vh.vFavoris);

        vh.vFavoris.setOnClickListener(v -> {
            listener.onClickFavoris(upd.getId(), "prf");
        });

        if(upd.getStatus().equals("Fermé")) {
            vh.vRoom.setText(R.string.parking_ferme_short);
            vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_red));
        }
        else if(upd.getPlacesLibres() == 0) {
            vh.vRoom.setText(R.string.parking_complet_short);
            vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_red));
        }
        else if(upd.getPlacesLibres() <= upd.getCapaciteActuelle()*0.2) {
            vh.vRoom.setText(upd.getPlacesLibres().toString()+context.getString(R.string.places_dispos));
            vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_orange));
        }
        else {
            vh.vRoom.setText(upd.getPlacesLibres().toString()+context.getString(R.string.places_dispos));
            vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_green));
        }

        List<Double> coord = upd.getCoordonnees();
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

    public void setParkings(List<ParkAndRideDetails> parkingList) {
        this.parkingList = parkingList;
        notifyDataSetChanged();
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void updateFavorisStar(Set<String> parkingsFavoris, String id, ImageView favoris){
        if(parkingsFavoris.contains(id)){
            favoris.setImageResource(R.drawable.ic_baseline_star_24);
        }
        else {
            favoris.setImageResource(R.drawable.ic_baseline_star_border_24);
        }
    }

    public  void setFavoris(Set<String> parkingsFavoris){
        this.parkingsFavoris = parkingsFavoris;
    }
}