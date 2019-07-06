package com.example.googleclassroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentActivity extends AppCompatActivity {

    Class myclass ;
    User student ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        myclass = (Class) getIntent().getSerializableExtra("stclass");
        student = (User) getIntent().getSerializableExtra("user");

        TextView StudentName = findViewById(R.id.namest);
        ImageView avatar = findViewById(R.id.picstudent);

        byte[] imgByte = student.avatar ;
        Bitmap bmp= BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        avatar.setImageBitmap(bmp);
        StudentName.setText(student.username);


        Toolbar toolbar = findViewById(R.id.studentaction) ;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu , menu);
        return true ;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
