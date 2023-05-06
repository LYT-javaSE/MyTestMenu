package com.example.mytestmenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestmenu.R;
import com.example.mytestmenu.entity_class.Hospitals;

import java.util.List;

public class RegHosAdapter extends RecyclerView.Adapter<RegHosAdapter.MyViewHolder> {

    private List<Hospitals> mHospitals;
    public RegHosAdapter(List<Hospitals> hospitals) {
        this.mHospitals = hospitals;
    }

    @Override
    public RegHosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView1.setText(mHospitals.get(position).getName());
        holder.mTextView2.setText(mHospitals.get(position).getAddress());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public MyViewHolder(View v) {
            super(v);
            mTextView1 = v.findViewById(R.id.hospital_name);
            mTextView2=v.findViewById(R.id.hospital_address);
        }
    }

    @Override
    public int getItemCount() {
        return mHospitals.size();
    }
}
