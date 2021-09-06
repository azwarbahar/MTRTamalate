package com.skripsi.mtrtamalate.ui.koordinator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.adapter.LaporanPetugasAdapter;
import com.skripsi.mtrtamalate.adapter.RiwayatPetugasAdapter;
import com.skripsi.mtrtamalate.models.laporan.Laporan;
import com.skripsi.mtrtamalate.models.laporan.ResponLaporan;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatLaporanActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ImageView img_kosong;
    private CardView cv_progressBar;
    private SwipeRefreshLayout swipe_continer;
    private RecyclerView rv_laporan;
    private LaporanPetugasAdapter laporanPetugasAdapter;
    private ArrayList<Laporan> laporans;

    private String role;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_laporan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        swipe_continer = findViewById(R.id.swipe_continer);
        cv_progressBar = findViewById(R.id.cv_progressBar);
        img_kosong = findViewById(R.id.img_kosong);
        rv_laporan = findViewById(R.id.rv_laporan);

        swipe_continer = findViewById(R.id.swipe_continer);
        swipe_continer.setOnRefreshListener(this);
        swipe_continer.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark);
        swipe_continer.post(new Runnable() {
            @Override
            public void run() {
                role = getIntent().getStringExtra("role");
                id = getIntent().getStringExtra("id");
                prepareLoad(role, id);
            }
        });

    }

    private void loadDataPetugas(String session_id) {
        onLoadData();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponLaporan> responLaporanCall = apiInterface.getLaporanPetugas(session_id);
        responLaporanCall.enqueue(new Callback<ResponLaporan>() {
            @Override
            public void onResponse(Call<ResponLaporan> call, Response<ResponLaporan> response) {
                swipe_continer.setRefreshing(false);
                if (response.isSuccessful()){
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
                        laporans = (ArrayList<Laporan>) response.body().getResult_laporan_petugas();
                        if (laporans.size()<1){
                            onEmptyData();
                        } else {
                            onGetData();
                            rv_laporan.setLayoutManager(new LinearLayoutManager(RiwayatLaporanActivity.this));
                            laporanPetugasAdapter = new LaporanPetugasAdapter(RiwayatLaporanActivity.this, laporans, "koordinator", false);
                            rv_laporan.setAdapter(laporanPetugasAdapter);
                        }

                    } else {
                        onEmptyData();
                        new SweetAlertDialog(RiwayatLaporanActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Mohon Maaf...")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    onEmptyData();
                    new SweetAlertDialog(RiwayatLaporanActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Mohon Maaf...")
                            .setContentText("Terjadi Kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponLaporan> call, Throwable t) {
                onEmptyData();
                swipe_continer.setRefreshing(false);
                new SweetAlertDialog(RiwayatLaporanActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Mohon Maaf...")
                        .setContentText("Terjadi Kesalahan System!")
                        .show();

            }
        });

    }

    private void loadDataMasyarakat(String session_id) {
        onLoadData();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponLaporan> responLaporanCall = apiInterface.getLaporanMasyarakat(session_id);
        responLaporanCall.enqueue(new Callback<ResponLaporan>() {
            @Override
            public void onResponse(Call<ResponLaporan> call, Response<ResponLaporan> response) {
                swipe_continer.setRefreshing(false);
                if (response.isSuccessful()){
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
                        laporans = (ArrayList<Laporan>) response.body().getResult_laporan_masyarakat();
                        if (laporans.size()<1){
                            onEmptyData();
                        } else {
                            onGetData();
                            rv_laporan.setLayoutManager(new LinearLayoutManager(RiwayatLaporanActivity.this));
                            laporanPetugasAdapter = new LaporanPetugasAdapter(RiwayatLaporanActivity.this, laporans, "koordinator", false);
                            rv_laporan.setAdapter(laporanPetugasAdapter);
                        }

                    } else {
                        onEmptyData();
                        new SweetAlertDialog(RiwayatLaporanActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Mohon Maaf...")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    onEmptyData();
                    new SweetAlertDialog(RiwayatLaporanActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Mohon Maaf...")
                            .setContentText("Terjadi Kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponLaporan> call, Throwable t) {
                onEmptyData();
                swipe_continer.setRefreshing(false);
                new SweetAlertDialog(RiwayatLaporanActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Mohon Maaf...")
                        .setContentText("Terjadi Kesalahan System!")
                        .show();

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void prepareLoad(String role, String id) {
        if (role.equals("masyarakat")){
            loadDataMasyarakat(id);
        } else {
            loadDataPetugas(id);
        }
    }

    @Override
    public void onRefresh() {
        prepareLoad(role, id);
    }

    private void onLoadData() {

        cv_progressBar.setVisibility(View.VISIBLE);
        img_kosong.setVisibility(View.GONE);
        rv_laporan.setVisibility(View.GONE);
    }

    private void onGetData(){

        cv_progressBar.setVisibility(View.GONE);
        img_kosong.setVisibility(View.GONE);
        rv_laporan.setVisibility(View.VISIBLE);
    }

    private void onEmptyData(){

        cv_progressBar.setVisibility(View.GONE);
        img_kosong.setVisibility(View.VISIBLE);
        rv_laporan.setVisibility(View.GONE);
    }
}