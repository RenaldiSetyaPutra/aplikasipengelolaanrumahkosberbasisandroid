package com.example.rumahkost.ui.penghuni;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class U_Penghuni extends AppCompatActivity {
    private String KEY_NAME = "nama";
    Button btn;
    RadioGroup jk;
    TextView nama, asal, hp, tglmsk, tid, namalama, unitlama, jklama;
    RadioButton laki, perempuan;
    Spinner unit;
    String id;
    String rb;
    ArrayList<String> spinnerlist;
    ArrayAdapter<String> adapter;
    DatePickerDialog datePickerDialog;
    String status = "Kosong";
    String unithapus = "Unit Telah Dihapus";
    Button btndate;
    Calendar calendar = Calendar.getInstance();
    Locale ide = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy",ide);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upenghuni);
        DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
        tid   = findViewById(R.id.etid);
        nama = findViewById(R.id.et1);
        jk =  findViewById(R.id.radiogrup);
        asal = findViewById(R.id.et3);
        hp = findViewById(R.id.et4);
        tglmsk = findViewById(R.id.date);
        unit = findViewById(R.id.spinner);
        namalama = findViewById(R.id.namalama);
        jklama = findViewById(R.id.jklama);
        unitlama = findViewById(R.id.unitlama);
        laki = findViewById(R.id.b_laki);
        perempuan = findViewById(R.id.b_perempuan);
        btndate = findViewById(R.id.btndate);

        namalama.setVisibility(View.INVISIBLE);
        unitlama.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        id = extras.getString(KEY_NAME);

        db.child("Penghuni").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(id)) {
                    final String getId = dataSnapshot.child(id).child("id").getValue(String.class);
                    final String getNama = dataSnapshot.child(id).child("nama").getValue(String.class);
                    final String getJk = dataSnapshot.child(id).child("jk").getValue(String.class);
                    final String getAsal = dataSnapshot.child(id).child("asal").getValue(String.class);
                    final String getNohp = dataSnapshot.child(id).child("no_hp").getValue(String.class);
                    final String getTglMsk = dataSnapshot.child(id).child("tgl_msk").getValue(String.class);
                    final String getUnit = dataSnapshot.child(id).child("unit").getValue(String.class);

                    tid.setText(getId);
                    nama.setText(getNama);
                    jklama.setText(getJk);
                    asal.setText(getAsal);
                    hp.setText(getNohp);
                    tglmsk.setText(getTglMsk);

                    namalama.setText(getNama);
                    unitlama.setText(getUnit);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(U_Penghuni.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        tglmsk.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        jk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.b_laki:
                        // do operations specific to this selection
                        rb = "Laki-laki";
                        break;
                    case R.id.b_perempuan:
                        // do operations specific to this selection
                        rb = "Perempuan";
                        break;
                }
            }
        });

        db.child("Unit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnerlist = new ArrayList<>();;
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    String  unt = item.child("nama").getValue(String.class);
                    if (dataSnapshot.hasChild(unt)) {
                        //final String getstatus = dataSnapshot.child(unt).child("status").getValue(String.class);
                        //final String getunit = dataSnapshot.child(id).child("status").getValue(String.class);
                        //if(getstatus.equals(status)){
                            spinnerlist.add(unt);
                        //}
                    }

                }
                adapter = new ArrayAdapter<String>(U_Penghuni.this, android.R.layout.simple_spinner_dropdown_item, spinnerlist);
                unit.setAdapter(adapter);

                int spinnerPosition = adapter.getPosition(unitlama.getText().toString());
                unit.setSelection(spinnerPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String uid     = tid.getText().toString();
                String unama   = nama.getText().toString();
                String ujk     = rb;
                String uasal   = asal.getText().toString();
                String uhp     = hp.getText().toString();
                String utglmsk = tglmsk.getText().toString();
                String uunit   = unit.getSelectedItem().toString();

                String tnama   = namalama.getText().toString();
                String tunit   = unitlama.getText().toString();

                if(unama.isEmpty()){
                    nama.setError("Masukan Nama");
                } else if (uasal.isEmpty()){
                    asal.setError("Masukan Asal");
                }else if (uhp.isEmpty()){
                    hp.setError("Masukan Nomor Handphone");
                } else if (utglmsk.isEmpty()){
                    tglmsk.setError("Masukan Tanggal Masuk");
                }else {
                    if(unama.equals(id)) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("jk", rb);
                        map.put("asal", asal.getText().toString());
                        map.put("no_hp", hp.getText().toString());
                        map.put("tgl_msk", tglmsk.getText().toString());
                        map.put("unit", unit.getSelectedItem().toString());
                        db.child("Penghuni").child(tnama).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if(tunit.equals(unithapus)) {
                                    db.child("Unit").child(tunit).removeValue();

                                    HashMap<String, Object> map1 = new HashMap<>();
                                    map1.put("status", unama);
                                    db.child("Unit").child(uunit).updateChildren(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            HashMap<String, Object> map1 = new HashMap<>();
                                            map1.put("status", status);
                                            Toast.makeText(U_Penghuni.this, "Data Berhasil Diubah!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }else{
                                    db.child("Penghuni").child(unama).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            HashMap<String, Object> map1 = new HashMap<>();
                                            map1.put("status", unama);
                                            db.child("Unit").child(uunit).updateChildren(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    HashMap<String, Object> map1 = new HashMap<>();
                                                    map1.put("status", status);
                                                    db.child("Unit").child(tunit).updateChildren(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(U_Penghuni.this, "Data Berhasil di Update!", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(U_Penghuni.this, "Data Gagal Diubah ", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } else {
                        db.child("Penghuni").child(id).removeValue();

                        db.child("Penghuni").child(unama).child("id").setValue(uid);
                        db.child("Penghuni").child(unama).child("nama").setValue(unama);
                        db.child("Penghuni").child(unama).child("jk").setValue(ujk);
                        db.child("Penghuni").child(unama).child("asal").setValue(uasal);
                        db.child("Penghuni").child(unama).child("no_hp").setValue(uhp);
                        db.child("Penghuni").child(unama).child("tgl_msk").setValue(utglmsk);
                        db.child("Penghuni").child(unama).child("unit").setValue(uunit);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("status", unama);
                        db.child("Unit").child(uunit).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("status", status);
                                db.child("Unit").child(tunit).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(U_Penghuni.this, "Data Berhasil di Update!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        });
                    }                 }
            }
        });
    }
}