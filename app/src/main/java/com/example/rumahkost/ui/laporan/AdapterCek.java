package com.example.rumahkost.ui.laporan;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.example.rumahkost.ui.pembayaran.Adapter_Pembayaran;
import com.example.rumahkost.ui.pembayaran.Model_Pembayaran;
import com.example.rumahkost.ui.penghuni.Model_Penghuni;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterCek extends RecyclerView.Adapter<AdapterCek.MyViewHolder>{
    private List<Model_Penghuni> list;
    private List<Model_Penghuni> listPeng;
    private List<Model_Pembayaran> listPem;
    private List<Model_Pembayaran> listFilter;
    private Activity activity;
    private List<String> ambil;
    Adapter_Pembayaran adapter;
    TextView textid,text1, text2, text3, text4, text5, text6;
    long total;
    LayoutInflater inflater;
    View view;
    String getPenghuni, ambilstatus, getStatus, getNama, getId;

    String statusbayar = "Sudah Bayar";
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");

    public AdapterCek(List<Model_Penghuni>list, List<Model_Pembayaran>listFilter, Activity activity) {
        this.activity = activity;
        this.list = list;
        this.listFilter = listFilter;
    }


    @NonNull
    @Override
    public AdapterCek.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = LayoutInflater.from(activity).inflate(R.layout.layout_cekbayar, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCek.MyViewHolder holder, int position) {
        final Model_Penghuni peng = list.get(position);
        holder.penghuni.setText(peng.getNama());

        for (int i = 0; i < listFilter.size(); i++) {
            getPenghuni = listFilter.get(i).getPenghuni();
            getStatus = listFilter.get(i).getStatus();

            if (getPenghuni.equals(peng.getNama()) == true) {
                holder.status.setText(statusbayar);
            }else{

            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView penghuni, status;
        CardView cardhasil;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            penghuni = itemView.findViewById(R.id.penghuni);
            status = itemView.findViewById(R.id.status);
            cardhasil = itemView.findViewById(R.id.cardhasil);
        }
    }
}