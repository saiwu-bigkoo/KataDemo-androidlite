package com.bigkoo.commonui.dialog;

import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Sai on 2018/4/19.
 */

public class BaseBottomDialogFragment extends BaseDialogFragment{
    protected void setWindowParams() {
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.getDecorView().setPadding(0,0, 0, 0);
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;
        window.setAttributes(windowParams);
    }

}
