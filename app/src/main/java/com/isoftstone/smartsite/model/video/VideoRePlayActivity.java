package com.isoftstone.smartsite.model.video;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.utils.DateUtils;
import com.isoftstone.smartsite.utils.FilesUtils;
import com.isoftstone.smartsite.utils.MediaScanner;
import com.isoftstone.smartsite.utils.ToastUtils;
import com.isoftstone.smartsite.widgets.CustomDatePicker;
import com.uniview.airimos.Player;
import com.uniview.airimos.listener.OnDragReplayListener;
import com.uniview.airimos.listener.OnGetReplayPosListener;
import com.uniview.airimos.listener.OnQueryReplayListener;
import com.uniview.airimos.listener.OnStartReplayListener;
import com.uniview.airimos.listener.OnStopReplayListener;
import com.uniview.airimos.manager.ServiceManager;
import com.uniview.airimos.obj.QueryCondition;
import com.uniview.airimos.obj.RecordInfo;
import com.uniview.airimos.parameter.QueryReplayParam;
import com.uniview.airimos.parameter.StartReplayParam;
import com.uniview.airimos.thread.RecvStreamThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhangyinfu on 2017/10/20.
 */

public class VideoRePlayActivity extends Activity implements  View.OnClickListener{
    private static final String TAG = "zyf_RePlayVideoActivity";

    private SurfaceView mSurfaceView;
    private Player mPlayer;
    private Context mContext;
    private RecvStreamThread mRecvStreamThread = null;
    private String mResCode;
    private String mBeginTime;
    private String mEndTime;
    private String mFileName;
    private int mPosition;
    private ImageView mBackView;

    //message.what
    private static final int STATE_CHANGED = 0x111; //播放状态变化
    private static final int VISIBLE_TOP_BOTTOM = 0x113;    //视频顶部，底部布局的显示隐藏
    private static final int SEEKBAR_TOUCHED = 0x114;   //滑动进度条被手动滑动后(快进快退)
    private static final int SYSTEM_TIME_CHANED = 0x115;   //系统时间发生改变时
    private static final int START_REPLAY_VIDEO = 0x116;   //开始播放历史视频
    private static final int STOP_REPLAY_VIDEO = 0x117;    //停止播放视频
    private static final int PAUSE_REPLAY_VIDEO = 0x118;    //暂停播放视频
    private static final int RESTART_REPLAY_VIDEO = 0x119;    //继续播放视频

    private static final int PLAY = 0;
    private static final int PAUSE = 1;
    private static final int STOP = 2;
    private int mVideoState = STOP; //播放状态

    private boolean isTopBottomVisible = true;  //视频顶部，底部布局显隐标识
    private long mStartDateTime;
    private long mEndDateTime;
    private String mCurrentTime;
    private int mCurrentProgress;
    private String mVideoBeginTime;
    private String mVideoEndTime;

    private SeekBar  mLandSeekBar;
    private SeekBar  mPortSeekBar;
    private ImageView mCaptureView;
    private ImageView mShowFullScreenView;
    private ImageView mShowSmallScreenView;
    private LinearLayout mPortLayout;
    private Toolbar mTitleToolbar;
    private LinearLayout mPlaySeekbarLandLayout;
    private RelativeLayout mPlaySeekbarPortLayout;

    private TextView mCameraCodeView;
    private TextView mCameraTypeView;
    private String mCameraType;
    private TextView mCameraNameView;
    private String mCameraName;
    private boolean isCameraOnLine = false;
    private ImageView mIsOnLineView;
    private TextView mBeginDateView;
    private TextView mBeginTimeView;
    private TextView mEndDateView;
    private TextView mEndTimeView;
    private RelativeLayout mBeginDateTimeLayout;
    private RelativeLayout mEndDateTimeLayout;
    private TextView mStartPlayView;
    private ImageView mPortStopPlayView;
    private ImageView mLandStopPlayView;
    private boolean isDraged = false;
    private String mDragTime;
    private boolean isStoped = false;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        /**if(!HttpPost.mVideoIsLogin){
            Toast.makeText(this,"观看视频需要联网，请确认网络是否连接成功",Toast.LENGTH_LONG).show();
            finish();
        }*/

