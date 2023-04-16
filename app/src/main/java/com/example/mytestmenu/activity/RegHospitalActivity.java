package com.example.mytestmenu.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.mytestmenu.R;

public class RegHospitalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_hospital);
        openDialog("一、预约范围\n" +
                "\n" +
                "1.1 挂号平台提供次日起七天的预约服务，用户可预约中医院的大部分科室次日起至七天的就诊号源。具体的预约挂号周期和号源信息，以网站公示为准。\n" +
                "\n" +
                "二、实名制注册预约\n" +
                "\n" +
                "2.1 挂号平台采取实名制注册，用户首次预约必须注册就诊人的真实有效基本信息，包括就诊人员的真实姓名、有效证件号（身份证）、性别、电话、手机号码等基本信息（如已是居民健康卡用户，通过身份证号验证后，设置好密码便可完成注册）。\n" +
                "\n" +
                "2.2 用户凭本人有效身份证和手机短信到医院取号就诊。\n" +
                "\n" +
                "2.3 挂号平台注册，1个手机号只能关联1个有效证件号（一个账号能够关联5个常用就诊人）。\n" +
                "\n" +
                "三、就诊人信息管理\n" +
                "\n" +
                "3.1 挂号平台不支持修改证件号码。\n" +
                "\n" +
                "四、预约确认与就诊\n" +
                "\n" +
                "4.1 预约成功后，平台会自动下发预约成功短信到用户注册的手机上\n" +
                "\n" +
                "4.2 如果您预约成功后，请妥善保存短信信息，以便随时查询，同时也是您用于取号就诊的有效凭证之一。\n" +
                "\n" +
                "4.3 如果您未收到或不慎丢失“预约成功短信”，可登录挂号平台网站，在“个人中心 - 我的预约”中查找到相应的预约信息。\n" +
                "\n" +
                "4.4 成功预约后，平台将自动保存用户预约记录。就诊当天，您需要在医院规定的取号时间之内，前往医院取号就诊，并缴纳医院规定的相关费用，逾期预约自动作废。具体取号时间请以预约成功的短信通知时间为准。\n" +
                "\n" +
                "4.5 在医院取号时需要提供以下重要凭证：\n" +
                "\n" +
                "4.5.1 就诊当日取号时，就诊人需凭注册的有效身份证、预约成功通知短信至医院挂号窗口取号（个别医院如有特殊要求，请以医院实际要求为准）；取号时医务人员将核对就诊者的身份信息和预约记录。\n" +
                "\n" +
                "五、查询、取消预约\n" +
                "\n" +
                "5.1 用户在规定的取消时限内可以取消预约号，超过取消预约截止时间系统将限制用户取消预约，取消预约截止时间为就诊日期的前一天下午15:00；用户可登录挂号平台网站，在“个人中心 - 我的预约”中直接取消。\n" +
                "\n" +
                "5.2 用户登录挂号平台网站，在“个人中心 - 我的预约”中可查询到自己相关的预约信息。\n" +
                "\n" +
                "5.3 如果您在就诊当天不能前往医院取号就诊，请提前通过挂号平台取消预约，否则会因造成号源的浪费，影响其他患者的就诊而被记录进爽约档案，由此会影响到您今后的预约行为，请您谅解。\n" +
                "\n" +
                "六、预约限制\n" +
                "\n" +
                "为了防止号贩子倒号行为，满足绝大多数百姓预约就诊的公平性和最大利益，挂号平台采取了预约挂号限制。同一患者在同一就诊日、同一医院、同一科室只能预约1次；在同一就诊日的预约总量不可超过2次；在七日内的预约总量不可超过3次。\n" +
                "\n" +
                "七、爽约的判定及处罚\n" +
                "\n" +
                "7.1 爽约是指用户未按医院规定的取号时间到医院指定的挂号窗口取号，未能按时取号就诊即视为爽约，该预约号源自动作废。如仍需就诊可重新预约或到医院挂号窗口挂号。\n" +
                "\n" +
                "7.2 连续无故爽约累计达到3次的用户将自动进入系统爽约黑名单，此后3个月内将取消其预约挂号资格。\n" +
                "\n" +
                "八、特别声明\n" +
                "\n" +
                "8.1 本服务为用户提供便捷的预约方式，不承诺所有用户随时都能约到指定的号，有不便之处敬请谅解，有任何好的建议也可以直接向我们反馈。\n" +
                "\n","在线挂号须知");
    }


    //弹出对话框
    private void openDialog(String strMsg, String strTitle) {
        new AlertDialog.Builder(this)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        })
                .show();
    }

}