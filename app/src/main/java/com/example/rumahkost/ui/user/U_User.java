package com.example.rumahkost.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class U_User extends AppCompatActivity {
    private String KEY_NAME = "nama";
    Button btn;
    TextView text1, text2, text3, tid, tuser;
    Spinner spinner;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uuser);
        DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
        tid   = findViewById(R.id.tid);
        tuser = findViewById(R.id.etUsername);
        text1 = findViewById(R.id.newUsername);
        text2 = findViewById(R.id.newPassword);
        text3 = findViewById(R.id.newConfirmPassword);
        spinner = findViewById(R.id.spinner);

        tuser.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        id = extras.getString(KEY_NAME);

        db.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(id)) {
                    final String getId = dataSnapshot.child(id).child("id").getValue(String.class);
                    final String getUsername = dataSnapshot.child(id).child("username").getValue(String.class);
                    final String getPass = dataSnapshot.child(id).child("password").getValue(String.class);
                    final String getStatus = dataSnapshot.child(id).child("status").getValue(String.class);

                    tid.setText(getId);
                    text1.setText(getUsername);

                    tuser.setText(getUsername);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn = findViewById(R.id.btnUuser);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String uid     = tid.getText().toString();
                String user    = tuser.getText().toString();
                String uuser   = text1.getText().toString();
                String upass   = text2.getText().toString();
                String ustatus = spinner.getSelectedItem().toString();

                if(uuser.isEmpty()){
                    text1.setError("Masukan Nama");
                } else if (upass.isEmpty()){
                    text2.setError("Masukan Password");
                }else {
                    if(uuser.equals(id)) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("password", text2.getText().toString());
                        map.put("status", spinner.getSelectedItem().toString());

                        db.child("User").child(user).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(U_User.this, "Data Berhasil di Update", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(U_User.this, "Data Gagal di Update ", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } else {
                        db.child("User").child(id).removeValue();

                        db.child("User").child(uuser).child("id").setValue(uid);
                        db.child("User").child(uuser).child("username").setValue(uuser);
                        db.child("User").child(uuser).child("password").setValue(upass);
                        db.child("User").child(uuser).child("status").setValue(ustatus);

                        Toast.makeText(U_User.this, "Data Berhasil di Update", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
}