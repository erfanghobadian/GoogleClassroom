package com.example.googleclassroom;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class STUAdapter extends RecyclerView.Adapter<STUAdapter.STUViewHolder> {


    static class STUViewHolder extends RecyclerView.ViewHolder {
        TextView StudentName ;
        ImageButton imageButton ;
        STUViewHolder(View itemView) {
        super(itemView);
            StudentName = itemView.findViewById(R.id.studentname);
        }
    }

    User user ;
    boolean check = false ;
    ArrayList<User> students ;
    Class myclass ;
    People activity ;

    STUAdapter(Class myclass , User user , People activity) {
        this.myclass = myclass ;
        this.user = user ;
        this.students = myclass.students ;
        this.activity = activity ;
    }

    @Override
    public void onAttachedToRecyclerView( RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public STUViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sudent_card, viewGroup, false);
        STUViewHolder pvh = new STUViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder( STUViewHolder stuViewHolder, int i) {
        final User student = students.get(i);
        stuViewHolder.StudentName.setText(student.username);

    }

    @Override
    public int getItemCount() {
        return students.size();
    }
}
