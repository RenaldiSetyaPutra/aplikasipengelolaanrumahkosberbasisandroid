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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class U_Pengaduan extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatePickerDialog datePickerDialog;
    private String KEY_NAME = "nama";
    long maxid = 0;
    TextView tid;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnernama;
    ArrayList<String> spinnerunit;
    Spinner nama, unit;
    EditText tglbyr;
    EditText namalama;
    EditText unitlama;
    EditText pengaduan, pengaduanlama;
    Button button;
    String id;
    Button date;
    Calendar calendar = Calendar.getInstance();
    Locale ide = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy",ide);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upengaduan);

        tid = findViewById(R.id.etid);
        nama = findViewById(R.id.nama);
        unit = findViewById(R.id.unit);
        tglbyr = findViewById(R.id.tglbyr);
        button = findViewById(R.id.button);
        date = findViewById(R.id.date);
        pengaduan = findViewById(R.id.pengaduan);


        namalama  = findViewById(R.id.namalama);
        unitlama  = findViewById(R.id.unitlama);
        pengaduanlama = findViewById(R.id.pengaduanlama);

        namalama.setVisibility(View.INVISIBLE);
        pengaduanlama.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        id = extras.getString(KEY_NAME);

        db.child("Pengaduan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(id)) {
                    final String getId = dataSnapshot.child(id).child("id").getValue(String.class);
                    final String getNama = dataSnapshot.child(id).child("namapenghuni").getValue(String.class);
                    final String getUnit = dataSnapshot.child(id).child("unitpenghuni").getValue(String.class);
                    final String getTglPengaduan = dataSnapshot.child(id).child("tglpengaduan").getValue(String.class);
                    final String getPengaduan = dataSnapshot.child(id).child("pengaduan").getValue(String.class);

                    tid.setText(getId);
                    tglbyr.setText(getTglPengaduan);
                    pengaduan.setText(getPengaduan);

                    namalama.setText(getNama);
                    unitlama.setText(getUnit);
                    pengaduanlama.setText(getPengaduan);
                }
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
                adapter = new ArrayAdapter<String>(U_Pengaduan.this, android.R.layout.simple_spinner_dropdown_item, spinnernama);
                nama.setAdapter(adapter);

                int spinnerPosition = adapter.getPosition(namalama.getText().toString());
                nama.setSelection(spinnerPosition);
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
                adapter = new ArrayAdapter<String>(U_Pengaduan.this, android.R.layout.simple_spinner_dropdown_item, spinnerunit);
                unit.setAdapter(adapter);

                int spinnerPosition = adapter.getPosition(unitlama.getText().toString());
                unit.setSelection(spinnerPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(U_Pengaduan.this, new DatePickerDialog.OnDateSetListener() {
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
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String getid    = tid.getText().toString();
                String getnama  = nama.getSelectedItem().toString();
                String getunit   = unit.getSelectedItem().toString();
                String getdate  = tglbyr.getText().toString();
                String getpengaduan = pengaduan.getText().toString();

                if (getdate.isEmpty()){
                    tglbyr.setError("Masukan Tanggal Pengaduan");
                } else {
                    db.child("Pengaduan").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("namapenghuni", nama.getSelectedItem().toString());
                            map.put("unitpenghuni", unit.getSelectedItem().toString());
                            map.put("tglpengaduan", tglbyr.getText().toString());
                            map.put("pengaduan", pengaduan.getText().toString());

                            db.child("Pengaduan").child(getid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(U_Pengaduan.this, "Data Berhasil di Update", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(U_Pengaduan.this, "Data Gagal di Update ", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
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