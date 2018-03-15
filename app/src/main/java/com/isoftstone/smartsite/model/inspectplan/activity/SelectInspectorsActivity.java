package com.isoftstone.smartsite.model.inspectplan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gjiazhe.wavesidebar.WaveSideBar;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.user.BaseUserBean;
import com.isoftstone.smartsite.model.inspectplan.adapter.InspectorsAdapter;
import com.isoftstone.smartsite.model.inspectplan.adapter.InspectorsIconAdapter;
import com.isoftstone.smartsite.model.inspectplan.data.InspectorData;
import com.isoftstone.smartsite.utils.ImageUtils;
import com.isoftstone.smartsite.utils.ZhongWen2PinYinUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-24.
 */

public class SelectInspectorsActivity extends Activity{
    private ArrayList<InspectorData> list = null;
    private InspectorData inspectorDate;
    private ListView listView_Contact;
    private HorizontalScrollView listView_Icon;
    public InspectorsAdapter inspectorsAdapter;
    public InspectorsIconAdapter iconAdapter;
    private LinearLayout linearLayout_inspector_icon;
    private HttpPost mHttpPost;
    private ImageButton btnBack;
    private Button btnEnsure;
    private ArrayList<BaseUserBean> userList;
    private ArrayList<BaseUserBean> selectedInspectorsList = null;
    private BaseUserBean selectedInspector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_inspectors);
        listView_Contact = (ListView) findViewById(R.id.listView_Contact) ;
        listView_Icon = (HorizontalScrollView) findViewById(R.id.listView_Icon) ;
        linearLayout_inspector_icon = (LinearLayout) findViewById(R.id.linear_inspector_icon);
        btnBack = (ImageButton) findViewById(R.id.btn_back) ;
        btnEnsure = (Button)findViewById(R.id.btn_inspectors) ;
        btnBack.setOnClickListener(mGoBack);
        btnEnsure.setOnClickListener(mEnsure);
        list = new ArrayList<InspectorData>();

        initDate();//本地填充巡查人员数据
    }

    public View.OnClickListener mGoBack = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener mEnsure = new View.OnClickListener() {
        public void onClick(View v) {

            selectedInspectorsList = new ArrayList<BaseUserBean>();
            for (int i=0; i < list.size(); i++){
                if (list.get(i).isSelected) {
                    selectedInspector = new BaseUserBean();

                    selectedInspector.setAccount(list.get(i).getAccount());
                    selectedInspector.setAccountType(list.get(i).getAccountType());
                    selectedInspector.setAddress(list.get(i).getAddress());
                    selectedInspector.setCreateTime(list.get(i).getCreateTime());
                    selectedInspector.setCreator(list.get(i).getCreator());
                    selectedInspector.setDelFlag(list.get(i).getDelFlag());
                    selectedInspector.setDepartmentId(list.get(i).getDepartmentId());
                    selectedInspector.setDescription(list.get(i).getDescription());
                    selectedInspector.setEmail(list.get(i).getEmail());
                    selectedInspector.setEmployeeCode(list.get(i).getEmployeeCode());
                    selectedInspector.setFax(list.get(i).getFax());
                    selectedInspector.setLocked(list.get(i).getLocked());
                    selectedInspector.setId(list.get(i).getId());
                    selectedInspector.setImageData(list.get(i).getImageData());
                    selectedInspector.setName(list.get(i).getName());
                    selectedInspector.setPassword(list.get(i).getPassword());
                    selectedInspector.setResetPwd(list.get(i).getResetPwd());
                    selectedInspector.setRegisterId(list.get(i).getRegisterId());
                    selectedInspector.setSex(list.get(i).getSex());
                    selectedInspector.setTelephone(list.get(i).getTelephone());

                    selectedInspectorsList.add(selectedInspector);

//                    Log.e("Finish","选中了   姓名：" + selectedInspector.getName() + ", 账号:" + selectedInspector.getAccount());
                }
            }

//            Log.e("Finish","选中了" + selectedInspectorsList.size() + "个人");

            Intent intent = new Intent();
            intent.setAction("action");
            intent.putExtra("list", (ArrayList<BaseUserBean>) selectedInspectorsList);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = (Bundle) msg.obj;
            ArrayList<BaseUserBean> list2 = (ArrayList<BaseUserBean>) bundle.get("list");
//            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
            if (list2.size() > 0) {
                Log.e("slecetInspectorsHandler", "Handler中接收到：" + list2.size() + " 个用户的信息");
            } else {
                Log.e("slecetInspectorsHandler", "Handler中无法获取服务器的消息，请稍后再试！");
            }
//            if (list2 != null && list2.size() > 0) {
                for (int i = 0; i < list2.size(); i++) {
                    inspectorDate = new InspectorData();

                    inspectorDate.setSort(ZhongWen2PinYinUtils.getPinYinFirstHeadChar(list2.get(i).getName()));
                    inspectorDate.setIsVisible(View.VISIBLE);
                    inspectorDate.setIsSelected(false); // 默认全都不选中

                    inspectorDate.setAccount(list2.get(i).getAccount());
                    inspectorDate.setAccountType(list2.get(i).getAccountType());
                    inspectorDate.setAddress(list2.get(i).getAddress());
                    inspectorDate.setCreateTime(list2.get(i).getCreateTime());
                    inspectorDate.setCreator(list2.get(i).getCreator());
                    inspectorDate.setDelFlag(list2.get(i).getDelFlag());
                    inspectorDate.setDepartmentId(list2.get(i).getDepartmentId());
                    inspectorDate.setDescription(list2.get(i).getDescription());
                    inspectorDate.setEmail(list2.get(i).getEmail());
                    inspectorDate.setEmployeeCode(list2.get(i).getEmployeeCode());
                    inspectorDate.setFax(list2.get(i).getFax());
                    inspectorDate.setLocked(list2.get(i).getLocked());
                    inspectorDate.setId(list2.get(i).getId());
                    inspectorDate.setImageData(list2.get(i).getImageData());
                    inspectorDate.setName(list2.get(i).getName());
                    inspectorDate.setPassword(list2.get(i).getPassword());
                    inspectorDate.setResetPwd(list2.get(i).getResetPwd());
                    inspectorDate.setRegisterId(list2.get(i).getRegisterId());
                    inspectorDate.setSex(list2.get(i).getSex());
                    inspectorDate.setTelephone(list2.get(i).getTelephone());

                    for (int j = 0; j < i; j++) {
                        if (ZhongWen2PinYinUtils.getPinYinFirstHeadChar(list2.get(j).getName()).equals(ZhongWen2PinYinUtils.getPinYinFirstHeadChar(list2.get(i).getName()))) {
                            inspectorDate.setIsVisible(View.GONE);
                            break;
                        }
                    }
//                    Log.e("handler","已加载至第" + String.valueOf(i) + "条数据，姓名为：" + inspectorDate.getName() + ", 账户为：" + inspectorDate.getAccount());
                    list.add(inspectorDate);
//                }
//                Log.e("handler","已加载" + list.size() + "条数据");
            }
            initSelectedStatus();
            refreshHorizontalScrollView();
            initIspectorsAdaptor();
            initSideBar();

//            else {
//                Log.e("handler","无法获取服务器数据，请稍后再试!");
//            }
        }
    };

    public void initSelectedStatus(){
        Intent intent = getIntent();
        if("action".equals(intent.getAction())) {
            selectedInspectorsList = (ArrayList<BaseUserBean>) intent.getSerializableExtra("list");
            if (selectedInspectorsList == null || selectedInspectorsList.size() == 0){
                Log.e("Intent", "接收到的选中人员信息为空");
            }else {

                for(int i = 0; i < selectedInspectorsList.size(); i ++)
                {
                    for(int j = 0; j < list.size(); j++){
//                        if (selectedInspectorsList.get(i).getName().equals(list.get(j).getName())){
                        if (selectedInspectorsList.get(i).getAccount().equals(list.get(j).getAccount())){
                            list.get(j).setIsSelected(true);
                            break;
                        }
                    }
                    Log.e("Intent","接收到选中人员信息为: (姓名)" + selectedInspectorsList.get(i).getName() + "(账号)" + selectedInspectorsList.get(i).getAccount());
                }
            }
        } else {
            Log.e("Intent", "未接收到选中人员信息");
        }
    }

    public void initDate() {
        new Thread(networkTask).start();
    }

    public void initIspectorsAdaptor(){
//        if (list == null || list.size() < 1) {
//            Log.e("SelectInspector","list is null");
//        }else {
//            inspectorsAdapter = new InspectorsAdapter(this, list);
            inspectorsAdapter = new InspectorsAdapter(this, list, linearLayout_inspector_icon);
            listView_Contact.setAdapter(inspectorsAdapter);
            Log.e("initIspectorsAdaptor","加载数据成功");
//        }
    }

    public void initSideBar() {
        WaveSideBar sideBar = (WaveSideBar) findViewById(R.id.side_bar);
        sideBar.setIndexItems("↑","☆", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#");
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                Log.d("WaveSideBar", index);
                // Do something here ....
                if (list.size() > 0 && list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getSort().equals(index)) {
                            listView_Contact.smoothScrollToPosition(i);
                            inspectorsAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }else {
                    Log.e("initSideBar","list is null");
                }
            }
        });
    }

    public void refreshHorizontalScrollView() {
        if(linearLayout_inspector_icon != null) {
            linearLayout_inspector_icon.clearAnimation();
        }
        for (int i = 0; i < list.size(); i ++)
        {
            View inflate = View.inflate(this, R.layout.inspector_icon_item, null);
            ImageView inspector_icon = (ImageView) inflate.findViewById(R.id.imageView_icon);
            if(list.get(i).getImageData() != null) {
                ImageUtils.loadImageWithPlaceHolder(this, inspector_icon, mHttpPost.getFileUrl(list.get(i).getImageData()));
            } else {
                inspector_icon.setImageResource(R.drawable.default_head);
            }
            if ( list.get(i).getIsSelected() ) {
                linearLayout_inspector_icon.addView(inflate);
            }
        }
    }

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            mHttpPost = new HttpPost();
            if(mHttpPost.findUserAll() != null){
                userList = ZhongWen2PinYinUtils.getAscendingUser(mHttpPost.findUserAll());
                Log.e("select inspectors", "一共获取到" + userList.size() + " 名用户数据");
            }else {
                Log.e("select inspectors", "无法获取用户数据，请稍后再试!!!");
            }
            Bundle bundle=new Bundle();
            bundle.putSerializable("list",(ArrayList<BaseUserBean>)userList);
            Message message=handler.obtainMessage(1, bundle);
            handler.sendMessage(message);


