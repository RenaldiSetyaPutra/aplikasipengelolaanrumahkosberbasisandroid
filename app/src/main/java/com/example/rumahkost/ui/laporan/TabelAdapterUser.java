package com.example.rumahkost.ui.laporan;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rumahkost.R;
import com.example.rumahkost.ui.user.Model_User;

import java.util.List;

public class TabelAdapterUser extends RecyclerView.Adapter<TabelAdapterUser.MyViewHolder> {
    private List<Model_User> list;
    private Activity activity;

    public TabelAdapterUser(List<Model_User>list, Activity activity) {
        this.activity = activity;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = LayoutInflater.from(activity).inflate(R.layout.tabel_user, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == 0){
            holder.id.setText("ID");
            holder.username.setText("Username");
            holder.password.setText("Password");
            holder.status.setText("Status");

        }else {
            final Model_User usr = list.get(position-1);
            holder.id.setText(usr.getId());
            holder.username.setText(usr.getUsername());
            holder.password.setText(usr.getUsername());
            holder.status.setText(usr.getStatus());
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView id, username, password, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            username = itemView.findViewById(R.id.username);
            password = itemView.findViewById(R.id.password);
            status   = itemView.findViewById(R.id.status);
        }
    }
}

