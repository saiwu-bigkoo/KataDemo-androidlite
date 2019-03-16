package com.bigkoo.commonui.textview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.bigkoo.commonui.R;

/**
 * Created by Sai on 2018/4/1.
 * 倒计时控件
 */
public class CountDownView extends AppCompatTextView{
    public static final long MAXCOUNT = 60000L;
    public static final long COUNTDOWNINTERVAL = 1000;
    private Long until = MAXCOUNT;
    private CodeCountDownTimer countDownTimer;
    public static final String SPTAG = "CountDownViewSP";
    public static final String SPTIMEKEY = "CountDownView_StartTime";

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onCountDownTimerStart(){
        //每次新倒计时要记录时间
        saveStartTime();
        onCountDownTimerContinueStart();
    }
    private void onCountDownTimerContinueStart(){
        setText(getContext().getApplicationContext().getString(R.string.countdown_s, String.valueOf(until/1000)));
        setEnabled(false);
        countDownTimer = new CodeCountDownTimer(until, COUNTDOWNINTERVAL);
        countDownTimer.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //因为进入界面肯定是开始倒计时，默认就不能点击，倒计时结束才能点
        setEnabled(false);
        initData();
    }

    private void initData() {
        //读取上一次开始时间，然后对比当前时间差，是否在MAXCOUNT以内。如果是则继续倒计时
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SPTAG, Context.MODE_PRIVATE);
        long previousStartTime = sharedPreferences.getLong(SPTIMEKEY,System.currentTimeMillis() - MAXCOUNT);
        long time = (System.currentTimeMillis() - previousStartTime);
        boolean isContinue = time < MAXCOUNT;
        until = isContinue ? MAXCOUNT - time : MAXCOUNT;
        //继续倒计时不用重新设定开始计时的时间，如果是新倒计时就要记录开始时间
        if(isContinue)
            onCountDownTimerContinueStart();
        else
            onCountDownTimerStart();
    }

    public static boolean isCountDowning(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPTAG, Context.MODE_PRIVATE);
        long previousStartTime = sharedPreferences.getLong(SPTIMEKEY,System.currentTimeMillis() - MAXCOUNT);
        long time = (System.currentTimeMillis() - previousStartTime);
        boolean isContinue = time < MAXCOUNT;
        return isContinue;
    }
    @Override
    protected void onDetachedFromWindow() {
        countDownTimer.cancel();
        super.onDetachedFromWindow();
    }

    private void saveStartTime() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SPTAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(SPTIMEKEY, System.currentTimeMillis());
        editor.commit();
    }

    class CodeCountDownTimer extends CountDownTimer {
        public CodeCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            until = millisUntilFinished;
            setText(getContext().getApplicationContext().getString(R.string.countdown_s, String.valueOf(until/1000)));
        }

        @Override
        public void onFinish() {
            until = MAXCOUNT;
            setText(getContext().getApplicationContext().getString(R.string.resendcode));
            setEnabled(true);
            cancel();
        }
    }

}
