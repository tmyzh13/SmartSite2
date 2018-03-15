package com.isoftstone.smartsite.model.map.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.common.App;
import com.isoftstone.smartsite.http.aqi.DataQueryVoBean;
import com.isoftstone.smartsite.http.video.DevicesBean;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.model.main.ui.PMDataInfoActivity;
import com.isoftstone.smartsite.model.main.ui.PMHistoryInfoActivity;
import com.isoftstone.smartsite.model.system.ui.PermissionsActivity;
import com.isoftstone.smartsite.model.system.ui.PermissionsChecker;
import com.isoftstone.smartsite.model.video.SnapPicturesActivity;
import com.isoftstone.smartsite.model.video.VideoPlayActivity;
import com.isoftstone.smartsite.model.video.VideoRePlayActivity;
import com.isoftstone.smartsite.model.video.bean.AlbumInfo;
import com.isoftstone.smartsite.model.video.bean.PhotoInfo;
import com.isoftstone.smartsite.model.video.bean.PhotoList;
import com.isoftstone.smartsite.model.video.utils.ThumbnailsUtil;
import com.isoftstone.smartsite.utils.DensityUtils;
import com.isoftstone.smartsite.utils.FilesUtils;
import com.isoftstone.smartsite.utils.MapUtils;
import com.isoftstone.smartsite.utils.NetworkUtils;
import com.isoftstone.smartsite.utils.ToastUtils;
import com.isoftstone.smartsite.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.isoftstone.smartsite.model.main.adapter.PMDevicesListAdapter.COLOR_0;
import static com.isoftstone.smartsite.model.main.adapter.PMDevicesListAdapter.COLOR_150;
import static com.isoftstone.smartsite.model.main.adapter.PMDevicesListAdapter.COLOR_250;
import static com.isoftstone.smartsite.model.main.adapter.PMDevicesListAdapter.COLOR_350;
import static com.isoftstone.smartsite.model.main.adapter.PMDevicesListAdapter.COLOR_420;
import static com.isoftstone.smartsite.model.main.adapter.PMDevicesListAdapter.COLOR_50;
import static com.isoftstone.smartsite.model.main.adapter.PMDevicesListAdapter.COLOR_600;

/**
 * Created by zw on 2017/10/14.
 */

public class MapMainFragment extends BaseFragment implements AMap.OnMarkerClickListener, View.OnClickListener {

    private TextureMapView mMapView;
    private AMap mAMap;
    private MyLocationStyle myLocationStyle;

    private static final int ADD_MARKER = 0x0001;

    private List<Marker> markers = new ArrayList<>();

    private FrameLayout mapContentView;
    private HttpPost mHttpPost;
    private ArrayList<DevicesBean> mVideoList  = new ArrayList<>();
    private ArrayList<DataQueryVoBean> mEnvList  = new ArrayList<>();
    private LatLng aotiLatLon;

    private DevicesBean currentVideoBean;
    private DataQueryVoBean currentEnvirBean;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == ADD_MARKER){
                initMarker();
            }
        }
    };
    private TextView tv_deviceNumber;
    private TextView tv_isOnline;
    private TextView tv_deviceTime;
    private TextView tv_deviceAddress;
    private View videoView;
    private ImageView iv_video;
    private TextView tv_video;
    private View historyView;
    private ImageView iv_history;
    private TextView tv_history;
    private View galleryView;
    private ImageView iv_gallery;
    private TextView tv_gallery;
    private View eviorment_view;
    private TextView tv_pm10;
    private TextView tv_pm25;
    private TextView tv_pmco2;

    private View background_line;
    private PopupWindow mPopWindow;
    private Marker roundMarker;

    private float zoom = 13f;
    private CameraPosition mCameraPosition;
    private double mLat,mLon = 0;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_map_main;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState){

        initToolbar();

        initPopWindow();

        initData();

        mapContentView = (FrameLayout) rootView.findViewById(R.id.map_content);

    }

    private void initData(){
        mHttpPost = new HttpPost();
    }

    private void initToolbar(){
        TextView tv_title = (TextView) rootView.findViewById(R.id.toolbar_title);
        tv_title.setText("地图");

        rootView.findViewById(R.id.btn_back).setOnClickListener(this);
        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.btn_icon);
        imageButton.setImageResource(R.drawable.search);
