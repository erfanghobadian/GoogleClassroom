package com.example.googleclassroom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


public class ClassActivity extends AppCompatActivity{





    private DrawerLayout mdrawerLayout ;
    private ActionBarDrawerToggle mToggle ;
    private Toolbar mToolbar ;
    private ImageView imageView ;
    private TextView usernameTextView ;

    Class myclass ;
    User user ;

    final Fragment fragment1 = new Classwork();
    final Fragment fragment2 = new People();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        myclass = (Class) getIntent().getSerializableExtra("myclass");
        user = (User) getIntent().getSerializableExtra("user") ;






        mToolbar = findViewById(R.id.class_action) ;
        mToolbar.setTitle(myclass.name);

        setSupportActionBar(mToolbar);


        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putSerializable("myclass" , myclass);
        fragment1.setArguments(bundle);
        fragment2.setArguments(bundle);






        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_classwork:

                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        return true;

                    case R.id.action_people:
                        fm.beginTransaction().hide(active).show(fragment2).commit();
                        active = fragment2;
                        return true;

                }
                return false;
            }
        });


    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.actionbar , menu);
//        return true;
//    }




}
