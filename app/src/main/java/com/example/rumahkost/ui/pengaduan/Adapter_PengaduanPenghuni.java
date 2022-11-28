package com.example.rumahkost.ui.pengaduan;

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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Adapter_PengaduanPenghuni extends RecyclerView.Adapter<Adapter_PengaduanPenghuni.MyViewHolder> {
    private String KEY_NAME = "nama";
    private List<Model_Pengaduan> list;
    private Activity activity;

    TextView textid,text1, text2, text3, text4;
    LayoutInflater inflater;
    View view;
    String status = "Kosong";
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");

    public Adapter_PengaduanPenghuni(List<Model_Pengaduan>list, Activity activity) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = LayoutInflater.from(activity).inflate(R.layout.layout_pengaduan_penghuni, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Model_Pengaduan pem = list.get(position);
        holder.penghuni.setText(pem.getNamapenghuni());
        holder.tgl.setText("Tanggal Pengaduan : " + pem.getTglpengaduan());
        holder.pengaduan.setText(pem.getPengaduan());


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(activity, U_Pengaduan.class);
                in.putExtra(KEY_NAME, pem.getId());
                activity.startActivity(in);
            }
        });

        holder.cardhasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder a = new AlertDialog.Builder(activity);
                inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.activity_pengaduan,null);
                a.setView(view);
                a.setCancelable(true);
                a.setTitle("Detail Pembayaran");

                textid =  view.findViewById(R.id.textViewid);
                text1 =  view.findViewById(R.id.textView1);
                text2 =  view.findViewById(R.id.textView2);
                text3 =  view.findViewById(R.id.textView3);
                text4 =  view.findViewById(R.id.textView4);

                final String getId   = pem.getId();
                final String getnama = pem.getNamapenghuni();
                final String getunt  = pem.getUnitpenghuni();
                final String gettgl  = pem.getTglpengaduan();
                final String gethrg  = pem.getPengaduan();

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
        TextView penghuni, tgl, pengaduan;
        CardView cardhasil;
        ImageView edit;
        SearchView cari;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            penghuni = itemView.findViewById(R.id.penghuni);
            tgl = itemView.findViewById(R.id.tgl);
            pengaduan = itemView.findViewById(R.id.pengaduan);
            cardhasil = itemView.findViewById(R.id.cardhasil);
            edit   = itemView.findViewById(R.id.edit);
            cari   = itemView.findViewById(R.id.cari);
        }
    }
}
