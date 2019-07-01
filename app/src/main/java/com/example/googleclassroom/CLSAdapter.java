package com.example.googleclassroom ;

import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class CLSAdapter extends RecyclerView.Adapter<CLSAdapter.PrimaryViewHolder> {



    public static class PrimaryViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mSecondaryRecyclerView;
        TextView TopicName ;
        ImageButton imageButton ;



        public PrimaryViewHolder(View itemView) {
            super(itemView);
            mSecondaryRecyclerView = itemView.findViewById(R.id.rv_child);
            TopicName = itemView.findViewById(R.id.topic_name);
            imageButton = itemView.findViewById(R.id.imageButtonPC);
        }



    }

    User user ;
    boolean check = false ;

    Class myclass ;
    ArrayList<Topic> topics ;
    Classwork activity ;

    public CLSAdapter(Class myclass , User user , Classwork activity) {
        this.myclass = myclass ;
        this.topics = myclass.topics;
        this.user = user ;
        this.activity = activity ;

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
        final Topic topic = topics.get(i) ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(viewHolder.itemView.getContext());
        viewHolder.mSecondaryRecyclerView.setLayoutManager(linearLayoutManager);
        ClSAdapterChild adapterChild = new ClSAdapterChild(topics.get(i).assignments , check);
        viewHolder.mSecondaryRecyclerView.setAdapter(adapterChild);

        for (User usr:myclass.teachers) {
            if (usr.username.equals(user.username))
                check = true ;
        }
        if (!check) {
            viewHolder.imageButton.setVisibility(View.INVISIBLE);
        }

            viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("click");
                    showPopupMenu(v , myclass , topic );
                }
            });



    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    private void showPopupMenu(final View view , final Class cls , final Topic topic) {
        // inflate menu
        final PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.crad_menu_cw, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id= menuItem.getItemId();
                if (id == R.id.remove_card) {
                    popup.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    builder.setMessage("Remove Topic ?").setTitle("Sure ?");

                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        Socket s = new Socket("10.0.2.2" , 8080);
                                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                                        String[] a = {"RemoveTopicFromClass" ,  cls.code , topic.name  };
                                        oos.writeObject(a);
                                        oos.flush();



                                        oos.close();
                                        ois.close();
                                        s.close();

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                            }.start();

                            RefreshCLW refreshCLW = new RefreshCLW(activity);
                            refreshCLW.execute("RefreshCLW", user.username, user.password, myclass.code);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                if (id == R.id.edit_card) {}
                return false;
            }
        });
        popup.show();
    }



}







class ClSAdapterChild extends RecyclerView.Adapter<ClSAdapterChild.SecondViewHolder> {
    public static class SecondViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButton ;
        public SecondViewHolder(View view) {
            super(view);
            imageButton = view.findViewById(R.id.imageButtonCC);
        }
    }

    boolean check ;
    ArrayList<Assignment> assignments ;

    ClSAdapterChild(ArrayList<Assignment> assignments , boolean check) {
        this.assignments = assignments ;
        this.check = check ;
    }

    @Override
    public void onBindViewHolder( SecondViewHolder viewHolder, int i) {

        if (check) {
            viewHolder.imageButton.setVisibility(View.INVISIBLE);
        }

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