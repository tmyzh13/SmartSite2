package com.isoftstone.smartsite.model.message.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isoftstone.smartsite.R;
import com.isoftstone.smartsite.base.BaseFragment;
import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.message.BeforeNMessageBean;
import com.isoftstone.smartsite.http.message.MessageBean;
import com.isoftstone.smartsite.jpush.MyReceiver;
import com.isoftstone.smartsite.model.message.MessageUtils;
import com.isoftstone.smartsite.model.muckcar.ui.SlagcarInfoActivity;

import java.util.ArrayList;

/**
 * Created by yanyongjun on 2017/10/15.
 */

public class MsgFragment extends BaseFragment {
/*    private ViewPager mViewPager = null;
    ArrayList<Fragment> mFragList = new ArrayList<Fragment>();
    private SparseArray<TextView> mSwitchLab = new SparseArray<>();*/

    public static final int FRAGMENT_TYPE_VCR = 0;
    public static final int FRAGMENT_TYPE_ENVIRON = 1;
    public static final int FRAGMENT_TYPE_SYNERGY = 2;


    private RelativeLayout mVcr = null;
    private RelativeLayout mEnviron = null;
    private RelativeLayout mTripartite = null;
    private Activity mActivity = null;

    public static final String FRAGMENT_TYPE = "type";
    public static final String FRAGMENT_DATA = "data";
    public static final int QUERY_TYPE_VCR = 1;
    public static final int QUERY_TYPE_ENVIRON = 2;
    public static final int QUERY_TYPE_THREE_PARTY = 3;

    private HttpPost mHttpPost = null;
    private SparseArray<Object> mVcrmsg = new SparseArray<Object>();

    private int[] unReadMsgCountView = new int[]{R.id.lab_environ_unread_num, R.id.lab_vcr_unread_num, R.id.lab_threeparty_unread_num};
    private int[] dateView = new int[]{R.id.lab_environ_time, R.id.lab_vcr_time, R.id.lab_thirpartite_time};
    private int[] titleView = new int[]{R.id.lab_environ_msg, R.id.lab_vcr_msg, R.id.lab_threeparty_msg};

    private boolean mInForgourd = false;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void afterCreated(Bundle savedInstanceState) {
        init();
    }

    private void init() {
        mActivity = getActivity();

        //环境监控
        mEnviron = (RelativeLayout) rootView.findViewById(R.id.conlayout_environ);
        mEnviron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, MessageListActivity.class);
                i.putExtra("type", MessageUtils.SEARCH_CODE_ENVIRON);
                mActivity.startActivity(i);
            }
        });

        //视频监控
        mVcr = (RelativeLayout) rootView.findViewById(R.id.conlayout_vcr);
        mVcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, MessageListActivity.class);
                i.putExtra("type", MessageUtils.SEARCH_CODE_VEDIO);
                mActivity.startActivity(i);
            }
        });

        //三方协同
        mTripartite = (RelativeLayout) rootView.findViewById(R.id.conlayout_thirpartite);
        mTripartite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, MessageListActivity.class);
                i.putExtra("type", MessageUtils.SEARCH_CODE_THREE_PARTY);
                mActivity.startActivity(i);
            }
        });

        //渣土车监控
        View v = rootView.findViewById(R.id.conlayout_dirtcar);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, MessageListActivity.class);
                i.putExtra("type", MessageUtils.SEARCH_CODE_DIRTCAR);
                mActivity.startActivity(i);
