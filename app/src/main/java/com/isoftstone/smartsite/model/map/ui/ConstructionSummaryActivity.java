package com.isoftstone.smartsite.model.map.ui;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.patrolinfo.DepartmentMonthDataBean;
import com.isoftstone.smartsite.http.patrolinfo.DepartmentsMonthTasks;
import com.isoftstone.smartsite.http.patrolinfo.ReportDataBean;
import com.isoftstone.smartsite.http.patrolinfo.UserTaskCountBean;
import com.isoftstone.smartsite.model.map.adapter.PersonRankArrayAdapter;
import com.isoftstone.smartsite.model.map.bean.MyValueFomatter;
import com.isoftstone.smartsite.model.map.bean.MyXFormatter;
import com.isoftstone.smartsite.model.map.bean.PersonRankCompare;
import com.isoftstone.smartsite.model.map.bean.ReportDataBeanCompare;
import com.isoftstone.smartsite.utils.DensityUtils;
import com.isoftstone.smartsite.utils.LogUtils;
import com.isoftstone.smartsite.utils.ToastUtils;
import com.isoftstone.smartsite.widgets.CustomDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zw on 2017/11/25.
 */

public class ConstructionSummaryActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final int UPDATE_FIRST_CHART = 0x0001;
    private final int UPDATE_SECOND_CHART = 0x0002;
    private final int UPDATE_THIRD_CHART = 0x0003;
    private final int UPDATE_FOUR_CHART = 0x0004;
    private final int UPDATE_FIVE_CHART = 0x0005;
    private int currentUpdateChart = UPDATE_FIRST_CHART;

    private final int HANDLER_FIRST_CHART_OK = 0x0011;
    private final int HANDLER_FIRST_CHART_FAIL = 0x0012;
    private final int HANDLE_SECOND_CHART_OK = 0x0021;
    private final int HANDLE_SECOND_CHART_FAIL = 0x0022;
    private final int HANDLE_THRID_CHART_OK = 0x0031;
    private final int HANDLE_THRID_CHART_FAIL = 0x0032;
    private final int HANDLE_FOUR_CHART_OK = 0x0041;
    private final int HANDLE_FOUR_CHART_FAIL = 0x0042;
    private final int HANDLE_FIVE_CHART_OK = 0x0051;
    private final int HANDLE_FIVE_CHART_FAIL = 0x0052;

    private LineChart lineChart;
    private LineChart lineChart2;
    private LineChart lineChart3;
    private TextView tv_company_rank_date;
    private String selectDate;
    private CustomDatePicker customDatePicker;
    private LoadingDailog loadingDailog;
    private HttpPost httpPost;

    private List<ReportDataBean> reportDataBeans;
    private List<UserTaskCountBean> userTaskCountBeans;

    private String selectTwoDate,selectThirdDate,selectFourDate,selectFiveDate;
    private boolean isFirstInSecondChart = true;
    private boolean isFirstInThridChart = true;
    private boolean isFirstInFourChart = true;
    private boolean isFirstInFiveChart = true;

    private DepartmentMonthDataBean monthDataBean;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HANDLER_FIRST_CHART_OK:
                    isFirstInSecondChart = false;
                    updateFirstChartData();
                    //更新第二个图表
                    personRankAdapter = new PersonRankArrayAdapter(ConstructionSummaryActivity.this,R.layout.layout_summary_spinner, personRankList);
                    spinner_person_rank.setAdapter(personRankAdapter);
                    currentUpdateChart = UPDATE_SECOND_CHART;
                    spinner_person_rank.setSelection(0);
                    break;
                case HANDLER_FIRST_CHART_FAIL:
                    ToastUtils.showShort("没有获取到巡查单位任务排名数据！");
                    break;
                case HANDLE_SECOND_CHART_OK:
                    updateSecondChartData();
                    if(isFromFirst){
                        //更新第三个图表
                        spinnerThrid.setAdapter(personRankAdapter);
                        currentUpdateChart = UPDATE_THIRD_CHART;
                        spinnerThrid.setSelection(0);
                    } else {
                        loadingDailog.dismiss();
                    }

                    break;
                case HANDLE_SECOND_CHART_FAIL:
                    loadingDailog.dismiss();
                    ToastUtils.showShort("没有获取到月度人员任务完成排名数据！");
                    break;
                case HANDLE_THRID_CHART_OK:
                    updateThridChartData();
                    if(isFromFirst){
                        //更新第四个图表
                        currentUpdateChart = UPDATE_FOUR_CHART;
                        updateChart(selectFourDate);
                    } else {
                        loadingDailog.dismiss();
                    }
                    break;
                case HANDLE_THRID_CHART_FAIL:
                    ToastUtils.showShort("没有获取到单位月度任务量数据！");
                    break;
                case HANDLE_FOUR_CHART_OK:
                    updateFourChartData();
                    if(monthTasks == null || monthTasks.getData().size() == 0){
                        tv_four_view1.setVisibility(View.INVISIBLE);
                        tv_four_view2.setVisibility(View.INVISIBLE);
                        fourView1.setVisibility(View.INVISIBLE);
                        fourView2.setVisibility(View.INVISIBLE);
                    } else {
                        for (int i = 0; i < monthTasks.getList().size(); i++) {
                            LogUtils.e(TAG,monthTasks.getList().get(i).toString());
                        }
                        if(monthTasks.getList().get(1).size() == 0){
                            tv_four_view1.setVisibility(View.INVISIBLE);
                            fourView1.setVisibility(View.INVISIBLE);
                        } else {
                            tv_four_view1.setVisibility(View.VISIBLE);
                            fourView1.setVisibility(View.VISIBLE);
                            try {
//                                int departmentId = Integer.parseInt(monthTasks.getList().get(1).get(0).getDepartmentId());
//                                tv_four_view1.setText(httpPost.getCompanyNameByid(departmentId));
                                if(oldFourClickedId1 == -1){
                                    fourView1.setVisibility(View.INVISIBLE);
                                    tv_four_view1.setText("");
                                } else {
                                    fourView1.setVisibility(View.VISIBLE);
                                    tv_four_view1.setText(personRankList.get(oldFourClickedId1));
                                }
                            }catch (Exception e){
                                tv_four_view1.setText("");
                            }

                        }

                        if(monthTasks.getList().get(0).size() == 0){
                            tv_four_view2.setVisibility(View.INVISIBLE);
                            fourView2.setVisibility(View.INVISIBLE);
                        } else {
                            try{
//                                int departmentId = Integer.parseInt(monthTasks.getList().get(0).get(0).getDepartmentId());
//                                tv_four_view2.setText(httpPost.getCompanyNameByid(departmentId));
                                if(oldFourClickedId2 == -1){
                                    fourView2.setVisibility(View.INVISIBLE);
                                    tv_four_view2.setText("");
                                } else {
                                    tv_four_view2.setVisibility(View.VISIBLE);
                                    tv_four_view2.setText(personRankList.get(oldFourClickedId2));
                                }

                            }catch (Exception e){
                                tv_four_view2.setText("");
                            }

                        }
                    }

                    if(isFromFirst){
                        //更新第五个图表
                        currentUpdateChart = UPDATE_FIVE_CHART;
                        updateChart(selectFiveDate);
                    } else {
                        loadingDailog.dismiss();
                    }

                    break;
                case HANDLE_FOUR_CHART_FAIL:
                    ToastUtils.showShort("没有获取到单位月度任务量对比！");
                    break;
                case HANDLE_FIVE_CHART_OK:
                    updateFiveChartData();
                    if(fiveMonthTasks == null || fiveMonthTasks.getData().size() == 0){
                        tv_five_view1.setVisibility(View.INVISIBLE);
                        tv_five_view2.setVisibility(View.INVISIBLE);
                        fiveView1.setVisibility(View.INVISIBLE);
                        fiveView2.setVisibility(View.INVISIBLE);
                    } else {
                        if(fiveMonthTasks.getDate().get(1).size() == 0){
                            tv_five_view1.setVisibility(View.INVISIBLE);
                            fiveView1.setVisibility(View.INVISIBLE);
                        } else {
                            try{
//                                int departmentId = Integer.parseInt(fiveMonthTasks.getDate().get(1).get(0).getDepartmentId());
//                                tv_five_view1.setText(httpPost.getCompanyNameByid(departmentId));
                                if(oldFiveClickedId1 == -1){
                                    fiveView1.setVisibility(View.INVISIBLE);
                                    tv_five_view1.setText("");
                                }else {
                                    fiveView1.setVisibility(View.VISIBLE);
                                    tv_five_view1.setText(personRankList.get(oldFiveClickedId1));
                                }
                            }catch (Exception e){
                                tv_five_view1.setText("");
                            }


                        }

                        if(fiveMonthTasks.getDate().get(0).size() == 0){
                            tv_five_view2.setVisibility(View.INVISIBLE);
                            fiveView2.setVisibility(View.INVISIBLE);
                        } else {
                            try{
//                                int departmentId = Integer.parseInt(fiveMonthTasks.getDate().get(0).get(0).getDepartmentId());
//                                tv_five_view2.setText(httpPost.getCompanyNameByid(departmentId));
                                if(oldFiveClickedId2 == -1){
                                    fiveView2.setVisibility(View.INVISIBLE);
                                    tv_five_view2.setText("");
                                } else {
                                    fiveView2.setVisibility(View.VISIBLE);
                                    tv_five_view2.setText(personRankList.get(oldFiveClickedId2));
                                }
                            }catch (Exception e){
                                tv_five_view2.setText("");
                            }
                        }
                    }

                    if(isFromFirst){
                        currentUpdateChart = UPDATE_FIRST_CHART;
                        isFromFirst = false;
                    }
                    loadingDailog.dismiss();
                    break;
                case HANDLE_FIVE_CHART_FAIL:
                    ToastUtils.showShort("没有获取到单位月度报告量！");
                    break;
            }
        }
    };
    private HorizontalBarChart firstBarChart;
    private TextView tv_person_rank;
    private Spinner spinner_person_rank;
    private PersonRankArrayAdapter<String> personRankAdapter;

    private String currentPersonRankIdString = "";
    private String currentCompanyTotalIdString = "";
    private String currentCompanyTotalCompareIdString1 = "";
    private String currentCompanyTotalCompareIdString2 = "";
    private String currentCompanyMonthIdString1 = "";
    private String currentCompanyMonthIdString2 = "";

    private List<String> personRankList;
    private List<Integer> personRankIdList;

    private BarChart senondBarChart;
    private TextView tv_company_total;
    private PersonRankArrayAdapter<String> companyTotalAdapter;
    private ArrayList<String> companyTotalList;
    private Spinner spinnerThrid;
    private TextView tv_company_total_compare;
    private DepartmentsMonthTasks monthTasks,fiveMonthTasks;

    private boolean isFromFirst = true;
    private View fourView1;
    private View fourView2;
    private TextView tv_four_view1;
    private TextView tv_four_view2;
    private TextView tv_four_view_choose;
    private MyFourCheckboxAdapter fourCheckboxAdapter;
    private PopupWindow fourCheckPopwindow;
    private View fiveView1;
    private View fiveView2;
    private TextView tv_five_view1;
    private TextView tv_five_view2;
    private TextView tv_five_view_choose;
    private MyFourCheckboxAdapter myFiveCheckboxAdapter;
    private PopupWindow fiveCheckPopwindow;
    private TextView tv_company_month_report;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_construction_summary;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        httpPost = new HttpPost();

        initView();
        initDatePicker(selectDate);
        initLoadingDialog();

        initToolBar();
        initFirstBarChart();
        initSecondBarChart();
        initThridBarChart();
        initFourBarChart();
        initFiveBarChart();

        updateChart(selectDate);
    }

    private void initView(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        selectDate = sdf.format(new Date());

        tv_company_rank_date = (TextView) findViewById(R.id.tv_company_rank_date);

        tv_company_rank_date.setText(parseTimeOnlyYearAndMonth(selectDate));
        tv_company_rank_date.setOnClickListener(this);

        //second
        tv_person_rank = (TextView) findViewById(R.id.tv_person_rank);
        tv_person_rank.setText(parseTimeOnlyYearAndMonth(selectDate));
        tv_person_rank.setOnClickListener(this);
        spinner_person_rank = (Spinner) findViewById(R.id.spinner_person_rank);
        personRankList = new ArrayList<>();
        personRankIdList = new ArrayList<>();
        personRankList.add("选择单位");
        personRankAdapter = new PersonRankArrayAdapter(this,R.layout.layout_summary_spinner, personRankList);
        personRankAdapter.setDropDownViewResource(R.layout.layout_summary_spinner_item);
        spinner_person_rank.setAdapter(personRankAdapter);
        spinner_person_rank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isFirstInSecondChart) {
                    isFirstInSecondChart = false;
                    return;
                }

                currentUpdateChart = UPDATE_SECOND_CHART;
                currentPersonRankIdString = personRankIdList.get(position) + "";
                updateChart(selectTwoDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_person_rank.setDropDownVerticalOffset(DensityUtils.dip2px(this,36));

        //third
        tv_company_total = (TextView) findViewById(R.id.tv_company_total);
        tv_company_total.setText(parseTimeOnlyYearAndMonth(selectDate));
        tv_company_total.setOnClickListener(this);
        spinnerThrid = (Spinner) findViewById(R.id.spinner_company_total);
        companyTotalList = new ArrayList<>();
        companyTotalList.add("选择单位");
        companyTotalAdapter = new PersonRankArrayAdapter(this, R.layout.layout_summary_spinner, companyTotalList);
        companyTotalAdapter.setDropDownViewResource(R.layout.layout_summary_spinner_item);
        spinnerThrid.setAdapter(companyTotalAdapter);
        spinnerThrid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isFirstInThridChart) {
                    isFirstInThridChart = false;
                    return;
                }

                currentUpdateChart = UPDATE_THIRD_CHART;
                currentCompanyTotalIdString = personRankIdList.get(position) + "";
                updateChart(selectThirdDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerThrid.setDropDownVerticalOffset(DensityUtils.dip2px(this,36));

        //four
        tv_company_total_compare = (TextView) findViewById(R.id.tv_company_total_compare);
        tv_company_total_compare.setText(parseTimeOnlyYearAndMonth(selectDate));
        tv_company_total_compare.setOnClickListener(this);

        fourView1 = findViewById(R.id.four_view1);
        fourView2 = findViewById(R.id.four_view2);
        tv_four_view1 = (TextView) findViewById(R.id.tv_four_view1);
        tv_four_view2 = (TextView) findViewById(R.id.tv_four_view2);
        tv_four_view_choose = (TextView) findViewById(R.id.tv_company_total_compare_choose);
        tv_four_view_choose.setOnClickListener(this);
        initFourChartPopWindow();

        //five
        tv_company_month_report = (TextView) findViewById(R.id.tv_company_month_report);
        tv_company_month_report.setText(parseTimeOnlyYearAndMonth(selectDate));
        tv_company_month_report.setOnClickListener(this);

        fiveView1 = findViewById(R.id.five_view1);
        fiveView2 = findViewById(R.id.five_view2);
        tv_five_view1 = (TextView) findViewById(R.id.tv_five_view1);
        tv_five_view2 = (TextView) findViewById(R.id.tv_five_view2);
        tv_five_view_choose = (TextView) findViewById(R.id.tv_company_month_report_choose);
        tv_five_view_choose.setOnClickListener(this);
        initFiveChartPopWindow();
    }

    private void initDatePicker(String date){
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                //时间格式：2017-06-01 00:00
                updateChart(time);
            }
        },"2010-01-01 00:00",date);
        customDatePicker.showYearMonth();
        customDatePicker.setIsLoop(false); // 不允许循环滚动
    }

    private void initLoadingDialog(){
        loadingDailog = new LoadingDailog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(true)
                .setCancelOutside(false).create();
    }

    private void initToolBar(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        TextView tv_title = (TextView) findViewById(R.id.toolbar_title);
        tv_title.setText("巡查概况");
        ImageButton imageButton = (ImageButton) findViewById(R.id.btn_icon);
        imageButton.setOnClickListener(this);
        imageButton.setImageResource(R.drawable.environmentlist);
        imageButton.setVisibility(View.GONE);
    }

    private void initFirstBarChart() {
        firstBarChart = (HorizontalBarChart) findViewById(R.id.hbc);

        firstBarChart.setTouchEnabled(false); // 设置是否可以触摸
        firstBarChart.setDragEnabled(false);// 是否可以拖拽
        firstBarChart.setScaleEnabled(false);// 是否可以缩放
        firstBarChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放

        //设置显示边界
        firstBarChart.setDrawBorders(false);

        //网格
        firstBarChart.setDrawGridBackground(false);

        //不显示描述
        Legend legend = firstBarChart.getLegend();
        legend.setEnabled(false);
        firstBarChart.setDescription(null);

        firstBarChart.setNoDataText("没有获取到数据。");
    }

    private void initSecondBarChart(){
        senondBarChart = (BarChart) findViewById(R.id.person_rank_chart);
        senondBarChart.setTouchEnabled(false); // 设置是否可以触摸
        senondBarChart.setDragEnabled(false);// 是否可以拖拽
        senondBarChart.setScaleEnabled(false);// 是否可以缩放
        senondBarChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放

        //设置显示边界
        senondBarChart.setDrawBorders(false);

        //网格
        senondBarChart.setDrawGridBackground(false);

        //不显示描述
        Legend legend = senondBarChart.getLegend();
        legend.setEnabled(false);
        senondBarChart.setDescription(null);

        senondBarChart.setNoDataText("没有获取到数据。");

    }

    private void initThridBarChart(){
        lineChart = (LineChart) findViewById(R.id.company_total_line_chart);

        //设置显示边界
        lineChart.setDrawBorders(false);

        //网格
        lineChart.setDrawGridBackground(false);

        //不显示描述
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        lineChart.setDescription(null);

        lineChart.setNoDataText("没有获取到数据。");

        lineChart.setTouchEnabled(false); // 设置是否可以触摸
        lineChart.setDragEnabled(false);// 是否可以拖拽
        lineChart.setScaleEnabled(false);// 是否可以缩放
        lineChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放

    }

    private void initFourBarChart(){
        lineChart2 = (LineChart) findViewById(R.id.linechart4);

        //设置显示边界
        lineChart2.setDrawBorders(false);

        //网格
        lineChart2.setDrawGridBackground(false);

        //不显示描述
        Legend legend = lineChart2.getLegend();
        legend.setEnabled(false);
        lineChart2.setDescription(null);

        lineChart2.setNoDataText("没有获取到数据。");

        lineChart2.setTouchEnabled(false); // 设置是否可以触摸
        lineChart2.setDragEnabled(false);// 是否可以拖拽
        lineChart2.setScaleEnabled(false);// 是否可以缩放
        lineChart2.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放
    }

    private void initFiveBarChart(){
        lineChart3 = (LineChart) findViewById(R.id.linechart5);

        //设置显示边界
        lineChart3.setDrawBorders(false);

        //网格
        lineChart3.setDrawGridBackground(false);

        lineChart3.setNoDataText("没有获取到数据。");

        //不显示描述
        Legend legend = lineChart3.getLegend();
        legend.setEnabled(false);
        lineChart3.setDescription(null);

        lineChart3.setTouchEnabled(false); // 设置是否可以触摸
        lineChart3.setDragEnabled(false);// 是否可以拖拽
        lineChart3.setScaleEnabled(false);// 是否可以缩放
        lineChart3.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放
    }

    private void updateChart(String time){
        String year = time.substring(0,4);
        String month = time.substring(5,7);
        String strTime = year + "年" + month + "月";

        loadingDailog.show();
        if(currentUpdateChart == UPDATE_FIRST_CHART){
            selectDate = time;
            selectTwoDate = time;
            selectThirdDate = time;
            selectFourDate = time;
            selectFiveDate = time;
            tv_company_rank_date.setText(strTime);
            loadFirstChartData(year,month);
        } else if(currentUpdateChart == UPDATE_SECOND_CHART){
            selectTwoDate = time;
            tv_person_rank.setText(strTime);
            loadSecondCharData(year,month);
        } else if(currentUpdateChart == UPDATE_THIRD_CHART){
            selectThirdDate = time;
            tv_company_total.setText(strTime);
            loadThirdChartDate(year,month);
        } else if(currentUpdateChart == UPDATE_FOUR_CHART){
            selectFourDate = time;
            tv_company_total_compare.setText(strTime);
            loadFourChartDate(year,month);
        } else if(currentUpdateChart == UPDATE_FIVE_CHART){
            selectFiveDate = time;
            tv_company_month_report.setText(strTime);
            loadFiveChartDate(year,month);
        }
    }

    private void loadFirstChartData(final String year,final String month){
        new Thread(new Runnable() {
            @Override
            public void run() {
                reportDataBeans = httpPost.getPatrolReportData(year + "-" + month);
                if(reportDataBeans != null && reportDataBeans.size() != 0){
                    Collections.sort(reportDataBeans,new ReportDataBeanCompare());
                    //更新第二个图表
                    currentPersonRankIdString = reportDataBeans.get(0).getDepartmentId();
                    personRankList = new ArrayList<>();
                    for (int i = 0; i < reportDataBeans.size(); i++) {
                        int departmentId = Integer.parseInt(reportDataBeans.get(i).getDepartmentId());
                        String companyName = httpPost.getCompanyNameByid(departmentId);
                        personRankList.add(companyName);
                        personRankIdList.add(departmentId);
                    }

                    //更新第三个图表
                    currentCompanyTotalIdString = reportDataBeans.get(0).getDepartmentId();
                    //更新第四个图表
                    currentCompanyTotalCompareIdString2 = reportDataBeans.get(0).getDepartmentId();
                    oldFourClickedId2 = 0;
                    //更新第五个图表
                    currentCompanyMonthIdString2 = reportDataBeans.get(0).getDepartmentId();
                    oldFiveClickedId2 = 0;

                    if(reportDataBeans.size() > 1){
                        currentCompanyTotalCompareIdString1 = reportDataBeans.get(1).getDepartmentId();
                        currentCompanyMonthIdString1 = reportDataBeans.get(1).getDepartmentId();
                        oldFourClickedId1 = 1;
                        oldFiveClickedId1 = 1;
                    }

                }else {
                    mHandler.sendEmptyMessage(HANDLER_FIRST_CHART_FAIL);
                }
                mHandler.sendEmptyMessage(HANDLER_FIRST_CHART_OK);
            }
        }).start();
    }

    private void loadSecondCharData(final String year,final String month){
        new Thread(new Runnable() {
            @Override
            public void run() {
                userTaskCountBeans = httpPost.getDepartmentUserTaskData(year + "-" + month,currentPersonRankIdString);

                if(userTaskCountBeans != null && userTaskCountBeans.size() != 0){
                    Collections.sort(userTaskCountBeans,new PersonRankCompare());
                } else {
                    mHandler.sendEmptyMessage(HANDLE_SECOND_CHART_FAIL);
                }
                mHandler.sendEmptyMessage(HANDLE_SECOND_CHART_OK);
            }
        }).start();
    }

    private void loadThirdChartDate(final String year,final String month){
        new Thread(new Runnable() {
            @Override
            public void run() {
                monthDataBean = httpPost.getDepartmentMonthDat(year + "-" + month,currentCompanyTotalIdString);
                if(monthDataBean == null || monthDataBean.getAll().size() == 0){
                    mHandler.sendEmptyMessage(HANDLE_THRID_CHART_FAIL);
                }
                mHandler.sendEmptyMessage(HANDLE_THRID_CHART_OK);
            }
        }).start();
    }

    private void loadFourChartDate(final String year,final String month){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] strs = null;
                if(TextUtils.isEmpty(currentCompanyTotalCompareIdString1)){
                    strs = new String[]{currentCompanyTotalCompareIdString2};
                } else {
                    strs = new String[]{currentCompanyTotalCompareIdString2,currentCompanyTotalCompareIdString1};
                }
                monthTasks = httpPost.getDepartmentsMonthTasks(year + "-" + month,strs);
                if(monthTasks == null || (monthTasks.getData().size() == 0 && monthTasks.getList().size() == 0)){
                    mHandler.sendEmptyMessage(HANDLE_FOUR_CHART_FAIL);
                }
                mHandler.sendEmptyMessage(HANDLE_FOUR_CHART_OK);

            }
        }).start();

    }

    private void loadFiveChartDate(final String year,final String month){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] strs = null;
                if(TextUtils.isEmpty(currentCompanyMonthIdString1)){
                    strs = new String[]{currentCompanyMonthIdString2};
                } else {
                    strs = new String[]{currentCompanyMonthIdString2,currentCompanyMonthIdString1};
                }
                fiveMonthTasks = httpPost.getDepartmentReport(year + "-" + month,strs);
                if(fiveMonthTasks == null || (fiveMonthTasks.getData().size() == 0 && fiveMonthTasks.getDate().size() == 0)){
                    mHandler.sendEmptyMessage(HANDLER_FIRST_CHART_FAIL);
                }
                mHandler.sendEmptyMessage(HANDLE_FIVE_CHART_OK);

            }
        }).start();
    }

    private void updateFirstChartData(){
        if(reportDataBeans != null && reportDataBeans.size() != 0){
            int dataCount = reportDataBeans.size() > 5 ? 5 : reportDataBeans.size();
            int maxCount = reportDataBeans.get(0).getUnCount() + reportDataBeans.get(0).getOff();

            if(reportDataBeans.get(0).getOff() == 0){

            }

            if(dataCount > 5){
                reportDataBeans = reportDataBeans.subList(0,5);
            }
            ArrayList tempList = new ArrayList();
            for (int i = 0; i < dataCount; i++) {
                tempList.add(0,reportDataBeans.get(i));
            }
            reportDataBeans = tempList;

            ViewGroup.LayoutParams layoutParams = firstBarChart.getLayoutParams();
            layoutParams.height = DensityUtils.dip2px(this,50 * dataCount);

            //X轴
            XAxis xAxis = firstBarChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(false);

            xAxis.setAxisLineColor(Color.parseColor("#dddddd"));


            //Y轴
            YAxis yLeftAxis = firstBarChart.getAxisLeft();
            YAxis yRrightAxis = firstBarChart.getAxisRight();
            yLeftAxis.setAxisLineColor(Color.parseColor("#dddddd"));
            yRrightAxis.setAxisLineColor(Color.parseColor("#dddddd"));
            yLeftAxis.setAxisMaximum(maxCount);
            yLeftAxis.setAxisMinimum(0);
            yRrightAxis.setAxisMaximum(maxCount);
            yRrightAxis.setAxisMinimum(0);
            yLeftAxis.setDrawLabels(false);
//        if(maxCount > 100){
//            yRrightAxis.setLabelCount(maxCount/100);
//            yRrightAxis.setLabelCount(maxCount/100);
//        } else {
//            yRrightAxis.setLabelCount(maxCount/10);
//            yRrightAxis.setLabelCount(maxCount/10);
//        }
            yRrightAxis.setLabelCount(10);
            yRrightAxis.setLabelCount(10);
            yLeftAxis.setGridColor(Color.parseColor("#ededed"));
            yRrightAxis.setGridColor(Color.parseColor("#ededed"));

            //保证Y轴从0开始，不然会上移一点
            yLeftAxis.setAxisMinimum(0f);
            yRrightAxis.setAxisMinimum(0f);

            //设置x轴的数据
            ArrayList<Float> xValues = new ArrayList<>();
            ArrayList<String> xVaulesName = new ArrayList<>();
            for (int i = 0; i < dataCount; i++) {
                xValues.add((float) i);
                if(personRankList.size() >= 5){
                    xVaulesName.add(personRankList.get(4 - i));
                } else {
                    xVaulesName.add(personRankList.get(personRankList.size() - 1 - i));
                }

            }

            xAxis.setLabelCount(xValues.size() - 1,false);
            MyXFormatter xFormatter = new MyXFormatter(xVaulesName);
            xAxis.setValueFormatter(xFormatter);

            //设置y轴的数据()
            List<List<Float>> yValues = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                List<Float> yValue = new ArrayList<>();
                for (int j = 0; j < dataCount; j++) {
                    if(i == 0){
                        float temp = (float)reportDataBeans.get(j).getOff() + reportDataBeans.get(j).getUnCount();
                        yValue.add(temp);
                    } else {
                        yValue.add((float) reportDataBeans.get(j).getOff());
                    }

                }
                yValues.add(yValue);
            }

            //颜色集合
            List<Integer> colours = new ArrayList<>();
            colours.add(Color.parseColor("#c6c6c6"));
            colours.add(Color.parseColor("#3464dd"));


            //线的名字集合
            List<String> names = new ArrayList<>();
            names.add("折线一");
            names.add("折线二");

            BarData barData = new BarData();
            barData.setBarWidth(0.5f);
            for (int i = 0; i < yValues.size(); i++) {
                ArrayList<BarEntry> entries = new ArrayList<>();
                for (int j = 0; j < yValues.get(i).size() ; j++) {

                    entries.add(new BarEntry(xValues.get(j), yValues.get(i).get(j)));
                }
                BarDataSet barDataSet = new BarDataSet(entries, names.get(i));

                barDataSet.setColor(colours.get(i));
                barDataSet.setValueTextColor(Color.WHITE);
                barDataSet.setValueTextSize(10f);
                barDataSet.setValueFormatter(new MyValueFomatter());
                barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

                for (int j = 0; j < yValues.get(i).size() ; j++) {
                    float temp = xValues.get(j);
                    if(temp < 1){
                        barDataSet.setDrawValues(false);
                        break;
                    }
                }

                barData.addDataSet(barDataSet);
            }

            firstBarChart.setDrawValueAboveBar(false);
            firstBarChart.setData(barData);
            firstBarChart.invalidate();
        } else {
            firstBarChart.setDrawValueAboveBar(false);
            firstBarChart.setData(null);
            firstBarChart.invalidate();
        }


    }

    private void updateSecondChartData(){
        if(userTaskCountBeans != null && userTaskCountBeans.size() != 0){

            int dataCount = userTaskCountBeans.size() > 5 ? 5 : userTaskCountBeans.size();
            int maxCount = userTaskCountBeans.get(0).getUnCount() + userTaskCountBeans.get(0).getOffCount();

            ArrayList tempList = new ArrayList();
            for (int i = 0; i < dataCount; i++) {
                tempList.add(userTaskCountBeans.get(i));
            }
            userTaskCountBeans = tempList;

            //X轴
            XAxis xAxis = senondBarChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisLineColor(Color.parseColor("#dddddd"));

            //Y轴
            YAxis yLeftAxis = senondBarChart.getAxisLeft();
            YAxis yRrightAxis = senondBarChart.getAxisRight();
            yLeftAxis.setAxisLineColor(Color.parseColor("#dddddd"));
            yRrightAxis.setAxisLineColor(Color.TRANSPARENT);
            yLeftAxis.setAxisMaximum(maxCount > 5 ? maxCount : 5);
            yLeftAxis.setAxisMinimum(0);
            yRrightAxis.setAxisMaximum(maxCount > 5 ? maxCount : 5);
            yRrightAxis.setAxisMinimum(0);
            yRrightAxis.setDrawLabels(false);
            yLeftAxis.setLabelCount(5);
            yLeftAxis.setGridColor(Color.parseColor("#ededed"));
            yRrightAxis.setGridColor(Color.parseColor("#ededed"));

            //保证Y轴从0开始，不然会上移一点
            yLeftAxis.setAxisMinimum(0f);
            yRrightAxis.setAxisMinimum(0f);

            //设置x轴的数据
            ArrayList<Float> xValues = new ArrayList<>();
            ArrayList<String> xVaulesName = new ArrayList<>();
            for (int i = 0; i < dataCount; i++) {
                if(userTaskCountBeans.get(i).getUser() != null){
                    xVaulesName.add(userTaskCountBeans.get(i).getUser().getName());
                    xValues.add((float) i);
                }
            }
            if(dataCount < 5){
                for (int i = 0; i < 5 - dataCount; i++) {
                    xVaulesName.add("");
                    xValues.add((float) i);
                }
            }
            xAxis.setLabelCount(xValues.size() - 1,false);
            MyXFormatter xFormatter = new MyXFormatter(xVaulesName);
            xAxis.setValueFormatter(xFormatter);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            //设置y轴的数据()
            List<List<Float>> yValues = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                List<Float> yValue = new ArrayList<>();
                for (int j = 0; j < dataCount; j++) {
                    if(userTaskCountBeans.get(j) != null){
                        if(i == 0){
                            yValue.add(userTaskCountBeans.get(j).getOffCount() == 0 ? (float) 0.2 : userTaskCountBeans.get(j).getOffCount());
                        } else {
                            yValue.add((float) (userTaskCountBeans.get(j).getUnCount() == 0 ? 0.2 : userTaskCountBeans.get(j).getUnCount()));
                        }

                    }
                }
                yValues.add(yValue);
            }

            //颜色集合
            List<Integer> colours = new ArrayList<>();
            colours.add(Color.parseColor("#3464dd"));
            colours.add(Color.parseColor("#c6c6c6"));

            //线的名字集合
            List<String> names = new ArrayList<>();
            names.add("折线一");
            names.add("折线二");

            BarData barData = new BarData();
            barData.setBarWidth(0.5f);
            for (int i = 0; i < yValues.size(); i++) {
                ArrayList<BarEntry> entries = new ArrayList<>();
                for (int j = 0; j < yValues.get(i).size(); j++) {
                    entries.add(new BarEntry(xValues.get(j), yValues.get(i).get(j)));
                }
                BarDataSet barDataSet = new BarDataSet(entries, names.get(i));

                barDataSet.setDrawValues(false);
                barDataSet.setColor(colours.get(i));
                barDataSet.setValueTextColor(Color.WHITE);
                barDataSet.setValueTextSize(10f);
                barDataSet.setValueFormatter(new MyValueFomatter());
                barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                barData.addDataSet(barDataSet);
            }



            float groupSpace = 0.3f; //柱状图组之间的间距

            float barWidth = 0.25f;
            float barSpace = 0.1f;

            // (0.2 + 0.02) * 4 + 0.08 = 1.00 -> interval per "group"
            barData.setBarWidth(barWidth);


            barData.groupBars(0, groupSpace, barSpace);

            senondBarChart.setDrawValueAboveBar(false);

            senondBarChart.setData(barData);

            xAxis.setAxisMaximum(5);
            xAxis.setAxisMinimum(0);
            xAxis.setLabelCount(5,false);
            xAxis.setCenterAxisLabels(true);
            senondBarChart.invalidate();
        } else {
            senondBarChart.setData(null);
            senondBarChart.invalidate();
        }


    }

    private void updateThridChartData(){
        if(monthDataBean != null && monthDataBean.getAll() != null
                && monthDataBean.getAll().size() != 0){

            int monthDays = getMonthDays(monthDataBean.getAll().get(0).getTime());
            ArrayList<ReportDataBean> all = new ArrayList<>();
            ArrayList<ReportDataBean> off = new ArrayList<>();
            for (int i = 0; i < monthDays; i++) {
                ReportDataBean bean = new ReportDataBean();
                bean.setUnCount(0);
                bean.setOff(0);
                all.add(bean);
                off.add(bean);
            }
            int allMax = monthDataBean.getAll().get(0).getUnCount() + monthDataBean.getAll().get(0).getOff();
//            int offMax = monthDataBean.getOff().get(0).getOff();

            for (int i = 0; i < monthDataBean.getAll().size(); i++) {
                int temp = monthDataBean.getAll().get(i).getCount();
                if(temp > allMax){
                    allMax = temp;
                }
                int day = replaceTime(monthDataBean.getAll().get(i).getTime()) - 1;
                all.remove(day);
                all.add(day,monthDataBean.getAll().get(i));

            }
//            for (int i = 0; i < monthDataBean.getOff().size(); i++) {
//                int temp = monthDataBean.getOff().get(i).getOff();
//                if(temp > offMax){
//                    offMax = temp;
//                }
//                int day = replaceTime(monthDataBean.getOff().get(i).getTime()) - 1;
//                off.remove(day);
//                off.add(day,monthDataBean.getOff().get(i));
//            }

            //X轴
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisLineColor(Color.parseColor("#dddddd"));

            //Y轴
            YAxis yLeftAxis = lineChart.getAxisLeft();
            YAxis yRrightAxis = lineChart.getAxisRight();
            yLeftAxis.setAxisLineColor(Color.parseColor("#dddddd"));
            yRrightAxis.setAxisLineColor(Color.TRANSPARENT);
            yLeftAxis.setAxisMaximum((float) ((allMax > 5 ? allMax : 5 ) * 1.5));
            yLeftAxis.setAxisMinimum(0);
            yRrightAxis.setAxisMaximum(allMax);
            yRrightAxis.setAxisMinimum(0);
            yRrightAxis.setDrawLabels(false);
            yLeftAxis.setLabelCount(5);
            yLeftAxis.setGridColor(Color.parseColor("#ededed"));
            yRrightAxis.setGridColor(Color.parseColor("#ededed"));

            //保证Y轴从0开始，不然会上移一点
            yLeftAxis.setAxisMinimum(0f);
            yRrightAxis.setAxisMinimum(0f);

            //设置x轴的数据
            ArrayList<Float> xValues = new ArrayList<>();
            ArrayList<String> xVaulesName = new ArrayList<>();
            for (int i = 1; i <= monthDays; i++) {
                xVaulesName.add((i < 10 ? "0" : "") + i);
                xValues.add((float) i);
            }
            if(monthDays % 2 == 0){
                xVaulesName.add(0,"0");
                xValues.add(0,0f);
            }
//            xAxis.setLabelCount(xValues.size(),false);
            MyXFormatter xFormatter = new MyXFormatter(xVaulesName);
            xAxis.setValueFormatter(xFormatter);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            //设置y轴的数据()
            List<Entry> yValues1 = new ArrayList<>();
            List<Entry> yValues2 = new ArrayList<>();

            if(monthDays % 2 == 0){
                for (int i = 0; i < all.size(); i++) {
                    yValues1.add(new Entry(i + 1,all.get(i).getCount()));
                    yValues2.add(new Entry(i + 1,off.get(i).getOff()));
                }
                yValues1.add(0,new Entry(0,0));
                yValues2.add(0,new Entry(0,0));
            } else {
                for (int i = 0;i < all.size(); i++){
//                yValues1.add(new Entry(i,all.get(i).getOff() + all.get(i).getUnCount()));
                    yValues1.add(new Entry(i,all.get(i).getCount()));
                    yValues2.add(new Entry(i,off.get(i).getOff()));
                }
            }



            LineDataSet set1 = null;
            LineDataSet set2 = null;
            if (lineChart.getData() != null &&
                    lineChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
                set2 = (LineDataSet) lineChart.getData().getDataSetByIndex(1);
                set1.setValues(yValues1);
                set2.setValues(yValues2);
                lineChart.getData().notifyDataChanged();
                lineChart.notifyDataSetChanged();
            } else {

                // create a dataset and give it a type
                set1 = new LineDataSet(yValues1, "DataSet");
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setColor(Color.parseColor("#c6c6c6"));
                set1.setDrawCircles(false);
                set1.setLineWidth(1f);
                set1.setCircleRadius(5f);
                set1.setFillAlpha(255);
                set1.setDrawFilled(true);

                set1.setFillColor(Color.parseColor("#c6c6c6"));
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                set1.setDrawCircleHole(false);
//            set.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return lineChart.getAxisLeft().getAxisMaximum();
//                }
//            });

                // create a dataset and give it a type
                set2 = new LineDataSet(yValues2, "DataSet");
                set2.setAxisDependency(YAxis.AxisDependency.LEFT);
                set2.setColor(Color.parseColor("#3464dd"));
                set2.setDrawCircles(false);
                set2.setLineWidth(1f);
                set2.setCircleRadius(5f);
                set2.setFillAlpha(255);
                set2.setDrawFilled(true);
                set2.setFillColor(Color.parseColor("#3464dd"));
                set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                set2.setDrawCircleHole(false);

                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1); // add the datasets
                dataSets.add(set2);

                // create a data object with the datasets
                LineData data = new LineData(dataSets);
                data.setDrawValues(false);

                // set data
                lineChart.setData(data);
            }

            xAxis.setAxisMaximum(xValues.size());
            xAxis.setAxisMinimum(0);
            xAxis.setLabelCount(xValues.size()/2,false);
            lineChart.invalidate();
        } else {
            lineChart.setData(null);
            lineChart.invalidate();
        }

    }

    private void updateFourChartData(){
        if(monthTasks != null && monthTasks.getData().size() != 0){

            int month = getMonthDays(monthTasks.getData().get(0));
            int maxCount = 0;

            List<ReportDataBean> first = new ArrayList<>();
            List<ReportDataBean> second = new ArrayList<>();
            for (int i = 0; i < month; i++) {
                ReportDataBean bean = new ReportDataBean();
                bean.setCount(0);
                first.add(bean);
                second.add(bean);
            }
            if(monthTasks.getList().get(0).size() != 0){
                for (int i = 0; i < monthTasks.getList().get(0).size(); i++) {
                    ReportDataBean bean = monthTasks.getList().get(0).get(i);

                    int temp = bean.getCount();
                    if(temp > maxCount){
                        maxCount = temp;
                    }

                    int day = replaceTime(bean.getTime()) - 1;
                    first.remove(day);
                    first.add(day,bean);
                }
            }

            if(monthTasks.getList().get(1).size() != 0){
                for (int i = 0; i < monthTasks.getList().get(1).size(); i++) {
                    ReportDataBean bean = monthTasks.getList().get(1).get(i);

                    int temp = bean.getCount();
                    if(temp > maxCount){
                        maxCount = temp;
                    }

                    int day = replaceTime(bean.getTime()) - 1;
                    second.remove(day);
                    second.add(day,bean);
                }
            }


            //X轴
            XAxis xAxis = lineChart2.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(true);
            xAxis.setGridColor(Color.parseColor("#ededed"));
            xAxis.setAxisLineColor(Color.parseColor("#dddddd"));

            //Y轴
            YAxis yLeftAxis = lineChart2.getAxisLeft();
            YAxis yRrightAxis = lineChart2.getAxisRight();
            yLeftAxis.setAxisLineColor(Color.parseColor("#dddddd"));
            yRrightAxis.setAxisLineColor(Color.TRANSPARENT);
            yLeftAxis.setAxisMaximum(maxCount > 5 ? maxCount : 5);
            yLeftAxis.setAxisMinimum(0);
            yRrightAxis.setAxisMaximum(maxCount > 5 ? maxCount : 5);
            yRrightAxis.setAxisMinimum(0);
            yRrightAxis.setDrawLabels(false);
            yLeftAxis.setLabelCount(5);
            yLeftAxis.setGridColor(Color.parseColor("#ededed"));
            yRrightAxis.setGridColor(Color.parseColor("#ededed"));

            //保证Y轴从0开始，不然会上移一点
            yLeftAxis.setAxisMinimum(0f);
            yRrightAxis.setAxisMinimum(0f);

            //设置x轴的数据
            ArrayList<Float> xValues = new ArrayList<>();
            ArrayList<String> xVaulesName = new ArrayList<>();
            for (int i = 1; i <= month; i++) {
                xVaulesName.add((i < 10 ? "0" : "") + i);
                xValues.add((float) i);
            }
            if(month % 2 == 0){
                xVaulesName.add(0,"0");
                xValues.add(0,0f);
            }
            xAxis.setLabelCount(xValues.size(),false);
            MyXFormatter xFormatter = new MyXFormatter(xVaulesName);
            xAxis.setValueFormatter(xFormatter);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            //设置y轴的数据()
            List<Entry> yValues1 = new ArrayList<>();
            List<Entry> yValues2 = new ArrayList<>();
            boolean drawY1 = false;
            boolean drawY2 = false;
            if(month % 2 == 0){
                for (int i = 0;i < month; i++){
                    int firstMin = first.get(i).getCount();
                    int secondMin = second.get(i).getCount();
                    if(firstMin != 0){
                        drawY1 = true;
                    }
                    if(secondMin != 0){
                        drawY2 = true;
                    }
                    yValues1.add(new Entry(i + 1,firstMin));
                    yValues2.add(new Entry(i + 1,secondMin));
                }
                yValues1.add(0,new Entry(0,0));
                yValues2.add(0,new Entry(0,0));
            } else {
                for (int i = 0;i < month; i++){
                    int firstMin = first.get(i).getCount();
                    int secondMin = second.get(i).getCount();
                    if(firstMin != 0){
                        drawY1 = true;
                    }
                    if(secondMin != 0){
                        drawY2 = true;
                    }
                    yValues1.add(new Entry(i,firstMin));
                    yValues2.add(new Entry(i,secondMin));
                }
            }



            LineDataSet set1 = null;
            LineDataSet set2 = null;
            if (lineChart2.getData() != null &&
                    lineChart2.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) lineChart2.getData().getDataSetByIndex(0);
                set2 = (LineDataSet) lineChart2.getData().getDataSetByIndex(1);

                set1.setValues(yValues1);
                set2.setValues(yValues2);
                set1.setVisible(drawY1);
                set2.setVisible(drawY2);
                lineChart2.getData().notifyDataChanged();
                lineChart2.notifyDataSetChanged();
            } else {

                // create a dataset and give it a type
                set1 = new LineDataSet(yValues1,"DataSet");
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                //orange
                set1.setColor(Color.parseColor("#ff9e5d"));
                set1.setDrawCircles(true);
                set1.setLineWidth(2f);
                set1.setCircleRadius(4f);
                set1.setCircleColor(Color.parseColor("#ff9e5d"));
                set1.setFillAlpha(255);
                set1.setDrawFilled(false);
                set1.setVisible(drawY1);

                set1.setFillColor(Color.parseColor("#c6c6c6"));
                set1.setMode(LineDataSet.Mode.LINEAR);
                set1.setDrawCircleHole(true);
                set1.setCircleHoleRadius(2f);
                set1.setCircleColorHole(Color.WHITE);
//            set.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return lineChart.getAxisLeft().getAxisMaximum();
//                }
//            });

                // create a dataset and give it a type
                set2 = new LineDataSet(yValues2, "DataSet");
                set2.setAxisDependency(YAxis.AxisDependency.LEFT);
                set2.setColor(Color.parseColor("#61a4ff"));
                set2.setDrawCircles(true);
                set2.setCircleColor(Color.parseColor("#61a4ff"));
                set2.setLineWidth(2f);
                set2.setCircleRadius(4f);
                set2.setFillAlpha(255);
                set2.setDrawFilled(false);
                set2.setFillColor(Color.parseColor("#3464dd"));
                set2.setMode(LineDataSet.Mode.LINEAR);
                set2.setDrawCircleHole(true);
                set2.setCircleHoleRadius(2f);
                set2.setCircleColorHole(Color.WHITE);
                set2.setVisible(drawY2);

                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1); // add the datasets
                dataSets.add(set2);

                // create a data object with the datasets
                LineData data = new LineData(dataSets);
                data.setDrawValues(false);

                // set data
                lineChart2.setData(data);
            }

            xAxis.setAxisMaximum(xValues.size());
            xAxis.setAxisMinimum(0);
