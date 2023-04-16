package com.example.mytestmenu.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
    private Button send;

    private MsgAdapter msgAdapter;
    private List<Msg> msgList = new ArrayList<Msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        inputText = (EditText)findViewById(R.id.input_text);
        send = (Button)findViewById(R.id.send);
        msgRecyclerView = (RecyclerView)findViewById(R.id.rv_inquiry);

        //初始化消息数据
        initMsgs();

        //LayoutManager用于执行RecyclerView的布局方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(msgAdapter);
        send.setOnClickListener(this);
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
        if (!"".equals(content)) {
            Msg msg = new Msg(content, Msg.TYPE_SEND);
            msgList.add(msg);
            //当有新消息时，刷新RecyclerView中的显示
            msgAdapter.notifyItemInserted(msgList.size() - 1);
            //将RecyclerView定位到最后一行
            msgRecyclerView.scrollToPosition(msgList.size() - 1);
            //清空输入框中的内容
            inputText.setText("");
        }
    }
}