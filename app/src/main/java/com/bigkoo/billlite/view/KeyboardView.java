package com.bigkoo.billlite.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.billlite.R;
import com.bigkoo.billlite.utils.StringUtil;
import com.orhanobut.logger.Logger;

/**
 * 自定义键盘
 *
 * @author wuhao
 */
public class KeyboardView extends LinearLayout implements View.OnClickListener {

    public static final int KEY_1 = R.id.key1;
    public static final int KEY_2 = R.id.key2;
    public static final int KEY_3 = R.id.key3;
    public static final int KEY_4 = R.id.key4;
    public static final int KEY_5 = R.id.key5;
    public static final int KEY_6 = R.id.key6;
    public static final int KEY_7 = R.id.key7;
    public static final int KEY_8 = R.id.key8;
    public static final int KEY_9 = R.id.key9;
    public static final int KEY_0 = R.id.key0;
    public static final int KEY_DOT = R.id.keyDot;
    public static final int KEY_PLUS = R.id.keyPlus;
    public static final int KEY_ENTER = R.id.keyEnter;
    public static final int KEY_DEL = R.id.keyDel;
    public static final int KEY_FN = R.id.keyFn;

    private TextView tvNumber;
    private EditText etContent;
    private TextView tvKey0;
    private TextView tvKey1;
    private TextView tvKey2;
    private TextView tvKey3;
    private TextView tvKey4;
    private TextView tvKey5;
    private TextView tvKey6;
    private TextView tvKey7;
    private TextView tvKey8;
    private TextView tvKey9;
    private TextView tvKeyDot;
    private TextView tvKeyPlus;
    private View ivKeyDel;
    private TextView tvKeyFn;
    private TextView tvKeyEnter;

    private OnKeyClickListener listener;//键盘事件

