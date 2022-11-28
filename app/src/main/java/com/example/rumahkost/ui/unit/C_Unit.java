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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class C_Unit extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    Button btn;
    Spinner  spinner3, spinner4;
    EditText  nama;
    TextView spinner2;
    TextView tid;
    long maxid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cunit);

        tid = findViewById(R.id.etid);
        nama =  findViewById(R.id.etnama);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        spinner4 = findViewById(R.id.spinner4);

        spinner2.setText("Kosong");

        db.child("Unit").addValueEventListener(new ValueEventListener() {
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

        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String getid           = tid.getText().toString();
                String getnama         = nama.getText().toString();
                String getfasilitas    = spinner3.getSelectedItem().toString();
                String getharga        = spinner4.getSelectedItem().toString();
                String getstatus       = spinner2.getText().toString();

                if(getid.isEmpty()){
                    tid.setError("Masukan ID");
                }else {
                    db.child("Unit").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(getnama)){
                                Toast.makeText(C_Unit.this, "Unit Sudah Ada", Toast.LENGTH_SHORT).show();
                            } else{
                                db.child("Unit").child(getnama).child("id").setValue(getid);
                                db.child("Unit").child(getnama).child("nama").setValue(getnama);
                                db.child("Unit").child(getnama).child("fasilitas").setValue(getfasilitas);
                                db.child("Unit").child(getnama).child("harga").setValue(getharga);
                                db.child("Unit").child(getnama).child("status").setValue(getstatus);


                                Toast.makeText(C_Unit.this, "Data Berhasil Disimpan!",Toast.LENGTH_SHORT).show();
                                finish();
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