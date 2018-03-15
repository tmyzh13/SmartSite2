package com.isoftstone.smartsite.model.map.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.isoftstone.smartsite.http.user.BaseUserBean;
import com.isoftstone.smartsite.model.map.adapter.MapTaskDetailRecyclerViewAdapter;
import com.isoftstone.smartsite.utils.DensityUtils;
import com.isoftstone.smartsite.utils.LogUtils;
import com.isoftstone.smartsite.utils.MapUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zw on 2017/11/19.
 */

public class MapTaskDetailActivity extends BaseActivity implements View.OnClickListener, AMap.OnMarkerClickListener {

    private final int UPDATE_USER_GUIJI = 0x0001;
    private final int NO_GUI_JI = 0x0002;
    private final int AFTER_GET_USERS = 0x0003;
    private final int NO_DATA = 0x0004;

    private MapView mapView;
    private AMap aMap;
    private LatLng aotiLatLon = new LatLng(30.482348,114.514417);

    private RecyclerView rv;
    private Marker startMarker;
    private Polyline polyline;
    private Marker endMarker;
    private MapTaskDetailRecyclerViewAdapter recyclerViewAdapter;
    private Marker roundMarker;
    private PopupWindow mPopWindow;
    private UserTrackBean userTrackBean;
    private LoadingDailog loadingDailog;

    private BaseUserBean currentUserBean;
    private HttpPost httpPost;
    private ArrayList<UserTrackBean> currentUserTrackBeans;
    private List<Marker> touXiangMarkers = new ArrayList<>();
    private List<Marker> doneMarkers = new ArrayList<>();
    private List<Marker> notDoneMarkers = new ArrayList<>();
    private PatrolTaskBean patrolTaskBean;
    private ArrayList<BaseUserBean> userBeans;
    private ArrayList<PatrolPositionBean> patrolPositionBeans;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case AFTER_GET_USERS:
                    recyclerViewAdapter.setDatas(userBeans,userBeans.indexOf(currentUserBean));
                    updateUserGuiji();
                    break;
                case NO_DATA:
                    loadingDailog.dismiss();
                    ToastUtils.showShort("没有获取到任务详情！");
                    break;
                case UPDATE_USER_GUIJI:
                    loadingDailog.dismiss();
                    updateTaskPoints();
                    addAndRemoveUserGuiJi();
                    break;
                case NO_GUI_JI:
                    loadingDailog.dismiss();
                    ToastUtils.showShort("没有获取到轨迹！");
                    updateTaskPoints();
                    break;
            }
        }
    };
    private TextView tv_task_name;
    private ImageView iv_task_status;
    private TextView tv_time;
    private TextView tv_person;
    private TextView tv_address;
    private Marker currentClickMarker;
    private long taskId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_map_task_detail;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        httpPost = new HttpPost();
        userTrackBean = (UserTrackBean) getIntent().getSerializableExtra("data");
        taskId = userTrackBean.getTaskId();

        initToorBar();
        initRecyclerView();
        initMapView(savedInstanceState);
        initLocation(aotiLatLon);
        initPopWindow();
        initLoadingDialog();

        getUsers();
    }

    private void initToorBar(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        ImageButton btn = (ImageButton) findViewById(R.id.btn_icon);
        btn.setImageResource(R.drawable.search);
        btn.setOnClickListener(this);
        btn.setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("任务详情");
    }

    private void initRecyclerView(){
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);
        recyclerViewAdapter = new MapTaskDetailRecyclerViewAdapter(this,userTrackBean.getPatrolTask().getUsers());
        recyclerViewAdapter.setItemClickListener(new MapTaskDetailRecyclerViewAdapter.onMapTaskItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = rv.getChildAdapterPosition(view);
                recyclerViewAdapter.updateViews(position);
                if(userBeans != null) {
                    currentUserBean = userBeans.get(position);
                } else if(userTrackBean != null && userTrackBean.getPatrolTask() != null & userTrackBean.getPatrolTask().getUsers() != null){
                    currentUserBean = userTrackBean.getPatrolTask().getUsers().get(position);
                }

                updateUserGuiji();
            }
        });
        rv.setAdapter(recyclerViewAdapter);
    }

    private void initLoadingDialog(){
        loadingDailog = new LoadingDailog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(true)
                .setCancelOutside(true).create();
    }


    private void removeMarkers(){
        if(polyline != null){
            polyline.remove();
        }

        for (int i = 0; i < doneMarkers.size(); i++) {
            doneMarkers.get(i).remove();
        }
        doneMarkers.clear();

        for (int i = 0; i < notDoneMarkers.size(); i++) {
            notDoneMarkers.get(i).remove();
        }
        notDoneMarkers.clear();

        for (int i = 0; i < touXiangMarkers.size(); i++) {
            touXiangMarkers.get(i).remove();
        }
        touXiangMarkers.clear();
    }

    private void updateTaskPoints(){

        removeMarkers();

        if(patrolPositionBeans == null) return;

        List<PatrolPositionBean> beans = new ArrayList<>();
        if(currentUserTrackBeans.size() != 0){
            int userId = currentUserTrackBeans.get(0).getUserId();
            for (int i = 0; i < patrolPositionBeans.size(); i++) {
                if(patrolPositionBeans.get(i).getUser() != null && patrolPositionBeans.get(i).getUser().getId() == userId){
                    beans.add(patrolPositionBeans.get(i));
                }
            }
        }


        for (int i = 0; i < patrolPositionBeans.size(); i++) {
            PatrolPositionBean bean = patrolPositionBeans.get(i);
            LatLng latLng = new LatLng(bean.getLatitude(),bean.getLongitude());
            //0未巡查  1已巡查
            if(bean.getStatus() == 0){
                addNotDoneRound(bean,latLng);
            } else if(bean.getStatus() == 1){
                addDoneRound(bean,latLng);
                if (beans.contains(bean)){
                    addMarker(bean,latLng);
                }
            }
        }

        if(beans.size() != 0){

        }
    }

    private void addNotDoneRound(PatrolPositionBean bean,LatLng latLng){
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.weiwanchengdianwei));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        Marker marker = aMap.addMarker(markerOption);
        marker.setAnchor(0.5f,0.5f);
