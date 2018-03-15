package com.isoftstone.smartsite.model.map.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
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

import static com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE;
import static com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER;
import static com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER;

/**
 * Created by zw on 2017/11/19.
 */

public class ConstructionMontitoringMapActivity extends BaseActivity implements View.OnClickListener, AMap.OnMarkerClickListener {


    private final int NO_DATA = 0x0001;
    private final int INIT_DATA = 0x0002;
    private final int UPDATE_USER_GUIJI = 0x0003;
    private final int NO_GUI_JI = 0x0004;
    private final int FINISH_TASK_POSITION = 0x0005;
    private final int FINISH_TASK = 0x0006;
    private final int FINISH_TASK_POSITION_TOAST = 0x0007;

    private MapView mapView;
    private AMap aMap;
    private ImageView iv_status;
    private MapTaskDetailRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView rv;

    private LatLng aotiLatLon = new LatLng(30.482348,114.514417);

    private TextView tv_task_name;
    private ImageView iv_task_status;
    private TextView tv_time;
    private TextView tv_address;
    private TextView tv_person;
    private PopupWindow mPopWindow;
    private ImageView iv_start_task;
    private View content_parent;
    private LoadingDailog loadingDailog;
    private HttpPost httpPost;
    private PatrolTaskBean patrolTaskBean;
    private ArrayList<BaseUserBean> userBeans;
    private ArrayList<PatrolPositionBean> patrolPositionBeans;

    private ArrayList<UserTrackBean> currentUserTrackBeans;

    private BaseUserBean currentUserBean;
    private PatrolPositionBean currentPatorPositionBean;
    private Location currentLocation;

    private long loginUseId = HttpPost.mLoginBean.getmUserBean().getLoginUser().getId();

