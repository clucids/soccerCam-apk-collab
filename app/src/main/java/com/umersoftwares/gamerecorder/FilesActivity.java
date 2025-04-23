package com.umersoftwares.gamerecorder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilesActivity extends AppCompatActivity implements RecordingsAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecordingsAdapter adapter;
    private List<String> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_files);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Recordings List");
        }

        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecordingsAdapter(this, fileList,this);
        recyclerView.setAdapter(adapter);


        swipeRefreshLayout.setOnRefreshListener(this::refreshRecordingsList);

        refreshRecordingsList();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



    private void refreshRecordingsList() {
        swipeRefreshLayout.setRefreshing(true);

        fileList.clear();


        Api.getClient().getRecordingsList().enqueue(new Callback<FileListResponse>() {
            @Override
            public void onResponse(Call<FileListResponse> call, Response<FileListResponse> response) {
                if (response.body()!=null){
                    fileList.addAll(response.body().getFiles());
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(FilesActivity.this, "No files received", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<FileListResponse> call, Throwable t) {
                Toast.makeText(FilesActivity.this, "Error getting files list", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onDownloadClick(String fileName) {
        openUrlInBrowser("http://10.42.0.1:5000/recordings/"+fileName);
    }

    private void openUrlInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }


    @Override
    public void onUsbClick(String fileName) {
        Api.getClient().transferRecording(fileName).enqueue(new Callback<FileResponse>() {
            @Override
            public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                if (response.body()!=null){
                    Toast.makeText(FilesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FilesActivity.this, "USB Transfer not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FileResponse> call, Throwable t) {
                Toast.makeText(FilesActivity.this, "USB Transfer command send failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteClick(String fileName) {
        Api.getClient().deleteRecording(fileName).enqueue(new Callback<FileResponse>() {
            @Override
            public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                if (response.body()!=null){
                    Toast.makeText(FilesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().isSuccess()){
                        refreshRecordingsList();
                    }
                }else{
                    Toast.makeText(FilesActivity.this, "Invalid response from delete command", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FileResponse> call, Throwable t) {
                Toast.makeText(FilesActivity.this, "Delete command send failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}