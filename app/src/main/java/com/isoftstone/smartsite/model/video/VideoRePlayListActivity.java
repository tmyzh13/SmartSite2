package com.isoftstone.smartsite.model.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.video.VideoMonitorBean;
import com.isoftstone.smartsite.model.map.ui.VideoMonitorMapActivity;
import com.isoftstone.smartsite.model.video.Adapter.VideoRePlayAdapter;
import com.isoftstone.smartsite.utils.ToastUtils;
import com.isoftstone.smartsite.widgets.CustomDatePicker;
import com.uniview.airimos.listener.OnQueryReplayListener;
import com.uniview.airimos.manager.ServiceManager;
import com.uniview.airimos.obj.QueryCondition;
import com.uniview.airimos.obj.RecordInfo;
import com.uniview.airimos.parameter.QueryReplayParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhangyinfu on 2017/10/22.
 */

public class VideoRePlayListActivity extends Activity implements  View.OnClickListener, VideoRePlayAdapter.AdapterViewOnClickListener{
    private static final String TAG = "zyf_VideoRePlayList";
    private ListView mListView = null;
    private Context mContext;
    private TextView mResCodeTv;
    private TextView mResTypeTv;
    private TextView mResNameTv;
    private ImageView mIsOnlineIv;

    private TextView mBeginDateTv;
    private TextView mBeginTimeTv;
    private RelativeLayout mBeginTimeLayout;
    private TextView mEndDateTv;
    private TextView mEndTimeTv;
    private RelativeLayout mEndTimeLayout;
    private TextView mQueryText;
    private RelativeLayout gotomap;

    private String mResCode = null;
    private String mBeginDate = null;
    private String mEngDate = null;
    private Boolean isCameraOnLine = false;
    private int mResSubType;

    private ImageButton mImageView_back = null;
    private ImageButton mImageView_icon = null;
    private TextView toolbar_title = null;
    private AlertDialog mAlertDialog = null;

