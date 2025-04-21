package com.umersoftwares.gamerecorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceState {

    @SerializedName("is_recording")
    @Expose
    private boolean isRecording;

    @SerializedName("is_paused")
    @Expose
    private boolean isPaused;

    // No-arg constructor (needed for Gson deserialization)
    public DeviceState() {
    }

    // Constructor
    public DeviceState(boolean isRecording, boolean isPaused) {
        this.isRecording = isRecording;
        this.isPaused = isPaused;
    }

    // Getters
    public boolean isRecording() {
        return isRecording;
    }

    public boolean isPaused() {
        return isPaused;
    }

    // Setters
    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}