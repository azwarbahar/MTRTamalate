package com.skripsi.mtrtamalate.ui.masyarakat.sampah;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

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
import com.skripsi.mtrtamalate.models.sampah.ResponSampah;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.masyarakat.FormLaporActivity;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSampahActivity extends AppCompatActivity {

    private RecyclerView rv_data_sampah;
    private AnyChartView any_chart_view;
    private DataSampahAdapter dataSampahAdapter;
    private TextView tv_total_keseluruhan;

    private SweetAlertDialog pDialog;
    ArrayList<String> kelurahan = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sampah);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_total_keseluruhan = findViewById(R.id.tv_total_keseluruhan);


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

        loadDataTotal();
        loadDataKelurahan();

    }

    private void loadDataKelurahan() {

        kelurahan.add("Balang Baru");
        kelurahan.add("Barombong");
        kelurahan.add("Bongaya");
        kelurahan.add("Bonto Duri");
        kelurahan.add("Jongaya");
        kelurahan.add("Maccini Sombala");
        kelurahan.add("Mangasa");
        kelurahan.add("Manuruki");
        kelurahan.add("Pabaeng-Baeng");
        kelurahan.add("Parang Tambung");
        kelurahan.add("Tanjung Merdeka");
        rv_data_sampah.setLayoutManager(new LinearLayoutManager(this));
        dataSampahAdapter = new DataSampahAdapter(this, kelurahan);
        rv_data_sampah.setAdapter(dataSampahAdapter);
    }

    private void loadDataTotal() {

        pDialog = new SweetAlertDialog(DataSampahActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponSampah> responSampahCall = apiInterface.getTotalSampah();
        responSampahCall.enqueue(new Callback<ResponSampah>() {
            @Override
            public void onResponse(Call<ResponSampah> call, Response<ResponSampah> response) {
                pDialog.dismiss();
                String kode = response.body().getKode();
                if (kode.equals("1")) {

                    tv_total_keseluruhan.setText(response.body().getTotal_berat());

                }

            }

            @Override
            public void onFailure(Call<ResponSampah> call, Throwable t) {
                pDialog.dismiss();

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}