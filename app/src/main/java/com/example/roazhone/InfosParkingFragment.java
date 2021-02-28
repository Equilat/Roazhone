package com.example.roazhone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.UndergroundParkingDetails;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class InfosParkingFragment extends Fragment implements OnMapReadyCallback {

    private TextView nom;
    private TextView places;
    private TextView placesPMR;
    private TextView adresse;
    private TextView horaires;
    private View divider;
    private TextView tarifsTexte;
    private TableLayout tarifs;
    private ArrayList<Pair<String, String>> tarifsPairs;
    private double lon;
    private double lat;
    private SupportMapFragment mv;
    private GoogleMap map;
    private ImageView favoris;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View myView = inflater.inflate(R.layout.infos_parking_fragment, container, false);

        favoris = myView.findViewById(R.id.ipf_favoris);

        Object o = InfosParkingFragmentArgs.fromBundle(getArguments()).getParkAndRideDetails();
        if(o == null) {
            o = InfosParkingFragmentArgs.fromBundle(getArguments()).getUndergroundParkingDetails();
        }

        if(o instanceof ParkAndRideDetails) {

            ParkAndRideDetails parkAndRideDetails = (ParkAndRideDetails) o;
            //Tarifs, horaires et divider invisibles
            tarifs = myView.findViewById(R.id.ipf_tarifs_liste);
            tarifs.setVisibility(View.INVISIBLE);
            tarifsTexte = myView.findViewById(R.id.ipf_tarifs_texte);
            tarifsTexte.setVisibility(View.INVISIBLE);
            divider = myView.findViewById(R.id.ipf_divider);
            divider.setVisibility(View.INVISIBLE);

            //Nom du parking
            nom = myView.findViewById(R.id.ipf_nom);
            nom.setText(getString(R.string.parking) + parkAndRideDetails.getNomParking());

            //Place dans le Parking
            places = myView.findViewById(R.id.ipf_nb_places);
            placesPMR = myView.findViewById(R.id.ipf_nb_places_pmr);

            //Geolocalisation du parking
            List<Double> coord = parkAndRideDetails.getCoordonnees();
            lat = coord.get(0);
            lon = coord.get(1);

            if(parkAndRideDetails.getStatus().equals("Fermé")){
                places.setText(getString(R.string.parking_ferme_long));
                places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_red));
                placesPMR.setVisibility(View.INVISIBLE);

            }
            else if (parkAndRideDetails.getStatus().equals("Complet") || parkAndRideDetails.getPlacesLibres() == 0){
                places.setText(getString(R.string.parking_complet_long));
                places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_red));
                placesPMR.setVisibility(View.INVISIBLE);

            }
            else {
                if (parkAndRideDetails.getPlacesLibres() <= parkAndRideDetails.getCapaciteActuelle()*0.2) {
                    places.setText(parkAndRideDetails.getPlacesLibres() + getString(R.string.places_dispos));
                    places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_orange));
                    placesPMR.setText(parkAndRideDetails.getNombreLibresPMR() + getString(R.string.places_pmr_dispos));
                    placesPMR.setVisibility(View.VISIBLE);
                }
                else {
                    places.setText(parkAndRideDetails.getPlacesLibres() + getString(R.string.places_dispos));
                    places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_green));
                    placesPMR.setText(parkAndRideDetails.getNombreLibresPMR() + getString(R.string.places_pmr_dispos));
                    placesPMR.setVisibility(View.VISIBLE);
                }
            }

            //Adresse
            adresse = myView.findViewById(R.id.ipf_adresse);
            adresse.setText(getAddress(parkAndRideDetails.getCoordonnees().get(0), parkAndRideDetails.getCoordonnees().get(1)));

            //Horaires
            horaires = myView.findViewById(R.id.ipf_horaires);
            horaires.setText(getString(R.string.horaires_pr));

            //Favoris
            updateFavorisStar("prf", parkAndRideDetails.getId());
            setOnClickFavoris("prf", parkAndRideDetails.getId());
        }
        else if (o instanceof UndergroundParkingDetails) {
            //Places PMR invisible
            placesPMR = myView.findViewById(R.id.ipf_nb_places_pmr);
            placesPMR.setVisibility(View.INVISIBLE);

            //Divider visible
            divider = myView.findViewById(R.id.ipf_divider);
            divider.setVisibility(View.VISIBLE);

            UndergroundParkingDetails undergroundParkingDetails = (UndergroundParkingDetails) o;

            //Nom du parking
            nom = myView.findViewById(R.id.ipf_nom);
            nom.setText(getString(R.string.parking) + undergroundParkingDetails.getNomParking());

            //Place dans le Parking
            places = myView.findViewById(R.id.ipf_nb_places);

            //Geolocalisation du parking
            List<Double> coord = undergroundParkingDetails.getGeo();
            lat = coord.get(0);
            lon = coord.get(1);

            if(undergroundParkingDetails.getStatus().equals(getString(R.string.parking_ferme_short))){
                places.setText(getString(R.string.parking_ferme_long));
                places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_red));
            }
            else if (undergroundParkingDetails.getStatus().equals(getString(R.string.parking_complet_short)) || undergroundParkingDetails.getPlacesLibres() == 0){
                places.setText(getString(R.string.parking_complet_long));
                places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_red));
            }
            else {
                if (undergroundParkingDetails.getPlacesLibres() <= undergroundParkingDetails.getPlacesMax()*0.2) {
                    places.setText(undergroundParkingDetails.getPlacesLibres() + getString(R.string.places_dispos));
                    places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_orange));
                }
                else {
                    places.setText(undergroundParkingDetails.getPlacesLibres() + getString(R.string.places_dispos));
                    places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_green));
                }

            }

            //Adresse
            adresse = myView.findViewById(R.id.ipf_adresse);
            adresse.setText(getAddress(undergroundParkingDetails.getGeo().get(0), undergroundParkingDetails.getGeo().get(1)));

            //Horaires
            horaires = myView.findViewById(R.id.ipf_horaires);
            horaires.setText(undergroundParkingDetails.getHoraires());

            //Tarifs
            tarifsPairs = new ArrayList<>();
            tarifsPairs.add(new Pair<>(getString(R.string.tarif_15), undergroundParkingDetails.getTarif15()));
            tarifsPairs.add(new Pair<>(getString(R.string.tarif_30), undergroundParkingDetails.getTarif30()));
            tarifsPairs.add(new Pair<>(getString(R.string.tarif_1h), undergroundParkingDetails.getTarif1h()));
            tarifsPairs.add(new Pair<>(getString(R.string.tarif_1h30), undergroundParkingDetails.getTarif1h30()));
            tarifsPairs.add(new Pair<>(getString(R.string.tarif_2h), undergroundParkingDetails.getTarif2h()));
            tarifsPairs.add(new Pair<>(getString(R.string.tarif_3h), undergroundParkingDetails.getTarif3h()));
            tarifsPairs.add(new Pair<>(getString(R.string.tarif_4h), undergroundParkingDetails.getTarif4h()));

            tarifs = myView.findViewById(R.id.ipf_tarifs_liste);
            tarifs.setVisibility(View.VISIBLE);
            tarifsTexte = myView.findViewById(R.id.ipf_tarifs_texte);
            tarifsTexte.setVisibility(View.VISIBLE);

            for (Pair<String, String> tarif : tarifsPairs
                 ) {
                    TableRow row = new TableRow(getContext());

                    TextView tv = new TextView(getContext());
                    tv.setText(tarif.first + tarif.second + "€");
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_grey_5));
                    tv.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);

                    row.addView(tv);

                    tarifs.addView(row);
            }

            //Favoris
            updateFavorisStar("upf", undergroundParkingDetails.getId());
            setOnClickFavoris("upf", undergroundParkingDetails.getId());

        }

        return myView ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton gMapsLink = (FloatingActionButton) view.findViewById(R.id.ipf_maps_button);
        gMapsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lon);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        mv = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.ipf_adresse_map));
        mv.onCreate(savedInstanceState);
        mv.onResume();
        mv.getMapAsync(this);
    }


    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this.getContext(), Locale.FRANCE);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getAddressLine(0));
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        return result.toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng coords = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions()
                .position(coords)
                .title("nom"));
        CameraPosition camPos = new CameraPosition.Builder().target(coords).zoom(15).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPos));
    }

    public void setOnClickFavoris(String favoris_key, String id){

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        favoris.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            Set<String> parkingsFavoris = sharedPref.getStringSet(favoris_key, new HashSet<>());
            if(parkingsFavoris.contains(id)){
                parkingsFavoris.remove(id);
                editor.putStringSet(favoris_key, parkingsFavoris);
                editor.apply();
                favoris.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
            else {
                parkingsFavoris.add(id);
                editor.putStringSet(favoris_key, parkingsFavoris);
                editor.apply();
                favoris.setImageResource(R.drawable.ic_baseline_star_24);
            }

            updateFavorisStar(favoris_key, id);
        });
    }

    public void updateFavorisStar(String favoris_key, String id){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Set<String> parkingsFavoris = sharedPref.getStringSet(favoris_key, new HashSet<>());
        if(parkingsFavoris.contains(id)){
            favoris.setImageResource(R.drawable.ic_baseline_star_24);
        }
        else {
            favoris.setImageResource(R.drawable.ic_baseline_star_border_24);
        }
    }
}