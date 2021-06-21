package com.mtx.mobile.employee.student;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtx.mobile.employee.R;
import com.mtx.mobile.employee.model.UploadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DieHard_04 on 22-05-2021.
 */
public class StudentAcademicAdapter extends RecyclerView.Adapter<StudentAcademicAdapter.MyViewHolder> {
    private List<UploadInfo> list = new ArrayList<>();
    private Context context;

    public StudentAcademicAdapter(Context context, List<UploadInfo> models) {
        this.context = context;
        this.list = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_student_academic, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvDoc.setText(list.get(position).getName());
        holder.tvEventName.setText(list.get(position).getEventName());
        holder.tvEventTime.setText(list.get(position).getEventTime());
        holder.tvEventDes.setText(list.get(position).getEventDescription());
        holder.tvDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Opening the upload file in browser using the upload url
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(list.get(position).getUrl()));
                context.startActivity(intent);

//                Intent intent = new Intent(v.getContext(), ViewPdfActivity.class);
//                intent.putExtra("url", list.get(position).getUrl());
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvDoc, tvEventName, tvEventTime, tvEventDes;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoc = itemView.findViewById(R.id.tvDoc);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventTime = itemView.findViewById(R.id.tvEventDate);
            tvEventDes = itemView.findViewById(R.id.tvEventDesc);
        }
    }

    private String getFileExtentaion(Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return  map.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
