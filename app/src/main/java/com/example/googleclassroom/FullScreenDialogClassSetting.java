package com.example.googleclassroom;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;

public class FullScreenDialogClassSetting extends DialogFragment {

    public static String TAG = "FullScreenDialogClassSetting";
    User user ;
    Class myclass ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        user = (User) getArguments().getSerializable("user");
        myclass = (Class) getArguments().getSerializable("myclass") ;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fullscreenclasssetting, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setTitle("Class Setting");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        final EditText ClassName = view.findViewById(R.id.settingclsname) ;
        final EditText ClassDes  = view.findViewById(R.id.settingdscrp);
        final EditText ClassRoom = view.findViewById(R.id.settingRoom) ;
        final TextView ClassCode = view.findViewById(R.id.settingCode) ;


        ClassName.setText(myclass.name);
        ClassDes.setText(myclass.des);
        ClassRoom.setText(myclass.room);
        ClassCode.setText("Class Code : " + myclass.code);
        ClassCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(myclass.code);
                Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });









        toolbar.inflateMenu(R.menu.save_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.saveMenu) {
                    EditClass sendData = new EditClass(FullScreenDialogClassSetting.this , user );
                    sendData.execute("EditClass" , myclass.code , ClassName.getText().toString() , ClassDes.getText().toString() , ClassRoom.getText().toString());
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


class EditClass extends AsyncTask<String , Void , String>{
    Socket s ;
    ObjectOutputStream oos ;
    ObjectInputStream ois ;
    DataInputStream dis;
    Class myclass ;
    User user ;
    WeakReference<FullScreenDialogClassSetting> activityReference ;

    EditClass(FullScreenDialogClassSetting context , User user ) {
        activityReference = new WeakReference<>(context);
        this.user = user ;

    }

    @Override
    protected String doInBackground(String... input) {
        try {
            s = new Socket(activityReference.get().getResources().getString(R.string.ip) , 8080);
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            oos.writeObject(input);
            oos.flush();
            oos.flush();
            myclass = (Class) ois.readObject() ;

            System.out.println(myclass.name);


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
        FullScreenDialogClassSetting activity = activityReference.get();
        activity.myclass = myclass ;
        activity.dismiss();

    }
}


