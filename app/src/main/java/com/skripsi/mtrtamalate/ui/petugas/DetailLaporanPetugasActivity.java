package com.skripsi.mtrtamalate.ui.petugas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.laporan.Laporan;
import com.skripsi.mtrtamalate.models.laporan.ResponLaporan;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.koordinator.DetailLaporanActivity;
import com.skripsi.mtrtamalate.ui.koordinator.DetailMasyarakatActivity;
import com.skripsi.mtrtamalate.ui.koordinator.DetailPetugasActivity;
import com.skripsi.mtrtamalate.ui.masyarakat.FormLaporActivity;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.ViewImageActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
    private String id_laporan;
    private String id_masyarakat;
    private String id_petugas_sekarang;
    private String id_petugas;
    private String status_laporan;

    private SweetAlertDialog pDialog;

    private SharedPreferences mPreferences;

    private Laporan laporan;
    private RelativeLayout continer_dialog;
    private LinearLayout ll_foto;
    private LinearLayout ll_batal;
    private TextView tvcoba;

    private Bitmap bitmap_bukti_tindakan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan_petugas);
        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        id_petugas_sekarang = mPreferences.getString(Constanta.SESSION_ID_PETUGAS, "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

//        img_menu = findViewById(R.id.img_menu);
        tvcoba = findViewById(R.id.tvcoba);
        tv_selesai = findViewById(R.id.tv_selesai);
        rl_foto = findViewById(R.id.rl_foto);
        tv_alamat = findViewById(R.id.tv_alamat);
        tv_nama = findViewById(R.id.tv_nama);
        tv_nik = findViewById(R.id.tv_nik);
        tv_keterangan = findViewById(R.id.tv_keterangan);
        tv_status = findViewById(R.id.tv_status);
        tv_tanggal_lapor = findViewById(R.id.tv_tanggal_lapor);
        tv_tanggal_ditindaki = findViewById(R.id.tv_tanggal_ditindaki);
        continer_dialog = findViewById(R.id.continer_dialog);
        ll_foto = findViewById(R.id.ll_foto);
        ll_batal = findViewById(R.id.ll_batal);
        continer_dialog.setVisibility(View.GONE);

        laporan_parcelable = getIntent().getParcelableExtra("extra_data");
        id_laporan = laporan_parcelable.getIdLaporan();
        id_masyarakat = laporan_parcelable.getMasyarakatId();
        id_petugas = laporan_parcelable.getPetugasId();
        tv_alamat.setText(laporan_parcelable.getAlamatLaporan());
        tv_keterangan.setText(laporan_parcelable.getKeteranganLaporan());
        status_laporan = laporan_parcelable.getStausLaporan();
        if (status_laporan.equals("New")) {
            tv_selesai.setText("Tindaki");
            tv_status.setText("Terbaru");
            tv_status.setTextColor(ContextCompat.getColor(this, R.color.newText));
            tv_status.setBackground(ContextCompat.getDrawable(DetailLaporanPetugasActivity.this, R.drawable.bg_status_new));
        } else if (status_laporan.equals("Proccess")) {
            tv_selesai.setText("Selesai");
            tv_status.setText("Proses");
            tv_status.setTextColor(ContextCompat.getColor(this, R.color.proccessText));
            tv_status.setBackground(ContextCompat.getDrawable(DetailLaporanPetugasActivity.this, R.drawable.bg_status_proccess));
        } else if (status_laporan.equals("Done")) {
            tv_status.setText("Selesai");
            tv_status.setTextColor(ContextCompat.getColor(this, R.color.doneText));
            tv_status.setBackground(ContextCompat.getDrawable(DetailLaporanPetugasActivity.this, R.drawable.bg_status_done));
        } else {
            tv_status.setText("Batal");
            tv_status.setTextColor(ContextCompat.getColor(this, R.color.cancelText));
            tv_status.setBackground(ContextCompat.getDrawable(DetailLaporanPetugasActivity.this, R.drawable.bg_status_cancel));
        }
        tv_tanggal_lapor.setText("Laporan Pada: " + laporan_parcelable.getCreatedAt());
        if (laporan_parcelable.getStausLaporan().equals("Done")) {
            tv_tanggal_ditindaki.setText("Ditindaki Pada: " + laporan_parcelable.getUpdateAt());
        } else {
            tv_tanggal_ditindaki.setText("Ditindaki Pada: - ");
        }
        getMasyarakat(laporan_parcelable.getMasyarakatId());
        setMarker(laporan_parcelable.getLatitudeLaporan(), laporan_parcelable.getLongitudeLaporan());
        url_image = laporan_parcelable.getFotoLaporan();

//        img_menu.setOnClickListener(this::clickMenu);
        rl_foto.setOnClickListener(this::clickFoto);
        tv_selesai.setOnClickListener(this::clickSelesai);
        ll_foto.setOnClickListener(this::clickCamera);
        ll_batal.setOnClickListener(this::clickBatalCamera);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {

                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap resized = (Bitmap) data.getExtras().get("data");
                        int nh = (int) (resized.getHeight() * (512.0 / resized.getWidth()));
                        bitmap_bukti_tindakan = Bitmap.createScaledBitmap(resized, 512, nh, true);
                        sendSelesai(bitmap_bukti_tindakan);
                    }
                    break;
            }
        }
    }

    private String imgToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void sendSelesai(Bitmap bitmap_bukti_tindakan) {
        String image_send = imgToString(bitmap_bukti_tindakan);
//        tvcoba.setText(image_send);
        sendDone(id_laporan, id_petugas_sekarang, "Done", image_send);
    }

    private void clickBatalCamera(View view) {
        continer_dialog.setVisibility(View.GONE);
        loadLaporanId(id_laporan);
    }

    private void clickCamera(View view) {
        continer_dialog.setVisibility(View.GONE);
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
//                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                            File file = new File(Environment.getExternalStorageDirectory(),
//                                    "/MTR_Tamalate/Laporan Petugas/photo_" + timeStamp + ".png");
//                            imageUri = Uri.fromFile(file);

//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                            startActivityForResult(intent, 0);

                            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);

                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    private void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void loadLaporanId(String id_laporan){

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponLaporan> responLaporanCall = apiInterface.getLaporanId(id_laporan);
        responLaporanCall.enqueue(new Callback<ResponLaporan>() {
            @Override
            public void onResponse(Call<ResponLaporan> call, Response<ResponLaporan> response) {
                if (response.isSuccessful()){
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
                        laporan = response.body().getResult_laporan();
                        initLaporanCallback(laporan);
                    } else {
                        Toast.makeText(DetailLaporanPetugasActivity.this, "Gagal Callback Data..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailLaporanPetugasActivity.this, "Gagal Callback Data..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponLaporan> call, Throwable t) {
                Toast.makeText(DetailLaporanPetugasActivity.this, "Gagal Callback Data..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initLaporanCallback(Laporan laporan) {
        id_laporan = laporan.getIdLaporan();
        id_masyarakat = laporan.getMasyarakatId();
        id_petugas = laporan.getPetugasId();
        tv_alamat.setText(laporan.getAlamatLaporan());
        tv_keterangan.setText(laporan.getKeteranganLaporan());
        status_laporan = laporan.getStausLaporan();
        if (status_laporan.equals("New")) {
            tv_selesai.setText("Tindaki");
            tv_status.setText("Terbaru");
            tv_status.setTextColor(ContextCompat.getColor(this, R.color.newText));
            tv_status.setBackground(ContextCompat.getDrawable(DetailLaporanPetugasActivity.this, R.drawable.bg_status_new));
        } else if (status_laporan.equals("Proccess")) {
            tv_selesai.setText("Selesai");
            tv_status.setText("Proses");
            tv_status.setTextColor(ContextCompat.getColor(this, R.color.proccessText));
            tv_status.setBackground(ContextCompat.getDrawable(DetailLaporanPetugasActivity.this, R.drawable.bg_status_proccess));
        } else if (status_laporan.equals("Done")) {
            tv_status.setText("Selesai");
            tv_status.setTextColor(ContextCompat.getColor(this, R.color.doneText));
            tv_status.setBackground(ContextCompat.getDrawable(DetailLaporanPetugasActivity.this, R.drawable.bg_status_done));
        } else {
            tv_status.setText("Batal");
            tv_status.setTextColor(ContextCompat.getColor(this, R.color.cancelText));
            tv_status.setBackground(ContextCompat.getDrawable(DetailLaporanPetugasActivity.this, R.drawable.bg_status_cancel));
        }
        tv_tanggal_lapor.setText("Laporan Pada: " + laporan.getCreatedAt());
        if (laporan.getStausLaporan().equals("Done")) {
            tv_tanggal_ditindaki.setText("Ditindaki Pada: " + laporan.getUpdateAt());
        } else {
            tv_tanggal_ditindaki.setText("Ditindaki Pada: - ");
        }
        getMasyarakat(laporan.getMasyarakatId());
        url_image = laporan.getFotoLaporan();

    }

    private void clickSelesai(View view) {

        pDialog = new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        if (status_laporan.equals("New")) {

            new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Tindaki")
                    .setContentText("Anda ingin menuju titik lokasi laporan ?")
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
//                                    pDialog.show();
                            sendProccess(id_laporan, id_petugas_sekarang, "Proccess");
                        }
                    })
                    .show();
        } else if (status_laporan.equals("Proccess")) {
            pDialog.dismiss();
            continer_dialog.setVisibility(View.VISIBLE);

//            new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText("Selesai")
//                    .setContentText("Anda Telah Selesai Menindaki Laporan ?")
//                    .setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            pDialog.dismiss();
//                            sweetAlertDialog.dismiss();
//                        }
//                    })
//                    .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            sweetAlertDialog.dismiss();
////                                    pDialog.show();
//                            sendDone(id_laporan, id_petugas_sekarang, "Done");
//                        }
//                    })
//                    .show();
        }
    }

    private void sendProccess(String id_laporan, String id_petugas_sekarang, String status) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponLaporan> responLaporanCall = apiInterface.editLaporanStatus(id_laporan, id_petugas_sekarang,
                status);
        responLaporanCall.enqueue(new Callback<ResponLaporan>() {
            @Override
            public void onResponse(Call<ResponLaporan> call, Response<ResponLaporan> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success..")
                                .setContentText("Permintaan Berhasil..")
                                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        loadLaporanId(id_laporan);
                                    }
                                })
                                .show();
                    } else {
                        new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Gagal..")
                                .setContentText("Terjadi kesalahan!, Kode : " + kode)
                                .show();
                    }
                } else {
                    new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Gagal..")
                            .setContentText("Terjadi kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponLaporan> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal..")
                        .setContentText("Terjadi kesalahan Sistem!")
                        .show();
            }
        });

    }

    private void sendDone(String id_laporan, String id_petugas_sekarang, String status, String image_send) {

        pDialog = new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponLaporan> responLaporanCall = apiInterface.editLaporanSelesaiTindaki(id_laporan, id_petugas_sekarang,
                status, image_send);
        responLaporanCall.enqueue(new Callback<ResponLaporan>() {
            @Override
            public void onResponse(Call<ResponLaporan> call, Response<ResponLaporan> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success..")
                                .setContentText("Permintaan Berhasil..")
                                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        configIntent();
                                    }
                                })
                                .show();
                    } else {
                        new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Gagal..")
                                .setContentText("Terjadi kesalahan!, Kode : " + kode)
                                .show();
                    }
                } else {
                    new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Gagal..")
                            .setContentText("Terjadi kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponLaporan> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal..")
                        .setContentText("Terjadi kesalahan Sistem!")
                        .show();
            }
        });

    }

    private void configIntent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                pDialog = new SweetAlertDialog(DetailLaporanPetugasActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Menyimpan Data..");
                pDialog.setCancelable(true);
                pDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.dismiss();
                        finish();
                    }
                }, 2500);
            }
        }, 400);
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