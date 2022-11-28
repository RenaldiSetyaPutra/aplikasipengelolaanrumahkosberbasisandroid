package com.example.rumahkost.ui.laporan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.example.rumahkost.ui.unit.Model_Unit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LaporanUnit extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    RecyclerView rv;
    ArrayList<Model_Unit> listUnit;
    TabelAdapterUnit adapter;
    Button btnpdf;
    int pageWidth = 2480;
    int pageHeight = 3580;
    int no = 1;
    String id,namaunt, fasilitas, hargasewa, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_unit);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        rv = findViewById(R.id.rvtabelunit);
        rv.setLayoutManager(new LinearLayoutManager(LaporanUnit.this));
        RefreshList();

        btnpdf = findViewById(R.id.btnpdf);
        btnpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void buatPDF() {
    }

    private void RefreshList() {
        db.child("Unit").orderByChild("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUnit = new ArrayList<>();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Model_Unit unt = ds.getValue(Model_Unit.class);
                    listUnit.add(unt);
                    no = no++;
                    id = unt.getId();
                    namaunt = unt.getNama();
                    fasilitas = unt.getFasilitas();
                    hargasewa = unt.getHarga();
                    status = unt.getStatus();
                }
                adapter = new TabelAdapterUnit(listUnit, LaporanUnit.this);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}