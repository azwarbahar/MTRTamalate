package com.skripsi.mtrtamalate.ui.koordinator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.kendaraan.Kendaraan;
import com.skripsi.mtrtamalate.models.kendaraan.ResponseKendaraan;
import com.skripsi.mtrtamalate.models.petugas.Petugas;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.utils.Constanta;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanPetugasActivity extends AppCompatActivity {

    private CircularImageView img_profile;
    private TextView tv_nama;
    private TextView tv_kendaraan;
    private Petugas petugas_parcelable;
    private Kendaraan kendaraan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_petugas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        petugas_parcelable = getIntent().getParcelableExtra("data_petugas");

        img_profile = findViewById(R.id.img_profile);
        tv_nama = findViewById(R.id.tv_nama);
        tv_kendaraan = findViewById(R.id.tv_kendaraan);

        loadDataPetugas(petugas_parcelable);


    }

    private void loadDataPetugas(Petugas petugas_parcelable) {

        tv_nama.setText(petugas_parcelable.getNamaPekerja());
        Glide.with(this)
                .load(Constanta.URL_IMG_PETUGAS + petugas_parcelable.getFotoPekerja())
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(img_profile);

        loadDataKendaraan(petugas_parcelable.getKendaraanPekerja());


    }

    private void loadDataKendaraan(String kendaraanPekerja) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseKendaraan> responseKendaraanCall = apiInterface.getKendaraanPetugas(kendaraanPekerja);
        responseKendaraanCall.enqueue(new Callback<ResponseKendaraan>() {
            @Override
            public void onResponse(Call<ResponseKendaraan> call, Response<ResponseKendaraan> response) {
                kendaraan = response.body().getKendaraan();
                initKendaraan(kendaraan);
            }

            @Override
            public void onFailure(Call<ResponseKendaraan> call, Throwable t) {

            }
        });

    }

    private void initKendaraan(Kendaraan kendaraan) {

        tv_kendaraan.setText("Kendaraab : "+kendaraan.getNamaKendaraan() +
                " - " + kendaraan.getNomorKendaraan());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}