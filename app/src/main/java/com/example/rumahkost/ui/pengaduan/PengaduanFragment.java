package com.example.rumahkost.ui.pengaduan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.example.rumahkost.ui.pembayaran.strtotime;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PengaduanFragment extends Fragment {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    RecyclerView rv;
    ArrayList<Model_Pengaduan> lisPengaduan = new ArrayList<>();
    Adapter_Pengaduan adapter;
    FloatingActionButton tambah;
    ImageView edit, delete;
    Button cari;
    EditText min, max;
    Button btn_min, btn_max;

    Context context;
    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
    Date date_minimal;
    Date date_maximal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengaduan, null);
        context = getContext();
        min        = view.findViewById(R.id.min);
        max        = view.findViewById(R.id.max);
        btn_min    = view.findViewById(R.id.btn_min);
        btn_max    = view.findViewById(R.id.btn_max);
        rv         = view.findViewById(R.id.tampil);

        edit   = view.findViewById(R.id.edit);
        delete = view.findViewById(R.id.delete);
        tambah = view.findViewById(R.id.add);
        cari   = view.findViewById(R.id.cari);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        RefreshList();

        btn_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        min.setText(simpleDateFormat.format(calendar.getTime()));
                        date_minimal = calendar.getTime();

                        String input1 = min.getText().toString();
                        String input2 = max.getText().toString();

                        if(input1.isEmpty() && input2.isEmpty()){
                            cari.setEnabled(false);
                        }else {
                            cari.setEnabled(true);
                        }
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btn_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        max.setText(simpleDateFormat.format(calendar.getTime()));
                        date_maximal = calendar.getTime();

                        String input1 = max.getText().toString();
                        String input2 = min.getText().toString();

                        if(input1.isEmpty() && input2.isEmpty()){
                            cari.setEnabled(false);
                        }else {
                            cari.setEnabled(true);
                        }
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = db.child("Pengaduan");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        showListenerRange(dataSnapshot, date_minimal, date_maximal);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        tambah.setVisibility(View.INVISIBLE);
        return  view;
    }

    private void showListener(DataSnapshot dataSnapshot) {
        lisPengaduan.clear();
        for (final DataSnapshot ds : dataSnapshot.getChildren()) {
            Model_Pengaduan pem = ds.getValue(Model_Pengaduan.class);
            lisPengaduan.add(pem);
        }
        adapter = new Adapter_Pengaduan(lisPengaduan, getActivity());
        rv.setAdapter(adapter);
    }

    private void showListenerRange(DataSnapshot dataSnapshot, Date date_minimal, Date date_maximal) {
        lisPengaduan.clear();
        for (final DataSnapshot ds : dataSnapshot.getChildren()) {
            Model_Pengaduan pem = ds.getValue(Model_Pengaduan.class);
            if (pem != null) {
                Date current = strtotime.strtotime(pem.getTglpengaduan());
                if (current.getTime() >= date_minimal.getTime() && current.getTime() <= date_maximal.getTime()) {
                    lisPengaduan.add(pem);
                }
            }
        }
        adapter = new Adapter_Pengaduan(lisPengaduan, getActivity());
        rv.setAdapter(adapter);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RefreshList();
    }

    public void RefreshList() {
        db.child("Pengaduan").orderByChild("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showListener(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
