package com.skripsi.mtrtamalate.ui.masyarakat.akun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.utils.Constanta;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAkunActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private TextInputEditText tie_nama;
    private TextInputEditText tie_alamat;
    private TextInputEditText tie_telpon;
    private TextInputEditText tie_usia;
    private RelativeLayout rl_edit;

    private String id;
    private String nama;
    private String alamat;
    private String telpon;
    private String usia;

    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_akun);
        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tie_nama = findViewById(R.id.tie_nama);
        tie_alamat = findViewById(R.id.tie_alamat);
        tie_telpon = findViewById(R.id.tie_telpon);
        tie_usia = findViewById(R.id.tie_usia);
        rl_edit = findViewById(R.id.rl_edit);
        rl_edit.setOnClickListener(this::simpanData);

        setFirstData();

    }

    private void simpanData(View view) {

        pDialog = new SweetAlertDialog(EditAkunActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        new SweetAlertDialog(EditAkunActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Edit Profil")
                .setContentText("Simpan Perubahan Data ?")
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
                        pDialog.show();
                        sendData();
                    }
                })
                .show();
    }

    private void sendData() {

        String send_nama = tie_nama.getText().toString();
        String send_alamat = tie_alamat.getText().toString();
        String send_telpon = tie_telpon.getText().toString();
        String send_usia = tie_usia.getText().toString();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseMasyarakat> responseMasyarakatCall = apiInterface.editProfil(id,
                send_nama, send_alamat, send_telpon, send_usia);
        responseMasyarakatCall.enqueue(new Callback<ResponseMasyarakat>() {
            @Override
            public void onResponse(Call<ResponseMasyarakat> call, Response<ResponseMasyarakat> response) {
                pDialog.dismiss();
                if (response.isSuccessful()){
                    String kode = String.valueOf(response.body().getKode());
                    if (kode.equals("1")){
                        new SweetAlertDialog(EditAkunActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success..")
                                .setContentText(response.body().getPesan())
                                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();

                                        editor = mPreferences.edit();
                                        editor.putString(Constanta.SESSION_NAMA_MASYARAKAT, send_nama);
                                        editor.putString(Constanta.SESSION_ALAMAT_MASYARAKAT, send_alamat);
                                        editor.putString(Constanta.SESSION_TELPON_MASYARAKAT, send_telpon);
                                        editor.putString(Constanta.SESSION_USIA_MASYARAKAT, send_usia);
                                        editor.apply();
                                        finish();

                                    }
                                })
                                .show();
                    } else {
                        new SweetAlertDialog(EditAkunActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Mohon Maaf...")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    new SweetAlertDialog(EditAkunActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Mohon Maaf...")
                            .setContentText("Terjadi Kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMasyarakat> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(EditAkunActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Mohon Maaf...")
                        .setContentText("Terjadi Kesalahan System!")
                        .show();
            }
        });
    }

    private void setFirstData() {
        id = mPreferences.getString(Constanta.SESSION_ID_MASYARAKAT, "");
        nama = mPreferences.getString(Constanta.SESSION_NAMA_MASYARAKAT, "");
        alamat = mPreferences.getString(Constanta.SESSION_ALAMAT_MASYARAKAT, "");
        telpon = mPreferences.getString(Constanta.SESSION_TELPON_MASYARAKAT, "");
        usia = mPreferences.getString(Constanta.SESSION_USIA_MASYARAKAT, "");

        tie_nama.setText(nama);
        tie_alamat.setText(alamat);
        tie_telpon.setText(telpon);
        tie_usia.setText(usia);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}