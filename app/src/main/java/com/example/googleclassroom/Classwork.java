package com.example.googleclassroom;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
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



public class Classwork extends Fragment {

    boolean isteacher = false ;
    RecyclerView rv ;
    User user ;
    Class myclass ;
    public Classwork() {

    }

    void intadaper() {
        CLSAdapter adapter = new CLSAdapter(myclass , user , this) ;
        rv.setAdapter(adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_classwork, container, false) ;
        user = (User) getArguments().getSerializable("user");
        myclass = (Class) getArguments().getSerializable("myclass") ;
        for (User usr : myclass.teachers) {
            if (usr.username.equals(user.username))
                isteacher = true ;
        }
        System.out.println(myclass.code);
        setHasOptionsMenu(true);


        rv = v.findViewById(R.id.clwrv_parent) ;
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        intadaper();


        // For FAB
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
                            FullScreenDialogCreateAssignment dialog = new FullScreenDialogCreateAssignment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user" , user);
                            bundle.putSerializable("myclass" , myclass);
                            dialog.setArguments(bundle);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            dialog.show(ft, FullScreenDialogCreateAssignment.TAG);
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
                                                    Socket s = new Socket(getResources().getString(R.string.ip), 8080);
                                                    ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                                                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                                                    System.out.println("Hello");
                                                    String[] a = {"CreateTopic", myclass.code ,  topicname.getText().toString()};
                                                    System.out.println(a[1] +" "+ a[2]);
                                                    oos.writeObject(a);
                                                    oos.flush();



                                                    oos.close();
                                                    ois.close();
                                                    s.close();
                                                    RefreshCLW refreshCLW = new RefreshCLW(Classwork.this);
                                                    refreshCLW.execute("RefreshCLW", user.username, user.password, myclass.code);

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

        if (id==R.id.clsaction_setting) {
            FullScreenDialogClassSetting dialog = new FullScreenDialogClassSetting();
            Bundle bundle = new Bundle();
            bundle.putSerializable("user" , user);
            bundle.putSerializable("myclass" , myclass);
            dialog.setArguments(bundle);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            dialog.show(ft, FullScreenDialogClassSetting.TAG);
        }
        else if (id==R.id.clsaction_info) {
            Intent intent2 = new Intent(getActivity() , AboutClass.class);
            intent2.putExtra("myclass" , myclass) ;
            startActivity(intent2);
        }
        else if (id==R.id.clsaction_refresh) {
            RefreshCLW refreshCLW = new RefreshCLW(Classwork.this);
            refreshCLW.execute("RefreshCLW", user.username, user.password, myclass.code);
        }
        else if (id==R.id.clsaction_about) {
            FullScreenAboutUs dialog = new FullScreenAboutUs();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            dialog.show(ft, FullScreenAboutUs.TAG);
        }
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
            s = new Socket(activityReference.get().getResources().getString(R.string.ip) , 8080);
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




