package com.example.googleclassroom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;

public class RegisterActivity extends AppCompatActivity {
    ImageView imageView;
    Button ChooseImageBtn ;
    EditText UsernameText;
    EditText PasswordText;
    Button RegisterBtn ;
    Boolean usernameL = false;
    Boolean username = false;
    Boolean password = false;
    byte [] imgbyte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        UsernameText = findViewById(R.id.usernameR);
        PasswordText = findViewById(R.id.passwordR);
        imageView = findViewById(R.id.imageView);
        ChooseImageBtn = findViewById(R.id.button);
        RegisterBtn = findViewById(R.id.RegisterBTN);






        // Check Username is Empty or NOT
        UsernameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (UsernameText.length() ==0) {
                    UsernameText.setError("Empty");
                    usernameL = true;
                }
                else
                    UsernameText.setError(null);
                    usernameL = false ;
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Socket s = new Socket(getResources().getString(R.string.ip), 8080);
                            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                            String[] a = {"UserNameCheck" , UsernameText.getText().toString()};
                            oos.writeObject(a);
                            oos.flush();

                            if (!ois.readBoolean()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UsernameText.setError("Username is Duplicate");
                                        username = true;
                                    }
                                });
                            }else {
                                username = false;
                            }

                            oos.close();
                            ois.close();
                            s.close();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });


        // Check Password is Empty or NOT
        PasswordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (PasswordText.length() ==0) {
                    PasswordText.setError("Empty");
                    password =true ;
                }

                else if (PasswordText.length() < 6) {
                    PasswordText.setError("Password Must Be more than 5 Char");
                    password = true ;
                }
                else {
                    PasswordText.setError(null);
                    password = false ;
                }

            }
        });




        ChooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(RegisterActivity.this);

            }
        });


        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!username && !password && !usernameL) {
                    String us = UsernameText.getText().toString();
                    String ps = PasswordText.getText().toString();
                    SendRegister sendRegister = new SendRegister(RegisterActivity.this);
                    if (imageView.getDrawable() !=null) {
                        Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        imgbyte = baos.toByteArray();
                    }
                    else
                        imgbyte = new byte[1];
                    sendRegister.execute(imgbyte,"Register", us, ps);
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Please Check The Errors", Toast.LENGTH_LONG).show();
                }

            }
        });




    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                                    imageView.setImageBitmap(bitmap);

                                }catch (Exception e){e.printStackTrace();}

                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }


    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }


}


// Class For BackGround Socket Login
class SendRegister extends AsyncTask<Object,Void , String> {
    Socket s ;
    ObjectOutputStream oos ;
    ObjectInputStream ois ;
    DataInputStream dis;
    Boolean answer ;
    User user;
    WeakReference<RegisterActivity> activityReference ;

    SendRegister(RegisterActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(Object... input) {
        try {
            s = new Socket(activityReference.get().getResources().getString(R.string.ip) , 8080);
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            String[] strings = {(String)input[1] , (String)input[2] , (String)input[3]} ;
            byte[] imgByte = (byte[])input[0];
            oos.writeObject(strings);
            oos.flush();
            oos.writeObject(imgByte);
            oos.flush();
            answer= ois.readBoolean();
            if (answer) {
                user = (User)ois.readObject();
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
        RegisterActivity activity = activityReference.get();
        if (activity== null || activity.isFinishing()) return;

        if (answer) {
            Toast.makeText(activity, "Your Logged in Successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("user" , user);
            activity.startActivity(intent);
        }else {

        }

    }
}

