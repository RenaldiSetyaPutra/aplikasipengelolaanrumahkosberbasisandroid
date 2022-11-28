package com.example.rumahkost.ui.laporan;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.example.rumahkost.ui.unit.Model_Unit;

import java.util.List;

public class TabelAdapterUnit extends RecyclerView.Adapter<TabelAdapterUnit.MyViewHolder> {
    private List<Model_Unit> list;
    private Activity activity;

    public TabelAdapterUnit(List<Model_Unit>list, Activity activity) {
        this.activity = activity;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = LayoutInflater.from(activity).inflate(R.layout.tabel_unit, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == 0){
            holder.id.setText("ID");
            holder.namaunt.setText("Nama Unit");
            holder.fasilitas.setText("Fasilitas");
            holder.hargasewa.setText("Harga Sewa");
            holder.status.setText("Status");
        }else {
            final Model_Unit unt = list.get(position-1);
            holder.id.setText(unt.getId());
            holder.namaunt.setText(unt.getNama());
            holder.fasilitas.setText(unt.getFasilitas());
            holder.hargasewa.setText(unt.getHarga());
            holder.status.setText(unt.getStatus());

        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }
    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView id,namaunt, fasilitas, hargasewa, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            namaunt = itemView.findViewById(R.id.namaunit);
            fasilitas = itemView.findViewById(R.id.fasilitas);
            hargasewa = itemView.findViewById(R.id.hargasewa);
            status   = itemView.findViewById(R.id.status);
        }
    }
}

