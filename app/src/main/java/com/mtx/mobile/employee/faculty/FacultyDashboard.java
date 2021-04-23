package com.mtx.mobile.employee.faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtx.mobile.employee.R;
import com.mtx.mobile.employee.utils.Constant;

public class FacultyDashboard extends AppCompatActivity {

    private LinearLayout llHome, llAcademic, llPlacement, llCulturalActivities;
    private String email;
    private TextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_dashboard);
        Intent intent = getIntent();
        if (intent !=null) {
            email = intent.getStringExtra("email");
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
                Intent intent = new Intent(FacultyDashboard.this, FacultyHomeActivity.class);
                Constant.MODULE_TYPE = "Academic";
                startActivity(intent);
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
}