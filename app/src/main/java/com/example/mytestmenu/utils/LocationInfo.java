package com.example.mytestmenu.utils;

public class LocationInfo {
        private String longitude;//经度
        private String latitude;//纬度
        private String text;//信息内容
        public  String detailAddress;//详细地址 (搜索的关键字)

        public LocationInfo(String lon, String lat, String detailAddress, String text){
            this.longitude = lon;
            this.latitude = lat;
            this.text = text;
            this.detailAddress = detailAddress;


        }

        public String getLongitude() {
            return longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getText() {
            return text;
        }

        public String getDetailAddress() {
            return detailAddress;
        }


}
