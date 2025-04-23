package com.umersoftwares.gamerecorder;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Button;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    WebView webView1,webView2;
    String cameraOneURL = "http://10.42.0.1:5000/video_feed_0";
    String cameraTwoURL = "http://10.42.0.1:5000/video_feed_1";

    Handler handler;
    Runnable runnable;

    // State from Raspberry pi
    boolean isRecording, isPaused;

    boolean disconnectIssued = false;

    Button startStopButton, pauseResumeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        webViewOneInit();
        webViewTwoInit();

        startStopButton = findViewById(R.id.button_startstop);
        pauseResumeButton = findViewById(R.id.button_pauseresume);
        pauseResumeButton.setEnabled(false);

        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                // 👉 Your code to run every second
                Api.getClient().state().enqueue(new Callback<DeviceState>() {
                    @Override
                    public void onResponse(Call<DeviceState> call, Response<DeviceState> response) {
                        piStateHandle(response.body());
                    }

                    @Override
                    public void onFailure(Call<DeviceState> call, Throwable t) {
                        disconnectHandler();
                    }
                });

                // Post the runnable again after 500ms
                handler.postDelayed(this, 500);
            }
        };

        handler.post(runnable);

    }

    void piStateHandle(DeviceState deviceState){
        if (deviceState == null){
            // close the activity
            disconnectHandler();
        }
        isRecording = deviceState.isRecording();
        isPaused = deviceState.isPaused();

        if (!isRecording){
            startStopButton.setText("Start");
            pauseResumeButton.setText("Pause");
            pauseResumeButton.setEnabled(false);
        }else{
            startStopButton.setText("Stop");
            pauseResumeButton.setEnabled(true);
            if (isPaused){
                pauseResumeButton.setText("Resume");
            }else{
                pauseResumeButton.setText("Pause");
            }
        }
    }

    void disconnectHandler(){
        if (!disconnectIssued){
            disconnectIssued = true;
            Intent intent=new Intent(MainActivity.this, DisconnectActivity.class);
            startActivity(intent);
            finish();
        }
    }

    void webViewOneInit() {
        webView1 = findViewById(R.id.web_view_1);

        // Get screen width
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        // Aspect ratio: height = width * (720 / 1280)
        int calculatedHeight = (int) (screenWidth * (720.0 / 1280.0));

        // Set layout params
        ViewGroup.LayoutParams params = webView1.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = calculatedHeight;
        webView1.setLayoutParams(params);

        WebSettings webSettings = webView1.getSettings();
        webSettings.setJavaScriptEnabled(true);  // not strictly necessary unless you add interaction
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        webView1.setWebViewClient(new WebViewClient());
        webView1.loadUrl(cameraOneURL);
    }

    void webViewTwoInit() {
        webView2 = findViewById(R.id.web_view_2);

        // Get screen width
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        // Aspect ratio: height = width * (720 / 1280)
        int calculatedHeight = (int) (screenWidth * (720.0 / 1280.0));

        // Set layout params
        ViewGroup.LayoutParams params = webView2.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = calculatedHeight;
        webView2.setLayoutParams(params);

        WebSettings webSettings = webView2.getSettings();
        webSettings.setJavaScriptEnabled(true);  // not strictly necessary unless you add interaction
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        webView2.setWebViewClient(new WebViewClient());
        webView2.loadUrl(cameraTwoURL);
    }

    public void startStopClicked(View view){
        if (isRecording){
            stopClicked(view);
        }else{
            startClicked(view);
        }
    }
    public void pauseContinueClicked(View view){
        if (isRecording){
            if (isPaused){
                resumeClicked(view);
            }else{
                pauseClicked(view);
            }
        }
    }

    public void startClicked(View view){
        Api.getClient().record().enqueue(new Callback<DeviceState>() {
            @Override
            public void onResponse(Call<DeviceState> call, Response<DeviceState> response) {
                piStateHandle(response.body());
            }

            @Override
            public void onFailure(Call<DeviceState> call, Throwable t) {
                disconnectHandler();
            }
        });
    }
    public void stopClicked(View view){
        Api.getClient().stop().enqueue(new Callback<DeviceState>() {
            @Override
            public void onResponse(Call<DeviceState> call, Response<DeviceState> response) {
                piStateHandle(response.body());
            }

            @Override
            public void onFailure(Call<DeviceState> call, Throwable t) {
                disconnectHandler();
            }
        });
    }
    public void pauseClicked(View view){
        Api.getClient().pause().enqueue(new Callback<DeviceState>() {
            @Override
            public void onResponse(Call<DeviceState> call, Response<DeviceState> response) {
                piStateHandle(response.body());
            }

            @Override
            public void onFailure(Call<DeviceState> call, Throwable t) {
                disconnectHandler();
            }
        });
    }
    public void resumeClicked(View view){
        Api.getClient().resume().enqueue(new Callback<DeviceState>() {
            @Override
            public void onResponse(Call<DeviceState> call, Response<DeviceState> response) {
                piStateHandle(response.body());
            }

            @Override
            public void onFailure(Call<DeviceState> call, Throwable t) {
                disconnectHandler();
            }
        });
    }

    public void recordingsClicked(View view){
        Intent intent = new Intent(MainActivity.this, FilesActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}