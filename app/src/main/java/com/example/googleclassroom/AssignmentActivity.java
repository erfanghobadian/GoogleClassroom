package com.example.googleclassroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

public class AssignmentActivity extends AppCompatActivity {

    Class myclass ;
    User user ;
    Assignment assignment ;
    final Fragment fragment1 = new Instructions();
    final Fragment fragment2 = new StudentWork();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        myclass = (Class) getIntent().getSerializableExtra("class");
        user = (User) getIntent().getSerializableExtra("user");
        assignment = (Assignment) getIntent().getSerializableExtra("ass");

        Toolbar toolbar = findViewById(R.id.assignmentacction) ;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putSerializable("myclass" , myclass);
        bundle.putSerializable("ass" , assignment);
        fragment1.setArguments(bundle);
        fragment2.setArguments(bundle);


        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();
        tabLayout.addTab(tabLayout.newTab().setText("Instructions"));
        tabLayout.addTab(tabLayout.newTab().setText("Student Work"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position==0) {
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                }
                else if (position==1) {
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("android:support:fragments", null);
    }
}
