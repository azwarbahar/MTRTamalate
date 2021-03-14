package com.skripsi.mtrtamalate.ui.masyarakat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.laporan.ResponLaporan;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.TitikLokasiActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormLaporActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private RelativeLayout rl_foto;
    private RelativeLayout rl_send;
    private EditText et_keterangan;
    private ImageView img_foto;
    private Bitmap bitmap_gambar;

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

    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_lapor);
        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        img_foto = findViewById(R.id.img_foto);
        rl_foto = findViewById(R.id.rl_foto);
        et_keterangan = findViewById(R.id.et_keterangan);
        rl_send = findViewById(R.id.rl_send);

        rl_foto.setOnClickListener(this::clickFoto);
        rl_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pDialog = new SweetAlertDialog(FormLaporActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                String keterangan = et_keterangan.getText().toString();
                if (img_foto.getDrawable() == null) {
                    pDialog.dismiss();
                    new SweetAlertDialog(FormLaporActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Uups..")
                            .setContentText("Bukti foto belum dilengkapi!")
                            .show();
                } else if (keterangan.isEmpty() || keterangan.equals("")) {
                    pDialog.dismiss();
                    new SweetAlertDialog(FormLaporActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Uups..")
                            .setContentText("Mohon lengkapi Keterangan!")
                            .show();
                } else {

                    new SweetAlertDialog(FormLaporActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Mengirim")
                            .setContentText("Mengirim Laporan langsung ke Pengelolah sampah dan petugas sampah ?")
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
                                    pDialog.show();
                                    clickSend(keterangan);
                                }
                            })
                            .show();
                }
            }
        });

        loadDataSession();

    }

    private void loadDataSession() {
        id_masyarakat = mPreferences.getString(Constanta.SESSION_ID_MASYARAKAT, "");
        nik_masyarakat = mPreferences.getString(Constanta.SESSION_NIK_MASYARAKAT, "");
        nama_masyarakat = mPreferences.getString(Constanta.SESSION_NAMA_MASYARAKAT, "");
        alamat_masyarakat = mPreferences.getString(Constanta.SESSION_ALAMAT_MASYARAKAT, "");
        area_masyarakat = mPreferences.getString(Constanta.SESSION_AREA_MASYARAKAT, "");
        telpon_masyarakat = mPreferences.getString(Constanta.SESSION_TELPON_MASYARAKAT, "");
        usia_masyarakat = mPreferences.getString(Constanta.SESSION_USIA_MASYARAKAT, "");
        kelurahan_masyarakat = mPreferences.getString(Constanta.SESSION_KELURAHAN_MASYARAKAT, "");
        latitude_masyarakat = mPreferences.getString(Constanta.SESSION_LATITUDE_MASYARAKAT, "");
        longitude_masyarakat = mPreferences.getString(Constanta.SESSION_LONGITUDE_MASYARAKAT, "");
        password_masyarakat = mPreferences.getString(Constanta.SESSION_PASSWORD_MASYARAKAT, "");
        foto_masyarakat = mPreferences.getString(Constanta.SESSION_FOTO_MASYARAKAT, "");
        status_masyarakat = mPreferences.getString(Constanta.SESSION_STATUS_MASYARAKAT, "");
        pembayaran_masyarakat = mPreferences.getString(Constanta.SESSION_PEMBAYARAN_MASYARAKAT, "");
        status_marker = mPreferences.getString(Constanta.SESSION_STATUS_MARKER, "");
    }

    private void clickSend(String keterangan) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponLaporan> responLaporanCall = apiInterface.AddLaporan(keterangan, imgToString(), latitude_masyarakat,
                longitude_masyarakat, alamat_masyarakat, nik_masyarakat, kelurahan_masyarakat, area_masyarakat, id_masyarakat);
        responLaporanCall.enqueue(new Callback<ResponLaporan>() {
            @Override
            public void onResponse(Call<ResponLaporan> call, Response<ResponLaporan> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        new SweetAlertDialog(FormLaporActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success..")
                                .setContentText("Laporan Berhasil disampaikan..")
                                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        configIntent();
                                    }
                                })
                                .show();
                    } else {
                        new SweetAlertDialog(FormLaporActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Gagal..")
                                .setContentText("Terjadi kesalahan!")
                                .show();
                    }
                } else {
                    new SweetAlertDialog(FormLaporActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Gagal..")
                            .setContentText("Terjadi kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponLaporan> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(FormLaporActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal..")
                        .setContentText("Terjadi kesalahan!")
                        .show();
            }
        });
    }

    private void configIntent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                pDialog = new SweetAlertDialog(FormLaporActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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

    private String imgToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap_gambar.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void clickFoto(View view) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showDialogOptionImage(FormLaporActivity.this);
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

    private void showDialogOptionImage(Context context) {
        final CharSequence[] options = {"Mengambil gambar", "Pilih dari galeri", "Tutup"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Mengambil gambar")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Pilih dari galeri")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                } else if (options[item].equals("Tutup")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap resized = (Bitmap) data.getExtras().get("data");
                        bitmap_gambar = Bitmap.createScaledBitmap(resized, 1000, 1000, false);
                        img_foto.setImageBitmap(bitmap_gambar);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                bitmap_gambar = BitmapFactory.decodeFile(picturePath);
                                img_foto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}