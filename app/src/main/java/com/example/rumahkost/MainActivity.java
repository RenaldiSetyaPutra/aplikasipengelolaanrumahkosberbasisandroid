package com.example.rumahkost;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    EditText etusername;
    EditText etpassword;
    TextView lupatext;
    Button logbtn;
    Activity activity;


    @Override    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;

        etusername = (EditText) findViewById(R.id.username);
        etpassword = (EditText) findViewById(R.id.password);
        logbtn = (Button) findViewById(R.id.loginButton);
        lupatext = (TextView) findViewById(R.id.lupa);

        logbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final String cuser = etusername.getText().toString();
                final String cpass = etpassword.getText().toString();

                if(cuser.isEmpty()){
                    etusername.setError("Masukan Username");
                } else if (cpass.isEmpty()){
                    etpassword.setError("Masukan Password");
                } else {

                    db.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(cuser)){
                                final String getPass = dataSnapshot.child(cuser).child("password").getValue(String.class);
                                final String getStatus = dataSnapshot.child(cuser).child("status").getValue(String.class);
                                if(getPass.equals(cpass)){
                                    if (getStatus.equals("Admin")) {
                                        startActivity(new Intent(MainActivity.this, Homee.class));
                                        Toast.makeText(MainActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else if (getStatus.equals("Pemilik")) {
                                        startActivity(new Intent(MainActivity.this, Homee_Pemilik.class));
                                        Toast.makeText(MainActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        startActivity(new Intent(MainActivity.this, Homee_Penghuni.class));
                                        Toast.makeText(MainActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();

                                        finish();
                                    }
                                }else{
                                    Toast.makeText(MainActivity.this,"Login Gagal!",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(MainActivity.this,"Login Gagal!",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

        lupatext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Keluar Aplikasi");
                builder.setMessage("Yakin Keluar?");
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        activity.finishAffinity();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
}