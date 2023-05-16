package com.example.mytestmenu.adapter;

import android.annotation.SuppressLint;
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
    private ItemClickListener mItemClickListener; //添加接口
    public RegHosAdapter(List<Hospitals> hospitals, ItemClickListener listener) {
        this.mHospitals = hospitals;
        this.mItemClickListener = listener;
    }

    @Override
    public RegHosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mTextView1.setText(mHospitals.get(holder.getAdapterPosition()).getName());
        holder.mTextView2.setText(mHospitals.get(holder.getAdapterPosition()).getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() { //添加itemView的点击事件监听器
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(mHospitals.get(position));
            }
        });
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
    //定义接口
    public interface ItemClickListener {
        void onItemClick(Hospitals hospital);
    }
}
