package com.example.rumahkost.ui.unit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;

public class U_Unit extends AppCompatActivity {
    private String KEY_NAME = "nama";
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    Button btn;
    Spinner  spinner3, spinner4;
    EditText nama, namalama;
    TextView tid, spinner2;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uunit);
        tid = findViewById(R.id.etid);
        nama =  findViewById(R.id.etnama);
        namalama = findViewById(R.id.namalama);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        spinner4 = findViewById(R.id.spinner4);


        namalama.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        id = extras.getString(KEY_NAME);

        db.child("Unit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(id)) {
                    final String getId = dataSnapshot.child(id).child("id").getValue(String.class);
                    final String getNama = dataSnapshot.child(id).child("nama").getValue(String.class);
                    final String getStatus = dataSnapshot.child(id).child("status").getValue(String.class);

                    tid.setText(getId);
                    nama.setText(getNama);
                    spinner2.setText(getStatus);

                    namalama.setText(getNama);

                }
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
                String uid          = tid.getText().toString();
                String tnama        = namalama.getText().toString();
                String unama        = nama.getText().toString();
                String ufasilitas   = spinner3.getSelectedItem().toString();
                String uharga       = spinner4.getSelectedItem().toString();
                String ustatus      = spinner2.getText().toString();

                if(unama.isEmpty()){
                    nama.setError("Masukan Nama");
                } else {
                    db.child("Unit").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (unama.equals(id)) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("fasilitas", spinner3.getSelectedItem().toString());
                                map.put("harga", spinner4.getSelectedItem().toString());
                                map.put("status", spinner2.getText().toString());

                                db.child("Unit").child(tnama).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(com.example.rumahkost.ui.unit.U_Unit.this, "Data Berhasil di Update", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(com.example.rumahkost.ui.unit.U_Unit.this, "Data Gagal di Update ", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } else {
                                db.child("Unit").child(id).removeValue();

                                db.child("Unit").child(unama).child("id").setValue(uid);
                                db.child("Unit").child(unama).child("nama").setValue(unama);
                                db.child("Unit").child(unama).child("fasilitas").setValue(ufasilitas);
                                db.child("Unit").child(unama).child("harga").setValue(uharga);
                                db.child("Unit").child(unama).child("status").setValue(ustatus);

                                HashMap<String, Object> map1 = new HashMap<>();
                                map1.put("unit", unama);
                                db.child("Penghuni").child(ustatus).updateChildren(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(com.example.rumahkost.ui.unit.U_Unit.this, "Data Berhasil Diubah!", Toast.LENGTH_SHORT).show();
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