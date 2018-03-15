package com.isoftstone.smartsite.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by 2013020220 on 2017/11/29.
 */

public class MyPopuWindow extends PopupWindow {
    private Context context;
    private String title;
    private ArrayList<String> data;
    private View rootView;
    private TextView tv_title;
    private ListView lv;
    private TextView tv_ok;
    private OnDataCheckedListener onDataCheckedListener;
    private ArrayList<Integer> choice_data = new ArrayList<Integer>();
    private boolean mChecked;

    public MyPopuWindow(Context context, String title, ArrayList<String> data, OnDataCheckedListener onDataCheckedListener) {
        super(context);
        this.context = context;
        this.title = title;
        this.data = data;
        this.onDataCheckedListener = onDataCheckedListener;
        initView();
    }


    private void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_main_mypopuwindow, null);
        tv_title = (TextView) rootView.findViewById(R.id.title);
        lv = (ListView) rootView.findViewById(R.id.lv);
        tv_ok = (TextView) rootView.findViewById(R.id.tv_ok);
        this.setContentView(rootView);
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.update();
        tv_title.setText(title);
        tv_ok.setOnClickListener(clickListener);
        lv.setAdapter(new MyBaseAdapter(context, data));

    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (choice_data.size() == 2) {
                int first = choice_data.get(0);
                int second = choice_data.get(1);
                if (first > second) {
                    onDataCheckedListener.onDataCheck(data.get(second), data.get(first), second, first);
                } else {
                    onDataCheckedListener.onDataCheck(data.get(first), data.get(second), first, second);
                }
            } else {
                ToastUtils.showShort("只能选择两个数据哦");
                return;
            }
            choice_data.clear();
            dismiss();
        }
    };

    class MyBaseAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> data;

        public MyBaseAdapter(Context context, ArrayList<String> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int user_choice = position;
            MyHolderView myHolderView;
            if (convertView == null) {
                myHolderView = new MyHolderView();
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_mypopuwindow_item_detail, null);
                myHolderView.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                myHolderView.textView = (TextView) convertView.findViewById(R.id.tv);
                convertView.setTag(myHolderView);
            } else {
                myHolderView = (MyHolderView) convertView.getTag();
            }
            myHolderView.textView.setText(data.get(position));
            myHolderView.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!choice_data.contains(user_choice)) {
                            choice_data.add((Integer) user_choice);
                        }
                    } else {
                        if (choice_data.contains(user_choice)) {
                            choice_data.remove((Integer) user_choice);
                        }
                    }
                    if (choice_data.size()>2) {
                        choice_data.remove(0);
                    }
                    notifyDataSetChanged();
                }
            });
            if (choice_data.contains(position)) {
                myHolderView.checkBox.setChecked(true);
            } else {
                myHolderView.checkBox.setChecked(false);
            }
            myHolderView.checkBox.setTag(position);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return data == null ? null : data.get(position);
        }
    }

    static class MyHolderView {
        public CheckBox checkBox;
        public TextView textView;
    }

    public interface OnDataCheckedListener {
        void onDataCheck(String left, String right, int first_choice, int second_choice);
    }
}
