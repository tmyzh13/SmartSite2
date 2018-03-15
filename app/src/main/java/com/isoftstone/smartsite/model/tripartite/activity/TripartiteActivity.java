package com.isoftstone.smartsite.model.tripartite.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.model.tripartite.data.ReportData;
import com.isoftstone.smartsite.model.tripartite.fragment.CheckReportMainFragment;
import com.isoftstone.smartsite.model.tripartite.fragment.InspectReportMainFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yanyongjun on 2017/10/16.
 * 三方协同的主页面
 */

public class TripartiteActivity extends BaseActivity {
    ArrayList<Fragment> mFragList = new ArrayList<Fragment>();
    private SparseArray<TextView> mSwitchLab = new SparseArray<>();
    private SparseArray<ImageView> mSwitchImg = new SparseArray<>();

    public static final int FRAGMENT_TYPE_INSPECT_REPORT = 0;
    public static final int FRAGMENT_TYPE_CHECK_REPORT = 1;

    public static final String FRAGMENT_TYPE = "type";
    public static final String FRAGMENT_DATA = "data";

    //View in this activity
    private ViewPager mViewPager = null;
    private View mDefaultBar = null; //the default bar at the title
    private View mSearchBar = null; // the search bar show if click the search button in default bar
    private HttpPost mHttpPost = null;
    private FragmentPagerAdapter mPagerAdapter = null;
    public boolean mIsUIInSearchMode = false;
    public boolean mIsDataInSearchMode = false;


    private ArrayList<ReportData> mDatas = new ArrayList<>();

    public static final int[] STATUS_IMG = new int[]{R.drawable.pending, R.drawable.pending,
            R.drawable.waitvisiting, R.drawable.sendback, R.drawable.pass};
    public static final HashMap<String, Integer> mAttach = new HashMap<>();
    public static final ArrayList<String> mDocList = new ArrayList<>();
    public static final ArrayList<String> mPdfList = new ArrayList<>();
    public static final ArrayList<String> mPptList = new ArrayList<>();
    public static final ArrayList<String> mVideoList = new ArrayList<>();
    public static final ArrayList<String> mXlsList = new ArrayList<>();
    public static final ArrayList<String> mImageList = new ArrayList<>();

    static {
        mAttach.put(".doc", R.drawable.doc);
        mAttach.put(".pdf", R.drawable.pdf);
        mAttach.put(".ppt", R.drawable.ppt);
        mAttach.put(".video", R.drawable.video);
        mAttach.put(".xls", R.drawable.xls);
        mAttach.put(".image", R.drawable.pic);

        mDocList.add(".doc");
        mDocList.add(".docx");

        mPdfList.add(".pdf");

        mPptList.add(".ppt");
        mPptList.add(".pptx");

        mVideoList.add(".mpeg");
        mVideoList.add(".mpg");
        mVideoList.add(".dat");
        mVideoList.add(".avi");
        mVideoList.add(".mov");
        mVideoList.add(".asf");
        mVideoList.add(".wmv");
        mVideoList.add(".navi");
        mVideoList.add(".3gp");
        mVideoList.add(".ra");
        mVideoList.add(".ram");
        mVideoList.add(".mkv");
        mVideoList.add(".flv");
        mVideoList.add(".f4v");
        mVideoList.add(".rmvb");
        mVideoList.add(".webm");
        mVideoList.add(".hddvd");
        mVideoList.add(".qsv");
        mVideoList.add(".mp4");
        mVideoList.add(".mov");
        mVideoList.add(".asf");
        mVideoList.add(".rm");
        mVideoList.add(".vob");
        mVideoList.add(".mp3");

        mXlsList.add(".txt");
        mXlsList.add(".csv");
        mXlsList.add(".xls");
        mXlsList.add(".xlsx");

        mImageList.add(".bmp");
        mImageList.add(".pcx");
        mImageList.add(".gif");
        mImageList.add(".tiff");
        mImageList.add(".jpeg");
        mImageList.add(".jpg");
        mImageList.add(".png");
        mImageList.add(".tga");
        mImageList.add(".exif");
        mImageList.add(".fpx");
        mImageList.add(".svg");
        mImageList.add(".psd");
        mImageList.add(".cdr");
        mImageList.add(".pcd");
        mImageList.add(".cdr");
        mImageList.add(".dxf");
        mImageList.add(".ufo");
        mImageList.add(".eps");
        mImageList.add(".ai");
        mImageList.add(".hdpi");
        mImageList.add(".raw");
        mImageList.add(".wmf");
        mImageList.add(".lic");
        mImageList.add(".fli");
        mImageList.add(".emf");
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_tripartite;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        init();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.report_view_pager);
        mDefaultBar = findViewById(R.id.toolbar_default);
        mSearchBar = findViewById(R.id.toolbar_search);

