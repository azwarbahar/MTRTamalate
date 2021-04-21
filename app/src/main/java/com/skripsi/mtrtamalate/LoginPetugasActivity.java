package com.skripsi.mtrtamalate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.models.petugas.Petugas;
import com.skripsi.mtrtamalate.models.petugas.ResponsePetugas;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.koordinator.KoordinatorActivity;
import com.skripsi.mtrtamalate.ui.masyarakat.MasyarakatActivity;
import com.skripsi.mtrtamalate.ui.petugas.PetugasActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPetugasActivity extends AppCompatActivity {


    private TextInputEditText tie_nik;
    private TextInputEditText tie_password;
    private TextInputLayout til_nik;
    private TextInputLayout til_password;
    private Petugas petugas;

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    private TextView tv2;
    private TextView tv_login_masyarakat;
    private String data_intent;


    private String id_pekerja;
    private String nik_pekerja;
    private String nama_pekerja;
    private String jenis_kelamin_pekerja;
    private String usia_pekerja;
    private String alamat_pekerja;
    private String latitude_pekerja;
    private String longitude_pekerja;
    private String telpon_pekerja;
    private String kelurahan_pekerja;
    private String password;
    private String area_pekerja;
    private String kendaraan_pekerja;
    private String foto_pekerja;
    private String status_pekerja;
    private String status_kerja_pekerja;
    private String role_pekerja;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_petugas);

        sharedpreferences = getApplicationContext().getSharedPreferences(Constanta.MY_SHARED_PREFERENCES,
                MODE_PRIVATE);

        tv_login_masyarakat = findViewById(R.id.tv_login_masyarakat);
        tv2 = findViewById(R.id.tv2);
        data_intent = getIntent().getStringExtra("login_data");
        tv2.setText("Login " + data_intent);

        tv_login_masyarakat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPetugasActivity.this, LoginActivity.class));
                finish();
            }
        });

        tie_nik = findViewById(R.id.tie_nik);
        tie_password = findViewById(R.id.tie_password);
        til_nik = findViewById(R.id.til_nik);
        til_password = findViewById(R.id.til_password);

        TextView tv_btn_login = findViewById(R.id.tv_btn_login);
        tv_btn_login.setOnClickListener(new View.OnClickListener() {
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

        SweetAlertDialog pDialog = new SweetAlertDialog(LoginPetugasActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponsePetugas> responseMasyarakatCall = apiInterface.authPetugas(nik, pass);
        responseMasyarakatCall.enqueue(new Callback<ResponsePetugas>() {
            @Override
            public void onResponse(Call<ResponsePetugas> call, Response<ResponsePetugas> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    switch (kode) {
                        case "0":
                            til_nik.setError(pesan);
                            new SweetAlertDialog(LoginPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opss..")
                                    .setContentText(pesan)
                                    .show();
                            break;
                        case "2":
                            til_password.setError(pesan);
                            new SweetAlertDialog(LoginPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opss..")
                                    .setContentText(pesan)
                                    .show();
                            break;
                        case "1":
                            petugas = response.body().getPetugas();
                            initAuth(petugas);
                            break;
                        default:
                            new SweetAlertDialog(LoginPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opss..")
                                    .setContentText("Login Gagal!")
                                    .show();
                            break;
                    }
                } else {
                    new SweetAlertDialog(LoginPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Opss..")
                            .setContentText("Login Gagal!")
                            .show();
                }

            }

            @Override
            public void onFailure(Call<ResponsePetugas> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(LoginPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Opss..")
                        .setContentText("Terjadi Kesalahan Sistem!")
                        .show();
            }
        });
    }

    private void initAuth(Petugas petugas) {

        id_pekerja = petugas.getIdPekerja();
        nik_pekerja = petugas.getNikPekerja();
        nama_pekerja = petugas.getNamaPekerja();
        jenis_kelamin_pekerja = petugas.getJenisKelaminPekerja();
        usia_pekerja = petugas.getUsiaPekerja();
        alamat_pekerja = petugas.getAlamatPekerja();
        latitude_pekerja = petugas.getLatitudePekerja();
        longitude_pekerja = petugas.getLongitudePekerja();
        telpon_pekerja = petugas.getTelponPekerja();
        kelurahan_pekerja = petugas.getKelurahanPekerja();
        password = petugas.getPassword();
        area_pekerja = petugas.getAreaPekerja();
        kendaraan_pekerja = petugas.getKendaraanPekerja();
        foto_pekerja = petugas.getFotoPekerja();
        status_pekerja = petugas.getStatusPekerja();
        status_kerja_pekerja = petugas.getStatusKerjaPekerja();
        role_pekerja = petugas.getRolePekerja();

        String status = petugas.getStatusPekerja();
        if (status.equals("Aktif")) {
            startSession(id_pekerja, nik_pekerja, nama_pekerja, jenis_kelamin_pekerja, usia_pekerja,
                    alamat_pekerja, latitude_pekerja, longitude_pekerja, telpon_pekerja,
                    kelurahan_pekerja, password, area_pekerja, kendaraan_pekerja,
                    foto_pekerja, status_pekerja, status_kerja_pekerja, role_pekerja);
            if (!role_pekerja.isEmpty()) {
                switch (role_pekerja) {
                    case "masyarakat":
                        startActivity(new Intent(this, MasyarakatActivity.class));
                        finish();
                        break;
                    case "Petugas":
                        startActivity(new Intent(this, PetugasActivity.class));
                        finish();
                        break;
                    case "Koordinator":
                        startActivity(new Intent(this, KoordinatorActivity.class));
                        finish();
                        break;
                }
            }
//            startActivity(new Intent(LoginPetugasActivity.this, PetugasActivity.class));
//            finish();
        } else {
            SweetAlertDialog sweetAlertDialogError = new SweetAlertDialog(LoginPetugasActivity.this,
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

    private void startSession(String id_pekerja, String nik_pekerja, String nama_pekerja,
                              String jenis_kelamin_pekerja, String usia_pekerja, String alamat_pekerja,
                              String latitude_pekerja, String longitude_pekerja, String telpon_pekerja,
                              String kelurahan_pekerja, String password, String area_pekerja,
                              String kendaraan_pekerja, String foto_pekerja, String status_pekerja,
                              String status_kerja_pekerja, String role_pekerja) {


        editor = sharedpreferences.edit();
        editor.putString(Constanta.SESSION_ROLE, role_pekerja);
        // data
        editor.putString(Constanta.SESSION_ID_PETUGAS, id_pekerja);
        editor.putString(Constanta.SESSION_NIK_PETUGAS, nik_pekerja);
        editor.putString(Constanta.SESSION_NAMA_PETUGAS, nama_pekerja);
        editor.putString(Constanta.SESSION_JEKEL_PETUGAS, jenis_kelamin_pekerja);
        editor.putString(Constanta.SESSION_USIA_PETUGAS, usia_pekerja);
        editor.putString(Constanta.SESSION_ALAMAT_PETUGAS, alamat_pekerja);
        editor.putString(Constanta.SESSION_LATITUDE_PETUGAS, latitude_pekerja);
        editor.putString(Constanta.SESSION_LONGITUDE_PETUGAS, longitude_pekerja);
        editor.putString(Constanta.SESSION_TELPON_PETUGAS, telpon_pekerja);
        editor.putString(Constanta.SESSION_KELURAHAN_PETUGAS, kelurahan_pekerja);
        editor.putString(Constanta.SESSION_PASSWORD_PETUGAS, password);
        editor.putString(Constanta.SESSION_AREA_PETUGAS, area_pekerja);
        editor.putString(Constanta.SESSION_KENDARAAN_PETUGAS, kendaraan_pekerja);
        editor.putString(Constanta.SESSION_FOTO_PETUGAS, foto_pekerja);
        editor.putString(Constanta.SESSION_STATUS_PETUGAS, status_pekerja);
        editor.putString(Constanta.SESSION_STATUS_KERJA_PETUGAS, status_kerja_pekerja);
        editor.putString(Constanta.SESSION_ROLE_PETUGAS, role_pekerja);
        editor.apply();

    }

}