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
import com.mtx.mobile.employee.utils.Constant;

import java.util.Calendar;

public class FacultyDashboard extends AppCompatActivity {

    private LinearLayout llHome, llAcademic, llPlacement, llCulturalActivities;
    private String email;
    private TextView user_name;
    public int mMonth, mYear, mDay, mHour, mMin, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_dashboard);
        Intent intent = getIntent();
        if (intent !=null) {
            email = intent.getStringExtra("EMAIL");
        }
        initView();
    }

    private void initView() {
        llHome= findViewById(R.id.llHome);
        llAcademic = findViewById(R.id.llAcademic);
        llPlacement = findViewById(R.id.llPlacement);
        llCulturalActivities = findViewById(R.id.llCulturalActivities);
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
                openDateDialogForEscalationSummary();
            }
        });

        llPlacement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultyDashboard.this, FacultyHomeActivity.class);
                Constant.MODULE_TYPE = "Placement";
                startActivity(intent);
            }
        });

        llCulturalActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultyDashboard.this, FacultyHomeActivity.class);
                Constant.MODULE_TYPE = "CulturalActivities";
                startActivity(intent);
            }
        });
    }

    private void openDateDialogForEscalationSummary() {
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
                    Intent intent = new Intent(FacultyDashboard.this, FacultyAcademicActivity.class);
                    Constant.MODULE_TYPE = "Academic";
                    intent.putExtra("selectedDate", selectedDate);
                    startActivity(intent);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
    }
}