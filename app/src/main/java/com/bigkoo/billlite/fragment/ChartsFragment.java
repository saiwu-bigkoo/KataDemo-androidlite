package com.bigkoo.billlite.fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.bigkoo.billlite.R;
import com.bigkoo.billlite.bean.BillBean;
import com.bigkoo.billlite.bean.DayGroupBean;
import com.bigkoo.billlite.constants.Constants;
import com.bigkoo.billlite.presenter.ChartsPresenter;
import com.bigkoo.billlite.utils.DateUtil;
import com.bigkoo.billlite.utils.PieChartValueFormatter;
import com.bigkoo.billlite.view.ChartsView;
import com.bigkoo.katafoundation.fragment.BaseDetailFragment;
import com.bigkoo.katafoundation.fragment.BaseFragment;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 周统计视图
 *
 * @author wuhao
 */
public class ChartsFragment extends BaseDetailFragment<ChartsPresenter> implements ChartsView {

    public static String KEY_FORMAT = "format";
    private PieChart payPieChart;//支出饼状图
    private PieChart incomePieChart;//收入饼状图
    private LineChart lineChart;//收支曲线图

    private ArrayList<Entry> yLineChartIncome;
    private ArrayList<Entry> yLineChartPay;

    private int tag;

    public static ChartsFragment newInstance(String format) {
        Bundle args = new Bundle();
        args.putString(KEY_FORMAT, format);
        ChartsFragment fragment = new ChartsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_charts;
    }

    @Override
    protected void initView() {
        lineChart = findViewById(R.id.lineChart);
        payPieChart = findViewById(R.id.payPieChart);
        incomePieChart = findViewById(R.id.incomePieChart);
        initLineChart();
        initPieChart(payPieChart);
        initPieChart(incomePieChart);
    }

    @Override
    protected void initData() {
        tag = Integer.parseInt(getTag());
        String format = getArguments().getString(KEY_FORMAT);
        getPresenter().setFormat(format);
        switch (tag) {
            case Constants.WEEK:
                getPresenter().lineChartForWeek(DateUtil.formatDateTime(new Date(), DateUtil.DF_YYYY_MM));
                break;
            case Constants.MONTH:
                getPresenter().lineChartForMonth(DateUtil.formatDateTime(new Date(), DateUtil.DF_YYYY_MM));
                break;
            case Constants.YEAR:
                break;
        }

        initPie();
    }

    private void initPie() {
        int[] payPieColor = {
                //灰，浅绿，橙，蓝，绿
                Color.rgb(181, 194, 202), Color.rgb(129, 216, 200),
                Color.rgb(241, 214, 145), Color.rgb(108, 176, 223),
                Color.rgb(195, 221, 155)
        };
        HashMap pay = getPresenter().getMockPieChartData(Constants.TYPE_PAY);
        HashMap income = getPresenter().getMockPieChartData(Constants.TYPE_INCOME);
        drawPie(payPieChart, pay, payPieColor);
        drawPie(incomePieChart, income, payPieColor);
    }

