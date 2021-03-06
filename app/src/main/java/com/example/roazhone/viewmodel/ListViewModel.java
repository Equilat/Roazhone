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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ListViewModel extends AndroidViewModel {

    private final MutableLiveData<String> lastUpdateTime;
    private APICalls repository;
    private MutableLiveData<List<UndergroundParkingDetails>> undergroundParkingDetails;
    private MutableLiveData<List<ParkAndRideDetails>> parkAndRideDetails;
    private double latitude = 0;
    private double longitude = 0;

    public ListViewModel(@NonNull Application application) {
        super(application);
        lastUpdateTime = new MutableLiveData<>();
        undergroundParkingDetails = new MutableLiveData<>();
        parkAndRideDetails = new MutableLiveData<>();
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

    public void initialize(boolean internetConnection) {
        repository = APICalls.getInstance();
        if (internetConnection) {
            undergroundParkingDetails = repository.searchUndergroundParkingDetails();
            parkAndRideDetails = repository.searchParkAndRideDetails();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            lastUpdateTime.postValue(sdf.format(new Date()));
        } else {
            lastUpdateTime.postValue("Pas d'accès Internet");
        }

    }

    public LiveData<String> getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void sortUnderByFreePlaces() {
        if(undergroundParkingDetails.getValue() != null) {
            undergroundParkingDetails.getValue().sort(UndergroundParkingDetails.undergroundFreePlacesComparator);
        }
    }

    public void sortPrByFreePlaces() {
        if(parkAndRideDetails.getValue() != null) {
            parkAndRideDetails.getValue().sort(ParkAndRideDetails.parkAndRideFreePlacesComparator);
        }
    }

    public void sortUnderByUserDistance() {
        if(undergroundParkingDetails.getValue() != null) {
            undergroundParkingDetails.getValue().sort(UndergroundParkingDetails.undergroundUserDistanceComparator);
        }
    }

    public void sortPrByUserDistance() {
        if(parkAndRideDetails.getValue() != null) {
            parkAndRideDetails.getValue().sort(ParkAndRideDetails.parkAndRideDetailsUserDistanceComparator);
        }
    }

    public void computeUserDistancesUnderground() {
        if (latitude != 0 && longitude != 0 && undergroundParkingDetails.getValue() != null) {
            Objects.requireNonNull(undergroundParkingDetails.getValue()).forEach(upd -> upd.computeUserDistance(latitude, longitude));
        }
    }

    public void computeUserDistancesPr() {
        if (latitude != 0 && longitude != 0 && parkAndRideDetails.getValue() != null) {
            parkAndRideDetails.getValue().forEach(prd -> prd.computeUserDistance(latitude, longitude));
        }
    }
}
