package com.skripsi.mtrtamalate.ui.koordinator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.sampah.ResponSampah;
import com.skripsi.mtrtamalate.models.sampah.Sampah;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.masyarakat.FormLaporActivity;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.TitikLokasiActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporDataSampahActivity extends AppCompatActivity {

    private CardView cv_tambah;
    private CardView cv_kurang;

    private SharedPreferences mPreferences;

    private TextView tv_jumlah_motor;
    private RelativeLayout rl_send;
    private String id;
    private String kelurahan;
    private SweetAlertDialog pDialog;
    String tanggal_sekarang;
    private Sampah sampah;
    private boolean ready_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor_data_sampah);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        id = mPreferences.getString(Constanta.SESSION_ID_PETUGAS, "");
        kelurahan = mPreferences.getString(Constanta.SESSION_KELURAHAN_PETUGAS, "");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tanggal_sekarang = dateFormat.format(new Date());
        setReadySend(id, tanggal_sekarang);

        rl_send = findViewById(R.id.rl_send);
        cv_kurang = findViewById(R.id.cv_kurang);
        cv_tambah = findViewById(R.id.cv_tambah);
        tv_jumlah_motor = findViewById(R.id.tv_jumlah_motor);

        cv_kurang.setOnClickListener(this::clickKurangMotor);
        cv_tambah.setOnClickListener(this::clickTambahMotor);
        rl_send.setOnClickListener(this::clickSend);

    }

    private void setReadySend(String id, String tanggal_sekarang) {

        pDialog = new SweetAlertDialog(LaporDataSampahActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponSampah> responSampahCall = apiInterface.cekTodaySendPetugas(id, tanggal_sekarang);
        responSampahCall.enqueue(new Callback<ResponSampah>() {
            @Override
            public void onResponse(Call<ResponSampah> call, Response<ResponSampah> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        if (response.body().getResult_data() == null) {
                            setModeAktifSend();
                        } else {
                            setModeDisableSend();
                        }
                    } else {
                        setModeAktifSend();
                    }
                } else {
                    setModeAktifSend();
                }

            }

            @Override
            public void onFailure(Call<ResponSampah> call, Throwable t) {
                pDialog.dismiss();
                setModeAktifSend();

            }
        });

    }

    private void clickSend(View view) {

        if (ready_send) {
            pDialog = new SweetAlertDialog(LaporDataSampahActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();

            new SweetAlertDialog(LaporDataSampahActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Mengirim")
                    .setContentText("Melaporkan Data Sampah Harian ?")
                    .setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            pDialog.dismiss();
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            proccesSend();
                        }
                    })
                    .show();

        } else {
            new SweetAlertDialog(LaporDataSampahActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf")
                    .setContentText("Anda Sudah mengirim Laporan Data Sampah Hari ini!")
                    .show();
        }

    }

    private void proccesSend() {
        String jumlah_data = tv_jumlah_motor.getText().toString();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponSampah> responSampahCall = apiInterface.AddSampah(jumlah_data, "Pikul", kelurahan, id, tanggal_sekarang);
        responSampahCall.enqueue(new Callback<ResponSampah>() {
            @Override
            public void onResponse(Call<ResponSampah> call, Response<ResponSampah> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        new SweetAlertDialog(LaporDataSampahActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success..")
                                .setContentText("Laporan Berhasil dikirim..")
                                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        setReadySend(id, tanggal_sekarang);
                                    }
                                })
                                .show();
                    } else {
                        new SweetAlertDialog(LaporDataSampahActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Gagal..")
                                .setContentText("Terjadi kesalahan!, Kode : " + kode)
                                .show();
                    }
                } else {
                    new SweetAlertDialog(LaporDataSampahActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Gagal..")
                            .setContentText("Terjadi kesalahan!")
                            .show();
                }

            }

            @Override
            public void onFailure(Call<ResponSampah> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(LaporDataSampahActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal..")
                        .setContentText("Terjadi kesalahan Sistem!")
                        .show();
            }
        });

    }

    private void setModeAktifSend() {
        rl_send.setBackground(ContextCompat.getDrawable(LaporDataSampahActivity.this, R.color.colorPrimaryDark));
        ready_send = true;
    }

    private void setModeDisableSend() {
        rl_send.setBackground(ContextCompat.getDrawable(LaporDataSampahActivity.this, R.color.grey));
        ready_send = false;
    }

    private void clickTambahMotor(View view) {
        int awal = Integer.parseInt(tv_jumlah_motor.getText().toString());
        String hasil = String.valueOf(awal + 1);
        tv_jumlah_motor.setText(hasil);
    }

    private void clickKurangMotor(View view) {
        int awal = Integer.parseInt(tv_jumlah_motor.getText().toString());
        int hasil = awal - 1;
        if (hasil < 1) {
            tv_jumlah_motor.setText("0");
        } else {
            tv_jumlah_motor.setText(String.valueOf(hasil));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}