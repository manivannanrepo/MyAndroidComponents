package com.mani.apps.myservieapp.webservice;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mani.apps.myservieapp.model.Change;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCalls {

    private static final String BASE_URL = "https://git.eclipse.org/r/";
    private APIService apiService;

    public RetrofitCalls() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        apiService = retrofit.create(APIService.class);
    }

    public void loadChages() {
        Call<List<Change>> call = apiService.loadChanges("status:open");
        call.enqueue(new Callback<List<Change>>() {
            @Override
            public void onResponse(@NonNull Call<List<Change>> call, @NonNull Response<List<Change>> response) {
                if (response.isSuccessful()) {
                    List<Change> changes = response.body();
                    if (changes != null) {
                        changes.forEach(change -> Log.i("Retrofit", "" + change.getSubject()));
                    }
                } else {
                    Log.i("Retrofit", "Error : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Change>> call, @NonNull Throwable t) {
                Log.i("Retrofit", "Failure : " + t.getMessage());
            }
        });
    }

}