        //init the default view state
        mDefaultBar.setVisibility(View.VISIBLE);
        mSearchBar.setVisibility(View.GONE);
        initSearchMode();

    }

    private void initSearchMode() {


        final TextView cancel_search = (TextView) findViewById(R.id.search_btn_icon_right);
        final EditText search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        View btn_search = findViewById(R.id.btn_search);
        final View search_btn_back = findViewById(R.id.search_btn_back);

        if (mIsUIInSearchMode) {
            enterSearchMode();
        } else {
            exitSearchMode();
        }

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterSearchMode();
            }
        });

        cancel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(search_edit_text.getText().toString())) {
                    exitSearchMode();
                } else {
                    for (Fragment temp : mFragList) {
                        if (temp instanceof CheckReportMainFragment) {
                            ((CheckReportMainFragment) temp).queryData(true, search_edit_text.getText().toString());
                        } else if (temp instanceof InspectReportMainFragment) {
                            ((InspectReportMainFragment) temp).queryData(true, search_edit_text.getText().toString());
                        }
                    }
                }
            }
        });


//        cancel_search.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                if (TextUtils.isEmpty(search_edit_text.text.toString())) {
//                    exitSearchMode()
//                } else {
//                    queryData(search_edit_text.text.toString(),true)
//                }
//            }
//        })

        search_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitSearchMode();
            }
        });

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    cancel_search.setText("取消");
                } else {
                    cancel_search.setText("搜索");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void enterSearchMode() {
        View toolbar_search = findViewById(R.id.toolbar_search);
        View toolbar_default = findViewById(R.id.toolbar_default);
        TextView cancel_search = (TextView) findViewById(R.id.search_btn_icon_right);
        EditText search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        View btn_search = findViewById(R.id.btn_search);
        View search_btn_back = findViewById(R.id.search_btn_back);
        toolbar_default.setVisibility(View.INVISIBLE);
        toolbar_search.setVisibility(View.VISIBLE);
        mIsUIInSearchMode = true;
        search_edit_text.setText("");
        cancel_search.setText("取消");
    }

    private void exitSearchMode() {
        View toolbar_search = findViewById(R.id.toolbar_search);
        View toolbar_default = findViewById(R.id.toolbar_default);
        TextView cancel_search = (TextView) findViewById(R.id.search_btn_icon_right);
        EditText search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        View btn_search = findViewById(R.id.btn_search);
        View search_btn_back = findViewById(R.id.search_btn_back);
        search_edit_text.setText("");
        toolbar_default.setVisibility(View.VISIBLE);
        toolbar_search.setVisibility(View.INVISIBLE);
        mIsUIInSearchMode = false;
        if (mIsDataInSearchMode) {
            for (Fragment temp : mFragList) {
                if (temp instanceof CheckReportMainFragment) {
                    ((CheckReportMainFragment) temp).queryData(true, search_edit_text.getText().toString());
                } else if (temp instanceof InspectReportMainFragment) {
                    ((InspectReportMainFragment) temp).queryData(true, search_edit_text.getText().toString());
                }
            }
        }
    }

    /**
     * 当没有巡查报告
     */
    private void hideFrag() {
        try {
            if (mHttpPost.mLoginBean.getmUserBean().getmPermission().isM_PATROL_ACCEPT() && mHttpPost.mLoginBean.getmUserBean().getmPermission().isM_PATROL_REPORT()) {
                return;
            }
            LinearLayout linearSwitch = (LinearLayout) findViewById(R.id.linear_switch);
            LinearLayout linearSwtichImg = (LinearLayout) findViewById(R.id.linear_switch_img);
            if (!mHttpPost.mLoginBean.getmUserBean().getmPermission().isM_PATROL_ACCEPT()) {
                linearSwitch.setVisibility(View.GONE);
                linearSwtichImg.setVisibility(View.GONE);
                mFragList.remove(1);
                mPagerAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(0);
            }
            if (!mHttpPost.mLoginBean.getmUserBean().getmPermission().isM_PATROL_REPORT()) {
                linearSwitch.setVisibility(View.GONE);
                linearSwtichImg.setVisibility(View.GONE);
                mFragList.remove(0);
                mPagerAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(0);

                //hide the add report button
                View v = findViewById(R.id.btn_add);
                v.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        mHttpPost = new HttpPost();
        initView();
        Fragment inspectFrag = new InspectReportMainFragment();
        Fragment checkFrag = new CheckReportMainFragment();

        mFragList.add(inspectFrag);
        mFragList.add(checkFrag);

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragList.get(position);
            }

            @Override
            public int getCount() {
                return mFragList.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //TODO
            }

            @Override
            public void onPageSelected(int position) {
                chooseFrag(position);
                initTitleOnClickListener(position);
            }


            @Override
            public void onPageScrollStateChanged(int state) {
                //TODO
            }
        });

        TextView v1 = (TextView) findViewById(R.id.lab_inspect);
        TextView v2 = (TextView) findViewById(R.id.lab_check);


        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFrag(0);
                mViewPager.setCurrentItem(0);
            }
        });
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFrag(1);
                mViewPager.setCurrentItem(1);
            }
        });

        mSwitchLab.put(0, v1);
        mSwitchLab.put(1, v2);
        ImageView img1 = (ImageView) findViewById(R.id.img_inspect);
        mSwitchImg.put(0, img1);
        ImageView img2 = (ImageView) findViewById(R.id.img_check);
        mSwitchImg.put(1, img2);

        chooseFrag(0);
        initTitleOnClickListener(0);
        mViewPager.setCurrentItem(0);

        hideFrag();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //new QueryMsgTask().execute();
    }

    public ArrayList<ReportData> getDatas() {
        return mDatas;
    }

    private void chooseFrag(int position) {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.shape_threeparty_lab);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        for (int i = 0; i < mSwitchLab.size(); i++) {
            TextView v = mSwitchLab.get(i);
            if (i == position) {
                v.setTextColor(res.getColor(R.color.mainColor));
                mSwitchImg.get(i).setVisibility(View.VISIBLE);
            } else {
                v.setTextColor(res.getColor(R.color.main_text_color));
                mSwitchImg.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    private void initTitleOnClickListener(int position) {
        switch (position) {
            case FRAGMENT_TYPE_INSPECT_REPORT:
                break;
            case FRAGMENT_TYPE_CHECK_REPORT:
                break;
            default:
                break;
        }
    }

    /**
     * 点击返回键之后的操作
     *
     * @param v
     */
    public void onBackBtnClick(View v) {
        finish();
    }

    /**
     * 点击新增报告界面按钮
     *
     * @param v
     */
    public void onAddReportBtnClick(View v) {
        Intent intent = new Intent(this, AddReportActivity.class);
        startActivity(intent);
    }

    /**
     * 点击搜索按钮
     *
     * @param v
     */
    public void onSearchBtnClick(View v) {
        mSearchBar.setVisibility(View.VISIBLE);
        mDefaultBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 当位于搜索状态时，点击toolbar上面的取消按钮
     *
     * @param v
     */
    public void onCancelSearchBtnClick(View v) {
        mSearchBar.setVisibility(View.GONE);
        mDefaultBar.setVisibility(View.VISIBLE);
    }
}
