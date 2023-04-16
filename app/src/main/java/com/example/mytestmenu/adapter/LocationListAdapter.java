package com.example.mytestmenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.mytestmenu.R;
import com.example.mytestmenu.utils.LocationInfo;

import java.util.List;
public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.Holder> {

    private Context context;
    private List<LocationInfo> data;
//    private OnItemClickListener onItemClickListener;

    public LocationListAdapter(Context context, List<LocationInfo> data){
        this.context =context;
        this.data = data;
    }



    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_location, null);
        Holder holder = new Holder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.tv_adress.setText(data.get(position).getDetailAddress());
        holder.tv_content.setText(data.get(position).getText());

//        // 绑定数据到列表项
//        AddressInfo poi = data.get(position);
//        // 设置列表项的点击事件
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemClickListener != null) {
//                    // 触发点击事件，传递点击的 POI 数据
//                    onItemClickListener.onItemClick(poi);
//                }
//            }
//        });
    }
//    // 设置点击事件监听器
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
//    // 点击事件监听器接口
//    public interface OnItemClickListener {
//        void onItemClick(AddressInfo poi);
//    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        private TextView tv_adress,tv_content;
        public Holder(View itemView) {
            super(itemView);
            tv_adress = (TextView) itemView.findViewById(R.id.tv_adress);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }



}