    @Override
    protected void onStart() {
        super.onStart();
        mContext = getApplicationContext();
    }


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_video_replay_list);

        init();

        /*获取Intent中的Bundle对象*/
        if(bundle == null) {
            bundle = this.getIntent().getExtras();
        }

        String resCode = bundle.getString("resCode");
        mResSubType =  bundle.getInt("resSubType");
        String resName = bundle.getString("resName");
        isCameraOnLine = bundle.getBoolean("isOnline");
        mBeginDate = bundle.getString("beginTime");
        mEngDate = bundle.getString("endTime");

        mResCodeTv.setText(resCode);
        mResCode = resCode;
        setCameraType(mResTypeTv, mResSubType);
        mResNameTv.setText(resName);
        if (isCameraOnLine) {
            mIsOnlineIv.setImageResource(R.drawable.online);
        } else {
            mIsOnlineIv.setImageResource(R.drawable.offline);
        }

        mBeginDateTv.setText(mBeginDate.split(" ")[0]);
        mEndDateTv.setText(mEngDate.split(" ")[0]);
        mEndTimeTv.setText(mEngDate.split(" ")[1]);

        queryOrStartReplayVideo(mResCode, mBeginDate + " 00:00:00", mEngDate, false);
    }

    private void init(){
        mListView = (ListView) findViewById(R.id.list);
        mResCodeTv = (TextView) findViewById(R.id.res_code_tv);
        mResTypeTv = (TextView) findViewById(R.id.res_type_tv);
        mResNameTv = (TextView) findViewById(R.id.res_name_tv);
        mIsOnlineIv = (ImageView) findViewById(R.id.is_online_tv);
        gotomap = (RelativeLayout) findViewById(R.id.gotomap);

        mBeginDateTv = (TextView) findViewById(R.id.begin_date_txt);
        mBeginTimeTv = (TextView) findViewById(R.id.begin_time_txt);
        mEndDateTv = (TextView) findViewById(R.id.end_date_txt);
        mEndTimeTv = (TextView) findViewById(R.id.end_time_txt);
        mBeginTimeLayout = (RelativeLayout) findViewById(R.id.begin_date_time_layout);
        mEndTimeLayout = (RelativeLayout) findViewById(R.id.end_date_time_layout);
        mQueryText = (TextView) findViewById(R.id.query_txt);
        mBeginTimeLayout.setOnClickListener(this);
        mEndTimeLayout.setOnClickListener(this);
        mQueryText.setOnClickListener(this);
        gotomap.setOnClickListener(this);

        mImageView_back = (ImageButton)findViewById(R.id.btn_back);
        mImageView_icon = (ImageButton)findViewById(R.id.btn_icon);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("历史监控");
        mImageView_icon.setVisibility(View.INVISIBLE);
        mImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mImageView_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 固定摄像机：1; 云台摄像机：2; 高清固定摄像机：3; 高清云台摄像机：4; 车载摄像机：5; 不可控标清摄像机：6; 不可控高清摄像机：7;
     * @param textView
     * @param resSubType
     */
    private void setCameraType(TextView textView,int resSubType) {
        if (1 == resSubType) {
            textView.setText(R.string.camera_type_1);
        } else if (2 == resSubType) {
            textView.setText(R.string.camera_type_2);
        } else if (3 == resSubType) {
            textView.setText(R.string.camera_type_3);
        } else if (4 == resSubType) {
            textView.setText(R.string.camera_type_4);
        } else if (5 == resSubType) {
            textView.setText(R.string.camera_type_5);
        } else if (6 == resSubType) {
            textView.setText(R.string.camera_type_6);
        } else if (7 == resSubType) {
            textView.setText(R.string.camera_type_7);
        } else {
            textView.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.begin_date_time_layout:
                showDatePickerDialog(mBeginDateTv, mBeginTimeTv, true);
                break;
            case R.id.end_date_time_layout:
                showDatePickerDialog(mEndDateTv, mEndTimeTv, false);
                break;
            case R.id.query_txt:
                String  beginDateTime = mBeginDateTv.getText().toString() + " " + mBeginTimeTv.getText().toString();
                String  endDateTime = mEndDateTv.getText().toString() + " " + mEndTimeTv.getText().toString();
                if (isDateOneBigger(beginDateTime, endDateTime)) {
                    ToastUtils.showShort(getText(R.string.data_pick_dialog_error_msg2).toString());
                    return;
                };
                queryOrStartReplayVideo(mResCode, beginDateTime, endDateTime, true);
                break;
            case R.id.gotomap:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),VideoMonitorMapActivity.class);
                getApplicationContext().startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void showDatePickerDialog (final TextView dateText, final TextView timeText, final boolean isBeginDateTime) {
        //final Calendar sCalendar = Calendar.getInstance();
        /**DatePickerDialog dialog = new DatePickerDialog(VideoRePlayListActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                sCalendar.set(year, monthOfYear, dayOfMonth);
                editText.setText(DateFormat.format("yyy-MM-dd", sCalendar).toString());
            }
        }, sCalendar.get(Calendar.YEAR), sCalendar.get(Calendar.MONTH), sCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();*/
        //ToastUtils.showShort("showDatePickerDialog");

        /**View rootView = null;
        if (this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT) {
            rootView = View.inflate(mContext, R.layout.date_time_picker, null);
        } else if (this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE) {
            rootView = View.inflate(mContext, R.layout.date_time_picker_for_land, null);
        }

        final DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.new_act_date_picker);
        final TimePicker timePicker =  (TimePicker) rootView.findViewById(R.id.new_act_time_picker);

        // Init DatePicker
        int year;
        int month;
        int day;
        if (StringUtils.isEmpty(dateText.getText().toString())) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            year =  Integer.parseInt(dateText.getText().toString().split("-")[0]);
            month = Integer.parseInt(dateText.getText().toString().split("-")[1]);
            day = Integer.parseInt(dateText.getText().toString().split("-")[2]);
        }
        datePicker.init(year, month, day, null);

        // Init TimePicker
        int hour;
        int minute;
        if (StringUtils.isEmpty(timeText.getText().toString())) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        } else {
            hour = Integer.parseInt(timeText.getText().toString().split(":")[0]);
            minute = Integer.parseInt(timeText.getText().toString().split(":")[1]);
        }
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        // Build DateTimeDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoRePlayListActivity.this);
        builder.setView(rootView);
        builder.setTitle(R.string.select_date_time);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dateStr = DateUtils.formatDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                dateText.setText(dateStr);
                String timeStr = DateUtils.formatTime(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                timeText.setText(timeStr + (isBeginDateTime ? ":00" : ":59"));
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();*/

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());

        CustomDatePicker customDatePicker = new CustomDatePicker(VideoRePlayListActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                //quyu_date.setText(time.substring(0,7));
                ToastUtils.showShort(time.toString());
                dateText.setText(time.split(" ")[0]);
                timeText.setText(time.split(" ")[1] + (isBeginDateTime ? ":00" : ":59"));
            }
        }, "1970-01-01 00:00", "2037-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 不显示时和分
        customDatePicker.setShowType(1);
        //customDatePicker.showYearMonth();
        customDatePicker.setIsLoop(false); // 不允许循环滚动
        customDatePicker.show(dateText.getText().toString() + " " + timeText.getText().toString());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null != mAlertDialog && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    public void queryOrStartReplayVideo(final String cameraCode, final String beginDateTime, final String endDateTime, final boolean isStartOptions) {

        //查询回放记录参数
        QueryReplayParam p = new QueryReplayParam(cameraCode, beginDateTime, endDateTime, new QueryCondition(0, 100, true));

        //查询回放记录结果监听
        OnQueryReplayListener queryListener = new OnQueryReplayListener() {
            @Override
            public void onQueryReplayResult(long errorCode, String errorDesc, List<RecordInfo> recordList) {
                if (errorCode != 0 || recordList == null ){
                    Toast.makeText(mContext,"error info: " + errorDesc, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (recordList.size() <= 0) {
                    Toast.makeText(mContext,"此时段没有录像...",Toast.LENGTH_SHORT).show();
                    mListView.setAdapter(null);
                    return;
                }

                if (isStartOptions) {
                    startReplayVideo(cameraCode, beginDateTime, endDateTime);
                } else {
                    int size = recordList.size();
                    ArrayList<VideoMonitorBean> sList = new ArrayList<VideoMonitorBean>();
                    VideoMonitorBean video;

                    for (int i = 0; i < size; i++) {
                        video = new VideoMonitorBean(beginDateTime,  endDateTime
                                , recordList.get(i).getFileName(), cameraCode, mResSubType, mResNameTv.getText().toString(), isCameraOnLine);
                        sList.add(video);
                    }
                    //刷新ListView
                    mListView.setAdapter(null);

                    VideoRePlayAdapter adapter = new VideoRePlayAdapter(VideoRePlayListActivity.this);
                    adapter.notifyDataSetChanged();
                    adapter.setData(sList);
                    mListView.setAdapter(adapter);
                    mListView.invalidate();
                }
            }
        };

        //先查询指定时间段内有的回放记录
        ServiceManager.queryReplay(p, queryListener);
    }

    @Override
    public void viewOnClickListener(VideoRePlayAdapter.ViewHolder viewHolder) {

    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    private void startReplayVideo(String resCode, String beginTime, String endTime) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("resCode", resCode);
        bundle.putString("beginTime", beginTime);
        bundle.putString("endTime", endTime);
        bundle.putString("resSubType", mResTypeTv.getText().toString());
        bundle.putString("resName", mResNameTv.getText().toString());
        bundle.putBoolean("isOnline", isCameraOnLine);
        bundle.putInt("position", 0);
        intent.putExtras(bundle);
        intent.setClass(mContext, VideoRePlayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