    private boolean isTaskCompleted = true;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NO_DATA:
                    loadingDailog.dismiss();
                    ToastUtils.showLong("没有获取到任务详情！");
                    break;
                case INIT_DATA:
                    if(isTaskCompleted){
                        iv_status.setVisibility(View.GONE);
                    } else {
                        iv_status.setVisibility(View.VISIBLE);
                    }
                    loadingDailog.dismiss();
                    updateRecyclerView();
                    updateTaskPoints();
                    updateUserGuiji();
                    break;
                case UPDATE_USER_GUIJI:
                    loadingDailog.dismiss();
                    addAndRemoveUserGuiJi();
                    break;
                case NO_GUI_JI:
                    loadingDailog.dismiss();
                    ToastUtils.showShort("没有获取到轨迹！");
                    if(polyline != null){
                        polyline.remove();
                    }
                    break;
                case FINISH_TASK_POSITION_TOAST:
                    ToastUtils.showShort("恭喜已经巡查完成,正在重新获取任务详情！");
                    mPopWindow.dismiss();
                    break;
                case FINISH_TASK_POSITION:
                    getData();
                    break;
                case FINISH_TASK:
                    ToastUtils.showShort("恭喜，任务已完成！");
                    iv_status.setVisibility(View.GONE);
                    isTaskCompleted = true;
                    break;
            }
        }
    };
    private Polyline polyline;
    private List<Marker> touXiangMarkers;
    private List<Marker> doneMarkers;
    private List<Marker> notDoneMarkers;
    private Marker currentClickMarker;
    private Marker roundMarker;

    private int taskId = -1;
    private MyLocationStyle myLocationStyle;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_map_construction_monitoring;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {

        taskId = (int) getIntent().getLongExtra("taskId",taskId);
        if(taskId == 0){
            ToastUtils.showLong("没有获取到任务信息！");
        }
        initToolBar();
        iv_status = (ImageView) findViewById(R.id.iv_status);
        iv_status.setOnClickListener(this);
        httpPost = new HttpPost();
        touXiangMarkers = new ArrayList<>();
        doneMarkers = new ArrayList<>();
        notDoneMarkers = new ArrayList<>();
        initMapView(savedInstanceState);
//        initLocation(aotiLatLon);
        initLoadingDialog();
        initRecyclerView();
        initPopWindow();
        initNowLocation();

        getData();

//        initMarkers();
//        addDoneRound(aotiLatLon);
//        addNotDoneRound(latLng2);

    }

    private void initToolBar(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        ImageButton btn = (ImageButton) findViewById(R.id.btn_icon);
        btn.setImageResource(R.drawable.search);
        btn.setOnClickListener(this);
        btn.setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("任务详情");
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
                patrolTaskBean= httpPost.patrolTaskFindOne(taskId);
                if(patrolTaskBean != null){
                    userBeans = patrolTaskBean.getUsers();
                    patrolPositionBeans = patrolTaskBean.getPatrolPositions();
                    int status = patrolTaskBean.getTaskStatus();
                    if(status == 2){
                        isTaskCompleted = true;
                    } else {
                        isTaskCompleted = false;
                    }

                    mHandler.sendEmptyMessage(INIT_DATA);
                } else {
                    mHandler.sendEmptyMessage(NO_DATA);
                }
            }
        }).start();
    }

    private void updateRecyclerView(){
        if(currentUserBean == null){
            currentUserBean = userBeans.get(0);
        }
        int position = userBeans.indexOf(currentUserBean);
        recyclerViewAdapter.setDatas(userBeans,position);
    }

    private void updateTaskPoints(){


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

        for (int i = 0; i < patrolPositionBeans.size(); i++) {
            PatrolPositionBean bean = patrolPositionBeans.get(i);
            LatLng latLng = new LatLng(bean.getLatitude(),bean.getLongitude());
            //0未巡查  1已巡查
            if(bean.getStatus() == 0){
                addNotDoneRound(bean,latLng);
            } else if(bean.getStatus() == 1){
                addDoneRound(bean,latLng);
                addMarker(bean,latLng);
            }

        }
    }

    private List<Bitmap> bitmaps = new ArrayList<>();
    private void addMarker(final PatrolPositionBean bean, LatLng latLng){
        BaseUserBean baseUserBean = bean.getUser();

        final MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
//        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        final String url = HttpPost.URL + "/" + baseUserBean.imageData;

        /*View centerView = LayoutInflater.from(ConstructionMontitoringMapActivity.this).inflate(R.layout.layout_marker_with_icon,null);
        CircleImageView civ = (CircleImageView) centerView.findViewById(R.id.civ);
        civ.setImageResource(R.drawable.default_head);
        markerOption.icon(BitmapDescriptorFactory.fromView(centerView));
        Marker marker = aMap.addMarker(markerOption);
        marker.setAnchor(0.5f,1f);
        marker.setObject(bean);
        touXiangMarkers.add(marker);*/

        //------------------------------------


        BitmapTypeRequest<String> bitmapTypeRequest = Glide.with(getApplicationContext()).load(url)
                .asBitmap();

        SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                View centerView = LayoutInflater.from(ConstructionMontitoringMapActivity.this).inflate(R.layout.layout_marker_with_icon,null);
                CircleImageView civ = (CircleImageView) centerView.findViewById(R.id.civ);
                civ.setImageResource(R.drawable.default_head);
                markerOption.icon(BitmapDescriptorFactory.fromView(centerView));
                Marker marker = aMap.addMarker(markerOption);
                marker.setAnchor(0.5f,1f);
                marker.setObject(bean);
                touXiangMarkers.add(marker);
            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                View centerView = LayoutInflater.from(ConstructionMontitoringMapActivity.this).inflate(R.layout.layout_marker_with_icon,null);
                CircleImageView civ = (CircleImageView) centerView.findViewById(R.id.civ);
                civ.setImageBitmap(resource);
                markerOption.icon(BitmapDescriptorFactory.fromView(centerView));
                Marker marker = aMap.addMarker(markerOption);
                marker.setAnchor(0.5f,1f);
                bean.bitmap = resource;
                marker.setObject(bean);
                touXiangMarkers.add(marker);
            }
        };

        bitmapTypeRequest.into(simpleTarget);

    }

    private void updateUserGuiji(){
        if(currentUserBean == null) return;

        final UserTrackBean bean = new UserTrackBean();
        bean.setTaskId(patrolTaskBean.getTaskId());
        long userId = currentUserBean.getId();
        int intUserId = (int) userId;
        bean.setUserId(intUserId);
        LogUtils.e(TAG,"userId : " + userId + " , taskId : " + patrolTaskBean.getTaskId());
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

    private void initRecyclerView(){
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);
        recyclerViewAdapter = new MapTaskDetailRecyclerViewAdapter(this,userBeans);
        recyclerViewAdapter.setItemClickListener(new MapTaskDetailRecyclerViewAdapter.onMapTaskItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = rv.getChildAdapterPosition(view);
                currentUserBean = userBeans.get(position);
                recyclerViewAdapter.updateViews(position);
                updateUserGuiji();
            }
        });
        rv.setAdapter(recyclerViewAdapter);
    }

    private void initPopWindow(){
        View popRootView = LayoutInflater.from(this).inflate(R.layout.layout_construction_monitoring_popwindw,null);
        popRootView.findViewById(R.id.iv_dismiss).setOnClickListener(this);
        tv_task_name = (TextView) popRootView.findViewById(R.id.tv_task_name);
        iv_task_status = (ImageView) popRootView.findViewById(R.id.iv_status);
        tv_time = (TextView) popRootView.findViewById(R.id.tv_time);
        tv_person = (TextView) popRootView.findViewById(R.id.tv_person);
        tv_address = (TextView) popRootView.findViewById(R.id.tv_address);
        iv_start_task = (ImageView) popRootView.findViewById(R.id.iv_start_task);
        iv_start_task.setVisibility(View.GONE);
        iv_start_task.setOnClickListener(this);
        content_parent = popRootView.findViewById(R.id.content_parent);

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

    private void initMarkers(){
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(aotiLatLon);
        markerOption.visible(true);

        markerOption.draggable(false);//设置Marker可拖动

        View centerView = LayoutInflater.from(this).inflate(R.layout.layout_marker_with_icon,null);
        markerOption.icon(BitmapDescriptorFactory.fromView(centerView));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        Marker startMarker = aMap.addMarker(markerOption);
        startMarker.setAnchor(0.5f,1f);
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
        //它不用点击效果
//        marker.setObject(bean);
        doneMarkers.add(marker);
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
        marker.setObject(bean);
        notDoneMarkers.add(marker);
    }

    private boolean isFirstIn = true;
    private void initNowLocation(){
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.interval(30000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(isFirstIn){
                    myLocationStyle.myLocationType(LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
                    aMap.setMyLocationStyle(myLocationStyle);
                    initLocation(new LatLng(location.getLatitude(),location.getLongitude()));
                    isFirstIn = false;
                }
                currentLocation = location;
                updateNowLocation(location);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;

            case R.id.iv_start_task:
                checkTaskPointOrUpdateTaskPoint();
                break;
            case R.id.iv_dismiss:
                mPopWindow.dismiss();
                break;
            //检查任务是否完成
            case R.id.iv_status:
                for (int i = 0; i < patrolPositionBeans.size(); i++) {
                    if(patrolPositionBeans.get(i).getStatus() == 0){
                        ToastUtils.showShort("还有巡查点任务未完成，请先完成再执行此操作！");
                        return;
                    }
                }
                if(isTaskCompleted){
                    ToastUtils.showShort("任务已完成，请勿重复操作！");
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpPost.executeTask(patrolTaskBean.getTaskId(),patrolTaskBean.getTaskName());
                        mHandler.sendEmptyMessage(FINISH_TASK);
                    }
                }).start();
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getObject() != null){
            PatrolPositionBean bean = (PatrolPositionBean) marker.getObject();
            currentPatorPositionBean = bean;
            tv_task_name.setText(bean.getPosition());
            //0未巡查  1已巡查
            if(bean.getStatus() == 0){
                currentClickMarker = marker;
                iv_start_task.setVisibility(View.VISIBLE);
                content_parent.setVisibility(View.GONE);
                iv_task_status.setImageResource(R.drawable.weiwancheng);
                tv_time.setText("");
            } else if(bean.getStatus() == 1){
                LogUtils.e(TAG,"marker  1 ");
                currentClickMarker = marker;
                iv_start_task.setVisibility(View.GONE);
                content_parent.setVisibility(View.VISIBLE);
                iv_task_status.setImageResource(R.drawable.yiwancheng);
                if(bean.getExecutionTime().length() >=10){

                    tv_time.setText(bean.getExecutionTime());
                }else {
                    tv_time.setText(bean.getExecutionTime());
                }
                tv_person.setText(bean.getUser().name);
                try{
                    String id = bean.getUser().getDepartmentId();
                    tv_address.setText(httpPost.getCompanyNameByid(Integer.parseInt(id)));
                } catch (Exception e){
                    tv_address.setText("未获取到部门信息");
                }
            }
            if(currentClickMarker != null){
                LogUtils.e(TAG,"marker  2 ");
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
        } else {
            civ.setVisibility(View.VISIBLE);
            civ.setImageResource(R.drawable.default_head);
        }

        markerOption1.icon(BitmapDescriptorFactory.fromView(centerView));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption1.setFlat(true);//设置marker平贴地图效果

        roundMarker = aMap.addMarker(markerOption1);
        roundMarker.setAnchor(0.5f,0.5f);

    }


    //30秒间隔上传一次用户的坐标
    private void updateNowLocation(Location location){
        if(isTaskCompleted) {
            return;
        }

        final double lat = location.getLatitude();
        final double lon = location.getLongitude();
        if(lat != 0 && lon != 0 ){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(patrolTaskBean != null){
                        httpPost.userTrack(loginUseId,taskId,lon,lat);
                    }
                }
            }).start();
        } else {
            ToastUtils.showShort("没有获取到您当前的位置信息，无法上传您的位置信息！");
        }

    }

    //检查是否在附近以及完成任务
    private void checkTaskPointOrUpdateTaskPoint(){
        if(currentUserBean.getId() != loginUseId){
            ToastUtils.showLong("请先切换到自己再完成任务！");
            return;
        }
        LatLng positionLatlng = new LatLng(currentPatorPositionBean.getLatitude(),currentPatorPositionBean.getLongitude());
        LatLng userLatlng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        float distance = AMapUtils.calculateLineDistance(positionLatlng,userLatlng);
        if(distance > 300){
            ToastUtils.showShort("您距离任务点的距离为" +(int)distance + "米,请在300米以内执行此操作！");
            return;
        } else {
            loadingDailog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    httpPost.updatePatrolPositionStatus(currentPatorPositionBean.getId(),currentPatorPositionBean.getPosition());
                    mHandler.sendEmptyMessage(FINISH_TASK_POSITION_TOAST);
                    SystemClock.sleep(2000);
                    mHandler.sendEmptyMessage(FINISH_TASK_POSITION);
                }
            }).start();
        }
    }

 /*   public Bitmap setGeniusIcon(String url) {
        Bitmap bitmap = null;
        View centerView = LayoutInflater.from(this).inflate(R.layout.layout_marker_with_icon,null);
        CircleImageView civ = (CircleImageView) centerView.findViewById(R.id.civ);
        ImageUtils.loadImage(civ,url);
        bitmap = convertViewToBitmap(centerView);
        return bitmap;
    }

    public Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }*/

    long currentTime;
    @Override
    public void onBackPressed() {
        double lastTime = currentTime;
        currentTime = System.currentTimeMillis();
        if(currentTime - lastTime < 3000){
            super.onBackPressed();
        } else {
            ToastUtils.showLong("退出此界面将不会再获取您的实时位置！若要退出，请再点击一次！");
        }
    }

    public void addRoundLine(){
        List<LatLng> latLngs = MapUtils.getAroundLatlons();
        Polyline polyline = aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.parseColor("#3464dd")));
    }
}
