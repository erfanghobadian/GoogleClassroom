package com.example.googleclassroom;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;


public class LoginActivity extends AppCompatActivity {
    Button LoginBtn ;
    Button SignUpBtn ;
    EditText username ;
    EditText password ;
    String us ;
    String ps ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginBtn = findViewById(R.id.LB);
        SignUpBtn = findViewById(R.id.SB);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 us = username.getText().toString();
                 ps = password.getText().toString();
                 if (us.isEmpty() || ps.isEmpty()) {
                     Toast.makeText(LoginActivity.this, "Please Fill Username and Password", Toast.LENGTH_LONG).show();
                 }
                 else {
                     LoginCheck sendLogin = new LoginCheck(LoginActivity.this);
                     sendLogin.execute("Login",us, ps);
                     Toast.makeText(LoginActivity.this, "Please Wait ...", Toast.LENGTH_LONG).show();

                 }
            }
        });


    }

}

class LoginCheck extends AsyncTask<String,Void , String> {
    Socket s ;
    ObjectOutputStream Odos ;
    ObjectInputStream ois ;
    DataInputStream dis;
    boolean answer ;
    WeakReference<LoginActivity> activityReference ;

    LoginCheck(LoginActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            s = new Socket("10.0.2.2" , 8080);
            Odos = new ObjectOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            Odos.writeObject(strings);
            Odos.flush();
            answer = dis.readBoolean();




        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        LoginActivity activity = activityReference.get();
        if (activity== null || activity.isFinishing()) return;

        if (answer) {
            Toast.makeText(activity, "Your Logged in Successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
        }

    }
}

