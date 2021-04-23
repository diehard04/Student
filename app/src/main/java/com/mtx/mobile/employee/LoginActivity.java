package com.mtx.mobile.employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.mtx.mobile.employee.db.DatabaseHelper;
import com.mtx.mobile.employee.faculty.FacultyDashboard;
import com.mtx.mobile.employee.utils.InputValidation;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;
    private AppCompatEditText textInputEditTextEmail;
    private AppCompatEditText textInputEditTextPassword;
    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private TextView userType;
    private RadioButton selectFaculty;
    private String studentEmail = "Ramesh@gmail.com";
    private String studentPwd = "123456";
    private String facultyEmail = "Rahul@gmail.com";
    private String facultyPwd = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    public void selectProfile(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.selectFaculty:
                if (checked) {
                    userType.setText("Faculty");
                    Toast.makeText(getBaseContext(), "Your have selected Faculty profile", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.selectStudent:
                if (checked) {
                    userType.setText("Student");
                    Toast.makeText(getBaseContext(), "Your have selected Student profile", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        userType = findViewById(R.id.userType);
        selectFaculty = findViewById(R.id.selectFaculty);
        userType.setText("Faculty");
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                moveToDashBoard();
                //verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    private void moveToDashBoard() {
        if (textInputEditTextEmail.getText().toString().trim().isEmpty()) {
            textInputEditTextEmail.setError("Please enter Email");
            return;
        } else if (textInputEditTextPassword.getText().toString().trim().isEmpty()) {
            textInputEditTextPassword.setError("Please enter Password");
            return;
        } else {
            textInputEditTextEmail.setError(null);
            textInputEditTextPassword.setError(null);
            Intent intent =new Intent(this, FacultyDashboard.class);
            intent.putExtra("email", textInputEditTextEmail.getText().toString().trim());
            startActivity(intent);
            finish();
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, getString(R.string.error_message_email))) {
            return;
        }

        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {


            Intent accountsIntent = new Intent(activity, UsersListActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);


        } else {
            // Snack Bar to show success message that record is wrong
            //Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}