        setContentView(R.layout.activity_video_replay);
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

        /*获取Intent中的Bundle对象*/
        if(bundle == null) {
            bundle = this.getIntent().getExtras();
        }

        /*获取Bundle中的数据，注意类型和key*/
        mResCode = bundle.getString("resCode");
        mBeginTime = bundle.getString("beginTime");
        mEndTime = bundle.getString("endTime");
        mFileName = bundle.getString("fileName");
        mPosition = bundle.getInt("position");
        mCameraName = bundle.getString("resName");
        mCameraType= bundle.getString("resSubType");
        isCameraOnLine = bundle.getBoolean("isOnline");


        mBackView = (ImageView) findViewById(R.id.iv_back);
        mBackView.setOnClickListener(this);
        mCaptureView = (ImageView)findViewById(R.id.capture_replay_view);
        mCaptureView.setOnClickListener(this);
        mShowFullScreenView = (ImageView) findViewById(R.id.show_full_screen_view);
        mShowFullScreenView.setOnClickListener(this);
        mShowSmallScreenView = (ImageView) findViewById(R.id.show_small_screen_view);
        mShowSmallScreenView.setOnClickListener(this);
        mPortLayout = (LinearLayout) findViewById(R.id.port_layout);
        mTitleToolbar = (Toolbar) findViewById(R.id.video_toolbar);
        mPlaySeekbarLandLayout = (LinearLayout) findViewById(R.id.play_seekbar_land_layout);
        mPlaySeekbarPortLayout = (RelativeLayout) findViewById(R.id.play_seekbar_port_layout);
        mCameraCodeView  = (TextView) findViewById(R.id.res_code_tv);
        mCameraCodeView.setText(mResCode);
        mCameraTypeView = (TextView) findViewById(R.id.res_type_tv);
        mCameraTypeView.setText(mCameraType);
        mCameraNameView = (TextView) findViewById(R.id.res_name_tv);
        mCameraNameView.setText(mCameraName);
        mIsOnLineView = (ImageView) findViewById(R.id.is_online_tv);
        if (isCameraOnLine) {
            mIsOnLineView.setImageResource(R.drawable.online);
        } else {
            mIsOnLineView.setImageResource(R.drawable.offline);
        }

        mBeginDateView = (TextView) findViewById(R.id.begin_date_txt);
        mBeginDateView.setText(mBeginTime.split(" ")[0]);
        mEndDateView = (TextView) findViewById(R.id.end_date_txt);
        mEndDateView.setText(mEndTime.split(" ")[0]);

        mBeginTimeView = (TextView) findViewById(R.id.begin_time_txt);
        mBeginTimeView.setText(mBeginTime.split(" ")[1]);
        mEndTimeView = (TextView) findViewById(R.id.end_time_txt);
        mEndTimeView.setText(mEndTime.split(" ")[1]);
        mBeginDateTimeLayout = (RelativeLayout) findViewById(R.id.begin_date_time_layout);
        mEndDateTimeLayout = (RelativeLayout) findViewById(R.id.end_date_time_layout);
        mBeginDateTimeLayout.setOnClickListener(this);
        mEndDateTimeLayout.setOnClickListener(this);

        mStartPlayView = (TextView) findViewById(R.id.start_play_view);
        mStartPlayView.setOnClickListener(this);
        mPortStopPlayView = (ImageView) findViewById(R.id.video_stop_for_port);
        mLandStopPlayView = (ImageView) findViewById(R.id.video_stop_for_land);
        mPortStopPlayView.setOnClickListener(this);
        mLandStopPlayView.setOnClickListener(this);




        mLandSeekBar = (SeekBar) findViewById(R.id.play_seekbar_for_land);
        mLandSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("seekBarProgress", seekBar.getProgress());
                message.setData(bundle);
                message.what = SEEKBAR_TOUCHED;

                if (mHandler.hasMessages(SEEKBAR_TOUCHED)) {
                    mHandler.removeMessages(SEEKBAR_TOUCHED);
                }

