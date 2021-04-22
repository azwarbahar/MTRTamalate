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
import com.skripsi.mtrtamalate.adapter.RiwayatPetugasAdapter;
import com.skripsi.mtrtamalate.models.laporan.Laporan;
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataMasyarakatActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private String kelurahan;
    private RecyclerView rv_masyarakat;
    private DataMasyarakatAdapter dataMasyarakatAdapter;

    private ImageView img_kosong;
    private CardView cv_search;
    private CardView cv_progressBar;
    private EditText et_cari;
    private SwipeRefreshLayout swipe_continer;

    private ArrayList<Masayarkat> masayarkats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_masyarakat);
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
        rv_masyarakat = findViewById(R.id.rv_masyarakat);


        swipe_continer = findViewById(R.id.swipe_continer);
        swipe_continer.setOnRefreshListener(this);
        swipe_continer.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark);
        swipe_continer.post(new Runnable() {
            @Override
            public void run() {
                loadDataMasyarakat(kelurahan);
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

    private void filter(String text) {
        ArrayList<Masayarkat> filteredList = new ArrayList<>();
        for (Masayarkat item : masayarkats) {
            if (item.getNamaMasyarakat().toLowerCase().contains(text.toLowerCase()) ||
                    item.getNikMasyarakat().toLowerCase().contains(text.toLowerCase()) ||
                    item.getTelponMasyarakat().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        dataMasyarakatAdapter.filterList(filteredList);
    }

    private void loadDataMasyarakat(String kelurahan) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseMasyarakat> responseMasyarakatCall = apiInterface.getMasyarakatKelurahan(kelurahan);
        responseMasyarakatCall.enqueue(new Callback<ResponseMasyarakat>() {
            @Override
            public void onResponse(Call<ResponseMasyarakat> call, Response<ResponseMasyarakat> response) {
                swipe_continer.setRefreshing(false);
                if (response.isSuccessful()){
                    String kode = String.valueOf(response.body().getKode());
                    if (kode.equals("1")){
                        masayarkats = (ArrayList<Masayarkat>) response.body().getMasyarakat_data();
                        if (masayarkats.size() < 1){
                            onEmptyData();
                        } else {
                            onGetData();
                            rv_masyarakat.setLayoutManager(new LinearLayoutManager(DataMasyarakatActivity.this));
                            dataMasyarakatAdapter = new DataMasyarakatAdapter(DataMasyarakatActivity.this,
                                    masayarkats);
                            rv_masyarakat.setAdapter(dataMasyarakatAdapter);
                        }
                    } else {
                        onEmptyData();
                        new SweetAlertDialog(DataMasyarakatActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Mohon Maaf...")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    onEmptyData();
                    new SweetAlertDialog(DataMasyarakatActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Mohon Maaf...")
                            .setContentText("Terjadi Kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMasyarakat> call, Throwable t) {
                onEmptyData();
                swipe_continer.setRefreshing(false);
                new SweetAlertDialog(DataMasyarakatActivity.this, SweetAlertDialog.ERROR_TYPE)
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

    @Override
    public void onRefresh() {
        loadDataMasyarakat(kelurahan);
    }

    private void onLoadData() {
        et_cari.setEnabled(false);
        cv_progressBar.setVisibility(View.VISIBLE);
        img_kosong.setVisibility(View.GONE);
        rv_masyarakat.setVisibility(View.GONE);
    }

    private void onGetData(){
        et_cari.setEnabled(true);
        cv_progressBar.setVisibility(View.GONE);
        img_kosong.setVisibility(View.GONE);
        rv_masyarakat.setVisibility(View.VISIBLE);
    }

    private void onEmptyData(){
        et_cari.setEnabled(false);
        cv_progressBar.setVisibility(View.GONE);
        img_kosong.setVisibility(View.VISIBLE);
        rv_masyarakat.setVisibility(View.GONE);
    }
}