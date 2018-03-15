package com.isoftstone.smartsite.model.tripartite.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanyongjun on 2017/11/5.
 */

public class ReadVisitReportFrag extends BaseFragment {

    private List<Uri> attach = new ArrayList<>();
    private GridView mAttachView = null;
    private SimpleAdapter mAttachAdapter = null;
    private ArrayList<Map<String, Object>> mData = null;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_read_visit_report;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        initGridView();
    }

    public void initGridView() {
        mAttachView = (GridView) getView().findViewById(R.id.grid_view_source_report_temp);
        if(mAttachView == null){

        }

        mData = new ArrayList<Map<String, Object>>();
        Map<String, Object> data = new HashMap<>();
        data.put("image", R.drawable.attachment);
        mData.add(data);
        mAttachAdapter = new SimpleAdapter(getActivity(), mData, R.layout.add_attach_grid_item, new String[]{"image"}, new int[]{R.id.image});
        if (mAttachView != null) {
            mAttachView.setAdapter(mAttachAdapter);

            mAttachView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                if (position == mData.size() - 1) {
                    //点击添加附件
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("image*//*");
                    startActivityForResult(i, REQUEST_ACTIVITY_ATTACH);
                }*/
                }
            });
        }
    }
}
