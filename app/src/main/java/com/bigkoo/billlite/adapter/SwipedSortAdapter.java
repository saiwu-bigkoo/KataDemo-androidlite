package com.bigkoo.billlite.adapter;

import androidx.recyclerview.widget.ItemTouchHelper;

import com.bigkoo.billlite.listener.OnSwipedSortListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Collections;

/**
 * Created by sai on 2018/4/30.
 * 拖动排序的adapter
 */

public abstract class SwipedSortAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    private OnSwipedSortListener listener;
    protected ItemTouchHelper itemTouchHelper;

    public SwipedSortAdapter(int layoutResId, OnSwipedSortListener listener) {
        super(layoutResId);
        this.listener = listener;
    }

    public void setItemTouchHelperExtension(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    public void move(int fromPosition, int toPosition) {
        if (fromPosition < mData.size() && toPosition < mData.size()) {
            //交换数据位置
            if (listener != null) {
                listener.onItemChange(fromPosition, toPosition);
            }
            Collections.swap(mData, fromPosition, toPosition);
            //刷新位置交换
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    public void swipe(int position) {
        if (listener != null) {
            listener.onItemSwipe(position);
        }
        notifyItemRemoved(position);
    }

}