//        marker.setObject(bean);
        notDoneMarkers.add(marker);
    }

    private void addDoneRound(PatrolPositionBean bean,LatLng latLng){
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.yiwanchengdianwei));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        Marker marker = aMap.addMarker(markerOption);
        marker.setAnchor(0.5f,0.5f);
        marker.setClickable(true);
        marker.setObject(bean);
        doneMarkers.add(marker);
    }

    private List<Bitmap> bitmaps = new ArrayList<>();
    private void addMarker(final PatrolPositionBean bean, LatLng latLng){
        BaseUserBean baseUserBean = bean.getUser();

        final MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        final String url = HttpPost.URL + "/" + baseUserBean.imageData;


//        markerOption.icon(BitmapDescriptorFactory.fromBitmap(setGeniusIcon(url)));

        BitmapTypeRequest<String> bitmapTypeRequest = Glide.with(getApplicationContext()).load(url)
                .asBitmap();

        SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                View centerView = LayoutInflater.from(MapTaskDetailActivity.this).inflate(R.layout.layout_marker_with_icon,null);
                CircleImageView civ = (CircleImageView) centerView.findViewById(R.id.civ);
                civ.setImageBitmap(resource);
                markerOption.icon(BitmapDescriptorFactory.fromView(centerView));
                Marker marker = aMap.addMarker(markerOption);
                marker.setAnchor(0.5f,1f);
                bean.bitmap = resource;
                marker.setObject(bean);
                touXiangMarkers.add(marker);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                View centerView = LayoutInflater.from(MapTaskDetailActivity.this).inflate(R.layout.layout_marker_with_icon,null);
                markerOption.icon(BitmapDescriptorFactory.fromView(centerView));
                Marker marker = aMap.addMarker(markerOption);
                marker.setAnchor(0.5f,1f);
                bean.bitmap = null;
                marker.setObject(bean);
                touXiangMarkers.add(marker);
            }
        };

        bitmapTypeRequest.into(simpleTarget);

    }

    private void getUsers(){
        loadingDailog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                patrolTaskBean= httpPost.patrolTaskFindOne(taskId);
                LogUtils.e(TAG,"haha : " + patrolTaskBean.toString());
                if(patrolTaskBean != null){
                    userBeans = patrolTaskBean.getUsers();
                    patrolPositionBeans = patrolTaskBean.getPatrolPositions();
                    long userId = userTrackBean.getUser().getId();
                    for (int i = 0; i < userBeans.size(); i++) {
                        if(userBeans.get(i).getId() == userId){
                            currentUserBean = userBeans.get(i);
                        }
                    }
                    if(currentUserBean == null){
                        currentUserBean = userBeans.get(0);
                    }
                    mHandler.sendEmptyMessage(AFTER_GET_USERS);
                } else {
                    mHandler.sendEmptyMessage(NO_DATA);
                }
            }
        }).start();
    }

    private void updateUserGuiji(){
        if(currentUserBean == null) return;

        final UserTrackBean bean = new UserTrackBean();
        LogUtils.e(TAG,"userTrackBean.getTaskId() : " + userTrackBean.getTaskId());
        bean.setTaskId(userTrackBean.getTaskId());
        long userId = currentUserBean.getId();
        int intUserId = (int) userId;
        bean.setUserId(intUserId);
        loadingDailog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentUserTrackBeans = httpPost.findByUserIdAndTaskId(bean);
                if(currentUserBean == null || currentUserTrackBeans.size() == 0){
                    mHandler.sendEmptyMessage(NO_GUI_JI);
                }else {
                    mHandler.sendEmptyMessage(UPDATE_USER_GUIJI);
                }
            }
        }).start();

    }

    private void addAndRemoveUserGuiJi(){
        if(currentUserTrackBeans == null) return;

        if(polyline != null){
            polyline.remove();
        }

        List<LatLng> latLngs = new ArrayList<LatLng>();
        for (int i = 0; i < currentUserTrackBeans.size(); i++) {
            UserTrackBean bean = currentUserTrackBeans.get(i);
            LatLng latLng = new LatLng(bean.getLatitude(),bean.getLongitude());
            latLngs.add(latLng);
        }
        polyline = aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(20).color(Color.parseColor("#4f4de6")));
    }


    private void initMapView(Bundle savedInstanceState){
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setOnMarkerClickListener(this);
        addRoundLine();
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


    private void initPopWindow(){
        View popRootView = LayoutInflater.from(this).inflate(R.layout.layout_construction_monitoring_popwindw,null);
        popRootView.findViewById(R.id.iv_dismiss).setOnClickListener(this);
        tv_task_name = (TextView) popRootView.findViewById(R.id.tv_task_name);
        iv_task_status = (ImageView) popRootView.findViewById(R.id.iv_status);
        tv_time = (TextView) popRootView.findViewById(R.id.tv_time);
        tv_person = (TextView) popRootView.findViewById(R.id.tv_person);
        tv_address = (TextView) popRootView.findViewById(R.id.tv_address);

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
                if(currentClickMarker != null){
                    currentClickMarker.setVisible(true);
                    currentClickMarker = null;
                }
            }
        });

    }


    private void initLocation(LatLng latLng){
        CameraPosition mCameraPosition = new CameraPosition(latLng,13f,0,0);

        CameraUpdate update = CameraUpdateFactory.newCameraPosition(mCameraPosition);
        if(aMap != null){
            aMap.animateCamera(update);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_icon:

                break;
            case R.id.iv_dismiss:
                mPopWindow.dismiss();
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getObject() != null){
            currentClickMarker = marker;
            PatrolPositionBean bean = (PatrolPositionBean) marker.getObject();
            tv_task_name.setText(bean.getPosition());
            //if(bean.getExecutionTime().length() >=10){
            //    tv_time.setText(bean.getExecutionTime().substring(0,10));
            //}else {
                tv_time.setText(bean.getExecutionTime());
            //}
            tv_person.setText(bean.getUser().name);
            try{
                String id = bean.getUser().getDepartmentId();
                tv_address.setText(httpPost.getCompanyNameByid(Integer.parseInt(id)));
            }catch (Exception e){
                tv_address.setText("未获取到部门信息");
            }

            if(currentClickMarker != null){
                currentClickMarker.setVisible(false);
            }
            addAndRemoveRoundMarker(new LatLng(bean.getLatitude(),bean.getLongitude()),bean.bitmap);
            mPopWindow.showAtLocation(mapView, Gravity.BOTTOM,0, DensityUtils.dip2px(this,-8));
        }
        return true;
    }

    private void addAndRemoveRoundMarker(LatLng latLng,Bitmap bitmap){
        MarkerOptions markerOption1 = new MarkerOptions();

        markerOption1.position(latLng);

        markerOption1.visible(true);

        markerOption1.draggable(false);//设置Marker可拖动
        View centerView = LayoutInflater.from(this).inflate(R.layout.layout_map_task_backround,null);
        CircleImageView civ = (CircleImageView) centerView.findViewById(R.id.civ);

        if(bitmap != null){
            civ.setVisibility(View.VISIBLE);
            civ.setImageBitmap(bitmap);
        }

        markerOption1.icon(BitmapDescriptorFactory.fromView(centerView));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption1.setFlat(true);//设置marker平贴地图效果

        roundMarker = aMap.addMarker(markerOption1);
        roundMarker.setAnchor(0.5f,0.5f);

    }

    public void addRoundLine(){
        List<LatLng> latLngs = MapUtils.getAroundLatlons();
        Polyline polyline = aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.parseColor("#3464dd")));
    }
}
