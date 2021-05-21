package com.mtx.mobile.employee.faculty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.mtx.mobile.employee.R;
import com.mtx.mobile.employee.model.EventModel;
import com.mtx.mobile.employee.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FacultyHomeActivity extends AppCompatActivity {
    private RecyclerView rvEvent;
    private List<EventModel> modelList = new ArrayList<>();
    private Toolbar toolbar;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);
        toolbar = findViewById(R.id.toolBar);
        selectedDate = getIntent().getStringExtra("selectedDate");
        initView();
    }

    private void initView() {
        rvEvent = findViewById(R.id.rvEvent);
        rvEvent.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        modelList.clear();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        if (Constant.MODULE_TYPE.equals("HOME")) {
            toolbar.setTitle("Event:  " + formatter.format(new Date()));
            modelList = getEventList();
        } else if (Constant.MODULE_TYPE.equals("Academic")) {
            toolbar.setTitle("Academic:  " + selectedDate);
            modelList = getAcademicList();
        } else if (Constant.MODULE_TYPE.equals("Placement")) {
            toolbar.setTitle("Placement:  " + formatter.format(new Date()));
            modelList = getPlacementList();
        } else if (Constant.MODULE_TYPE.equals("CulturalActivities")) {
            toolbar.setTitle("CulturalActivities:  " + formatter.format(new Date()));
            modelList = getCulturalActivities();
        }
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), modelList);
        rvEvent.setAdapter(eventAdapter);
    }

    private List<EventModel> getCulturalActivities() {
        List<EventModel> list = new ArrayList<>();
        list.add(new EventModel("Paper Presentation"));
        list.add(new EventModel("Inter college level"));
        list.add(new EventModel("Singing"));
        list.add(new EventModel("State level"));
        return list;
    }

    private List<EventModel> getPlacementList() {
        List<EventModel> list = new ArrayList<>();
        list.add(new EventModel("Placement Updates"));
        list.add(new EventModel("Training Updates"));

        return list;
    }

    private List<EventModel> getAcademicList() {
        List<EventModel> list = new ArrayList<>();
        list.add(new EventModel("Department"));
        list.add(new EventModel("Semesters"));
        list.add(new EventModel("Internal Marks"));
        list.add(new EventModel("Attendance"));
        return list;
    }

    private List<EventModel> getEventList() {
        List<EventModel> list = new ArrayList<>();
        list.add(new EventModel("Embedded Computing Systems"));
        list.add(new EventModel("Advanced DBMS"));
        list.add(new EventModel("Advanced Computer Architectures"));
        list.add(new EventModel(" Networks Laboratory"));
        list.add(new EventModel("Web Programming Laboratory"));

        list.add(new EventModel("Digital Signal Processing"));
        list.add(new EventModel(" Java and J2EE"));
        return list;
    }
}