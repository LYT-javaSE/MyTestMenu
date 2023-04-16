package com.example.mytestmenu.entity_class;

import org.litepal.crud.LitePalSupport;

import java.sql.Timestamp;

public class Users extends LitePalSupport {
    private int id;
    private int userAge;
    private Timestamp birthDay;
    private String userName;
    private String userPhone;
    private String userPassword;
    private String userRePassword;
    private String userAvatar;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setBirthDay(Timestamp birthDay){this.birthDay=birthDay;}
    public Timestamp getBirthDay(){return birthDay;}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserRePassword(String userRePassword) {
        this.userRePassword = userRePassword;
    }

    public String getUserRePassword() {
        return userRePassword;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
    public String getUserAvatar() {
        return userAvatar;
    }
}
