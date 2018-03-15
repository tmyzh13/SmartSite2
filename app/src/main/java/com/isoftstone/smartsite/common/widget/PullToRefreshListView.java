package com.isoftstone.smartsite.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isoftstone.smartsite.R;


/***
 * 下拉刷新
 *
 * @author K
 *
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {
    private final static String TAG = PullToRefreshListView.class
            .getSimpleName();

    /**
     * 下拉刷新
     */
    private final static int PULL_TO_REFRESH = 0;
    /**
     * 释放刷新
     */
    private final static int RELEASE_TO_REFRESH = 1;
    /**
     * 刷新中..
     */
    private final static int REFRESHING = 2;
    /**
     * 刷新完成
     */
    private final static int DONE = 3;
    private LayoutInflater inflater;
    private RelativeLayout mHeaderView;
    private TextView mTipsText;
    private ImageView mArrowView;
    private ProgressBar mSpinner;
    private RotateAnimation animRotate;
    private RotateAnimation animReverseRotate;

    private boolean isRecored;

    /**
     * 默认paddingTop为 header高度的负值，使header在屏幕外不可见
     **/
    private int mHeaderViewPaddingTop;

    /**
     * header布局xml文件原始定义的paddingTop
     */
    private int mHeaderOrgPaddingTop;

    private GestureDetector gestureDetector;

    private int mPullState;

    public OnRefreshListener refreshListener;
    public OnLastItemVisibleListener lastItemVisibleListener;
    private boolean lastItemVisible;
    private Context mContext;

    private View mFooterView;
    private boolean mIsLoading = false;
    private int mCurrentScrollState;

    /**
     * 第一个Item是否可见
     */
    private boolean isFirstItemVisible;
    /** 要实现的部分 begin**/

    /**刷新的接口**/
    public interface OnRefreshListener {
        /**
         * 上拉刷新
         */
        public void onRefresh();

        /**
         * 下拉加载下一页
         */
        public void onLoadMore();
    }

    /**
     * 下拉加载下一页结束
     */
    public void onLoadMoreComplete() {
        mIsLoading = false;
        mFooterView.setVisibility(View.GONE);
    }

    /**
     * 设置上拉和下拉的监听
     * @param refreshListener
     */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    /**
     * 上来刷新结束
     */
    public void onRefreshComplete() {
        mPullState = DONE;
        changeHeaderViewByState(mPullState);
    }

    /** 要实现的部分 end **/

    public interface OnLastItemVisibleListener {
        public void onLastItemVisible(int lastIndex);
    }

    public PullToRefreshListView(Context context) {
        this(context, null);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        initFooterView();
        initArrowAnimation();
        initPullHeader(context);
        // 为自定义ListView控件绑定滚动监听事件
        setOnScrollListener(this);
        gestureDetector = new GestureDetector(context, gestureListener);
    }

    private void initFooterView() {
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.listview_root, null);
        addFooterView(mFooterView);
        mFooterView.setVisibility(GONE);
    }

    /***
     * 实例化下拉ListView的Header布局
     *
     * @param context
     */
    private void initPullHeader(Context context) {
        inflater = LayoutInflater.from(context);
        mHeaderView = (RelativeLayout) inflater.inflate(
                R.layout.pull_to_refresh_head, null);
        mArrowView = (ImageView) mHeaderView
                .findViewById(R.id.arrow);
        mSpinner = (ProgressBar) mHeaderView
                .findViewById(R.id.progress_bar);
        mTipsText = (TextView) mHeaderView.findViewById(R.id.description);

        mHeaderOrgPaddingTop = mHeaderView.getPaddingTop();
        measureView(mHeaderView);
        mHeaderViewPaddingTop = -mHeaderView.getMeasuredHeight();
        setHeaderPaddingTop(mHeaderViewPaddingTop);
        mHeaderView.invalidate();
        addHeaderView(mHeaderView);
    }

    private void setHeaderPaddingTop(int paddingTop) {
        mHeaderView.setPadding(mHeaderView.getPaddingLeft(), paddingTop,
                mHeaderView.getPaddingRight(), mHeaderView.getPaddingBottom());
    }

    /**
     * 实例化下拉箭头动画
     */
    private void initArrowAnimation() {
        // 定义一个旋转角度为0 到-180度的动画，时长100ms
        animRotate = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animRotate.setInterpolator(new LinearInterpolator());
        animRotate.setDuration(100);
        animRotate.setFillAfter(true);

        animReverseRotate = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animReverseRotate.setInterpolator(new LinearInterpolator());
        animReverseRotate.setDuration(100);
        animReverseRotate.setFillAfter(true);
    }

    public void onScroll(AbsListView view, int firstVisiableItem,
                         int visibleItemCount, int totalItemCount) {
        isFirstItemVisible = firstVisiableItem == 0 ? true : false;

        boolean loadMore = firstVisiableItem + visibleItemCount >= totalItemCount;

        if (loadMore) {
            if (mPullState != REFRESHING && lastItemVisible == false
                    && lastItemVisibleListener != null) {
                lastItemVisible = true;
                // including Header View,here using totalItemCount - 2
                lastItemVisibleListener.onLastItemVisible(totalItemCount - 2);
            }
        } else {
            lastItemVisible = false;
        }

        // 判断是否滑动到底部：第一个可见的个数与 所有可见Item个数之和 大于等于item总数
        boolean isBottom = firstVisiableItem + visibleItemCount >= totalItemCount;
        //Log.e(TAG,"yanlog isBottom:"+isBottom+" mIsLoading:"+mIsLoading +" mCurrentScrollState:"+mCurrentScrollState);
        if (!mIsLoading && isBottom
                && (mCurrentScrollState != OnScrollListener.SCROLL_STATE_IDLE)) {
            if (refreshListener != null) {
                mIsLoading = true;
                refreshListener.onLoadMore();
                mFooterView.setVisibility(View.VISIBLE);
            }
        }

    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // OnScrollListener.SCROLL_STATE_FLING :手指离开屏幕甩动中
        // OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:手指正在屏幕上滑动中
        // OnScrollListener.SCROLL_STATE_IDLE: 闲置的，未滑动
        Log.i("onScroll", "onScrollStateChanged");
        mCurrentScrollState = scrollState;
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (onTouched.onTouchEvent(event)) {
            return true;
        }
        return super.dispatchTouchEvent(event);
    }

    private interface OnTouchEventListener {
        public boolean onTouchEvent(MotionEvent ev);
    }

    private OnTouchEventListener onTouched = new OnTouchEventListener() {
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (isRecored) {
                        requestDisallowInterceptTouchEvent(false);
                        if (mPullState != REFRESHING) {
                            if (mPullState == PULL_TO_REFRESH) {
                                mPullState = DONE;
                                changeHeaderViewByState(mPullState);
                            } else if (mPullState == RELEASE_TO_REFRESH) {
                                mPullState = REFRESHING;
                                changeHeaderViewByState(mPullState);
                                onRefresh();
                            }
                        }
                        isRecored = false;
                        return true;
                    }
                    break;
            }
            return gestureDetector.onTouchEvent(event);
        }
    };

    /**
     * 自定义手势探测器
     */
    private OnGestureListener gestureListener = new OnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            int deltaY = (int) (e1.getY() - e2.getY());
            if (mPullState != REFRESHING) {
                // 第一个可见，且手势下拉
                if (!isRecored && isFirstItemVisible && deltaY < 0) {
                    isRecored = true;
                    requestDisallowInterceptTouchEvent(true);
                }
                if (isRecored) {
                    int paddingTop = mHeaderView.getPaddingTop();
                    // 释放刷新的过程
                    if (paddingTop < 0 && paddingTop > mHeaderViewPaddingTop) {
                        if (mPullState == RELEASE_TO_REFRESH) {
                            changeHeaderViewByState(PULL_TO_REFRESH);
                        }
                        mPullState = PULL_TO_REFRESH;
                    } else if (paddingTop >= 0) {
                        if (mPullState == PULL_TO_REFRESH) {
                            changeHeaderViewByState(RELEASE_TO_REFRESH);
                        }
                        mPullState = RELEASE_TO_REFRESH;
                    }

                    // 根据手指滑动状态动态改变header高度
                    int topPadding = (int) (mHeaderViewPaddingTop - deltaY / 2);
                    mHeaderView.setPadding(mHeaderView.getPaddingLeft(),
                            topPadding, mHeaderView.getPaddingRight(),
                            mHeaderView.getPaddingBottom());
                    mHeaderView.invalidate();
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
    };

    public void onRefreshing() {
        mPullState = REFRESHING;
        changeHeaderViewByState(mPullState);
    }

    /**
     * 改变刷新状态时，调用该方法来改变headerView 显示的内容
     *
     * @param state 刷新状态
     */
    private void changeHeaderViewByState(int state) {
        switch (state) {
            case RELEASE_TO_REFRESH:
                mSpinner.setVisibility(View.GONE);
                mTipsText.setVisibility(View.VISIBLE);
                mArrowView.setVisibility(View.VISIBLE);
                mArrowView.clearAnimation();
                mArrowView.startAnimation(animRotate);
                //mTipsText.setText("1");
                break;
            case PULL_TO_REFRESH:
                mSpinner.setVisibility(View.GONE);
                mTipsText.setVisibility(View.VISIBLE);
                mArrowView.setVisibility(View.VISIBLE);
                mArrowView.clearAnimation();
                mArrowView.startAnimation(animReverseRotate);
                //mTipsText.setText("2");
                break;
            case REFRESHING:
                // 设置paddingTop为原始paddingTop
                setHeaderPaddingTop(mHeaderOrgPaddingTop);
                // 设置header布局为不可点击，进度条转圈中..
                mHeaderView.invalidate();

                mSpinner.setVisibility(View.VISIBLE);
                mArrowView.clearAnimation();
                mArrowView.setVisibility(View.GONE);
                //mTipsText.setText("3");
                break;
            case DONE:
                // 设置header消失动画
                if (mHeaderViewPaddingTop - 1 < mHeaderView.getPaddingTop()) {
                    ResetAnimimation animation = new ResetAnimimation(mHeaderView,
                            mHeaderViewPaddingTop, false);
                    animation.setDuration(300);
                    mHeaderView.startAnimation(animation);
                }

                mSpinner.setVisibility(View.GONE);
                mArrowView.setVisibility(View.VISIBLE);
                mArrowView.clearAnimation();
                //mArrowView.setImageResource(R.drawable.default_head);

                //mTipsText.setText("3");
                setSelection(0); // listview显示到第一个Item
                break;
        }
    }

    // 点击刷新
    public void clickRefresh() {
        setSelection(0);
        mPullState = REFRESHING;
        changeHeaderViewByState(mPullState);
        onRefresh();
    }



    public void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
        this.lastItemVisibleListener = listener;
    }

    public void onRefreshComplete(String update) {
        onRefreshComplete();
    }



    private void onRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    /***
     * 计算headView的width及height值
     *
     * @param child
     *            计算控件对象
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 消失动画
     */
    public class ResetAnimimation extends Animation {
        private int targetHeight;
        private int originalHeight;
        private int extraHeight;
        private View view;
        private boolean down;
        private int viewPaddingBottom;
        private int viewPaddingRight;
        private int viewPaddingLeft;

        protected ResetAnimimation(View view, int targetHeight, boolean down) {
            this.view = view;
            this.viewPaddingLeft = view.getPaddingLeft();
            this.viewPaddingRight = view.getPaddingRight();
            this.viewPaddingBottom = view.getPaddingBottom();
            this.targetHeight = targetHeight;
            this.down = down;
            originalHeight = view.getPaddingTop();
            extraHeight = this.targetHeight - originalHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {

            int newHeight;
            newHeight = (int) (targetHeight - extraHeight
                    * (1 - interpolatedTime));
            view.setPadding(viewPaddingLeft, newHeight, viewPaddingRight,
                    viewPaddingBottom);
            view.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

    }

}