package com.example.mytestmenu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestmenu.R;
import com.example.mytestmenu.entity_class.Doctors;
import com.example.mytestmenu.entity_class.MsgContent;
import com.example.mytestmenu.fragment.DocMineFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class RegDoctAdapter extends RecyclerView.Adapter<RegDoctAdapter.MyViewHolder> {

    private Context mContext;
    private List<Doctors> mDoctors;
    private ItemClickListener mItemClickListener; //添加接口
    public RegDoctAdapter(List<Doctors> doctors, ItemClickListener listener) {
        this.mDoctors = doctors;
        this.mItemClickListener = listener;
    }

    public RegDoctAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public RegDoctAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctors, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mTextView1.setText(mDoctors.get(holder.getAdapterPosition()).getDoctName());
        holder.mTextView2.setText(mDoctors.get(holder.getAdapterPosition()).getDoctOfHospital());
        holder.mTextView3.setText(mDoctors.get(holder.getAdapterPosition()).getDoctOfOffice());
        holder.mTextView4.setText(mDoctors.get(holder.getAdapterPosition()).getDoctTile());

        //添加itemView的点击事件监听器
        holder.itemView.setOnClickListener(v -> mItemClickListener.onItemClick(mDoctors.get(position)));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
//        public ImageView mImageView5;
        public MyViewHolder(View v) {
            super(v);
            mTextView1 = v.findViewById(R.id.tvName);
            mTextView2 = v.findViewById(R.id.tvHospital);
            mTextView3=v.findViewById(R.id.tvOffice);
            mTextView4 = v.findViewById(R.id.tvTitle);
//            mImageView5 = v.findViewById(R.id.red_dot);
        }
    }

    @Override
    public int getItemCount() {
        return mDoctors.size();
    }
    //定义接口
    public interface ItemClickListener {
        void onItemClick(Doctors doctors);
    }
}
