package com.example.googleclassroom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mdrawerLayout ;
    private ActionBarDrawerToggle mToggle ;
    private Toolbar mToolbar ;
    private ImageView imageView ;
    private TextView usernameTextView ;
    User user ;

    RecyclerView rv ;





    void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(user , this);
        rv.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (User) getIntent().getSerializableExtra("user");

        rv= findViewById(R.id.my_recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        initializeAdapter();


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
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user" , user);
                        dialog.setArguments(bundle);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        dialog.show(ft, FullScreenDialogCreateClass.TAG);
                        popup.dismiss();
                    }
                    else if (Iid == R.id.Join_Class) {
                        FullScreenDialogJoinClass dialog = new FullScreenDialogJoinClass();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user" , user);
                        dialog.setArguments(bundle);
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
        else if (id == R.id.action_refresh) {
            Refresh ARefresh = new Refresh(MainActivity.this);
            ARefresh.execute("Refresh" , user.username , user.password);
        }
        else if (id == R.id.action_about) {
            FullScreenAboutUs dialog = new FullScreenAboutUs();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            dialog.show(ft, FullScreenAboutUs.TAG);
        }

        return super.onOptionsItemSelected(item);
    }


    // For Items Selected in Drawer
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_refresh) {
            Refresh NRefresh = new Refresh(MainActivity.this);
            NRefresh.execute("Refresh" , user.username , user.password);

        }

        else if (id == R.id.nav_about) {
            FullScreenAboutUs dialog = new FullScreenAboutUs();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            dialog.show(ft, FullScreenAboutUs.TAG);
        }


        mdrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }




}


class Refresh extends AsyncTask<Object,Void , String> {
    Socket s ;
    ObjectOutputStream oos ;
    ObjectInputStream ois ;
    WeakReference<MainActivity> activityReference ;
    User user ;
    Refresh(MainActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(Object... input) {
        try {
            s = new Socket(activityReference.get().getResources().getString(R.string.ip) , 8080);
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            String[] strings = {(String) input[0] , (String)input[1] , (String)input[2]};
            oos.writeObject(strings);
            oos.flush();
            user = (User)ois.readObject();


            oos.close();
            ois.close();
            s.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        MainActivity activity = activityReference.get();
        if (activity== null || activity.isFinishing()) return;
        activity.user = user ;
        activity.initializeAdapter();

    }
}




