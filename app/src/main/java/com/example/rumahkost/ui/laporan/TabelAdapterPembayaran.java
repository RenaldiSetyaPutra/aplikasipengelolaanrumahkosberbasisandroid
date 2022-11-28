package com.example.rumahkost.ui.laporan;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.example.rumahkost.ui.pembayaran.Model_Pembayaran;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TabelAdapterPembayaran extends RecyclerView.Adapter<TabelAdapterPembayaran.MyViewHolder> {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private List<Model_Pembayaran> list;
    private Activity activity;
    int harga;


    public TabelAdapterPembayaran(List<Model_Pembayaran>list, Activity activity) {
        this.activity = activity;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = LayoutInflater.from(activity).inflate(R.layout.tabel_pembayaran, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == 0){
            holder.id.setText("ID");
            holder.penghuni.setText("Nama Penghuni");
            holder.unit.setText("Unit");
            holder.tglbyr.setText("Tanggal Pembayaran");
            holder.harga.setText("Harga Sewa");
        }else if (position <= list.size()){
            final Model_Pembayaran pem = list.get(position-1);
            holder.id.setText(pem.getId());
            holder.penghuni.setText(pem.getPenghuni());
            holder.unit.setText(pem.getUnit());
            holder.tglbyr.setText(pem.getTglbyr());
            holder.harga.setText(pem.getHarga());
        }else{
            harga = 500000 * list.size();
            String total = String.valueOf(harga);
            holder.id.setText("");
            holder.penghuni.setText("");
            holder.unit.setText("");
            holder.tglbyr.setText("Total");
            String resultRupiah = formatRupiah(Double.parseDouble(total));
            holder.harga.setText(resultRupiah);
        }
    }

    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    public int getItemCount() {
        return list.size()+2;
    }
    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView id,penghuni, unit, tglbyr, harga;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            penghuni = itemView.findViewById(R.id.namapenghuni);
            unit = itemView.findViewById(R.id.unit);
            tglbyr = itemView.findViewById(R.id.tglpembayaran);
            harga   = itemView.findViewById(R.id.hargasewa);
        }
    }
}

