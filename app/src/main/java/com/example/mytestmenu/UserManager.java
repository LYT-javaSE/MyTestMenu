package com.example.mytestmenu;

public class UserManager {
    private static UserManager instance;
    private String userPhone;
    private String doctPhone;

    private UserManager() {
        // 私有构造函数，防止外部实例化
    }
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String phoneNumber) {
        this.userPhone = phoneNumber;
    }

    public String getDoctPhone() {
        return doctPhone;
    }

    public void setDoctPhone(String phoneNumber) {
        this.doctPhone = phoneNumber;
    }

}
