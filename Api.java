package com.example.cc.postalcode;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface Api {

    @POST("maps/api/geocode/json?")
    Call<Map> Data(@QueryMap HashMap<String,String> data);

}
