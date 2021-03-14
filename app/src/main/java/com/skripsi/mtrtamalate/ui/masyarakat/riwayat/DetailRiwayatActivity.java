package com.skripsi.mtrtamalate.ui.masyarakat.riwayat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.laporan.Laporan;
import com.skripsi.mtrtamalate.models.petugas.Petugas;
import com.skripsi.mtrtamalate.models.petugas.ResponsePetugas;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.TitikLokasiActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRiwayatActivity extends AppCompatActivity {


    private RelativeLayout rl_status;
    private ImageView img_status;
    private ImageView img_foto;
    private TextView tv_status;
    private TextView tv_batal;
    private TextView tv_tanggal;
    private TextView tv_keterangan;
    private TextView tv_alamat;

    //petugas
    private TextView tv_tdk_tersedia;
    private TextView tv_nama_petugas;
    private TextView tv_jekel_petugas;
    private ImageView img_petugas;
    private CardView cv_foto_petugas;

    private String id_laporan;
    private String status;
    private String keterangan;
    private String tanggal;
    private String alamat;
    private String foto_laporan;
    private String id_petugas;

    private Petugas petugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rl_status = findViewById(R.id.rl_status);
        img_status = findViewById(R.id.img_status);
        img_foto = findViewById(R.id.img_foto);
        tv_status = findViewById(R.id.tv_status);
        tv_batal = findViewById(R.id.tv_batal);
        tv_tanggal = findViewById(R.id.tv_tanggal);
        tv_keterangan = findViewById(R.id.tv_keterangan);
        tv_alamat = findViewById(R.id.tv_alamat);

        tv_tdk_tersedia = findViewById(R.id.tv_tdk_tersedia);
        tv_nama_petugas = findViewById(R.id.tv_nama_petugas);
        tv_jekel_petugas = findViewById(R.id.tv_jekel_petugas);
        img_petugas = findViewById(R.id.img_petugas);
        cv_foto_petugas = findViewById(R.id.cv_foto_petugas);

        Laporan laporan = getIntent().getParcelableExtra("extra_data_riwayat");
        assert laporan != null;
        loadData(laporan);
    }

    private void loadData(Laporan laporan) {

        id_laporan = laporan.getIdLaporan();
        status = laporan.getStausLaporan();
        keterangan = laporan.getKeteranganLaporan();
        tanggal = laporan.getCreatedAt();
        alamat = laporan.getAlamatLaporan();
        id_petugas = laporan.getPetugasId();
        foto_laporan = laporan.getFotoLaporan();
        Glide.with(this)
                .load(Constanta.URL_IMG_LAPORAN + foto_laporan)
                .into(img_foto);

        tv_keterangan.setText(keterangan);
        tv_alamat.setText(alamat);
        tv_tanggal.setText("Tanggal Lapor : "+tanggal);

        initStatus(status);

        loadPetugsa(id_petugas);
    }

    private void petugasTdkAda(){
        cv_foto_petugas.setVisibility(View.GONE);
        tv_nama_petugas.setVisibility(View.GONE);
        tv_jekel_petugas.setVisibility(View.GONE);
        tv_tdk_tersedia.setVisibility(View.VISIBLE);
    }

    private void petugasAda(){
        cv_foto_petugas.setVisibility(View.VISIBLE);
        tv_nama_petugas.setVisibility(View.VISIBLE);
        tv_jekel_petugas.setVisibility(View.VISIBLE);
        tv_tdk_tersedia.setVisibility(View.GONE);
    }

    private void initStatus(String status) {
        if (status.equals("Proccess")){
            tv_status.setText("Laporan Sedang Proses..");
            tv_batal.setVisibility(View.GONE);
            rl_status.setBackground(ContextCompat.getDrawable(DetailRiwayatActivity.this, R.color.colorBackWarning));
            Glide.with(this)
                    .load(R.drawable.icon_hourglass)
                    .into(img_status);
        } else if (status.equals("Done")){
            tv_status.setText("Laporan Telah ditindaki!");
            tv_batal.setVisibility(View.GONE);
            rl_status.setBackground(ContextCompat.getDrawable(DetailRiwayatActivity.this, R.color.colorBackSuccess));
            Glide.with(this)
                    .load(R.drawable.icon_success)
                    .into(img_status);
        } else {
            tv_status.setText("Laporan Dibatalkan!");
            tv_batal.setVisibility(View.GONE);
            rl_status.setBackground(ContextCompat.getDrawable(DetailRiwayatActivity.this, R.color.colorBackDanger));
            Glide.with(this)
                    .load(R.drawable.icon_cancel)
                    .into(img_status);
        }
    }

    private void loadPetugsa(String id_petugas) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponsePetugas> responsePetugasCall = apiInterface.getPetugasId(id_petugas);
        responsePetugasCall.enqueue(new Callback<ResponsePetugas>() {
            @Override
            public void onResponse(Call<ResponsePetugas> call, Response<ResponsePetugas> response) {
                if (response.isSuccessful()){
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
                        petugasAda();
                        petugas = response.body().getPetugas();
                        initPetugas(petugas);
                    } else {
                        petugasTdkAda();
                    }
                } else {
                    petugasTdkAda();
                }
            }

            @Override
            public void onFailure(Call<ResponsePetugas> call, Throwable t) {
                petugasTdkAda();
            }
        });
    }

    private void initPetugas(Petugas petugas) {
        String foto = petugas.getFotoPekerja();
        String nama = petugas.getNamaPekerja();
        String jekel = petugas.getJenisKelaminPekerja();

        tv_nama_petugas.setText(nama);
        tv_jekel_petugas.setText(jekel);
        Glide.with(this)
                .load(Constanta.URL_IMG_PETUGAS + foto)
                .into(img_petugas);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}