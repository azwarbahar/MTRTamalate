package com.skripsi.mtrtamalate.ui.petugas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.skripsi.mtrtamalate.models.laporan.Laporan;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.koordinator.DetailLaporanActivity;
import com.skripsi.mtrtamalate.ui.koordinator.DetailMasyarakatActivity;
import com.skripsi.mtrtamalate.ui.koordinator.DetailPetugasActivity;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.ViewImageActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailLaporanPetugasActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
//    private ImageView img_menu;
    private Laporan laporan_parcelable;
    private TextView tv_alamat;
    private TextView tv_keterangan;
    private TextView tv_nama;
    private TextView tv_nik;
    private TextView tv_status;
    private TextView tv_tanggal_lapor;
    private TextView tv_tanggal_ditindaki;
    private TextView tv_selesai;
    private RelativeLayout rl_foto;

    private String url_image;
    private LatLng latLngzoom;
    private String id_masyarakat;
    private String id_petugas;
    private String status_laporan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan_petugas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

//        img_menu = findViewById(R.id.img_menu);
        tv_selesai = findViewById(R.id.tv_selesai);
        rl_foto = findViewById(R.id.rl_foto);
        tv_alamat = findViewById(R.id.tv_alamat);
        tv_nama = findViewById(R.id.tv_nama);
        tv_nik = findViewById(R.id.tv_nik);
        tv_keterangan = findViewById(R.id.tv_keterangan);
        tv_status = findViewById(R.id.tv_status);
        tv_tanggal_lapor = findViewById(R.id.tv_tanggal_lapor);
        tv_tanggal_ditindaki = findViewById(R.id.tv_tanggal_ditindaki);

        laporan_parcelable = getIntent().getParcelableExtra("extra_data");
        id_masyarakat = laporan_parcelable.getMasyarakatId();
        id_petugas = laporan_parcelable.getPetugasId();
        tv_alamat.setText(laporan_parcelable.getAlamatLaporan());
        tv_keterangan.setText(laporan_parcelable.getKeteranganLaporan());
        status_laporan = laporan_parcelable.getStausLaporan();
        if (status_laporan.equals("Proccess")){
            tv_status.setText("Proses");
            tv_status.setTextColor(ContextCompat.getColor(this, R.color.proccessText));
            tv_status.setBackground(ContextCompat.getDrawable(DetailLaporanPetugasActivity.this, R.drawable.bg_status_proccess));
        } else if (status_laporan.equals("Done")){
            tv_status.setText("Selesai");
            tv_status.setTextColor(ContextCompat.getColor(this, R.color.doneText));
            tv_status.setBackground(ContextCompat.getDrawable(DetailLaporanPetugasActivity.this, R.drawable.bg_status_done));
        } else {
            tv_status.setText("Batal");
            tv_status.setTextColor(ContextCompat.getColor(this, R.color.cancelText));
            tv_status.setBackground(ContextCompat.getDrawable(DetailLaporanPetugasActivity.this, R.drawable.bg_status_cancel));
        }
        tv_tanggal_lapor.setText("Laporan Pada: "+ laporan_parcelable.getCreatedAt());
        if (laporan_parcelable.getStausLaporan().equals("Done")){
            tv_tanggal_ditindaki.setText("Ditindaki Pada: "+ laporan_parcelable.getUpdateAt());
        } else {
            tv_tanggal_ditindaki.setText("Ditindaki Pada: - ");
        }
        getMasyarakat(laporan_parcelable.getMasyarakatId());
        setMarker(laporan_parcelable.getLatitudeLaporan(), laporan_parcelable.getLongitudeLaporan());
        url_image = laporan_parcelable.getFotoLaporan();

//        img_menu.setOnClickListener(this::clickMenu);
        rl_foto.setOnClickListener(this::clickFoto);
        tv_selesai.setOnClickListener(this::clickSelesai);

    }

    private void clickSelesai(View view) {
        String id_laporan;
        String id_petugas;
        String status;
    }

    private void clickFoto(View view) {
        Intent intent = new Intent(DetailLaporanPetugasActivity.this, ViewImageActivity.class);
        intent.putExtra("role", "foto_laporan");
        intent.putExtra("foto", url_image);
        startActivity(intent);
    }

    private void setMarker(String latitudeLaporan, String longitudeLaporan) {
        latLngzoom = new LatLng(Double.parseDouble(latitudeLaporan), Double.parseDouble(longitudeLaporan));
    }

    private BitmapDescriptor bitmapDescriptor(Context context) {
        int height = 60;
        int width = 45;
        Drawable vectorDrawble = ContextCompat.getDrawable(context, R.drawable.marker_laporan);
        assert vectorDrawble != null;
        vectorDrawble.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawble.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void getMasyarakat(String masyarakatId) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseMasyarakat> responseMasyarakatCall = apiInterface.getMasyarakatId(masyarakatId);
        responseMasyarakatCall.enqueue(new Callback<ResponseMasyarakat>() {
            @Override
            public void onResponse(Call<ResponseMasyarakat> call, Response<ResponseMasyarakat> response) {
                if (response.isSuccessful()) {
                    tv_nama.setText("Nama : " + response.body().getResult_masyarakat().getNamaMasyarakat());
                    tv_nik.setText("NIK : " + response.body().getResult_masyarakat().getNikMasyarakat());

                }
            }

            @Override
            public void onFailure(Call<ResponseMasyarakat> call, Throwable t) {

            }
        });

    }

    public interface PickerLaporanOptionListener {
        void lihatPelapor();

        void lihatPetugas();

        void lihatFoto();
    }

    public static void showPickerLaporanOption(Context context, DetailLaporanActivity.PickerLaporanOptionListener listener) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.option));

        // add a list
        String[] animals = {context.getString(R.string.lihat_pelapor),
                context.getString(R.string.petugas),
                context.getString(R.string.foto_laporan)};
        builder.setItems(animals, (dialog, which) -> {
            switch (which) {
                case 0:
                    listener.lihatPelapor();
                    break;
                case 1:
                    listener.lihatPetugas();
                    break;
                case 2:
                    listener.lihatFoto();
                    break;
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (latLngzoom != null) {
            map.clear();
            googleMap.addMarker(new MarkerOptions().title("Titik Laporan")
                    .icon(bitmapDescriptor(getApplicationContext()))
                    .position(latLngzoom));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngzoom, 15));
        }

    }
}