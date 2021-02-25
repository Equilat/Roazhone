package com.example.roazhone.api;

import android.net.TrafficStats;
import android.util.Log;

import com.example.roazhone.model.ParkAndRideDetails;
import com.example.roazhone.model.UndergroundParkingDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Service using Retrofit to calls the remote API.
 */
public interface APIService {
    String TAG = APIService.class.getName();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(List.class, new Deserializer<List<UndergroundParkingDetails>>())
            .registerTypeAdapter(List.class, new Deserializer<List<ParkAndRideDetails>>())
            .create();

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new LoggingInterceptor())
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://data.rennesmetropole.fr/api/")
            .addConverterFactory(new NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build();


    @GET("records/1.0/search/?dataset=export-api-parking-citedia&q=")
    Call<List<UndergroundParkingDetails>> getUndergroundParkingDetails();

    @GET("records/1.0/search/?dataset=etat-des-parcs-relais-du-reseau-star-en-temps-reel&q=&sort=idparc&facet=nom&facet=etat ")
    Call<List<ParkAndRideDetails>> getRelayParkingDetails();

    /**
     * Custom Interceptor.
     */
    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            TrafficStats.setThreadStatsTag(1234);
            Request request = chain.request();
            long t1 = System.nanoTime();
            Log.wtf(TAG, String.format("--> Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            okhttp3.Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Log.wtf(TAG, String.format("<-- Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            assert response.body() != null;
            MediaType contentType = response.body().contentType();
            String content = response.body().string();
            ResponseBody wrappedBody = ResponseBody.create(contentType, content);
            return response.newBuilder().body(wrappedBody).build();
        }
    }
}
