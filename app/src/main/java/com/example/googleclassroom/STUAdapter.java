package com.example.googleclassroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class STUAdapter extends RecyclerView.Adapter<STUAdapter.STUViewHolder> {


    static class STUViewHolder extends RecyclerView.ViewHolder {
        TextView StudentName ;
        ImageButton imageButton ;
        ImageView avatar ;
        STUViewHolder(View itemView) {
        super(itemView);
            StudentName = itemView.findViewById(R.id.studentname);
            imageButton = itemView.findViewById(R.id.estudentBTN);
            avatar = itemView.findViewById(R.id.studentimg);
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

        for (User usr:myclass.teachers) {
            if (usr.username.equals(user.username))
                check = true ;
        }

        stuViewHolder.StudentName.setText(student.username);
        byte[] imgByte = student.avatar;
        System.out.println(Arrays.toString(imgByte));
        Bitmap bmp= BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        stuViewHolder.avatar.setImageBitmap(bmp);
        System.out.println(student.username);
        if (!check){
            stuViewHolder.imageButton.setVisibility(View.INVISIBLE);
        }
        stuViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v , myclass , student);
            }
        });

    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    private void showPopupMenu(final View view , final Class cls , final User student) {
        // inflate menu
        final PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.estudentmenu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id= menuItem.getItemId();
                if (id == R.id.remove_student) {
                    popup.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    builder.setMessage("Remove Student " + student.username + " From Class??").setTitle("Sure ?");

                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        Socket s = new Socket(view.getResources().getString(R.string.ip), 8080);
                                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                                        String[] a = {"RemoveFromClass" , student.username , student.password , cls.code  };
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
                            RefreshPOE refreshPOE = new RefreshPOE(activity);
                            refreshPOE.execute("RefreshCLW", user.username, user.password, myclass.code) ;
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
                return false;
            }
        });
        popup.show();
    }

}
