package com.example.mytestmenu.entity_class;

public class Guahao {
    private int id;
    private String patientName;
    private String sex;
    private int age;
    private String phone;
//    预约日期，而预约创建时间由服务端生成
    private String registerDate;
    private String hospital;
    private String office;
    private String doctorName;
    private String doctorNum;
//    用户账户人手机号
    private String userPhone;




    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setPatientName(String patientName) {this.patientName = patientName;}

    public String getPatientName() {
        return patientName;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public int getAge() {
        return age;
    }
    public void setPhone(String phone) {this.phone = phone;}

    public String getPhone() {
        return phone;
    }

    public void setRegisterDate(String registerDate) {this.registerDate = registerDate;}

    public String  getRegisterDate() {
        return registerDate;
    }
    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getHospital() {
        return hospital;
    }
    public void setOffice(String office) {this.office = office;}

    public String getOffice() {
        return office;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorNum(String doctorNum) {
        this.doctorNum = doctorNum;
    }

    public String getDoctorNum() {
        return doctorNum;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhone() {return userPhone;}
}
