package com.bigkoo.billlite.fragment;

import androidx.recyclerview.widget.ItemTouchHelper;

import com.bigkoo.billlite.R;
import com.bigkoo.billlite.adapter.SwipedSortAdapter;
import com.bigkoo.billlite.bean.CategoryBean;
import com.bigkoo.billlite.bean.CategoryEvent;
import com.bigkoo.billlite.helper.SwipedSortItemTouchHelperCallback;
import com.bigkoo.billlite.listener.OnSwipedSortListener;
import com.bigkoo.billlite.presenter.CategoryManagePresenter;
import com.bigkoo.billlite.utils.ImageUtil;
import com.bigkoo.billlite.view.CategoryManageView;
import com.bigkoo.katafoundation.fragment.BaseListFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 分类管理视图，收入和支出共用，通过Type参数区分
 *
 * @author wuhao
 */
public class CategoryManageFragment extends BaseListFragment<CategoryManagePresenter>
        implements CategoryManageView, OnSwipedSortListener {
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_categorymanage;
    }

    @Override
    protected void initView() {
        super.initView();
        ItemTouchHelper.Callback callback = new SwipedSortItemTouchHelperCallback((SwipedSortAdapter) adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        ((SwipedSortAdapter) adapter).setItemTouchHelperExtension(itemTouchHelper);
    }

    @Override
    protected void initData() {
        //设置收入还是支出
        getPresenter().setType(Integer.valueOf(getTag()));
        getPresenter().onLoadData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        getPresenter().toObservable(CategoryEvent.class, new Consumer<CategoryEvent>() {
            @Override
            public void accept(CategoryEvent categoryEvent) throws Exception {
                if (categoryEvent.getEventType() == CategoryEvent.ADD) {
                    adapter.addData(categoryEvent.getCategoryBean());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemChange(int fromPosition, int toPosition) {
        //分类排序交换
        CategoryBean from = (CategoryBean) adapter.getData().get(fromPosition);
        from.setSort(toPosition);
        CategoryBean to = (CategoryBean) adapter.getData().get(toPosition);
        to.setSort(fromPosition);
        ArrayList<CategoryBean> changes = new ArrayList<>();
        changes.add(from);
        changes.add(to);
        getPresenter().onUpdate(changes);
        Logger.d("交换位置："+new Gson().toJson(changes));
    }

    @Override
    public void onItemSwipe(int position) {
        CategoryBean category = (CategoryBean) adapter.getItem(position);
        getPresenter().onDelete(category);
        Logger.d("删除数据:" + new Gson().toJson(adapter.getData().get(position)));
        adapter.remove(position);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new SwipedSortAdapter<CategoryBean>(R.layout.item_categorymanage, this) {
            @Override
            protected void convert(final BaseViewHolder viewHolder, CategoryBean item) {
                viewHolder.addOnClickListener(R.id.loContent);
                viewHolder.setText(R.id.tvCategory,
                        item.getName()).setImageResource(R.id.ivCategoryIcon, ImageUtil.getResId(item.getIcon()));
            }
        };
    }

    @Override
    public void onDataSetChange(Object data) {
        adapter.setNewData((List) data);
    }

    @Override
    public void onLoadComplete() {

    }

    @Override
    public void onLoadingMore(boolean isLoadingMore) {

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

    }

    @Override
    public void onStatusNetworkError(boolean statusNetworkError, String msg) {

    }
}
