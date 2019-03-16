package com.bigkoo.billlite.view;

import com.bigkoo.billlite.bean.CategoryBean;
import com.bigkoo.kataframework.mvpview.BaseDetailView;

/**
 * Created by sai on 2018/5/12.
 */

public interface BillDetailView extends BaseDetailView{
    void onDeleteComplete();
    void onQueryCategroy(CategoryBean categoryBean);
}
