package com.example.googleclassroom ;

import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;


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

        for (Assignment ass:topic.assignments) {
            System.out.println(ass.title);
        }
        for (User usr:myclass.teachers) {
            if (usr.username.equals(user.username))
                check = true ;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(viewHolder.itemView.getContext());
        viewHolder.mSecondaryRecyclerView.setLayoutManager(linearLayoutManager);
        ClSAdapterChild adapterChild = new ClSAdapterChild(topics.get(i).assignments , check , activity , user , myclass);
        viewHolder.mSecondaryRecyclerView.setAdapter(adapterChild);


        if (!check) {
            viewHolder.imageButton.setVisibility(View.INVISIBLE);
        }
        if (topic.name.equals("No Topic")) {
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
                                        Socket s = new Socket(activity.getResources().getString(R.string.ip), 8080);
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
                if (id == R.id.edit_card) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity.getContext());
                    LayoutInflater inflater = activity.requireActivity().getLayoutInflater();
                    View v = inflater.inflate(R.layout.dialog_create_topic, null);
                    final EditText topicname =  v.findViewById(R.id.ctopic_name);
                    topicname.setText(topic.name);
                    builder.setView(v).setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (topicname.length()!=0) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        try {
                                            Socket s = new Socket(activity.getResources().getString(R.string.ip), 8080);
                                            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                                            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                                            System.out.println("Hello");
                                            String[] a = {"EditTopic", myclass.code ,topic.name ,   topicname.getText().toString()};
                                            System.out.println(a[1] +" "+ a[2]);
                                            oos.writeObject(a);
                                            oos.flush();



                                            oos.close();
                                            ois.close();
                                            s.close();
                                            RefreshCLW refreshCLW = new RefreshCLW(activity);
                                            refreshCLW.execute("RefreshCLW", user.username, user.password, myclass.code);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            }
                            else
                                Toast.makeText(activity.getContext(), "Topic Name is Empty", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });


                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                return false;
            }
        });
        popup.show();
    }



}







class ClSAdapterChild extends RecyclerView.Adapter<ClSAdapterChild.SecondViewHolder> {
    public static class SecondViewHolder extends RecyclerView.ViewHolder {
        TextView assTitle ;
        TextView assDateTime ;
        CardView cardView ;
        public SecondViewHolder(View view) {
            super(view);
            assTitle = view.findViewById(R.id.asstitle) ;
            assDateTime = view.findViewById(R.id.DateTimeAss) ;
            cardView = view.findViewById(R.id.clwchild_card) ;
        }
    }

    boolean check ;
    ArrayList<Assignment> assignments ;
    Classwork activity ;
    User user  ;
    Class myclass;
    ClSAdapterChild(ArrayList<Assignment> assignments , boolean check , Classwork classwork , User user , Class myclass) {
        this.assignments = assignments ;
        this.check = check ;
        this.activity = classwork ;
        this.user = user ;
        this.myclass = myclass ;

    }

    @Override
    public void onBindViewHolder(final SecondViewHolder viewHolder, int i) {
        final Assignment assignment = assignments.get(i);

        viewHolder.assTitle.setText(assignment.title);
        viewHolder.assDateTime.setText(assignment.due.get(Calendar.DAY_OF_MONTH) + "/" + (assignment.due.get(Calendar.MONTH)+ 1) + "/" + assignment.due.get(Calendar.YEAR));
        viewHolder.assDateTime.setText(viewHolder.assDateTime.getText() + "-" + assignment.due.get(Calendar.HOUR_OF_DAY) + ":" +assignment.due.get(Calendar.MINUTE));
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(check);
                if (check) {
                    Intent intent = new Intent(activity.getActivity(), AssignmentActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("class", myclass);
                    intent.putExtra("ass", assignment);
                    activity.startActivity(intent);
                }
                else  {
                    Intent intent = new Intent(activity.getActivity(), AssignmentStudentActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("class", myclass);
                    intent.putExtra("ass", assignment);
                    activity.startActivity(intent);
                }


            }
        });
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