package com.example.rumahkost.ui.pembayaran;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rumahkost.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class C_Pembayaran extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatePickerDialog datePickerDialog;
    long maxid = 0;
    TextView tid;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnernama;
    ArrayList<String> spinnerunit;
    ArrayList<String> spinnerharga;
    Spinner nama, unit, harga;
    EditText tglbyr;
    Button button;
    Button date;
    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy",id);
    String status = "Sudah Bayar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpembayaran);

        tid = findViewById(R.id.etid);
        nama = findViewById(R.id.nama);
        unit = findViewById(R.id.unit);
        harga = findViewById(R.id.harga);
        button = findViewById(R.id.button);
        date = findViewById(R.id.date);

        tglbyr = findViewById(R.id.tglbyr);

        db.child("Pembayaran").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxid = (dataSnapshot.getChildrenCount());
                String id = String.valueOf(maxid+1);
                tid.setText(id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        db.child("Penghuni").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnernama = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    String  nma = item.child("nama").getValue(String.class);
                    spinnernama.add(nma);
                }
                adapter = new ArrayAdapter<String>(C_Pembayaran.this, android.R.layout.simple_spinner_dropdown_item, spinnernama);
                nama.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        db.child("Unit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnerunit = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    String  unt = item.child("nama").getValue(String.class);
                    spinnerunit.add(unt);
                }
                adapter = new ArrayAdapter<String>(C_Pembayaran.this, android.R.layout.simple_spinner_dropdown_item, spinnerunit);
                unit.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(C_Pembayaran.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        tglbyr.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getid    = tid.getText().toString();
                String getnama  = nama.getSelectedItem().toString();
                String getunit   = unit.getSelectedItem().toString();
                String getdate  = tglbyr.getText().toString();
                String getharga = harga.getSelectedItem().toString();

                if (getdate.isEmpty()){
                    tglbyr.setError("Masukan Tanggal Masuk");
                }else {
                    db.child("Pembayaran").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                db.child("Pembayaran").child(getid).child("id").setValue(getid);
                                db.child("Pembayaran").child(getid).child("penghuni").setValue(getnama);
                                db.child("Pembayaran").child(getid).child("unit").setValue(getunit);
                                db.child("Pembayaran").child(getid).child("tglbyr").setValue(getdate);
                                db.child("Pembayaran").child(getid).child("harga").setValue(getharga);
                                db.child("Pembayaran").child(getid).child("status").setValue(status);

                                Toast.makeText(C_Pembayaran.this, "Data Berhasil Disimpan!", Toast.LENGTH_SHORT).show();
                                finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
}