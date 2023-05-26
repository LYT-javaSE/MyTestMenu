package com.example.mytestmenu.activity;

import static com.example.mytestmenu.activity.LoginActivity.isPatient;
import static com.example.mytestmenu.chat.WebSocketManager.sendMessage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestmenu.R;
import com.example.mytestmenu.UserManager;
import com.example.mytestmenu.adapter.ChatAdapter;
import com.example.mytestmenu.entity_class.MsgContent;
import com.example.mytestmenu.utils.ActivityCollector;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private static List<MsgContent> imMsgContentList = new ArrayList<>();
//    分别表示医生名、所有消息id序号（即服务器数据库全局自增主键）、用户自己名
    private static String chat_toUser, chat_mid, chat_user;

    private static String dPhone,uPhone;
    private static String chatCode;
    private static RecyclerView recyclerView;
    private EditText chatInputText;
    private TextView tvName;
    private Button chatButton;
    private static LinearLayoutManager layoutManager;
    public static ChatAdapter adapter;

    static UserManager userManager = UserManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        Intent intent=getIntent();
        String doctName=intent.getStringExtra("doctor_name");
        String userName=intent.getStringExtra("user_name");

        dPhone=intent.getStringExtra("doctor_phone");
        uPhone=intent.getStringExtra("user_phone");
        if (dPhone != null && uPhone == null){
            //此处是当前会话的聊天对象用户code。此处应该由消息列表页传入
            chat_toUser=dPhone;
        }else {
            //此处是当前会话的聊天对象用户code。此处应该由消息列表页传入
            chat_toUser=uPhone;
        }
        tvName=findViewById(R.id.bt_name);
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycview);
        layoutManager = new LinearLayoutManager(this);
        chatButton = (Button) findViewById(R.id.chat_send);
        chatButton.setOnClickListener(this);
        chatInputText = (EditText) findViewById(R.id.chat_inputText);

        if (doctName != null && userName == null){
            tvName.setText(doctName);
        }else {
            tvName.setText(userName);
        }
        initMsg();
    }

    @SuppressLint("Range")
    public static void initMsg(){

        //初始化历史聊天数据
        imMsgContentList.clear(); //清空list中的数据，重新加载
        if (isPatient) {
            System.out.println("当前用户为‘用户’："+isPatient);
            chatCode = userManager.getUserPhone();
            System.out.println("用户是："+chatCode);
        } else {
            System.out.println("当前用户为‘医生’："+isPatient);
            chatCode = userManager.getDoctPhone();
            System.out.println("医生是："+chatCode);
        }

        Cursor cursor= LitePal.findBySQL("select * from MsgContent where sender_id in(?,?) and recipient_id in(?,?) order by mid",chat_toUser,chatCode,chat_toUser,chatCode);

        //获取当前用户code和对象用户code的所有聊天记录，按照mid排序
        if (cursor.moveToFirst()){   //如果存在数据
            do {
                MsgContent i=new MsgContent();
                i.setMid(Integer.parseInt(cursor.getString(cursor.getColumnIndex("mid"))));
                i.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                i.setCid(cursor.getString(cursor.getColumnIndex("cid")));
                i.setIshost(cursor.getString(cursor.getColumnIndex("ishost")));
                i.setCreate_time(cursor.getString(cursor.getColumnIndex("create_time")));
                i.setContent(cursor.getString(cursor.getColumnIndex("content")));
                i.setMsg_type(Integer.parseInt(cursor.getString(cursor.getColumnIndex("msg_type"))));
                i.setRecipient_id(cursor.getString(cursor.getColumnIndex("recipient_id")));
                i.setSender_id(cursor.getString(cursor.getColumnIndex("sender_id")));
                i.setCreate_time(cursor.getString(cursor.getColumnIndex("create_time")));
                imMsgContentList.add(i);  //获取数据信息添加到list中
            }while (cursor.moveToNext());
        }
        cursor.close();
        layoutManager.setStackFromEnd(true);  //将recyclerView的界面滑动到最后一行
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ChatAdapter(imMsgContentList);  //适配器加载list数据
        recyclerView.setAdapter(adapter);  //更新界面
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_send:
                if (!chatInputText.getText().toString().trim().equals("")) {
                    send();
                }
                break;
        }
    }
    private void send() {
        try {
            // 处理手机号逻辑
            if (isPatient) {
                System.out.println("发送者为用户："+isPatient);
                chat_user = userManager.getUserPhone();
            } else {
                System.out.println("发送者为医生："+isPatient);
                chat_user = userManager.getDoctPhone();
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            MsgContent sendIm = new MsgContent();

            int maxMid = LitePal.max(MsgContent.class, "mid", int.class);
            int maxId = LitePal.max(MsgContent.class, "id", int.class);
            System.out.println("最大mid的值是："+maxMid);
            System.out.println("最大id的值是："+maxId);
            // 设置新记录的自增列值为最大值加一
            sendIm.setMid(maxMid + 1);
            sendIm.setId(maxId + 1);

            sendIm.setCid("c_" + chat_user + "_to_" + chat_toUser);
            sendIm.setContent(chatInputText.getText().toString().trim());  //获取输入框文字
            sendIm.setCreate_time(df.format(new Date()));
            sendIm.setMsg_type(0);
            sendIm.setRecipient_id(chat_toUser);
            sendIm.setSender_id(chat_user);
            sendIm.save();
            JSONObject init = new JSONObject();
            init.put("mid",sendIm.getMid());
            init.put("id",sendIm.getId());
            init.put("content", sendIm.getContent());
            init.put("sender_id", sendIm.getSender_id());
            init.put("recipient_id", sendIm.getRecipient_id());
            init.put("msg_type", sendIm.getMsg_type());
            init.put("cid", sendIm.getCid());
            init.put("create_time", sendIm.getCreate_time());
            //调用长连接消息发送接口。并封装方法sendMsg;
            sendMessage(init.toString());
            layoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(layoutManager);  //将recyclerView列表滚动到最底部
            imMsgContentList.add(sendIm);  //将消息添加到消息list
            adapter = new ChatAdapter(imMsgContentList);  //更新消息list到recycler适配器
            recyclerView.setAdapter(adapter);
            chatInputText.setText("");//清空输入框数据，为下次输入做准备
            int position = adapter.getItemCount() - 1; // 获取最后一条消息的位置
            recyclerView.scrollToPosition(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        ActivityCollector.removeActivity(ChatActivity.this);
        finish();
        super.onBackPressed();
    }

}
