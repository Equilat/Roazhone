package com.example.roazhone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roazhone.api.APICalls;
import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.UndergroundParkingDetails;
import com.example.roazhone.viewmodel.ListViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeFragment extends Fragment implements View.OnLongClickListener {

    private BottomNavigationView bottomNavigationView;
    private ListViewModel listViewModel;
    private APICalls apiCalls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.home_fragment, container, false);
        bottomNavigationView = myView.findViewById(R.id.activity_main_bottom_navigation);
        disableMenuTooltip();

        return myView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.cardList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final UndergroundParkingAdapter undergroundParkingAdapter = new UndergroundParkingAdapter();
        final ParkAndRideAdapter parkAndRideAdapter = new ParkAndRideAdapter();
        recyclerView.setAdapter(undergroundParkingAdapter);

        listViewModel = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        listViewModel.initialize();
        listViewModel.getUndergroundParkingDetails().observe(getViewLifecycleOwner(), new Observer<List<UndergroundParkingDetails>>() {
            @Override
            public void onChanged(@Nullable List<UndergroundParkingDetails> undergroundParkingDetails) {
                undergroundParkingAdapter.setParkings(undergroundParkingDetails);
            }
        });
        /**listViewModel.getUndergroundParkingDetails().observe(getViewLifecycleOwner(), new Observer<List<UndergroundParkingDetails>>() {
        @Override public void onChanged(@Nullable List<UndergroundParkingDetails> undergroundParkingDetails) {
        undergroundParkingAdapter.setParkings(undergroundParkingDetails);
        }
        });*/
        listViewModel.getParkAndRideDetails().observe(getViewLifecycleOwner(), new Observer<List<ParkAndRideDetails>>() {
            @Override
            public void onChanged(@Nullable List<ParkAndRideDetails> parkAndRideDetails) {
                parkAndRideAdapter.setParkings(parkAndRideDetails);
            }
        });
    }

    private void disableMenuTooltip() {
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
