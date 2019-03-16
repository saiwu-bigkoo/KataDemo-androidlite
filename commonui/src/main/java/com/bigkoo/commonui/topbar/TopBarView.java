package com.bigkoo.commonui.topbar;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.commonui.R;

/**
 * Created by Sai on 2018/3/27.
 * 顶部导航栏
 * colorMode 有 Light 和 Dark模式
 * titleText 设置标题
 * rightText 设置右侧文字按钮
 * rightBackground 设置右侧文字按钮背景
 * showBack 控制左侧图标显示和隐藏
 * showDivider 控制底部分割线显示和隐藏
 * rightIcon 左侧返回键更换图标
 * rightIcon 右侧图标
 * 在 Activity 中 写 onTopBarBack(View view) 函数响应左侧按钮
 * 在 Activity 中 写 onTopBarRightClick(View view) 函数响应右侧按钮,含文字和图标，通过id判断
 * 其他 请看get set方法
 */
public class TopBarView extends Toolbar {
    public static final int TOPBARMODE_LIGHT = 0;
    public static final int TOPBARMODE_DARK = 1;
    private int mode = TOPBARMODE_LIGHT;
    private String titleText;
    private String rightText;
    private TextView tvTopBarTitle;
    private TextView tvTopBarRight;
    private ImageView ivTopBarBack;
    private ImageView ivTopBarRight;
    private View dividerView;
    private int titleBackground;
    private int rightBackground;
    private boolean showBack;
    private boolean showDivider;
    private int leftIcon;
    private int rightIcon;
    public static final int NULLRES = -1;//不设置资源

    public TopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setContentInsetsRelative(0,0);
        setContentInsetsAbsolute(0,0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.TopBarView);
        mode =typedArray.getColor(R.styleable.TopBarView_colorMode, mode);
        titleText = typedArray.getString(R.styleable.TopBarView_titleText);
        rightText = typedArray.getString(R.styleable.TopBarView_rightText);
        titleBackground = typedArray.getColor(R.styleable.TopBarView_titleBackground, getResources().getColor(R.color.topbar_color_titleBackground));
        rightBackground = typedArray.getColor(R.styleable.TopBarView_rightBackground, getResources().getColor(R.color.topbar_color_titleBackground));
        showBack = typedArray.getBoolean(R.styleable.TopBarView_showBack, true);
        leftIcon = typedArray.getResourceId(R.styleable.TopBarView_leftIcon, NULLRES);
        rightIcon = typedArray.getResourceId(R.styleable.TopBarView_rightIcon, NULLRES);
        showDivider = typedArray.getBoolean(R.styleable.TopBarView_showDivider, true);
//        String clickMethod = typedArray.getString(R.styleable.TopBarView_onClick);
//        try {
//            Method method = getContext().getClass().getMethod(clickMethod, View.class);
//            method.invoke(context, View.class);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

        typedArray.recycle();
        initView(context);
        initData();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.topbarview, this);
        tvTopBarTitle = findViewById(R.id.tvTopBarTitle);
        tvTopBarRight = findViewById(R.id.tvTopBarRight);
        ivTopBarBack = findViewById(R.id.ivTopBarBack);
        ivTopBarRight = findViewById(R.id.ivTopBarRight);
        dividerView = findViewById(R.id.dividerView);

        ivTopBarBack.setVisibility(showBack?VISIBLE:INVISIBLE);
        ivTopBarRight.setVisibility(rightIcon != NULLRES?VISIBLE:GONE);
        dividerView.setVisibility(showDivider?VISIBLE:INVISIBLE);
        tvTopBarRight.setVisibility(!TextUtils.isEmpty(rightText)?VISIBLE:INVISIBLE);
        switch (mode){
            case TOPBARMODE_LIGHT:
                tvTopBarTitle.setTextColor(context.getResources().getColor(R.color.topbar_color_title_light));
                tvTopBarRight.setTextColor(context.getResources().getColor(R.color.topbar_color_righttext_light));
                ivTopBarBack.setImageResource(R.drawable.ic_topbar_back_black);
                break;
            case TOPBARMODE_DARK:
                tvTopBarTitle.setTextColor(context.getResources().getColor(R.color.topbar_color_title_dark));
                tvTopBarRight.setTextColor(context.getResources().getColor(R.color.topbar_color_righttext_dark));
                ivTopBarBack.setImageResource(R.drawable.ic_topbar_back_white);
                break;
        }

        tvTopBarTitle.setBackgroundColor(titleBackground);
        tvTopBarRight.setBackgroundColor(rightBackground);
        if(leftIcon != NULLRES)
            ivTopBarBack.setImageResource(leftIcon);
        if(rightIcon != NULLRES)
            ivTopBarRight.setImageResource(rightIcon);

    }

    private void initData() {
        if(!TextUtils.isEmpty(titleText))
            tvTopBarTitle.setText(titleText);
        if(!TextUtils.isEmpty(rightText))
            tvTopBarRight.setText(rightText);
    }

    public String getTitleText() {
        return titleText;
    }

    /**
     * 设置中间标题
     * @param titleText
     */
    public void setTitleText(String titleText) {
        this.titleText = titleText;
        if(!TextUtils.isEmpty(titleText)) {
            tvTopBarTitle.setText(titleText);
        }
        else {
            tvTopBarTitle.setText("");
        }
    }

    public String getRightText() {
        return rightText;
    }

    /**
     * 设置右侧文字
     * @param rightText
     */
    public void setRightText(String rightText) {
        this.rightText = rightText;
        if(!TextUtils.isEmpty(rightText)) {
            tvTopBarRight.setText(rightText);
            tvTopBarRight.setVisibility(VISIBLE);
        }
        else {
            tvTopBarRight.setText("");
            tvTopBarRight.setVisibility(INVISIBLE);
        }
    }

    public boolean isShowBack() {
        return showBack;
    }

    public void setShowBack(boolean showBack) {
        this.showBack = showBack;
        ivTopBarBack.setVisibility(showBack?VISIBLE:INVISIBLE);
    }

    /**
     * 设置左侧图标
     * @param resId
     */
    public void setLeftIcon(int resId) {
        ivTopBarBack.setVisibility(showBack?VISIBLE:INVISIBLE);
        ivTopBarBack.setImageResource(resId);
    }

    /**
     * 设置左侧图标
     * @param resId
     */
    public void setRightIcon(int resId) {
        ivTopBarRight.setVisibility(resId != NULLRES?VISIBLE:GONE);
        ivTopBarRight.setImageResource(resId);
    }

    public boolean isShowDivider() {
        return showDivider;
    }

    /**
     * 设置分割线显示或隐藏
     * @param showDivider
     */
    public void setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;
        dividerView.setVisibility(showDivider?VISIBLE:INVISIBLE);
    }

    public TextView getTopBarTitleView() {
        return tvTopBarTitle;
    }
//
//    public ImageView getBackView() {
//        return ivTopBarBack;
//    }
//
//    public View getDividerView() {
//        return dividerView;
//    }
//
//    public ImageView getRightIconView() {
//        return ivTopBarRight;
//    }
//
//    public TextView getRightTextView() {
//        return tvTopBarRight;
//    }
}
