package com.isoftstone.smartsite.model.system.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseFragment;


/**
 * Created by zyf on 2017/10/13 21:00.
 */

public class SystemMainFragment extends BaseFragment{

    private LinearLayout linearLayout;//用户头像父节点LL
    private ImageView imageView;//用户头像IV
    String picPath;//头像路径
    private LinearLayout individualCenterLinearLayout;//个人中心Btn
    private LinearLayout aboutUsLinearLayout;//关于我们Btn
    private LinearLayout logOffLinearLayout;//退出Btn
    private PermissionsChecker mPermissionsChecker;

    public static final String TAG_SYSTEM_MAIN_FRAME = "TAG_SYSTEM_MAIN_FRAME";
    public static final String TAG_INDIVIDUAL_CNTER_FRAME = "TAG_INDIVIDUAL_CNTER_FRAME";

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_main_system;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        Fragment systemFragment = new SystemFragment();
        getChildFragmentManager().beginTransaction().add(R.id.fl_system_content, systemFragment, TAG_SYSTEM_MAIN_FRAME).commitAllowingStateLoss();
    }

}
