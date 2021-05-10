package com.skripsi.mtrtamalate.ui.koordinator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.adapter.DataMasyarakatAdapter;
import com.skripsi.mtrtamalate.adapter.DataPetugasAdapter;
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.models.petugas.Petugas;
import com.skripsi.mtrtamalate.models.petugas.ResponsePetugas;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataPetugasActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private String kelurahan;
    private RecyclerView rv_petugas;
    private DataPetugasAdapter dataPetugasAdapter;

    private ImageView img_kosong;
    private CardView cv_search;
    private CardView cv_progressBar;
    private EditText et_cari;
    private SwipeRefreshLayout swipe_continer;

    private ArrayList<Petugas> petugases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_petugas);
        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);

        kelurahan = mPreferences.getString(Constanta.SESSION_KELURAHAN_PETUGAS, "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        swipe_continer = findViewById(R.id.swipe_continer);
        cv_progressBar = findViewById(R.id.cv_progressBar);
        img_kosong = findViewById(R.id.img_kosong);
        cv_search = findViewById(R.id.cv_search);
        et_cari = findViewById(R.id.et_cari);
        rv_petugas = findViewById(R.id.rv_petugas);


        swipe_continer = findViewById(R.id.swipe_continer);
        swipe_continer.setOnRefreshListener(this);
        swipe_continer.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark);
        swipe_continer.post(new Runnable() {
            @Override
            public void run() {
                onLoadData();
                loadDataPekerja(kelurahan);
            }
        });


        et_cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLoadData();
        loadDataPekerja(kelurahan);
    }

    private void loadDataPekerja(String kelurahan) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponsePetugas> responseMasyarakatCall = apiInterface.getPetugasKelurahan(kelurahan);
        responseMasyarakatCall.enqueue(new Callback<ResponsePetugas>() {
            @Override
            public void onResponse(Call<ResponsePetugas> call, Response<ResponsePetugas> response) {
                swipe_continer.setRefreshing(false);
                if (response.isSuccessful()) {
                    String kode = String.valueOf(response.body().getKode());
                    if (kode.equals("1")) {
                        petugases = (ArrayList<Petugas>) response.body().getPetugas_data();
                        if (petugases.size() < 1) {
                            onEmptyData();
                        } else {
                            onGetData();
                            rv_petugas.setLayoutManager(new LinearLayoutManager(DataPetugasActivity.this));
                            dataPetugasAdapter = new DataPetugasAdapter(DataPetugasActivity.this,
                                    petugases);
                            rv_petugas.setAdapter(dataPetugasAdapter);
                        }
                    } else {
                        onEmptyData();
                        new SweetAlertDialog(DataPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Mohon Maaf...")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    onEmptyData();
                    new SweetAlertDialog(DataPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Mohon Maaf...")
                            .setContentText("Terjadi Kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePetugas> call, Throwable t) {
                onEmptyData();
                swipe_continer.setRefreshing(false);
                new SweetAlertDialog(DataPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Mohon Maaf...")
                        .setContentText("Terjadi Kesalahan System!")
                        .show();
            }
        });

    }


    private void filter(String text) {
        ArrayList<Petugas> filteredList = new ArrayList<>();
        if (!petugases.isEmpty()) {
            for (Petugas item : petugases) {
                if (item.getNamaPekerja().toLowerCase().contains(text.toLowerCase()) ||
                        item.getNikPekerja().toLowerCase().contains(text.toLowerCase()) ||
                        item.getStatusKerjaPekerja().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            dataPetugasAdapter.filterList(filteredList);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRefresh() {
        loadDataPekerja(kelurahan);
    }

    private void onLoadData() {
        et_cari.setEnabled(false);
        cv_progressBar.setVisibility(View.VISIBLE);
        img_kosong.setVisibility(View.GONE);
        rv_petugas.setVisibility(View.GONE);
    }

    private void onGetData() {
        et_cari.setEnabled(true);
        cv_progressBar.setVisibility(View.GONE);
        img_kosong.setVisibility(View.GONE);
        rv_petugas.setVisibility(View.VISIBLE);
    }

    private void onEmptyData() {
        et_cari.setEnabled(false);
        cv_progressBar.setVisibility(View.GONE);
        img_kosong.setVisibility(View.VISIBLE);
        rv_petugas.setVisibility(View.GONE);
    }
}