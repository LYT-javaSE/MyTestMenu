package com.example.mytestmenu.adapter;

import android.annotation.SuppressLint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestmenu.R;

import com.example.mytestmenu.utils.AdminData;

import java.util.List;

public class AdminInfoAdapter extends RecyclerView.Adapter<AdminInfoAdapter.MyViewHolder>{


    private List<AdminData> mAdminList;

    public AdminInfoAdapter(List<AdminData> adminData) {
        this.mAdminList = adminData;
    }

    @Override
    public AdminInfoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, parent, false);
        AdminInfoAdapter.MyViewHolder vh = new AdminInfoAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdminInfoAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mTextView1.setText(mAdminList.get(holder.getAdapterPosition()).getAdminName());
        holder.mTextView2.setText(mAdminList.get(holder.getAdapterPosition()).getAdminPhone());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public MyViewHolder(View v) {
            super(v);
            mTextView1 = v.findViewById(R.id.admin_name);
            mTextView2 = v.findViewById(R.id.admin_phone);
        }
    }

    @Override
    public int getItemCount() {
        return mAdminList.size();
    }

}

