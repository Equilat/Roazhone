package com.example.roazhone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment implements View.OnLongClickListener, NavigationView.OnNavigationItemSelectedListener, LocationListener, ParkAndRideAdapter.OnFavorisClicked, UndergroundParkingAdapter.OnFavorisClicked {

    public static final String permission_location_params = "La permission d'accès à la localisation est désactivée";
    public static final String permission_location_explain = "La permission d'accès à la localisation est nécessaire";
    static final int PERMISSION_ID = 1;
    private final String TAG = HomeFragment.class.getName();
    public LocationManager locationManager;
    private SwipeRefreshLayout swipeContainer;
    private boolean sortByDispo;
    private boolean sortByDistance;
    private boolean sortByFavoris;
    private Handler handler;
    private BottomNavigationView bottomNavigationView;
    private ListViewModel listViewModel;
    private RecyclerView recyclerView;
    private UndergroundParkingAdapter undergroundParkingAdapter;
    private ParkAndRideAdapter parkAndRideAdapter;
    private TextView viewUpdateTime;
    private View myView;
    private final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            Log.wtf(TAG, "Auto Refresh");
            getLocation();
            listViewModel.initialize();
            // Repeat this the same runnable code block again
            handler.postDelayed(this, 60000);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        myView = inflater.inflate(R.layout.home_fragment, container, false);
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

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Set<String> parkAndRideFavoris = sharedPref.getStringSet("prf", new HashSet<>());
        Set<String> undergroundFavoris = sharedPref.getStringSet("upf", new HashSet<>());

        undergroundParkingAdapter = new UndergroundParkingAdapter(this.getContext(), undergroundFavoris, this);
        parkAndRideAdapter = new ParkAndRideAdapter(this.getContext(), parkAndRideFavoris, this);

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
                listViewModel.computeUserDistancesUnderground();
                sortByDistance();
                //sortByFavoris();
                sortByDispo();
                undergroundParkingAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });

        listViewModel.getParkAndRideDetails().observe(getViewLifecycleOwner(), new Observer<List<ParkAndRideDetails>>() {
            @Override
            public void onChanged(@Nullable List<ParkAndRideDetails> parkAndRideDetails) {
                parkAndRideAdapter.setParkings(parkAndRideDetails);
                listViewModel.computeUserDistancesPr();
                sortByDistance();
                sortByDispo();
                //sortByFavoris();
                parkAndRideAdapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLocation();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_menu_dispo:
                item.setChecked(!item.isChecked());
                sortByDispo = item.isChecked();
                sortByDistance = false;
                sortByDispo();
                return true;
            case R.id.sort_menu_distance:
                item.setChecked(!item.isChecked());
                sortByDistance = item.isChecked();
                sortByDispo = false;
                sortByDistance();
                return true;
            case R.id.sort_menu_favoris:
                item.setChecked(!item.isChecked());
                sortByFavoris = item.isChecked();
                sortByFavoris();
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

    private void sortByFavoris() {
        if (sortByFavoris) {
            SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
            Set<String> upFavoris = sharedPref.getStringSet("upf", new HashSet<>());
            Set<String> prFavoris = sharedPref.getStringSet("prf", new HashSet<>());
            listViewModel.sortParkingByFavoris(upFavoris, prFavoris);
            undergroundParkingAdapter.notifyDataSetChanged();
            parkAndRideAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupCheckable(0, false, true);
        inflater.inflate(R.menu.sort_menu, menu);
    }

    /**
     * Get the user's location.
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.checkPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, "La localisation est nécessaire", PERMISSION_ID);
        } else {
            if (isLocationEnabled()) {
                undergroundParkingAdapter.setIsLoading(true);
                parkAndRideAdapter.setIsLoading(true);
                locationManager = (LocationManager) this.requireContext().getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            } else {
                Toast.makeText(this.getContext(), "Veuillez activer" + " votre GPS...", Toast.LENGTH_LONG).show();
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
        listViewModel.setLatitude(location.getLatitude());
        listViewModel.setLongitude(location.getLongitude());
        //Toast.makeText(this.getActivity(), "LOCATION CHANGED :" + listViewModel.getLatitude() + " | " + listViewModel.getLongitude(), Toast.LENGTH_SHORT).show();
        listViewModel.computeUserDistancesUnderground();
        listViewModel.computeUserDistancesPr();
        undergroundParkingAdapter.notifyDataSetChanged();
        parkAndRideAdapter.notifyDataSetChanged();
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
                this.explain(permission, requestCode, rational_text);
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
     * @param permission  permission
     * @param requestCode requestCode
     * @param message     a message that explains why the permission is needed
     */
    public void explain(String permission, int requestCode, String message) {
        Snackbar.make(myView, message, Snackbar.LENGTH_LONG).setAction("Activer", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.this.requestPermissions(new String[]{permission},
                        requestCode);
            }
        }).show();
    }

    /**
     * Shows a SnackBar in order for the user to go to the parameters
     * and check the permission.
     *
     * @param message explicative message
     */
    public void displayOptions(String message) {
        Snackbar.make(myView, message, Snackbar.LENGTH_LONG).setAction("Paramètres", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                final Uri uri = Uri.fromParts("package", HomeFragment.this.getActivity().getPackageName(), null);
                intent.setData(uri);
                HomeFragment.this.startActivity(intent);
            }
        }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && permissions.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    undergroundParkingAdapter.setIsLoading(true);
                    parkAndRideAdapter.setIsLoading(true);
                    getLocation();
                    listViewModel.initialize();
                } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
                    this.displayOptions(permission_location_params);
                } else {
                    this.explain(permissions[0], requestCode, permission_location_explain);
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onClickFavoris(String id, String key){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Set<String> parkingsFavoris = sharedPref.getStringSet(key, new HashSet<>());
        SharedPreferences.Editor editor = sharedPref.edit();
        if(parkingsFavoris.contains(id)){
            parkingsFavoris.remove(id);
        }
        else {
            parkingsFavoris.add(id);
        }
        editor.putStringSet(key, parkingsFavoris);
        boolean res = editor.commit();
        Log.d(TAG, "onClickFavoris: " + res);
    }

}
