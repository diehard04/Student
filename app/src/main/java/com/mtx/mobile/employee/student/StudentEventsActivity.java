package com.mtx.mobile.employee.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mtx.mobile.employee.R;
import com.mtx.mobile.employee.model.Common;
import com.mtx.mobile.employee.model.UploadInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StudentEventsActivity extends AppCompatActivity {

    private RecyclerView rvStudentDoc;
    private StudentEventAdapter adapter;
    private List<UploadInfo> list = new ArrayList<>();
    private DatabaseReference mDatabaseRef;
    ProgressDialog dialog;
    private Toolbar toolBar;
    private AppCompatEditText searchEvent;
    private AppCompatButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_events);

        initView();
        getDocListFromFirebase();
    }

    private void getDocListFromFirebase() {
        list.clear();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Events");
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
        adapter = new StudentEventAdapter(this, list);
        rvStudentDoc.setLayoutManager(new LinearLayoutManager(this));
        rvStudentDoc.setAdapter(adapter);
        toolBar = findViewById(R.id.toolBar);
        toolBar.setTitle("Events : ");
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