package com.skripsi.mtrtamalate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.masyarakat.MasyarakatActivity;
import com.skripsi.mtrtamalate.ui.petugas.PetugasActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText tie_nik;
    private TextInputEditText tie_password;
    private TextInputLayout til_nik;
    private TextInputLayout til_password;
    private Masayarkat masayarkat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tie_nik = findViewById(R.id.tie_nik);
        tie_password = findViewById(R.id.tie_password);
        til_nik = findViewById(R.id.til_nik);
        til_password = findViewById(R.id.til_password);

        ImageView img_login = findViewById(R.id.img_login);
        img_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                til_nik.setError(null);
                til_password.setError(null);

                String nik = tie_nik.getText().toString();
                String pass = tie_password.getText().toString();

                if (nik.equals("") || nik.isEmpty()) {
                    til_nik.setError("Lengkapi");
                } else if (pass.equals("") || pass.isEmpty()) {
                    til_password.setError("Lengkapi");
                } else {
                    loadLogin(nik, pass);
                }
            }
        });

    }

    private void loadLogin(String nik, String pass) {

        SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseMasyarakat> responseMasyarakatCall = apiInterface.authMasyarakat(nik, pass);
        responseMasyarakatCall.enqueue(new Callback<ResponseMasyarakat>() {
            @Override
            public void onResponse(Call<ResponseMasyarakat> call, Response<ResponseMasyarakat> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = String.valueOf(response.body().getKode());
                    String pesan = response.body().getPesan();
                    switch (kode) {
                        case "0":
                            til_nik.setError(pesan);
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opss..")
                                    .setContentText(pesan)
                                    .show();
                            break;
                        case "2":
                            til_password.setError(pesan);
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opss..")
                                    .setContentText(pesan)
                                    .show();
                            break;
                        case "1":
                            masayarkat = response.body().getMasayarkat();
                            initAuth(masayarkat);
                            break;
                        default:
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opss..")
                                    .setContentText("Login Gagal!")
                                    .show();
                            break;
                    }
                } else {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Opss..")
                            .setContentText("Login Gagal!")
                            .show();
                }

            }

            @Override
            public void onFailure(Call<ResponseMasyarakat> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Opss..")
                        .setContentText("Terjadi Kesalahan Sistem!")
                        .show();
            }
        });



    }

    private void initAuth(Masayarkat masayarkat) {

        String status = masayarkat.getStatusMasyarakat();
        if (status.equals("Aktif")){
            startActivity(new Intent(LoginActivity.this, MasyarakatActivity.class));
        } else {
            SweetAlertDialog sweetAlertDialogError = new SweetAlertDialog(LoginActivity.this,
                    SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialogError.setTitleText("Opss..");
            sweetAlertDialogError.setCancelable(false);
            sweetAlertDialogError.setContentText("Username telah di suspend!");
            sweetAlertDialogError.setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                }
            });
            sweetAlertDialogError.show();
        }

    }
}