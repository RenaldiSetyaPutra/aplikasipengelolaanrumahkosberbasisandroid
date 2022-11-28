package com.example.rumahkost.ui.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Adapter_User extends RecyclerView.Adapter<Adapter_User.MyViewHolder> {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private String KEY_NAME = "nama";
    private List<Model_User> list;
    private Activity activity;
    TextView textid,text1, text2, text3;
    LayoutInflater inflater;
    View view;

    public Adapter_User(List<Model_User>list, Activity activity) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = LayoutInflater.from(activity).inflate(R.layout.layout_user, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Model_User usr = list.get(position);
        holder.username.setText(usr.getUsername());
        holder.status.setText(usr.getStatus());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.child("User").child(usr.getUsername()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(activity, "Data Berhasil Dihapus!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "Data Gagal di Hapus", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setMessage("Apakah Yakin Ingin Menghapus Data "+ usr.getUsername());
                builder.show();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(activity, U_User.class);
                in.putExtra(KEY_NAME, usr.getUsername());
                activity.startActivity(in);
            }
        });

        holder.cardhasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder a = new AlertDialog.Builder(activity);
                inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.activity_user,null);
                a.setView(view);
                a.setCancelable(true);
                a.setTitle("Detail User");

                textid = view.findViewById(R.id.textViewid);
                text1  = view.findViewById(R.id.textView1);
                text2  = view.findViewById(R.id.textView2);
                text3  = view.findViewById(R.id.textView3);

                String getUsername  = usr.getUsername();
                String getPassword  = usr.getPassword();
                String getId        = usr.getId();
                String getStatus    = usr.getStatus();

                textid.setText(getId);
                text1.setText(getUsername);
                text2.setText(getPassword);
                text3.setText(getStatus);


                a.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                });
                a.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView  username, status;
        CardView cardhasil;
        ImageView delete, edit;
        SearchView cari;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            status   = itemView.findViewById(R.id.status);
            cardhasil = itemView.findViewById(R.id.cardhasil);
            delete = itemView.findViewById(R.id.delete);
            edit   = itemView.findViewById(R.id.edit);
            cari   = itemView.findViewById(R.id.cari);
        }
    }
}
