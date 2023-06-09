package com.example.mytestmenu.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mytestmenu.R;
import com.example.mytestmenu.activity.BeforeInquiryActivity;
import com.example.mytestmenu.activity.MapActivity;
import com.example.mytestmenu.activity.RegHospitalActivity;
import com.example.mytestmenu.activity.ShowDoctorListActivity;
import com.example.mytestmenu.activity.ShowRecordActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    private String phone;
    private View mView;
    private ViewPager mViewPaper;
    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
    //记录上一次点的位置
    private int oldPosition = 0;
    //存放图片的id
    private int[] imageIds = new int[]{
            R.drawable.bg1,
            R.drawable.bg2,
            R.drawable.bg1,
            R.drawable.bg2
    };
    //存放图片的标题
    private String[] titles = new String[]{
            "轮播1",
            "轮播2",
            "轮播3",
            "轮播4",
    };
    private TextView title;
    private ViewPagerAdapter adapter;
    private ScheduledExecutorService scheduledExecutorService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_home, null);
        setView();

        Bundle bundle=getArguments();
        if (bundle != null) {
            phone=bundle.getString("phone");
            Log.d("接收Main", "onCreateView: "+phone);
        }

//        跳转到地图
        ImageView imgView=mView.findViewById(R.id.img_address);
        imgView.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),MapActivity.class);
            getActivity().startActivity(intent);
        });
//         跳转到挂号
        ImageView imgView1=mView.findViewById(R.id.guahao);
        imgView1.setOnClickListener(v -> {
            Intent intent1=new Intent(getContext(), RegHospitalActivity.class);
            intent1.putExtra("userPhone",phone);
            Log.d("home传递","显示——————"+phone);
            getActivity().startActivity(intent1);
        });
//         跳转到问诊
        ImageView imgView2=mView.findViewById(R.id.wenzhen);
        imgView2.setOnClickListener(v -> {
            Intent intent2=new Intent(getContext(), BeforeInquiryActivity.class);
            getActivity().startActivity(intent2);
        });


        //         跳转到记录
        ImageView imgView3=mView.findViewById(R.id.record);
        imgView3.setOnClickListener(v -> {
            Intent intent3=new Intent(getContext(), ShowRecordActivity.class);
            intent3.putExtra("userPhone",phone);
            Log.d("home传递", "显示——*******"+phone);
            getActivity().startActivity(intent3);
        });


        //         跳转到查询
        ImageView imgView4=mView.findViewById(R.id.chaxun);
        imgView4.setOnClickListener(v -> {
            Intent intent4=new Intent(getContext(), ShowDoctorListActivity.class);
            getActivity().startActivity(intent4);
        });



        return mView;
    }

    private void setView(){
        mViewPaper = (ViewPager)mView.findViewById(R.id.vpban);

        //显示的图片
        images = new ArrayList<ImageView>();
        for(int i = 0; i < imageIds.length; i++){
            ImageView imageView = new ImageView(getActivity());
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }
        //显示的小点
        dots = new ArrayList<View>();
        dots.add(mView.findViewById(R.id.dot_0));
        dots.add(mView.findViewById(R.id.dot_1));
        dots.add(mView.findViewById(R.id.dot_2));
        dots.add(mView.findViewById(R.id.dot_3));

        title = (TextView) mView.findViewById(R.id.title);
        title.setText(titles[0]);

        adapter = new ViewPagerAdapter();
        mViewPaper.setAdapter(adapter);

        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                title.setText(titles[position]);
                dots.get(position).setBackgroundResource(R.drawable.dot_now);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_later);
                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /*定义的适配器*/
    public class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return images.size();
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            // TODO Auto-generated method stub
//          super.destroyItem(container, position, object);
//          view.removeView(view.getChildAt(position));
//          view.removeViewAt(position);
            view.removeView(images.get(position));
        }
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            // TODO Auto-generated method stub
            view.addView(images.get(position));
            return images.get(position);
        }
    }

    /**
     * 利用线程池定时执行动画轮播
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                2,
                2,
                TimeUnit.SECONDS);
    }

    /**
     * 图片轮播任务
     *
     */
    private class ViewPageTask implements Runnable{
        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 接收子线程传递过来的数据
     */
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        };
    };

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }
}