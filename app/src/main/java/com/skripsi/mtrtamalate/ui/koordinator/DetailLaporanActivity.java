package com.skripsi.mtrtamalate.ui.koordinator;

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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.laporan.Laporan;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.ImagePickerActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailLaporanActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private ImageView img_menu;
    private Laporan laporan_parcelable;
    private TextView tv_alamat;
    private TextView tv_keterangan;
    private TextView tv_nama;
    private TextView tv_nik;
    private TextView tv_status;

    private LatLng latLngzoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        img_menu = findViewById(R.id.img_menu);
        tv_alamat = findViewById(R.id.tv_alamat);
        tv_nama = findViewById(R.id.tv_nama);
        tv_nik = findViewById(R.id.tv_nik);
        tv_keterangan = findViewById(R.id.tv_keterangan);
        tv_status = findViewById(R.id.tv_status);

        laporan_parcelable = getIntent().getParcelableExtra("extra_data");
        tv_alamat.setText(laporan_parcelable.getAlamatLaporan());
        tv_keterangan.setText(laporan_parcelable.getKeteranganLaporan());
        tv_status.setText(laporan_parcelable.getStausLaporan());
        getMasyarakat(laporan_parcelable.getMasyarakatId());
        setMarker(laporan_parcelable.getLatitudeLaporan(), laporan_parcelable.getLongitudeLaporan());


        img_menu.setOnClickListener(this::clickMenu);


    }

    private void setMarker(String latitudeLaporan, String longitudeLaporan) {
        latLngzoom = new LatLng(Double.parseDouble(latitudeLaporan), Double.parseDouble(longitudeLaporan));
    }

    private BitmapDescriptor bitmapDescriptor(Context context) {
        int height = 60;
        int width = 35;
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

    public static void showPickerLaporanOption(Context context, PickerLaporanOptionListener listener) {
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

    private void clickMenu(View view) {
        showPickerLaporanOption(this, new PickerLaporanOptionListener() {
            @Override
            public void lihatPelapor() {
                Toast.makeText(DetailLaporanActivity.this, "klik pelapor", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void lihatPetugas() {
                Toast.makeText(DetailLaporanActivity.this, "klik Petugas", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void lihatFoto() {
                Toast.makeText(DetailLaporanActivity.this, "Klik Foto", Toast.LENGTH_SHORT).show();
            }
        });
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
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngzoom, 14));
        }

    }
}