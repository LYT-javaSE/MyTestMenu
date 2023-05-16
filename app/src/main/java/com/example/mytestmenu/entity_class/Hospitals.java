package com.example.mytestmenu.entity_class;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class Hospitals extends LitePalSupport {
    private int id;
    private String name;
    private String office;
    private String address;
    private List<Offices> officesList = new ArrayList<>();


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String hospitalName) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setOffice(String office) {
        this.office = office;
    }
    public String getOffice() {
        return office;
    }

    public void setAddress(String address) {this.address = address;}
    public String getAddress() {
        return address;
    }

    public List<Offices> getOfficesList() {
        return officesList;
    }
    public void setOfficesList(List<Offices> officesList) {
        this.officesList = officesList;
    }
}
