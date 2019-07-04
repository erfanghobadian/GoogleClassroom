package com.example.googleclassroom;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class TCHAdapter extends RecyclerView.Adapter<TCHAdapter.TCHViewHolder> {


    static class TCHViewHolder extends RecyclerView.ViewHolder {
        TextView TeacherName ;
        ImageView avatar ;
        TCHViewHolder(View itemView) {
        super(itemView);
            TeacherName = itemView.findViewById(R.id.teachername);
            avatar = itemView.findViewById(R.id.teacherimg);
        }
    }

    User user ;
    boolean check = false ;
    ArrayList<User> teachers ;
    Class myclass ;
    People activity ;

    TCHAdapter(Class myclass , User user , People activity) {
        this.myclass = myclass ;
        this.user = user ;
        this.teachers = myclass.teachers ;
        this.activity = activity ;
    }

    @Override
    public void onAttachedToRecyclerView( RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TCHViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.teacher_card, viewGroup, false);
        TCHViewHolder pvh = new TCHViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder( TCHViewHolder tchViewHolder, int i) {
        final User teacher = teachers.get(i);
        tchViewHolder.TeacherName.setText(teacher.username);
        byte[] imgByte = teacher.avatar;
        System.out.println(Arrays.toString(imgByte));
        Bitmap bmp= BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        tchViewHolder.avatar.setImageBitmap(bmp);

    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }
}
