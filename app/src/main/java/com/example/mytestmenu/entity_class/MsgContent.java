package com.example.mytestmenu.entity_class;

import org.litepal.crud.LitePalSupport;


public class MsgContent extends LitePalSupport {

    private int mid;  //所有消息id序号，服务器数据库全局自增主键
    private int id;   //消息id序号
    private String cid;  //会话发送方、接收方拼接id，用于会话识别判断,格式为c_{}_to_{}
    private String content;//会话内容
    private String sender_id;
    private String recipient_id;
    private int msg_type;//文本或图片， 0：文字， 1：图片
    private String create_time;//发送一条消息的当前时间
    private String ishost; //该消息发出用户是否为当前用户,服务端数据不存在该字段
//    private boolean hasNewMessage;

    public int getMid() {
        return mid;
    }
    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public String getCid() {
        return cid;
    }
    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getSender_id() {
        return sender_id;
    }
    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getRecipient_id() {
        return recipient_id;
    }
    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public int getMsg_type() {
        return msg_type;
    }
    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public String getCreate_time() {
        return create_time;
    }
    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIshost() {
        return ishost;
    }
    public void setIshost(String ishost) {
        this.ishost = ishost;
    }
}

