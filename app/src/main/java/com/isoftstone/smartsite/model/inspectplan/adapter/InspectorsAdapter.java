package com.isoftstone.smartsite.model.inspectplan.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.user.BaseUserBean;
import com.isoftstone.smartsite.model.inspectplan.data.InspectorData;
import com.isoftstone.smartsite.utils.ImageUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-24.
 */

public class InspectorsAdapter extends BaseAdapter {

    private ArrayList<InspectorData> list = null;
    private Context mContext;
    LinearLayout linearLayout_inspector_icon;
    private HttpPost mHttpPost;

    public InspectorsAdapter() {
        super();
    }

    public InspectorsAdapter(Context mContext, ArrayList<InspectorData> list) {
        this.mContext = mContext;
        this.list = list;
        Log.i("ContactAdapter","list length is:" + list.size());
    }

    public InspectorsAdapter(Context mContext, ArrayList<InspectorData> list, LinearLayout linearLayout_inspector_icon) {
        this.mContext = mContext;
        this.list = list;
        this.linearLayout_inspector_icon = linearLayout_inspector_icon;
        Log.i("ContactAdapter","list length is:" + list.size());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public InspectorData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mHttpPost = new HttpPost();
        ViewHolder holder = null;
        final InspectorData contactDate = getItem(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.inspector_item, parent, false);
            holder = new ViewHolder();
            holder.textView_Sort = (TextView)convertView.findViewById(R.id.textView_sort);
            holder.textView_ContactName = (TextView)convertView.findViewById(R.id.textView_contactName);
            holder.imageView_ContactIcon = (ImageView)convertView.findViewById(R.id.imageView_contactIcon);
            holder.checkBox_ContactIsCheck = (CheckBox)convertView.findViewById(R.id.checkBox_contactIsCheck);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.textView_Sort.setText(contactDate.getSort());
        holder.textView_ContactName.setText(contactDate.getName());
        holder.textView_Sort.setVisibility(contactDate.getIsVisible());
//        holder.imageView_ContactIcon

        holder.checkBox_ContactIsCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,"点击了多选框",Toast.LENGTH_SHORT).show();
                if(linearLayout_inspector_icon != null) {
                    linearLayout_inspector_icon.removeAllViews();
                }
                for (int i = 0; i < list.size(); i ++)
                {
                    View inflate = View.inflate(mContext, R.layout.inspector_icon_item, null);
                    ImageView inspector_icon = (ImageView) inflate.findViewById(R.id.imageView_icon);
                    if(list.get(i).getImageData() != null) {
                        ImageUtils.loadImageWithPlaceHolder(mContext, inspector_icon, mHttpPost.getFileUrl(list.get(i).getImageData()));
                    } else {
                        inspector_icon.setImageResource(R.drawable.default_head);
                    }
                    if ( list.get(i).getIsSelected() ) {
                        linearLayout_inspector_icon.addView(inflate);
                    }
                }

            }
        });

        holder.checkBox_ContactIsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                contactDate.setIsSelected(isChecked);
            }
        });
        holder.checkBox_ContactIsCheck.setChecked(contactDate.getIsSelected());
        if(contactDate.getImageData() != null) {
            ImageUtils.loadImageWithPlaceHolder(mContext, holder.imageView_ContactIcon, mHttpPost.getFileUrl(contactDate.getImageData()));
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView textView_Sort;
        public TextView textView_ContactName;
        public ImageView imageView_ContactIcon;
        public CheckBox checkBox_ContactIsCheck;
    }
}
