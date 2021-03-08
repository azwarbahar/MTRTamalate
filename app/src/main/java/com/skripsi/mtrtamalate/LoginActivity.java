package com.skripsi.mtrtamalate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.masyarakat.MasyarakatActivity;
import com.skripsi.mtrtamalate.ui.petugas.PetugasActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

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

    private String id_masyarakat;
    private String nik_masyarakat;
    private String nama_masyarakat;
    private String alamat_masyarakat;
    private String area_masyarakat;
    private String telpon_masyarakat;
    private String usia_masyarakat;
    private String kelurahan_masyarakat;
    private String latitude_masyarakat;
    private String longitude_masyarakat;
    private String password_masyarakat;
    private String foto_masyarakat;
    private String status_masyarakat;
    private String pembayaran_masyarakat;
    private String status_marker;

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedpreferences = getApplicationContext().getSharedPreferences(Constanta.MY_SHARED_PREFERENCES,
                MODE_PRIVATE);
        String role = sharedpreferences.getString(Constanta.SESSION_ROLE, "");
        if (!role.isEmpty()) {
            switch (role) {
                case "masyarakat":
                    startActivity(new Intent(this, MasyarakatActivity.class));
                    finish();
                    break;
                case "petugas":
                    startActivity(new Intent(this, PetugasActivity.class));
                    finish();
                    break;
                case "koordinator":
                    Toast.makeText(this, "Koordinator", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(this, DashboardSpvActivity.class));
//                    finish();
                    break;
            }
        }

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

        id_masyarakat = masayarkat.getIdMasyarakat();
        nik_masyarakat = masayarkat.getNikMasyarakat();
        nama_masyarakat = masayarkat.getNamaMasyarakat();
        alamat_masyarakat = masayarkat.getAlamatMasyarakat();
        area_masyarakat = masayarkat.getAreaMasyarakat();
        telpon_masyarakat = masayarkat.getTelponMasyarakat();
        usia_masyarakat = masayarkat.getUsiaMasyarakat();
        kelurahan_masyarakat = masayarkat.getKelurahanMasyarakat();
        latitude_masyarakat = masayarkat.getLatitudeMasyarakat();
        longitude_masyarakat = masayarkat.getLongitudeMasyarakat();
        password_masyarakat = masayarkat.getPasswordMasyarakat();
        foto_masyarakat = masayarkat.getFotoMasyarakat();
        status_masyarakat = masayarkat.getStatusMasyarakat();
        pembayaran_masyarakat = masayarkat.getPembayaranMasyarakat();
        status_marker = masayarkat.getStatusMarker();

        String status = masayarkat.getStatusMasyarakat();
        if (status.equals("Aktif")){
            startSession(id_masyarakat, nik_masyarakat, nama_masyarakat, alamat_masyarakat, area_masyarakat,
                    telpon_masyarakat, usia_masyarakat, kelurahan_masyarakat, latitude_masyarakat,
                    longitude_masyarakat, password_masyarakat, foto_masyarakat, status_masyarakat,
                    pembayaran_masyarakat, status_marker);
            startActivity(new Intent(LoginActivity.this, MasyarakatActivity.class));
            finish();
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

    private void startSession(String id_masyarakat, String nik_masyarakat, String nama_masyarakat,
                              String alamat_masyarakat, String area_masyarakat, String telpon_masyarakat,
                              String usia_masyarakat, String kelurahan_masyarakat,
                              String latitude_masyarakat, String longitude_masyarakat,
                              String password_masyarakat, String foto_masyarakat, String status_masyarakat,
                              String pembayaran_masyarakat, String status_marker) {

        editor = sharedpreferences.edit();
        editor.putString(Constanta.SESSION_ROLE, "masyarakat");
        // data
        editor.putString(Constanta.SESSION_ID_MASYARAKAT, id_masyarakat);
        editor.putString(Constanta.SESSION_NIK_MASYARAKAT, nik_masyarakat);
        editor.putString(Constanta.SESSION_NAMA_MASYARAKAT, nama_masyarakat);
        editor.putString(Constanta.SESSION_ALAMAT_MASYARAKAT, alamat_masyarakat);
        editor.putString(Constanta.SESSION_AREA_MASYARAKAT, area_masyarakat);
        editor.putString(Constanta.SESSION_TELPON_MASYARAKAT, telpon_masyarakat);
        editor.putString(Constanta.SESSION_USIA_MASYARAKAT, usia_masyarakat);
        editor.putString(Constanta.SESSION_KELURAHAN_MASYARAKAT, kelurahan_masyarakat);
        editor.putString(Constanta.SESSION_LATITUDE_MASYARAKAT, latitude_masyarakat);
        editor.putString(Constanta.SESSION_LONGITUDE_MASYARAKAT, longitude_masyarakat);
        editor.putString(Constanta.SESSION_PASSWORD_MASYARAKAT, password_masyarakat);
        editor.putString(Constanta.SESSION_FOTO_MASYARAKAT, foto_masyarakat);
        editor.putString(Constanta.SESSION_STATUS_MASYARAKAT, status_masyarakat);
        editor.putString(Constanta.SESSION_PEMBAYARAN_MASYARAKAT, pembayaran_masyarakat);
        editor.putString(Constanta.SESSION_STATUS_MARKER, status_marker);
        editor.apply();

    }
}