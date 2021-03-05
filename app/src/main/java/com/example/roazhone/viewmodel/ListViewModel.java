package com.example.roazhone.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.roazhone.api.APICalls;
import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.UndergroundParkingDetails;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class ListViewModel extends AndroidViewModel {

    private final MutableLiveData<String> lastUpdateTime;
    private APICalls repository;
    private MutableLiveData<List<UndergroundParkingDetails>> undergroundParkingDetails;
    private MutableLiveData<List<ParkAndRideDetails>> parkAndRideDetails;
    private double latitude = 0;
    private double longitude = 0;

    public ListViewModel(@NonNull Application application) {
        super(application);
        lastUpdateTime = new MutableLiveData<String>();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public MutableLiveData<List<UndergroundParkingDetails>> getUndergroundParkingDetails() {
        return undergroundParkingDetails;
    }

    public MutableLiveData<List<ParkAndRideDetails>> getParkAndRideDetails() {
        return parkAndRideDetails;
    }

    public void initialize() {
        repository = APICalls.getInstance();
        undergroundParkingDetails = repository.searchUndergroundParkingDetails();
        parkAndRideDetails = repository.searchParkAndRideDetails();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        lastUpdateTime.setValue(sdf.format(new Date()));
    }

    public LiveData<String> getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void sortUnderByFreePlaces() {
        Objects.requireNonNull(undergroundParkingDetails.getValue()).sort(UndergroundParkingDetails.undergroundFreePlacesComparator);
    }
    public void sortPrByFreePlaces() {
        Objects.requireNonNull(parkAndRideDetails.getValue()).sort(ParkAndRideDetails.parkAndRideFreePlacesComparator);
    }

    public void sortUnderByUserDistance() {
        Objects.requireNonNull(undergroundParkingDetails.getValue()).sort(UndergroundParkingDetails.undergroundUserDistanceComparator);
    }
    public void sortPrByUserDistance() {
        Objects.requireNonNull(parkAndRideDetails.getValue()).sort(ParkAndRideDetails.parkAndRideDetailsUserDistanceComparator);
    }

    public void computeUserDistancesUnderground() {
        if (latitude != 0 && longitude != 0) {
            Objects.requireNonNull(undergroundParkingDetails.getValue()).forEach(upd -> upd.computeUserDistance(latitude, longitude));
        }
    }

    public void computeUserDistancesPr() {
        if (latitude != 0 && longitude != 0) {
            Objects.requireNonNull(parkAndRideDetails.getValue()).forEach(prd -> prd.computeUserDistance(latitude, longitude));
        }
    }
}
