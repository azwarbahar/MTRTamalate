package com.skripsi.mtrtamalate.ui.masyarakat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skripsi.mtrtamalate.LoginActivity;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.utils.Constanta;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasyarakatActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masyarakat);

        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        String id_masyarakat = mPreferences.getString(Constanta.SESSION_ID_MASYARAKAT, "");
        loadDataMasyarakat(id_masyarakat);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_masyarakat);
        NavigationUI.setupWithNavController(bottomNavigationView, navController); 

    }

    private void loadDataMasyarakat(String id_masyarakat) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseMasyarakat> responseMasyarakatCall = apiInterface.getMasyarakatId(id_masyarakat);
        responseMasyarakatCall.enqueue(new Callback<ResponseMasyarakat>() {
            @Override
            public void onResponse(Call<ResponseMasyarakat> call, Response<ResponseMasyarakat> response) {
                if (response.isSuccessful()) {
                    masayarkat = response.body().getResult_masyarakat();
                    initData(masayarkat);
                }
            }

            @Override
            public void onFailure(Call<ResponseMasyarakat> call, Throwable t) {

            }
        });

    }

    private void initData(Masayarkat masayarkat) {

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

        startSession(id_masyarakat, nik_masyarakat, nama_masyarakat, alamat_masyarakat, area_masyarakat,
                telpon_masyarakat, usia_masyarakat, kelurahan_masyarakat, latitude_masyarakat,
                longitude_masyarakat, password_masyarakat, foto_masyarakat, status_masyarakat,
                pembayaran_masyarakat, status_marker);

        if (!status_masyarakat.equals("Aktif")) {

            SweetAlertDialog sweetAlertDialogError = new SweetAlertDialog(MasyarakatActivity.this,
            SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialogError.setTitleText("Maaf..");
            sweetAlertDialogError.setCancelable(false);
            sweetAlertDialogError.setContentText("Akun anda telah di suspend!");
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

        editor = mPreferences.edit();
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