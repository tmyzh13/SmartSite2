package com.isoftstone.smartsite.model.system.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.user.UserBean;;
import com.isoftstone.smartsite.http.user.BaseUserBean;
import com.isoftstone.smartsite.utils.ImageUtils;
import com.isoftstone.smartsite.utils.NetworkUtils;
import com.isoftstone.smartsite.utils.ToastUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zyf on 2017/10/13 20:00.
 * 个人中心页面
 * modified by zyf on 2017/10/21
 * modified by zyf on 2017/11/1
 */
public class IndividualCenterFragment extends BaseFragment implements UploadUtil.OnUploadProcessListener, View.OnClickListener{
    private boolean mHandledPress = false;
    private ImageView mHeadImageView;//用户头像IV
    private Bitmap mHeadBitmap;//裁剪后得图片
    String picPath;//头像路径
    private PermissionsChecker mPermissionsChecker;
    private static final int REQUEST_CODE = 100; // 权限检查请求码

    private Fragment mCurrentFrame;
    private EditText mUserNameView;
    private ImageView mUserName;
    private TextView mUserRoleView;
    private TextView mUserSexView;
    private TextView mUserAccountView;
    private EditText mUserPwdView;
    private ImageView mUserPwd;
    private RelativeLayout mShowPwdLayout;
    private EditText mUserPhoneNumView;
    private ImageView mUserPhoneNum;
    private TextView mUserCompanyView;
    private EditText mUserAutographView;
    private ImageView mUserAutograph;
    private RelativeLayout mUserCompanySelectLayout;
    boolean isShowCancelOption = true;
    private Long mUserId;
    private Bitmap mUploadHeadBitmap;
    private boolean isShowPwd = false;

    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton1;
    private RadioButton mRadioButton2;
    private static final String SEX_MALE_CODE = "0";
    private static final String SEX_FEMALE_CODE = "1";

    private HttpPost mHttpPost = null;

    /* 请求识别码 选择图库*/
    private static final int IMAGE_REQUEST_CODE = 1;
    /* 请求识别码 照相机*/
    private static final int CAMERA_REQUEST_CODE = 2;
    /* 请求识别码 裁剪*/
    private static final int RESULECODE = 3;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Context mContext;

    @Override
    public void onStart() {
        super.onStart();
        backHandlerInterface.setSelectedFragment(this);
        mContext = getContext();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_individual_center;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        init();

        initToolbar();

        mCurrentFrame = IndividualCenterFragment.this;
        if (!(getActivity() instanceof BackHandlerInterface)) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        } else {
            // 强转为接口实例  
            backHandlerInterface = (BackHandlerInterface) getActivity();
        }

