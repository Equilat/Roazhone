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
    private static boolean intialized = false;
    private MutableLiveData<List<UndergroundParkingDetails>> undergroundParkingDetails;
    private MutableLiveData<List<ParkAndRideDetails>> parkAndRideDetails;

    public ListViewModel(@NonNull Application application) {
        super(application);
        if (!isIntialized()) {
            repository = new APICalls();
        }
        repository.searchUndergroundParkingDetails();
        repository.searchParkAndRideDetails();
        undergroundParkingDetails = repository.getUndergroundParkingDetailsLiveData();
        parkAndRideDetails = repository.getParkAndRideLiveData();
    }

    public boolean isIntialized() {
        return intialized;
    }

    public MutableLiveData<List<UndergroundParkingDetails>> getUndergroundParkingDetails() {
        return undergroundParkingDetails;
    }

    public MutableLiveData<List<ParkAndRideDetails>> getParkAndRideDetails() {
        return parkAndRideDetails;
    }

    public void setRepository(APICalls r) {
        repository = r;
        undergroundParkingDetails = repository.getUndergroundParkingDetailsLiveData();
        parkAndRideDetails = repository.getParkAndRideLiveData();
    }
}
