package com.bigkoo.billlite.listener;

/**
 * Created by sai on 2018/4/30.
 */

public interface OnSwipedSortListener {
    void onItemChange(int fromPosition, int toPosition);
    void onItemSwipe(int position);
}
