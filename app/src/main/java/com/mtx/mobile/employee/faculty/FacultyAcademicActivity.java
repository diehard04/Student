package com.mtx.mobile.employee.faculty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.mtx.mobile.employee.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;

import static java.security.AccessController.getContext;

public class FacultyAcademicActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String selectedDate;
    private AppCompatButton bttnPicDoc;
    private ArrayList<Object> docPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_academic);
        toolbar = findViewById(R.id.toolBar);
        selectedDate = getIntent().getStringExtra("selectedDate");
        toolbar.setTitle("Event:  " + selectedDate);
        askPermission();
        initView();
    }

    private void askPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // Storage permission are allowed.
                        bttnPicDoc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FilePickerBuilder.getInstance()
                                        .setMaxCount(10) //optional
                                        .setActivityTheme(R.style.LibAppTheme) //optional
                                        .pickFile(FacultyAcademicActivity.this);

                            }
                        });
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // Storage permission are not allowed.
                        askPermission();
                    }
                })
                .start();

    }

    private void initView() {
        bttnPicDoc = findViewById(R.id.bttnPicDoc);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_DOC:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getCharSequenceArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    Log.d("docPaths = ", docPaths.toString());

                    for (int i =0; i<docPaths.size(); i++) {
                        File file = new File(String.valueOf(docPaths.get(i)));
                        Log.d("file ", file.getAbsolutePath());
                        uploadFileToServer(file);
                    }
//                    String path = getFilePath();
//                    File file =  new File(path,"temp");
//                    String test = "test data";
//                    FileOutputStream fos = null; // save
//                    try {
//                        fos = new FileOutputStream(file, true);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        fos.write(test.getBytes());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        fos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
                break;
        }
        //addThemToView(photoPaths, docPaths);
    }

    private void uploadFileToServer(File file) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
            if (file != null){
                //displaying a progress dialog while upload is going on
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getFilePath() {
        String folderPath =  Environment.getExternalStorageDirectory().toString().toString() + "/Employee";
        File dir = new File(folderPath);
        if (!dir.exists() && !dir.mkdirs()) {

        }
        return folderPath;
    }
}