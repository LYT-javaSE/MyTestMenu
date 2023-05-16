package com.example.mytestmenu.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestmenu.R;
import com.example.mytestmenu.entity_class.Offices;

import java.util.List;

public class RegOffAdapter extends RecyclerView.Adapter<RegOffAdapter.MyViewHolder> {

    private List<Offices> mOffices;
    private ItemClickListener mItemClickListener; //添加接口
    public RegOffAdapter(List<Offices> offices, ItemClickListener listener) {
        this.mOffices = offices;
        this.mItemClickListener = listener;
    }

    @Override
    public RegOffAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_office, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mTextView.setText(mOffices.get(holder.getAdapterPosition()).getOffice());
        //添加itemView的点击事件监听器
        holder.itemView.setOnClickListener(v -> mItemClickListener.onItemClick(mOffices.get(position)));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public MyViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.office_name);
        }
    }

    @Override
    public int getItemCount() {
        return mOffices.size();
    }
    //定义接口
    public interface ItemClickListener {
        void onItemClick(Offices office);
    }
}
