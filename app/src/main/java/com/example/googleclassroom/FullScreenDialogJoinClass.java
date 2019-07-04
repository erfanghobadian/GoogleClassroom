package com.example.googleclassroom;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;

public class FullScreenDialogJoinClass extends DialogFragment {

    public static String TAG = "FullScreenDialogJoinClass";
    User user ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        user = (User) getArguments().getSerializable("user");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fullscreenjoinclass, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setTitle("Join Class");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        final EditText ClassCode = view.findViewById(R.id.ClassCode) ;
        ClassCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (ClassCode.length()==0)
                    ClassCode.setError("Class Code Required");
                    
            }
        });


        toolbar.inflateMenu(R.menu.join_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.joinMenu) {
                    if (ClassCode.length()!=0) {
                        JoinClass join = new JoinClass(getActivity() , user);
                        join.execute("JoinClass", user.username, user.password, ClassCode.getText().toString());
                        dismiss();
                    }else
                        Toast.makeText(getActivity(), "Class Code Can not Be Empty", Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });
        
        
        


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);

        }
    }
}


class JoinClass extends AsyncTask<Object,Void , String> {
    Socket s ;
    ObjectOutputStream oos ;
    ObjectInputStream ois ;
    DataInputStream dis;
    Boolean answer ;
    Class myclass ;
    User user ;
    WeakReference<FragmentActivity> activityReference ;

    JoinClass(FragmentActivity context , User user) {
        activityReference = new WeakReference<>(context);
         this.user = user ;
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
            oos.writeObject(input[3]);
            oos.flush();
            answer = ois.readBoolean();
            if (answer) {
             myclass = (Class)ois.readObject() ;
            }





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

        FragmentActivity activity = activityReference.get();
        if (activity== null || activity.isFinishing()) return;

        if (answer) {


            boolean check = false ;
            for (User usr : myclass.teachers) {
                if (usr.username.equals(user.username))
                    check = true ;
            }

            if (check) {
                Toast.makeText(activity, "You Are Teacher in This Class", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(activity, ClassActivity.class);
                intent.putExtra("myclass", myclass);
                intent.putExtra("user", user);
                activity.startActivity(intent);
            }


        }else {
            Toast.makeText(activity, "Class Code is Not Correct", Toast.LENGTH_SHORT).show();
        }

    }
}
