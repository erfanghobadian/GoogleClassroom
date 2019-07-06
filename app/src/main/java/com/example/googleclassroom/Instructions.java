package com.example.googleclassroom;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Instructions extends Fragment {

    User user ;
    Class myclass ;
    Assignment assignment ;

    public Instructions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_instructions, container, false);
        ImageView attach = v.findViewById(R.id.attach) ;
        TextView des = v.findViewById(R.id.desass) ;
        TextView due = v.findViewById(R.id.duedate) ;
        user = (User) getArguments().getSerializable("user");
        myclass = (Class) getArguments().getSerializable("myclass") ;
        assignment = (Assignment) getArguments().getSerializable("ass") ;




        setHasOptionsMenu(true);

        System.out.println(Arrays.toString(assignment.attach));

        des.setText(assignment.des);
        due.setText("Due : " + assignment.due.get(Calendar.DAY_OF_MONTH) + "/" + (assignment.due.get(Calendar.MONTH)+ 1) + "/" + assignment.due.get(Calendar.YEAR));
        due.setText(due.getText() + "-" + assignment.due.get(Calendar.HOUR_OF_DAY) + ":" +assignment.due.get(Calendar.MINUTE));
        byte[] imgByte = assignment.attach;
        if (imgByte==null) {
            attach.setVisibility(View.INVISIBLE);
        }
        if (imgByte !=null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            attach.setImageBitmap(bmp);
        }





        return  v ;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.inst_menu , menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId() ;
        if (id == R.id.editass) {
            FullScreenDialogEditAssignment dialog = new FullScreenDialogEditAssignment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("user" , user);
            bundle.putSerializable("myclass" , myclass);
            bundle.putSerializable("ass" , assignment);
            dialog.setArguments(bundle);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            dialog.show(ft, FullScreenDialogEditAssignment.TAG);


        }
        else if (id == R.id.removeass) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Socket s = new Socket(getResources().getString(R.string.ip), 8080);
                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                        String[] a = {"RemoveAss" , myclass.code , assignment.topic.name , assignment.title} ;
                        oos.writeObject(a);
                        oos.flush();

                        oos.close();
                        ois.close();
                        s.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
