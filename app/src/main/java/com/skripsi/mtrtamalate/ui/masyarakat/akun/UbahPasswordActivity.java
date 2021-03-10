package com.skripsi.mtrtamalate.ui.masyarakat.akun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

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

public class UbahPasswordActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private TextInputEditText tie_password_lama;
    private TextInputEditText tie_password_baru;
    private String id_masyarakat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        id_masyarakat = mPreferences.getString(Constanta.SESSION_ID_MASYARAKAT, "");

        tie_password_lama = findViewById(R.id.tie_password_lama);
        tie_password_baru = findViewById(R.id.tie_password_baru);
        RelativeLayout rl_edit = findViewById(R.id.rl_edit);
        rl_edit.setOnClickListener(this::clickSimpan);

    }

    private void clickSimpan(View view) {

        String old_password = tie_password_lama.getText().toString();
        String new_password = tie_password_baru.getText().toString();

        if (old_password.isEmpty() || old_password.equals("")) {
            new SweetAlertDialog(UbahPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal...")
                    .setContentText("Password lama tidak boleh kosong!")
                    .show();
        } else if (new_password.isEmpty() || new_password.equals("")) {
            new SweetAlertDialog(UbahPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal...")
                    .setContentText("Password baru tidak boleh kosong!")
                    .show();
        } else if (old_password.equals(new_password)) {
            new SweetAlertDialog(UbahPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal...")
                    .setContentText("Password baru dan lama tidak boleh sama!")
                    .show();
        } else {
            startUpdatePassword(old_password, new_password);
        }
    }

    private void startUpdatePassword(String old_password, String new_password) {
        SweetAlertDialog pDialog = new SweetAlertDialog(UbahPasswordActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseMasyarakat> responseMasyarakatCall = apiInterface.editPassword(id_masyarakat, old_password, new_password);
        responseMasyarakatCall.enqueue(new Callback<ResponseMasyarakat>() {
            @Override
            public void onResponse(Call<ResponseMasyarakat> call, Response<ResponseMasyarakat> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = String.valueOf(response.body().getKode());
                    if (kode.equals("0")) {
                        new SweetAlertDialog(UbahPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Gagal...")
                                .setContentText(response.body().getPesan())
                                .show();
                    } else if (kode.equals("1")) {
                        SweetAlertDialog success = new SweetAlertDialog(UbahPasswordActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        success.setTitleText("Success..");
                        success.setCancelable(false);
                        success.setContentText("Edit Password Berhasil");
                        success.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                tie_password_baru.setText("");
                                tie_password_lama.setText("");
                                finish();
                            }
                        });
                        success.show();
                    } else if (kode.equals("2")) {
                        new SweetAlertDialog(UbahPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Gagal...")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    new SweetAlertDialog(UbahPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Mohon Maaf...")
                            .setContentText("Terjadi Kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMasyarakat> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(UbahPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Permintaan Gagal, Terjadi Kesalahan Sistem!")
                        .show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}