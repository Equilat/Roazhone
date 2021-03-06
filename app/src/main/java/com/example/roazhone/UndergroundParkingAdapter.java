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

import com.example.roazhone.model.UndergroundParkingDetails;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UndergroundParkingAdapter extends RecyclerView.Adapter<UndergroundParkingViewHolder> {

    private List<UndergroundParkingDetails> parkingList;
    private final Context context;
    private Set<String> parkingsFavoris;
    private View itemView;
    private boolean isLoading = false;

    private OnFavorisClicked listener;

    interface OnFavorisClicked {
        void onClickFavoris(String id, String key);
    }

    public UndergroundParkingAdapter(Context context) {
        this.context = context;
        parkingList = new ArrayList<>();
        setHasStableIds(true);
    }

    public UndergroundParkingAdapter(Context context, List<UndergroundParkingDetails> parkingList) {
        this.context = context;
        this.parkingList = parkingList;
        setHasStableIds(true);
    }

    public UndergroundParkingAdapter(Context context, Set<String> parkingsFavoris, OnFavorisClicked listener) {
        this.context = context;
        this.parkingsFavoris = parkingsFavoris;
        this.listener = listener;
        parkingList = new ArrayList<>();
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public UndergroundParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);

        return new UndergroundParkingViewHolder(itemView, parkingList);
    }

    @Override
    public void onBindViewHolder(@NonNull UndergroundParkingViewHolder vh, int i) {
        UndergroundParkingDetails upd = parkingList.get(i);
        vh.vName.setText(upd.getNomParking());

        if (isLoading) {
            vh.vDistance.setText(itemView.getResources().getString(R.string.calcul_en_cours));
        } else if (upd.getUserDistance() == null) {
            vh.vDistance.setVisibility(View.INVISIBLE);
        }

        if (upd.getUserDistance() != null) {
            String text = String.format(context.getString(R.string.distance_kilometres), upd.getUserDistance().toString());
            vh.vDistance.setText(text);
            isLoading = false;
        }

        updateFavorisStar(parkingsFavoris, upd.getId(), vh.vFavoris);

        vh.vFavoris.setOnClickListener(v -> {
            listener.onClickFavoris(upd.getId(), "upf");
        });

        try {
            parkingStatusCalculation(upd);
            OpeningPeriod openingPeriod = new OpeningPeriod(context, upd.getNomParking());
            if (!openingPeriod.isOpen()) {
                upd.setStatus(context.getString(R.string.parking_ferme_short_no_accents));
            }
            if (upd.getStatus().equals(context.getString(R.string.parking_ferme_short_no_accents))) {
                vh.vRoom.setText(R.string.parking_ferme_short);
                vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_red));
            } else if (upd.getPlacesLibres() == 0) {
                vh.vRoom.setText(R.string.parking_complet_short);
                vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_red));
            } else if (upd.getPlacesLibres() <= upd.getPlacesMax() * 0.2) {
                vh.vRoom.setText(upd.getPlacesLibres().toString() + context.getString(R.string.places_dispos));
                vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_orange));
            } else {
                vh.vRoom.setText(upd.getPlacesLibres().toString() + context.getString(R.string.places_dispos));
                vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_green));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Double> coord = upd.getGeo();
        double lat = coord.get(0);
        double lon = coord.get(1);
        ImageButton gMapsLink = (ImageButton) vh.itemView.findViewById(R.id.itinaryButton);
        gMapsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void parkingStatusCalculation(UndergroundParkingDetails upd) throws ParseException {

    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setParkings(List<UndergroundParkingDetails> parkingList) {
        this.parkingList = parkingList;
        notifyDataSetChanged();
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
