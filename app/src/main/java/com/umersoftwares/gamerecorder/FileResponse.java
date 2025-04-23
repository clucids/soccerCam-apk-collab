package com.umersoftwares.gamerecorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}