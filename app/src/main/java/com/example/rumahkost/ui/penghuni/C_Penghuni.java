package com.example.rumahkost.ui.penghuni;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rumahkost.R;
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

public class C_Penghuni extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    Button btn;
    RadioGroup rg;
    String  rb;
    Spinner spinner;
    EditText text1, text3, text4, text5;
    EditText date;
    TextView tid;
    long maxid = 0;
    ArrayList<String> spinnerlist;
    ArrayAdapter<String> adapter;
    DatePickerDialog datePickerDialog;
    String unit;
    String status = "Kosong";
    Button btndate;
    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy",id);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpenghuni);

        // initiate the date picker and a button
        date = (EditText) findViewById(R.id.date);
        btndate = findViewById(R.id.btndate);
        // perform click event on edit text
        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(C_Penghuni.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        date.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        tid = findViewById(R.id.etid);
        text1 = findViewById(R.id.et1);
        rg =  findViewById(R.id.radiogrup);
        text3 = findViewById(R.id.et3);
        text4 = findViewById(R.id.et4);
        text5 = findViewById(R.id.date);
        spinner = findViewById(R.id.spinner);
        btndate = findViewById(R.id.btndate);

        db.child("Penghuni").addValueEventListener(new ValueEventListener() {
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

        db.child("Unit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnerlist = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    String  unt = item.child("nama").getValue(String.class);
                    if (dataSnapshot.hasChild(unt)) {
                        final String getstatus = dataSnapshot.child(unt).child("status").getValue(String.class);
                            if(getstatus.equals(status)){
                                spinnerlist.add(unt);
                            }
                    }

                }
                adapter = new ArrayAdapter<String>(C_Penghuni.this, android.R.layout.simple_spinner_dropdown_item, spinnerlist);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
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

        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String getid     = tid.getText().toString();
                String getnama   = text1.getText().toString();
                String getjk     = rb;
                String getasl    = text3.getText().toString();
                String getnohp   = text4.getText().toString();
                String gettglmsk = text5.getText().toString();
                String getunit   = spinner.getSelectedItem().toString();

                if(getnama.isEmpty()){
                    text1.setError("Masukan Nama");
                } else if (getasl.isEmpty()){
                    text3.setError("Masukan Asal");
                }else if (getnohp.isEmpty()){
                    text4.setError("Masukan Nomor Handphone");
                } else if (gettglmsk.isEmpty()){
                    text5.setError("Masukan Tanggal Masuk");
                }else {
                    db.child("Penghuni").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(getnama)) {
                                    text1.setError("Nama Sudah Ada!");
                                } else if (getunit.equals(unit)) {
                                    Toast.makeText(C_Penghuni.this, "Unit Sudah Ditempati!", Toast.LENGTH_SHORT).show();
                                } else {
                                    db.child("Penghuni").child(getnama).child("id").setValue(getid);
                                    db.child("Penghuni").child(getnama).child("nama").setValue(getnama);
                                    db.child("Penghuni").child(getnama).child("jk").setValue(getjk);
                                    db.child("Penghuni").child(getnama).child("asal").setValue(getasl);
                                    db.child("Penghuni").child(getnama).child("no_hp").setValue(getnohp);
                                    db.child("Penghuni").child(getnama).child("tgl_msk").setValue(gettglmsk);
                                    db.child("Penghuni").child(getnama).child("unit").setValue(getunit);


                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("status", getnama);
                                            db.child("Unit").child(getunit).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(C_Penghuni.this, "Data Berhasil Disimpan!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });

                                }
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