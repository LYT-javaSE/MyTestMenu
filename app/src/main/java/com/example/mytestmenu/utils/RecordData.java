package com.example.mytestmenu.utils;

import java.util.Date;

public class RecordData {
//    挂号人姓名
        private String patientName;
        private String hospital;
        private String office;
        public  String doctorName;
//        预约的挂号时间
        public String registerDate;

        public String getPatientName() {
            return patientName;
        }
        public void setPatientName(String patientName){this.patientName=patientName;}
        public String getHospital() {
            return hospital;
        }
        public void setHospital(String hospital) {this.hospital = hospital;}
        public String getOffice() {
        return office;
    }
        public void setOffice(String office) {this.office = office;}
        public String getDoctorName() {
            return doctorName;
        }
        public void setDoctorName(String doctorName){this.doctorName=doctorName;}
        public String getRegisterDate(){return registerDate;}
        public void setRegisterDate(String registerDate){this.registerDate=registerDate;}



}
