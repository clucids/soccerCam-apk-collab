package com.umersoftwares.gamerecorder;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("/record")
    Call<DeviceState> record();

    @GET("/stop")
    Call<DeviceState> stop();

    @GET("/pause")
    Call<DeviceState> pause();

    @GET("/resume")
    Call<DeviceState> resume();

    @GET("/state")
    Call<DeviceState> state();

}
