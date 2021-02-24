package com.example.roazhone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.roazhone.viewModel.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.concurrent.Flow.Subscription;

public class HomeFragment extends Fragment implements View.OnLongClickListener {

    private BottomNavigationView bottomNavigationView;
    private HomeViewModel homeViewModel;
    private Subscription loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.home_fragment, container, false);
        bottomNavigationView = myView.findViewById(R.id.activity_main_bottom_navigation);
        disableMenuTooltip();
        setupViewModel(savedInstanceState);

        return myView;
    }

    private void setupViewModel(Bundle savedInstanceState) {
        homeViewModel = new HomeViewModel();
        if(savedInstanceState == null) {
        }
        else {
        }

    }

    private void disableMenuTooltip(){
        View parkingLogo = bottomNavigationView.getChildAt(0).findViewById(R.id.parkingLogo);
        View parkAndRideLogo = bottomNavigationView.getChildAt(0).findViewById(R.id.parkAndRideLogo);

        parkingLogo.setOnLongClickListener(this);
        parkAndRideLogo.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }
}
