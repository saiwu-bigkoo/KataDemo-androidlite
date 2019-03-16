package com.bigkoo.commonui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.bigkoo.commonui.R;

/**
 * Created by Sai on 2018/4/16.
 * 分享的dialog
 */

public class ShareDialogFragment extends BaseBottomDialogFragment {
    public static final int CANCEL = -1;
    public static final int WECHAT = 0;
    public static final int WECHATMOMENTS = 1;
    public static final int QQ = 2;
    public static final int SINAWEIBO = 3;
    public static final int COLLECT = 4;
    public static final int REPORT = 5;
    public static final int COPYLINK = 6;
    public static final int SHAREDIALOG_TYPE_NORMAL = 1;
    public static final int SHAREDIALOG_TYPE_MORE = 2;
    private IShareItemClickListener itemClickListener;

    public static ShareDialogFragment newInstance(final IShareItemClickListener itemClickListener, final int shareDialogType) {
        ShareDialogFragment dialogFragment = newInstance(new OnCallDialog() {
            @Override
            public Dialog getDialog(Context context) {
                Dialog dialog = new Dialog(context, R.style.BottomDialogStyle);
                dialog.setContentView(R.layout.dialog_share);
                init(dialog, itemClickListener,shareDialogType);
                return dialog;
            }
        }, true, null);
        return dialogFragment;
    }

    public static ShareDialogFragment newInstance(OnCallDialog call, boolean cancelable, OnDialogCancelListener cancelListener) {
        ShareDialogFragment instance = new ShareDialogFragment();
        instance.setCancelable(cancelable);
        instance.onCancelListener = cancelListener;
        instance.onCallDialog = call;
        return instance;
    }


    private static void init(Dialog dialog, final IShareItemClickListener itemClickListener, int shareDialogType) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Integer.valueOf(String.valueOf(v.getTag()));
                itemClickListener.onItemClick(index);
            }
        };
        View tvShareWechat = dialog
                .findViewById(R.id.tvShareWechat);
        View tvShareWechatMoments = dialog
                .findViewById(R.id.tvShareWechatMoments);
        View tvShareQQ = dialog
                .findViewById(R.id.tvShareQQ);
        View tvShareSinaWeibo = dialog
                .findViewById(R.id.tvShareSinaWeibo);
        View tvShareCollect = dialog
                .findViewById(R.id.tvShareCollect);
        View tvShareReport = dialog
                .findViewById(R.id.tvShareReport);
        View tvShareCopyLink = dialog
                .findViewById(R.id.tvShareCopyLink);
        View tvCancel = dialog
                .findViewById(R.id.tvCancel);

        tvShareWechat.setOnClickListener(onClickListener);
        tvShareWechatMoments.setOnClickListener(onClickListener);
        tvShareQQ.setOnClickListener(onClickListener);
        tvShareSinaWeibo.setOnClickListener(onClickListener);
        tvShareCollect.setOnClickListener(onClickListener);
        tvShareReport.setOnClickListener(onClickListener);
        tvShareCopyLink.setOnClickListener(onClickListener);
        tvCancel.setOnClickListener(onClickListener);

        if(shareDialogType == SHAREDIALOG_TYPE_MORE){
            View vMoreDivider = dialog
                    .findViewById(R.id.vMoreDivider);
            View loMore = dialog
                    .findViewById(R.id.loMore);

            vMoreDivider.setVisibility(View.VISIBLE);
            loMore.setVisibility(View.VISIBLE);
        }
    }

    private static void initShareDialogType(int shareDialogType) {


    }

    public IShareItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(IShareItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface IShareItemClickListener{
        void onItemClick(int index);
    }
}
