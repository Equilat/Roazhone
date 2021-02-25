package com.example.roazhone.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.roazhone.api.APICalls;
import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.UndergroundParkingDetails;

import java.util.List;

public class TestViewModel extends AndroidViewModel {
    private APICalls apiCalls;
    private LiveData<List<UndergroundParkingDetails>> undergroundParkingsResponseLiveData;
    private MutableLiveData<List<ParkAndRideDetails>> relayParkingsResponseLiveData;

    public TestViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        apiCalls = new APICalls();
        undergroundParkingsResponseLiveData = apiCalls.getUndergroundParkingDetailsLiveData();
        relayParkingsResponseLiveData = apiCalls.getParkAndRideLiveData();
    }

    public void searchUndergroundParkings() {
        apiCalls.searchUndergroundParkingDetails();
    }

    public void searchRelayParkings() {
        apiCalls.searchParkAndRideDetails();
    }

    public LiveData<List<UndergroundParkingDetails>> getUndergroundParkingsResponseLiveData() {
        return undergroundParkingsResponseLiveData;
    }

    public MutableLiveData<List<ParkAndRideDetails>> getRelayParkingsResponseLiveData() {
        return relayParkingsResponseLiveData;
    }
}
