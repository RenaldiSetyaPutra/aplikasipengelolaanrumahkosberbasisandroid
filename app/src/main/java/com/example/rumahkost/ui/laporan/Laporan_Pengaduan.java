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
import com.example.rumahkost.ui.pembayaran.strtotime;
import com.example.rumahkost.ui.pengaduan.Model_LaporanPengaduan;
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

public class Laporan_Pengaduan extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    RecyclerView rv;
    ArrayList<Model_LaporanPengaduan> listPengaduan = new ArrayList<>();
    TabelAdapterPengaduan adapter;
    Button btnpdf;
    int no = 1;
    Button cari;
    EditText min, max;
    Button btn_min, btn_max;
    String ide,penghuni, unit, tglbyr, harga;

    Context context;
    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
    Date date_minimal;
    Date date_maximal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_pengaduan);

        //ActivityCompat.requestPermissions(this, new String[]{
        // Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        context = this;
        min        = findViewById(R.id.min);
        max        = findViewById(R.id.max);
        btn_min    = findViewById(R.id.btn_min);
        btn_max    = findViewById(R.id.btn_max);
        cari       = findViewById(R.id.cari);

        btnpdf     = findViewById(R.id.btnpdf);
        rv         = findViewById(R.id.rvtabelpembayaran);
        rv.setLayoutManager(new LinearLayoutManager(Laporan_Pengaduan.this));
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
                Query query = db.child("LaporanPengaduan").orderByChild("tglpengaduan");
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

        btnpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showListenerRange(DataSnapshot dataSnapshot, Date date_minimal, Date date_maximal) {
        listPengaduan.clear();
        for (final DataSnapshot ds : dataSnapshot.getChildren()) {
            Model_LaporanPengaduan pem = ds.getValue(Model_LaporanPengaduan.class);
            if (pem != null) {
                Date current = strtotime.strtotime(pem.getTglpengaduan());
                if (current.getTime() >= date_minimal.getTime() && current.getTime() <= date_maximal.getTime()) {
                    listPengaduan.add(pem);
                }
            }
        }
        adapter = new TabelAdapterPengaduan(listPengaduan, Laporan_Pengaduan.this);
        rv.setAdapter(adapter);
    }


    private void RefreshList() {
        db.child("LaporanPengaduan").orderByChild("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showListener(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showListener(DataSnapshot dataSnapshot) {
        listPengaduan.clear();
        for (final DataSnapshot ds : dataSnapshot.getChildren()) {
            Model_LaporanPengaduan pem = ds.getValue(Model_LaporanPengaduan.class);
            listPengaduan.add(pem);
            no = no++;
            penghuni = pem.getNamapenghuni();
            unit = pem.getUnitpenghuni();
            tglbyr = pem.getTglpengaduan();
            harga = pem.getPengaduan();
        }
        adapter = new TabelAdapterPengaduan(listPengaduan, Laporan_Pengaduan.this);
        rv.setAdapter(adapter);
    }
}