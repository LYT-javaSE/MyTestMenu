package com.example.mytestmenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestmenu.R;
import com.example.mytestmenu.utils.Msg;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Msg> mMsgList;
    private Context context;
    public MsgAdapter(List<Msg> mMsgList,Context context) {
        this.mMsgList = mMsgList;
        this.context=context;
    }
    //在该方法中将msg_item布局加载进来，然后创建一个ViewHolder实例，
    //并把加载出来的布局传入到构造函数中，最后将ViewHolder实例返回。
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == Msg.TYPE_SEND) {
            View senderView = inflater.inflate(R.layout.item_sender_msg, parent, false);
            return new SenderViewHolder(senderView);
        } else if(viewType == Msg.TYPE_RECEIVED) {
            View receiverView = inflater.inflate(R.layout.item_receiver_msg, parent, false);
            return new ReceiverViewHolder(receiverView);
        }else {
            return null;
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Msg msgL = mMsgList.get(position);
        if (holder instanceof SenderViewHolder) {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.tvSenderMessage.setText(msgL.getContent());
        } else if (holder instanceof ReceiverViewHolder) {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            receiverViewHolder.tvReceiverMessage.setText(msgL.getContent());
        }
    }
    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
    @Override
    public int getItemViewType(int position) {
        Msg message = mMsgList.get(position);
        if (message.getType() == Msg.TYPE_SEND) {
            return Msg.TYPE_SEND;
        } else if (message.getType() == Msg.TYPE_RECEIVED) {
            return Msg.TYPE_RECEIVED;
        }
        return super.getItemViewType(position);
    }

    static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView tvSenderMessage;
        SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSenderMessage = itemView.findViewById(R.id.right_msg);
        }
    }

    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView tvReceiverMessage;
        ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReceiverMessage = itemView.findViewById(R.id.left_msg);
        }
    }

}
