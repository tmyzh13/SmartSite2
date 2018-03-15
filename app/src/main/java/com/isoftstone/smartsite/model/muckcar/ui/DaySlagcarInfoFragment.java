package com.isoftstone.smartsite.model.muckcar.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.http.muckcar.ArchMonthFlowBean;
import com.isoftstone.smartsite.http.muckcar.CarInfoBean;
import com.isoftstone.smartsite.http.muckcar.McFlowBean;
import com.isoftstone.smartsite.model.main.ui.AirMonitoringActivity;
import com.isoftstone.smartsite.utils.DateUtils;
import com.isoftstone.smartsite.utils.SharedPreferencesUtils;
import com.isoftstone.smartsite.widgets.CustomDatePicker;
import com.isoftstone.smartsite.widgets.MyPopuWindow;

import org.joda.time.LocalDate;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;

/**
 * Created by 2013020220 on 2017/11/23.
 */

public class DaySlagcarInfoFragment extends BaseFragment {
    public static final int TIME_INIT_TEXTVIEW_LIST = 0;
    public static final int TIME_INIT_DOUBLE_LINE_CHART = 1;
    private int mDayOrMonthFlag = 1; //1月  0日
    private LinearLayout layout_1;
    private LinearLayout layout_2;
    private ArrayList<CarInfoBean> mCarInfoList;
    private String[] archName;
    private Spinner spinner_address = null;
    private TextView date_liuliangpaiming = null;
    private TextView date_louduanbaojinglv = null;
    private TextView date_liuliangduibi = null;
    private long liuliangduibi_id = 0;
    private long[] baojinglv_addressid;
    private ArchMonthFlowBean mArchMonthFlowBean;
    private LineChart liuliangduibi_linechart;
    private LineChart baojinglv_linechart;
    private TextView baojinglv_address;
    private LinearLayout list_trextviews;

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    private  boolean showDialog=false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIME_INIT_TEXTVIEW_LIST:
                    initTextViews();
                    break;
                case TIME_INIT_DOUBLE_LINE_CHART:
                    break;
                default:
                    break;
            }

        }
    };
    private Spinner address_baojinglv;
    private LineChart lineChart_baojinglv;
    private TextView liuliang;
    private TextView liuliangduibi;
    private TextView baojinglv;
    private TextView baojinlv_duibi;
    private TextView choice_load_one;
    private TextView choice_load_two;
    private ImageView liuliangduibi_arrow;
    private MyPopuWindow myPopuWindow;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(null);
        }
    }

    private ArrayList<String> choiceLists;
    private MyPopuWindow.OnDataCheckedListener dataCheckedListener = new MyPopuWindow.OnDataCheckedListener() {
        @Override
        public void onDataCheck(String left, String right, int first_choice, int second_choice) {
            myPopuWindow = null;
            choice_load_one.setText(left);
            choice_load_two.setText(right);
            baojinlv_duibi.setText(left);
            if (mCarInfoList != null) {
                if (mCarInfoList.size() >= 2) {
                    baojinglv_addressid = new long[2];
                    baojinglv_addressid[0] = mCarInfoList.get(first_choice).getArch().getId();
                    baojinglv_addressid[1] = mCarInfoList.get(second_choice).getArch().getId();
                } else {
                    baojinglv_addressid = new long[1];
                    baojinglv_addressid[0] = mCarInfoList.get(first_choice).getArch().getId();
                }
            }
            ((SlagcarInfoActivity) getActivity()).getBaojinglvData(mDayOrMonthFlag);
        }
    };

    private void initMyPopuWindow() {
        if (myPopuWindow == null) {
            myPopuWindow = new MyPopuWindow(getActivity(), "请选择两个：", choiceLists, dataCheckedListener);
        }
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        choiceLists = new ArrayList<String>();
        initMyPopuWindow();
        liuliang = (TextView) rootView.findViewById(R.id.liuliang);
        liuliangduibi = (TextView) rootView.findViewById(R.id.liuliangduibi);
        baojinglv = (TextView) rootView.findViewById(R.id.baojinglv);
        if (mDayOrMonthFlag == 1) {
            liuliang.setText("路段渣土车月度流量排名");
            liuliangduibi.setText("路段渣土车月度流量对比");
            baojinglv.setText("渣土车流量路段对比");
        } else if (mDayOrMonthFlag == 0) {
            liuliang.setText("路段渣土日流量排名");
            liuliangduibi.setText("路段渣土车日流量对比");
            baojinglv.setText("渣土车流量路段对比");
        }

        list_trextviews = (LinearLayout) rootView.findViewById(R.id.list_textview);//动态tv数据
        layout_1 = (LinearLayout) rootView.findViewById(R.id.liuliangduibi_detail);
        layout_2 = (LinearLayout) rootView.findViewById(R.id.warning_detail);
        lineChart_baojinglv = (LineChart) layout_2.findViewById(R.id.chart_baojinglv);
        spinner_address = (Spinner) layout_1.findViewById(R.id.spinner_address);
        spinner_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                liuliangduibi_id = mCarInfoList.get(position).getArch().getId();
                ((SlagcarInfoActivity) getActivity()).getLiuliangduibiData(mDayOrMonthFlag);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //流量对比
        date_liuliangduibi = (TextView) layout_1.findViewById(R.id.date_liuliangduibi);
        date_liuliangduibi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
                if (mDayOrMonthFlag == 1) {
                    customDatePicker2.showYearMonth();
                } else if (mDayOrMonthFlag == 0) {
                    customDatePicker2.showSpecificTime(false); // 不显示时和分
                }
                customDatePicker2.show(now);
            }
        });
        //流量排行
        date_liuliangpaiming = (TextView) rootView.findViewById(R.id.date_liuliangpaiming);
        date_liuliangpaiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
                if (mDayOrMonthFlag == 1) {
                    customDatePicker1.showYearMonth();
                } else if (mDayOrMonthFlag == 0) {
                    customDatePicker1.showSpecificTime(false); // 不显示时和分
                }
                customDatePicker1.show(now);
            }
        });
        date_louduanbaojinglv = (TextView) rootView.findViewById(R.id.date_louduanbaojinglv);
        date_louduanbaojinglv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
                if (mDayOrMonthFlag == 1) {
                    customDatePicker3.showYearMonth();
                } else if (mDayOrMonthFlag == 0) {
                    customDatePicker3.showSpecificTime(false); // 不显示时和分
                }
                customDatePicker3.show(now);
            }
        });

        //报警率,选择路段

        baojinlv_duibi = (TextView) layout_2.findViewById(R.id.baojinglv_duibi);
        liuliangduibi_arrow = (ImageView) layout_2.findViewById(R.id.baojinglv_duibi_arrow);
        choice_load_one = (TextView) layout_2.findViewById(R.id.choice_load_one);
        choice_load_two = (TextView) layout_2.findViewById(R.id.choice_load_two);
        baojinlv_duibi.setOnClickListener(baojinglvduiClickListener);
        liuliangduibi_arrow.setOnClickListener(baojinglvduiClickListener);


        liuliangduibi_linechart = (LineChart) layout_1.findViewById(R.id.chart_liuliangduibi);
        baojinglv_linechart = (LineChart) rootView.findViewById(R.id.chart_baojinglv);
        if (mDayOrMonthFlag == 1) {
            date_liuliangduibi.setText(DateUtils.getNewTime_2());
            date_liuliangpaiming.setText(DateUtils.getNewTime_2());
            date_louduanbaojinglv.setText(DateUtils.getNewTime_2());
        } else if (mDayOrMonthFlag == 0) {
            date_liuliangduibi.setText(DateUtils.getNewTime_1());
            date_liuliangpaiming.setText(DateUtils.getNewTime_1());
            date_louduanbaojinglv.setText(DateUtils.getNewTime_1());
        }
        ((SlagcarInfoActivity) getActivity()).getLiuliangpaimingData(mDayOrMonthFlag);
        initDatePicker();

    }

    private CustomDatePicker customDatePicker1, customDatePicker2, customDatePicker3;

    //初始化多个textview
    private void initTextViews() {
        list_trextviews.removeAllViews();
        if (mCarInfoList != null && mCarInfoList.size() != 0) {
            int tv_num = mCarInfoList.size();
            CarInfoBean mCarInfoBean;
            for (int i = 0; i < tv_num; i++) {
                mCarInfoBean = mCarInfoList.get(i);
                MyTextView myTextView = new MyTextView(mContext, mCarInfoBean.getArch().getName(), mCarInfoBean.getIsAlarmMc(), mCarInfoBean.getNoAlarmMc());
                list_trextviews.addView(myTextView);
            }
        }
    }

    private void initDatePicker() {
        setShowDialog(true);
        customDatePicker1 = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                if (mDayOrMonthFlag == 1) {
                    date_liuliangpaiming.setText(time.substring(0, 7));
                } else if (mDayOrMonthFlag == 0) {
                    date_liuliangpaiming.setText(time.substring(0, 10));
                }
                ((SlagcarInfoActivity) getActivity()).getLiuliangpaimingData(mDayOrMonthFlag);

            }
        }, "2010-01-01 00:00", "2037-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.setIsLoop(false); // 不允许循环滚动

        customDatePicker2 = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                if (mDayOrMonthFlag == 1) {
                    date_liuliangduibi.setText(time.substring(0, 7));
                } else if (mDayOrMonthFlag == 0) {
                    date_liuliangduibi.setText(time.substring(0, 10));
                }
                ((SlagcarInfoActivity) getActivity()).getLiuliangduibiData(mDayOrMonthFlag);
            }
        }, "2010-01-01 00:00", "2037-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.setIsLoop(false); // 不允许循环滚动

        customDatePicker3 = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                if (mDayOrMonthFlag == 1) {
                    date_louduanbaojinglv.setText(time.substring(0, 7));
                } else if (mDayOrMonthFlag == 0) {
                    date_louduanbaojinglv.setText(time.substring(0, 10));
                }
                ((SlagcarInfoActivity) getActivity()).getBaojinglvData(mDayOrMonthFlag);
            }
        }, "2010-01-01 00:00", "2037-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker3.setIsLoop(false); // 不允许循环滚动
    }

    public void setDayOrMonthFlag(int dayOrMonthFlag) {
        mDayOrMonthFlag = dayOrMonthFlag;
    }

    public int getDayOrMonthFlag() {
        return mDayOrMonthFlag;
    }

    private View.OnClickListener baojinglvduiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initMyPopuWindow();
            myPopuWindow.showAtLocation(rootView.findViewById(R.id.scrollview), Gravity.CENTER, 0, 0);
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_slagcar_info;
    }

    public void setCarInfoList(ArrayList<CarInfoBean> carInfoList) {
        if(carInfoList == null||carInfoList.size()==0){
            ((SlagcarInfoActivity) getActivity()).closeDlg();
            return;
        }
        SharedPreferencesUtils.saveBaseWidth(mContext, 0.000f);
        handler.sendEmptyMessage(TIME_INIT_TEXTVIEW_LIST);
        int size = carInfoList.size();
        for (int i = 0; i < size; i++) {
            choiceLists.add(carInfoList.get(i).getArch().getName());
            if (i == 0) {
                String name = carInfoList.get(0).getArch().getName();
                choice_load_one.setText(name);
                baojinlv_duibi.setText(name);
            }
            if (i == 1) {
                choice_load_two.setText(carInfoList.get(1).getArch().getName());
            }
        }
        //加载流量对比地址选择
        mCarInfoList = carInfoList;
        archName = null;
        if (mCarInfoList != null) {
            archName = new String[mCarInfoList.size()];
            for (int i = 0; i < mCarInfoList.size(); i++) {
                CarInfoBean carInfoBean = mCarInfoList.get(i);
                archName[i] = carInfoBean.getArch().getName();
            }

        }
        if (archName != null) {
            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, archName);
            spinner_address.setAdapter(adapter);
        }
        //获取报警率地区

        if (mCarInfoList != null) {
            if (mCarInfoList.size() >= 2) {
                baojinglv_addressid = new long[2];
                baojinglv_addressid[0] = mCarInfoList.get(0).getArch().getId();
                baojinglv_addressid[1] = mCarInfoList.get(1).getArch().getId();
            } else {
                baojinglv_addressid = new long[1];
                baojinglv_addressid[0] = mCarInfoList.get(0).getArch().getId();
            }
        }
        ((SlagcarInfoActivity) getActivity()).getBaojinglvData(mDayOrMonthFlag);
    }

    public long[] getBaojinglvAddressId() {
        return baojinglv_addressid;
    }

    public String getLiuliangpaimingTime() {
        return date_liuliangpaiming.getText().toString();
    }

    public String getLiuliangduibiTime() {
        return date_liuliangduibi.getText().toString();
    }

    public String getBaojinglvTime() {
        return date_louduanbaojinglv.getText().toString();
    }

    public long getLiuliangduibi_id() {
        return liuliangduibi_id;
    }

    public void setLiuliangduibi(ArchMonthFlowBean archMonthFlowBean) {
        mArchMonthFlowBean = archMonthFlowBean;
        //跟新数据
        int max = 0;
        if (mArchMonthFlowBean != null) {
            liuliangduibi_linechart.setDrawGridBackground(false);
            liuliangduibi_linechart.getDescription().setEnabled(false);
            liuliangduibi_linechart.setTouchEnabled(true);
            liuliangduibi_linechart.setDragEnabled(false);  //是否可以缩放
            liuliangduibi_linechart.setScaleEnabled(false);  //是否可以缩放
            liuliangduibi_linechart.setPinchZoom(false);
            liuliangduibi_linechart.setExtraOffsets(10, 0, 10, 20);

            XAxis xAxis = liuliangduibi_linechart.getXAxis();
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            if(mDayOrMonthFlag == 1){
                xAxis.setAxisMaximum(31);
            }else  if(mDayOrMonthFlag == 0){
                xAxis.setAxisMaximum(23);
            }

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            YAxis leftAxis = liuliangduibi_linechart.getAxisLeft();
            leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
            leftAxis.enableGridDashedLine(10f, 0f, 0f);
            leftAxis.setDrawZeroLine(false);
            leftAxis.setDrawLimitLinesBehindData(false);
            leftAxis.setAxisMinimum(0f);


            liuliangduibi_linechart.getAxisRight().setEnabled(false);


            liuliangduibi_linechart.animateX(2500);

            // get the legend (only possible after setting data)
            Legend l = liuliangduibi_linechart.getLegend();
            l.setForm(Legend.LegendForm.LINE);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setEnabled(false);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            ArrayList<McFlowBean> countFlow = mArchMonthFlowBean.getCountFlow();
            if (countFlow != null && countFlow.size() > 0) {

                ArrayList<Entry> values = new ArrayList<Entry>();
                for (int i = 0; i < countFlow.size(); i++) {
                    McFlowBean mcFlowBean = countFlow.get(i);
                    if (mDayOrMonthFlag == 1) {
                        LocalDate date = new LocalDate(mcFlowBean.getDataTimeDay());
                        int index = date.getDayOfMonth();
                        String value = mcFlowBean.getFlow() + "";
                        if (max < mcFlowBean.getFlow()) {
                            max = Integer.parseInt(value);
                        }
                        Entry entry = new Entry(index, Float.parseFloat(value));
                        values.add(entry);
                    } else if (mDayOrMonthFlag == 0) {
                        int index = Integer.parseInt(mcFlowBean.getDataTimeDay());
                        String value = mcFlowBean.getFlow() + "";
                        if (max < mcFlowBean.getFlow()) {
                            max = Integer.parseInt(value);
                        }
                        Entry entry = new Entry(index, Float.parseFloat(value));
                        values.add(entry);
                    }

                }

                LineDataSet set1 = new LineDataSet(values, "DataSet 1");
                set1.setDrawIcons(false);
                // set the line to be drawn like this "- - - - - -"
                set1.setColor(Color.parseColor("#4879df"));
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 10f}, 0f));
                set1.setFormSize(15.f);
                set1.setDrawCircles(false);
                set1.setDrawFilled(true);
                set1.setHighlightEnabled(false);
                set1.setDrawValues(false);
                set1.setFillAlpha(255);
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                set1.setFillColor(Color.parseColor("#4879df"));

                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                    //set1.setFillDrawable(drawable);
                } else {
                    set1.setFillColor(Color.BLACK);
                }
                dataSets.add(set1); // add the datasets
            }

            ArrayList<McFlowBean> isAlarms = mArchMonthFlowBean.getIsAlarms();
            if (isAlarms != null && isAlarms.size() > 0) {
                ArrayList<Entry> values_2 = new ArrayList<Entry>();
                for (int i = 0; i < isAlarms.size(); i++) {
                    McFlowBean mcFlowBean = isAlarms.get(i);
                    if (mDayOrMonthFlag == 1) {
                        LocalDate date = new LocalDate(mcFlowBean.getDataTimeDay());
                        int day = date.getDayOfMonth();
                        String value = mcFlowBean.getFlow() + "";
                        Entry entry = new Entry(day, Float.parseFloat(value));
                        values_2.add(entry);
                    } else if (mDayOrMonthFlag == 0) {
                        int index = Integer.parseInt(mcFlowBean.getDataTimeDay());
                        String value = mcFlowBean.getFlow() + "";
                        Entry entry = new Entry(index, Float.parseFloat(value));
                        values_2.add(entry);
                    }

                }


                LineDataSet set2 = new LineDataSet(values_2, "DataSet 2");
                set2.setDrawIcons(false);
                // set the line to be drawn like this "- - - - - -"
                set2.enableDashedLine(10f, 0f, 0f);//设置连线样式
                set2.setColor(Color.RED);
                set2.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 10f}, 0f));
                set2.setCircleColor(Color.parseColor("#599fff"));
                //set2.setCircleRadius(4f);//设置焦点圆心的大小
                set2.setCircleColorHole(Color.WHITE);
                set2.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
                set2.setHighlightEnabled(false);//是否禁用点击高亮线
                set2.setDrawFilled(true);//设置禁用范围背景填充
                set2.setFillAlpha(255);
                set2.setFillColor(Color.RED);
                set2.setDrawCircles(false);
                set2.setDrawValues(false);
                set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                    //set1.setFillDrawable(drawable);
                } else {
                    set2.setFillColor(Color.BLACK);
                }

                dataSets.add(set2); // add the datasets
            }

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            //设置最大高度
            leftAxis.setAxisMaximum(max);
            // set data
            liuliangduibi_linechart.setData(data);

        }
    }


    public void setBaojinglv(ArchMonthFlowBean archMonthFlowBean) {

        int max = 0;
        if (archMonthFlowBean == null) {
            return;
        }
        if (archMonthFlowBean.getMcFlows() == null) {
            return;
        }
        baojinglv_linechart.setDrawGridBackground(false);

        // no description text
        baojinglv_linechart.getDescription().setEnabled(false);

        // enable touch gestures
        baojinglv_linechart.setTouchEnabled(true);

        // enable scaling and dragging
        baojinglv_linechart.setDragEnabled(false);  //是否可以缩放
        baojinglv_linechart.setScaleEnabled(false);  //是否可以缩放
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        baojinglv_linechart.setPinchZoom(true);
        baojinglv_linechart.setExtraOffsets(10, 0, 10, 20);


        XAxis xAxis = baojinglv_linechart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        if(mDayOrMonthFlag == 1){
            xAxis.setAxisMaximum(31);
        }else  if(mDayOrMonthFlag == 0){
            xAxis.setAxisMaximum(23);
        }
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        YAxis leftAxis = baojinglv_linechart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines

        leftAxis.enableGridDashedLine(10f, 0f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setAxisMinimum(0f);

        baojinglv_linechart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        baojinglv_linechart.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = baojinglv_linechart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setEnabled(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        if (archMonthFlowBean.getMcFlows().get(0).size() >= 1) {
            ArrayList<McFlowBean> list = archMonthFlowBean.getMcFlows().get(0);
            {
                ArrayList<Entry> values_2 = new ArrayList<Entry>();
                for (int i = 0; i < list.size(); i++) {
                    McFlowBean mcFlowBean = list.get(i);
                    if (mDayOrMonthFlag == 1) {
                        LocalDate date = new LocalDate(mcFlowBean.getDataTimeDay());
                        int day = date.getDayOfMonth();
                        String value = mcFlowBean.getFlow() + "";
                        Entry entry = new Entry(day, Float.parseFloat(value));
                        values_2.add(entry);
                    } else if (mDayOrMonthFlag == 0) {
                        int index = Integer.parseInt(mcFlowBean.getDataTimeDay());
                        String value = mcFlowBean.getFlow() + "";
                        Entry entry = new Entry(index, Float.parseFloat(value));
                        values_2.add(entry);
                    }
                }


                LineDataSet set2 = new LineDataSet(values_2, "DataSet 2");
                set2.setDrawIcons(false);
                // set the line to be drawn like this "- - - - - -"
                set2.enableDashedLine(10f, 0f, 0f);//设置连线样式
                set2.setColor(Color.parseColor("#599fff"));
                set2.setCircleColor(Color.parseColor("#599fff"));
                set2.setDrawCircleHole(false);
                set2.setFormLineWidth(1f);
                set2.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 10f}, 0f));
                set2.setFormSize(15.f);
                set2.setLineWidth(1f);//设置线宽

                set2.setCircleRadius(4f);//设置焦点圆心的大小
                set2.setDrawCircleHole(true);
                set2.setCircleHoleRadius(2);
                set2.setCircleColorHole(Color.WHITE);
                set2.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
                set2.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
                set2.setHighlightEnabled(false);//是否禁用点击高亮线
                set2.setValueTextSize(9f);//设置显示值的文字大小
                set2.setDrawFilled(false);//设置禁用范围背景填充
                set2.setDrawValues(false);

                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                    //set1.setFillDrawable(drawable);
                } else {
                    set2.setFillColor(Color.BLACK);
                }

                dataSets.add(set2); // add the datasets
            }
        }

        if (archMonthFlowBean.getMcFlows().get(1).size() >= 2) {
            ArrayList<McFlowBean> list = archMonthFlowBean.getMcFlows().get(1);
            ArrayList<Entry> values = new ArrayList<Entry>();
            for (int i = 0; i < list.size(); i++) {
                McFlowBean mcFlowBean = list.get(i);
                if (mDayOrMonthFlag == 1) {
                    LocalDate date = new LocalDate(mcFlowBean.getDataTimeDay());
                    int index = date.getDayOfMonth();
                    String value = mcFlowBean.getFlow() + "";
                    if (max < mcFlowBean.getFlow()) {
                        max = Integer.parseInt(value);
                    }
                    Entry entry = new Entry(index, Float.parseFloat(value));
                    values.add(entry);
                } else if (mDayOrMonthFlag == 0) {
                    int index = Integer.parseInt(mcFlowBean.getDataTimeDay());
                    String value = mcFlowBean.getFlow() + "";
                    if (max < mcFlowBean.getFlow()) {
                        max = Integer.parseInt(value);
                    }
                    Entry entry = new Entry(index, Float.parseFloat(value));
                    values.add(entry);
                }
            }

            LineDataSet set1 = new LineDataSet(values, "DataSet 1");
            set1.setDrawIcons(false);
            // set the line to be drawn like this "- - - - - -"
            //set1.enableDashedLine(10f, 10f, 0f);
            set1.setColor(Color.RED);
            set1.setCircleColor(Color.RED);
            set1.setLineWidth(1f);
            set1.setCircleRadius(4f);//设置焦点圆心的大小
            set1.setDrawCircleHole(true);
            set1.setCircleHoleRadius(2);
            set1.setCircleColorHole(Color.WHITE);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(false);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 10f}, 0f));
            set1.setFormSize(15.f);
            set1.setDrawFilled(false);
            set1.setHighlightEnabled(false);
            set1.setDrawValues(false);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                //set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }
            dataSets.add(set1); // add the datasets
        }

        // create a data object with the datasets
        LineData data = new LineData(dataSets);
        // set data
        baojinglv_linechart.setData(data);
    }


}
