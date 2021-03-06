package com.example.roazhone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.Parking;
import com.example.roazhone.model.UndergroundParkingDetails;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FavorisParkingAdapter extends RecyclerView.Adapter<FavorisParkingAdapter.ParkingViewHolder> {

    private Set<String> parkAndRideFavoris;
    private Set<String> undergroundFavoris;
    private List<Parking> dataset;
    private List<ParkAndRideDetails> parkAndRideDetailsList;
    private List<UndergroundParkingDetails> undergroundParkingDetailsList;
    private Context context;
    private View itemView;
    boolean isLoading;

    private OnFavorisClicked listener;

    interface OnFavorisClicked {
        void onClickFavoris(String id, String key);
    }

    public FavorisParkingAdapter(Context context, Set<String> undergroundFavoris, Set<String> parkAndRideFavoris, OnFavorisClicked listener) {
        this.context = context;
        this.undergroundFavoris = undergroundFavoris;
        this.parkAndRideFavoris = parkAndRideFavoris;
        this.listener = listener;
        this.undergroundParkingDetailsList = new ArrayList<>();
        this.parkAndRideDetailsList = new ArrayList<>();
        dataset = new ArrayList<>();
    }

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);

        return new ParkingViewHolder(itemView, dataset);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder vh, int i) {
        Parking parking = dataset.get(i);

        if (parking instanceof ParkAndRideDetails) {

            ParkAndRideDetails upd = (ParkAndRideDetails) parking;

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

            updateFavorisStar(parkAndRideFavoris, upd.getId(), vh.vFavoris);

            vh.vFavoris.setOnClickListener(v -> {
                listener.onClickFavoris(upd.getId(), "prf");
            });

            if (upd.getStatus().equals("Fermé")) {
                vh.vRoom.setText(R.string.parking_ferme_short);
                vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_red));
            } else if (upd.getPlacesLibres() == 0) {
                vh.vRoom.setText(R.string.parking_complet_short);
                vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_red));
            } else if (upd.getPlacesLibres() <= upd.getCapaciteActuelle() * 0.2) {
                vh.vRoom.setText(upd.getPlacesLibres().toString() + context.getString(R.string.places_dispos));
                vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_orange));
            } else {
                vh.vRoom.setText(upd.getPlacesLibres().toString() + context.getString(R.string.places_dispos));
                vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_green));
            }

            List<Double> coord = upd.getCoordonnees();
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
        } else if (parking instanceof UndergroundParkingDetails) {
            UndergroundParkingDetails upd = (UndergroundParkingDetails) parking;
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

            updateFavorisStar(undergroundFavoris, upd.getId(), vh.vFavoris);

            vh.vFavoris.setOnClickListener(v -> {
                listener.onClickFavoris(upd.getId(), "upf");
            });

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
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setParkAndRide(List<ParkAndRideDetails> parkingList) {
        this.parkAndRideDetailsList = parkingList;
        filterParkings();
        notifyDataSetChanged();
    }

    public void setUndergroundParking(List<UndergroundParkingDetails> parkingList) {
        this.undergroundParkingDetailsList = parkingList;
        filterParkings();
        notifyDataSetChanged();
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void updateFavorisStar(Set<String> parkingsFavoris, String id, ImageView favoris) {
        if (parkingsFavoris.contains(id)) {
            favoris.setImageResource(R.drawable.ic_baseline_star_24);
        } else {
            favoris.setImageResource(R.drawable.ic_baseline_star_border_24);
        }
    }

    public void setParkAndRideFavoris(Set<String> parkingsFavoris) {
        this.parkAndRideFavoris = parkingsFavoris;
        filterParkings();
        notifyDataSetChanged();
    }

    public void setUndergroundFavoris(Set<String> parkingsFavoris) {
        this.undergroundFavoris = parkingsFavoris;
        filterParkings();
        notifyDataSetChanged();
    }

    public void filterParkings() {
        this.dataset.clear();
        for (ParkAndRideDetails p : parkAndRideDetailsList) {
            if (parkAndRideFavoris.contains(p.getId())) {
                dataset.add(p);
            }
        }
        for (UndergroundParkingDetails p : undergroundParkingDetailsList) {
            if (undergroundFavoris.contains(p.getId())) {
                dataset.add(p);
            }
        }
    }

    public class ParkingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView vFavoris;
        protected TextView vName;
        protected TextView vRoom;
        protected TextView vDistance;
        private final List<? extends Parking> parkingList;

        public ParkingViewHolder(View v, List<? extends Parking> parkAndRideDetailsList) {
            super(v);
            v.setOnClickListener(this);
            vName = v.findViewById(R.id.parkingName);
            vRoom = v.findViewById(R.id.parkingRoom);
            vFavoris = v.findViewById(R.id.parkingFavoris);
            vDistance = v.findViewById(R.id.parkingDistance);
            this.parkingList = parkAndRideDetailsList;
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

}
