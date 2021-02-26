package com.example.roazhone;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.roazhone.api.APICalls;
import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.UndergroundParkingDetails;
import com.example.roazhone.viewmodel.ListViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class HomeFragment extends Fragment implements View.OnLongClickListener, NavigationView.OnNavigationItemSelectedListener {
    private SwipeRefreshLayout swipeContainer;
    private Handler handler;
    private BottomNavigationView bottomNavigationView;
    private ListViewModel listViewModel;
    private final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            Log.wtf("Handlers", "Called on main thread");
            listViewModel.initialize();
            // Repeat this the same runnable code block again another 2 seconds
            // 'this' is referencing the Runnable object
            handler.postDelayed(this, 60000);
        }
    };
    private RecyclerView recyclerView;
    private APICalls apiCalls;
    private UndergroundParkingAdapter undergroundParkingAdapter;
    private ParkAndRideAdapter parkAndRideAdapter;
    private TextView viewUpdateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.home_fragment, container, false);
        bottomNavigationView = myView.findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        disableMenuTooltip();
        viewUpdateTime = myView.findViewById(R.id.last_update_time);

        handler = new Handler();
        handler.post(runnableCode);

        return myView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.cardList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        undergroundParkingAdapter = new UndergroundParkingAdapter(this.getContext());
        parkAndRideAdapter = new ParkAndRideAdapter(this.getContext());

        listViewModel = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        listViewModel.initialize();
        listViewModel.getLastUpdateTime().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String lastUpdateTime) {
                viewUpdateTime.setText(String.format("Mis à jour à : %s", lastUpdateTime));
            }
        });
        listViewModel.getUndergroundParkingDetails().observe(getViewLifecycleOwner(), new Observer<List<UndergroundParkingDetails>>() {
            @Override
            public void onChanged(@Nullable List<UndergroundParkingDetails> undergroundParkingDetails) {
                undergroundParkingAdapter.setParkings(undergroundParkingDetails);
                swipeContainer.setRefreshing(false);
            }
        });

        listViewModel.getParkAndRideDetails().observe(getViewLifecycleOwner(), new Observer<List<ParkAndRideDetails>>() {
            @Override
            public void onChanged(@Nullable List<ParkAndRideDetails> parkAndRideDetails) {
                parkAndRideAdapter.setParkings(parkAndRideDetails);
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                listViewModel.initialize();
            }

        });
        swipeContainer.setColorSchemeResources(R.color.roazhone_yellow);

    }

    public void onStart() {
        super.onStart();
        MenuItem selectedItem = bottomNavigationView.getMenu().findItem(bottomNavigationView.getSelectedItemId());
        if (selectedItem.getItemId() == R.id.parkingItem) {
            recyclerView.setAdapter(undergroundParkingAdapter);
        } else if (selectedItem.getItemId() == R.id.parkAndRideItem) {
            recyclerView.setAdapter(parkAndRideAdapter);
        }
    }

    private void disableMenuTooltip() {
        View parkingLogo = bottomNavigationView.getChildAt(0).findViewById(R.id.parkingItem);
        View parkAndRideLogo = bottomNavigationView.getChildAt(0).findViewById(R.id.parkAndRideItem);

        parkingLogo.setOnLongClickListener(this);
        parkAndRideLogo.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.parkingItem:
                recyclerView.setAdapter(undergroundParkingAdapter);
                break;
            case R.id.parkAndRideItem:
                recyclerView.setAdapter(parkAndRideAdapter);
                break;
        }
        return true;
    }
}
