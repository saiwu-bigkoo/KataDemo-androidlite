package com.bigkoo.billlite.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.billlite.R;
import com.bigkoo.billlite.bean.BillBean;
import com.bigkoo.billlite.bean.DayGroupBean;
import com.bigkoo.billlite.constants.Constants;
import com.bigkoo.billlite.utils.DateUtil;
import com.bigkoo.billlite.utils.ImageUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by sai on 2018/4/30.
 */

public class BillGroupByDateAdapter extends BaseQuickAdapter<BillBean, BaseViewHolder> implements StickyRecyclerHeadersAdapter {
    private StickyRecyclerHeadersDecoration decoration;
    private List<DayGroupBean> dayGroups;
    private SparseArray<String> headerSparseArray;

    private Context context;

    public BillGroupByDateAdapter(Context context) {
        super(R.layout.item_billgroup_detail);
        decoration = new StickyRecyclerHeadersDecoration(this);
        headerSparseArray = new SparseArray<>();
        this.context = context;
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                decoration.invalidateHeaders();
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, BillBean item) {

        helper.setImageResource(R.id.ivCategory, ImageUtil.getResId(item.getIcon()));
        //helper.setBackgroundRes(R.id.tvRemark, R.mipmap.ic_launcher);
        helper.setText(R.id.tvRemark, item.getRemark());
        switch (item.getBillType()) {
            case Constants.TYPE_INCOME:
                helper.setText(R.id.tvMoney, String.valueOf(item.getIncome()));
                break;
            case Constants.TYPE_PAY:
                helper.setText(R.id.tvMoney, "-" + item.getPay());
                break;
        }
        helper.addOnClickListener(R.id.loItem);
    }

    @Override
    public long getHeaderId(int position) {
        BillBean item = getItem(position);
        if (item == null) {
            return -1;
        }
        return DateUtil.parseDate(item.getDate(), DateUtil.DF_YYYY_MM_DD).getTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_billgroupbydate_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        int headerPosition = getHeaderPosition(getItem(position));
        if (headerPosition < 0) {
            //没有数据，返回
            return;
        }

        DayGroupBean dayGroup = dayGroups.get(headerPosition);
        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);

        //只显示日月，不显示年
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日", Locale.CHINA);
        tvDate.setText(format.format(DateUtil.parseDate(dayGroup.getDate(), DateUtil.DF_YYYY_MM_DD)));

        TextView tvMoney = holder.itemView.findViewById(R.id.tvMoney);
        String sMoneyFormat = "";
        double income = dayGroup.getIncome();
        double pay = dayGroup.getPay();
        //哪个为0哪个不显示
        if (0 == income) {
            sMoneyFormat = context.getResources().getString(R.string.main_list_pay);
            tvMoney.setText(String.format(sMoneyFormat, pay));
        } else if (0 == pay) {
            sMoneyFormat = context.getResources().getString(R.string.main_list_income);
            tvMoney.setText(String.format(sMoneyFormat, income));
        } else {
            sMoneyFormat = context.getResources().getString(R.string.main_list_income_and_pay);
            tvMoney.setText(String.format(sMoneyFormat, income, pay));
        }
    }

    public List<DayGroupBean> getDayGroups() {
        return dayGroups;
    }

    public void setDayGroups(List<DayGroupBean> dayGroups) {
        this.dayGroups = dayGroups;
    }

    public StickyRecyclerHeadersDecoration getDecoration() {
        return decoration;
    }

    public int getHeaderPosition(BillBean bill) {
        try {
            int position = headerSparseArray.indexOfValue(bill.getDate());
            if (position < 0) {
                position = 0;
                for (DayGroupBean dayGroup : dayGroups) {
                    if (dayGroup.getDate().equals(bill.getDate())) {
                        headerSparseArray.put(position, bill.getDate());
                        return position;
                    }
                    position++;
                }
            }
        } catch (NullPointerException e) {
        }
        return -1;
    }
}
