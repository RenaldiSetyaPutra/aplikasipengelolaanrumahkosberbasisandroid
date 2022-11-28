package com.example.rumahkost.ui.laporan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.example.rumahkost.ui.pembayaran.Model_Pembayaran;
import com.example.rumahkost.ui.pembayaran.strtotime;
import com.example.rumahkost.ui.penghuni.Model_Penghuni;
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

public class CekStatus extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private String KEY_NAME = "nama";
    RecyclerView rv;
    ArrayList<Model_Penghuni> listPenghuni = new ArrayList<>();
    ArrayList<Model_Pembayaran> listPembayaran =  new ArrayList<>();
    ArrayList<Model_Pembayaran> listFilter =  new ArrayList<>();
    AdapterCek adapter;
    Adapter adapter1;
    Button cari, btnpdf;
    EditText min, max;
    Button btn_min, btn_max;

    Context context;
    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
    Date date_minimal;
    Date date_maximal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_status);

        context = this;

        min        = findViewById(R.id.min);
        max        = findViewById(R.id.max);
        btn_min    = findViewById(R.id.btn_min);
        btn_max    = findViewById(R.id.btn_max);
        cari       = findViewById(R.id.cari);

        rv    = findViewById(R.id.tampil);
        btnpdf = findViewById(R.id.btnpdf);

        rv.setLayoutManager(new LinearLayoutManager(CekStatus.this));
        rv.setItemAnimator(new DefaultItemAnimator());
        RefreshList();

        btnpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                Query query = db.child("Pembayaran");
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


    }

    private void showListenerRange(DataSnapshot dataSnapshot, Date date_minimal, Date date_maximal) {
        listPembayaran.clear();
        listPenghuni.clear();

        for (final DataSnapshot dse : dataSnapshot.getChildren()) {
            Model_Pembayaran pem = dse.getValue(Model_Pembayaran.class);
            listPembayaran.add(pem);

            if (pem != null) {
                Date current = strtotime.strtotime(pem.getTglbyr());
                if (current.getTime() >= date_minimal.getTime() && current.getTime() <= date_maximal.getTime()) {
                    listFilter.add(pem);
                }
            }
        }
        listPembayaran.clear();


        db.child("Penghuni").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Model_Penghuni peng = ds.getValue(Model_Penghuni.class);
                    listPenghuni.add(peng);
                }
                adapter = new AdapterCek(listPenghuni, listFilter, CekStatus.this);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showListener(DataSnapshot dataSnapshot) {
        listPenghuni.clear();
        for (final DataSnapshot ds : dataSnapshot.getChildren()) {
            Model_Penghuni peng = ds.getValue(Model_Penghuni.class);
            listPenghuni.add(peng);
        }
        adapter = new AdapterCek(listPenghuni, listFilter,CekStatus.this);
        rv.setAdapter(adapter);
    }


    private void RefreshList() {
        db.child("Penghuni").addValueEventListener(new ValueEventListener() {
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