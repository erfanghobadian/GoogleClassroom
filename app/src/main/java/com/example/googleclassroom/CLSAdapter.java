package com.example.googleclassroom ;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class CLSAdapter extends RecyclerView.Adapter<CLSAdapter.PrimaryViewHolder> {



    public static class PrimaryViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mSecondaryRecyclerView;
        TextView TopicName ;


        public PrimaryViewHolder(View itemView) {
            super(itemView);
            mSecondaryRecyclerView = itemView.findViewById(R.id.rv_child);
            TopicName = itemView.findViewById(R.id.topic_name);
        }



    }

    Class myclass ;
    ArrayList<Topic> topics ;

    public CLSAdapter(Class myclass) {
        this.myclass = myclass ;
        this.topics = myclass.topics;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PrimaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clwparent_card, viewGroup, false);
        PrimaryViewHolder pvh = new PrimaryViewHolder(v) ;
        return pvh;
    }

    @Override
    public void onBindViewHolder(PrimaryViewHolder viewHolder, int i) {

        viewHolder.TopicName.setText(topics.get(i).name);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(viewHolder.itemView.getContext());
        viewHolder.mSecondaryRecyclerView.setLayoutManager(linearLayoutManager);
        ClSAdapterChild adapterChild = new ClSAdapterChild(topics.get(i).assignments);
        viewHolder.mSecondaryRecyclerView.setAdapter(adapterChild);
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }


}







class ClSAdapterChild extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static class SecondViewHolder extends RecyclerView.ViewHolder {
        public SecondViewHolder(View view) {
            super(view);
        }
    }

    ArrayList<Assignment> assignments ;

    ClSAdapterChild(ArrayList<Assignment> assignments) {
        this.assignments = assignments ;
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, int i) {

    }
    @Override
    public SecondViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clwchild_card, viewGroup, false);
        SecondViewHolder pvh = new SecondViewHolder(v) ;
        return pvh;
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }
}