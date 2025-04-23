package com.umersoftwares.gamerecorder;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import okhttp3.ResponseBody;
import retrofit2.http.POST;
import retrofit2.http.Path;


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

    @GET("/recordings/{filename}")
    Call<ResponseBody> downloadRecording(@Path("filename") String filename);

    @GET("/recordings")
    Call<FileListResponse> getRecordingsList();

    @DELETE("/delete/{filename}")
    Call<FileResponse> deleteRecording(@Path("filename") String filename);

    @POST("/transfer/{filename}")
    Call<FileResponse> transferRecording(@Path("filename") String filename);



}
