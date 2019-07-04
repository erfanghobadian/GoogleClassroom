package com.example.googleclassroom ;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
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
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ClassViewHolder> {

     static class ClassViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView className;
        TextView classDes ;
        TextView classRoom ;
        TextView textCard ;
        ImageButton imageButton;


        ClassViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cardClass);
            className = itemView.findViewById(R.id.className);
            classDes = itemView.findViewById(R.id.classDes) ;
            classRoom = itemView.findViewById(R.id.classRoom) ;
            textCard = itemView.findViewById(R.id.cardText);
            imageButton = itemView.findViewById(R.id.imageButton);

        }
    }

    List<Class> aClasses;
     User user ;
     boolean check = false ;

     MainActivity activity ;
    RVAdapter(User user , MainActivity activity){
        this.user = user ;
        this.aClasses = user.classes;
        this.activity = activity ;
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
        for (User usr:cls.teachers) {
            if (usr.username.equals(user.username))
                check = true ;
        }





        personViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v , cls );
            }
        });


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
                intent.putExtra("myclass" , cls);
                intent.putExtra("user" , user);
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return aClasses.size();
    }


    private void showPopupMenu(final View view , final Class cls) {
        // inflate menu
        final PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.cart_menu, popup.getMenu());
        if (check) {
            popup.getMenu().findItem(R.id.unenroll).setVisible(false);
        }
        else {
            popup.getMenu().findItem(R.id.changeinfo).setVisible(false);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id= menuItem.getItemId();
                if (id == R.id.unenroll) {
                    popup.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    builder.setMessage("Unenroll ??").setTitle("Sure ?");

                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        Socket s = new Socket(activity.getResources().getString(R.string.ip) , 8080);
                                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                                        String[] a = {"RemoveFromClass" , user.username , user.password , cls.code  };
                                        oos.writeObject(a);
                                        oos.flush();


                                        Refresh refresh = new Refresh(activity);
                                        refresh.execute("Refresh" , user.username , user.password);

                                        oos.close();
                                        ois.close();
                                        s.close();

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                            }.start();
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
                if (id == R.id.changeinfo) {}
                return false;
            }
        });
        popup.show();
    }

}