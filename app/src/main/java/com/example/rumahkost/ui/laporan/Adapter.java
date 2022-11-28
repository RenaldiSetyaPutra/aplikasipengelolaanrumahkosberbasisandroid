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
import com.example.rumahkost.ui.pembayaran.Model_Pembayaran;
import com.example.rumahkost.ui.penghuni.Model_Penghuni;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private List<Model_Pembayaran> list;
    private List<Model_Penghuni> listPeng;
    private List<Model_Pembayaran> listFilter;
    private Activity activity;


    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");

    public Adapter(List<Model_Pembayaran>list, List<Model_Pembayaran>listFilter, Activity activity) {
        this.activity = activity;
        this.list = list;
        this.listFilter = listFilter;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = LayoutInflater.from(activity).inflate(R.layout.layout_cekbayar, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Model_Pembayaran pem = list.get(position);
        holder.penghuni.setText(pem.getPenghuni());

        for (int i = 0; i < listFilter.size(); i++) {
            String getNama   = listFilter.get(i).getPenghuni();
            String getStatus = listFilter.get(i).getStatus();


            if (getNama.equals(pem.getPenghuni()) == true){
                holder.status.setText(getStatus);
            }else {
                
            }
        }



        db.child("Penghuni").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPeng = new ArrayList<>();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Model_Penghuni huni = ds.getValue(Model_Penghuni.class);
                    listPeng.add(huni);

                    if (huni.getNama().equals(pem.getPenghuni())) {
                        //holder.penghuni.setText(huni.getNama());
                    } else  {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView status, penghuni;
        CardView cardhasil;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            penghuni = itemView.findViewById(R.id.penghuni);
            cardhasil = itemView.findViewById(R.id.cardhasil);
        }
    }
}
