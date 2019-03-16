package com.bigkoo.billlite.view;

import com.bigkoo.billlite.bean.CategoryBean;
import com.bigkoo.kataframework.mvpview.BaseListView;

import java.util.ArrayList;

public interface CategoryAddView extends BaseListView {
    void onSave();
    void onQueryDeleteCategories(ArrayList<CategoryBean> categories);
}
