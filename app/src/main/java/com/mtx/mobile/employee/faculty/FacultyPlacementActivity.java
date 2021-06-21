package com.mtx.mobile.employee.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mtx.mobile.employee.R;
import com.mtx.mobile.employee.model.UploadInfo;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class FacultyPlacementActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String selectedDate;
    private AppCompatButton bttnPicDoc;
    private ArrayList<Object> docPaths = new ArrayList<>();
    // instance for firebase storage and StorageReference
    NotificationCompat.Builder mBuilder;
    private final static String notification_channel_id = "notification_channel_id";
    private final static String notification_channel_name = "notification_channel_name";
    private StorageReference mStorageRef;
    private DatabaseReference mDataBaseRef;
    private AppCompatButton bttnSubmit;
    private List<Uri> uriList = new ArrayList<>();
    private AppCompatEditText etEventName, etEventTime, etEventDescription;
    private TextInputLayout tilEventName, tilEventTime, tilEventDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_placement);
        toolbar = findViewById(R.id.toolBar);
        mStorageRef = FirebaseStorage.getInstance().getReference("placement");
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("placement");
        // get the Firebase  storage reference

        selectedDate = getIntent().getStringExtra("selectedDate");
        toolbar.setTitle("Placement Event:  " + selectedDate);
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
                                        .pickFile(FacultyPlacementActivity.this);

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
        bttnSubmit = findViewById(R.id.bttnSubmit);

        etEventName = findViewById(R.id.etEventName);
        etEventTime = findViewById(R.id.etEventTime);
        etEventDescription = findViewById(R.id.etEventDescription);

        tilEventName = findViewById(R.id.tilEventName);
        tilEventTime = findViewById(R.id.tilEventTime);
        tilEventDescription = findViewById(R.id.tilEventDescription);

        bttnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select Doc", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etEventName.getText().toString().trim().isEmpty()) {
                    tilEventName.setError("please Enter Event Name");
                    return;
                } else {
                    etEventName.setEnabled(true);
                    tilEventName.setError("");
                }
                if (etEventTime.getText().toString().trim().isEmpty()) {
                    tilEventTime.setError("please Enter Event Time");
                    return;
                } else {
                    etEventTime.setEnabled(true);
                    tilEventTime.setError("");
                }
                if (etEventDescription.getText().toString().trim().isEmpty()) {
                    tilEventDescription.setError("please Enter Event Description");
                    return;
                } else {
                    etEventDescription.setEnabled(true);
                    tilEventDescription.setError("");
                }

                for (int i = 0; i < uriList.size(); i++) {
                    String name = etEventName.getText().toString();
                    String date = etEventTime.getText().toString();
                    String desc = etEventDescription.getText().toString();
                    Log.d("FacultyPlacement ", " name = " + name+ " date = " + date + " desc = " + desc);
                    uploadFileToServer(uriList.get(i), name, date, desc);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getCharSequenceArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    Log.d("docPaths = ", docPaths.toString());

                    for (int i = 0; i < docPaths.size(); i++) {
                        Uri uri = (Uri) docPaths.get(i);
                        String s = getRealPathFromUri(getApplicationContext(), uri);
                        Log.d("file ", s);
                        uriList.add(uri);
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

    private void uploadFileToServer(final Uri uri, String eventName, String eventTime, String eventDescription) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            if (uri != null) {
                //displaying a progress dialog while upload is going on
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtentaion(uri));
                fileRef.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //if the upload is successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();
                                File file = new File(uri.toString());
                                String filename = file.getPath().substring(file.getPath().lastIndexOf("/") + 1);
                                addNotification(filename);


                                final String name = taskSnapshot.getMetadata().getName();
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                        String str = sdf.format(new Date());

                                        String dateTime = selectedDate + " " + str;
                                        UploadInfo model = new UploadInfo(name, uri.toString(), dateTime, eventName, eventTime, eventDescription);

                                        String uploadId = mDataBaseRef.push().getKey();
                                        mDataBaseRef.child(uploadId).setValue(model);
                                    }
                                });

                                //and displaying a success toast
                                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                //and displaying error message
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //calculating progress percentage
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                Log.d("progress = ", progress + "");
                                //displaying percentage in progress dialog
                                progressDialog.setMessage("Uploading...");
                            }
                        });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getFilePath() {
        String folderPath = Environment.getExternalStorageDirectory().toString().toString() + "/Employee";
        File dir = new File(folderPath);
        if (!dir.exists() && !dir.mkdirs()) {

        }
        return folderPath;
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void addNotification(String fileName) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, FacultyDashboard.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(notification_channel_id, notification_channel_name, importance);
            mChannel.setDescription("Student");
            mChannel.enableVibration(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(mChannel);

            mBuilder = new NotificationCompat.Builder(this, notification_channel_id);
            mBuilder.setContentTitle("Uploaded")
                    .setTicker("Student app")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setBadgeIconType(R.drawable.ic_notification)
                    .setContentText(fileName + " Upload successfully")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    //.setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                    .setChannelId(notification_channel_id)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setWhen(System.currentTimeMillis());
        } else {
            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentTitle("Uploaded")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentText(fileName + " Upload successfully")
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setDefaults(Notification.DEFAULT_VIBRATE);
        }
        notificationManager.notify(1, mBuilder.build());
    }

    private String getFileExtentaion(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}