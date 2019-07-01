package com.example.googleclassroom;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class TCHAdapter extends RecyclerView.Adapter<TCHAdapter.TCHViewHolder> {


    static class TCHViewHolder extends RecyclerView.ViewHolder {
        TextView TeacherName ;
        ImageButton imageButton ;
        TCHViewHolder(View itemView) {
        super(itemView);
            TeacherName = itemView.findViewById(R.id.teachername);
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
//        System.out.println(teacher.username);

    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }
}
