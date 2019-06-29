package com.example.googleclassroom;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class People extends Fragment {

    boolean isteacher = false ;

    User user ;
    Class myclass ;


    public People() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_people, container, false);

        user = (User) getArguments().getSerializable("user");
        myclass = (Class) getArguments().getSerializable("myclass") ;

        for (User usr : myclass.teachers) {
            if (usr.username.equals(user.username))
                isteacher = true ;
        }

        setHasOptionsMenu(true);





        return v ;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.people_menu , menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.pepaction_refresh) {}
        else if (id==R.id.pepaction_about) {}
        else if (id==R.id.pepaction_noti) {}
        return super.onOptionsItemSelected(item);
    }

}
