package com.example.roazhone;

import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.UndergroundParkingDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InfosParkingFragment extends Fragment {

    private TextView nom;
    private TextView places;
    private TextView placesPMR;
    private TextView adresse;
    private TextView horaires;
    private TableLayout tarifs;
    private ArrayList<Pair<String, String>> tarifsPairs;
    private TableRow row;
    private TextView tv1;
    private View divider;
    private TextView tarifsTexte;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.infos_parking_fragment, container, false);

        //Exemple 2
        /*
        UndergroundParkingDetails parkingDetails = new UndergroundParkingDetails();
        parkingDetails.setNomParking("Hoche");
        parkingDetails.setStatus("OUVERT");
        parkingDetails.setPlacesLibres(13);
        parkingDetails.setPlacesMax(325);
        List<Double> coord = new ArrayList<>();
        coord.add(48.1152367);
        coord.add(-1.677049014);
        parkingDetails.setGeo(coord);
        parkingDetails.setHoraires("Lundi, 7h30 à minuit. Mardi au samedi, 7h30 à 2h, sauf dimanche et jours fériés.");
        parkingDetails.setTarif15("0.40");
        parkingDetails.setTarif30("0.80");
        parkingDetails.setTarif1h("1.60");
        parkingDetails.setTarif1h30("1.80");
        parkingDetails.setTarif2h("3");
        parkingDetails.setTarif3h("4");
        parkingDetails.setTarif4h("5");
         */

        //Exemple
        ParkAndRideDetails parkingDetails = new ParkAndRideDetails();
        parkingDetails.setNomParking("Henri Fréville");
        parkingDetails.setStatus("OUVERT");
        parkingDetails.setPlacesLibres(20);
        parkingDetails.setCapaciteActuelle(400);
        parkingDetails.setNombreLibresPMR(15);
        List<Double> coord = new ArrayList<>();
        coord.add(48.1152367);
        coord.add(-1.677049014);
        parkingDetails.setCoordonnees(coord);

        Object o = parkingDetails;

        if(o instanceof ParkAndRideDetails) {

            ParkAndRideDetails relayParkingDetails = (ParkAndRideDetails) o;

            //Tarifs, horaires et divider invisibles
            tarifs = myView.findViewById(R.id.ipf_tarifs_liste);
            tarifs.setVisibility(View.INVISIBLE);
            tarifsTexte = myView.findViewById(R.id.ipf_tarifs_texte);
            tarifsTexte.setVisibility(View.INVISIBLE);
            horaires = myView.findViewById(R.id.ipf_horaires);
            horaires.setVisibility(View.INVISIBLE);
            divider = myView.findViewById(R.id.ipf_divider);
            divider.setVisibility(View.INVISIBLE);

            //Nom du parking
            nom = myView.findViewById(R.id.ipf_nom);
            nom.setText(getString(R.string.parking) + relayParkingDetails.getNomParking());

            //Place dans le Parking
            places = myView.findViewById(R.id.ipf_nb_places);
            placesPMR = myView.findViewById(R.id.ipf_nb_places_pmr);

            if(relayParkingDetails.getStatus().equals(getString(R.string.parking_ferme_short))){
                places.setText(getString(R.string.parking_ferme_long));
                places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_red));
                placesPMR.setVisibility(View.INVISIBLE);

            }
            else if (relayParkingDetails.getStatus().equals(getString(R.string.parking_complet_short))){
                places.setText(getString(R.string.parking_complet_long));
                places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_red));
                placesPMR.setVisibility(View.INVISIBLE);

            }
            else {
                if (relayParkingDetails.getPlacesLibres() <= relayParkingDetails.getCapaciteActuelle()*0.1) {
                    places.setText(relayParkingDetails.getPlacesLibres() + getString(R.string.places_dispos));
                    places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_orange));
                    placesPMR.setText(relayParkingDetails.getNombreLibresPMR() + getString(R.string.places_pmr_dispos));
                    placesPMR.setVisibility(View.VISIBLE);
                }
                else {
                    places.setText(relayParkingDetails.getPlacesLibres() + getString(R.string.places_dispos));
                    places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_green));
                    placesPMR.setText(relayParkingDetails.getNombreLibresPMR() + getString(R.string.places_pmr_dispos));
                    placesPMR.setVisibility(View.VISIBLE);
                }
            }

            //Adresse
            adresse = myView.findViewById(R.id.ipf_adresse);
            adresse.setText(getAddress(relayParkingDetails.getCoordonnees().get(0), relayParkingDetails.getCoordonnees().get(1)));

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

            if(undergroundParkingDetails.getStatus().equals(getString(R.string.parking_ferme_short))){
                places.setText(getString(R.string.parking_ferme_long));
                places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_red));
            }
            else if (undergroundParkingDetails.getStatus().equals(getString(R.string.parking_complet_short))){
                places.setText(getString(R.string.parking_complet_long));
                places.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_red));
            }
            else {
                if (undergroundParkingDetails.getPlacesLibres() <= undergroundParkingDetails.getPlacesMax()*0.1) {
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
            horaires.setVisibility(View.VISIBLE);
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
                    row = new TableRow(getContext());

                    tv1 = new TextView(getContext());
                    tv1.setText(tarif.first + tarif.second + "€");
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    tv1.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_grey_5));
                    tv1.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);

                    row.addView(tv1);

                    tarifs.addView(row);
            }
        }

        return myView ;
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
}