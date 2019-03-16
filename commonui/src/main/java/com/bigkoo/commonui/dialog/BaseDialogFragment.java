package com.bigkoo.commonui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Sai on 2018/4/12.
 * 通用的Dialog
 */

public class BaseDialogFragment extends AppCompatDialogFragment {
    /**
     * 监听弹出窗是否被取消
     */
    protected OnDialogCancelListener onCancelListener;

    /**
     * 回调获得需要显示的dialog
     */
    protected OnCallDialog onCallDialog;

    public interface OnDialogCancelListener {
        void onCancel();
    }

    public interface OnCallDialog {
        Dialog getDialog(Context context);
    }

    public static BaseDialogFragment newInstance(OnCallDialog call, boolean cancelable) {
        return newInstance(call, cancelable, null);
    }

    public static BaseDialogFragment newInstance(OnCallDialog call, boolean cancelable, OnDialogCancelListener cancelListener) {
        BaseDialogFragment instance = new BaseDialogFragment();
        instance.setCancelable(cancelable);
        instance.onCancelListener = cancelListener;
        instance.onCallDialog = call;
        return instance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (null == onCallDialog) {
            super.onCreateDialog(savedInstanceState);
        }
        return onCallDialog.getDialog(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            setWindowParams();
        }
    }

    protected void setWindowParams() {
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowParams = window.getAttributes();
//        windowParams.dimAmount = 0.0f;
        window.setAttributes(windowParams);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (onCancelListener != null) {
            onCancelListener.onCancel();
        }
    }

    public boolean isShowing(){
        boolean isShowing = false;
        if(getDialog() !=null)
            isShowing = getDialog().isShowing();
        return isShowing;
    }

}