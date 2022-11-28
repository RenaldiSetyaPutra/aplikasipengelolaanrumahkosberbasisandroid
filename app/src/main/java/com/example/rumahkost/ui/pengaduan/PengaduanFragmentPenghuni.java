package com.example.rumahkost.ui.pengaduan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.example.rumahkost.ui.penghuni.Adapter_Penghuni;
import com.example.rumahkost.ui.penghuni.Model_Penghuni;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PengaduanFragmentPenghuni extends Fragment {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    RecyclerView rv;
    ArrayList<Model_Pengaduan> lisPengaduan = new ArrayList<>();
    Adapter_PengaduanPenghuni adapter;
    FloatingActionButton tambah;
    ImageView edit, delete;
    EditText cari;

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengaduan_penghuni, null);
        context = getContext();
        rv         = view.findViewById(R.id.tampil);

        edit   = view.findViewById(R.id.edit);
        delete = view.findViewById(R.id.delete);
        tambah = view.findViewById(R.id.add);
        cari   = view.findViewById(R.id.cari);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        RefreshList();


        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    Search(s.toString());
                }else{
                    Search("");
                }
            }
        });
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), C_Pengaduan.class);
                startActivity(intent);
            }
        });
        return  view;
    }

    private void Search(String s) {
        db.child("Pengaduan").orderByChild("namapenghuni").startAt(s).endAt(s + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    lisPengaduan.clear();
                    for(DataSnapshot dss : dataSnapshot.getChildren()){
                        final Model_Pengaduan peng = dss.getValue(Model_Pengaduan.class);
                        lisPengaduan.add(peng);
                    }
                    Adapter_PengaduanPenghuni adapter = new Adapter_PengaduanPenghuni(lisPengaduan, getActivity());
                    rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else {
                    cari.setError("Data Tidak Ditemukan!");
                    lisPengaduan.clear();
                    Adapter_PengaduanPenghuni adapter = new Adapter_PengaduanPenghuni(lisPengaduan, getActivity());
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RefreshList();
    }

    public void RefreshList() {
        db.child("Pengaduan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lisPengaduan = new ArrayList<>();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Model_Pengaduan peng = ds.getValue(Model_Pengaduan.class);
                    peng.setId(peng.getId());
                    lisPengaduan.add(peng);
                }
                adapter = new Adapter_PengaduanPenghuni(lisPengaduan, getActivity());
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}