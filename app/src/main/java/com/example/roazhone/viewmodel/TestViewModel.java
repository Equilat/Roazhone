package com.example.roazhone.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.roazhone.api.APICalls;
import com.example.roazhone.model.RelayParkingRecord;
import com.example.roazhone.model.UndergroundParkingRecord;

import java.util.List;

public class TestViewModel extends AndroidViewModel {
    private APICalls apiCalls;
    private LiveData<List<UndergroundParkingRecord>> undergroundParkingsResponseLiveData;
    private LiveData<List<RelayParkingRecord>> relayParkingsResponseLiveData;

    public TestViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        apiCalls = new APICalls();
        undergroundParkingsResponseLiveData = apiCalls.getUndergroundParkingRecords();
        relayParkingsResponseLiveData = apiCalls.getRelayParkingLiveData();
    }

    public void searchUndergroundParkings() {
        apiCalls.searchUndergroundParkingRecords();
    }

    public void searchRelayParkings() {
        apiCalls.searchRelayParkingRecords();
    }

    public LiveData<List<UndergroundParkingRecord>> getUndergroundParkingsResponseLiveData() {
        return undergroundParkingsResponseLiveData;
    }

    public LiveData<List<RelayParkingRecord>> getRelayParkingsResponseLiveData() {
        return relayParkingsResponseLiveData;
    }
}
