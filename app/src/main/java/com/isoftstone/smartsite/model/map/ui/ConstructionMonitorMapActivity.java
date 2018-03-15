package com.isoftstone.smartsite.model.map.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.patroltask.PatrolPositionBean;
import com.isoftstone.smartsite.http.patroltask.PatrolTaskBean;
import com.isoftstone.smartsite.http.patroluser.UserTrackBean;
import com.isoftstone.smartsite.model.map.adapter.MapTaskAdapter;
import com.isoftstone.smartsite.utils.DensityUtils;
import com.isoftstone.smartsite.utils.LogUtils;
import com.isoftstone.smartsite.utils.MapUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zw on 2017/11/18.
 */

public class ConstructionMonitorMapActivity extends BaseActivity implements View.OnClickListener, AMap.OnMarkerClickListener {

    private static final int NO_DATA = 0x0001;
    private static final int INIT_VIEW = 0x0002;

    private MapView mapView;
    private AMap aMap;
    private LatLng aotiLatLon = new LatLng(30.482348,114.514417);
    private TextView tv_person;
    private TextView tv_company;
    private PopupWindow mPopWindow;

    private Marker roundMarker;
    private HttpPost httpPost;
    private ArrayList<UserTrackBean> userTrackBeans;
    private boolean hasData = false;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NO_DATA:
                    loadingDailog.dismiss();
                    ToastUtils.showLong("没有获取到正在执行任务的巡查人员数据！");
                    break;
                case INIT_VIEW:
                    loadingDailog.dismiss();
                    hasData = true;
                    initMarkers();
                    break;
            }
        }
    };
    private LoadingDailog loadingDailog;
    private ListView lv;
    private MapTaskAdapter taskAdapter;
    private TextView tv_no_task;
    private List<PatrolPositionBean> currentPatrolPositionBeans;

    private List<PatrolTaskBean> patrolTaskBeans = new ArrayList<>();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_map_construction_monitor;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {

        httpPost = new HttpPost();
        initToorBar();
        initMapView(savedInstanceState);
        initLocation(aotiLatLon);
        initLoadingDialog();
        initPopWindow();

        getData();

    }

    private void initToorBar(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        ImageButton btn = (ImageButton) findViewById(R.id.btn_icon);
        btn.setImageResource(R.drawable.map_list);
        btn.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("巡查人员实时监控");
    }

    private void initMapView(Bundle savedInstanceState){
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
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

    private void initLoadingDialog(){
        loadingDailog = new LoadingDailog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(true)
                .setCancelOutside(true).create();
    }

    private void getData(){
        loadingDailog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                userTrackBeans = httpPost.getUserTrack();
                if(userTrackBeans == null || userTrackBeans.size() == 0){
                    mHandler.sendEmptyMessage(NO_DATA);
                }else {
                    mHandler.sendEmptyMessage(INIT_VIEW);
                }
            }
        }).start();
    }

    private void initMarkers(){
        for (int i = 0; i < userTrackBeans.size(); i++) {
            final UserTrackBean bean = userTrackBeans.get(i);

            final MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(new LatLng(bean.getLatitude(),bean.getLongitude()));
            markerOption.visible(true);

            markerOption.draggable(false);//设置Marker可拖动

            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(true);//设置marker平贴地图效果

            String url = HttpPost.URL + "/" + bean.getUser().imageData;

            BitmapTypeRequest<String> bitmapTypeRequest = Glide.with(getApplicationContext()).load(url)
                    .asBitmap();

            SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    View centerView = LayoutInflater.from(ConstructionMonitorMapActivity.this).inflate(R.layout.layout_map_corner_marker,null);
                    CircleImageView civ = (CircleImageView) centerView.findViewById(R.id.civ);
                    civ.setImageBitmap(resource);
                    markerOption.icon(BitmapDescriptorFactory.fromView(centerView));
                    Marker marker = aMap.addMarker(markerOption);
                    marker.setAnchor(0.5f,0.5f);
                    marker.setObject(bean);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    View centerView = LayoutInflater.from(ConstructionMonitorMapActivity.this).inflate(R.layout.layout_map_corner_marker,null);
                    markerOption.icon(BitmapDescriptorFactory.fromView(centerView));
                    Marker marker = aMap.addMarker(markerOption);
                    marker.setAnchor(0.5f,0.5f);
                    marker.setObject(bean);
                }
            };

            bitmapTypeRequest.into(simpleTarget);
        }
    }

    private void initPopWindow(){
        View popRootView = LayoutInflater.from(this).inflate(R.layout.layout_map_task,null);
        tv_no_task = (TextView) popRootView.findViewById(R.id.tv_no_task);
        popRootView.findViewById(R.id.iv_dismiss).setOnClickListener(this);
        tv_person = (TextView) popRootView.findViewById(R.id.tv_person);
        tv_company = (TextView) popRootView.findViewById(R.id.tv_company);
        lv = (ListView) popRootView.findViewById(R.id.lv);
        lv.setDivider(new ColorDrawable(Color.parseColor("#eeeeee")));
        lv.setDividerHeight(2);
        taskAdapter = new MapTaskAdapter(this,null);
        lv.setAdapter(taskAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserTrackBean bean = userTrackBeans.get(position);
                LogUtils.e(TAG,bean.toString());
                Intent intent = new Intent(ConstructionMonitorMapActivity.this,MapTaskDetailActivity.class);
                intent.putExtra("data",bean);
                startActivity(intent);
                mPopWindow.dismiss();
            }
        });

        mPopWindow = new PopupWindow(this);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setContentView(popRootView);
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

    private void addAndRemoveRoundMarker(LatLng latLng){
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

    /*private boolean isFirstIn = true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(isFirstIn && hasData){
            showPopWindow();
            isFirstIn = false;
        }
    }*/

    private void showPopWindow(){
        if(mPopWindow != null){
            mPopWindow.showAtLocation(mapView, Gravity.BOTTOM,0, DensityUtils.dip2px(this,-8));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_icon:
                if(userTrackBeans != null && userTrackBeans.size() != 0){
                    Intent intent = new Intent(this,ConstructionMonitorListActivity.class);
                    intent.putExtra("data",userTrackBeans);
                    startActivity(intent);
                } else {
                    ToastUtils.showLong("当前没有人在执行任务！");
                }

                break;
            case R.id.iv_dismiss:
                mPopWindow.dismiss();
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getObject() != null){
            UserTrackBean bean = (UserTrackBean) marker.getObject();
            PatrolTaskBean taskBean = bean.getPatrolTask();
            tv_person.setText(bean.getUser().name);
            try{
                String id = bean.getUser().getDepartmentId();
                tv_company.setText(httpPost.getCompanyNameByid(Integer.parseInt(id)));
            } catch (Exception e){
                tv_company.setText("未获取到部门信息");
            }
            currentPatrolPositionBeans = bean.getPatrolTask().getPatrolPositions();
            if(taskBean == null ){
                lv.setVisibility(View.INVISIBLE);
                tv_no_task.setVisibility(View.VISIBLE);
            } else {
                lv.setVisibility(View.VISIBLE);
                tv_no_task.setVisibility(View.GONE);
                patrolTaskBeans.clear();
                patrolTaskBeans.add(taskBean);
                taskAdapter.setDatas(patrolTaskBeans);
            }

            LatLng latLng = new LatLng(bean.getLatitude(),bean.getLongitude());
            initLocation(latLng);
            addAndRemoveRoundMarker(latLng);
            showPopWindow();
        }

        return true;
    }

    public void addRoundLine(){
        List<LatLng> latLngs = MapUtils.getAroundLatlons();
        Polyline polyline = aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.parseColor("#3464dd")));
    }
}
