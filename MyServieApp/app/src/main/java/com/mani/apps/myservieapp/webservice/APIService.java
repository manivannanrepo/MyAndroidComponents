package com.mani.apps.myservieapp.webservice;

import com.mani.apps.myservieapp.model.Change;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("changes/")
    Call<List<Change>> loadChanges(@Query("q") String status);

}