                mHandler.sendMessage(message);
            }
        });

        mPortSeekBar = (SeekBar) findViewById(R.id.play_seekbar_for_port);
        mPortSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() == 100) {
                    isDraged = false;
                    if (mHandler.hasMessages(STOP_REPLAY_VIDEO)) {
                        mHandler.removeMessages(STOP_REPLAY_VIDEO);
                    }
                    mHandler.sendEmptyMessage(STOP_REPLAY_VIDEO);
                } else {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("seekBarProgress", seekBar.getProgress());
                    message.setData(bundle);
                    message.what = SEEKBAR_TOUCHED;

                    if (mHandler.hasMessages(SEEKBAR_TOUCHED)) {
                        mHandler.removeMessages(SEEKBAR_TOUCHED);
                    }

                    mHandler.sendMessage(message);
                }
            }
        });

        visiblePlayerSeekbarLayout(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

        mPlaySeekbarPortLayout.setVisibility(View.GONE);

        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //if (null != mPlayer && mPlayer.AVIsPlaying()) {
                            //mHandler.sendEmptyMessage(STOP_REPLAY_VIDEO);
                        //} else {
                            if (mHandler.hasMessages(START_REPLAY_VIDEO)) {
                                mHandler.hasMessages(START_REPLAY_VIDEO);
                            }
                            mHandler.sendEmptyMessage(START_REPLAY_VIDEO);
                        //}
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 启动历史回放
     *
     * @param cameraCode 摄像机编码
     */
    private void startReplay(final String cameraCode, String beginTime, String endTime, final String fileName, final int position) {


        //查询回放记录参数
        QueryReplayParam p = new QueryReplayParam(cameraCode, DateUtils.checkDataTime(beginTime, true), DateUtils.checkDataTime(endTime, false), new QueryCondition(0, 100, true));

        //查询回放记录结果监听
        OnQueryReplayListener queryListener = new OnQueryReplayListener() {
            @Override
            public void onQueryReplayResult(long errorCode, String errorDesc, List<RecordInfo> recordList) {
                if (errorCode != 0 || recordList == null ){
                    Toast.makeText(mContext, errorDesc, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (recordList.size() <= 0) {
                    ToastUtils.showShort(mContext.getText(R.string.replay_video_error_for_no_list).toString());
                    return;
                }

                RecordInfo currentRecord;
                if (position < recordList.size()) {
                    currentRecord = recordList.get(position);
                } else {
                    currentRecord = recordList.get(0);
                }

                mVideoBeginTime = currentRecord.getBeginTime();
                mVideoEndTime = currentRecord.getEndTime();
                /**for (int i = 0; i < recordList.size(); i++) {
                    Log.i(TAG,"fileName= " + fileName);
                    Log.i(TAG,"xxxxName= " + recordList.get(i).getFileName());
                    if (null != fileName && fileName.equals(recordList.get(i).getFileName())) {
                        ToastUtils.showShort("i = " + i);
                        currentRecord = recordList.get(i);
                    }
                }

                if (currentRecord == null) {
                    Toast.makeText(mContext,"此录像出现问题 无法播放...size =" + recordList.size(),Toast.LENGTH_SHORT).show();
                    return;
                }*/

                //启动回放的参数
                StartReplayParam p = new StartReplayParam();
                p.setCameraCode(cameraCode);
                p.setRecodeInfo(currentRecord);
                p.setBitrate(64 * 8);  //64KB码率
                p.setFramerate(18);     //15帧率
                p.setResolution(2);     //4CIF分辨率


                OnStartReplayListener listener = new OnStartReplayListener() {
                    @Override
                    public void onStartReplayResult(long errorCode, String errorDesc, String playSession) {
                        //设播放会话给Player
                        if (mPlayer == null) {
                            return;
                        }
                        mPlayer.setPlaySession(playSession);

                        //先停掉已有的播放
                        if (mRecvStreamThread != null) {
                            mPlayer.AVStopPlay();
                            mRecvStreamThread.interrupt();
                            mRecvStreamThread = null;
                        }

                        //启动播放解码
                        mPlayer.AVStartPlay();

                        //启动收流线程
                        mRecvStreamThread = new RecvStreamThread(mPlayer, playSession);
                        mRecvStreamThread.start();

                        mStartDateTime = getNowDateTime();
                        mSurfaceView.setBackground(null);
                        mSurfaceView.setOnTouchListener(null);
                        changeState(PLAY);

                        mLandSeekBar.setProgress(0);
                        mPortSeekBar.setProgress(0);
                        isStoped = false;

                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                            mPlaySeekbarPortLayout.setVisibility(View.GONE);
                        } else {
                            mPlaySeekbarPortLayout.setVisibility(View.VISIBLE);
                        }
                        mPortStopPlayView.setImageDrawable(getResources().getDrawable(R.drawable.video_stop));
                        mLandStopPlayView.setImageDrawable(getResources().getDrawable(R.drawable.video_stop));

                        //mPortSeekBar.setEnabled(true);
                        //mLandSeekBar.setEnabled(true);
                        mPortSeekBar.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return false;
                            }
                        });

                        mLandSeekBar.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return false;
                            }
                        });
                    }
                };


                Log.i("zzz", "startReplay..........");
                //启动回放
                ServiceManager.startReplay(p, listener);
            }
        };

        //先查询指定时间段内有的回放记录
        ServiceManager.queryReplay(p, queryListener);

    }

    public void  stopReplay() {
        if(mPlayer != null) {

            //停止回放
            ServiceManager.stopReplay(mPlayer.getPlaySession(), new OnStopReplayListener() {
                @Override
                public void onStopReplayResult(long errorCode, String errorDesc) {
                    mEndDateTime = getNowDateTime();
                    mStartPlayView.setEnabled(true);
                    mStartPlayView.setTextColor(getResources().getColor(R.color.mainColor));
                    mPortStopPlayView.setImageDrawable(getResources().getDrawable(R.drawable.litplay));
                    mLandStopPlayView.setImageDrawable(getResources().getDrawable(R.drawable.litplay));
                    //if (errorCode == 0) {
                    //    isDraged = false;
                    //}
                }
            });
        }
        isStoped = true;
        //停止收流线程
        if (mRecvStreamThread != null) {
            mRecvStreamThread.interrupt();
            mRecvStreamThread = null;
        }

        if(mPlayer != null && mPlayer.AVIsPlaying()) {
            //停止播放解码
            mPlayer.AVStopPlay();
        }

        changeState(STOP);
        //isDraged = false;
        //mPortSeekBar.setEnabled(false);
        //mLandSeekBar.setEnabled(false);
        mPortSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mLandSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //mSurfaceView.setBackground(getResources().getDrawable(R.drawable.media_preview));
    }


    public void  pauseReplay() {

        //isStoped = true;

        if(mPlayer != null && mPlayer.AVIsPlaying()) {
            //停止播放解码
            mPlayer.AVStopPlay();
            mEndDateTime = getNowDateTime();
            mStartPlayView.setEnabled(true);
            mStartPlayView.setTextColor(getResources().getColor(R.color.mainColor));
            mPortStopPlayView.setImageDrawable(getResources().getDrawable(R.drawable.litplay));
            mLandStopPlayView.setImageDrawable(getResources().getDrawable(R.drawable.litplay));
        }

        changeState(STOP);
        isDraged = false;
        //mPortSeekBar.setEnabled(false);
        //mLandSeekBar.setEnabled(false);
        mPortSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mLandSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //mSurfaceView.setBackground(getResources().getDrawable(R.drawable.media_preview));
    }

    public void  reStartReplay() {
        Log.i("zzz", "reStartReplay.....222.....");
        if (mPlayer != null) {

            mPlayer.AVStartPlay();

            ServiceManager.dragReplay(mPlayer.getPlaySession(), mCurrentTime, new OnDragReplayListener() {
                @Override
                public void onDragReplayResult(long errorCode, String errorDesc) {
                    if (errorCode == 0) {

                        changeState(PLAY);

                        mStartDateTime = getNowDateTime();

                        mStartPlayView.setEnabled(false);
                        mStartPlayView.setTextColor(getResources().getColor(R.color.hit_text_color));

                        mPortStopPlayView.setImageDrawable(getResources().getDrawable(R.drawable.video_stop));
                        mLandStopPlayView.setImageDrawable(getResources().getDrawable(R.drawable.video_stop));

                        mPortSeekBar.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return false;
                            }
                        });

                        mLandSeekBar.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return false;
                            }
                        });

                    } else {
                        ToastUtils.showShort("errorCode: " + errorCode + " &errorDesc : " + errorDesc);
                    }

                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        try {
            stopReplay();
        } catch (Exception e) {
            Log.i(TAG, "stopReplay video throw a exception : " + e.getMessage());
        }

        //销毁Player
        if (null != mPlayer) {
            mPlayer.AVFinalize();
            mPlayer = null;
        }

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        //stopReplay();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**case R.id.play_play:
                if (null == mPlayer) {
                    return;
                }
                if (mPlayer.AVIsPlaying()) {
                    stopReplay();
                } else {
                    if (mVideoState == STOP) {

                        String dragTimeStr = DateUtils.getPauseTime(mVideoBeginTime, mVideoEndTime, DateUtils.getTimeDifference(mEndDateTime, mStartDateTime));
                        ToastUtils.showShort(dragTimeStr);
                        //拖动回放，"2016-01-10 12:00:00"为要播放的时间，具体能用的时间在queryReplay中返回
                        ServiceManager.dragReplay(mPlayer.getPlaySession(), dragTimeStr, new OnDragReplayListener() {
                            @Override
                            public void onDragReplayResult(long errorCode, String errorDesc) {
                                ToastUtils.showShort("errorCode: " + errorCode + " &errorDesc : " + errorDesc);
                            }
                        });
                        //ToastUtils.showShort("mVideoBeginTime : " + mVideoBeginTime + "    &  mVideoEndTime:  " + mVideoEndTime);
                    }
                }
                break;*/

            case R.id.iv_back:
                VideoRePlayActivity.this.finish();
                break;
            case R.id.capture_replay_view:
                //抓拍图片，返回路径
                //String path = mPlayer.snatch(null);
                String path = mPlayer.snatch(FilesUtils.getSnatchPath(mResCode));
                if (null != path) {
                    Toast.makeText(VideoRePlayActivity.this, path, Toast.LENGTH_SHORT).show();

                    String filePaths = path.substring(0, path.lastIndexOf("/") -1);
                    String mineType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg");
                    MediaScanner.getInstace().scanFile(mContext, new MediaScanner.ScanFile(filePaths,mineType));
                }
                break;
            case R.id.show_full_screen_view:
                if (this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                break;
            case R.id.show_small_screen_view:
                 if (this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE) {
                     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                 }
                break;
            case R.id.start_play_view:
                if (mStartPlayView.isEnabled()) {

                    if (mRecvStreamThread == null) {
                        mHandler.sendEmptyMessage(START_REPLAY_VIDEO);
                    } else {
                        mHandler.sendEmptyMessage(RESTART_REPLAY_VIDEO);
                    }

                }
                break;
            case R.id.begin_date_time_layout:
                showDatePickerDialog(mBeginDateView, mBeginTimeView, true);
                break;
            case R.id.end_date_time_layout:
                showDatePickerDialog(mEndDateView, mEndTimeView, false);
                break;
            case R.id.video_stop_for_land:
            case R.id.video_stop_for_port:

                if (isStoped) {

                    if (mHandler.hasMessages(START_REPLAY_VIDEO)) {
                        mHandler.removeMessages(START_REPLAY_VIDEO);
                    }

                    mHandler.sendEmptyMessage(START_REPLAY_VIDEO);
                } else {
                    if (mPlayer!=null && mPlayer.AVIsPlaying()) {
                        //mHandler.sendEmptyMessage(STOP_REPLAY_VIDEO);

                        if (mHandler.hasMessages(PAUSE_REPLAY_VIDEO)) {
                            mHandler.removeMessages(PAUSE_REPLAY_VIDEO);
                        }

                        mHandler.sendEmptyMessage(PAUSE_REPLAY_VIDEO);
                    } else if (mPlayer!=null && !mPlayer.AVIsPlaying()) {
                        //mHandler.sendEmptyMessage(START_REPLAY_VIDEO);

                        if (mHandler.hasMessages(RESTART_REPLAY_VIDEO)) {
                            mHandler.removeMessages(RESTART_REPLAY_VIDEO);
                        }

                        mHandler.sendEmptyMessage(RESTART_REPLAY_VIDEO);
                    }
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final Configuration config = newConfig;

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                onScreenOrientationChanged(config);
            }
        });
    }


    /**
     * 设置顶部，底部布局的显示和隐藏
     */
    private void visiblePlayerSeekbarLayout(boolean isLand0rientation) {
        if (isLand0rientation) {
            mLandSeekBar.setProgress(mPortSeekBar.getProgress());
            mPortLayout.setVisibility(View.GONE);
            mPlaySeekbarLandLayout.setVisibility(View.VISIBLE);
            mPlaySeekbarPortLayout.setVisibility(View.GONE);
            mTitleToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        } else {
            mPortSeekBar.setProgress(mLandSeekBar.getProgress());
            mPortLayout.setVisibility(View.VISIBLE);
            mPlaySeekbarLandLayout.setVisibility(View.GONE);
            mPlaySeekbarPortLayout.setVisibility(View.VISIBLE);
            mTitleToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    /**
     * 当屏幕方向发生改变时
     *
     * @param config
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void onScreenOrientationChanged(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            hideStatusBarAndNavigationBar();

            Display d = getWindowManager().getDefaultDisplay();

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;
            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSurfaceView.getLayoutParams();
            lp.setMargins(0,0,(realWidth - displayWidth) > 0 ? (realWidth - displayWidth) : 0 , (realHeight - displayHeight) > 0 ?  (realHeight - displayHeight) : 0);
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mSurfaceView.setLayoutParams(lp);


            visiblePlayerSeekbarLayout(true);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mPlaySeekbarLandLayout.getLayoutParams();
            layoutParams.setMargins(0,0,(realWidth - displayWidth) > 0 ? (realWidth - displayWidth) + getResources().getDimensionPixelOffset(R.dimen.play_seekbar_layout_margin_right) : getResources().getDimensionPixelOffset(R.dimen.play_seekbar_layout_margin_right)
                    , (realHeight - displayHeight) > 0 ?  (realHeight - displayHeight) + getResources().getDimensionPixelOffset(R.dimen.play_seekbar_layout_margin_bottom) : getResources().getDimensionPixelOffset(R.dimen.play_seekbar_layout_margin_bottom));
            mPlaySeekbarLandLayout.setLayoutParams(layoutParams);
            //mIvSuspension.setImageResource(R.mipmap.video_play_icon_suspension);
            //mRlTopSeerBar.setVisibility(View.VISIBLE);
            //mTvSystemTime.setVisibility(View.VISIBLE);
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            showStatusBarAndNavigationBar();

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSurfaceView.getLayoutParams();
            int topMargin = getResources().getDimensionPixelOffset(R.dimen.tool_bar_height);
            lp.setMargins(0,topMargin,0,0);
            //lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = getResources().getDimensionPixelOffset(R.dimen.surface_view_height);
            mSurfaceView.setLayoutParams(lp);


            visiblePlayerSeekbarLayout(false);
            //mIvSuspension.setImageResource(R.mipmap.video_flotting_play_icon_suspension);
            //mRlTopSeerBar.setVisibility(View.GONE);
            //mTvSystemTime.setVisibility(View.GONE);
        }
    }

    class surfaceCallback implements SurfaceHolder.Callback {

        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "===== surfaceCreated =====");
            //if (mPlayer != null && !mPlayer.AVIsPlaying()) {
            //    startReplay(mResCode, mBeginTime, mEndTime, mFileName, mPosition);
            //}
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(TAG, "===== surfaceChanged =====");
            //if (mPlayer != null) {
            //    mPlayer.changeDisplaySize(width, height);
            //}
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder arg0) {
            Log.d(TAG, "===== surfaceDestroyed =====");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STATE_CHANGED:
                    videoStateChanged();
                    break;
                case VISIBLE_TOP_BOTTOM:
                    //visibleSurfaceTopAndBottom();
                    if (isTopBottomVisible && !mHandler.hasMessages(VISIBLE_TOP_BOTTOM)) {
                        mHandler.sendEmptyMessageDelayed(VISIBLE_TOP_BOTTOM, 5000);
                    }
                    break;
                case SEEKBAR_TOUCHED:
                    final String dragTimeStr = DateUtils.getProgressTime(mVideoBeginTime, mVideoEndTime, msg.getData().getInt("seekBarProgress"));
                    //ToastUtils.showShort(dragTimeStr + "      &    " + msg.getData().getInt("seekBarProgress"));
                    Log.d(TAG, "===== "+  dragTimeStr + "      &    " + msg.getData().getInt("seekBarProgress") + " =====");
                    ServiceManager.dragReplay(mPlayer.getPlaySession(), dragTimeStr, new OnDragReplayListener() {
                        @Override
                        public void onDragReplayResult(long errorCode, String errorDesc) {
                            //ToastUtils.showShort("errorCode: " + errorCode + " &errorDesc : " + errorDesc);
                            if (errorCode == 0) {
                                mStartDateTime = getNowDateTime();
                                isDraged = true;
                                mDragTime = dragTimeStr;
                                try {
                                    SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    long endTime = dfs.parse(dragTimeStr).getTime();
                                    long stratTime = dfs.parse(mVideoBeginTime).getTime();
                                    int progress = DateUtils.getProgress(mVideoBeginTime, mVideoEndTime, DateUtils.getTimeDifference(endTime, stratTime));
                                    mLandSeekBar.setProgress(progress);
                                    mPortSeekBar.setProgress(progress);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    break;
                case SYSTEM_TIME_CHANED:

                    break;
                case START_REPLAY_VIDEO:
                    //mSurfaceView.setBackground(null);
                    if (mPlayer != null && !mPlayer.AVIsPlaying()) {
                        mStartPlayView.setEnabled(false);
                        mStartPlayView.setTextColor(getResources().getColor(R.color.hit_text_color));
                        String beginDateTime = mBeginDateView.getText().toString() + " " + mBeginTimeView.getText().toString();
                        String endDateTime = mEndDateView.getText().toString() + " " + mEndTimeView.getText().toString();

                        startReplay(mResCode, beginDateTime, endDateTime, mFileName, mPosition);
                    }
                    break;
                case STOP_REPLAY_VIDEO:
                    //mSurfaceView.setBackground(getResources().getDrawable(R.drawable.media_preview));
                    stopReplay();
                    break;
                case PAUSE_REPLAY_VIDEO:
                     pauseReplay();
                    break;

                case RESTART_REPLAY_VIDEO:
                    reStartReplay();
                    break;
            }
        }
    };

    /**
     * 改变Video的状态
     *
     * @param state
     */
    private void changeState(int state) {
        mVideoState = state;
        mHandler.sendEmptyMessage(STATE_CHANGED);
        if (state == PLAY) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    /**防止当onDestroy方法调用时，mVideoPlayer
                     * 已经为null,但是这边还在发消息，导致空指针异常**/
                    if (mPlayer == null) {
                        return;
                    }

                    //mCurrentProgress = 0;//DateUtils.getProgress(mVideoBeginTime, mVideoEndTime, DateUtils.getTimeDifference(getNowDateTime(), mStartDateTime));
                    Log.i("zzz", "progress : "  + mCurrentProgress + "");

                    ServiceManager.getReplayPos(mPlayer.getPlaySession(), new OnGetReplayPosListener() {
                        @Override
                        public void onGetReplayResult(String replayPos, long errorCode, String errorDesc) {

                            if (0 == errorCode) {
                                //Toast.makeText(VideoRePlayActivity.this, "当前播放时间" + replayPos, Toast.LENGTH_SHORT).show();
                                mCurrentTime = replayPos;
                                mCurrentProgress = DateUtils.getProgress(mVideoBeginTime, mVideoEndTime, mCurrentTime);
                                Log.i("zzz", "progress 222222222222222222222: "  + mCurrentProgress +   "     isStoped = " + isStoped);
                            } else {
                                //Toast.makeText(VideoRePlayActivity.this, errorDesc, Toast.LENGTH_SHORT).show();
                                //mCurrentProgress = DateUtils.getProgress(mVideoBeginTime, mVideoEndTime, DateUtils.getTimeDifference(getNowDateTime(), mStartDateTime));
                            }

                        }
                    });

                    if (isDraged) {
                        try {
                            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            long endTime = dfs.parse(mDragTime).getTime();
                            long stratTime = dfs.parse(mVideoBeginTime).getTime();
                            int newprogress = DateUtils.getProgress(mVideoBeginTime, mVideoEndTime, DateUtils.getTimeDifference(endTime, stratTime) + DateUtils.getTimeDifference(getNowDateTime(), mStartDateTime));
                            Log.i("zzz", "progress 33333333333333333: "  + newprogress +   "     isStoped = " + isStoped);

                            if (Math.abs(newprogress - mCurrentProgress) < 5) {
                                mCurrentProgress = newprogress;
                            }

                            mLandSeekBar.setProgress(newprogress);
                            mPortSeekBar.setProgress(newprogress);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!isStoped) {
                            Log.i("zzz", "progress 444444444444: "  + mCurrentProgress +   "     isStoped = " + isStoped);
                            mLandSeekBar.setProgress(mCurrentProgress);
                            mPortSeekBar.setProgress(mCurrentProgress);
                        }
                    }

                    if (mPlayer.AVIsPlaying()) {
                        if (mCurrentProgress < 100) {
                            mHandler.postDelayed(this, 1000);
                        }
                        if(mCurrentProgress >= 100) {
                            isDraged = false;

                            if (mHandler.hasMessages(STOP_REPLAY_VIDEO)) {
                                mHandler.removeMessages(STOP_REPLAY_VIDEO);
                            }

                            mHandler.sendEmptyMessage(STOP_REPLAY_VIDEO);
                        }
                    }

                }
            });
        } else  if (state == STOP) {
            isDraged = false;
        }
    }

    /**
     * 当Video的状态改变时
     */
    private void videoStateChanged() {
        switch (mVideoState) {
            case PLAY:
                //mPlayPlayIV.setImageResource(R.drawable.pause);
                //mPlayPlayIV.invalidate();
                break;
            /**case PAUSE:
                mPlayPlayIV.setImageResource(R.drawable.litplay);
                break;*/
            case STOP:
                //mPlayPlayIV.setImageResource(R.drawable.litplay);
                //mPlayPlayIV.invalidate();
                break;
        }
    }

    public long getNowDateTime() {
        Date now = new Date();
        return now.getTime();
    }

    private void hideStatusBarAndNavigationBar() {
        //隐藏导航栏
        /**View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/

        //隐藏状态栏
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void showStatusBarAndNavigationBar() {
        //显示导航栏
        /**View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);*/

        //显示状态栏
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public void showDatePickerDialog (final TextView dateText, final TextView timeText, final boolean isBeginDateTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());

        CustomDatePicker customDatePicker = new CustomDatePicker(VideoRePlayActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                dateText.setText(time.split(" ")[0]);
                timeText.setText(time.split(" ")[1] + (isBeginDateTime ? ":00" : ":59"));
            }
        }, "1970-01-01 00:00", "2037-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 显示时和分
        //customDatePicker.showYearMonth();
        customDatePicker.setIsLoop(false); // 不允许循环滚动
        customDatePicker.setShowType(1);
        customDatePicker.show(dateText.getText().toString() + " " + timeText.getText().toString());
    }
}
