package com.example.newspaper.admin.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newspaper.databinding.FragmentReportBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class ReportFragment extends Fragment {

    private FragmentReportBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout bằng View Binding
        binding = FragmentReportBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Thiết lập dữ liệu hiển thị
        binding.viewCountText.setText("120,000");
        binding.articleCountText.setText("3,200");

        // Gọi các hàm setup biểu đồ
        setupBarChart();
        setupPieChart();
        setupLineChart();
    }

    private void setupBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 12000));
        entries.add(new BarEntry(1, 9500));
        entries.add(new BarEntry(2, 7200));
        entries.add(new BarEntry(3, 4600));
        entries.add(new BarEntry(4, 3100));

        BarDataSet dataSet = new BarDataSet(entries, "Lượt xem theo chuyên mục");
        dataSet.setColor(Color.parseColor("#E0F7EC"));

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        BarChart barChart = binding.barChart;
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }

    private void setupPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40f, "Tin mới"));
        entries.add(new PieEntry(25f, "Điểm tin"));
        entries.add(new PieEntry(20f, "Kinh doanh"));
        entries.add(new PieEntry(15f, "Khác"));

        PieDataSet dataSet = new PieDataSet(entries, "Tỷ lệ chuyên mục");
        dataSet.setColors(new int[]{
                Color.parseColor("#A5D6A7"),
                Color.parseColor("#FFECB3"),
                Color.parseColor("#90CAF9"),
                Color.parseColor("#FFCCBC")
        });

        PieData pieData = new PieData(dataSet);

        PieChart pieChart = binding.pieChart;
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void setupLineChart() {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 5000));
        entries.add(new Entry(2, 8000));
        entries.add(new Entry(3, 7500));
        entries.add(new Entry(4, 11000));
        entries.add(new Entry(5, 9500));

        LineDataSet dataSet = new LineDataSet(entries, "Lượt xem theo tháng");
        dataSet.setColor(Color.parseColor("#0bd18a"));
        dataSet.setCircleColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);

        LineChart lineChart = binding.lineChart;
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.animateX(1000);
        lineChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
