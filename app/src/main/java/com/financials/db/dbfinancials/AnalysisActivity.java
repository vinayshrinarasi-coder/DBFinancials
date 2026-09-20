package com.financials.db.dbfinancials;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AnalysisActivity extends AppCompatActivity {

    private PieChart bankPieChart;
    private PieChart categoryPieChart;
    private BarChart amountBarChart;
    private BarChart interestBarChart;
    private DatabaseHandler db;

    private static class IndianCurrencyFormatter extends ValueFormatter {
        private float totalAmount = 0f;

        public IndianCurrencyFormatter() {
        }

        public IndianCurrencyFormatter(float totalAmount) {
            this.totalAmount = totalAmount;
        }

        @Override
        public String getPieLabel(float value, PieEntry entry) {
            String amountStr = formatIndianCurrency((long) value);
            String label = entry.getLabel();
            if (totalAmount > 0) {
                float percentage = (value / totalAmount) * 100f;
                return String.format(Locale.getDefault(), "%s (%s)(%.1f%%)", amountStr, label, percentage);
            }
            return String.format("%s (%s)", amountStr, label);
        }

        @Override
        public String getFormattedValue(float value) {
            return formatIndianCurrency((long) value);
        }

        private String formatIndianCurrency(long amount) {
            StringBuilder sb = new StringBuilder();
            String amountStr = String.valueOf(amount);
            int len = amountStr.length();
            if (len <= 3) return amountStr;
            
            sb.append(amountStr.substring(len - 3));
            int i = len - 3;
            while (i > 0) {
                sb.insert(0, ",");
                int start = Math.max(0, i - 2);
                sb.insert(0, amountStr.substring(start, i));
                i -= 2;
            }
            return sb.toString();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Financial Analysis");
        }

        bankPieChart = findViewById(R.id.bankPieChart);
        categoryPieChart = findViewById(R.id.categoryPieChart);
        amountBarChart = findViewById(R.id.amountBarChart);
        interestBarChart = findViewById(R.id.interestBarChart);

        db = new DatabaseHandler(this);
        List<Policy> policies = db.getAllPolicies();

        setupCategoryPieChart(policies);
        setupBankPieChart(policies);
        setupAmountBarChart(policies);
        setupInterestBarChart(policies);
    }

    private void setupBankPieChart(List<Policy> policies) {
        Map<String, Double> bankDeposits = new HashMap<>();
        float totalAmount = 0f;
        for (Policy p : policies) {
            String bank = p.getBankName();
            if (bank == null || bank.isEmpty()) bank = "Unknown";
            double amount = p.getDepositAmount();
            bankDeposits.put(bank, bankDeposits.getOrDefault(bank, 0.0) + amount);
            totalAmount += (float) amount;
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : bankDeposits.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(8f); // Smaller text to fit more info
        dataSet.setValueFormatter(new IndianCurrencyFormatter(totalAmount));
        
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(85f);
        dataSet.setValueLinePart1Length(0.6f);
        dataSet.setValueLinePart2Length(0.8f);
        dataSet.setValueLineColor(Color.BLACK);
        dataSet.setUsingSliceColorAsValueLineColor(true); // Matches line with slice color for clarity

        PieData data = new PieData(dataSet);
        bankPieChart.setData(data);
        bankPieChart.getDescription().setEnabled(false);
        bankPieChart.setCenterText("Deposits by Bank");
        bankPieChart.setDrawEntryLabels(false); 
        
        com.github.mikephil.charting.components.Legend l = bankPieChart.getLegend();
        l.setVerticalAlignment(com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(com.github.mikephil.charting.components.Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setWordWrapEnabled(true);
        l.setXEntrySpace(8f);
        l.setYEntrySpace(5f);
        l.setTextSize(9f);
        l.setFormSize(9f);
        l.setMaxSizePercent(0.95f); 

        // Significant bottom offset for legend wrap
        bankPieChart.setExtraOffsets(35, 5, 35, 100); 
        bankPieChart.setHoleRadius(35f);
        bankPieChart.setTransparentCircleRadius(40f);

        bankPieChart.animateY(1000);
        bankPieChart.invalidate();
    }

    private void setupCategoryPieChart(List<Policy> policies) {
        Map<String, Double> categoryDeposits = new HashMap<>();
        float totalAmount = 0f;
        for (Policy p : policies) {
            String category = p.getCategory();
            if (category == null || category.isEmpty()) category = "Bank";
            double amount = p.getDepositAmount();
            categoryDeposits.put(category, categoryDeposits.getOrDefault(category, 0.0) + amount);
            totalAmount += (float) amount;
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryDeposits.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);
        dataSet.setValueFormatter(new IndianCurrencyFormatter(totalAmount));

        PieData data = new PieData(dataSet);
        categoryPieChart.setData(data);
        categoryPieChart.getDescription().setEnabled(false);
        categoryPieChart.setCenterText("By Category");
        categoryPieChart.setDrawEntryLabels(true);
        categoryPieChart.setEntryLabelColor(Color.BLACK);

        categoryPieChart.animateY(1000);
        categoryPieChart.invalidate();
    }

    private void setupAmountBarChart(List<Policy> policies) {
        double totalDeposit = 0;
        double totalMaturity = 0;

        for (Policy p : policies) {
            totalDeposit += p.getDepositAmount();
            totalMaturity += p.getMaturityAmount();
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, (float) totalDeposit));
        entries.add(new BarEntry(1, (float) totalMaturity));

        BarDataSet dataSet = new BarDataSet(entries, "Amounts");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS[0], ColorTemplate.MATERIAL_COLORS[1]);
        dataSet.setStackLabels(new String[]{"Total Deposit", "Total Maturity"});
        dataSet.setValueFormatter(new IndianCurrencyFormatter());

        BarData data = new BarData(dataSet);
        amountBarChart.setData(data);
        amountBarChart.getDescription().setEnabled(false);
        
        XAxis xAxis = amountBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Deposit", "Maturity"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        amountBarChart.getAxisLeft().setValueFormatter(new IndianCurrencyFormatter());
        amountBarChart.getAxisRight().setEnabled(false);

        amountBarChart.animateY(1000);
        amountBarChart.invalidate();
    }

    private void setupInterestBarChart(List<Policy> policies) {
        // Group by interest rate ranges or just show top 5-10 policies?
        // Let's show interest rates for the first 10 policies for simplicity or average per bank
        Map<String, List<Double>> bankInterests = new HashMap<>();
        for (Policy p : policies) {
            String bank = p.getBankName();
            if (bank == null || bank.isEmpty()) bank = "Unknown";
            bankInterests.computeIfAbsent(bank, k -> new ArrayList<>()).add(p.getRateOfInterest());
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int index = 0;
        for (Map.Entry<String, List<Double>> entry : bankInterests.entrySet()) {
            double avgInterest = 0;
            for (Double val : entry.getValue()) avgInterest += val;
            avgInterest /= entry.getValue().size();

            entries.add(new BarEntry(index, (float) avgInterest));
            labels.add(entry.getKey());
            index++;
            if (index >= 5) break; // Limit to 5 banks for readability
        }

        BarDataSet dataSet = new BarDataSet(entries, "Avg Interest Rate (%)");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);

        BarData data = new BarData(dataSet);
        interestBarChart.setData(data);
        interestBarChart.getDescription().setEnabled(false);

        XAxis xAxis = interestBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-45);

        interestBarChart.animateY(1000);
        interestBarChart.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
