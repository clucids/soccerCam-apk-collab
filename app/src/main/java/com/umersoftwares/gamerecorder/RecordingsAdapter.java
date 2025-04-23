package com.umersoftwares.gamerecorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.RecordingViewHolder> {

    private Context context;
    private List<String> fileList;
    private OnItemClickListener listener;

    // Constructor
    public RecordingsAdapter(Context context, List<String> fileList, OnItemClickListener listener) {
        this.context = context;
        this.fileList = fileList;
        this.listener = listener;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public RecordingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_card, parent, false);
        return new RecordingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordingViewHolder holder, int position) {
        String fileName = fileList.get(position);
        holder.textFileName.setText(fileName);

        // Click listeners for buttons
        holder.iconDownload.setOnClickListener(v -> listener.onDownloadClick(fileName));
        holder.iconUsb.setOnClickListener(v -> listener.onUsbClick(fileName));
        holder.iconDelete.setOnClickListener(v -> listener.onDeleteClick(fileName));
    }

    @Override
    public int getItemCount() {
        if (fileList == null){
            return 0;
        }
        return fileList.size();
    }


    static class RecordingViewHolder extends RecyclerView.ViewHolder {
        TextView textFileName;
        ImageButton iconDownload, iconUsb, iconDelete;

        public RecordingViewHolder(@NonNull View itemView) {
            super(itemView);
            textFileName = itemView.findViewById(R.id.text_file_name);
            iconDownload = itemView.findViewById(R.id.icon_download);
            iconUsb = itemView.findViewById(R.id.icon_usb);
            iconDelete = itemView.findViewById(R.id.icon_delete);
        }
    }

    // Interface for handling button clicks
    public interface OnItemClickListener {
        void onDownloadClick(String fileName);
        void onUsbClick(String fileName);
        void onDeleteClick(String fileName);
    }
}