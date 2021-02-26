package com.skripsi.mtrtamalate.ui.masyarakat.sampah;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.adapter.DataSampahAdapter;

import java.util.ArrayList;
import java.util.List;

public class DataSampahActivity extends AppCompatActivity {

    private RecyclerView rv_data_sampah;
    private AnyChartView any_chart_view;
    private DataSampahAdapter dataSampahAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sampah);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv_data_sampah = findViewById(R.id.rv_data_sampah);
        any_chart_view = findViewById(R.id.any_chart_view);
        any_chart_view.setProgressBar(findViewById(R.id.progress_bar));

        Pie pie = AnyChart.pie();


        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Tamalate", 250));
        data.add(new ValueDataEntry("Parang Tambung", 300));
        data.add(new ValueDataEntry("Bonto Duri", 500));
        data.add(new ValueDataEntry("Manuruki", 400));
        data.add(new ValueDataEntry("Pabaeng-baneng", 700));
        data.add(new ValueDataEntry("Barombong", 900));
        pie.data(data);
        pie.title("Data Kelurahan");
        pie.legend(false);

        any_chart_view.setChart(pie);

        rv_data_sampah.setLayoutManager(new LinearLayoutManager(this));
        dataSampahAdapter = new DataSampahAdapter(this);
        rv_data_sampah.setAdapter(dataSampahAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}