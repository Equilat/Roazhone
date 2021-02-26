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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ListViewModel extends AndroidViewModel {

    private MutableLiveData<String> lastUpdateTime;
    private APICalls repository;
    private MutableLiveData<List<UndergroundParkingDetails>> undergroundParkingDetails;
    private MutableLiveData<List<ParkAndRideDetails>> parkAndRideDetails;

    public ListViewModel(@NonNull Application application) {
        super(application);
        lastUpdateTime = new MutableLiveData<String>();
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

    public void sortParkingByFreePlaces(){
        Objects.requireNonNull(undergroundParkingDetails.getValue()).sort(UndergroundParkingDetails.undergroundFreePlacesComparator);
        Objects.requireNonNull(parkAndRideDetails.getValue()).sort(ParkAndRideDetails.parkAndRideFreePlacesComparator);
    }
}
