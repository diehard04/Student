package com.mtx.mobile.employee.faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtx.mobile.employee.R;
import com.mtx.mobile.employee.student.StudentAcademicActivity;
import com.mtx.mobile.employee.student.StudentEventsActivity;
import com.mtx.mobile.employee.student.StudentPlacementActivity;
import com.mtx.mobile.employee.utils.Constant;

import java.util.Calendar;

public class FacultyDashboard extends AppCompatActivity {

    private LinearLayout llHome, llAcademic, llPlacement, llEvents;
    private String email;
    private TextView user_name;
    public int mMonth, mYear, mDay, mHour, mMin, id;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_dashboard);
        Intent intent = getIntent();
        if (intent !=null) {
            email = intent.getStringExtra("EMAIL");
            userType = intent.getStringExtra("userType");
        }
        initView();
    }

    private void initView() {
        llHome= findViewById(R.id.llHome);
        llAcademic = findViewById(R.id.llAcademic);
        llPlacement = findViewById(R.id.llPlacement);
        llEvents = findViewById(R.id.llCulturalActivities);
        user_name = findViewById(R.id.user_name);
        user_name.setText(email);
        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultyDashboard.this, FacultyHomeActivity.class);
                Constant.MODULE_TYPE = "HOME";
                startActivity(intent);
            }
        });

        llAcademic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userType.equalsIgnoreCase("Student")) {
                    Intent intent = new Intent(FacultyDashboard.this, StudentAcademicActivity.class);
                    startActivity(intent);
                } else {
                    openDateDialogForEscalationSummary("academic");
                }
            }
        });

        llPlacement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userType.equalsIgnoreCase("Student")) {
                    Intent intent = new Intent(FacultyDashboard.this, StudentPlacementActivity.class);
                    startActivity(intent);
                } else {
                    openDateDialogForEscalationSummary("placement");
                }
            }
        });

        llEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userType.equalsIgnoreCase("Student")) {
                    Intent intent = new Intent(FacultyDashboard.this, StudentEventsActivity.class);
                    startActivity(intent);
                } else {
                    openDateDialogForEscalationSummary("events");
                }
            }
        });
    }

    private void openDateDialogForEscalationSummary(String eventType) {
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
                    if (eventType.equalsIgnoreCase("academic")) {
                        Intent intent = new Intent(FacultyDashboard.this, FacultyAcademicActivity.class);
                        Constant.MODULE_TYPE = "Academic";
                        intent.putExtra("selectedDate", selectedDate);
                        startActivity(intent);

                    } else if (eventType.equalsIgnoreCase("placement")) {
                        Intent intent = new Intent(FacultyDashboard.this, FacultyPlacementActivity.class);
                        Constant.MODULE_TYPE = "Placement";
                        intent.putExtra("selectedDate", selectedDate);
                        startActivity(intent);
                    }
                    else if (eventType.equalsIgnoreCase("events")) {
                        Intent intent = new Intent(FacultyDashboard.this, FacultyEventsActivity.class);
                        Constant.MODULE_TYPE = "Events";
                        intent.putExtra("selectedDate", selectedDate);
                        startActivity(intent);
                    }
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
    }
}