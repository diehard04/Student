package com.mtx.mobile.employee.student;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mtx.mobile.employee.R;
import com.mtx.mobile.employee.model.Common;
import com.mtx.mobile.employee.model.UploadInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StudentAcademicActivity extends AppCompatActivity {

    private RecyclerView rvStudentDoc;
    private StudentAcademicAdapter adapter;
    private List<UploadInfo> list = new ArrayList<>();
    private DatabaseReference mDatabaseRef;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_academic);

        initView();
        getDocListFromFirebase();
    }

    private void getDocListFromFirebase() {
        list.clear();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    UploadInfo uploadInfo = postSnapShot.getValue(UploadInfo.class);
                    Long currentDate = Common.convertdateToLong(Common.getCurrentDateAndTime());
                    Long accessDate = Common.convertdateToLong(uploadInfo.getDateTime());

                    if ((currentDate > accessDate)) {
                        list.add(uploadInfo);
                    }
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database Error : " + error.getMessage(), Toast.LENGTH_SHORT ).show();
                dialog.dismiss();

            }
        });

    }

    private void initView() {
        rvStudentDoc = findViewById(R.id.rvStudentDoc);
        adapter = new StudentAcademicAdapter(this, list);
        rvStudentDoc.setLayoutManager(new LinearLayoutManager(this));
        rvStudentDoc.setAdapter(adapter);
    }

    private String getFilePath() {
        String folderPath = Environment.getExternalStorageDirectory().toString().toString() + "/Student";
        File dir = new File(folderPath);
        if (!dir.exists() && !dir.mkdirs()) {

        }
        return folderPath;
    }

    private String getFileExtentaion(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return  map.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}