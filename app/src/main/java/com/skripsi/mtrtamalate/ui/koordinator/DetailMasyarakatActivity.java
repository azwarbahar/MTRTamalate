package com.skripsi.mtrtamalate.ui.koordinator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.area.Area;
import com.skripsi.mtrtamalate.models.area.ResponseArea;
import com.skripsi.mtrtamalate.models.kendaraan.ResponseKendaraan;
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.EditAkunActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMasyarakatActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;

    private ImageView img_foto;
    private TextView tv_nama;
    private TextView tv_telpon;
    private TextView tv_umur;
    private TextView tv_area;
    private TextView tv_alamat;

    private ImageView img_chevron;
    private RelativeLayout continer_map;

    private RelativeLayout rl_riwayat;
    private RelativeLayout rl_btn_aktif;
    private TextView tv_btn_aktif;

    private CardView cv_call;

    String id_masyarakat;
    private Masayarkat masayarkat;
    private Area area;

    private boolean ready_koordinat;

    private LatLng latLngzoom;

    private String telpon;
    private String status_pembayaran;

    private SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_masyarakat);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cv_call = findViewById(R.id.cv_call);
        tv_btn_aktif = findViewById(R.id.tv_btn_aktif);
        rl_btn_aktif = findViewById(R.id.rl_btn_aktif);
        rl_riwayat = findViewById(R.id.rl_riwayat);
        continer_map = findViewById(R.id.continer_map);
        img_foto = findViewById(R.id.img_foto);
        tv_nama = findViewById(R.id.tv_nama);
        tv_telpon = findViewById(R.id.tv_telpon);
        tv_umur = findViewById(R.id.tv_umur);
        tv_area = findViewById(R.id.tv_area);
        tv_alamat = findViewById(R.id.tv_alamat);
        img_chevron = findViewById(R.id.img_chevron);

        id_masyarakat = getIntent().getStringExtra("id_masyarakat");
        loadData(id_masyarakat);

        img_chevron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ready_koordinat) {
                    animationMaps();
                } else {
                    Toast.makeText(DetailMasyarakatActivity.this, "Koordinat masyarakat belum diatur! ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cv_call.setOnClickListener(this::clickCall);
        rl_riwayat.setOnClickListener(this::clickRiwayat);
        rl_btn_aktif.setOnClickListener(this::clickAktif);

    }

    private void loadData(String idMasyarakat) {

        pDialog = new SweetAlertDialog(DetailMasyarakatActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseMasyarakat> responseMasyarakatCall = apiInterface.getMasyarakatId(idMasyarakat);
        responseMasyarakatCall.enqueue(new Callback<ResponseMasyarakat>() {
            @Override
            public void onResponse(Call<ResponseMasyarakat> call, Response<ResponseMasyarakat> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    masayarkat = response.body().getResult_masyarakat();
                    setDataMasyarakat(masayarkat);
                }
            }

            @Override
            public void onFailure(Call<ResponseMasyarakat> call, Throwable t) {
                pDialog.dismiss();

            }
        });

    }

    private void clickAktif(View view) {

        pDialog = new SweetAlertDialog(DetailMasyarakatActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        if (status_pembayaran.equals("Sudah")) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseMasyarakat> responseMasyarakatCall = apiInterface.editPembayaran(id_masyarakat, "Belum");
            responseMasyarakatCall.enqueue(new Callback<ResponseMasyarakat>() {
                @Override
                public void onResponse(Call<ResponseMasyarakat> call, Response<ResponseMasyarakat> response) {
                    pDialog.dismiss();
                    if (response.isSuccessful()) {
                        String kode = String.valueOf(response.body().getKode());
                        if (kode.equals("1")) {
                            showSnackMessage("Permintaan berhasil!");
                            loadData(id_masyarakat);
                        } else {
                            showSnackMessage(response.body().getPesan());
                        }
                    } else {
                        showSnackMessage("Gagal Memproses Permintaan!");
                    }
                }

                @Override
                public void onFailure(Call<ResponseMasyarakat> call, Throwable t) {
                    pDialog.dismiss();
                    showSnackMessage("Terjadi Kesalahan Sistem!");
                }
            });

        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseMasyarakat> responseMasyarakatCall = apiInterface.editPembayaran(id_masyarakat, "Sudah");
            responseMasyarakatCall.enqueue(new Callback<ResponseMasyarakat>() {
                @Override
                public void onResponse(Call<ResponseMasyarakat> call, Response<ResponseMasyarakat> response) {
                    pDialog.dismiss();
                    if (response.isSuccessful()) {
                        String kode = String.valueOf(response.body().getKode());
                        if (kode.equals("1")) {
                            showSnackMessage("Permintaan berhasil!");
                            loadData(id_masyarakat);
                        } else {
                            showSnackMessage(response.body().getPesan());
                        }
                    } else {
                        showSnackMessage("Gagal Memproses Permintaan!");
                    }
                }

                @Override
                public void onFailure(Call<ResponseMasyarakat> call, Throwable t) {
                    pDialog.dismiss();
                    showSnackMessage("Terjadi Kesalahan Sistem!");
                }
            });


        }
    }

    private void showSnackMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .setAction("Close", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                    }
                })
                .show();
    }

    private void clickRiwayat(View view) {
        Intent intent = new Intent(DetailMasyarakatActivity.this, RiwayatLaporanActivity.class);
        intent.putExtra("role", "masyarakat");
        intent.putExtra("id", id_masyarakat);
        startActivity(intent);
    }

    private void clickCall(View view) {
        Uri number = Uri.parse("tel:" + telpon);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    private void onDone() {
        tv_btn_aktif.setText("Batalkan Pembayaran");
        rl_btn_aktif.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_greay_radius));
    }

    private void onNot() {
        tv_btn_aktif.setText("Aktifkan Pembayaran");
        rl_btn_aktif.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_primary_radius));
    }

    private void setDataMasyarakat(Masayarkat masayarkat) {

        tv_nama.setText(masayarkat.getNamaMasyarakat());
        tv_telpon.setText(masayarkat.getTelponMasyarakat());
        tv_umur.setText(masayarkat.getUsiaMasyarakat() + " Tahun");
        tv_alamat.setText(masayarkat.getAlamatMasyarakat());
        telpon = masayarkat.getTelponMasyarakat();
        status_pembayaran = masayarkat.getPembayaranMasyarakat();
        if (status_pembayaran.equals("Sudah")) {
            onDone();
        } else {
            onNot();
        }

        setArea(masayarkat.getAreaMasyarakat(), masayarkat.getKelurahanMasyarakat());
        if (masayarkat.getLatitudeMasyarakat().equals("-")) {
            ready_koordinat = false;
        } else {
            ready_koordinat = true;
            setMapLokasi(masayarkat.getLatitudeMasyarakat(), masayarkat.getLongitudeMasyarakat());
        }

        Glide.with(this)
                .load(Constanta.URL_IMG_MASYARAKAT + masayarkat.getFotoMasyarakat())
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(img_foto);


    }

    private void setArea(String areaMasyarakat, String kelurahanMasyarakat) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseArea> responseAreaCall = apiInterface.getAreaId(areaMasyarakat, kelurahanMasyarakat);
        responseAreaCall.enqueue(new Callback<ResponseArea>() {
            @Override
            public void onResponse(Call<ResponseArea> call, Response<ResponseArea> response) {
                area = response.body().getResult_area();
                initArea(area);
//                Toast.makeText(DetailMasyarakatActivity.this, "sukses", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseArea> call, Throwable t) {
//                Toast.makeText(DetailMasyarakatActivity.this, "gaga", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initArea(Area area) {
        tv_area.setText(area.getNama_area());
    }

    private void setMapLokasi(String latitudeMasyarakat, String longitudeMasyarakat) {
        latLngzoom = new LatLng(Double.parseDouble(latitudeMasyarakat), Double.parseDouble(longitudeMasyarakat));
    }

    private void animationMaps() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
        if (continer_map.getVisibility() == View.VISIBLE) {
            continer_map.animate()
                    .translationY(0)
                    .alpha(0.0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            continer_map.setVisibility(View.GONE);
                        }
                    });
        } else {
            continer_map.setVisibility(View.VISIBLE);
            continer_map.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(900)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        if (latLngzoom != null) {
            map.clear();
            googleMap.addMarker(new MarkerOptions().title("Koordinat Masyarakat")
                    .icon(bitmapDescriptor(getApplicationContext()))
                    .position(latLngzoom));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngzoom, 13));
        }
    }

    private BitmapDescriptor bitmapDescriptor(Context context) {
        int height = 70;
        int width = 50;
        Drawable vectorDrawble = ContextCompat.getDrawable(context, R.drawable.marker_lokasi_sampah);
        assert vectorDrawble != null;
        vectorDrawble.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawble.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}