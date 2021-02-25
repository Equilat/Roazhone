package com.example.roazhone.api;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.UndergroundParkingDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Class which calls the remote API.
 */
public class APICalls {

    private static final String TAG = APICalls.class.getName();
    private static APICalls apiCalls;
    private final MutableLiveData<List<UndergroundParkingDetails>> undergroundParkingLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ParkAndRideDetails>> parkAndRideLiveData = new MutableLiveData<>();

    public static APICalls getInstance() {
        if (apiCalls == null) {
            apiCalls = new APICalls();
        }
        return apiCalls;
    }

    /**
     * Call the API to get all the undergroung parking of Rennes.
     */
    public MutableLiveData<List<UndergroundParkingDetails>> searchUndergroundParkingDetails() {
        APIService apiService = APIService.retrofit.create(APIService.class);
        apiService.getUndergroundParkingDetails().enqueue(new Callback<List<UndergroundParkingDetails>>() {
            @Override
            public void onResponse(Call<List<UndergroundParkingDetails>> call, Response<List<UndergroundParkingDetails>> response) {
                if (response.body() != null) {
                    List<UndergroundParkingDetails> parkings = response.body();
                    undergroundParkingLiveData.setValue(parkings);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UndergroundParkingDetails>> call, @NonNull Throwable t) {
                Log.wtf(TAG, "onResponseFailed : " + call.request().url());
                t.printStackTrace();
                undergroundParkingLiveData.setValue(null);
            }
        });
        return undergroundParkingLiveData;
    }

    /**
     * Call the API to get all the relay parking of Rennes.
     */
    public MutableLiveData<List<ParkAndRideDetails>> searchParkAndRideDetails() {
        APIService apiService = APIService.retrofit.create(APIService.class);
        apiService.getRelayParkingDetails().enqueue(new Callback<List<ParkAndRideDetails>>() {
            @Override
            public void onResponse(Call<List<ParkAndRideDetails>> call, Response<List<ParkAndRideDetails>> response) {
                if (response.body() != null) {
                    List<ParkAndRideDetails> parkings = response.body();
                    parkAndRideLiveData.setValue(parkings);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ParkAndRideDetails>> call, @NonNull Throwable t) {
                Log.wtf(TAG, "onResponseFailed : " + call.request().url());
                t.printStackTrace();
                parkAndRideLiveData.setValue(null);
            }
        });
        return parkAndRideLiveData;
    }
}
