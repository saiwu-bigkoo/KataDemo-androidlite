package com.bigkoo.commonui.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.widget.TextView;

import com.bigkoo.commonui.R;

/**
 * Created by Sai on 2018/4/12.
 * Dialog通用类，不同的dialog用不同的Tag，在此类里面拓展不同类型
 */

public class DialogUtil {
    public static void dismissDialog(AppCompatActivity context,String tag){
        Fragment fragment = context.getSupportFragmentManager().findFragmentByTag(tag);
        BaseDialogFragment dialogFragment;
        if(fragment!=null){
            dialogFragment = (BaseDialogFragment) fragment;
            if(dialogFragment.isShowing())
                dialogFragment.dismiss();
        }
    }

    //loading----------------------------------------------------------
    public static String TAG_LOADING = "loading";
    public static void showLoadingDialog(AppCompatActivity context, final int msg) {
        showLoadingDialog(context, context.getApplicationContext().getString(msg));
    }
    public static void showLoadingDialog(AppCompatActivity context, final String msg) {
        Fragment fragment = context.getSupportFragmentManager().findFragmentByTag(TAG_LOADING);
        BaseDialogFragment dialogFragment;
        if(fragment!=null){
            dialogFragment = (BaseDialogFragment) fragment;
        }
        else {
            dialogFragment = BaseDialogFragment.newInstance(new BaseDialogFragment.OnCallDialog() {
                @Override
                public Dialog getDialog(Context context) {
                    Dialog dialog = new Dialog(context, R.style.ProgressDialogStyle);
                    dialog.setContentView(R.layout.dialog_custom_loading);

                    TextView text = dialog
                            .findViewById(R.id.tvLoadingMsg);
                    text.setText(msg);
                    return dialog;
                }
            }, false);
        }
        if(!dialogFragment.isShowing())
            dialogFragment.show(context.getSupportFragmentManager(),TAG_LOADING);
    }
    public static void dismissLoadingDialog(AppCompatActivity context){
        dismissDialog(context, TAG_LOADING);
    }
    //share----------------------------------------------------------
    public static String TAG_SHARE = "shareDialog";
    public static void showShareDialog(AppCompatActivity context, ShareDialogFragment.IShareItemClickListener itemClickListener) {
        showShareDialog(context, itemClickListener, ShareDialogFragment.SHAREDIALOG_TYPE_NORMAL);
    }
    public static void showShareMoreDialog(AppCompatActivity context, ShareDialogFragment.IShareItemClickListener itemClickListener) {
        showShareDialog(context, itemClickListener, ShareDialogFragment.SHAREDIALOG_TYPE_MORE);
    }
    private static void showShareDialog(AppCompatActivity context, ShareDialogFragment.IShareItemClickListener itemClickListener, int sharedialogType) {
        Fragment fragment = context.getSupportFragmentManager().findFragmentByTag(TAG_SHARE);
        BaseDialogFragment dialogFragment;
        if(fragment!=null){
            dialogFragment = (BaseDialogFragment) fragment;
        }
        else {
            dialogFragment = ShareDialogFragment.newInstance(itemClickListener, sharedialogType);
        }
        if(!dialogFragment.isShowing())
            dialogFragment.show(context.getSupportFragmentManager(),TAG_SHARE);
    }
    public static void dismissShareDialog(AppCompatActivity context){
        dismissDialog(context, TAG_SHARE);
    }
}
