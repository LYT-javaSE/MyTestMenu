package com.example.mytestmenu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestmenu.R;
import com.example.mytestmenu.entity_class.Users;

import java.util.List;

public class DealUserMsgAdapter extends RecyclerView.Adapter<DealUserMsgAdapter.MyViewHolder>{

    private Context mContext;
    private List<Users> mUsers;
    private DealUserMsgAdapter.ItemClickListener mItemClickListener; //添加接口
    public DealUserMsgAdapter(List<Users> users, DealUserMsgAdapter.ItemClickListener listener) {
        this.mUsers = users;
        this.mItemClickListener = listener;
    }

    public DealUserMsgAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public DealUserMsgAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        MyViewHolder vh = new DealUserMsgAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mTextView1.setText(mUsers.get(holder.getAdapterPosition()).getUserName());
        holder.mTextView2.setText(mUsers.get(holder.getAdapterPosition()).getUserSex());
        holder.mTextView3.setText(String.valueOf(mUsers.get(holder.getAdapterPosition()).getUserAge()));

        holder.itemView.setOnClickListener(v -> mItemClickListener.onItemClick(mUsers.get(position)));
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public MyViewHolder(View v) {
            super(v);
            mTextView1 = v.findViewById(R.id.tv_user_name);
            mTextView2 = v.findViewById(R.id.tv_user_sex);
            mTextView3 = v.findViewById(R.id.tv_user_age);
        }
    }

    public int getItemCount() {
        return mUsers.size();
    }

    //定义接口
    public interface ItemClickListener {
        void onItemClick(Users users);
    }

}