//        imageButton.setOnClickListener(this);
        imageButton.setVisibility(View.GONE);
    }

    private void initPopWindow(){
        View popWindowView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_map_video_monitor_popwindow,null);
        popWindowView.findViewById(R.id.iv_dismiss).setOnClickListener(this);
        tv_deviceNumber = (TextView) popWindowView.findViewById(R.id.tv_device_number);
        tv_isOnline = (TextView) popWindowView.findViewById(R.id.tv_isonline);
        tv_deviceTime = (TextView) popWindowView.findViewById(R.id.tv_device_time);
        tv_deviceAddress = (TextView) popWindowView.findViewById(R.id.tv_address);

        videoView = popWindowView.findViewById(R.id.video);
        iv_video = (ImageView) popWindowView.findViewById(R.id.iv_video);
        tv_video = (TextView) popWindowView.findViewById(R.id.tv_video);
        historyView = popWindowView.findViewById(R.id.history);
        iv_history = (ImageView) popWindowView.findViewById(R.id.iv_history);
        tv_history = (TextView) popWindowView.findViewById(R.id.tv_history);
        galleryView = popWindowView.findViewById(R.id.gallery);
        iv_gallery = (ImageView) popWindowView.findViewById(R.id.iv_gallery);
        tv_gallery = (TextView) popWindowView.findViewById(R.id.tv_gallery);
        videoView.setOnClickListener(this);
        historyView.setOnClickListener(this);
        galleryView.setOnClickListener(this);

        eviorment_view = popWindowView.findViewById(R.id.eviorment_view);

        tv_pm10 = (TextView) popWindowView.findViewById(R.id.text_pm10);
        tv_pm25 = (TextView) popWindowView.findViewById(R.id.text_pm25);
        tv_pmco2 = (TextView) popWindowView.findViewById(R.id.text_co2);

        background_line = popWindowView.findViewById(R.id.background_line);
        /*if(type == TYPE_CAMERA){
            eviorment_view.setVisibility(View.GONE);
            tv_video.setText("实时视频");
            tv_history.setText("历史监控");
            tv_gallery.setText("视频抓拍");

        } else if(type == TYPE_ENVIRONMENT){
            galleryView.setVisibility(View.GONE);
            background_line.setVisibility(View.GONE);
            tv_video.setText("实时数据");
            tv_history.setText("历史数据");
        }*/

        mPopWindow = new PopupWindow(getActivity());
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

    /*private void initChooseCameraPopWindow(){
        View chooseCameraView = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_choose_camera,null);
        View chooseCameraHeader = LayoutInflater.from(mContext).inflate(R.layout.layout_choose_camera_head,null);

        ListView chooseCameraListView = (ListView) chooseCameraView.findViewById(R.id.lv);
        chooseCameraListView.setAdapter(new ChooseCameraAdapter(getActivity()));
        chooseCameraListView.addHeaderView(chooseCameraHeader,null,false);
        chooseCameraListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("resCode", currentVideoBean.getDeviceCoding());
                    bundle.putInt("resSubType", currentVideoBean.getDeviceType());
                    intent.putExtras(bundle);
                    intent.setClass(mContext, VideoPlayActivity.class);
                    mContext.startActivity(intent);
                } else if(position == 2){
                    Date now = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String beginTime = formatter.format(now) + " 00:00:00";
                    String endTime = formatter.format(now) + " 23:59:59";

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("resCode", currentVideoBean.getDeviceCoding());
                    bundle.putInt("resSubType", currentVideoBean.getDeviceType());
                    bundle.putString("resName", currentVideoBean.getDeviceName());
                    bundle.putBoolean("isOnline", "0".equals(currentVideoBean.getDeviceStatus()));
                    bundle.putString("beginTime", beginTime);
                    bundle.putString("endTime", endTime);
                    //Toast.makeText(mContext, "ViewHolder: " +  ((ViewHolder)rootView.getTag()).name.getText().toString(), Toast.LENGTH_SHORT).show();
                    intent.putExtras(bundle);
                    intent.setClass(mContext, VideoRePlayListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else if(position == 3){
                    //打开系统相册浏览照片  
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                chooseCameraPopWindow.dismiss();
            }
        });

        chooseCameraPopWindow = new PopupWindow(getActivity());
        chooseCameraPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        chooseCameraPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        chooseCameraPopWindow.setContentView(chooseCameraView);
        chooseCameraPopWindow.setOutsideTouchable(false);
        chooseCameraPopWindow.setFocusable(true);
        chooseCameraPopWindow.setTouchable(true);
        chooseCameraPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }*/

    /*private void initDeviceInfoPopWindow(){
        View deviceInfoView = LayoutInflater.from(mContext).inflate(R.layout.layout_map_device_info,null);
        tvDeviceName = (TextView) deviceInfoView.findViewById(R.id.device_name);
        tvDeviceAddress = (TextView) deviceInfoView.findViewById(R.id.device_address);
        tvDeviceDate = (TextView) deviceInfoView.findViewById(R.id.device_date);
        btnDeviceInfo = (Button) deviceInfoView.findViewById(R.id.device_info);
        btnDeviceCancel = (Button) deviceInfoView.findViewById(R.id.device_cancel);
        btnDeviceCancel.setOnClickListener(this);
        btnDeviceInfo.setOnClickListener(this);


        deviceInfoPopWindow = new PopupWindow(getActivity());
        deviceInfoPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        deviceInfoPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        deviceInfoPopWindow.setContentView(deviceInfoView);

        deviceInfoPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deviceInfoPopWindow.setOutsideTouchable(false);
        deviceInfoPopWindow.setFocusable(true);
        deviceInfoPopWindow.setTouchable(true);
    }*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mLat == 0){
            mLat = 30.482348;
            mLon = 114.514417;
        }
        aotiLatLon = new LatLng(mLat,mLon);

        mMapView = new TextureMapView(getActivity());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mMapView.setLayoutParams(params);
        mapContentView.addView(mMapView,0);

        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mAMap.setOnMarkerClickListener(this);
        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                zoom = cameraPosition.zoom;
                mLat = cameraPosition.target.latitude;
                mLon = cameraPosition.target.longitude;
            }
        });

        UiSettings settings = mAMap.getUiSettings();
        settings.setLogoBottomMargin(DensityUtils.dip2px(getActivity(),52));
        settings.setZoomControlsEnabled(false);

        initLocation(aotiLatLon);
        addRoundLine();
        if(markers.size() != 0){
            initMarker();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    /*DevicesBean bean = new DevicesBean();
                    bean.setDeviceId("123");
                    bean.setDeviceStatus("0");
                    DevicesBean.DevicesArch arch = new DevicesBean.DevicesArch();
                    arch.setName("光谷一路（假数据）");
                    bean.setArch(arch);
                    bean.setInstallTime("2017-11-07");
                    bean.setLatitude("30.47");
                    bean.setLongitude("114.518672");

                    DevicesBean bean1 = new DevicesBean();
                    bean1.setDeviceId("123");
                    bean1.setDeviceStatus("1");
                    DevicesBean.DevicesArch arch1 = new DevicesBean.DevicesArch();
                    arch1.setName("光谷二路（假数据）");
                    bean1.setArch(arch1);
                    bean1.setInstallTime("2017-11-07");
                    bean1.setLatitude("30.47");
                    bean1.setLongitude("114.498072");*/

                    ArrayList<DevicesBean> videoList = mHttpPost.getDevices("1","","","");
                    if(videoList != null){
                        mVideoList = videoList;
                    }
                    ArrayList<DataQueryVoBean> envList = mHttpPost.onePMDevicesDataList("","0","","");
                    if(envList != null){
                        mEnvList = envList;
                    }

                    mHandler.sendEmptyMessage(ADD_MARKER);
                }
            }).start();
        }

    }

    private void initLocation(LatLng latLng){
        /*//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15f));

        mAMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
                mAMap.setMyLocationStyle(myLocationStyle);
                mAMap.setOnMyLocationChangeListener(null);
                lat = location.getLatitude();
                lon = location.getLongitude();
                initMarker();
            }
        });*/


        mCameraPosition = new CameraPosition(latLng,zoom,0,0);

        CameraUpdate update = CameraUpdateFactory.newCameraPosition(mCameraPosition);
        mAMap.animateCamera(update);

    }

    private void initMarker(){
        if(mEnvList.size() == 0 && mVideoList.size() == 0){
            ToastUtils.showLong("没有获取到设备信息!");
        }else {
            for (int i = 0;i<mVideoList.size();i++){
                DevicesBean bean = mVideoList.get(i);
                MarkerOptions markerOption = new MarkerOptions();
                double lat = Double.parseDouble(bean.getLatitude());
                double lon = Double.parseDouble(bean.getLongitude());
                markerOption.position(new LatLng(lat,lon));
                markerOption.visible(true);
                markerOption.draggable(false);//设置Marker可拖动
                //0在线，1离线，2故障
                if("0".equals(bean.getDeviceStatus())){
                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(),R.drawable.camera_normal)));
                }else if("1".equals(bean.getDeviceStatus())){
                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(),R.drawable.camera_gray)));
                }else if("2".equals(bean.getDeviceStatus())){
                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(),R.drawable.camera_red)));
                }
                // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                markerOption.setFlat(true);//设置marker平贴地图效果

                Marker marker = mAMap.addMarker(markerOption);

                marker.setObject(bean);
                markers.add(marker);
            }

            for (int i = 0;i<mEnvList.size();i++){
                DataQueryVoBean bean = mEnvList.get(i);
                MarkerOptions markerOption = new MarkerOptions();
                double lat = Double.parseDouble(bean.getLatitude());
                double lon = Double.parseDouble(bean.getLongitude());
                markerOption.position(new LatLng(lat,lon));
                markerOption.visible(true);
                markerOption.draggable(false);//设置Marker可拖动
                //0在线，1离线，2故障
                if(0 == bean.getDeviceStatus()){
                    double pm10 = bean.getPm10() == null ? 0 : bean.getPm10();
                   if(pm10 <= 0){
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(),R.drawable.environment_blue)));
                    }else if(pm10 <= 50){
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(),R.drawable.environment_green)));
                    }else if(pm10 <= 150){
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(),R.drawable.environment_yellow)));
                    }else if(pm10 <= 250){
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(),R.drawable.environment_orange)));
                    }else if(pm10 <= 350){
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(),R.drawable.environment_red)));
                    }else if(pm10 <= 420){
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(),R.drawable.environment_pink)));
                    }else {
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(),R.drawable.environment_dark)));
                    };
                }else if(1 == bean.getDeviceStatus()){
                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(),R.drawable.environment_gray)));
                }else if(2 == bean.getDeviceStatus()){
                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(),R.drawable.environment_red)));
                }
                // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                markerOption.setFlat(true);//设置marker平贴地图效果

                Marker marker = mAMap.addMarker(markerOption);

                marker.setObject(bean);
                markers.add(marker);
            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mMapView.onDestroy();
        mapContentView.removeView(mMapView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
        zoom = mAMap.getCameraPosition().zoom;
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mMapView != null){
            mMapView.onSaveInstanceState(outState);
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getObject() != null){
            //视频设备
            if(marker.getObject() instanceof DevicesBean){
                DevicesBean bean = (DevicesBean) marker.getObject();
                currentVideoBean = bean;
                if(bean.getDeviceCoding().length() >= 10){
                    tv_deviceNumber.setText(bean.getDeviceCoding().substring(0,10));
                } else {
                    tv_deviceNumber.setText(bean.getDeviceCoding());
                }
                if("0".equals(bean.getDeviceStatus())){
                    tv_isOnline.setText("在线");
                    tv_isOnline.setBackgroundResource(R.drawable.shape_map_online);
                } else if("1".equals(bean.getDeviceStatus())){
                    tv_isOnline.setText("离线");
                    tv_isOnline.setBackgroundResource(R.drawable.shape_offline);
                } else if("2".equals(bean.getDeviceStatus())){
                    tv_isOnline.setText("故障");
                    tv_isOnline.setBackgroundResource(R.drawable.shape_map_bad);
                }
                tv_deviceTime.setText("安装日期：" + bean.getInstallTime().substring(0,10));
                tv_deviceAddress.setText(bean.getDeviceName());
                if("0".equals(bean.getDeviceStatus())){
                    videoView.setClickable(true);
                    videoView.setEnabled(true);
                    historyView.setClickable(true);
                    historyView.setEnabled(true);
                    galleryView.setClickable(true);
                    galleryView.setEnabled(true);
                    iv_video.setImageResource(R.drawable.time);
                    iv_history.setImageResource(R.drawable.history);
                    iv_gallery.setImageResource(R.drawable.capture);
                    tv_video.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_history.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_gallery.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    videoView.setClickable(false);
                    videoView.setEnabled(false);
                    historyView.setClickable(true);
                    historyView.setEnabled(true);
                    galleryView.setClickable(false);
                    galleryView.setEnabled(false);
                    iv_video.setImageResource(R.drawable.timedisable);
                    iv_history.setImageResource(R.drawable.history);
                    iv_gallery.setImageResource(R.drawable.capturedisable);
                    tv_video.setTextColor(getResources().getColor(R.color.gray_9999));
                    tv_history.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_gallery.setTextColor(getResources().getColor(R.color.gray_9999));
                }
                tv_video.setText("实时视频");
                tv_history.setText("历史监控");
                tv_gallery.setText("视频抓拍");

                eviorment_view.setVisibility(View.GONE);
                background_line.setVisibility(View.VISIBLE);
                galleryView.setVisibility(View.VISIBLE);
                initLocation(new LatLng(Double.parseDouble(bean.getLatitude()),
                                Double.parseDouble(bean.getLongitude())));

                //环境设备
            }else if(marker.getObject() instanceof DataQueryVoBean){
                DataQueryVoBean bean = (DataQueryVoBean) marker.getObject();
                currentEnvirBean = bean;
                tv_deviceNumber.setText(bean.getDeviceCoding());
                if(0 == bean.getDeviceStatus()){
                    tv_isOnline.setText("在线");
                    tv_isOnline.setBackgroundResource(R.drawable.shape_map_online);
                } else if(1 == bean.getDeviceStatus()){
                    tv_isOnline.setText("离线");
                    tv_isOnline.setBackgroundResource(R.drawable.shape_offline);
                } else if(2 == bean.getDeviceStatus()){
                    tv_isOnline.setText("故障");
                    tv_isOnline.setBackgroundResource(R.drawable.shape_map_bad);
                }
                tv_deviceTime.setText("安装日期：" + bean.getInstallTime().substring(0,10));
                tv_deviceAddress.setText(bean.getDeviceName());
                if(0 == bean.getDeviceStatus()){
                    videoView.setClickable(true);
                    videoView.setEnabled(true);
                    historyView.setClickable(true);
                    historyView.setEnabled(true);
                    galleryView.setClickable(true);
                    galleryView.setEnabled(true);
                    iv_video.setImageResource(R.drawable.time);
                    iv_history.setImageResource(R.drawable.history);
                    iv_gallery.setImageResource(R.drawable.capture);
                    tv_video.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_history.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_gallery.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    videoView.setClickable(false);
                    videoView.setEnabled(false);
                    historyView.setClickable(true);
                    historyView.setEnabled(true);
                    galleryView.setClickable(false);
                    galleryView.setEnabled(false);
                    iv_video.setImageResource(R.drawable.timedisable);
                    iv_history.setImageResource(R.drawable.history);
                    iv_gallery.setImageResource(R.drawable.capturedisable);
                    tv_video.setTextColor(getResources().getColor(R.color.gray_9999));
                    tv_history.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_gallery.setTextColor(getResources().getColor(R.color.gray_9999));
                }
                eviorment_view.setVisibility(View.VISIBLE);
                background_line.setVisibility(View.GONE);
                galleryView.setVisibility(View.GONE);
                tv_video.setText("实时数据");
                tv_history.setText("历史数据");

                String pm10 = "";
                double d_pm10 = bean.getPm10() == null ? 0 : bean.getPm10();
                int pm_10 = (int) d_pm10;

                double d_pm25 = bean.getPm2_5() == null ? 0 : bean.getPm2_5();
                int pm_25 = (int) d_pm25;

                double d_co2 = bean.getCo2() == null ? 0 : bean.getCo2();
                int pm_co2 = (int) d_co2;

                if(pm_10 < 50){
                    pm10 = "PM10：<font color='" + COLOR_50 + "'>" + pm_10 + "</font>";
                } else if(pm_10 < 150){
                    pm10 = "PM10：<font color='" + COLOR_150 + "'>" + pm_10 + "</font>";
                } else if(pm_10 < 250){
                    pm10 = "PM10：<font color='" + COLOR_250 + "'>" + pm_10 + "</font>";
                } else if(pm_10 < 350){
                    pm10 = "PM10：<font color='" + COLOR_350 + "'>" + pm_10 + "</font>";
                } else if(pm_10 < 420){
                    pm10 = "PM10：<font color='" + COLOR_420 + "'>" + pm_10 + "</font>";
                } else {
                    pm10 = "PM10：<font color='" + COLOR_600 + "'>" + pm_10 + "</font>";
                }
                tv_pm10.setText(Html.fromHtml(pm10));
                String pm25 = "PM2.5：<font color='" + COLOR_0 + "'>" + pm_25 + "</font>";
                tv_pm25.setText(Html.fromHtml(pm25));
                String co2 = "CO2：<font color='" + COLOR_0 + "'>" + pm_co2 + "</font>";
                tv_pmco2.setText(Html.fromHtml(co2));


                initLocation(new LatLng(Double.parseDouble(bean.getLatitude()),
                        Double.parseDouble(bean.getLongitude())));
            }
            addRoundMarker();
            mPopWindow.showAtLocation(mMapView, Gravity.BOTTOM,0,DensityUtils.dip2px(App.getAppContext(),-8));
        }

        return true;
    }

    private void addRoundMarker(){
        MarkerOptions markerOption1 = new MarkerOptions();
        if("实时视频".equals(tv_video.getText())){
            markerOption1.position(new LatLng(Double.parseDouble(currentVideoBean.getLatitude()
            ), Double.parseDouble(currentVideoBean.getLongitude())));
        }else if("实时数据".equals(tv_video.getText())){
            markerOption1.position(new LatLng(Double.parseDouble(currentEnvirBean.getLatitude()
            ), Double.parseDouble(currentEnvirBean.getLongitude())));
        }

        markerOption1.visible(true);

        markerOption1.draggable(false);//设置Marker可拖动
        View centerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_map_video_monitor_center,null);
        markerOption1.icon(BitmapDescriptorFactory.fromView(centerView));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption1.setFlat(true);//设置marker平贴地图效果

        roundMarker = mAMap.addMarker(markerOption1);
        roundMarker.setAnchor(0.5f,0.5f);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.video:
                if("实时视频".equals(tv_video.getText())){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("resCode", currentVideoBean.getDeviceCoding());
                    bundle.putInt("resSubType", currentVideoBean.getCameraType());

                    if (!NetworkUtils.isConnected()){
                        ToastUtils.showShort(mContext.getText(R.string.network_can_not_be_used_toast).toString());
                        return;
                    } else if (!HttpPost.mVideoIsLogin) {
                        Utils.showInitVideoServerDialog((BaseActivity) getActivity(), Utils.ENTER_REAL_TIME_VIDEO, bundle);
                        return;
                    }

                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), VideoPlayActivity.class);
                    startActivity(intent);
                } else if("实时数据".equals(tv_video.getText())){
                    //实时数据
                    Intent intent = new Intent();
                    intent.putExtra("id",currentEnvirBean.getDeviceId());
                    intent.putExtra("address",currentEnvirBean.getDeviceName());
                    intent.setClass(getActivity(), PMDataInfoActivity.class);
                    this.startActivity(intent);
                }
                break;
            case R.id.history:
                if("历史监控".equals(tv_history.getText())){

                    Date now = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String beginTime = formatter.format(now) + " 00:00:00";
                    String endTime = formatter2.format(now);

                    Intent intent1 = new Intent();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("resCode", currentVideoBean.getDeviceCoding());
                    bundle1.putString("resSubType", currentVideoBean.getCameraType() + "");
                    bundle1.putString("resName", currentVideoBean.getDeviceName());
                    bundle1.putBoolean("isOnline", "0".equals(currentVideoBean.getDeviceStatus()));
                    bundle1.putString("beginTime", beginTime);
                    bundle1.putString("endTime", endTime);
                    bundle1.putInt("position", 0);

                    if (!NetworkUtils.isConnected()){
                        ToastUtils.showShort(mContext.getText(R.string.network_can_not_be_used_toast).toString());
                        return;
                    } else if (!HttpPost.mVideoIsLogin) {
                        Utils.showInitVideoServerDialog((BaseActivity) getActivity(), Utils.ENTER_HISTORICAL_VIDEO, bundle1);
                        return;
                    }

                    //Toast.makeText(mContext, "ViewHolder: " +  ((ViewHolder)rootView.getTag()).name.getText().toString(), Toast.LENGTH_SHORT).show();
                    intent1.putExtras(bundle1);
                    intent1.setClass(getActivity(), VideoRePlayActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(intent1);
                } else if("历史数据".equals(tv_history.getText())){
                    //历史数据
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PMHistoryInfoActivity.class);
                    intent.putExtra("id",currentEnvirBean.getDeviceId());
                    intent.putExtra("address",currentEnvirBean.getDeviceName());
                    this.startActivity(intent);
                }
                break;
            case R.id.gallery:
                //打开系统相册浏览照片  
                PermissionsChecker mPermissionsChecker = new PermissionsChecker(mContext);
                if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                    PermissionsActivity.startActivityForResult(getActivity(), REQUEST_CODE, PERMISSIONS);
                }else {
                    mListImageInfo.clear();
                    mlistPhotoInfo.clear();
                    new ImageAsyncTask().execute();
                }
                break;

            case R.id.btn_back:
                FragmentTabHost tabHost = (FragmentTabHost) getActivity().findViewById(R.id.tab_host);
                tabHost.setCurrentTab(0);
                break;
            case R.id.btn_icon:
                startActivity(new Intent(getActivity(),MapSearchActivity.class));
                break;
            case R.id.iv_dismiss:
                mPopWindow.dismiss();
                break;
        }
    }

    public void addRoundLine(){
        List<LatLng> latLngs = MapUtils.getAroundLatlons();
        Polyline polyline = mAMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.parseColor("#3464dd")));
    }

    private static final int REQUEST_CODE = 100; // 权限检查请求码
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private List<PhotoInfo> mlistPhotoInfo = new ArrayList<PhotoInfo>();
    private List<AlbumInfo> mListImageInfo = new ArrayList<AlbumInfo>();
    private class ImageAsyncTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected Object doInBackground(Void... params) {
            //获取缩略图
            ThumbnailsUtil.clear();
            ContentResolver sContentResolver = mContext.getContentResolver();
            String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA };
            Cursor cur = sContentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);

            if (cur!=null&&cur.moveToFirst()) {
                int image_id;
                String image_path;
                int image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
                int dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                do {
                    image_id = cur.getInt(image_idColumn);
                    image_path = cur.getString(dataColumn);
                    ThumbnailsUtil.put(image_id, "file://"+image_path);
                } while (cur.moveToNext());
            }
            //获取原图
            Cursor cursor = sContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "date_modified DESC");
            String _path="_data";
            String _album="bucket_display_name";
            HashMap<String,AlbumInfo> myhash = new HashMap<String, AlbumInfo>();
            AlbumInfo albumInfo = null;
            PhotoInfo photoInfo = null;
            if (cursor!=null&&cursor.moveToFirst())
            {
                do{
                    int index = 0;
                    int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String path = cursor.getString(cursor.getColumnIndex(_path));
                    String album = cursor.getString(cursor.getColumnIndex(_album));
                    List<PhotoInfo> stringList = new ArrayList<PhotoInfo>();
                    photoInfo = new PhotoInfo();
                    if(myhash.containsKey(album)){
                        albumInfo = myhash.remove(album);
                        if(mListImageInfo.contains(albumInfo)) {
                            index = mListImageInfo.indexOf(albumInfo);
                        }
                        photoInfo.setImage_id(_id);
                        photoInfo.setPath_file("file://"+path);
                        photoInfo.setPath_absolute(path);
                        albumInfo.getList().add(photoInfo);
                        mListImageInfo.set(index, albumInfo);
                        myhash.put(album, albumInfo);
                        //Log.i("zyf", albumInfo.toString() + "\n" + album);
                    }else{
                        albumInfo = new AlbumInfo();
                        stringList.clear();
                        photoInfo.setImage_id(_id);
                        photoInfo.setPath_file("file://"+path);
                        photoInfo.setPath_absolute(path);
                        stringList.add(photoInfo);
                        albumInfo.setImage_id(_id);
                        albumInfo.setPath_file("file://"+path);
                        albumInfo.setPath_absolute(path);
                        albumInfo.setName_album(album);
                        albumInfo.setList(stringList);
                        mListImageInfo.add(albumInfo);
                        myhash.put(album, albumInfo);
                        //Log.i("zyf", albumInfo.toString() + "\n" + album);
                    }
                }while (cursor.moveToNext());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            //Log.i("zzz", FilesUtils.SNATCH_PATH + mDevicesBean.getDeviceCoding() + "/");
            for (int i=0; i<mListImageInfo.size(); i++) {
                if (mListImageInfo.get(i).getPath_absolute().contains(FilesUtils.SNATCH_PATH + currentVideoBean.getDeviceCoding() + "/")) {
                    mlistPhotoInfo.addAll(mListImageInfo.get(i).getList());
                }
            }

            if (mlistPhotoInfo.size() <= 0 ) {
                ToastUtils.showShort(getText(R.string.snatch_photo_size_0_toast).toString());
                return;
            } else {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                PhotoList photo = new PhotoList();
                photo.setList(mlistPhotoInfo);
                bundle.putSerializable("list", photo);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(mContext, SnapPicturesActivity.class);
                mContext.startActivity(intent);
            }
        }
    }
}
