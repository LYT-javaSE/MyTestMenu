package com.example.mytestmenu.entity_class;

import org.litepal.crud.LitePalSupport;

public class Users extends LitePalSupport {
    private int id;
    private int userAge;
    private String  birthDay;
    private String userName;
    private String userPhone;
    private String userSex;
    private String userPassword;
    private String userRePassword;


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

    public void setBirthDay(String birthDay){this.birthDay=birthDay;}
    public String getBirthDay(){return birthDay;}

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

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserSex() {
        return userSex;
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

}
