package com.example.mytestmenu.entity_class;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class Offices extends LitePalSupport{

    private int id;
    private String officeName;
    private List<Hospitals> hospitalsList = new ArrayList<>();





    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setOfficeName(String doctNum) {
        this.officeName = officeName;
    }

    public String  getOfficeName() {
        return officeName;
    }



    public List<Hospitals> getHospitalsList() {
        return hospitalsList;
    }

    public void setHospitalsList(List<Hospitals> hospitalsList) {
        this.hospitalsList = hospitalsList;
    }

//    @Override
//    public String toString() {
//        return "offices{" +
//                "id=" + id +
//                ", 科室名='" + officeName + '\'' +
//                ", 所属医院列表=" + hospitalsList +
//                '}';
//    }

    }


