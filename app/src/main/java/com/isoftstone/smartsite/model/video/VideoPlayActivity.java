package com.isoftstone.smartsite.model.video;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.common.App;
import com.isoftstone.smartsite.model.main.view.RoundMenuView;
import com.isoftstone.smartsite.model.system.ui.ActionSheetDialog;
import com.isoftstone.smartsite.utils.FilesUtils;
import com.isoftstone.smartsite.utils.MediaScanner;
import com.isoftstone.smartsite.utils.ToastUtils;
import com.uniview.airimos.Player;
import com.uniview.airimos.listener.OnPtzCommandListener;
import com.uniview.airimos.listener.OnStartLiveListener;
import com.uniview.airimos.listener.OnStopLiveListener;
import com.uniview.airimos.listener.OnStreamChangeListener;
import com.uniview.airimos.manager.ServiceManager;
import com.uniview.airimos.parameter.PtzCommandParam;
import com.uniview.airimos.parameter.StartLiveParam;
import com.uniview.airimos.thread.RecvStreamThread;
import com.uniview.airimos.parameter.StreamParam;
import com.uniview.airimos.util.StreamUtil;

/**
 * Created by gone on 2017/10/17.
 * modifed by zhangyinfu on 2017/10/19
 */

public class VideoPlayActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "zyf_VideoPlayActivity";

    private SurfaceView mSurfaceView;
    private Player mPlayer;
    private Context mContext;
    private RecvStreamThread mRecvStreamThread = null;
    private RoundMenuView mRoundMenuView;
    private ImageView mCaptureView;
    private String mCameraCode;
    private static  final int SELECT_SOLID_COLOR = 0x00000000;
    private static  final int STROKR_COKOR = 0x00000000;
    private boolean isTouched = false;

    private int mSurfaceViewWidth;
    private int mSurfaceViewHeight;
    //固定摄像机：1; 云台摄像机：2; 高清固定摄像机：3; 高清云台摄像机：4; 车载摄像机：5; 不可控标清摄像机：6; 不可控高清摄像机：7;
    private static final int CAMERA_TYPE_TOW = 2;
    private static final int CAMERA_TYPE_FOUR = 4;

    private ImageView mBackView;
    private ImageView mChangePositionView;
    private boolean isNormalShow = true;//true 标识正常显示, false 标识反向显示
    private Button mZoomTeleView;
    private Button mZoomWideView;
    private boolean isZoomTeleTouched = false;
    private boolean isZoomWideTouched = false;

    private TextView mDefinitionView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        /**if(!HttpPost.mVideoIsLogin){
            Toast.makeText(this,"观看视频需要联网，请确认网络是否连接成功",Toast.LENGTH_LONG).show();
            finish();
        }*/

        setContentView(R.layout.activity_video_play);
        mContext = this;
        //SurfaceView用于渲染
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);

        //监听SurfaceView的变化
        mSurfaceView.getHolder().addCallback(new surfaceCallback());

        mSurfaceView.setZOrderOnTop(true);
        mSurfaceView.setZOrderMediaOverlay(true);
        //初始化一个Player对象

        mPlayer = new Player();
        mPlayer.AVInitialize(mSurfaceView.getHolder());

        mRoundMenuView = (RoundMenuView)findViewById(R.id.round_menu_view);

        //获取设备屏幕大小信息
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mSurfaceViewWidth = dm.widthPixels;
        mSurfaceViewHeight = dm.heightPixels;

        mCaptureView = (ImageView) findViewById(R.id.capture_view);
        mCaptureView.setOnClickListener(this);
        mBackView = (ImageView) findViewById(R.id.iv_back);
        mBackView.setOnClickListener(this);
        mChangePositionView = (ImageView) findViewById(R.id.iv_change_position);
        mChangePositionView.setOnClickListener(this);
        mZoomTeleView = (Button) findViewById(R.id.zoom_tele);
        mZoomWideView = (Button) findViewById(R.id.zoom_wide);
        LinearLayout zoomLayout = (LinearLayout) findViewById(R.id.zoom_layout);

        //mZoomTeleView.setOnClickListener(this);
        mZoomTeleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if (!isZoomTeleTouched) {
                        ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ZOOMTELE);
                        isZoomTeleTouched = true;
                    }
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ZOOMTELESTOP);
                    isZoomTeleTouched = false;
                }
                return  true;
            }
        });
        //mZoomWideView.setOnClickListener(this);
        mZoomWideView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if (!isZoomWideTouched) {
                        ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ZOOMWIDE);
                        isZoomWideTouched = true;
                    }
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ZOOMWIDESTOP);
                    isZoomWideTouched = false;
                }
                return  true;
            }
        });


        mDefinitionView = (TextView) findViewById(R.id.video_definition_view);
        mDefinitionView.setOnClickListener(this);


        /*获取Intent中的Bundle对象*/
        if(bundle == null) {
            bundle = this.getIntent().getExtras();
        }
        /*获取Bundle中的数据，注意类型和key*/
        mCameraCode = bundle.getString("resCode");
        int resSubType = bundle.getInt("resSubType");
        Log.i(TAG,"--------------mCameraCode-------" + mCameraCode + ";   resSubType = " +  resSubType);

        if( (CAMERA_TYPE_TOW == resSubType) || (CAMERA_TYPE_FOUR ==  resSubType)) {
            Log.i(TAG,"--------------zyf----VISIBLE---");
            //初始化摇杆控件
            mRoundMenuView.setVisibility(View.VISIBLE);
            initRoundMenuView();
            zoomLayout.setVisibility(View.VISIBLE);
        } else {
            Log.i(TAG,"--------------zyf----GONE---");
            mRoundMenuView.setVisibility(View.GONE);
            zoomLayout.setVisibility(View.GONE);
        }
        //startLive(mCameraCode);
    }

    /**
     * 启动实况
     *
     * @param cameraCode 摄像机编码
     */
    public void startLive(String cameraCode) {
        try {
            //启动实况的结果监听
            OnStartLiveListener listener = new OnStartLiveListener() {
                @Override
                public void onStartLiveResult(long errorCode, String errorDesc, String playSession) {
                    if (errorCode == 0){
                        //将播放会话设给Player
                        mPlayer.setPlaySession(playSession);

                        //先停掉别的接收流线程
                        if (mRecvStreamThread != null) {
                            mPlayer.AVStopPlay();
                            mRecvStreamThread.interrupt();
                            mRecvStreamThread = null;
                        }

                        //启动播放解码
                        mPlayer.AVStartPlay();
                        //修改監控界面大小為當前屏幕大小
                        //mPlayer.changeDisplaySize(mSurfaceViewWidth, mSurfaceViewHeight);

                        //收流线程启动
                        mRecvStreamThread = new RecvStreamThread(mPlayer, playSession);
                        mRecvStreamThread.start();
                        Log.i(TAG,"--------------zyf----mRecvStreamThread---");
                    }else{
                        ToastUtils.showShort(errorDesc);
                    }

                }

            };

            //设置启动实况的参数
            StartLiveParam p = new StartLiveParam();
            p.setCameraCode(cameraCode);
            p.setUseSecondStream(true); //使用辅流
            p.setBitrate(32* 8);   //32KB的码率
            p.setFramerate(18);     //18帧率
            p.setResolution(StreamParam.ResolutionType.CIF4);     //4CIF分辨率

            //启动实况
            ServiceManager.startLive(p, listener);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopLive() {
        //停止实况，第二个参数是null表示不接收结果
        ServiceManager.stopLive(mPlayer.getPlaySession(), new OnStopLiveListener() {
            @Override
            public void onStopLiveResult(long errorCode, String errorMsg) {
                if (errorCode == 0){
                    if (mRecvStreamThread != null){
                        mRecvStreamThread.interrupt();
                        mRecvStreamThread = null;
                    }
                } else {
                    Toast.makeText(VideoPlayActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
                }

                //不管服务器停止结果是否成功，我们都会停止Player播放解码
                if (null != mPlayer) {
                    mPlayer.AVStopPlay();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        if (null != mPlayer && !mPlayer.AVIsPlaying()) {
            try {
                startLive(mCameraCode);
            } catch (Exception e) {
                Log.i(TAG, "startLive video throw a exception : " + e.getMessage());
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (null != mPlayer && mPlayer.AVIsPlaying()) {
            try {
                stopLive();
            } catch (Exception e) {
                Log.i(TAG, "stopLive video throw a exception : " + e.getMessage());
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //销毁Player
        if (null != mPlayer) {
            mPlayer.AVFinalize();
            mPlayer = null;
        }

        super.onDestroy();
    }

    public void initRoundMenuView() {
        RoundMenuView.RoundMenu roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = SELECT_SOLID_COLOR;//Integer.parseInt(toHexEncoding(Color.GRAY));
        roundMenu.strokeColor = STROKR_COKOR;//Integer.parseInt(toHexEncoding(Color.GRAY));//ColorUtils.getColor(mContext, R.color.gray_9999);
        roundMenu.icon= drawableToBitmap(mContext,R.drawable.videoright);
        /**roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort("点击了down");
                ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.TILTDOWN);
            }
        };*/
        roundMenu.onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(!isTouched) {
                            ToastUtils.showShort("向下");
                            ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.TILTDOWN);
                            isTouched = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.TILTDOWNSTOP);
                        isTouched = false;
                        break;
                }
                return false;
            }
        };

        mRoundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = SELECT_SOLID_COLOR;//ColorUtils.getColor(getActivity(), R.color.gray_9999);
        roundMenu.strokeColor = STROKR_COKOR;//ColorUtils.getColor(getActivity(), R.color.gray_9999);
        roundMenu.icon= drawableToBitmap(mContext,R.drawable.videoright);//ImageUtils.drawable2Bitmap(getActivity(),R.drawable.ic_right);
        /**roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort("点击了left");
                ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.PANLEFT);
            }
        };*/
        roundMenu.onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(!isTouched) {
                            ToastUtils.showShort("向左");
                            ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.PANLEFT);
                            isTouched = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.PANLEFTSTOP);
                        isTouched = false;
                        break;
                }
                return false;
            }
        };

        mRoundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = SELECT_SOLID_COLOR;//Integer.parseInt(toHexEncoding(R.color.gray_9999));//ColorUtils.getColor(getActivity(), R.color.gray_9999);
        roundMenu.strokeColor = STROKR_COKOR;//Integer.parseInt(toHexEncoding(R.color.gray_9999));//ColorUtils.getColor(getActivity(), R.color.gray_9999);
        roundMenu.icon= drawableToBitmap(mContext,R.drawable.videoright);//ImageUtils.drawable2Bitmap(getActivity(),R.drawable.ic_right);
        /**roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort("点击了up");
                ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.TILTUP);
            }
        };*/
        roundMenu.onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(!isTouched) {
                            ToastUtils.showShort("向上");
                            ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.TILTUP);
                            isTouched = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.TILTUPSTOP);
                        isTouched = false;
                        break;
                }
                return false;
            }
        };

        mRoundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = SELECT_SOLID_COLOR;//Integer.parseInt(toHexEncoding(R.color.gray_9999));//ColorUtils.getColor(getActivity(), R.color.gray_9999);
        roundMenu.strokeColor = STROKR_COKOR;//Integer.parseInt(toHexEncoding(R.color.gray_9999));//ColorUtils.getColor(getActivity(), R.color.gray_9999);
        roundMenu.icon= drawableToBitmap(mContext,R.drawable.videoright);//ImageUtils.drawable2Bitmap(getActivity(),R.drawable.ic_right);
        /**roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort("点击了right");
                ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.PANRIGHT);
            }
        };*/
        roundMenu.onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(!isTouched) {
                            ToastUtils.showShort("向右");
                            ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.PANRIGHT);
                            isTouched = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.PANRIGHTSTOP);
                        isTouched = false;
                        break;
                }
                return false;
            }
        };

        mRoundMenuView.addRoundMenu(roundMenu);

        mRoundMenuView.setCoreMenu(STROKR_COKOR, SELECT_SOLID_COLOR, STROKR_COKOR
                , 1, 0.43, drawableToBitmap(mContext, R.drawable.videopoint)
                , new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ToastUtils.showShort(" ");
                        ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ALLSTOP);
                    }
                }, new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
    }

    /**
     * 云台控制
     * */
    public void ptzCommand(String cameraCode, int directionCode){

        //云台命令参数
        PtzCommandParam param = new PtzCommandParam();
        param.setCameraCode(cameraCode);
        param.setCmd(directionCode);
        param.setSpeed1(3);
        param.setSpeed2(3);

        try {
            OnPtzCommandListener listener = new OnPtzCommandListener() {
                @Override
                public void onPtzCommandResult(long errorCode, String errorDesc) {
                    if (errorCode != 0) {
                        Toast.makeText(VideoPlayActivity.this, errorDesc, Toast.LENGTH_SHORT).show();
                    }
                }
            };
            //云台控制接口
            ServiceManager.ptzCommand(param, listener);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static String toHexEncoding(int color) {
        String R, G, B;
        StringBuffer sb = new StringBuffer();
        R = Integer.toHexString(Color.red(color));
        G = Integer.toHexString(Color.green(color));
        B = Integer.toHexString(Color.blue(color));
        //判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
        sb.append("0x");
        sb.append(R);
        sb.append(G);
        sb.append(B);
        return sb.toString();
    }

    public static Bitmap drawableToBitmap(Context context, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);

        return BitmapFactory.decodeResource(context.getResources(), resId);
        /**Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;*/

        /**int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.capture_view:
                //抓拍图片，返回路径
                String path = mPlayer.snatch(FilesUtils.getSnatchPath(mCameraCode));
                if (null != path) {
                    Toast.makeText(VideoPlayActivity.this, path, Toast.LENGTH_SHORT).show();

                    /**try {
                        MediaStore.Images.Media.insertImage(getContentResolver(), filePath, fileName, null);
                    } catch (FileNotFoundException e) {
                        Log.d("zzz", "FileNotFoundException : "   +  e.getMessage());
                        e.printStackTrace();
                    }*/
                    String filePaths = path.substring(0, path.lastIndexOf("/") -1);
                    String mineType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg");
                    MediaScanner.getInstace().scanFile(mContext, new MediaScanner.ScanFile(filePaths,mineType));
                }
                break;
            case R.id.iv_back:
                VideoPlayActivity.this.finish();
                break;
            case R.id.iv_change_position:
                isNormalShow = !isNormalShow;
                //反向显示操作
                break;
            case R.id.zoom_tele:
                ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ZOOMTELE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ZOOMTELESTOP);
                    }
                }, 300);
                break;
            case R.id.zoom_wide:
                ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ZOOMWIDE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ZOOMWIDESTOP);
                    }
                }, 300);
                break;
            case R.id.video_definition_view:
                new ActionSheetDialog(VideoPlayActivity.this)
                        .builder(false)
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem(mContext.getText(R.string.video_definition_d1).toString(),
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {
                                        if (mDefinitionView.getText().toString().equals(mContext.getText(R.string.video_definition_d1).toString())) {
                                            //ToastUtils.showShort("当前视频清晰度已为" + mContext.getText(R.string.video_definition_d1).toString() + "，无需切换。");
                                            return;
                                        }
                                        changeVideoStream(StreamUtil.STREAM_TYPE_C);
                                    }
                                })
                        .addSheetItem(mContext.getText(R.string.video_definitdion_cif4).toString(),
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {
                                        if (mDefinitionView.getText().toString().equals(mContext.getText(R.string.video_definitdion_cif4).toString())) {
                                            //ToastUtils.showShort("当前视频清晰度已为" + mContext.getText(R.string.video_definitdion_cif4).toString() + "，无需切换。");
                                            return;
                                        }
                                        changeVideoStream(StreamUtil.STREAM_TYPE_B);

                                    }
                                })
                        .addSheetItem(mContext.getText(R.string.video_definition_cif).toString(),
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {
                                        if (mDefinitionView.getText().toString().equals(mContext.getText(R.string.video_definition_cif).toString())) {
                                            //ToastUtils.showShort("当前视频清晰度已为" + mContext.getText(R.string.video_definition_cif).toString() + "，无需切换。");
                                            return;
                                        }
                                        changeVideoStream(StreamUtil.STREAM_TYPE_F);
                                        mDefinitionView.setText(R.string.video_definition_cif);
                                    }
                                }).show();
                break;
            default:
                break;
        }

    }

    private void changeVideoStream(final int streamType) {

        StreamParam streamParam = StreamUtil.getStreamInfo(streamType, VideoPlayActivity.this);//D1 清晰码流

        ServiceManager.changeStream(mPlayer.getPlaySession(), streamParam, new OnStreamChangeListener() {
            @Override
            public void onStreamChangeResult(StreamParam streamParam, long errorCode, String errorDesc) {
                if (0 == errorCode) {
                    //Toast.makeText(VideoPlayActivity.this, "清晰度切换成功！", Toast.LENGTH_SHORT).show();
                    if (streamType == StreamUtil.STREAM_TYPE_C) {
                        mDefinitionView.setText(R.string.video_definition_d1);
                    } else if (streamType == StreamUtil.STREAM_TYPE_B){
                        mDefinitionView.setText(R.string.video_definitdion_cif4);
                    } else {
                        mDefinitionView.setText(R.string.video_definition_cif);
                    }

                } else {
                    Toast.makeText(VideoPlayActivity.this, errorDesc, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    class surfaceCallback implements SurfaceHolder.Callback {

        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "===== surfaceCreated =====");
            if (null != mPlayer && !mPlayer.AVIsPlaying()) {
               // startLive(mCameraCode);
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //Log.d(TAG, "===== surfaceChanged =====");
            if (mPlayer != null) {//mPlayer.changeDisplaySize(width, height);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder arg0) {
            Log.i(TAG, "===== surfaceDestroyed =====");
            //stopLive();
        }
    }

    /*private   double nLenStart = 0;
    public boolean onTouchEvent(MotionEvent event) {

        int nCnt = event.getPointerCount();

        int n = event.getAction();
        if( (event.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN && 2 == nCnt) {
            //<span style="color:#ff0000;">2表示两个手指</span>
            for(int i=0; i< nCnt; i++)
            {
                float x = event.getX(i);
                float y = event.getY(i);

                Point pt = new Point((int)x, (int)y);

            }

            int xlen = Math.abs((int)event.getX(0) - (int)event.getX(1));
            int ylen = Math.abs((int)event.getY(0) - (int)event.getY(1));

            nLenStart = Math.sqrt((double)xlen*xlen + (double)ylen * ylen);


        } else if( (event.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP  && 2 == nCnt) {

            for(int i=0; i< nCnt; i++)
            {
                float x = event.getX(i);
                float y = event.getY(i);

                Point pt = new Point((int)x, (int)y);

            }

            int xlen = Math.abs((int)event.getX(0) - (int)event.getX(1));
            int ylen = Math.abs((int)event.getY(0) - (int)event.getY(1));

            double nLenEnd = Math.sqrt((double)xlen*xlen + (double)ylen * ylen);

            if(nLenEnd > nLenStart)//通过两个手指开始距离和结束距离，来判断放大缩小
            {
                Toast.makeText(getApplicationContext(), "放大", 2000).show();
                ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ZOOMTELE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ZOOMTELESTOP);
                    }
                }, 300);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "缩小", 2000).show();
                ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ZOOMWIDE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptzCommand(mCameraCode, PtzCommandParam.PTZ_CMD.ZOOMWIDESTOP);
                    }
                }, 300);
            }
        }
        return super.onTouchEvent(event);
    }*/

}
