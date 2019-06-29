package com.example.googleclassroom;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mdrawerLayout ;
    private ActionBarDrawerToggle mToggle ;
    private Toolbar mToolbar ;
    private ImageView imageView ;
    private TextView usernameTextView ;
    private User user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (User) getIntent().getSerializableExtra("user");





        // For Toolbar
        mToolbar = findViewById(R.id.nav_action) ;
        setSupportActionBar(mToolbar);


        // For Drawer
        mdrawerLayout = findViewById(R.id.drawerLayout);



        // For Add Buttons to Toolbar
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mdrawerLayout, mToolbar, R.string.nav_open, R.string.nav_close);
        mdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        // For FAB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);


        imageView = headerView.findViewById(R.id.mainavatar);
        byte[] imgByte = user.avatar;
        System.out.println(Arrays.toString(imgByte));
        Bitmap bmp= BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        imageView.setImageBitmap(bmp);



        usernameTextView = headerView.findViewById(R.id.usernameMain);
        usernameTextView.setText(user.username);

        System.out.println(user.username);





    }



    // For Opening the Drawer
    @Override
    public void onBackPressed() {
        if (mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mdrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    // For Menu in Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar , menu);
        return true;
    }


    // For Items Selected in Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id  = item.getItemId();

        if (id == R.id.action_add) {

            final PopupMenu popup = new PopupMenu(this , findViewById(R.id.action_add));

            popup.getMenuInflater().inflate(R.menu.add_menu, popup.getMenu());


            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int Iid = item.getItemId();
                    if (Iid == R.id.Create_Class) {
                        FullScreenDialogCreateClass dialog = new FullScreenDialogCreateClass();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        dialog.show(ft, FullScreenDialogCreateClass.TAG);
                        popup.dismiss();
                    }
                    else if (Iid == R.id.Join_Class) {
                        FullScreenDialogJoinClass dialog = new FullScreenDialogJoinClass();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        dialog.show(ft, FullScreenDialogJoinClass.TAG);
                        popup.dismiss();

                    }
                    return true;
                }
            });


            popup.show();

//            Toast.makeText(this, "Add Button", Toast.LENGTH_SHORT).show();

        }
//        else if (id == R.id.) {}

        return super.onOptionsItemSelected(item);
    }


    // For Items Selected in Drawer
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_tools) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        mdrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }




}
