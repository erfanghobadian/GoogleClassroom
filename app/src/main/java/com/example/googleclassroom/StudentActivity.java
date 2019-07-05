package com.example.googleclassroom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StudentActivity extends AppCompatActivity {

    Class myclass ;
    User student ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        myclass = (Class) getIntent().getSerializableExtra("stclass");
        student = (User) getIntent().getSerializableExtra("user");


    }
}
