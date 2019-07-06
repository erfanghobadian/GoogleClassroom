package com.example.googleclassroom;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentWork extends Fragment {

    User user ;
    Class myclass ;
    Assignment assignment ;
    RecyclerView rvstudent;

    public StudentWork() {
        // Required empty public constructor
    }

    void intadaper() {

        MarkAdapter adapter = new MarkAdapter(myclass, user, this , assignment);
        rvstudent.setAdapter(adapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_student_work, container, false);
        user = (User) getArguments().getSerializable("user");
        myclass = (Class) getArguments().getSerializable("myclass") ;
        assignment = (Assignment) getArguments().getSerializable("ass") ;

        rvstudent = v.findViewById(R.id.studentassrv);
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        rvstudent.setLayoutManager(llm2);
        intadaper();


        return  v ;
    }

}
