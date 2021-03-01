package com.example.roazhone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.UndergroundParkingDetails;
import com.example.roazhone.viewmodel.ListViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment implements View.OnLongClickListener, NavigationView.OnNavigationItemSelectedListener, LocationListener {

    public static final String permission_location_params = "La permission d'accès à la localisation est désactivée";
    public static final String permission_location_explain = "La permission d'accès à la localisation est nécessaire";
    static final int PERMISSION_ID = 1;
    private final String TAG = HomeFragment.class.getName();
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    private SwipeRefreshLayout swipeContainer;
    private boolean sortByDispo;
    private boolean sortByDistance;
    private Handler handler;
    private BottomNavigationView bottomNavigationView;
    private ListViewModel listViewModel;
    private RecyclerView recyclerView;
    private UndergroundParkingAdapter undergroundParkingAdapter;
    private ParkAndRideAdapter parkAndRideAdapter;
    private TextView viewUpdateTime;
    private FusedLocationProviderClient mFusedLocationClient;
    private double userLatitude;
    private double userLongitude;
    private View myView;
    private final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            Log.wtf(TAG, "Auto Refresh");
            listViewModel.initialize();
            getLocation();
            // Repeat this the same runnable code block again
            handler.postDelayed(this, 60000);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        myView = inflater.inflate(R.layout.home_fragment, container, false);
        bottomNavigationView = myView.findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        disableMenuTooltip();
        viewUpdateTime = myView.findViewById(R.id.last_update_time);

        handler = new Handler();
        handler.post(runnableCode);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        return myView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.cardList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Set<String> parkAndRideFavoris = sharedPref.getStringSet("prf", new HashSet<>());
        Set<String> undergroundFavoris = sharedPref.getStringSet("upf", new HashSet<>());

        undergroundParkingAdapter = new UndergroundParkingAdapter(this.getContext(), undergroundFavoris);
        parkAndRideAdapter = new ParkAndRideAdapter(this.getContext(), parkAndRideFavoris);

        listViewModel = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        getLocation();
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
                undergroundParkingAdapter.setParkings(undergroundParkingDetails);
                listViewModel.computeUserDistancesUnderground(userLatitude, userLongitude);
                sortByDispo();
                sortByDistance();
                undergroundParkingAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });

        listViewModel.getParkAndRideDetails().observe(getViewLifecycleOwner(), new Observer<List<ParkAndRideDetails>>() {
            @Override
            public void onChanged(@Nullable List<ParkAndRideDetails> parkAndRideDetails) {
                parkAndRideAdapter.setParkings(parkAndRideDetails);
                listViewModel.computeUserDistancesPr(userLatitude, userLongitude);
                sortByDispo();
                sortByDistance();
                parkAndRideAdapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                listViewModel.initialize();
                getLocation();
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
                sortByDistance();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sort the parking by number of free places.
     */
    private void sortByDispo() {
        if (sortByDispo) {
            listViewModel.sortParkingByFreePlaces();
            undergroundParkingAdapter.notifyDataSetChanged();
            parkAndRideAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Sort the parking by distance to the user.
     */
    private void sortByDistance() {
        if (sortByDistance) {
            listViewModel.sortParkingByUserDistance();
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

    /**
     * Get the user's location.
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.checkPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, "La localisation est nécessaire", PERMISSION_ID);
        } else {
            if (isLocationEnabled()) {
                locationManager = (LocationManager) this.requireContext().getSystemService(Context.LOCATION_SERVICE);
                criteria = new Criteria();
                bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null) {
                    Log.e("TAG", "GPS is on");
                    userLatitude = location.getLatitude();
                    userLongitude = location.getLongitude();
                    Toast.makeText(this.getActivity(), "latitude:" + userLatitude + " longitude:" + userLongitude, Toast.LENGTH_SHORT).show();
                } else {
                    locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
                }
            } else {
                Toast.makeText(this.getContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }
    }

    /**
     * Checking if location is enabled.
     */
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.requireContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        locationManager.removeUpdates(this);
        userLatitude = location.getLatitude();
        userLongitude = location.getLongitude();
        Toast.makeText(this.getActivity(), "LOCATION CHANGED latitude:" + userLatitude + " longitude:" + userLongitude, Toast.LENGTH_SHORT).show();
    }

    /**
     * Check if the permissions or granted and ask them if it is not.
     *
     * @param permissions   permissions to verify/ask
     * @param rational_text explicative text about the permission
     * @param requestCode   requestCode
     */
    public void checkPermission(String[] permissions, String rational_text, int requestCode) {
        for (String permission : permissions) {
            if (this.requireActivity().shouldShowRequestPermissionRationale(permission)) {
                this.explain(this.getActivity(), permission, requestCode, rational_text);
            } else {
                this.requestPermissions(new String[]{permission},
                        requestCode);
            }
        }
    }

    /**
     * Shows a SnackBar in order for the user to go understand why the permission is needed and to re-ask the
     * permission.
     *
     * @param activity    activity
     * @param permission  permission
     * @param requestCode requestCode
     * @param message     a message that explains why the permission is needed
     */
    public void explain(Activity activity, String permission, int requestCode, String message) {
        Snackbar.make(myView, message, Snackbar.LENGTH_LONG).setAction("Activer", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.requestPermissions(new String[]{permission},
                        requestCode);
            }
        }).show();
    }

    /**
     * Shows a SnackBar in order for the user to go to the parameters
     * and check the permission.
     *
     * @param activity activity
     * @param message  explicative message
     */
    public void displayOptions(Activity activity, String message) {
        Snackbar.make(myView, message, Snackbar.LENGTH_LONG).setAction("Paramètres", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                final Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && permissions.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                    System.out.println("on perm result get location");
                } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
                    this.displayOptions(this.getActivity(), permission_location_params);
                } else {
                    this.explain(this.getActivity(), permissions[0], requestCode, permission_location_explain);
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
