package com.example.googleclassroom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutClass extends AppCompatActivity {

    Class myclass ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_class);
        myclass = (Class) getIntent().getSerializableExtra("myclass");
        TextView className = findViewById(R.id.ClassNameinfo);
        TextView classdsc = findViewById(R.id.dscrp);
        TextView classroom = findViewById(R.id.croominfo);

        className.setText(myclass.name);
        classdsc.setText(myclass.des);
        classroom.setText(myclass.room);



    }
}
