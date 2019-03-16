package com.bigkoo.commonui.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bigkoo.commonui.R;

import java.util.ArrayList;

/**
 * Created by Sai on 2018/4/16.
 */

@SuppressWarnings("unused") public class MultipleStatusView extends RelativeLayout {
    private static final RelativeLayout.LayoutParams DEFAULT_LAYOUT_PARAMS =
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);

    public static final int STATUS_CONTENT    = 0x00;
    public static final int STATUS_LOADING    = 0x01;
    public static final int STATUS_EMPTY      = 0x02;
    public static final int STATUS_ERROR      = 0x03;
    public static final int STATUS_NO_NETWORK = 0x04;

    private static final int NULL_RESOURCE_ID = -1;

    private View vEmpty;
    private View vError;
    private View vLoading;
    private View vNetWorkError;
    private View vContent;
    private int  mEmptyViewResId;
    private int  mErrorViewResId;
    private int  mLoadingViewResId;
    private int  mNoNetworkViewResId;
    private int  mContentViewResId;

    private int             mViewStatus;
    private LayoutInflater mInflater;

    private final ArrayList<Integer> mOtherIds = new ArrayList<>();

    public MultipleStatusView(Context context) {
        this(context, null);
    }

    public MultipleStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyleAttr, 0);
        mEmptyViewResId = a.getResourceId(R.styleable.MultipleStatusView_emptyView, R.layout.stateview_empty);
        mErrorViewResId = a.getResourceId(R.styleable.MultipleStatusView_errorView, R.layout.stateview_error);
        mLoadingViewResId = a.getResourceId(R.styleable.MultipleStatusView_loadingView, R.layout.stateview_loading);
        mNoNetworkViewResId = a.getResourceId(R.styleable.MultipleStatusView_networkerrorView, R.layout.stateview_networkerro);
        mContentViewResId = a.getResourceId(R.styleable.MultipleStatusView_contentView, NULL_RESOURCE_ID);
        a.recycle();
        mInflater = LayoutInflater.from(getContext());
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        showContent();
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear(vEmpty, vLoading, vError, vNetWorkError);
        if (null != mOtherIds) {
            mOtherIds.clear();
        }
        mInflater = null;
    }

    /**
     * 获取当前状态
     */
    public int getViewStatus() {
        return mViewStatus;
    }

    /**
     * 显示空视图
     */
    public final void showEmpty(boolean isEmpty) {
        if(isEmpty)
            showEmpty(mEmptyViewResId, DEFAULT_LAYOUT_PARAMS);
        else
            showContent();
    }

    /**
     * 显示空视图
     * @param layoutId 自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showEmpty(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showEmpty(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示空视图
     * @param view 自定义视图
     * @param layoutParams 布局参数
     */
    public final void showEmpty(View view, ViewGroup.LayoutParams layoutParams) {
        if(mViewStatus == STATUS_EMPTY)return;
        checkNull(view, "Empty view is null!");
        mViewStatus = STATUS_EMPTY;
        if (null == vEmpty) {
            vEmpty = view;
            mOtherIds.add(vEmpty.getId());
            addView(vEmpty, 0, layoutParams);
        }
        showViewById(vEmpty.getId());
    }

    /**
     * 显示错误视图
     */
    public final void showError(boolean isError) {
        if(isError)
            showError(mErrorViewResId, DEFAULT_LAYOUT_PARAMS);
        else
            showContent();
    }

    /**
     * 显示错误视图
     * @param layoutId 自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showError(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showError(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示错误视图
     * @param view 自定义视图
     * @param layoutParams 布局参数
     */
    public final void showError(View view, ViewGroup.LayoutParams layoutParams) {
        if(mViewStatus == STATUS_ERROR)return;
        checkNull(view, "Error view is null!");
        mViewStatus = STATUS_ERROR;
        if (null == vError) {
            vError = view;
            mOtherIds.add(vError.getId());
            addView(vError, 0, layoutParams);
        }
        showViewById(vError.getId());
    }

    /**
     * 显示加载中视图
     */
    public final void showLoading(boolean isLoading) {
        if(isLoading)
            showLoading(mLoadingViewResId, DEFAULT_LAYOUT_PARAMS);
        else
            showContent();
    }

    /**
     * 显示加载中视图
     * @param layoutId 自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showLoading(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showLoading(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示加载中视图
     * @param view 自定义视图
     * @param layoutParams 布局参数
     */
    public final void showLoading(View view, ViewGroup.LayoutParams layoutParams) {
        if(mViewStatus == STATUS_LOADING)return;
        checkNull(view, "Loading view is null!");
        mViewStatus = STATUS_LOADING;
        if (null == vLoading) {
            vLoading = view;
            mOtherIds.add(vLoading.getId());
            addView(vLoading, 0, layoutParams);
        }
        showViewById(vLoading.getId());
    }

    /**
     * 显示无网络视图
     */
    public final void showNoNetwork(boolean isNetWorkError) {
        if(isNetWorkError)
            showNoNetwork(mNoNetworkViewResId, DEFAULT_LAYOUT_PARAMS);
        else
            showContent();
    }

    /**
     * 显示无网络视图
     * @param layoutId 自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showNoNetwork(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showNoNetwork(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示无网络视图
     * @param view 自定义视图
     * @param layoutParams 布局参数
     */
    public final void showNoNetwork(View view, ViewGroup.LayoutParams layoutParams) {
        if(mViewStatus == STATUS_NO_NETWORK)return;
        checkNull(view, "No network view is null!");
        mViewStatus = STATUS_NO_NETWORK;
        if (null == vNetWorkError) {
            vNetWorkError = view;
            mOtherIds.add(vNetWorkError.getId());
            addView(vNetWorkError, 0, layoutParams);
        }
        showViewById(vNetWorkError.getId());
    }

    /**
     * 显示内容视图
     */
    public final void showContent() {
        if(mViewStatus == STATUS_CONTENT)return;

        mViewStatus = STATUS_CONTENT;
        if (null == vContent && mContentViewResId != NULL_RESOURCE_ID) {
            vContent = mInflater.inflate(mContentViewResId, null);
            addView(vContent, 0, DEFAULT_LAYOUT_PARAMS);
        }
        showContentView();
    }

    private void showContentView() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.setVisibility(mOtherIds.contains(view.getId()) ? View.GONE : View.VISIBLE);
        }
    }

    private View inflateView(int layoutId) {
        return mInflater.inflate(layoutId, null);
    }

    private void showViewById(int viewId) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.setVisibility(view.getId() == viewId ? View.VISIBLE : View.GONE);
        }
    }

    private void checkNull(Object object, String hint) {
        if (null == object) {
            throw new NullPointerException(hint);
        }
    }

    private void clear(View... views) {
        if (null == views) {
            return;
        }
        try {
            for (View view : views) {
                if (null != view) {
                    removeView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
