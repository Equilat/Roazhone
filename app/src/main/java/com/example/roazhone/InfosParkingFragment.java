package com.example.roazhone;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.resources.TextAppearance;

import java.util.HashMap;
import java.util.Map;

public class InfosParkingFragment extends Fragment {

    private TextView nom;
    private TextView places;
    private TextView adresse;
    private TextView horaires;
    private TableLayout tarifs;
    private Map<String, String> tarifsMap;
    private TableRow row;
    private TextView tv1,tv2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.infos_parking_fragment, container, false);

        tarifsMap = new HashMap<>();

        tarifsMap.put("Tarif 15'", "0.40");
        tarifsMap.put("Tarif 30'", "0.80");
        tarifsMap.put("Tarif 1h", "1.60");

        nom = myView.findViewById(R.id.ipf_nom);
        nom.setText("Parking Hoche");

        places = myView.findViewById(R.id.ipf_nb_places);
        places.setText("200 places disponible");

        adresse = myView.findViewById(R.id.ipf_adresse);
        adresse.setText("Place Hoche, 35000 Rennes");

        horaires = myView.findViewById(R.id.ipf_horaires);
        horaires.setText("Lundi, 7h30 à minuit. Mardi au samedi, 7h30 à 2h, sauf dimanche et jours fériés.");

        tarifs = myView.findViewById(R.id.ipf_tarifs_liste);
        for (Map.Entry<String, String> tarif : tarifsMap.entrySet()
             ) {
            row = new TableRow(getContext());

            tv1 = new TextView(getContext());
            tv1.setText(tarif.getKey() + " : ");
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv1.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_grey_4));


            tv2 = new TextView(getContext());
            tv2.setText(tarif.getValue() + "€");
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv2.setTextColor(ContextCompat.getColor(getContext(), R.color.roazhone_grey_4));

            row.addView(tv1);
            row.addView(tv2);

            tarifs.addView(row);

        }
        return myView ;
    }
}