package com.example.roazhone.api;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.roazhone.model.RelayParkingDetails;
import com.example.roazhone.model.RelayParkingDetails;
import com.example.roazhone.model.UndergroundParkingDetails;
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
    private final MutableLiveData<List<UndergroundParkingDetails>> undergroundParkingLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<RelayParkingDetails>> relayParkingLiveData = new MutableLiveData<>();

    /**
     * Call the API to get all the undergroung parking of Rennes.
     */
    public void searchUndergroundParkingDetailss() {
        APIService apiService = APIService.retrofit.create(APIService.class);
        Call<List<UndergroundParkingDetails>> call = apiService.getUndergroundParkingDetails();
        call.enqueue(new Callback<List<UndergroundParkingDetails>>() {
            @Override
            public void onResponse(Call<List<UndergroundParkingDetails>> call, Response<List<UndergroundParkingDetails>> response) {
                if (response.body() != null) {
                    List<UndergroundParkingDetails> parkings = response.body();
                    undergroundParkingLiveData.postValue(parkings);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UndergroundParkingDetails>> call, @NonNull Throwable t) {
                Log.wtf(TAG, "onResponseFailed : " + call.request().url());
                t.printStackTrace();
                undergroundParkingLiveData.postValue(null);
            }
        });
    }

    /**
     * Call the API to get all the relay parking of Rennes.
     */
    public void searchRelayParkingDetailss() {
        APIService apiService = APIService.retrofit.create(APIService.class);
        Call<List<RelayParkingDetails>> call = apiService.getRelayParkingDetails();
        call.enqueue(new Callback<List<RelayParkingDetails>>() {
            @Override
            public void onResponse(Call<List<RelayParkingDetails>> call, Response<List<RelayParkingDetails>> response) {
                if (response.body() != null) {
                    List<RelayParkingDetails> parkings = response.body();
                    relayParkingLiveData.postValue(parkings);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RelayParkingDetails>> call, @NonNull Throwable t) {
                Log.wtf(TAG, "onResponseFailed : " + call.request().url());
                t.printStackTrace();
                relayParkingLiveData.postValue(null);
            }
        });
    }

    public MutableLiveData<List<UndergroundParkingDetails>> getUndergroundParkingDetailsLiveData() {
        return undergroundParkingLiveData;
    }

    public MutableLiveData<List<RelayParkingDetails>> getRelayParkingDetailsLiveData() {
        return relayParkingLiveData;
    }
}
