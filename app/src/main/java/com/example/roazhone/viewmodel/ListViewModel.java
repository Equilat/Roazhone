package com.example.roazhone.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.roazhone.api.APICalls;
import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.UndergroundParkingDetails;

import java.util.List;

public class ListViewModel extends AndroidViewModel {

    private APICalls repository;
    private MutableLiveData<List<UndergroundParkingDetails>> undergroundParkingDetails;
    private MutableLiveData<List<ParkAndRideDetails>> parkAndRideDetails;

    public ListViewModel(@NonNull Application application) {
        super(application);
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
    }
}
