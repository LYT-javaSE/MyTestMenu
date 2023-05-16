package com.example.mytestmenu.adapter;

import android.annotation.SuppressLint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestmenu.R;

import com.example.mytestmenu.utils.RecordData;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder>{


    private List<RecordData> mRecordList;
    private RecordAdapter.ItemClickListener mItemClickListener; //添加接口
    public RecordAdapter(List<RecordData> recordData, RecordAdapter.ItemClickListener listener) {
        this.mRecordList = recordData;
        this.mItemClickListener = listener;
    }
    //在该方法中将msg_item布局加载进来，然后创建一个ViewHolder实例，
    //并把加载出来的布局传入到构造函数中，最后将ViewHolder实例返回。
    @Override
    public RecordAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        RecordAdapter.MyViewHolder vh = new RecordAdapter.MyViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(RecordAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mTextView1.setText(mRecordList.get(holder.getAdapterPosition()).getPatientName());
        holder.mTextView2.setText(mRecordList.get(holder.getAdapterPosition()).getHospital());
        holder.mTextView3.setText(mRecordList.get(holder.getAdapterPosition()).getOffice());
        holder.mTextView4.setText(mRecordList.get(holder.getAdapterPosition()).getDoctorName());
        holder.mTextView5.setText(mRecordList.get(holder.getAdapterPosition()).getRegisterDate());
        //添加itemView的点击事件监听器
        holder.itemView.setOnClickListener(v -> mItemClickListener.onItemClick(mRecordList.get(position)));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public MyViewHolder(View v) {
            super(v);
            mTextView1 = v.findViewById(R.id.text_name);
            mTextView2 = v.findViewById(R.id.text_hospital);
            mTextView3 = v.findViewById(R.id.text_department);
            mTextView4 = v.findViewById(R.id.text_doctor);
            mTextView5 = v.findViewById(R.id.text_time);
        }
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }


    public interface ItemClickListener {
        void onItemClick(RecordData recordData);
    }
}
