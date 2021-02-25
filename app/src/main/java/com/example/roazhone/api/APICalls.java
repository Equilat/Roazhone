package com.example.roazhone.api;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.roazhone.model.ParkAndRideRecord;
import com.example.roazhone.model.UndergroundParkingRecord;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Class which calls the remote API.
 */
public class APICalls {

    private static final String TAG = APICalls.class.getName();
    private final MutableLiveData<List<UndergroundParkingRecord>> undergroundParkingLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ParkAndRideRecord>> relayParkingLiveData = new MutableLiveData<>();

    /**
     * Call the API to get all the undergroung parking of Rennes.
     */
    public void searchUndergroundParkingRecords() {
        APIService apiService = APIService.retrofit.create(APIService.class);
        Call<List<UndergroundParkingRecord>> call = apiService.getUndergroundParkingRecords();
        call.enqueue(new Callback<List<UndergroundParkingRecord>>() {
            @Override
            public void onResponse(Call<List<UndergroundParkingRecord>> call, Response<List<UndergroundParkingRecord>> response) {
                if (response.body() != null) {
                    List<UndergroundParkingRecord> parkings = response.body();
                    undergroundParkingLiveData.postValue(parkings);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UndergroundParkingRecord>> call, @NonNull Throwable t) {
                Log.wtf(TAG, "onResponseFailed : " + call.request().url());
                t.printStackTrace();
                undergroundParkingLiveData.postValue(null);
            }
        });
    }

    /**
     * Call the API to get all the relay parking of Rennes.
     */
    public void searchRelayParkingRecords() {
        APIService apiService = APIService.retrofit.create(APIService.class);
        Call<List<ParkAndRideRecord>> call = apiService.getRelayParkingRecords();
        call.enqueue(new Callback<List<ParkAndRideRecord>>() {
            @Override
            public void onResponse(Call<List<ParkAndRideRecord>> call, Response<List<ParkAndRideRecord>> response) {
                if (response.body() != null) {
                    List<ParkAndRideRecord> parkings = response.body();
                    relayParkingLiveData.postValue(parkings);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ParkAndRideRecord>> call, @NonNull Throwable t) {
                Log.wtf(TAG, "onResponseFailed : " + call.request().url());
                t.printStackTrace();
                relayParkingLiveData.postValue(null);
            }
        });
    }

    public LiveData<List<UndergroundParkingRecord>> getUndergroundParkingRecords() {
        return undergroundParkingLiveData;
    }

    public MutableLiveData<List<ParkAndRideRecord>> getRelayParkingLiveData() {
        return relayParkingLiveData;
    }
}
