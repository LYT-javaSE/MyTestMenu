package com.example.mytestmenu.activity;

import com.amap.api.navi.AMapNavi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
//import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.autonavi.base.amap.mapcore.AMapNativeGlOverlayLayer;
import com.example.mytestmenu.R;
import com.example.mytestmenu.adapter.LocationListAdapter;
import com.example.mytestmenu.utils.LocationInfo;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener{
    private MapView mMapView = null;
    private AMap aMap = null;
    private MyLocationStyle myLocationStyle=null;

    private AMapNavi aMapNavi;
    private EditText mEt_keyword;
    private RecyclerView listView;
    private String keyWord = "";// 要输入的poi搜索关键字
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private List<LocationInfo> data = new ArrayList<>();
    private LocationListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mEt_keyword = (EditText) findViewById(R.id.tv_search);
        listView = (RecyclerView) findViewById(R.id.rv_map);
//        调整图标大小
        Drawable drawable = getResources().getDrawable(R.drawable.sousuo);
        drawable.setBounds(0, 0, 40, 40);//第一个 0 是距左边距离，第二个 0 是距上边距离，40 分别是长宽
        mEt_keyword.setCompoundDrawables(drawable , null, null, null);//只放左边

        //Java合规接口
        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this, true);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //方法自5.1.0版本后支持
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//显示POI搜索结果
        listView.setLayoutManager(new LinearLayoutManager(this));
        initListener();





//
////        导航实现
//        try {
//            aMapNavi=AMapNavi.getInstance(this);
//        } catch (com.amap.api.maps.AMapException e) {
//            throw new RuntimeException(e);
//        }
//        aMapNavi.setUseInnerVoice(true,false);
//
//        // 为POI列表项设置点击事件监听器
//        listAdapter=new LocationListAdapter(this,data);
//        listAdapter.setOnItemClickListener(poi->{
//                // 获取点击的POI数据，跳转到导航界面
//                startMapSeActivity(poi);
//        });



//        mLocationOption = new AMapLocationClientOption();
//        /**
//         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
//         */
//        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
////        if (null != locationClient) {
////            locationClient.setLocationOption(mLocationOption);
////            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
////            locationClient.stopLocation();
////            locationClient.startLocation();
////        }
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//        mLocationOption.setInterval(1000);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation amapLocation) {
//                if (amapLocation != null) {
//                    if (amapLocation.getErrorCode() == 0) {
////可在其中解析amapLocation获取相应内容。
//                    } else {
//                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                        Log.e("AmapError", "location Error, ErrCode:"
//                                + amapLocation.getErrorCode() + ", errInfo:"
//                                + amapLocation.getErrorInfo());
//                    }
//                }
//                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//
//            }
//        };


//        try {
//            mOfflineMapManager = new OfflineMapManager(this, this);
//        }catch (Exception e){
//
//        }
//        if (mOfflineMapManager != null){
//            // TODO
//        }//


//        try {
//            mAMapNavi = AMapNavi.getInstance(getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (mAMapNavi != null){
//// TODO
//        }
    }


//    private void startMapSeActivity(LocationInfo poi) {
//
////构建导航组件配置类，没有传入起点，所以起点默认为 “我的位置”
//        AmapNaviParams params = new AmapNaviParams(null, null, null, AmapNaviType.DRIVER, AmapPageType.ROUTE);
////启动导航组件
//        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, null);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    private void initListener() {
        mEt_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                keyWord = String.valueOf(charSequence);
                if ("".equals(keyWord)) {
                    Toast.makeText(MapActivity.this,"请输入搜索关键字",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    try {
                        doSearchQuery(keyWord);
                    } catch (AMapException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String key) throws AMapException {
        currentPage = 0;
        //不输入城市名称有些地方搜索不到
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(key, "", "");
        // 设置每页最多返回多少条poiitem
        query.setPageSize(10);
        // 设置查询页码
        query.setPageNum(currentPage);
        //构造 PoiSearch 对象，并设置监听
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        //调用 PoiSearch 的 searchPOIAsyn() 方法发送请求。
        poiSearch.searchPOIAsyn();
    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        //rCode 为1000 时成功,其他为失败
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            // 解析result   获取搜索poi的结果
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(query)) {  // 是否是同一条
                    poiResult = result;

                    // 取得第一页的poiitem数据，页数从数字0开始
                    //poiResult.getPois()可以获取到PoiItem列表
                    List<PoiItem> poiItems = poiResult.getPois();

                    //若当前城市查询不到所需POI信息，可以通过result.getSearchSuggestionCitys()获取当前Poi搜索的建议城市
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();
                    //如果搜索关键字明显为误输入，则可通过result.getSearchSuggestionKeywords()方法得到搜索关键词建议。
                    List<String> suggestionKeywords =  poiResult.getSearchSuggestionKeywords();

                    //解析获取到的PoiItem列表
                    for(PoiItem item : poiItems){
                        //获取经纬度对象
                        LatLonPoint llp = item.getLatLonPoint();
                        double lon = llp.getLongitude();
                        double lat = llp.getLatitude();
                        //返回POI的名称
                        String title = item.getTitle();
                        //返回POI的地址
                        String text = item.getSnippet();
                        data.add(new LocationInfo(String.valueOf(lon), String.valueOf(lat), title, text));
                    }
                    listAdapter = new LocationListAdapter(this, data);
                    listView.setAdapter(listAdapter);
                }
            } else {
                Toast.makeText(MapActivity.this,"无搜索结果",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MapActivity.this,"错误码"+rCode,Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {

    }




//
//    public void onClickListener(RecyclerView parent,View view,int position,AddressInfo data) {
////        Poi poi=new Poi(data.getText(),new LatLng(point.getLatitude(),point.getLongitude(),data.getPoiID()));
////        AmapNaviParams params=new AmapNaviParams(null,null,poi,AmapNaviType.DRIVER,AmapPageType.Route);
////        AmapPageType.getInstance().MapSeActivity(getApplicationContext(),params,null);
//    }
}
