package com.example.roazhone;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.UndergroundParkingDetails;
import com.example.roazhone.viewmodel.ListViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.time.Duration;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnLongClickListener, NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = HomeFragment.class.getName();
    private SwipeRefreshLayout swipeContainer;
    private boolean sortByDispo;
    private boolean sortByDistance;
    private Handler handler;
    private BottomNavigationView bottomNavigationView;
    private ListViewModel listViewModel;
    private final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            Log.wtf(TAG, "Auto Refresh");
            listViewModel.initialize();
            // Repeat this the same runnable code block again
            handler.postDelayed(this, 60000);
        }
    };
    private RecyclerView recyclerView;
    private UndergroundParkingAdapter undergroundParkingAdapter;
    private ParkAndRideAdapter parkAndRideAdapter;
    private TextView viewUpdateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
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
        recyclerView.setAdapter(undergroundParkingAdapter);

        listViewModel = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        listViewModel.initialize();
        listViewModel.getLastUpdateTime().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String lastUpdateTime) {
                viewUpdateTime.setText(String.format("Mis à jour à : %s", lastUpdateTime));
                swipeContainer.setRefreshing(false);
            }
        });
        listViewModel.getUndergroundParkingDetails().observe(getViewLifecycleOwner(), new Observer<List<UndergroundParkingDetails>>() {
            @Override
            public void onChanged(@Nullable List<UndergroundParkingDetails> undergroundParkingDetails) {
                sortByDispo();
                undergroundParkingAdapter.setParkings(undergroundParkingDetails);
                swipeContainer.setRefreshing(false);
            }
        });

        listViewModel.getParkAndRideDetails().observe(getViewLifecycleOwner(), new Observer<List<ParkAndRideDetails>>() {
            @Override
            public void onChanged(@Nullable List<ParkAndRideDetails> parkAndRideDetails) {
                sortByDispo();
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
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_menu_dispo:
                item.setChecked(!item.isChecked());
                sortByDispo = item.isChecked();
                sortByDispo();
                return true;
            case R.id.sort_menu_distance:
                item.setChecked(!item.isChecked());
                sortByDistance = item.isChecked();
                Toast.makeText(this.getContext(), "WIP", Toast.LENGTH_LONG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortByDispo() {
        if (sortByDispo) {
            listViewModel.sortParkingByFreePlaces();
            undergroundParkingAdapter.notifyDataSetChanged();
            parkAndRideAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //menu.setGroupCheckable(0, false, true);
        inflater.inflate(R.menu.sort_menu, menu);
    }
}