//                Intent intent = new Intent(mActivity, SlagcarInfoActivity.class);
//                mActivity.startActivity(intent);
            }
        });

        //巡查任务
        v = rootView.findViewById(R.id.conlayout_task);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, MessageListActivity.class);
                i.putExtra("type", MessageUtils.SEARCH_CODE_TASK);
                mActivity.startActivity(i);
            }
        });

        //巡查计划
        v = rootView.findViewById(R.id.conlayout_plan);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity, MessageListActivity.class);
                i.putExtra("type", MessageUtils.SEARCH_CODE_PLAN);
                mActivity.startActivity(i);
            }
        });


        mHttpPost = new HttpPost();
    }

    @Override
    public void onResume() {
        super.onResume();
        mInForgourd = true;
//        new QueryMsgTask(1).execute();
//        new QueryMsgTask(2).execute();
//        new QueryMsgTask(3).execute();
        new QueryAllMsgTask().execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        mInForgourd = false;
    }

    private class QueryAllMsgTask extends AsyncTask<String, Integer, BeforeNMessageBean> {
        @Override
        protected BeforeNMessageBean doInBackground(String... strings) {
            BeforeNMessageBean bean = mHttpPost.getBeforeNMessageList();
            return bean;
        }

        @Override
        protected void onPostExecute(BeforeNMessageBean bean) {
            if (bean == null) {
                return;
            }
            //环境检测消息
            try {
                TextView unReadMessageView = (TextView) rootView.findViewById(R.id.lab_environ_unread_num);
                TextView title = (TextView) rootView.findViewById(R.id.lab_environ_msg);
                TextView time = (TextView) rootView.findViewById(R.id.lab_environ_time);
                if (bean.getUnreadEnvironment() <= 0) {
                    unReadMessageView.setVisibility(View.INVISIBLE);
                } else {
                    unReadMessageView.setVisibility(View.VISIBLE);
                    unReadMessageView.setText(bean.getUnreadEnvironment() + "");
                }
                ArrayList<MessageBean> contents = bean.getEnvironment().getContent();
                if (contents.size() <= 0) {
                    time.setVisibility(View.INVISIBLE);
                } else {
                    time.setVisibility(View.VISIBLE);
                    title.setText(contents.get(0).getContent());
                    time.setText(contents.get(0).getUpdateTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //视频监控消息
            try {
                TextView unReadMessageView = (TextView) rootView.findViewById(R.id.lab_vcr_unread_num);
                TextView title = (TextView) rootView.findViewById(R.id.lab_vcr_msg);
                TextView time = (TextView) rootView.findViewById(R.id.lab_vcr_time);
                if (bean.getUnreadVideos() <= 0) {
                    unReadMessageView.setVisibility(View.INVISIBLE);
                } else {
                    unReadMessageView.setVisibility(View.VISIBLE);
                    unReadMessageView.setText(bean.getUnreadVideos() + "");
                }
                ArrayList<MessageBean> contents = bean.getVideo().getContent();
                if (contents.size() <= 0) {
                    time.setVisibility(View.INVISIBLE);
                } else {
                    time.setVisibility(View.VISIBLE);
                    title.setText(contents.get(0).getContent());
                    time.setText(contents.get(0).getUpdateTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //三方协同消息
            try {
                TextView unReadMessageView = (TextView) rootView.findViewById(R.id.lab_threeparty_unread_num);
                TextView title = (TextView) rootView.findViewById(R.id.lab_threeparty_msg);
                TextView time = (TextView) rootView.findViewById(R.id.lab_thirpartite_time);
                if (bean.getUnreadPatrol() <= 0) {
                    unReadMessageView.setVisibility(View.INVISIBLE);
                } else {
                    unReadMessageView.setVisibility(View.VISIBLE);
                    unReadMessageView.setText(bean.getUnreadPatrol() + "");
                }
                ArrayList<MessageBean> contents = bean.getPatrol().getContent();
                if (contents.size() <= 0) {
                    time.setVisibility(View.INVISIBLE);
                } else {
                    time.setVisibility(View.VISIBLE);
                    title.setText(contents.get(0).getContent());
                    time.setText(contents.get(0).getUpdateTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //渣土车识别消息
            try {
                TextView unReadMessageView = (TextView) rootView.findViewById(R.id.lab_dirtcar_unread_num);
                TextView title = (TextView) rootView.findViewById(R.id.lab_dirtcar_msg);
                TextView time = (TextView) rootView.findViewById(R.id.lab_dirtcar_time);
                if (bean.getUnreadMuckcar() <= 0) {
                    unReadMessageView.setVisibility(View.INVISIBLE);
                } else {
                    unReadMessageView.setVisibility(View.VISIBLE);
                    unReadMessageView.setText(bean.getUnreadMuckcar() + "");
                }

                ArrayList<MessageBean> contents = bean.getMuckcar().getContent();
                if (contents.size() <= 0) {
                    time.setVisibility(View.INVISIBLE);
                } else {
                    time.setVisibility(View.VISIBLE);
                    title.setText(contents.get(0).getContent());
                    time.setText(contents.get(0).getUpdateTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //巡查任务
            try {
                TextView unReadMessageView = (TextView) rootView.findViewById(R.id.lab_task_unread_num);
                TextView title = (TextView) rootView.findViewById(R.id.lab_task_msg);
                TextView time = (TextView) rootView.findViewById(R.id.lab_task_time);
                if (bean.getUnreadTask() <= 0) {
                    unReadMessageView.setVisibility(View.INVISIBLE);
                } else {
                    unReadMessageView.setVisibility(View.VISIBLE);
                    unReadMessageView.setText(bean.getUnreadTask() + "");
                }

                ArrayList<MessageBean> contents = bean.getTask().getContent();
                if (contents.size() <= 0) {
                    time.setVisibility(View.INVISIBLE);
                } else {
                    time.setVisibility(View.VISIBLE);
                    title.setText(contents.get(0).getContent());
                    time.setText(contents.get(0).getUpdateTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //巡查计划
            try {
                TextView unReadMessageView = (TextView) rootView.findViewById(R.id.lab_plan_unread_num);
                TextView title = (TextView) rootView.findViewById(R.id.lab_plan_msg);
                TextView time = (TextView) rootView.findViewById(R.id.lab_plan_time);
                if (bean.getUnreadPlan() <= 0) {
                    unReadMessageView.setVisibility(View.INVISIBLE);
                } else {
                    unReadMessageView.setVisibility(View.VISIBLE);
                    unReadMessageView.setText(bean.getUnreadPlan() + "");
                }

                ArrayList<MessageBean> contents = bean.getPlan().getContent();
                if (contents.size() <= 0) {
                    time.setVisibility(View.INVISIBLE);
                } else {
                    time.setVisibility(View.VISIBLE);
                    title.setText(contents.get(0).getContent());
                    time.setText(contents.get(0).getUpdateTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//
//    private class QueryMsgTask extends AsyncTask<String, Integer, SparseArray<Object>> {
//        private int mQueryType = 0;
//
//        public QueryMsgTask(int type) {
//            mQueryType = type;
//        }
//
//        @Override
//        protected SparseArray<Object> doInBackground(String... params) {
//            ArrayList<MessageBean> msgs = mHttpPost.getMessage("", "", "", mQueryType + "");
//            ArrayList<MsgData> datas = new ArrayList<>();
//            MsgUtils.toMsgData(datas, msgs);
//            int unreadCount = 0;
//            MsgData lastMsg = null;
//            for (MsgData temp : datas) {
//                if (lastMsg == null) {
//                    lastMsg = temp;
//                } else if (temp.getTime().after(lastMsg.getTime())) {
//                    lastMsg = temp;
//                }
//                if (temp.getStatus() == MsgData.STATUS_UNREAD) {
//                    unreadCount++;
//                }
//            }
//            SparseArray<Object> result = new SparseArray<>();
//            result.put(1, unreadCount);
//            if (lastMsg != null) {
//                result.put(2, lastMsg);
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(SparseArray<Object> s) {
//            super.onPostExecute(s);
//            if (!mInForgourd) {
//                return;
//            }
//            TextView unReadMessageView = (TextView) rootView.findViewById(unReadMsgCountView[mQueryType - 1]);
//            TextView title = (TextView) rootView.findViewById(titleView[mQueryType - 1]);
//            TextView time = (TextView) rootView.findViewById(dateView[mQueryType - 1]);
//            if ((Integer) s.get(1) == 0) {
//                unReadMessageView.setVisibility(View.GONE);
//            } else {
//                unReadMessageView.setVisibility(View.VISIBLE);
//                unReadMessageView.setText(s.get(1) + "");
//            }
//
//            MsgData msg = (MsgData) s.get(2);
//            if (msg != null) {
//                title.setText(msg.getTitle());
//                time.setText(msg.getDateString());
//            } else {
//                title.setText("");
//                time.setText("");
//            }
//        }
//    }
}
