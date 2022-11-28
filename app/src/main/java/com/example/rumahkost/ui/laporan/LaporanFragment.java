package com.example.rumahkost.ui.laporan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.rumahkost.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LaporanFragment extends Fragment {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    ImageButton penghuni, unit, pembayaran, user, pengaduan, cekbayar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_laporan, container, false);
        penghuni = view.findViewById(R.id.btnpenghuni);
        unit = view.findViewById(R.id.btnunit);
        pembayaran = view.findViewById(R.id.btnbayar);
        user = view.findViewById(R.id.btnuser);
        pengaduan = view.findViewById(R.id.btnpengaduan);
        cekbayar = view.findViewById(R.id.btncekbayar);


        penghuni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LaporanPenghuni.class);
                startActivity(intent);
            }
        });

        unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LaporanUnit.class);
                startActivity(intent);
            }
        });

        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Laporan_Pembayaran.class);
                startActivity(intent);
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LaporanUser.class);
                startActivity(intent);
            }
        });

        cekbayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CekStatus.class);
                startActivity(intent);
            }
        });

        pengaduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Laporan_Pengaduan.class);
                startActivity(intent);
            }
        });

        return view;
    }
}