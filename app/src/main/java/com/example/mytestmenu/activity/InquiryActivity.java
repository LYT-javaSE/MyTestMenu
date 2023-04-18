package com.example.mytestmenu.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mytestmenu.R;
import com.example.mytestmenu.adapter.MsgAdapter;
import com.example.mytestmenu.utils.Msg;

import java.util.ArrayList;
import java.util.List;

public class InquiryActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView msgRecyclerView;
    private EditText inputText;
    private Button btnSend;

    private MsgAdapter msgAdapter;
    private List<Msg> msgList = new ArrayList<Msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        inputText = (EditText)findViewById(R.id.input_text);
        btnSend = (Button)findViewById(R.id.send);
        msgRecyclerView = (RecyclerView)findViewById(R.id.rv_inquiry);

        //初始化消息数据
        initMsgs();

        //LayoutManager用于执行RecyclerView的布局方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(msgList,this);
        msgRecyclerView.setAdapter(msgAdapter);
        btnSend.setOnClickListener(this);
    }

    private void initMsgs() {
        Msg msg1 = new Msg("hello guy.", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("hello. who is that?",Msg.TYPE_SEND);
        msgList.add(msg2);
        Msg msg3 = new Msg("this is tom. nice talking to you. ", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }

    @Override
    public void onClick(View view) {
        String content = inputText.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            // 将输入的消息添加到消息数据中
            Msg msg = new Msg(content, Msg.TYPE_SEND);
            msgList.add(msg);
            // 刷新 RecyclerView，显示新添加的消息
            msgAdapter.notifyItemInserted(msgList.size() - 1);
            // 模拟接收方回复一条消息
            Msg receiverMessage = new Msg("自动回复：这是一条接收方的消息", Msg.TYPE_RECEIVED);
            msgList.add(receiverMessage);
            // 清空输入文本框
            inputText.setText("");
            // 在添加新消息后，将 RecyclerView 滚动到最后一条消息
            msgAdapter.notifyItemInserted(msgList.size() - 1);
            msgRecyclerView.smoothScrollToPosition(msgList.size() - 1);
        }
    }
}