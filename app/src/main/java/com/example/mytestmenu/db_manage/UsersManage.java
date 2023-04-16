//package com.example.mytestmenu.db_manage;
//
//import com.example.mytestmenu.entity_class.Users;
//
//import org.litepal.LitePal;
//
//import java.util.List;
//
//public class UsersManage {
//
////    注册用的
//    public boolean insertUser(String name,String phone,String pwd){
//        Users users = new Users();
//        users.setUserName(name);
//        users.setUserPhone(phone);
//        users.setUserPassword(pwd);
//        LitePal.getDatabase();
//        return users.save();
//    }
//
//
////    登录用的
//
//
////    验证用的
//    public boolean findByPhone(String phone){
//        List users = LitePal.findAll(Users.class);
//        for(Users person:users){
//            if(phone.trim().equals(person.getUserPhone())){
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//
//
//}
