package com.example.googleclassroom ;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ClassViewHolder> {

     static class ClassViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView className;
        TextView classDes ;
        TextView classRoom ;
        TextView textCard ;


        ClassViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cardClass);
            className = itemView.findViewById(R.id.className);
            classDes = itemView.findViewById(R.id.classDes) ;
            classRoom = itemView.findViewById(R.id.classRoom) ;
            textCard = itemView.findViewById(R.id.cardText);

        }
    }

    List<Class> aClasses;
     User user ;

    RVAdapter(User user){
        this.user = user ;
        this.aClasses = user.classes;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.class_cardview, viewGroup, false);
        ClassViewHolder pvh = new ClassViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ClassViewHolder personViewHolder, int i) {
        final Class cls = aClasses.get(i) ;
        personViewHolder.className.setText(cls.name);
        personViewHolder.classDes.setText(cls.des);
        personViewHolder.classRoom.setText(cls.room);
        boolean check = false ;
        for (User usr:cls.teachers) {
            if (usr.username.equals(user.username))
                check = true ;
        }


        if (check) {
            String text = cls.students.size() + " Student" ;
            personViewHolder.textCard.setText(text);
        }
        else {
            personViewHolder.textCard.setText(cls.teachers.get(0).username);
        }
        personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ClassActivity.class);
                intent.putExtra("class" , cls);
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return aClasses.size();
    }
}