    private int status = INPUT;//键盘状态
    private static final int INPUT = 0;//输入状态
    private static final int CALCULATE = 1;//计算状态

    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_keyboard, this);
        initView();
        initListener();
    }

    private void initListener() {
        tvKey0.setOnClickListener(this);
        tvKey1.setOnClickListener(this);
        tvKey2.setOnClickListener(this);
        tvKey3.setOnClickListener(this);
        tvKey4.setOnClickListener(this);
        tvKey5.setOnClickListener(this);
        tvKey6.setOnClickListener(this);
        tvKey7.setOnClickListener(this);
        tvKey8.setOnClickListener(this);
        tvKey9.setOnClickListener(this);
        tvKeyDot.setOnClickListener(this);
        tvKeyPlus.setOnClickListener(this);
        tvKeyFn.setOnClickListener(this);
        ivKeyDel.setOnClickListener(this);
        tvKeyEnter.setOnClickListener(this);
    }


    private void initView() {
        tvNumber = findViewById(R.id.tvNumber);
        etContent = findViewById(R.id.etContext);
        tvKeyEnter = findViewById(KEY_ENTER);
        tvKey0 = findViewById(KEY_0);
        tvKey1 = findViewById(KEY_1);
        tvKey2 = findViewById(KEY_2);
        tvKey3 = findViewById(KEY_3);
        tvKey4 = findViewById(KEY_4);
        tvKey5 = findViewById(KEY_5);
        tvKey6 = findViewById(KEY_6);
        tvKey7 = findViewById(KEY_7);
        tvKey8 = findViewById(KEY_8);
        tvKey9 = findViewById(KEY_9);
        tvKeyDot = findViewById(KEY_DOT);
        tvKeyPlus = findViewById(KEY_PLUS);
        tvKeyFn = findViewById(KEY_FN);
        tvKeyEnter = findViewById(KEY_ENTER);
        ivKeyDel = findViewById(KEY_DEL);
    }

    @Override
    public void onClick(View view) {
        StringBuffer old = new StringBuffer(tvNumber.getText().toString());
        int len = old.length();
        switch (view.getId()) {
            case KEY_0:

            case KEY_1:
            case KEY_2:
            case KEY_3:
            case KEY_4:
            case KEY_5:
            case KEY_6:
            case KEY_7:
            case KEY_8:
            case KEY_9:
                if (len == 1 && old.charAt(0) == '0') {
                    //删除首位的0
                    old.deleteCharAt(0);
                    tvNumber.setText(old.append(((TextView) view).getText().toString()));
                } else if (len > 3 && old.charAt(len - 1) != '+' && old.charAt(len - 3) == '.') {
                    //已经有2位小数了，忽略
                } else {
                    tvNumber.setText(old.append(((TextView) view).getText().toString()));
                }
                break;
            case KEY_PLUS:
                if (len == 0) {
                    //首位不能是+
                } else if (old.charAt(len - 1) == '+' || old.charAt(len - 1) == '.') {
                    //最后一位是+和小数点，忽略
                } else if (old.toString().contains("+")) {
                    //前面已经有+了，先计算结果
                    tvNumber.setText(calculate(old.toString()) + "+");
                    switchKeyEnter(CALCULATE);
                } else {
                    tvNumber.setText(old.append("+"));
                    switchKeyEnter(CALCULATE);
                }
                break;
            case KEY_DEL:
                if (len != 0) {
                    if (len < 3) {
                        //首位不会是+
                        old.deleteCharAt(len - 1);
                        switchKeyEnter(INPUT);
                    } else {
                        //删除后还存在+
                        if (old.deleteCharAt(len - 1).toString().contains("+")) {
                            switchKeyEnter(CALCULATE);
                        } else {
                            switchKeyEnter(INPUT);
                        }
                    }
                    tvNumber.setText(old);
                } else {
                    switchKeyEnter(INPUT);
                }

                break;
            case KEY_DOT:
                if (len == 0) {
                    //首位输入小数点，忽略
                } else if (old.charAt(len - 1) == '.' || old.charAt(len - 1) == '+') {
                    //前一位是小数点或者+，忽略
                } else if (!old.toString().contains("+") && old.toString().contains(".")) {
                    //没有运算符，且已经有小数点了，忽略
                } else if (old.toString().contains("+") &&
                        (old.substring(old.toString().indexOf("+") + 1, len - 1).toString().contains("."))) {
                    //有运算符，且后面已经有小数点了，忽略
                } else {
                    tvNumber.setText(old.append('.'));
                }
                break;
            case KEY_FN:
                if (listener != null) {
                    listener.onKeyFnClick(view);
                }
                break;
            case KEY_ENTER:
                if (status == CALCULATE) {
                    tvNumber.setText(calculate(old.toString()));
                    switchKeyEnter(INPUT);
                } else if (listener != null) {
                    listener.onKeyEnterClick();
                }
                break;
        }
    }

    /**
     * 切换Enter键状态
     * 0-输入状态
     * 1-运算状态
     *
     * @param newStatus
     */
    private void switchKeyEnter(int newStatus) {
        if (newStatus == INPUT) {
            this.status = INPUT;
            tvKeyEnter.setText(R.string.keyboard_enter);
        } else {
            this.status = CALCULATE;
            tvKeyEnter.setText(R.string.keyboard_equal);
        }
    }


    /**
     * 加法运算
     *
     * @param old 要运算才字符串
     * @return 运算结果
     */
    private String calculate(String old) {
        String[] values = old.split("[+]");
        //先判断下有没小数点,然后判断有没分割成2个数
        if (old.contains(".")) {
            if (values.length > 1) {
                String result = String.valueOf(Double.parseDouble(values[0]) + Double.parseDouble(values[1]));
                if (result.endsWith(".0")) {
                    //计算后结果是整数，则转换成整数
                    return result.substring(0, result.length() - 2);
                } else {
                    return result;
                }
            } else {
                return String.valueOf(values[0]);
            }
        } else {
            if (values.length > 1) {
                return String.valueOf(Integer.parseInt(values[0]) + Integer.parseInt(values[1]));

            } else {
                return String.valueOf(values[0]);
            }
        }
    }

    /**
     * 获取右侧数字
     *
     * @return 右侧数字，或者0.0
     */
    public double getRightNumber() {
        String str = tvNumber.getText().toString().trim();
        if (!TextUtils.isEmpty(str)) {
            return Double.parseDouble(str);
        } else {
            return 0.0;
        }
    }

    /**
     * 设置右侧数字
     */
    public void setRightNumber(double d) {
        String s = StringUtil.doubleTwoDecimal(d);
        tvNumber.setText(s);
    }

    /**
     * 获取左侧内容
     *
     * @return 左侧内容
     */
    public String getLeftContent() {
        return etContent.getText().toString().trim();
    }

    public void setLeftContent(String s) {
        if (!TextUtils.isEmpty(s))
            etContent.setText(s);
    }

    public void setKeyFnText(String text) {
        ((TextView) findViewById(R.id.keyFn)).setText(text);
    }

    public interface OnKeyClickListener {
        void onKeyEnterClick();

        void onKeyFnClick(View key);

    }

    /**
     * Enter按键点击事件
     *
     * @param listener
     */
    public void setOnKeyClickListener(OnKeyClickListener listener) {
        this.listener = listener;
    }
}