        //获取服务器数据显示..... zyf modifed.....
        new Thread(){
            @Override
            public void run() {
                try {
                    UserBean userBean = mHttpPost.getLoginUser();
                    MyThread myThread = new MyThread(userBean);
                    mHandler.post(myThread);
                } catch (Exception e) {
                    Log.i(TAG, "throws a exception: "  + e.getMessage());
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /**
     * 实例化
     */
    private void init() {
        mHttpPost = new HttpPost();
        mPermissionsChecker = new PermissionsChecker(getActivity().getApplicationContext());

        picPath = Environment.getExternalStorageDirectory() + "/isoftstone/";
        mHeadImageView = (ImageView) rootView.findViewById(R.id.head_iv_new);
        mUserNameView = (EditText) rootView.findViewById(R.id.user_name);
        mUserName = (ImageView) rootView.findViewById(R.id.img_edit_2);
        mUserRoleView = (TextView) rootView.findViewById(R.id.user_role);
        mUserSexView = (TextView) rootView.findViewById(R.id.user_sex);
        mUserAccountView = (TextView) rootView.findViewById(R.id.user_account);
        mUserPwdView = (EditText) rootView.findViewById(R.id.user_pwd);
        mUserPwd = (ImageView) rootView.findViewById(R.id.img_edit_6);
        mShowPwdLayout = (RelativeLayout)  rootView.findViewById(R.id.show_pwd_layout);
        mUserPhoneNumView = (EditText) rootView.findViewById(R.id.user_phoneNum);
        mUserPhoneNum = (ImageView) rootView.findViewById(R.id.img_edit_7);
        mUserCompanyView = (TextView) rootView.findViewById(R.id.user_company);
        mUserAutographView = (EditText) rootView.findViewById(R.id.user_autograph);
        mUserAutograph = (ImageView) rootView.findViewById(R.id.img_edit_9);
        mUserCompanySelectLayout = (RelativeLayout)  rootView.findViewById(R.id.droparrow_layout);
        mRadioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group);
        mRadioButton1 = (RadioButton) rootView.findViewById(R.id.radio_button_1);
        mRadioButton2 = (RadioButton) rootView.findViewById(R.id.radio_button_2);

        registerListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            ToastUtils.showShort("缺少主要权限, 无法运行");
            return;
        }

        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                // 如果是直接从相册获取
                case IMAGE_REQUEST_CODE:
                    cropPhoto(intent.getData());//裁剪图片
                    break;
                // 如果是调用相机拍照时
                case CAMERA_REQUEST_CODE:
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/" + SystemFragment.HEAD_IMAGE_NAME);
                    cropPhoto(Uri.fromFile(temp));//裁剪图片
                    break;
                case RESULECODE:
                    if (intent != null) {
                        Bundle extras = intent.getExtras();
                        mHeadBitmap = extras.getParcelable("data");
                        if (mHeadBitmap != null) {
                            setPicToView(mHeadBitmap);//保存在SD卡中
                        }
                    }
                    break;
                default:
                    break;

            }
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {

        Intent intentFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 设置文件类型
        intentFromGallery.setType("image/*");
        startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {

        if (PhoneInfoUtils.hasSdcard()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                    SystemFragment.HEAD_IMAGE_NAME)));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);//采用ForResult打开
        }
    }

    /**
     * 裁剪图片
     *
     * @param uri 照片的url地址
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", mContext.getResources().getDimensionPixelOffset(R.dimen.head_image_view_height));
        intent.putExtra("outputY", mContext.getResources().getDimensionPixelOffset(R.dimen.head_image_view_height));
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULECODE);
    }

    /**
     * 将裁剪得到的图片保存到本地
     */
    private void setPicToView(Bitmap bitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(picPath);

        if (!file.exists()) {
            // 创建文件夹
            file.mkdirs();
        }

        String fileName = picPath + SystemFragment.HEAD_IMAGE_NAME;//图片名字
        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                if (b != null) {
                    b.flush();
                    b.close();
                }
                mUploadHeadBitmap = bitmap;
                Log.i("zyf","mHeadBitmap = " +  mUploadHeadBitmap);
                mHeadImageView.setImageURI(null);
                mHeadImageView.setImageBitmap(null);
                mHeadImageView.setImageBitmap(mUploadHeadBitmap);
                mHeadImageView.invalidate();
                //toUploadHeadFile(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //上传图片
    private void toUploadHeadFile(final Bitmap bitmap) {
        new Thread(){
            @Override
            public void run() {
                mHttpPost.userImageUpload(bitmap,Bitmap.CompressFormat.JPEG);
            }
        }.start();
        /**String fileKey = "icon";
        UploadUtil uploadUtil = UploadUtil.getInstance();
        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
        Map<String, String> params = new HashMap<>();//请求参数
        uploadUtil.uploadFile(picPath + "head.jpg", fileKey, "下载链接", params);*/
    }

    @Override
    public void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(getActivity(), REQUEST_CODE, PERMISSIONS);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHeadBitmap != null) {
            mHeadBitmap.recycle();
            mHeadBitmap = null;
            System.gc();
        }
    }

    /**
     * 去上传文件
     */
    protected static final int TO_UPLOAD_FILE = 1;
    /**
     * 上传文件响应
     */
    protected static final int UPLOAD_FILE_DONE = 2;  //
    /**
     * 上传初始化
     */
    private static final int UPLOAD_INIT_PROCESS = 3;
    /**
     * 上传中
     */
    private static final int UPLOAD_IN_PROCESS = 4;

    @Override
    public void onUploadDone(int responseCode, String message) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onUploadProcess(int uploadSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_IN_PROCESS;
        msg.arg1 = uploadSize;
        mHandler.sendMessage(msg);
    }

    @Override
    public void initUpload(int fileSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_INIT_PROCESS;
        msg.arg1 = fileSize;
        mHandler.sendMessage(msg);
    }

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_UPLOAD_FILE:
                    break;
                case UPLOAD_INIT_PROCESS:
                    break;
                case UPLOAD_IN_PROCESS:
                    break;
                case UPLOAD_FILE_DONE:
                    String result = msg.obj + "";
