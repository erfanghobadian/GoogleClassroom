package com.example.googleclassroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.Calendar;

import static java.security.AccessController.getContext;

public class AssignmentStudentActivity extends AppCompatActivity {

    Class myclass ;
    User user ;
    Assignment assignment ;
    byte[] attachByte ;
    ImageView img ;
    HomeWork homework ;
    EditText HomeWorkText ;
    TextView mark ;
    TextView due;
    TextView des ;
    Button sendHomeWork ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_student);
        myclass = (Class) getIntent().getSerializableExtra("class");
        user = (User) getIntent().getSerializableExtra("user");
        assignment = (Assignment) getIntent().getSerializableExtra("ass");


        Toolbar toolbar = findViewById(R.id.assignmentstacction) ;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

         img = findViewById(R.id.attachst);
        Button Chooseimg = findViewById(R.id.image_assst) ;
        HomeWorkText = findViewById(R.id.texthomwwork) ;
        sendHomeWork = findViewById(R.id.sendhomework) ;
        mark = findViewById(R.id.mark) ;

        due = findViewById(R.id.duedatest) ;
        des = findViewById(R.id.desassst) ;


        init();


        Chooseimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AssignmentStudentActivity.this);

            }
        });

        sendHomeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assignment.due.getTimeInMillis() > System.currentTimeMillis()) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Socket s = new Socket(getResources().getString(R.string.ip), 8080);
                                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                                String[] a = {"AddHomeWork", myclass.code, user.username, user.password, assignment.topic.name, assignment.title, HomeWorkText.getText().toString()};
                                oos.writeObject(a);
                                oos.flush();
                                oos.writeObject(attachByte);
                                oos.flush();


                                oos.close();
                                ois.close();
                                s.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    finish();
                }
                else {
                    Toast.makeText(AssignmentStudentActivity.this, "Not Time", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void init() {
        des.setText(assignment.des);
        due.setText("Due : " + assignment.due.get(Calendar.DAY_OF_MONTH) + "/" + (assignment.due.get(Calendar.MONTH)+ 1) + "/" + assignment.due.get(Calendar.YEAR));
        due.setText(due.getText() + "-" + assignment.due.get(Calendar.HOUR_OF_DAY) + ":" +assignment.due.get(Calendar.MINUTE));



        if (assignment.works.containsKey(user.username)) {
            mark.setText("Your Current Point is : " + assignment.works.get(user.username).score);
            HomeWorkText.setText(assignment.works.get(user.username).text);
            sendHomeWork.setText("Change");
            if (assignment.works.get(user.username).attach !=null){
                Bitmap bmp = BitmapFactory.decodeByteArray(assignment.works.get(user.username).attach, 0, assignment.works.get(user.username).attach.length);
                img.setImageBitmap(bmp);
            }


        }
        else {
            mark.setText("You Have Not Send Your HomeWork Yet");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId() ;
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        if (id == R.id.student_refresh) {
            RefreshSTAss ref = new RefreshSTAss(this) ;
            ref.execute("RefreshSTASS" , user.username , user.password , myclass.code , assignment.topic.name ,assignment.title) ;
        }
        else if (id == R.id.student_noti) {

        }
        else if (id == R.id.student_about) {
            FullScreenAboutUs dialog = new FullScreenAboutUs();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            dialog.show(ft, FullScreenAboutUs.TAG);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu , menu);
        return true ;
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
                        img.setImageBitmap(selectedImage);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 1, baos);
                        attachByte = baos.toByteArray();
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
                                    img.setImageBitmap(bitmap);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 1, baos);
                                    attachByte = baos.toByteArray();

                                }catch (Exception e){e.printStackTrace();}

                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }





}



class RefreshSTAss extends AsyncTask<String,Void , String> {
    Socket s ;
    ObjectOutputStream oos ;
    ObjectInputStream ois ;
    WeakReference<AssignmentStudentActivity> activityReference ;
    User user ;
    Class myclass ;
    Assignment ass ;
    RefreshSTAss(AssignmentStudentActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... input) {
        try {
            s = new Socket(activityReference.get().getResources().getString(R.string.ip) , 8080);
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            oos.writeObject(input);
            oos.flush();
            user = (User)ois.readObject();
            myclass = (Class) ois.readObject();
            ass = (Assignment) ois.readObject();


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

        AssignmentStudentActivity activity = activityReference.get();
        if (activity== null || activity.isFinishing()) return;
        activity.user = user ;
        activity.myclass = myclass ;
        activity.assignment  = ass ;
        activity.init();

    }
}