package com.example.rumahkost.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserFragment extends Fragment {
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rumah-kost-61e15-default-rtdb.asia-southeast1.firebasedatabase.app/");
    RecyclerView rv;
    ArrayList<Model_User> listUser;
    Adapter_User adapter;
    FloatingActionButton tambah;
    ImageView edit, delete;
    EditText cari;
    CharSequence Search ="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        edit   = view.findViewById(R.id.edit);
        delete = view.findViewById(R.id.delete);
        tambah = view.findViewById(R.id.add);
        rv     = view.findViewById(R.id.tampil);
        cari   = view.findViewById(R.id.cari);

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    Search(s.toString());
                }else{
                    Search("");
                }
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        RefreshList();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), C_user.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void Search(String s) {
        db.child("User").orderByChild("username").startAt(s).endAt(s + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    listUser.clear();
                    for(DataSnapshot dss : dataSnapshot.getChildren()){
                        final Model_User usr = dss.getValue(Model_User.class);
                        listUser.add(usr);
                    }
                    Adapter_User adapter = new Adapter_User(listUser, getActivity());
                    rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else {
                    cari.setError("Data Tidak Ditemukan!");
                    listUser.clear();
                    Adapter_User adapter = new Adapter_User(listUser, getActivity());
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        RefreshList();
    }

    public void RefreshList() {
        db.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUser = new ArrayList<>();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Model_User usr = ds.getValue(Model_User.class);

                    listUser.add(usr);
                    adapter = new Adapter_User(listUser, getActivity());
                    rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }
}