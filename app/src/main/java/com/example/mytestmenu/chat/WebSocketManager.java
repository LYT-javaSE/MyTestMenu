package com.example.mytestmenu.chat;

import static com.example.mytestmenu.activity.LoginActivity.isPatient;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.mytestmenu.UserManager;
import com.example.mytestmenu.activity.BeforeInquiryActivity;
import com.example.mytestmenu.activity.ChatActivity;
import com.example.mytestmenu.entity_class.MsgContent;
import com.example.mytestmenu.utils.ActivityCollector;
import com.google.gson.Gson;

import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.litepal.LitePal;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WebSocketManager {
    private static final String SERVER_URI = "ws://10.0.2.2:9091/ws";
    private static JWebSocketClient webSocketClient;

    //登录者手机号,已修改
    public static String user_id;
    static UserManager userManager = UserManager.getInstance();

    public static void initializeWebSocket() {
        if (webSocketClient == null) {
            try {
                URI uri = URI.create(SERVER_URI);
                webSocketClient = new JWebSocketClient(uri) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        super.onOpen(handshakedata);
                        Log.d("WebSocketManager", "连接正在打开。。。");
                    }
                    @Override
                    public void onMessage(String message) {
                        Log.d("WebSocketManager", "收到的消息体: " + message);
                        Gson gson=new Gson();
                        MsgContent msgContent=gson.fromJson(String.valueOf(message), MsgContent.class);
                        String recipient_msg = msgContent.getContent();//获取到消息
                        Log.d("WebSocketManager", "获得的消息内容: "+recipient_msg);
                        //本次新增逻辑
                        Log.d("aaaaaaaaaaaaa", "mid的值: "+msgContent.getMid());
                        Log.d("aaaaaaaaaaaaa", "id的值: "+msgContent.getId());
                        Cursor cursor= LitePal.findBySQL("select * from MsgContent where mid = ?",String.valueOf(msgContent.getMid()));   //获取全局id主键，判断本机是否存在该mid数据
                        //不存在该mid，属于正常新增数据，可以保存
                        if (cursor.moveToFirst()==false) {
                            if (isPatient) {
                                System.out.println("当前为患者为发送方："+isPatient);
                                user_id = userManager.getUserPhone();
                            } else {
                                System.out.println("当前为医生为发送方"+isPatient);
                                user_id = userManager.getDoctPhone();
                            }
                            if (msgContent.getSender_id().equals(user_id)){
                                msgContent.setIshost("1");
                            }else {
                                msgContent.setIshost("0");
                            }
                            msgContent.save();
                            Log.d("保存了",String.valueOf(msgContent.getId()));
                        }
                        cursor.close();
                        //使用handle的消息机制，触发ui更新。
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (ActivityCollector.isActivityExist(ChatActivity.class)){ //判断聊天会话界面是否正在打开状态，是的话，就可以刷新页面了。
                                Log.d("yyyyyyyyy", "聊天会话界面是否正在打开状态(true/false): "+ActivityCollector.isActivityExist(ChatActivity.class));
                                ChatActivity.initMsg();  //chatActicvity中的msglist添加逻辑
                                ChatActivity.adapter.notifyDataSetChanged();
                                Log.d("uuuuuuuuu", "onMsg，正在刷新界面......");
                            }
//                            新消息到来时，用户端显示红点
//                            if (isPatient) {
//                                try {
//                                    BeforeInquiryActivity.mAdapter.notifyDataSetChanged();  //此处是消息列表页消息通知刷新，消息列表页就是我们微信，QQ那个好友消息列表页，我们还没画这个界面，所以暂且不提，注释掉，后面再说。
//                                }catch (Exception e){
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                System.out.println("当前为医生为发送方"+isPatient);//随便写的
//                            }

                        });
                    }
                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.d("WebSocketManager", "连接关闭: " + reason);
                    }
                    @Override
                    public void onError(Exception ex) {
                        Log.e("WebSocketManager", "WebSocket error: " + ex.getMessage());
                    }
                };
                connect();
            } catch (Exception e) {
                Log.e("WebSocketManager", "初始化失败: " + e.getMessage());
            }
        }
    }
    private static void connect() {
        new Thread() {
            @Override
            public void run() {
                Log.d("提示","开始连接socket");
                try {
                    webSocketClient.connectBlocking();
                    if (webSocketClient.isOpen()) {
                        if (isPatient) {
                            System.out.println("用户建立连接："+isPatient);
                            user_id = userManager.getUserPhone();
                        } else {
                            System.out.println("医生建立连接"+isPatient);
                            user_id = userManager.getDoctPhone();
                        }
                        webSocketClient.send("qwertyuiopasdfghjkl:"+user_id);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("Error","socket连接发送失败: "+e);
                }
            }
        }.start();
    }

    public static JWebSocketClient getWebSocketClient() {
        return webSocketClient;
    }

    public static void sendMessage(String message) {
        if (null != webSocketClient) {
            try {
                webSocketClient.send(message);
            } catch (WebsocketNotConnectedException e) {
                Log.e("长连接发送失败", String.valueOf(e));
            }
            Log.d("发送的消息", message);
        }
    }
}
