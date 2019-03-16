package com.bigkoo.billlite.activity;

import androidx.recyclerview.widget.GridLayoutManager;

import android.text.TextUtils;
import android.view.View;

import com.bigkoo.billlite.R;
import com.bigkoo.billlite.bean.CategoryBean;
import com.bigkoo.billlite.presenter.CategoryAddPresenter;
import com.bigkoo.billlite.utils.ImageUtil;
import com.bigkoo.billlite.view.CategoryAddView;
import com.bigkoo.commonui.edittext.EditTextWithDelView;
import com.bigkoo.commonui.topbar.TopBarView;
import com.bigkoo.katafoundation.activity.BaseListActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * 添加分类页面
 *
 * @author wuhao
 */
public class CategoryAddActivity extends BaseListActivity<CategoryAddPresenter> implements CategoryAddView {

    private TopBarView topBarView;
    private EditTextWithDelView etCategory;

    private int type;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_categoryadd;
    }

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("tab", 0);
        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setType(type);
        getPresenter().setCategory(categoryBean);
        getPresenter().queryDeleteCategories();
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new BaseQuickAdapter<CategoryBean, BaseViewHolder>(R.layout.item_billadd_category) {
            @Override
            protected void convert(BaseViewHolder helper, CategoryBean category) {
                helper.setText(R.id.tvCategory, category.getName());
                helper.setImageResource(R.id.ivCategory, ImageUtil.getResId(category.getIcon()));
            }
        };
    }

    @Override
    protected void initView() {
        super.initView();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        topBarView = findViewById(R.id.topBarView);
        etCategory = findViewById(R.id.etCategory);
        topBarView.setRightText(getString(R.string.app_option_save));

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CategoryBean category = (CategoryBean) adapter.getData().get(position);
                category.setStatus(0);
                getPresenter().update(category);
                adapter.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void onTopBarRightClick(View view) {
        String name = etCategory.getText().toString();
        saveCategory(name);
    }

    void saveCategory(String name) {
        if (!TextUtils.isEmpty(name)) {
            CategoryBean category = new CategoryBean();
            category.setIcon("ic_category_define");//自定义的分类固定使用此图标
            category.setName(name);
            category.setType(type);
            category.setStatus(0);
            getPresenter().save(category);
        } else {
            showToast(R.string.categoryadd_empty);
        }
    }

    @Override
    public void onRefreshing(boolean refreshing) {

    }

    @Override
    public void onStatusEmpty(boolean statusEmpty) {

    }

    @Override
    public void onStatusLoading(boolean statusLoading) {

    }

    @Override
    public void onStatusError(boolean statusError, int code, String msg) {
        if (statusError) {
            showToast(msg);
        }
    }

    @Override
    public void onStatusNetworkError(boolean statusNetworkError, String msg) {

    }


    @Override
    public void onDataSetChange(Object data) {

    }

    @Override
    public void onLoadComplete() {

    }

    @Override
    public void onLoadingMore(boolean isLoadingMore) {

    }

    @Override
    public void onSave() {
        finish();
    }

    @Override
    public void onQueryDeleteCategories(ArrayList<CategoryBean> categories) {
        if (categories.size() != 0){
            adapter.setNewData(categories);
            findViewById(R.id.deleteCategoriesView).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.deleteCategoriesView).setVisibility(View.GONE);
        }
    }
}