//            xAxis.setLabelCount(xValues.size() % 2 == 0 ? xValues.size()/2 : (int)(xValues.size()/2) + 1,false);
            xAxis.setLabelCount(xValues.size()/2,false);
            lineChart2.invalidate();
        } else {
            lineChart2.setData(null);
            lineChart2.invalidate();
        }


    }

    private void updateFiveChartData(){
        if(fiveMonthTasks != null && fiveMonthTasks.getData().size() != 0){

            int month = getMonthDays(fiveMonthTasks.getData().get(0));
            int maxCount = 0;

            List<ReportDataBean> first = new ArrayList<>();
            List<ReportDataBean> second = new ArrayList<>();
            for (int i = 0; i < month; i++) {
                ReportDataBean bean = new ReportDataBean();
                bean.setCount(0);
                first.add(bean);
                second.add(bean);
            }
            if(fiveMonthTasks.getDate().get(0).size() != 0){
                for (int i = 0; i < fiveMonthTasks.getDate().get(0).size(); i++) {
                    ReportDataBean bean = fiveMonthTasks.getDate().get(0).get(i);

                    int temp = bean.getCount();
                    if(temp > maxCount){
                        maxCount = temp;
                    }

                    int day = replaceTime(bean.getTime()) - 1;
                    first.remove(day);
                    first.add(day,bean);
                }
            }

            if(fiveMonthTasks.getDate().get(1).size() != 0){
                for (int i = 0; i < fiveMonthTasks.getDate().get(1).size(); i++) {
                    ReportDataBean bean = fiveMonthTasks.getDate().get(1).get(i);

                    int temp = bean.getCount();
                    if(temp > maxCount){
                        maxCount = temp;
                    }

                    int day = replaceTime(bean.getTime()) - 1;
                    second.remove(day);
                    second.add(day,bean);
                }
            }


            //X轴
            XAxis xAxis = lineChart3.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(true);
            xAxis.setGridColor(Color.parseColor("#ededed"));
            xAxis.setAxisLineColor(Color.parseColor("#dddddd"));

            //Y轴
            YAxis yLeftAxis = lineChart3.getAxisLeft();
            YAxis yRrightAxis = lineChart3.getAxisRight();
            yLeftAxis.setAxisLineColor(Color.parseColor("#dddddd"));
            yRrightAxis.setAxisLineColor(Color.TRANSPARENT);
            yLeftAxis.setAxisMaximum(maxCount > 5 ? maxCount : 5);
            yLeftAxis.setAxisMinimum(0);
            yRrightAxis.setAxisMaximum(maxCount > 5 ? maxCount : 5);
            yRrightAxis.setAxisMinimum(0);
            yRrightAxis.setDrawLabels(false);
            yLeftAxis.setLabelCount(5);
            yLeftAxis.setGridColor(Color.parseColor("#ededed"));
            yRrightAxis.setGridColor(Color.parseColor("#ededed"));

            //保证Y轴从0开始，不然会上移一点
            yLeftAxis.setAxisMinimum(0f);
            yRrightAxis.setAxisMinimum(0f);

            //设置x轴的数据
            ArrayList<Float> xValues = new ArrayList<>();
            ArrayList<String> xVaulesName = new ArrayList<>();
            for (int i = 1; i <= month; i++) {
                xVaulesName.add((i < 10 ? "0" : "") + i);
                xValues.add((float) i);
            }
            if(month % 2 == 0){
                xVaulesName.add(0,"0");
                xValues.add(0,0f);
            }
            xAxis.setLabelCount(xValues.size(),false);
            MyXFormatter xFormatter = new MyXFormatter(xVaulesName);
            xAxis.setValueFormatter(xFormatter);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            //设置y轴的数据()
            List<Entry> yValues1 = new ArrayList<>();
            List<Entry> yValues2 = new ArrayList<>();
            boolean drawY1 = false;
            boolean drawY2 = false;
            if(month % 2 == 0){
                for (int i = 0;i < month; i++){
                    int firstMin = first.get(i).getCount();
                    int secondMin = second.get(i).getCount();
                    if(firstMin != 0){
                        drawY1 = true;
                    }
                    if(secondMin != 0){
                        drawY2 = true;
                    }
                    yValues1.add(new Entry(i + 1,firstMin));
                    yValues2.add(new Entry(i + 1,secondMin));
                }
                yValues1.add(0,new Entry(0,0));
                yValues2.add(0,new Entry(0,0));
            } else {
                for (int i = 0;i < month; i++){
                    int firstMin = first.get(i).getCount();
                    int secondMin = second.get(i).getCount();
                    if(firstMin != 0){
                        drawY1 = true;
                    }
                    if(secondMin != 0){
                        drawY2 = true;
                    }
                    yValues1.add(new Entry(i,firstMin));
                    yValues2.add(new Entry(i,secondMin));
                }
            }

            LineDataSet set1 = null;
            LineDataSet set2 = null;
            if (lineChart3.getData() != null &&
                    lineChart3.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) lineChart3.getData().getDataSetByIndex(0);
                set2 = (LineDataSet) lineChart3.getData().getDataSetByIndex(1);

                set1.setValues(yValues1);
                set2.setValues(yValues2);
                set1.setVisible(drawY1);
                set2.setVisible(drawY2);
                lineChart3.getData().notifyDataChanged();
                lineChart3.notifyDataSetChanged();
            } else {

                // create a dataset and give it a type
                set1 = new LineDataSet(yValues1,"DataSet");
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                //orange
                set1.setColor(Color.parseColor("#ff9e5d"));
                set1.setDrawCircles(true);
                set1.setLineWidth(2f);
                set1.setCircleRadius(4f);
                set1.setCircleColor(Color.parseColor("#ff9e5d"));
                set1.setFillAlpha(255);
                set1.setDrawFilled(false);
                set1.setVisible(drawY1);

                set1.setFillColor(Color.parseColor("#c6c6c6"));
                set1.setMode(LineDataSet.Mode.LINEAR);
                set1.setDrawCircleHole(true);
                set1.setCircleHoleRadius(2f);
                set1.setCircleColorHole(Color.WHITE);
//            set.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return lineChart.getAxisLeft().getAxisMaximum();
//                }
//            });

                // create a dataset and give it a type
                set2 = new LineDataSet(yValues2, "DataSet");
                set2.setAxisDependency(YAxis.AxisDependency.LEFT);
                set2.setColor(Color.parseColor("#61a4ff"));
                set2.setDrawCircles(true);
                set2.setCircleColor(Color.parseColor("#61a4ff"));
                set2.setLineWidth(2f);
                set2.setCircleRadius(4f);
                set2.setFillAlpha(255);
                set2.setDrawFilled(false);
                set2.setFillColor(Color.parseColor("#3464dd"));
                set2.setMode(LineDataSet.Mode.LINEAR);
                set2.setDrawCircleHole(true);
                set2.setCircleHoleRadius(2f);
                set2.setCircleColorHole(Color.WHITE);
                set2.setVisible(drawY2);

                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1); // add the datasets
                dataSets.add(set2);

                // create a data object with the datasets
                LineData data = new LineData(dataSets);
                data.setDrawValues(false);

                // set data
                lineChart3.setData(data);
            }

            xAxis.setAxisMaximum(xValues.size());
            xAxis.setAxisMinimum(0);
            xAxis.setLabelCount(xValues.size() /2 ,false);
            lineChart3.invalidate();
        } else {
            lineChart3.setData(null);
            lineChart3.invalidate();
        }


    }

    private String parseTimeOnlyYearAndMonth(String date){
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        return year + "年" + month + "月";
    }

    private int currentSure = 0;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.tv_company_rank_date:
                currentUpdateChart = UPDATE_FIRST_CHART;
                isFromFirst = true;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
                customDatePicker.show(now);
                break;
            case R.id.tv_person_rank:
                currentUpdateChart = UPDATE_SECOND_CHART;

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now2 = sdf2.format(new Date());
                customDatePicker.show(now2);
                break;
            case R.id.tv_company_total:
                currentUpdateChart = UPDATE_THIRD_CHART;
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now3 = sdf3.format(new Date());
                customDatePicker.show(now3);
                break;
            case R.id.sure:
                if(currentSure == 4){
                    currentUpdateChart = UPDATE_FOUR_CHART;
                    fourCheckPopwindow.dismiss();
                } else if(currentSure == 5){
                    currentUpdateChart = UPDATE_FIVE_CHART;
                    fiveCheckPopwindow.dismiss();
                }

                break;
            case R.id.tv_company_total_compare:
                currentUpdateChart = UPDATE_FOUR_CHART;
                SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now4 = sdf4.format(new Date());
                customDatePicker.show(now4);
                break;
            case R.id.tv_company_month_report:
                currentUpdateChart = UPDATE_FIVE_CHART;
                SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now5 = sdf5.format(new Date());
                customDatePicker.show(now5);
                break;
            case R.id.tv_company_total_compare_choose:
                currentSure = 4;
                fourCheckPopwindow.showAtLocation(findViewById(R.id.scrollview), Gravity.BOTTOM,0,0);
                break;
            case R.id.tv_company_month_report_choose:
                currentSure = 5;
                fiveCheckPopwindow.showAtLocation(findViewById(R.id.scrollview), Gravity.BOTTOM,0,0);
                break;
        }
    }

    private int replaceTime(String time){
        time = time.substring(time.length() - 2,time.length());
        return Integer.parseInt(time);
    }

    private int getMonthDays(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void initFourChartPopWindow(){
        LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_summary_check_box,null);
        TextView tv_sure = (TextView) view.findViewById(R.id.sure);
        tv_sure.setOnClickListener(this);
        ListView fourListView = (ListView) view.findViewById(R.id.lv);
        fourCheckboxAdapter = new MyFourCheckboxAdapter(4);
        fourListView.setAdapter(fourCheckboxAdapter);

        fourCheckPopwindow = new PopupWindow(this);
        fourCheckPopwindow.setWidth(this.getWindowManager().getDefaultDisplay().getWidth());
        fourCheckPopwindow.setHeight(this.getWindowManager().getDefaultDisplay().getHeight()/ 2);
        fourCheckPopwindow.setContentView(view);
        fourCheckPopwindow.setOutsideTouchable(false);
        fourCheckPopwindow.setFocusable(true);
        fourCheckPopwindow.setTouchable(true);
        fourCheckPopwindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        fourCheckPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                updateChart(selectFourDate);
            }
        });


    }

    private void initFiveChartPopWindow(){
        LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_summary_check_box,null);
        TextView tv_sure = (TextView) view.findViewById(R.id.sure);
        tv_sure.setOnClickListener(this);
        ListView fiveListView = (ListView) view.findViewById(R.id.lv);
        myFiveCheckboxAdapter = new MyFourCheckboxAdapter(5);
        fiveListView.setAdapter(myFiveCheckboxAdapter);

        fiveCheckPopwindow = new PopupWindow(this);
        fiveCheckPopwindow.setWidth(this.getWindowManager().getDefaultDisplay().getWidth());
        fiveCheckPopwindow.setHeight(this.getWindowManager().getDefaultDisplay().getHeight()/ 2);
        fiveCheckPopwindow.setContentView(view);
        fiveCheckPopwindow.setOutsideTouchable(false);
        fiveCheckPopwindow.setFocusable(true);
        fiveCheckPopwindow.setTouchable(true);
        fiveCheckPopwindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        fiveCheckPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                updateChart(selectFiveDate);
            }
        });
    }

    private int oldFourClickedId1 = -1;
    private int oldFourClickedId2 = -1;
    private int oldFiveClickedId1 = -1;
    private int oldFiveClickedId2 = -1;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String checkStr = (String) buttonView.getTag();
        if(checkStr.startsWith("Four")){
            String index = checkStr.substring(4,checkStr.length());
            int checkId = Integer.parseInt(index);

            /**
             * by zw
             *
             * 1.预制两个点击的ID，0、1
             * 2.如果选择新的，就把1传给0，然后把新的传给1  保证被点击的按钮永远都是最新的
             * 3.取消选择时，保证剩余的要么都是-1，要么剩下的一个是旧的
             */
            if(isChecked){
                if(oldFourClickedId1 != -1 && oldFourClickedId2 != -1){
                    oldFourClickedId1 = oldFourClickedId2;
                    oldFourClickedId2 = checkId;
                } else if(oldFourClickedId1 != -1){
                    oldFourClickedId2 = checkId;
                } else {
                    oldFourClickedId1 = checkId;
                }

            }else {
                if(checkId == oldFourClickedId1){
                    oldFourClickedId1 = oldFourClickedId2;
                    oldFourClickedId2 = -1;
                } else if(checkId == oldFourClickedId2){
                    oldFourClickedId2 = -1;
                }

            }
            if(oldFourClickedId1 != -1 && reportDataBeans != null){
                currentCompanyTotalCompareIdString1 = personRankIdList.get(oldFourClickedId1) + "";
            } else {
                currentCompanyTotalCompareIdString1 = "";
            }
            if(oldFourClickedId2 != -1 && reportDataBeans != null){
                currentCompanyTotalCompareIdString2 = personRankIdList.get(oldFourClickedId2) + "";
            } else {
                currentCompanyTotalCompareIdString2 = "";
            }
            fourCheckboxAdapter.notifyDataSetChanged();
        } else if(checkStr.startsWith("Five")){
            String index = checkStr.substring(4,checkStr.length());
            int checkId = Integer.parseInt(index);

            if(isChecked){
                if(oldFiveClickedId1 != -1 && oldFiveClickedId2 != -1){
                    oldFiveClickedId1 = oldFiveClickedId2;
                    oldFiveClickedId2 = checkId;
                } else if(oldFiveClickedId1 != -1){
                    oldFiveClickedId2 = checkId;
                } else {
                    oldFiveClickedId1 = checkId;
                }

            }else {
                if(checkId == oldFiveClickedId1){
                    oldFiveClickedId1 = oldFiveClickedId2;
                    oldFiveClickedId2 = -1;
                } else if(checkId == oldFiveClickedId2){
                    oldFiveClickedId2 = -1;
                }

            }
            if(oldFiveClickedId1 != -1 && reportDataBeans != null){
                currentCompanyMonthIdString1 = personRankIdList.get(oldFiveClickedId1) + "";
            } else {
                currentCompanyMonthIdString1 = "";
            }
            if(oldFiveClickedId2 != -1 && reportDataBeans != null){
                currentCompanyMonthIdString2 = personRankIdList.get(oldFiveClickedId2) + "";
            } else {
                currentCompanyMonthIdString2 = "";
            }
            myFiveCheckboxAdapter.notifyDataSetChanged();
        }

    }

    private class MyFourCheckboxAdapter extends BaseAdapter {

        private int type = 0;

        public MyFourCheckboxAdapter(int type){
            this.type = type;
        }

        @Override
        public int getCount() {
            return personRankList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(ConstructionSummaryActivity.this).inflate(R.layout.layout_main_checkbox_item,parent,false);
            }

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
            if(type == 4){
                checkBox.setTag("Four" + position);
                if(oldFourClickedId1 == position || oldFourClickedId2 == position){
                    checkBox.setChecked(true);
                }else {
                    checkBox.setChecked(false);
                }
            } else if(type == 5){
                checkBox.setTag("Five" + position);
                if(oldFiveClickedId1 == position || oldFiveClickedId2 == position){
                    checkBox.setChecked(true);
                }else {
                    checkBox.setChecked(false);
                }
            }


            checkBox.setOnCheckedChangeListener(ConstructionSummaryActivity.this);
            TextView textView = (TextView) convertView.findViewById(R.id.tv);
            textView.setText(personRankList.get(position));

            return convertView;
        }
    }
}
