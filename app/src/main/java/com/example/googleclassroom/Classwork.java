package com.example.googleclassroom;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Classwork extends Fragment {


    boolean isteacher = false ;

    User user ;
    Class myclass ;
    public Classwork() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_classwork, container, false) ;


        user = (User) getArguments().getSerializable("user");
        myclass = (Class) getArguments().getSerializable("myclass") ;

        for (User usr : myclass.teachers) {
            if (usr.username.equals(user.username))
                isteacher = true ;
        }




        System.out.println(myclass.code);
        setHasOptionsMenu(true);




//         For FAB
        FloatingActionButton fab = v.findViewById(R.id.fab);

        if (!isteacher) {
            fab.hide();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.classwork_menu , menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {


        if (isteacher) {
            menu.findItem(R.id.clsaction_info).setVisible(false);
        }else {
            menu.findItem(R.id.clsaction_setting).setVisible(false) ;
        }
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.clsaction_setting) {}
        else if (id==R.id.clsaction_info) {}
        else if (id==R.id.clsaction_refresh) {}
        else if (id==R.id.clsaction_about) {}
        else if (id==R.id.clsaction_noti) {}
        return super.onOptionsItemSelected(item);
    }
}