    /**
     * 加载曲线图数据
     */
    private void initLine() {
//        //演示数据
//        ArrayList<Entry> yLineChartIncome = getPresenter().getMockLineChartData(7, 50);
//        ArrayList<Entry> yLineChartPay = getPresenter().getMockLineChartData(7, 200);

        //收入曲线
        LineDataSet incomeLine = drawSingleLine(yLineChartIncome, getString(R.string.charts_income_desc), 0xFF00B770);
        //支出曲线
        LineDataSet payLine = drawSingleLine(yLineChartPay, getString(R.string.charts_pay_desc), 0xFFFF0000);

        drawLines(lineChart, incomeLine, payLine);

        //x轴数据
        final Map<Integer, String> xMap = new HashMap<>();
        String[] valueArry;
        switch (tag) {
            case Constants.WEEK:
                valueArry = new String[]{"今天", "昨天", "前天", "前2天", "前3天", "前4天", "前5天"};
                break;
            case Constants.MONTH:
                valueArry = new String[31];
                for (int i = 1; i <= 31; i++) {
                    valueArry[i - 1] = "" + i;
                }
                break;
            case Constants.YEAR:
                valueArry = new String[12];
                for (int i = 1; i <= 12; i++) {
                    valueArry[i - 1] = i + "月";
                }
                break;
            default:
                valueArry = new String[0];
                break;
        }
        for (int i = 0; i < yLineChartIncome.size(); i++) {
            xMap.put((int) yLineChartIncome.get(i).getX(), valueArry[i]);
        }
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xMap.get((int) value);
            }
        });
    }

    /**
     * 配置曲线图轮廓
     */
    private void initLineChart() {
        lineChart.setTouchEnabled(false);//设置chart不可触摸
        lineChart.setDrawGridBackground(false);//不设置图表背景
        lineChart.setDescription(null);//不显示图标描述
        //lineChart.setTouchEnabled(true);
        //lineChart.setDragEnabled(true);
        lineChart.setNoDataText(getString(R.string.charts_nodata));//没有数据时显示的文本
        lineChart.getXAxis().setDrawGridLines(false);//不显示X轴网格线
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//x轴位置
        lineChart.getAxis(YAxis.AxisDependency.LEFT).setEnabled(false);//不显示Y轴
        lineChart.getAxis(YAxis.AxisDependency.RIGHT).setEnabled(false);
        lineChart.animateX(1000, Easing.EasingOption.EaseInOutQuad); //配置加载动画
        lineChart.invalidate();
    }

    /**
     * 绘制单条曲线
     *
     * @param yAxisValues 曲线顶点坐标
     * @param label       曲线标签
     * @param color       曲线颜色
     * @return 绘制好的曲线
     */
    public LineDataSet drawSingleLine(ArrayList<Entry> yAxisValues, String label, int color) {
        LineDataSet line;
        line = new LineDataSet(yAxisValues, label);
        line.setCubicIntensity(1f);
        line.setColor(color);
        line.setCircleColor(color);
        line.setLineWidth(1f);
        line.setCircleRadius(3f);
        line.setValueTextColor(color);
        line.setValueTextSize(12f);
        line.notifyDataSetChanged();
        return line;
    }

    /**
     * 绘制多条曲线到图表
     *
     * @param lineChart 绘制曲线的图表
     * @param lines     绘制的曲线
     */
    public void drawLines(LineChart lineChart, LineDataSet... lines) {
        LineData lineData = new LineData();
        for (LineDataSet line : lines) {
            lineData.addDataSet(line);
        }
        lineData.notifyDataChanged();
        lineChart.setData(lineData);
        lineChart.notifyDataSetChanged();
    }

    /**
     * 配置饼图轮廓
     *
     * @param pieChart 需要配置的饼图
     */
    public void initPieChart(PieChart pieChart) {
        pieChart.setExtraOffsets(25, 10, 25, 25); //设置边距
        pieChart.setRotationEnabled(true);//是否可以旋转
        pieChart.setHighlightPerTapEnabled(true);//点击是否放大
        pieChart.setDrawHoleEnabled(true);//设置为环形图
        pieChart.setHoleRadius(40f);//设置PieChart内部圆的半径
        pieChart.setTransparentCircleRadius(50f);//设置内部透明圆的半径
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setHoleColor(Color.WHITE);//设置内部圆的颜色
        pieChart.setTransparentCircleAlpha(100);//设置内部透明圆与内部圆间距透明度[0~255]数值越小越透明
        SpannableString spanString;
        if (pieChart.getId() == R.id.incomePieChart) {
            spanString = new SpannableString(getString(R.string.charts_income_top5));
        } else {
            spanString = new SpannableString(getString(R.string.charts_pay_top5));
        }
        spanString.setSpan(new RelativeSizeSpan(1.5f), 0, spanString.length() - 4,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        pieChart.setCenterText(spanString);//设置内部圆文字的内容
        pieChart.setCenterTextSize(16f);//设置环中文字的大小
        pieChart.getLegend().setEnabled(false);//不显示图例设置
        pieChart.setDescription(null);//不显示图标描述
        pieChart.setNoDataText(getString(R.string.charts_nodata));//设置没有数据时显示的文本
        pieChart.animateX(1000, Easing.EasingOption.EaseInOutQuad); //加载动画
        pieChart.invalidate();//将图表重绘以显示设置的属性和数据
    }

    /**
     * 绘制饼块
     *
     * @param pieChart  需要绘制的饼图
     * @param pieValues 饼块数据
     * @param colors    饼块颜色
     */
    private void drawPie(PieChart pieChart, Map<String, Float> pieValues, int[] colors) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        Set set = pieValues.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            entries.add(new PieEntry(Float.valueOf(entry.getValue().toString()), entry.getKey().toString()));
        }
        PieDataSet pie = new PieDataSet(entries, "");
        pie.setSliceSpace(0f);//设置饼块之间的间隔
        pie.setSelectionShift(10f);//设置饼块选中时偏离饼图中心的距离
        pie.setColors(colors);//设置饼块的颜色
        //设置数据显示方式
        pie.setValueLinePart1OffsetPercentage(80f);//数据连接线距图形片内部边界的距离，为百分数
        pie.setValueLinePart1Length(0.3f);//斜线的长度
        pie.setValueLinePart2Length(0.8f);//横线的长度
        pie.setValueLineColor(Color.DKGRAY);//设置连接线的颜色
        pie.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pies = new PieData(pie);
        pies.setValueFormatter(new PercentFormatter());
        //显示2位小数，单位为元
        pies.setValueFormatter(new PieChartValueFormatter(2, getString(R.string.charts_data_unit)));
        pies.setValueTextSize(12f);
        pies.setValueTextColor(Color.DKGRAY);
        pieChart.setData(pies);
        pieChart.invalidate();
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

    @Override
    public void onDataSetChange(Object data) {

    }

    @Override
    public void onLoadComplete() {


    }

    @Override
    public void onLineChartDataSetChange(Object data) {
        yLineChartIncome = new ArrayList<>();
        yLineChartPay = new ArrayList<>();
        List<DayGroupBean> datas = (List<DayGroupBean>) data;
        for (int i = 0; i < datas.size(); i++) {
            DayGroupBean item = datas.get(i);
            Entry income = new Entry(yLineChartIncome.size(), (float) item.getIncome());
            yLineChartIncome.add(income);
            Entry pay = new Entry(yLineChartPay.size(), (float) item.getPay());
            yLineChartPay.add(pay);
        }
        initLine();
    }
}
