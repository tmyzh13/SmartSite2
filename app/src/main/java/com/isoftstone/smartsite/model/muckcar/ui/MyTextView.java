package com.isoftstone.smartsite.model.muckcar.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.utils.DensityUtils;
import com.isoftstone.smartsite.utils.SharedPreferencesUtils;
import com.isoftstone.smartsite.utils.ToastUtils;

/**
 * Created by 2013020220 on 2017/11/26.
 */

public class MyTextView extends LinearLayout {
    private Context context;
    private String load_name;
    private int black_num;
    private int white_num;
    private final View view;
    private TextView tv_load_name;
    private TextView tv_white_list;
    private TextView tv_black_list;

    public MyTextView(Context context, String load_name, int black_num, int white_num) {
        super(context);
        this.context = context;
        this.load_name = load_name;
        this.white_num = white_num;
        this.black_num = black_num;
        view = View.inflate(context, R.layout.white_black_list_num, this);
        initView();
        initData();
    }


    private void initData() {
        int total = getTotalWidth() - 90;
        float base_width = SharedPreferencesUtils.getBaseWidth(context) + 0.000f;
        int total_num = white_num + black_num;
        if (base_width == 0.000f && total_num != 0) {
            base_width = 10*total / total_num;
            SharedPreferencesUtils.saveBaseWidth(context, base_width);
        }
        int black_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (base_width * black_num)/10 + 10, getResources().getDisplayMetrics());
        int white_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (base_width * white_num)/10 + 10, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams white_layoutParams = new LinearLayout.LayoutParams(white_width, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams black_layoutParams = new LinearLayout.LayoutParams(black_width, LayoutParams.WRAP_CONTENT);
        tv_black_list.setLayoutParams(black_layoutParams);
        tv_white_list.setLayoutParams(white_layoutParams);
    }

    private void initView() {
        tv_load_name = (TextView) view.findViewById(R.id.load_name);
        tv_load_name.setText(load_name);
        tv_white_list = (TextView) view.findViewById(R.id.white_list);
        tv_black_list = (TextView) view.findViewById(R.id.balck_list);
        tv_white_list.setText(white_num + "");
        tv_black_list.setText(black_num + "");

    }

    private int getTotalWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int i = wm.getDefaultDisplay().getWidth();
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (i / scale + 0.5f);
    }

}
