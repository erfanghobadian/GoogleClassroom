package com.example.googleclassroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.MarkViewHolder> {


    static class MarkViewHolder extends RecyclerView.ViewHolder {
        TextView StudentName ;
        CardView stcard ;
        ImageView avatar ;
        TextView stpoint ;
        MarkViewHolder(View itemView) {
        super(itemView);
            StudentName = itemView.findViewById(R.id.studentnameass);
            avatar = itemView.findViewById(R.id.studentimgass);
            stcard = itemView.findViewById(R.id.studentcardass) ;
            stpoint = itemView.findViewById(R.id.stpoint) ;
        }
    }

    User user ;
    boolean check = false ;
    ArrayList<User> students = new ArrayList<>() ;
    Class myclass ;
    StudentWork activity ;
    Assignment assignment ;

    MarkAdapter(Class myclass , User user , StudentWork activity ,Assignment assignment) {
        this.myclass = myclass ;
        this.user = user ;
        ArrayList<String> names = new ArrayList<>() ;
        names.addAll(assignment.works.keySet()) ;
        for (String name:names) {
            for (User st :myclass.students) {
                if (st.username.equals(name)) {
                    students.add(st);
                }
            }
        }
        this.assignment =assignment ;

        this.activity = activity ;
    }

    @Override
    public void onAttachedToRecyclerView( RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MarkViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sudent_cardass, viewGroup, false);
        MarkViewHolder pvh = new MarkViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final MarkViewHolder stuViewHolder, int i) {

        final User student = students.get(i);

        stuViewHolder.stpoint.setText("Point : " +assignment.works.get(student.username).score );

        stuViewHolder.StudentName.setText(student.username);
        byte[] imgByte = student.avatar;
        System.out.println(Arrays.toString(imgByte));
        Bitmap bmp= BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        stuViewHolder.avatar.setImageBitmap(bmp);
        System.out.println(student.username);


        stuViewHolder.stcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity.getContext());
                LayoutInflater inflater =activity.getLayoutInflater();
                View v2 = inflater.inflate(R.layout.dialog_mark, null);
                final EditText point =  v2.findViewById(R.id.hmpoint);
                final TextView hmtext =  v2.findViewById(R.id.hmtext);
                final ImageView hmattach =  v2.findViewById(R.id.hmattach);
                if (assignment.works.get(student.username).attach !=null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(assignment.works.get(student.username).attach, 0, assignment.works.get(student.username).attach.length);
                    hmattach.setImageBitmap(bmp);
                }
                hmtext.setText( " Text :   " + assignment.works.get(student.username).text);
                builder.setView(v2).setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (point.length()!=0) {
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        Socket s = new Socket(activity.getResources().getString(R.string.ip), 8080);
                                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                                        System.out.println("Hello");
                                        String[] a = {"MarkStudent", myclass.code , assignment.topic.name , assignment.title , student.username , point.getText().toString()};
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
                        }
                        else
                            Toast.makeText(activity.getContext(), "Point is Empty", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                AlertDialog dialog2 = builder.create();
                dialog2.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return students.size();
    }


}
