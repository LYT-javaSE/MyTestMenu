import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.example.mytestmenu.fragment.HomeFragment;

//package com.example.mytestmenu.adapter;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.viewpager.widget.PagerAdapter;
//
//import com.example.mytestmenu.R;
//
//import java.util.List;
//
//public class ViewPagerAdapter extends PagerAdapter {
//
//    Context context;
//    List<Integer> list;
//
//    public VpAdapter(Context context, List list){
//        this.context=context;
//        this.list=list;
//    }
//
//
//    /**
//     * 创建，即引入布局图片资源，往容器里添加视图
//     * @param container The containing View in which the page will be shown.
//     * @param position The page position to be instantiated.
//     * @return
//     */
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        View view=View.inflate(context, R.layout.fragment_home,null);
//        ImageView imageView=view.findViewById(R.id.vpimg);
//        imageView.setImageResource(list.get(position));
//        container.addView(view);
//        return view;
//    }
//    /**
//     * 销毁（即挪走上一个）
//     * @param container The containing View from which the page will be removed.
//     * @param position The page position to be removed.
//     * @param object The same object that was returned by
//     * {@link #instantiateItem(View, int)}.
//     */
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeView((View) object);
//    }
//
//
//
//    @Override
//    public int getCount() {
////        这里决定viewpager的页数,一般是传入list的长度
//        return list.size();
//    }
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view==object;
//    }
//
//
//
//}


