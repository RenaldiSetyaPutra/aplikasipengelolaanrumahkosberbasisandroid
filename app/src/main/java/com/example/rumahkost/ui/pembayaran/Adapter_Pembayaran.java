package com.example.rumahkost.ui.pembayaran;

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

public class Adapter_Pembayaran extends RecyclerView.Adapter<Adapter_Pembayaran.MyViewHolder> {
    private String KEY_NAME = "nama";
    private List<Model_Pembayaran> list;
    private Activity activity;

    TextView textid,text1, text2, text3, text4;
    LayoutInflater inflater;
    View view;
    String status = "Kosong";
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");

    public Adapter_Pembayaran(List<Model_Pembayaran>list, Activity activity) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = LayoutInflater.from(activity).inflate(R.layout.layout_pembayaran, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Model_Pembayaran pem = list.get(position);
        holder.penghuni.setText(pem.getPenghuni());
        holder.tgl.setText("Tanggal Bayar : " + pem.getTglbyr());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.child("Pembayaran").child(pem.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(activity, "Data Berhasil Dihapus!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "Data Gagal Dihapus", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setMessage("Apakah Yakin Ingin Menghapus Data "+ pem.getPenghuni());
                builder.show();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(activity, U_Pembayaran.class);
                in.putExtra(KEY_NAME, pem.getId());
                activity.startActivity(in);
            }
        });

        holder.cardhasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder a = new AlertDialog.Builder(activity);
                inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.activity_pembayaran,null);
                a.setView(view);
                a.setCancelable(true);
                a.setTitle("Detail Pembayaran");

                textid =  view.findViewById(R.id.textViewid);
                text1 =  view.findViewById(R.id.textView1);
                text2 =  view.findViewById(R.id.textView2);
                text3 =  view.findViewById(R.id.textView3);
                text4 =  view.findViewById(R.id.textView4);

                final String getId   = pem.getId();
                final String getnama = pem.getPenghuni();
                final String getunt  = pem.getUnit();
                final String gettgl  = pem.getTglbyr();
                final String gethrg  = pem.getHarga();

                textid.setText(getId);
                text1.setText(getnama);
                text2.setText(getunt);
                text3.setText(gettgl);
                text4.setText(gethrg);


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
        TextView penghuni, tgl;
        CardView cardhasil;
        ImageView delete, edit;
        SearchView cari;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            penghuni = itemView.findViewById(R.id.penghuni);
            tgl = itemView.findViewById(R.id.tgl);
            cardhasil = itemView.findViewById(R.id.cardhasil);
            delete = itemView.findViewById(R.id.delete);
            edit   = itemView.findViewById(R.id.edit);
            cari   = itemView.findViewById(R.id.cari);
        }
    }
}
