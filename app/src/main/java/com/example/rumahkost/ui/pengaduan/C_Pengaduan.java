package com.example.rumahkost.ui.pengaduan;

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

public class C_Pengaduan extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatePickerDialog datePickerDialog;
    long maxid = 0;
    TextView tid;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnernama;
    ArrayList<String> spinnerunit;
    Spinner nama, unit;
    EditText tglbyr, pengaduan;
    Button button;
    Button date;
    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy",id);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpengaduan);

        tid = findViewById(R.id.etid);
        nama = findViewById(R.id.nama);
        unit = findViewById(R.id.unit);
        button = findViewById(R.id.button);
        date = findViewById(R.id.date);
        pengaduan = findViewById(R.id.pengaduan);

        tglbyr = findViewById(R.id.tglbyr);

        db.child("Pengaduan").addValueEventListener(new ValueEventListener() {
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
                adapter = new ArrayAdapter<String>(C_Pengaduan.this, android.R.layout.simple_spinner_dropdown_item, spinnernama);
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
                adapter = new ArrayAdapter<String>(C_Pengaduan.this, android.R.layout.simple_spinner_dropdown_item, spinnerunit);
                unit.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(C_Pengaduan.this, new DatePickerDialog.OnDateSetListener() {
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
                String getpengaduan = pengaduan.getText().toString();

                if (getdate.isEmpty()){
                    tglbyr.setError("Masukan Tanggal Pengaduan");
                }else {
                    db.child("Pengaduan").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            db.child("Pengaduan").child(getid).child("id").setValue(getid);
                            db.child("Pengaduan").child(getid).child("namapenghuni").setValue(getnama);
                            db.child("Pengaduan").child(getid).child("unitpenghuni").setValue(getunit);
                            db.child("Pengaduan").child(getid).child("tglpengaduan").setValue(getdate);
                            db.child("Pengaduan").child(getid).child("pengaduan").setValue(getpengaduan);

                            Toast.makeText(C_Pengaduan.this, "Data Berhasil Disimpan!", Toast.LENGTH_SHORT).show();
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