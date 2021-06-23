package com.mtx.mobile.employee.student;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Calendar;
import java.util.List;

public class StudentPlacementActivity extends AppCompatActivity {


    private RecyclerView rvStudentDoc;
    private StudentAcademicAdapter adapter;
    private List<UploadInfo> list = new ArrayList<>();
    private DatabaseReference mDatabaseRef;
    ProgressDialog dialog;
    private View viewPickDate;
    public int mMonth, mYear, mDay, mHour, mMin, id;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_placement);

        initView();
    }
    private void getDocListFromFirebase(String selectedDate) {
        list.clear();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("placement");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    UploadInfo uploadInfo = postSnapShot.getValue(UploadInfo.class);
                    Long currentDate = Common.convertdateToLong(Common.getCurrentDateAndTime());
                    Long accessDate = Common.convertdateToLong(uploadInfo.getDateTime());

//                    if ((currentDate > accessDate)) {
                    String fileDateTime = uploadInfo.getDateTime();
                    String[] filDateTimes = fileDateTime.split(" ");
                    String date = filDateTimes[0];
                    if (date != null && date.equalsIgnoreCase(selectedDate))
                        list.add(uploadInfo);
                    //}
                }
                if (list.size() == 0) {
                    Toast.makeText(getApplicationContext(), "There is no file for this date : " + selectedDate, Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

    }

    private void initView() {
        rvStudentDoc = findViewById(R.id.rvStudentDoc);
        adapter = new StudentAcademicAdapter(this, list);
        rvStudentDoc.setLayoutManager(new LinearLayoutManager(this));
        rvStudentDoc.setAdapter(adapter);
        viewPickDate = findViewById(R.id.viewPickDate);
        viewPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        toolBar = findViewById(R.id.toolBar);

    }

    private void pickDate() {
        // get current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month + 1;
                    String mmonth = null, mday = null;
                    if (month < 10)
                        mmonth = "0" + month;
                    else
                        mmonth = "" + month;
                    if (dayOfMonth < 10)
                        mday = "0" + dayOfMonth;
                    else
                        mday = "" + dayOfMonth;
                    String selectedDate = mday + "-" + (mmonth) + "-" + year;
                    toolBar.setTitle("Event Date : " + selectedDate);
                    getDocListFromFirebase(selectedDate);

//                    Intent intent = new Intent(FacultyDashboard.this, FacultyAcademicActivity.class);
//                    Constant.MODULE_TYPE = "Academic";
//                    intent.putExtra("selectedDate", selectedDate);
//                    startActivity(intent);
                }
            }, mYear, mMonth, mDay);
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
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
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}