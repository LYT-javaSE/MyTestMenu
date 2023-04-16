package com.example.mytestmenu.entity_class;

import org.litepal.crud.LitePalSupport;

public class Doctors extends LitePalSupport {
    private int id;
    private int doctNum;
    private int doctAge;
    private String doctName;
    private String doctPhone;
    private String doctPassword;
    private String doctRePassword;
    private String doctTile;
    private String doctOfOffice;
    private String doctOfHospital;
    private String doctTalent;
    private String doctAvatar;





    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDoctNum(int doctNum) {this.doctNum = doctNum;}

    public int getDoctNum() {
        return doctNum;
    }

    public void setDoctAge(int doctAge) {
        this.doctAge = doctAge;
    }

    public int getDoctAge() {
        return doctAge;
    }

    public void setDoctName(String doctName) {
        this.doctName = doctName;
    }

    public String getDoctName() {
        return doctName;
    }

    public void setDoctPhone(String doctPhone) {
        this.doctPhone = doctPhone;
    }

    public String getDoctPhone() {
        return doctPhone;
    }

    public void setDoctPassword(String doctPassword) {
        this.doctPassword = doctPassword;
    }

    public String getDoctPassword() {
        return doctPassword;
    }

    public void setDoctRePassword(String doctRePassword) {this.doctRePassword = doctRePassword;}

    public String getDoctRePassword() {
        return doctRePassword;
    }

    public void setDoctTile(String doctTile) {
        this.doctTile = doctTile;
    }

    public String getDoctTile() {
        return doctTile;
    }

    public void setDoctOfOffice(String doctOfOffice) {
        this.doctOfOffice = doctOfOffice;
    }

    public String getDoctOfOffice() {
        return doctOfOffice;
    }

    public void setDoctOfHospital(String doctOfHospital) {
        this.doctOfHospital = doctOfHospital;
    }

    public String getDoctOfHospital() {
        return doctOfHospital;
    }

    public void setDoctTalent(String doctTalent) {this.doctTalent = doctTalent;}

    public String getDoctTalent() {
        return doctTalent;
    }

    public void setDoctAvatar(String doctAvatar) {this.doctAvatar = doctAvatar;}

    public String getDoctAvatar() {
        return doctAvatar;
    }
}
