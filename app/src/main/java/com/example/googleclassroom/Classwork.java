package com.example.googleclassroom;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;


/**
 * A simple {@link Fragment} subclass.
 */
public class Classwork extends Fragment {


    boolean isteacher = false ;

    RecyclerView rv ;

    User user ;
    Class myclass ;
    public Classwork() {
        // Required empty public constructor
    }

    void intadaper() {
        CLSAdapter adapter = new CLSAdapter(myclass , user , this) ;
        rv.setAdapter(adapter);
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


        myclass.topics.get(0).assignments.add(new Assignment());
        myclass.topics.get(0).assignments.add(new Assignment());
        myclass.topics.get(0).assignments.add(new Assignment());






        rv = v.findViewById(R.id.clwrv_parent) ;
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        intadaper();


//         For FAB
        FloatingActionButton fab = v.findViewById(R.id.fab);
        if (!isteacher) {
            fab.hide();
        }
        final PopupMenu popup = new PopupMenu(getContext() , v.findViewById(R.id.fab));
        popup.getMenuInflater().inflate(R.menu.fab_menu, popup.getMenu());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int Iid = item.getItemId();
                        if (Iid == R.id.Create_Ass) {

                            popup.dismiss();
                        }
                        else if (Iid == R.id.Create_Topic) {
                            popup.dismiss();


                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            LayoutInflater inflater = requireActivity().getLayoutInflater();
                            View v = inflater.inflate(R.layout.dialog_create_topic, null);
                            final EditText topicname =  v.findViewById(R.id.ctopic_name);
                            builder.setView(v).setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (topicname.length()!=0) {
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                super.run();
                                                try {
                                                    Socket s = new Socket("10.0.2.2", 8080);
                                                    ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                                                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                                                    String[] a = {"CreateTopic", myclass.code ,  topicname.getText().toString()};
                                                    System.out.println(a[1] +" "+ a[2]);
                                                    oos.writeObject(a);
                                                    oos.flush();

                                                    RefreshCLW refreshCLW = new RefreshCLW(Classwork.this);
                                                    refreshCLW.execute("RefreshCLW", user.username, user.password, myclass.code);

                                                    oos.close();
                                                    ois.close();
                                                    s.close();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                    }
                                    else
                                        Toast.makeText(getContext(), "Topic Name is Empty", Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });


                            AlertDialog dialog = builder.create();
                            dialog.show();



                        }
                        return true;
                    }
                });
                popup.show();

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
        else if (id==R.id.clsaction_refresh) {
            RefreshCLW refreshCLW = new RefreshCLW(Classwork.this);
            refreshCLW.execute("RefreshCLW", user.username, user.password, myclass.code);
        }
        else if (id==R.id.clsaction_about) {}
        else if (id==R.id.clsaction_noti) {}
        return super.onOptionsItemSelected(item);
    }


}

class RefreshCLW extends AsyncTask <String , Void , String> {
    Socket s ;
    ObjectOutputStream oos ;
    ObjectInputStream ois ;
    WeakReference<Classwork> activityReference ;
    User user ;
    Class myclass ;
    RefreshCLW(Classwork context) {
        activityReference = new WeakReference<>(context);
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            s = new Socket("10.0.2.2" , 8080);
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            oos.writeObject(strings);
            oos.flush();

            user = (User)ois.readObject();
            myclass = (Class)ois.readObject();



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
        super.onPostExecute(s);
        Classwork activity = activityReference.get();
        activity.user = user ;
        activity.myclass = myclass ;
        activity.intadaper();
    }
}




