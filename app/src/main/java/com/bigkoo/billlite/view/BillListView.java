package com.bigkoo.billlite.view;

import com.bigkoo.katafoundation.mvpview.BaseListRAMView;
import com.bigkoo.kataframework.mvpview.BaseListView;

/**
 * Created by sai on 2018/4/30.
 */

public interface BillListView extends BaseListRAMView {
    void onDayGroupsChange(Object data);
}