//            for(int i = 0; i < userList.size(); i++){
//                Log.e("imageData","图片信息：" + userList.get(i).getImageData());
//            }
        }
    };

    public void addTestData() {
        ArrayList<String> name = new ArrayList<String>();
        name.add("阿尔法");
        name.add("贝塔");
        name.add("三德子");
        name.add("死鬼");
        name.add("张三");
        list = new ArrayList<InspectorData>();
        for (int i = 0; i < 50; i++) {
            inspectorDate = new InspectorData();
            if (i % 10 == 0) {
                inspectorDate.setIsVisible(View.VISIBLE);
            } else {
                inspectorDate.setIsVisible(View.GONE);
            }
            inspectorDate.setIsSelected(false);
            inspectorDate.setName(name.get(i%5) + String.valueOf(i));
            inspectorDate.setSort(ZhongWen2PinYinUtils.getPinYinHeadChar(name.get(i%5)));

            list.add(inspectorDate);
//            Log.e("ZhongWen2PinYin",contactDate.getContactName() + "的首字母为：" + ZhongWen2PinYinUtils.getPinYinFirstHeadChar(name.get(i%5)) + " ，对应的ASCII码值为：" + ZhongWen2PinYinUtils.getPinYinFirstHeadChar(name.get(i%5)).toUpperCase().charAt(0));
        }
    }
}
