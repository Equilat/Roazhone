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

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class UndergroundParkingAdapter extends RecyclerView.Adapter<UndergroundParkingViewHolder> {

    private List<UndergroundParkingDetails> parkingList;
    private Context context;
    private Set<String> parkingsFavoris;
    private View itemView;


    public UndergroundParkingAdapter(Context context) {
        this.context = context;
        parkingList = new ArrayList<>();
    }

    public UndergroundParkingAdapter(Context context, List<UndergroundParkingDetails> parkingList) {
        this.context = context;
        this.parkingList = parkingList;
    }

    public UndergroundParkingAdapter(Context context, Set<String> parkingsFavoris) {
        this.context = context;
        this.parkingsFavoris = parkingsFavoris;
        parkingList = new ArrayList<>();
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
        vh.vDistance.setText(upd.getUserDistance() == null ? "distance" : upd.getUserDistance() + " km");
        if(parkingsFavoris!=null && parkingsFavoris.contains(upd.getId())){
            vh.vFavoris.setVisibility(View.VISIBLE);
        }
        else {
            vh.vFavoris.setVisibility(View.INVISIBLE);
        }

        try {
            if(!isOpened(upd.getNomParking()) || upd.getStatus().equals("FERME")) {
                vh.vRoom.setText(R.string.parking_ferme_short);
                vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_red));
            }
            else if(upd.getPlacesLibres() == 0) {
                vh.vRoom.setText(R.string.parking_complet_short);
                vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_red));
            }
            else if(upd.getPlacesLibres() <= upd.getPlacesMax()*0.2) {
                vh.vRoom.setText(upd.getPlacesLibres().toString()+context.getString(R.string.places_dispos));
                vh.vRoom.setTextColor(ContextCompat.getColor(this.context, R.color.roazhone_orange));
            }
            else {
                vh.vRoom.setText(upd.getPlacesLibres().toString()+context.getString(R.string.places_dispos));
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

    private boolean isOpened(String parkingName) throws ParseException {
        boolean ret = false;
        Calendar calendarToday = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        Calendar calendarTomorrow = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        calendarTomorrow.add(Calendar.DATE, 1);
        int dayOfWeek = calendarToday.get(Calendar.DAY_OF_WEEK);
        String[] parkingWeeklyOpeningHours = getParkingOpeningHours(parkingName);
        String[] parkingWeeklyClosingHours = getParkingClosingHours(parkingName);
        String parkingOpeningHours = parkingWeeklyOpeningHours[dayOfWeek - 1];
        String parkingClosingHours = parkingWeeklyClosingHours[dayOfWeek - 1];
        if(!parkingOpeningHours.equals("fermé")) {
            Date parkingOpeningTime = new SimpleDateFormat("HH:mm", Locale.FRANCE).parse(parkingOpeningHours);
            Date parkingClosingTime = new SimpleDateFormat("HH:mm", Locale.FRANCE).parse(parkingClosingHours);
            Date currentTime = calendarToday.getTime();
            if(parkingOpeningHours.equals(parkingClosingHours)) {
                ret = true;
            }
            else if(parkingClosingHours.compareTo("00:00") >= 0) {
                calendarTomorrow.setTime(parkingClosingTime);
                if((currentTime.after(parkingOpeningTime) ||currentTime.equals(parkingOpeningTime)) && currentTime.before(calendarTomorrow.getTime())) {
                    ret = true;
                }
            }
            else {
                calendarToday.setTime(parkingClosingTime);
                if((currentTime.after(parkingOpeningTime) ||currentTime.equals(parkingOpeningTime)) && currentTime.before(calendarToday.getTime())) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    private String[] getParkingOpeningHours(String parkingName) {
        String days = "";
        switch (parkingName) {
            case "Colombier":
                days = itemView.getResources().getString(R.string.colombier_ouverture);
                break;
            case "Gare-Sud":
                days = itemView.getResources().getString(R.string.gare_sud_ouverture);
                break;
            case "Dinan-Chezy":
                days = itemView.getResources().getString(R.string.dinan_chezy_ouverture);
                break;
            case "Vilaine":
                days = itemView.getResources().getString(R.string.vilaine_ouverture);
                break;
            case "Hoche":
                days = itemView.getResources().getString(R.string.hoche_ouverture);
                break;
            case "Kennedy":
                days = itemView.getResources().getString(R.string.kennedy_ouverture);
                break;
            case "Lices":
                days = itemView.getResources().getString(R.string.lices_ouverture);
                break;
            case "Charles-de-gaulle":
                days = itemView.getResources().getString(R.string.charles_de_gaulle_ouverture);
                break;
            case "Kleber":
                days = itemView.getResources().getString(R.string.kleber_ouverture);
                break;
            case "Arsenal":
                days = itemView.getResources().getString(R.string.arsenal_ouverture);
                break;
        }
        return days.split(",");
    }

    private String[] getParkingClosingHours(String parkingName) {
        String days = "";
        switch (parkingName) {
            case "Colombier":
                days = itemView.getResources().getString(R.string.colombier_fermeture);
                break;
            case "Gare-Sud":
                days = itemView.getResources().getString(R.string.gare_sud_fermeture);
                break;
            case "Dinan-Chezy":
                days = itemView.getResources().getString(R.string.dinan_chezy_fermeture);
                break;
            case "Vilaine":
                days = itemView.getResources().getString(R.string.vilaine_fermeture);
                break;
            case "Hoche":
                days = itemView.getResources().getString(R.string.hoche_fermeture);
                break;
            case "Kennedy":
                days = itemView.getResources().getString(R.string.kennedy_fermeture);
                break;
            case "Lices":
                days = itemView.getResources().getString(R.string.lices_fermeture);
                break;
            case "Charles-de-gaulle":
                days = itemView.getResources().getString(R.string.charles_de_gaulle_fermeture);
                break;
            case "Kleber":
                days = itemView.getResources().getString(R.string.kleber_fermeture);
                break;
            case "Arsenal":
                days = itemView.getResources().getString(R.string.arsenal_fermeture);
                break;
        }
        return days.split(",");
    }

    public void setParkings(List<UndergroundParkingDetails> parkingList) {
        this.parkingList = parkingList;
        notifyDataSetChanged();
    }
}
