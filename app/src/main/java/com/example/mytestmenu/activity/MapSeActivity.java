////package com.example.mytestmenu.activity;
////
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.recyclerview.widget.LinearLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////
////import android.os.Bundle;
////import android.text.Editable;
////import android.view.View;
////import android.widget.EditText;
////
////import com.amap.api.maps.model.LatLng;
////import com.amap.api.maps.model.Poi;
////import com.amap.api.services.core.LatLonPoint;
////import com.amap.api.services.help.Inputtips;
////import com.amap.api.services.help.InputtipsQuery;
////import com.amap.api.services.help.Tip;
////import com.example.mytestmenu.R;
//////import com.example.mytestmenu.adapter.RvAdapter;
////
////import java.util.ArrayList;
////import java.util.List;
////
////public class MapSeActivity extends AppCompatActivity {
////
////
////    private EditText editText;
////    private RecyclerView recyclerView;
////    private RvAdapter rvAdapter;
////    private AMapNavi aMapNavi;
////
////
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_map_se);
////
////        editText=findViewById(R.id.edit_query);
////        editText.addTextChangedListener(this);
//////
//////
////        recyclerView.findViewById(R.id.recyclearview);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////
////        rvAdapter=new RvAdapter(this,recyclerView,new ArrayList<>());
////        rvAdapter.setOnItemClickListener(this);
////        recyclerView.setAdapter(rvAdapter);
//////
//////
////        Inputtips inputTips = new Inputtips(this,(InputtipsQuery)null);
////        inputTips.setInputtipsListener(this);
////
////
////        aMapNavi=AMapNavi.getInstance(this);
////        aMapNavi.setUseInnerVoice(true,false);
//////
//////
////    }
////
////    @Override
////    public void onGetInputtips(final List<Tip> tipList, int i) {
////        //通过tipList获取Tip信息
////        rvAdapter.setData(tipList);
////    }
//////
//////
////    @Override
////    public void beforeTextChanged(CharSequence s,int start,int count,int after) {
////
////    }
//////
//////
//////
////    @Override
////    public void onTextChanged(CharSequence s,int start,int before,int count) {
////        //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
////        InputtipsQuery inputquery = new InputtipsQuery(String.valueOf(s),"");
////        inputquery.setCityLimit(true);//限制在当前城市
////        inputTips.setQuery(inputquery);
////        inputTips.requestInputtipsAsyn();
////    }
//////
//////
////
////    @Override
////    public void afterTextChanged(Editable s) {
////
////    }
//////
////    @Override
////    public void OnItemClick(RecyclerView parent, View v,int position,Tip data){
////        LatLonPoint point=data.getPoint();
////        Poi poi=new Poi(data.getName(),new LatLng(point.getLatitude(),point.getLongitude(),data.getPoiID()));
////        AmapNaviParams params=new AmapNaviParams(null,null,poi,AmapNaviType.DRIVER,AmapPageType.Route);
////        AmapPageType.getInstance().MapSeActivity(getApplicationContext(),params,null);
////   }
////
////}
//
//
//package com.example.mytestmenu.activity;
//import android.Manifest;
//import android.annotation.TargetApi;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.amap.api.services.core.AMapException;
//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.core.PoiItem;
//import com.amap.api.services.core.SuggestionCity;
//import com.amap.api.services.poisearch.PoiResult;
//import com.amap.api.services.poisearch.PoiSearch;
//import com.example.mytestmenu.R;
//import com.example.mytestmenu.adapter.LocationListAdapter;
//import com.example.mytestmenu.entity_class.AddressInfo;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MapSeActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener{
////    private EditText mEt_keyword;
////    private RecyclerView listView;
//
////    private static final int REQUEST_PERMISSION_LOCATION = 0;
////    private String keyWord = "";// 要输入的poi搜索关键字
////    private PoiResult poiResult; // poi返回的结果
////    private int currentPage = 0;// 当前页面，从0开始计数
////    private PoiSearch.Query query;// Poi查询条件类
////    private PoiSearch poiSearch;// POI搜索
////
////    private List<AddressInfo> mData = new ArrayList<>();
////    private LocationListAdapter listAdapter;
//
//
//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map_se);
////        mEt_keyword = (EditText) findViewById(R.id.edit_sear);
////        listView = (RecyclerView) findViewById(R.id.rv_map_se);
////        listView.setLayoutManager(new LinearLayoutManager(this));
//
//        //设置定位权限
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
//        } else {
//            //监听EditText输入
//            initListener();
//        }
//    }
//
//
////    private void initListener() {
////        mEt_keyword.addTextChangedListener(new TextWatcher() {
////            @Override
////            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////            }
////
////            @Override
////            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////                keyWord = String.valueOf(charSequence);
////                if ("".equals(keyWord)) {
////                    Toast.makeText(MapSeActivity.this,"请输入搜索关键字",Toast.LENGTH_SHORT).show();
////                    return;
////                } else {
////                    try {
////                        doSearchQuery(keyWord);
////                    } catch (AMapException e) {
////                        throw new RuntimeException(e);
////                    }
////                }
////            }
////
////            @Override
////            public void afterTextChanged(Editable editable) {
////
////            }
////        });
////    }
////
////    /**
////     * 开始进行poi搜索
////     */
////    protected void doSearchQuery(String key) throws AMapException {
////        currentPage = 0;
////        //不输入城市名称有些地方搜索不到
////        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
////        query = new PoiSearch.Query(key, "", "");
////        // 设置每页最多返回多少条poiitem
////        query.setPageSize(10);
////        // 设置查询页码
////        query.setPageNum(currentPage);
////        //构造 PoiSearch 对象，并设置监听
////        poiSearch = new PoiSearch(this, query);
////        poiSearch.setOnPoiSearchListener(this);
////        //调用 PoiSearch 的 searchPOIAsyn() 方法发送请求。
////        poiSearch.searchPOIAsyn();
////    }
////
////    /**
////     * POI信息查询回调方法
////     */
////    @Override
////    public void onPoiSearched(PoiResult result, int rCode) {
////        //rCode 为1000 时成功,其他为失败
////        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
////            // 解析result   获取搜索poi的结果
////            if (result != null && result.getQuery() != null) {
////                if (result.getQuery().equals(query)) {  // 是否是同一条
////                    poiResult = result;
////                    ArrayList<AddressInfo> data = new ArrayList<AddressInfo>();//自己创建的数据集合
////                    // 取得第一页的poiitem数据，页数从数字0开始
////                    //poiResult.getPois()可以获取到PoiItem列表
////                    List<PoiItem> poiItems = poiResult.getPois();
////
////                    //若当前城市查询不到所需POI信息，可以通过result.getSearchSuggestionCitys()获取当前Poi搜索的建议城市
////                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();
////                    //如果搜索关键字明显为误输入，则可通过result.getSearchSuggestionKeywords()方法得到搜索关键词建议。
////                    List<String> suggestionKeywords =  poiResult.getSearchSuggestionKeywords();
////
////                    //解析获取到的PoiItem列表
////                    for(PoiItem item : poiItems){
////                        //获取经纬度对象
////                        LatLonPoint llp = item.getLatLonPoint();
////                        double lon = llp.getLongitude();
////                        double lat = llp.getLatitude();
////                        //返回POI的名称
////                        String title = item.getTitle();
////                        //返回POI的地址
////                        String text = item.getSnippet();
////                        data.add(new AddressInfo(String.valueOf(lon), String.valueOf(lat), title, text));
////                    }
////                    listAdapter = new LocationListAdapter(this, data);
////                    listView.setAdapter(listAdapter);
////                }
////            } else {
////                Toast.makeText(MapSeActivity.this,"无搜索结果",Toast.LENGTH_SHORT).show();
////            }
////        } else {
////            Toast.makeText(MapSeActivity.this,"错误码"+rCode,Toast.LENGTH_SHORT).show();
////        }
////
////    }
////    /**
////     * POI信息查询回调方法
////     */
////    @Override
////    public void onPoiItemSearched(PoiItem item, int rCode) {
////
////    }
//}

package com.example.mytestmenu.activity;


import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;

public  class MapSeActivity {
    /**
     * 路线规划
     *
     * @param slat 起点纬度
     * @param slon 起点经度
     * @param dlat 终点纬度
     * @param dlon 终点经度
     */
    public void navigation(Context context, double slat, double slon, double dlat, double dlon) {
        Poi start = null;
        //如果设置了起点
        if (slat != 0 && slon != 0) {
            start = new Poi("起点名称", new LatLng(slat, slon), "");
        }
        Poi end = new Poi("终点名称", new LatLng(dlat, dlon), "");
        AmapNaviParams params = new AmapNaviParams(start, null, end, AmapNaviType.DRIVER);
        params.setUseInnerVoice(true);
        params.setMultipleRouteNaviMode(true);
        params.setNeedDestroyDriveManagerInstanceWhenNaviExit(true);
        //发起导航
        AmapNaviPage.getInstance().showRouteActivity(context, params, null);
    }

}