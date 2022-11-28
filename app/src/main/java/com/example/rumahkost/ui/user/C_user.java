package com.example.rumahkost.ui.user;

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

public class C_user extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    TextView tid;
    EditText etUsername;
    EditText etPassword;
    EditText etConfirmPassword;
    Spinner spinner;
    Button btnDaftar;
    long maxid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuser);

        tid = findViewById(R.id.etid);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        spinner = findViewById(R.id.spinner);
        btnDaftar = findViewById(R.id.btnDaftar);

        db.child("User").addValueEventListener(new ValueEventListener() {
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

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getid = tid.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String cnf_pwd = etConfirmPassword.getText().toString();
                String status = spinner.getSelectedItem().toString();

                if(username.isEmpty()){
                    etUsername.setError("Masukan Username");
                } else if (password.length()<8){
                    etPassword.setError("Password Minimal 8 Karakter");
                }else if (password.isEmpty()){
                    etPassword.setError("Masukan Password");
                } else if (cnf_pwd.isEmpty()){
                    etConfirmPassword.setError("Masukan Konfirmasi Password");
                }else if (!password.equals(cnf_pwd)){
                    etConfirmPassword.setError("Password Tidak Sama");
                }else {
                    db.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(username)){
                                Toast.makeText(C_user.this, "Username Sudah Digunakan!", Toast.LENGTH_SHORT).show();
                            } else{
                                db.child("User").child(username).child("id").setValue(getid);
                                db.child("User").child(username).child("username").setValue(username);
                                db.child("User").child(username).child("password").setValue(password);
                                db.child("User").child(username).child("status").setValue(status);

                                Toast.makeText(C_user.this, "Data Berhasil Disimpan!",Toast.LENGTH_SHORT).show();
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