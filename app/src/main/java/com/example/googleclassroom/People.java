package com.example.googleclassroom;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;


/**
 * A simple {@link Fragment} subclass.
 */
public class People extends Fragment {


    boolean isteacher = false;

    User user;
    Class myclass;
    RecyclerView rvteacher;
    RecyclerView rvstudent;
    ImageButton teacherbtn;
    ImageButton studentbtn;


    public People() {
        // Required empty public constructor
    }

    void intadaper() {
        TCHAdapter adapter = new TCHAdapter(myclass, user, this);
        rvteacher.setAdapter(adapter);
        STUAdapter stuAdapter = new STUAdapter(myclass, user, this);
        rvstudent.setAdapter(stuAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_people, container, false);

        user = (User) getArguments().getSerializable("user");
        myclass = (Class) getArguments().getSerializable("myclass");

        for (User usr : myclass.teachers) {
            if (usr.username.equals(user.username))
                isteacher = true;
        }
        setHasOptionsMenu(true);


        studentbtn = v.findViewById(R.id.studentBTN);
        teacherbtn = v.findViewById(R.id.teacherBTN);


        rvteacher = v.findViewById(R.id.teachrv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvteacher.setLayoutManager(llm);


        rvstudent = v.findViewById(R.id.studentsrv);
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        rvstudent.setLayoutManager(llm2);

        intadaper();


        if (!isteacher) {
            teacherbtn.setVisibility(View.INVISIBLE);
            studentbtn.setVisibility(View.INVISIBLE);
        }


        teacherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View vi = inflater.inflate(R.layout.dialog_add_teacher, null);
                final EditText tusername = vi.findViewById(R.id.cteachername);
                builder.setView(vi).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean tchinclass = false;
                        for (User tc : myclass.teachers) {
                            if (tc.username.equals(tusername.getText().toString())) {
                                tchinclass = true;
                            }
                        }
                        for (User st : myclass.students) {
                            if (st.username.equals(tusername.getText().toString())) {
                                tchinclass = true;
                            }
                        }
                        if (tchinclass) {
                            Toast.makeText(getContext(), "Teacher Already in Class", Toast.LENGTH_SHORT).show();
                        } else {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        Socket s = new Socket(getResources().getString(R.string.ip), 8080);
                                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                                        System.out.println("imHere2");
                                        String[] a = {"AddTeacherToClass", tusername.getText().toString(), myclass.code};
                                        System.out.println(a[0]);
                                        oos.writeObject(a);
                                        oos.flush();
                                        boolean answer = ois.readBoolean();
                                        if (!answer) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    System.out.println("Hey");
                                                    Toast.makeText(getContext(), "Teacher Not Found", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }

                                        RefreshPOE refreshPOE = new RefreshPOE(People.this);
                                        refreshPOE.execute("RefreshCLW", user.username, user.password, myclass.code);


                                        oos.close();
                                        ois.close();
                                        s.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }.start();
                        }


                    }

                }).setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialog,int which){
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
                dialog.show();

        }
    });


        studentbtn.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View vi = inflater.inflate(R.layout.dialog_add_student, null);
        final EditText studentusername = vi.findViewById(R.id.cstudent_username);
        builder.setView(vi).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean stinclass = false;
                for (User tc : myclass.teachers) {
                    if (tc.username.equals(studentusername.getText().toString())) {
                        stinclass = true;
                    }
                }
                for (User st : myclass.students) {
                    if (st.username.equals(studentusername.getText().toString())) {
                        stinclass = true;
                    }
                }
                if (stinclass) {
                    Toast.makeText(getContext(), "Student Already in Class", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Socket s = new Socket(getResources().getString(R.string.ip), 8080);
                                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                                System.out.println("imHere");
                                String[] a = {"AddStudentToClass", studentusername.getText().toString(), myclass.code};
                                System.out.println(a[0]);
                                oos.writeObject(a);
                                oos.flush();
                                boolean answer = ois.readBoolean();
                                if (!answer) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            System.out.println("Hey");
                                            Toast.makeText(getContext(), "Student Not Found", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

                                RefreshPOE refreshPOE = new RefreshPOE(People.this);
                                refreshPOE.execute("RefreshCLW", user.username, user.password, myclass.code);


                                oos.close();
                                ois.close();
                                s.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }.start();
                }
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
    });

        return v ;

}





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.people_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.pepaction_refresh) {
            RefreshPOE refreshPOE = new RefreshPOE(this);
            refreshPOE.execute("RefreshCLW", user.username, user.password, myclass.code);
        } else if (id == R.id.pepaction_about) {
            FullScreenAboutUs dialog = new FullScreenAboutUs();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            dialog.show(ft, FullScreenAboutUs.TAG);
        } else if (id == R.id.pepaction_noti) {
        }
        return super.onOptionsItemSelected(item);
    }


}


class RefreshPOE extends AsyncTask<String, Void, String> {
    Socket s;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    WeakReference<People> activityReference;
    User user;
    Class myclass;

    RefreshPOE(People context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            s = new Socket(activityReference.get().getResources().getString(R.string.ip), 8080);
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            oos.writeObject(strings);
            oos.flush();

            user = (User) ois.readObject();
            myclass = (Class) ois.readObject();


            oos.close();
            ois.close();
            s.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        People activity = activityReference.get();
        activity.user = user;
        activity.myclass = myclass;
        activity.intadaper();
    }
}

