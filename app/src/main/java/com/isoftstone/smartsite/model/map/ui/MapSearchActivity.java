package com.isoftstone.smartsite.model.map.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseActivity;

/**
 * Created by zw on 2017/10/30.
 */

public class MapSearchActivity extends BaseActivity implements View.OnClickListener {


    private EditText editText;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_map_search;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        findViewById(R.id.search_btn_back).setOnClickListener(this);
        findViewById(R.id.search_btn_icon_right).setOnClickListener(this);
        editText = (EditText) findViewById(R.id.search_edit_text);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_btn_back:
                this.finish();
                break;
            case R.id.search_btn_icon_right:
                editText.setText("");
                break;

        }
    }
}
