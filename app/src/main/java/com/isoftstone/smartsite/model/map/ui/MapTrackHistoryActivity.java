package com.isoftstone.smartsite.model.map.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.android.tu.loadingdialog.LoadingDailog;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.muckcar.MapMarkersVoBean;
import com.isoftstone.smartsite.model.dirtcar.activity.CameraDetailsActivity;
import com.isoftstone.smartsite.utils.DensityUtils;
import com.isoftstone.smartsite.utils.LogUtils;
import com.isoftstone.smartsite.utils.MapUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zw on 2017/11/19.
 */

public class MapTrackHistoryActivity extends BaseActivity implements View.OnClickListener, AMap.OnMarkerClickListener {

    private MapView mapView;
    private AMap aMap;
    private LatLng aotiLatLon = new LatLng(30.482348,114.514417);
    private HttpPost httpPost;

    private final int NO_DATA = 0x0001;
    private final int UPDATE_DATA = 0x0002;

    private String currentDate = "";
    private String today = "";

    private List<MapMarkersVoBean> mapMarkersVoBeans;
    private List<Marker> markerList = new ArrayList<>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NO_DATA:
                    loadingDailog.dismiss();
                    ToastUtils.showLong("没有获取到渣土车轨迹！");
                    removeGuiji();
                    break;
                case UPDATE_DATA:
                    loadingDailog.dismiss();
                    updateGuiji();
            }
        }
    };
    private TextView tv_date;
    private Marker startMarker;
    private Polyline polyline;
    private Marker endMarker;
    private TextView tv_device_number;
    private TextView tv_time;
    private TextView tv_address;
    private ImageView iv_look_pic;
    private PopupWindow mPopWindow;
    private Marker roundMarker;
    private float zoom = 13f;
    private LoadingDailog loadingDailog;

    private String licence;
    private MapMarkersVoBean currentBean;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_map_track_history;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        licence = getIntent().getStringExtra("licence");
        String time = getIntent().getStringExtra("time");
        if(!TextUtils.isEmpty(time)){
            currentDate = time;
            today = time;
        }else {
            getNowDate();
        }
        if(TextUtils.isEmpty(licence)){
            ToastUtils.showLong("没有获取到渣土车信息！");
        }
        httpPost = new HttpPost();

        initToolBar();
        initView();
        initLoadDialog();
        initMapView(savedInstanceState);
        initLocation(aotiLatLon);
        getData();
        addRoundLine();
    }
    public void addRoundLine(){
        List<LatLng> latLngs = MapUtils.getAroundLatlons();
        Polyline polyline = aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.parseColor("#3464dd")));
    }
    private void initToolBar(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(licence);
    }

    private void initView(){
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(currentDate);
        View truck_back = findViewById(R.id.truck_back);
        View truck_next = findViewById(R.id.truck_next);
        truck_back.setOnClickListener(this);
        truck_next.setOnClickListener(this);
    }

    private void initLoadDialog(){
        loadingDailog = new LoadingDailog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(true)
                .setCancelOutside(true).create();
    }

    private void initMapView(Bundle savedInstanceState){
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setOnMarkerClickListener(this);
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                zoom = cameraPosition.zoom;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    private void initLocation(LatLng latLng){
        CameraPosition mCameraPosition = new CameraPosition(latLng,zoom,0,0);

        CameraUpdate update = CameraUpdateFactory.newCameraPosition(mCameraPosition);
        if(aMap != null){
            aMap.animateCamera(update);
        }

    }

    private void getData(){
        loadingDailog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mapMarkersVoBeans = httpPost.getMapMarkers(licence,currentDate);
                if(mapMarkersVoBeans == null || mapMarkersVoBeans.size() == 0){
                    mHandler.sendEmptyMessage(NO_DATA);
                }else{
                    mHandler.sendEmptyMessage(UPDATE_DATA);
                }
            }
        }).start();
    }



    private void removeGuiji(){
        if(startMarker != null){
            startMarker.remove();
        }
        if(endMarker != null){
            endMarker.remove();
        }
        if(polyline != null){
            polyline.remove();
        }
        for (int i = 0; i < markerList.size(); i++) {
            Marker marker = markerList.get(i);
            marker.remove();
        }
    }

    private void addGuiji(){
        addStartMarker();
        if(mapMarkersVoBeans != null && mapMarkersVoBeans.size() > 1){
            addEndMarker();
        }
        addTrail();
    }

    private void updateGuiji(){
        removeGuiji();
        addGuiji();
    }

    private void addStartMarker(){
        MapMarkersVoBean bean = mapMarkersVoBeans.get(0);
        LatLng latLng = new LatLng(bean.getLatitude(),bean.getLongitude());
        initLocation(latLng);

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.blueround));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        startMarker = aMap.addMarker(markerOption);
        startMarker.setAnchor(0.5f,0.5f);
    }

    private void addEndMarker(){
        MapMarkersVoBean bean = mapMarkersVoBeans.get(mapMarkersVoBeans.size() - 1);
        LatLng latLng = new LatLng(bean.getLatitude(),bean.getLongitude());

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.blueround));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        endMarker = aMap.addMarker(markerOption);
        endMarker.setAnchor(0.5f,0.5f);
    }

    private void addTrail(){
        //经过描线
        List<LatLng> latLngs = new ArrayList<LatLng>();
        for (int i = 0; i < mapMarkersVoBeans.size(); i++) {
            MapMarkersVoBean bean = mapMarkersVoBeans.get(i);
            LatLng latLng = new LatLng(bean.getLatitude(),bean.getLongitude());
            latLngs.add(latLng);
            addMarker(latLng,bean);
        }

        polyline = aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(20).color(Color.parseColor("#4f4de6")));
    }

    private void addMarker(LatLng latLng,MapMarkersVoBean bean){
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.camera_normal));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        Marker marker = aMap.addMarker(markerOption);
        marker.setObject(bean);
        startMarker.setAnchor(0.5f,0.5f);
        markerList.add(marker);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.truck_back:
                updateDate(0);
                break;
            case R.id.truck_next:
                updateDate(1);
                break;
            case R.id.iv_look_pic:
                //跳转到CameraDetailsActivity
                Intent intent = new Intent(this, CameraDetailsActivity.class);
                intent.putExtra("licence",licence);
                intent.putExtra("date",currentDate); //时间格式为：2017-11-19
                intent.putExtra("device_coding",currentBean.getDeviceCoding());
                intent.putExtra("device_address",currentBean.getAddr());
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getObject() != null){
             currentBean = (MapMarkersVoBean) marker.getObject();
            if(mPopWindow == null){
                initPopWindow();
            }else {
                tv_device_number.setText(currentBean.getDeviceCoding());
                tv_time.setText("拍摄时间：" + currentBean.getInstallTime().substring(0,10));
                tv_address.setText(currentBean.getAddr());
                LatLng latLng = new LatLng(currentBean.getLatitude(),currentBean.getLongitude());
                initLocation(latLng);
                addRoundMarker(latLng);
                mPopWindow.showAtLocation(mapView, Gravity.BOTTOM,0, DensityUtils.dip2px(this,-8));
            }
        }
        return true;
    }

    private void initPopWindow(){
        View popWindowView = LayoutInflater.from(this).inflate(R.layout.layout_map_truck_popwindow,null);
        tv_device_number = (TextView) popWindowView.findViewById(R.id.tv_device_number);
        tv_time = (TextView) popWindowView.findViewById(R.id.tv_time);
        tv_address = (TextView) popWindowView.findViewById(R.id.tv_address);
        iv_look_pic = (ImageView) popWindowView.findViewById(R.id.iv_look_pic);
        iv_look_pic.setOnClickListener(this);

        mPopWindow = new PopupWindow(this);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setContentView(popWindowView);
        mPopWindow.setOutsideTouchable(false);
        mPopWindow.setFocusable(true);
        mPopWindow.setTouchable(true);
        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(roundMarker != null){
                    roundMarker.remove();
                }
            }
        });

    }

    private void addRoundMarker(LatLng latLng){
        MarkerOptions markerOption1 = new MarkerOptions();

        markerOption1.position(latLng);

        markerOption1.visible(true);

        markerOption1.draggable(false);//设置Marker可拖动
        View centerView = LayoutInflater.from(this).inflate(R.layout.layout_map_task_backround,null);
        markerOption1.icon(BitmapDescriptorFactory.fromView(centerView));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption1.setFlat(true);//设置marker平贴地图效果

        roundMarker = aMap.addMarker(markerOption1);
        roundMarker.setAnchor(0.5f,0.5f);
    }


    public void getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = formatter.format(currentTime);
        today = currentDate;
    }

    public void updateDate(int beforeOrNext){
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date == null) return;

        calendar.setTime(date);

        if(beforeOrNext == 0){
            int day = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE,day - 1);
            currentDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        }else {
            int day = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE,day + 1);
            currentDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        }
        tv_date.setText(currentDate);
        getData();
    }
}
