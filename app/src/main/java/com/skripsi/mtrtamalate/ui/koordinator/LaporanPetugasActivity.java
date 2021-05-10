package com.skripsi.mtrtamalate.ui.koordinator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.kendaraan.Kendaraan;
import com.skripsi.mtrtamalate.models.kendaraan.ResponseKendaraan;
import com.skripsi.mtrtamalate.models.petugas.Petugas;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.masyarakat.FormLaporActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanPetugasActivity extends AppCompatActivity {

    private CircularImageView img_profile;
    private TextView tv_nama;
    private TextView tv_kendaraan;
    private Petugas petugas_parcelable;
    private Kendaraan kendaraan;
    String alamat_latlig;
    private EditText et_keterangan;
    private EditText et_berat;

    private RelativeLayout rl_send;
    private RelativeLayout rl_foto;
    private Bitmap bitmap_gambar;
    private ImageView img_foto;
    private Uri imageUri;

    private String berat_send = "-";
    private String keterangan_send = "-";
    private String alamat_laporan = "-";
    private String latitude_laporan = "-";
    private String longitude_laporan = "-";
    private String koordinat = "-";
    private String timestamp = "-";
    private LocationManager locationManager;

    private boolean go_to_setting = false;
    private boolean is_ready_gps;


    private void getAddress(double latitud, double longitud) {

        Geocoder geocoder;
        List<Address> addressList;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addressList = geocoder.getFromLocation(latitud, longitud, 1);
            String address = addressList.get(0).getAddressLine(0);
            alamat_laporan = address;
//            et_keterangan.setText(alamat_latlig);
//            Toast.makeText(this, alamat_latlig, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_petugas);
        petugas_parcelable = getIntent().getParcelableExtra("data_petugas");
        // Set Time Date

        Thread myThread = null;

        Runnable runnable = new CountDownRunner();
        myThread = new Thread(runnable);
        myThread.start();

        File direct = new File(Environment.getExternalStorageDirectory() + "/MTR_Tamalate");
        if (!direct.exists()) {
            direct.mkdir();//directory is created;
            File folder = new File(direct.getAbsolutePath(), "Laporan Petugas");
            if (!folder.exists()) {
                folder.mkdir();
            }
        } else {
            File folder = new File(direct.getAbsolutePath(), "Laporan Petugas");
            if (!folder.exists()) {
                folder.mkdir();
            }
        }

        rl_send = findViewById(R.id.rl_send);
        et_keterangan = findViewById(R.id.et_keterangan);
        et_berat = findViewById(R.id.et_berat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            is_ready_gps = false;
            buildAlertMessageNoGps();
        } else {
            is_ready_gps = true;
            LocationListener locationListener = new MyLocationListener();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 3000, 10, locationListener);

        }


        img_profile = findViewById(R.id.img_profile);
        tv_nama = findViewById(R.id.tv_nama);
        tv_kendaraan = findViewById(R.id.tv_kendaraan);
        rl_foto = findViewById(R.id.rl_foto);
        img_foto = findViewById(R.id.img_foto);

        rl_foto.setOnClickListener(this::clickFoto);
        rl_send.setOnClickListener(this::clickSend);

        loadDataPetugas(petugas_parcelable);

    }

    private void clickSend(View view) {
        if (is_ready_gps) {
            if (ready_image()) {
                if (ready_berat()) {
                    if (ready_koordinat()){
                        new SweetAlertDialog(LaporanPetugasActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success..")
                                .setContentText("Laporan Berhasil disampaikan..")
                                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        // Koordinat kosong
                        new SweetAlertDialog(LaporanPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Gagal..")
                                .setContentText("Koordinat Kosong")
                                .show();
                    }
                } else {
                    // berat kosong
                    new SweetAlertDialog(LaporanPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Gagal..")
                            .setContentText("Berat Kosong")
                            .show();
                }

            } else {
                // imgae belum diset
                new SweetAlertDialog(LaporanPetugasActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal..")
                        .setContentText("Gambar Kosong")
                        .show();
            }

        } else {
            // gps blom aktip
            buildAlertMessageNoGps();
        }
    }

    private boolean ready_image() {
        return img_foto.getDrawable() != null;
    }

    private boolean ready_berat() {
        berat_send = et_berat.getText().toString();
        return !berat_send.isEmpty() && !berat_send.equals("-");
    }

    private boolean ready_koordinat() {
        return !koordinat.equals("-");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (go_to_setting) {
            locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                is_ready_gps = false;
                buildAlertMessageNoGps();
            } else {
                is_ready_gps = true;
                LocationListener locationListener = new MyLocationListener();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 3000, 10, locationListener);

            }
        }

        IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        this.registerReceiver(gpsSwitchStateReceiver, filter);


    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(gpsSwitchStateReceiver);
    }

    private BroadcastReceiver gpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (isGpsEnabled || isNetworkEnabled) {
                    is_ready_gps = true;
                } else {
                    // Handle Location turned OFF
                    is_ready_gps = false;
                }
            }
        }
    };

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS Anda sepertinya dinonaktifkan, apakah Anda ingin mengaktifkannya?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        go_to_setting = true;
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        is_ready_gps = false;
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void clickFoto(View view) {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            File file = new File(Environment.getExternalStorageDirectory(),
                                    "/MTR_Tamalate/Laporan Petugas/photo_" + timeStamp + ".png");
                            imageUri = Uri.fromFile(file);

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 0);

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

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (requestCode == 0) {
                        if (resultCode == RESULT_OK) {
                            try {
                                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(), imageUri);
                                img_foto.setImageBitmap(thumbnail);
                                String imageurl = getRealPathFromURI(imageUri);
//                                Toast.makeText(this, imageurl, Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                        Bitmap resized = (Bitmap) data.getExtras().get("data");
//                        bitmap_gambar = Bitmap.createScaledBitmap(resized, 1000, 1000, false);
//                        img_foto.setImageBitmap(bitmap_gambar);
                        }
                    }
                    break;
            }
        }
    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location loc) {
//            latitude_laporan
            latitude_laporan = String.valueOf(loc.getLatitude());
            longitude_laporan = String.valueOf(loc.getLongitude());
            koordinat = latitude_laporan + " - " + longitude_laporan;
            getAddress(loc.getLatitude(), loc.getLongitude());

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            is_ready_gps = false;
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            is_ready_gps = true;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    private void loadDataPetugas(Petugas petugas_parcelable) {

        tv_nama.setText(petugas_parcelable.getNamaPekerja());
        Glide.with(this)
                .load(Constanta.URL_IMG_PETUGAS + petugas_parcelable.getFotoPekerja())
                .into(img_profile);

        loadDataKendaraan(petugas_parcelable.getKendaraanPekerja());


    }

    private void loadDataKendaraan(String kendaraanPekerja) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseKendaraan> responseKendaraanCall = apiInterface.getKendaraanPetugas(kendaraanPekerja);
        responseKendaraanCall.enqueue(new Callback<ResponseKendaraan>() {
            @Override
            public void onResponse(Call<ResponseKendaraan> call, Response<ResponseKendaraan> response) {
                kendaraan = response.body().getKendaraan();
                initKendaraan(kendaraan);
            }

            @Override
            public void onFailure(Call<ResponseKendaraan> call, Throwable t) {

            }
        });

    }

    private void initKendaraan(Kendaraan kendaraan) {

        tv_kendaraan.setText("Kendaraan : " + kendaraan.getNamaKendaraan() +
                " - " + kendaraan.getNomorKendaraan());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class CountDownRunner implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doGetTime();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    private void doGetTime() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String millisInString = dateFormat.format(new Date());
            timestamp = millisInString;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}