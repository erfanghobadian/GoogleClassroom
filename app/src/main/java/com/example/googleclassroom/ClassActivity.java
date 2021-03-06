package com.example.googleclassroom;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;


public class ClassActivity extends AppCompatActivity{

    private Toolbar mToolbar ;
    boolean isteacher = false ;
    Class myclass ;
    User user ;
    final Fragment fragment1 = new Classwork();
    final Fragment fragment2 = new People();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_class);
        myclass = (Class) getIntent().getSerializableExtra("myclass");
        user = (User) getIntent().getSerializableExtra("user") ;


        for (User usr : myclass.teachers) {
            if (usr.username.equals(user.username))
                isteacher = true ;
        }


        mToolbar = findViewById(R.id.class_action) ;
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
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("android:support:fragments", null);
    }
}
