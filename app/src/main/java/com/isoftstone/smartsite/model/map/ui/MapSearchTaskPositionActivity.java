package com.isoftstone.smartsite.model.map.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.model.map.bean.TaskPositionBean;
import com.isoftstone.smartsite.utils.LogUtils;
import com.isoftstone.smartsite.utils.MapUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zw on 2017/11/21.
 */

public class MapSearchTaskPositionActivity extends BaseActivity implements View.OnClickListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener {

    public static final int RESULT_SAVE = 0x0001;

    private int id = 0;

    private MapView mapView;
    private AMap aMap;
    private EditText et;
    private LatLng currentLatLng;
    private Marker currentMarker;
    private ArrayList<LatLng> latLngs = new ArrayList<>();
    private List<String> latLngsName = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
    private LatLng aotiLatLon = new LatLng(30.482348,114.514417);
    private Marker deleteMarker;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_map_search_task_position;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        id = getIntent().getIntExtra("zhuapai",0);

        initToolBar();
        et = (EditText) findViewById(R.id.et);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    et.setCursorVisible(false);
                }
                return false;
            }
        });
        et.setOnClickListener(this);

        if(id == 100){
            et.setHint("请输入抓拍地点");
        }

        initMapView(savedInstanceState);
        initLocation(aotiLatLon);
    }


    private void initToolBar(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.iv_add_task).setOnClickListener(this);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        if(id == 100){
            toolbar_title.setText("抓拍地点");
        }else {
            toolbar_title.setText("任务地点");
        }

        TextView tv_save = (TextView) findViewById(R.id.btn_icon_right);
        tv_save.setText("保存");
        tv_save.setOnClickListener(this);
    }

    private void initMapView(Bundle savedInstanceState){
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        addRoundLine();
    }

    private void initLocation(LatLng latLng){
        CameraPosition mCameraPosition = new CameraPosition(latLng,13f,0,0);

        CameraUpdate update = CameraUpdateFactory.newCameraPosition(mCameraPosition);
        if(aMap != null){
            aMap.animateCamera(update);
        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_icon_right:
                saveAllPosition();
                break;
            case R.id.et:
                et.setCursorVisible(true);
                break;
            case R.id.iv_add_task:
                savePosition();
                break;
        }
    }

    private void savePosition(){

        if(currentMarker == null){
            ToastUtils.showShort("没有获取到坐标！");
            return;
        }

        if(latLngs.size() > 20){
            ToastUtils.showShort("最多只能输入20个地址！");
            return;
        }

        String name = et.getText().toString();
        if(TextUtils.isEmpty(name)){
            ToastUtils.showShort("巡查点名称不能为空！");
            return;
        }
        for (int i = 0; i < latLngs.size(); i++) {
            LatLng latLng = latLngs.get(i);
            if(currentLatLng.latitude == latLng.latitude &&
                    currentLatLng.longitude == latLng.longitude){
                ToastUtils.showShort("该巡查点地址已存在！");
                return;
            }
            if(TextUtils.equals(name,latLngsName.get(i))){
                ToastUtils.showShort("该巡查点名称重复,请重新输入！");
                return;
            }
        }


        LatLng latLng = (LatLng) currentMarker.getObject();
        currentMarker.remove();
        addRealMarker(latLng,name);
        latLngs.add(currentLatLng);
        latLngsName.add(name);
        markers.add(currentMarker);

        currentMarker = null;
        ToastUtils.showShort("保存成功！");

        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        et.setText("");

    }

    private void saveAllPosition(){
        if(latLngs.size() == 0) {
            ToastUtils.showShort("请先添加坐标信息再保存！");
            return;
        }

        Gson gson = new Gson();
        String latLngsJson = gson.toJson(latLngs);
        String latLngsNameJson = gson.toJson(latLngsName);
//        ArrayList<LatLng> list = gson.fromJson(latLngsJson,new TypeToken<ArrayList<LatLng>>(){}.getType());
        Intent intent = new Intent();
        intent.putExtra("latLngsJson",latLngsJson);
        intent.putExtra("latLngsNameJson",latLngsNameJson);
        intent.putParcelableArrayListExtra("latLngs",latLngs);
        ArrayList<String> temp = gson.fromJson(latLngsNameJson,ArrayList.class);

        LogUtils.e(TAG,latLngsNameJson);
        setResult(RESULT_SAVE,intent);
        this.finish();
    }

    private int clickPosition = 0;
    @Override
    public boolean onMarkerClick(Marker marker) {

        if(marker.equals(deleteMarker)){
            LogUtils.e(TAG,"deleteMarker onMarkerClick");
            deleteMarker.remove();
            Marker marker1 = markers.get(clickPosition);
            markers.remove(clickPosition);
            latLngs.remove(clickPosition);
            latLngsName.remove(clickPosition);
            marker1.remove();
            deleteMarker = null;
        } else if(markers.indexOf(marker) != -1){
            clickPosition = markers.indexOf(marker);
            TaskPositionBean bean = (TaskPositionBean) marker.getObject();
            marker.setVisible(false);
            addDeleteMarker(bean);
        }
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        currentLatLng = latLng;
        if(deleteMarker != null){
            deleteMarker.remove();
        }
        if(markers.size() != 0 && markers.get(clickPosition) != null && !markers.get(clickPosition).isVisible()){
            markers.get(clickPosition).setVisible(true);
        }
        addMarker(latLng);
    }

    private void addMarker(LatLng latLng){

        if(currentMarker != null && !markers.contains(currentMarker)){
            currentMarker.remove();
        }

        MarkerOptions markerOption = new MarkerOptions();

        markerOption.position(latLng);

        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_map_task_position_marker,null);
        TextView tv_address = (TextView) contentView.findViewById(R.id.tv_address);
        tv_address.setVisibility(View.GONE);
        ImageView iv_delete = (ImageView) contentView.findViewById(R.id.iv_delete);
        iv_delete.setVisibility(View.GONE);
        markerOption.icon(BitmapDescriptorFactory.fromView(contentView));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        currentMarker = aMap.addMarker(markerOption);
        currentMarker.setAnchor(0.5f,0.5f);
        currentMarker.setObject(latLng);
    }

    private void addRealMarker(LatLng latLng,String address){
        MarkerOptions markerOption = new MarkerOptions();

        markerOption.position(latLng);

        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_map_task_position_marker,null);
        TextView tv_address = (TextView) contentView.findViewById(R.id.tv_address);
        tv_address.setVisibility(View.VISIBLE);
        tv_address.setText(address);
        ImageView iv_delete = (ImageView) contentView.findViewById(R.id.iv_delete);
        iv_delete.setVisibility(View.GONE);
        contentView.setTag(latLng);
        markerOption.icon(BitmapDescriptorFactory.fromView(contentView));


        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        currentMarker = aMap.addMarker(markerOption);
        currentMarker.setAnchor(0.5f,0.5f);
        currentMarker.setObject(new TaskPositionBean(iv_delete,tv_address,latLng));
    }


    private void addDeleteMarker(TaskPositionBean bean){
        MarkerOptions markerOption = new MarkerOptions();

        markerOption.position(bean.getLatLng());

        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_map_task_position_marker,null);
        TextView tv_address = (TextView) contentView.findViewById(R.id.tv_address);
        tv_address.setVisibility(View.VISIBLE);
        tv_address.setText(bean.getTextView().getText());
        ImageView iv_delete = (ImageView) contentView.findViewById(R.id.iv_delete);
        iv_delete.setVisibility(View.VISIBLE);
        contentView.setTag(bean);

        markerOption.icon(BitmapDescriptorFactory.fromView(contentView));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        deleteMarker = aMap.addMarker(markerOption);
        deleteMarker.setAnchor(0.5f,0.5f);
    }

    public void addRoundLine(){
        List<LatLng> latLngs = MapUtils.getAroundLatlons();
        Polyline polyline = aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.parseColor("#3464dd")));
    }

}
