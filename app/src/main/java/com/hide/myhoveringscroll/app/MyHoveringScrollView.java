package com.hide.myhoveringscroll.app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 15/9/3
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MyHoveringScrollView extends FrameLayout {

    /**
     * 固定在顶部的View
     */
    private ViewGroup mTopView;

    /**
     * 固定在顶部的View里面的内容
     */
    private View mTopContent;

    /**
     * 固定在顶部的View在滚动条里最上端的位置
     */
    private int mTopViewTop;

    /**
     * 滚动条的内容
     */
    private ViewGroup mContentView;


    public MyHoveringScrollView(Context context) {
        this(context, null);
    }

    public MyHoveringScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MyHoveringScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyHoveringScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        post(new Runnable() {
            @Override
            public void run() {
                mContentView = (ViewGroup) getChildAt(0);
                removeAllViews();

                MyScrollView scrollView = new MyScrollView(getContext(), MyHoveringScrollView.this);
                scrollView.addView(mContentView);
                addView(scrollView);

            }
        });
    }


    public void setTopView(final int id) {
        post(new Runnable() {
            @Override
            public void run() {
                mTopView = (ViewGroup) mContentView.findViewById(id);

                int height = mTopView.getChildAt(0).getMeasuredHeight();
                ViewGroup.LayoutParams params = mTopView.getLayoutParams();
                params.height = height;
                mTopView.setLayoutParams(params);
                mTopViewTop = mTopView.getTop();
                mTopContent = mTopView.getChildAt(0);

            }
        });
    }

    public void onScroll(final int scrollY) {
        post(new Runnable() {
            @Override
            public void run() {
                if (mTopView == null
                        ) return;

                if (scrollY >= mTopViewTop
                        && mTopContent.getParent() == mTopView) {
                    mTopView.removeView(mTopContent);
                    addView(mTopContent);
                } else if (scrollY < mTopViewTop
                        && mTopContent.getParent() == MyHoveringScrollView.this) {
                    removeView(mTopContent);
                    mTopView.addView(mTopContent);
                }

            }
        });
    }

    @SuppressLint("ViewConstructor")
    private static class MyScrollView extends ScrollView {

        private MyHoveringScrollView mScrollView;

        public MyScrollView(Context context, MyHoveringScrollView scrollView) {
            super(context);
            mScrollView = scrollView;
        }


        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            mScrollView.onScroll(t);
        }

    }


}
