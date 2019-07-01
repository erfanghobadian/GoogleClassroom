package com.example.googleclassroom;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class People extends Fragment {



    boolean isteacher = false ;

    User user ;
    Class myclass ;
    RecyclerView rvteacher ;
    RecyclerView rvstudent ;
    ImageButton teacherbtn ;
    ImageButton studentbtn ;


    void intadaper() {
        TCHAdapter adapter = new TCHAdapter(myclass , user , this) ;
        rvteacher.setAdapter(adapter);
        STUAdapter stuAdapter = new STUAdapter(myclass, user , this);
        rvstudent.setAdapter(stuAdapter);

    }


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


        studentbtn = v.findViewById(R.id.studentBTN);
        teacherbtn = v.findViewById(R.id.teacherBTN);


        rvteacher = v.findViewById(R.id.teachrv) ;
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvteacher.setLayoutManager(llm);


        rvstudent = v.findViewById(R.id.studentsrv) ;
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        rvteacher.setLayoutManager(llm2);

        intadaper();


        teacherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        studentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



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
