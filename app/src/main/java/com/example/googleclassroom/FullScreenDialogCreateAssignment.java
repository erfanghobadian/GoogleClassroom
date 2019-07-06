package com.example.googleclassroom;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;


import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class FullScreenDialogCreateAssignment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {

    byte[] attachByte ;
    String TopicName ;
    int year ,month , day ;
    int hour , min ;

    EditText DateTimeText ;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;this.month = month ; this.day = dayOfMonth ;
        DateTimeText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay ; this.min = minute ;
        DateTimeText.setText(DateTimeText.getText() + "-" + hourOfDay + ":" + minute);
    }

    public static String TAG = "FullScreenDialogCreateAssignment";
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
        View view = inflater.inflate(R.layout.fullscreencreateassignment, container, false);
        DateTimeText = view.findViewById(R.id.DateTime) ;
        final EditText Points = view.findViewById(R.id.Points) ;
        final EditText Des = view.findViewById(R.id.AssDes) ;

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setTitle("Create Assignment");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        final EditText AssTitle = view.findViewById(R.id.AssTitle) ;



        toolbar.inflateMenu(R.menu.assignment_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.saveassignment) {
                    System.out.println(Arrays.toString(attachByte));
                    if (AssTitle.length()!=0 && DateTimeText.length()!=0 && Points.length()!=0) {
                        AddAss send = new AddAss(getActivity(), user, attachByte);
                        send.execute("CreateAssignment", myclass.code, TopicName, AssTitle.getText().toString(), Integer.toString(year), Integer.toString(month), Integer.toString(day), Integer.toString(hour), Integer.toString(min), Des.getText().toString(), Points.getText().toString());
                        dismiss();
                    }
                    else {
                        Toast.makeText(getContext(), "Fields are Empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (id == R.id.attachassignment) {
                    selectImage(getContext());

                }
                return false;
            }
        });




        DateTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                DatePickerDialog Date = new DatePickerDialog(getActivity() , FullScreenDialogCreateAssignment.this,year,month,day);
                TimePickerDialog Time = new TimePickerDialog(getActivity(), FullScreenDialogCreateAssignment.this, hour, minute,
                        DateFormat.is24HourFormat(getActivity()));
                Time.show();
                Date.show();
            }
        });



        Spinner spinner = view.findViewById(R.id.topic_spinner);

        ArrayList<String> topicsName  = new ArrayList<>() ;
        for (Topic t : myclass.topics) {
            topicsName.add(t.name) ;
        }
        spinner.setOnItemSelectedListener(this);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, topicsName);
        spinner.setAdapter(dataAdapter);








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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        TopicName = item ;
        System.out.println(item);


    }
    public void onNothingSelected(AdapterView<?> arg0) {
        TopicName = "No Topic" ;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 1, baos);
                        attachByte = baos.toByteArray();                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
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


class AddAss extends AsyncTask<String ,Void , String> {
    Socket s ;
    ObjectOutputStream oos ;
    ObjectInputStream ois ;
    byte[] attach ;
    Class myclass ;
    User user ;
    WeakReference<FragmentActivity> activityReference ;

    AddAss(FragmentActivity context , User user , byte[] attach) {
        activityReference = new WeakReference<>(context);
        this.user = user ;
        this.attach =attach ;
    }

    @Override
    protected String doInBackground(String... input) {
        try {
            s = new Socket(activityReference.get().getResources().getString(R.string.ip) , 8080);
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            oos.writeObject(input);
            oos.flush();

            oos.writeObject(attach);
            oos.flush();



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


    }
}


