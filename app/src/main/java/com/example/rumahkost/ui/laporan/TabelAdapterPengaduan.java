package com.example.rumahkost.ui.laporan;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.example.rumahkost.ui.pengaduan.Model_LaporanPengaduan;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TabelAdapterPengaduan extends RecyclerView.Adapter<TabelAdapterPengaduan.MyViewHolder> {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private List<Model_LaporanPengaduan> list;
    private Activity activity;

    public TabelAdapterPengaduan(List<Model_LaporanPengaduan>list, Activity activity) {
        this.activity = activity;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = LayoutInflater.from(activity).inflate(R.layout.tabel_pengaduan, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == 0){
            holder.penghuni.setText("Nama Penghuni");
            holder.unit.setText("Unit");
            holder.tglpengaduan.setText("Tanggal Pengaduan");
            holder.pengaduan.setText("Pengaduan");
        }else{
            final Model_LaporanPengaduan pem = list.get(position-1);
            holder.penghuni.setText(pem.getNamapenghuni());
            holder.unit.setText(pem.getUnitpenghuni());
            holder.tglpengaduan.setText(pem.getTglpengaduan());
            holder.pengaduan.setText(pem.getPengaduan());
        }
    }


    @Override
    public int getItemCount() {
        return list.size()+1;
    }
    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView penghuni, unit, tglpengaduan, pengaduan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            penghuni = itemView.findViewById(R.id.namapenghuni);
            unit = itemView.findViewById(R.id.unit);
            tglpengaduan = itemView.findViewById(R.id.tglpengaduan);
            pengaduan   = itemView.findViewById(R.id.pengaduan);
        }
    }
}

