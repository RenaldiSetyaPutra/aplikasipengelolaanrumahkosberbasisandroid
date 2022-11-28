package com.example.rumahkost.ui.laporan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.example.rumahkost.ui.user.Model_User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LaporanUser extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    RecyclerView rv;
    ArrayList<Model_User> listUser;
    TabelAdapterUser adapter;
    Button btnpdf;
    int pageWidth = 2480;
    int pageHeight = 3580;
    int no = 1;
    String id, username, password, status;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_user);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        rv = findViewById(R.id.rvtabeluser);
        rv.setLayoutManager(new LinearLayoutManager(LaporanUser.this));
        RefreshList();

        btnpdf = findViewById(R.id.btnpdf);
        btnpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void buatPDF() {
    }

    private void RefreshList() {
        db.child("User").orderByChild("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUser = new ArrayList<>();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Model_User usr = ds.getValue(Model_User.class);
                    listUser.add(usr);
                    no = no++;
                    id = usr.getId();
                    username = usr.getUsername();
                    password = usr.getPassword();
                    status   = usr.getStatus();
                }
                adapter = new TabelAdapterUser(listUser, LaporanUser.this);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}