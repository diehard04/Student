package com.mtx.mobile.employee.faculty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtx.mobile.employee.R;
import com.mtx.mobile.employee.model.UploadInfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by DieHard_04 on 18-07-2021.
 */
public class FacultyAcademicAdapter extends RecyclerView.Adapter<FacultyAcademicAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<UploadInfo> infoList = new ArrayList<>();
    private FacultyAcademicInterface facultyAcademicInterface;

    public FacultyAcademicAdapter(Context context, ArrayList<UploadInfo> infoList, FacultyAcademicInterface facultyAcademicInterface) {
        this.context = context;
        this.infoList = infoList;
        this.facultyAcademicInterface = facultyAcademicInterface;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_faculty_academnic, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.tv_name.setText(infoList.get(position).getName());
        holder.tv_date.setText(infoList.get(position).getDateTime());
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facultyAcademicInterface.onclick(infoList.get(holder.getAdapterPosition()).getUid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name, tv_date;
        ImageView iv_delete;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iv_delete  = itemView.findViewById(R.id.iv_delete);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    public interface FacultyAcademicInterface {
        void onclick(String uid);
    }
}