//                    paserResult(result);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_icon_right:
                //toolbar right view click
                //保存用戶數據 上傳到服务器..... zyf modifed.....
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showShort("当前无网络环境，无法完成用户信息更改操作");
                    break;
                }

                new Thread(){
                    @Override
                    public void run() {
                        try {
                            if (null != mUploadHeadBitmap) {
                                toUploadHeadFile(mUploadHeadBitmap);
                            }

                            UserBean userBean = mHttpPost.getLoginUser();
                            if (userBean != null) {
                                updateUserInfo(getUpdateUserBean(userBean.getLoginUser().getImageData()));
                            } else {
                                updateUserInfo(getUpdateUserBean(null));
                            }

                            Fragment systemFragment = new SystemFragment();
                            changeToAnotherFragment(mCurrentFrame, systemFragment);

                        } catch (Exception e) {
                            Log.i(TAG, "throws a exception: "  + e.getMessage());
                            e.printStackTrace();
                            ToastUtils.showShort("保存数据失败，请稍后尝试...");
                        }
                    }
                }.start();
                break;
            case R.id.btn_back:
                //back toolbar click
                Fragment systemFragment = new SystemFragment();
                changeToAnotherFragment(mCurrentFrame, systemFragment);
                break;
            case R.id.show_pwd_layout:
                isShowPwd = !isShowPwd;
                if (isShowPwd) {
                    //显示密码
                    mUserPwdView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //隐藏密码
                    mUserPwdView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            default:
                break;
        }
    }


    protected BackHandlerInterface backHandlerInterface;
    public interface BackHandlerInterface {
        public void setSelectedFragment(IndividualCenterFragment backHandledFragment);
    }
    /**
     * 其返回一个布尔值;意思是,如果对返回事件进行了处理就返回TRUE,如果不做处理就返回FALSE,让上层进行处理
     */
    public boolean onFragmentBackPressed() {
        if (!mHandledPress) {
            mHandledPress = true;
            Fragment systemFragment = new SystemFragment();
            changeToAnotherFragment(mCurrentFrame, systemFragment);
            return true;
        }
        return false;
    }

    private void changeToAnotherFragment(Fragment currentFrame,Fragment toFragment){
        //如果是不是用的v4的包，则用getActivity().getFragmentManager();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //注意v4包的配套使用;
        if(currentFrame != toFragment) {
            if(!toFragment.isAdded()) {
                transaction.hide(currentFrame).add(R.id.fl_system_content, toFragment).commitAllowingStateLoss();
            } else{
                transaction.hide(currentFrame).show(toFragment).commitAllowingStateLoss();
            }
            mCurrentFrame = toFragment;
        }
    }

    private void initToolbar(){
        TextView tv_title = (TextView) rootView.findViewById(R.id.toolbar_title);
        tv_title.setText(R.string.individual_center);

        rootView.findViewById(R.id.btn_back).setOnClickListener(this);

        TextView right_title = (TextView)rootView.findViewById(R.id.btn_icon_right);
        right_title.setText(R.string.ok);
        right_title.setOnClickListener(this);
    }

    private void initUserInfo(UserBean userBean) {
        if (null == userBean) {
            return;
        }
        mUserId = userBean.getLoginUser().getId();
        mUserNameView.setText(userBean.getLoginUser().getName());
        mUserRoleView.setText(userBean.getLoginUser().getEmployeeCode());
        mUserSexView.setText(userBean.getLoginUser().getSex() + "");
        mUserAccountView.setText(userBean.getLoginUser().getAccount());
        mUserPwdView.setText(userBean.getLoginUser().getPassword());
        mUserPhoneNumView.setText(userBean.getLoginUser().getTelephone());
        try{
            mUserCompanyView.setText(mHttpPost.getCompanyNameByid(Integer.parseInt(userBean.getLoginUser().getDepartmentId())));
        }catch (NumberFormatException e){
            mUserCompanyView.setText("公司ID为空");
        }
        mUserAutographView.setText(userBean.getLoginUser().getDescription());
        Log.i("zyf","getUserInfo--->" + userBean.toString());

        /**Bitmap headBitmap = BitmapFactory.decodeFile(picPath + userBean.getImageData());
        if (null != headBitmap) {
            mHeadImageView.setImageBitmap(headBitmap);
        } else {
            mHeadImageView.setImageResource(R.drawable.default_head);
        }*/

        String urlString = mHttpPost.getFileUrl(userBean.getLoginUser().getImageData());
        //ToastUtils.showShort("urlString = " + urlString);
        //String urlstr = "http://g.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=edebdc82f91986184112e7827add024b/b812c8fcc3cec3fda2f3fe96d788d43f86942707.jpg";
        Log.i("zyf"," initUserInfo urlString--->" + urlString);

        mHeadImageView.setImageURI(null);
        mHeadImageView.setImageDrawable(null);
        mHeadImageView.setImageBitmap(null);
        ImageUtils.loadImageWithPlaceHolder2(mContext, mHeadImageView, urlString, R.drawable.default_head);
        mHeadImageView.invalidate();


        if (SEX_MALE_CODE.equals(mUserSexView.getText().toString())) {
            mRadioButton1.setChecked(true);
            mRadioButton2.setChecked(false);
        } else if (SEX_FEMALE_CODE.equals(mUserSexView.getText().toString())) {
            mRadioButton1.setChecked(false);
            mRadioButton2.setChecked(true);
        }
    }

    private void updateUserInfo(BaseUserBean userBean) {
        Log.i("zyf","updateUserInfo--->" + userBean.toString());
        mHttpPost.userUpdate(userBean);
    }

    private BaseUserBean getUpdateUserBean(String imageData) {
        BaseUserBean userBean = new BaseUserBean();

        userBean.setId(mUserId);
        //userBean.setAccount("admin");
        //userBean.setPassword("bmeB4000");
        Log.i("zyf", "getUpdateUserBean   imageData = " + imageData);
        if (null != imageData) {
            userBean.setImageData(imageData);
        }
        //userBean.setFax("123");
        //userBean.setEmail("123@qq.com");
        //userBean.setCreateTime("2017-10-31T18:35:29.000+0800");
        //userBean.setAccountType(1);

        if (null !=  mUserNameView.getText()) {
            userBean.setName(mUserNameView.getText().toString());
        }
        if (null !=  mUserSexView.getText()) {
            try {
                userBean.setSex(Integer.parseInt(mUserSexView.getText().toString()));
            }catch (Exception e) {
                Log.i(TAG, "throws an excption : " + e.getMessage());
            }

        }
        //if (null != mUserAccountView.getText()) {
        //    userBean.setAccount(mUserAccountView.getText().toString());
        //}
        //if (null !=  mUserPwdView.getText()) {
            //userBean.setPassword(mUserPwdView.getText().toString());
        //    userBean.setPassword("bmeB4000");
        //}
        if (null !=  mUserPhoneNumView.getText()) {
            userBean.setTelephone(mUserPhoneNumView.getText().toString());
        }
        //if (null !=  mUserCompanyView.getText()) {
        //   userBean.setDepartmentId(mUserCompanyView.getText().toString());
       //}
        if (null !=  mUserAutographView.getText()) {
            userBean.setDescription(mUserAutographView.getText().toString());
        }
        return userBean;
    }

    private class MyThread implements Runnable {
        private MyThread (){}
        private UserBean userBean;
        private MyThread (UserBean userBean) {
            this.userBean = userBean;
        }
        @Override
        public void run() {
            initUserInfo(userBean);
        }
    }

    private void registerListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mRadioButton1.getId()) {
                    mUserSexView.setText(SEX_MALE_CODE);
                } else {
                    mUserSexView.setText(SEX_FEMALE_CODE);
                }
            }
        });

        mShowPwdLayout.setOnClickListener(this);

        mUserCompanySelectLayout.setOnClickListener(new LinearLayout.OnClickListener(){
            @Override
            public void onClick(View v) {
                isShowCancelOption = false;
                new ActionSheetDialog(getContext())
                        .builder(isShowCancelOption)
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem(mContext.getText(R.string.user_company_1).toString(),
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {
                                        mUserCompanyView.setText(R.string.user_company_1);
                                    }
                                })
                        .addSheetItem(mContext.getText(R.string.user_company_2).toString(),
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {
                                        mUserCompanyView.setText(R.string.user_company_2);
                                    }
                                })
                        .addSheetItem(mContext.getText(R.string.user_company_3).toString(),
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {
                                        mUserCompanyView.setText(R.string.user_company_3);
                                    }
                                }).show();
            }
        });

        mHeadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowCancelOption = true;
                new ActionSheetDialog(getContext())
                        .builder(isShowCancelOption)
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem(mContext.getText(R.string.album).toString(),
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {
                                        choseHeadImageFromGallery();
                                    }
                                })
                        .addSheetItem(mContext.getText(R.string.camera).toString(),
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {
                                        choseHeadImageFromCameraCapture();
                                    }
                                }).show();
            }
        });

        mUserNameView.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() != 0) {
                    mUserName.setImageResource(R.drawable.editcolumn);
                } else {
                    mUserName.setImageResource(R.drawable.addcolumn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mUserPwdView.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() != 0) {
                    mUserPwd.setImageResource(R.drawable.editcolumn);
                } else {
                    mUserPwd.setImageResource(R.drawable.addcolumn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mUserAutographView.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s != null && s.length() != 0) {
                    mUserAutograph.setImageResource(R.drawable.editcolumn);
                } else {
                    mUserAutograph.setImageResource(R.drawable.addcolumn);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() != 0) {
                    mUserAutograph.setImageResource(R.drawable.editcolumn);
                } else {
                    mUserAutograph.setImageResource(R.drawable.addcolumn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mUserPhoneNumView.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s != null && s.length() != 0) {
                    mUserPhoneNum.setImageResource(R.drawable.editcolumn);
                } else {
                    mUserPhoneNum.setImageResource(R.drawable.addcolumn);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() != 0) {
                    mUserPhoneNum.setImageResource(R.drawable.editcolumn);
                } else {
                    mUserPhoneNum.setImageResource(R.drawable.addcolumn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

}
