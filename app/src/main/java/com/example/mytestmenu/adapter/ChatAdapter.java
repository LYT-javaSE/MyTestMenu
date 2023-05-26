package com.example.mytestmenu.adapter;

import static com.example.mytestmenu.activity.LoginActivity.isPatient;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestmenu.R;
import com.example.mytestmenu.UserManager;
import com.example.mytestmenu.entity_class.MsgContent;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<MsgContent> mImMsgContentList;
    private Context mContext;
    private String user_id; //这里是全局获取
    static UserManager userManager = UserManager.getInstance();


    public Context getmContext(){
        return mContext;
    }
    public ChatAdapter(List<MsgContent> msgContent) {
        mImMsgContentList = msgContent;   //获取activity传入的消息list
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();  //获取上下文
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);  //获取卡片对象
        final ChatAdapter.ViewHolder holder = new ChatAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //recyclerView对应每行对象，position对应位置坐标
        final MsgContent imMsgContent = mImMsgContentList.get(position);


        Log.d("ssssssss{此时的}", "Sender_id: "+imMsgContent.getSender_id());
        if (isPatient) {
            System.out.println("目前是用户："+isPatient);
            user_id = userManager.getUserPhone();
            // 处理患者手机号逻辑
        } else {
            System.out.println("目前是医生"+isPatient);
            user_id = userManager.getDoctPhone();
        }
        //显示己方右侧卡片
        if(imMsgContent.getSender_id().equals(user_id)) {
            holder.leftLayout.setVisibility(View.GONE);  //左侧layout隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);  //右侧layout显示
            if (imMsgContent.getMsg_type() == 0) {   //等于1是图片消息，0是文字消息
                //完成加载图片逻辑
//                holder.rightPhoto.setVisibility(View.GONE);
                holder.rightMessage.setVisibility(View.VISIBLE);
                holder.rightMsg.setText(imMsgContent.getContent());
                holder.leftTimeTextView.setVisibility(View.GONE);
                holder.rightTimeTextView.setVisibility(View.VISIBLE);
                holder.rightTimeTextView.setText(imMsgContent.getCreate_time());
            } else {
            //完成加载图片逻辑
//            holder.rightPhoto.setVisibility(View.VISIBLE);
            holder.rightMessage.setVisibility(View.GONE);
            }
        }
        //显示对方左侧卡片，，，判断该消息发出用户是否为当前用户,若不是当前用户
        else if(imMsgContent.getIshost().equals("0")) {
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.VISIBLE);
            //等于1是图片消息，0是文字消息
            if (imMsgContent.getMsg_type() == 0) {
            // 完成加载图片逻辑
//            holder.leftPhoto.setVisibility(View.GONE);
                holder.leftMessage.setVisibility(View.VISIBLE);
                holder.leftMsg.setText(imMsgContent.getContent());
                holder.leftTimeTextView.setVisibility(View.VISIBLE);
                holder.rightTimeTextView.setVisibility(View.GONE);
                holder.leftTimeTextView.setText(imMsgContent.getCreate_time());
            }
        } else {
        //完成加载图片逻辑
//          holder.leftPhoto.setVisibility(View.VISIBLE);
            holder.leftMessage.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout ,leftMessage;  //左布局,左消息框
        LinearLayout rightLayout ,rightMessage;  //右布局，右消息框
        TextView leftMsg;  //左消息
        TextView rightMsg;  //右消息
        TextView leftTimeTextView;  // 左侧时间视图
        TextView rightTimeTextView; // 右侧时间视图
        CircleImageView rightAvater,leftAvater;  //左右头像  本期不予实现
        public ViewHolder(View view) {
            super(view);
            leftAvater=(CircleImageView)view.findViewById(R.id.left_avater);
            rightAvater=(CircleImageView)view.findViewById(R.id.right_avater);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMessage= (LinearLayout) view.findViewById(R.id.message_left);
            rightMessage= (LinearLayout) view.findViewById(R.id.message_right);
            leftMsg = (TextView) view.findViewById(R.id.chat_left_msg);
            rightMsg = (TextView) view.findViewById(R.id.chat_right_msg);
            leftTimeTextView=(TextView) view.findViewById(R.id.chat_left_time);
            rightTimeTextView=(TextView) view.findViewById(R.id.chat_right_time);
        }
    }

    @Override
    public int getItemCount() {
        return mImMsgContentList.size();
    }

//    private void initAvater(){
//        //加载头像
//    }

}


