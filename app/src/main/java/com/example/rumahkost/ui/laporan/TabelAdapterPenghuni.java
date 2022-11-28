package com.example.rumahkost.ui.laporan;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.example.rumahkost.ui.penghuni.Model_Penghuni;

import java.util.List;


public class TabelAdapterPenghuni extends RecyclerView.Adapter<TabelAdapterPenghuni.MyViewHolder> {
    private List<Model_Penghuni> list;
    private Activity activity;

    public TabelAdapterPenghuni(List<Model_Penghuni>list, Activity activity) {
        this.activity = activity;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = LayoutInflater.from(activity).inflate(R.layout.tabel_penghuni, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == 0){
            holder.id.setText("ID");
            holder.nama.setText("Nama Penghuni");
            holder.jk.setText("Jenis Kelamin");
            holder.asal.setText("Asal");
            holder.nohp.setText("No Hp");
            holder.tglmsk.setText("Tanggal Masuk");
            holder.unit.setText("Unit");
        }else {
            final Model_Penghuni peng = list.get(position-1);
            holder.id.setText(peng.getId());
            holder.nama.setText(peng.getNama());
            holder.jk.setText(peng.getJk());
            holder.asal.setText(peng.getAsal());
            holder.nohp.setText(peng.getNo_hp());
            holder.tglmsk.setText(peng.getTgl_msk());
            holder.unit.setText(peng.getUnit());
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }
    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView id,nama, jk, asal, nohp, tglmsk, unit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            nama = itemView.findViewById(R.id.nama);
            jk = itemView.findViewById(R.id.jk);
            asal = itemView.findViewById(R.id.asal);
            nohp   = itemView.findViewById(R.id.nohp);
            tglmsk   = itemView.findViewById(R.id.tglmsk);
            unit   = itemView.findViewById(R.id.unit);
        }
    }
}
