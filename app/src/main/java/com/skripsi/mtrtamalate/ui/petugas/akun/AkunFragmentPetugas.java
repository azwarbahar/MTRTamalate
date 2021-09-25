package com.skripsi.mtrtamalate.ui.petugas.akun;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.skripsi.mtrtamalate.LoginActivity;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.petugas.Petugas;
import com.skripsi.mtrtamalate.models.petugas.ResponsePetugas;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.AkunFragment;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.ImagePickerActivity;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.ViewImageActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AkunFragmentPetugas extends Fragment {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private Petugas petugas;
    private RelativeLayout rl_edit;
    private RelativeLayout rl_password;
    private RelativeLayout rl_logout;
    View view;

    private CircularImageView img_profile;
    private TextView tv_nama;
    private TextView tv_nik;
    private TextView tv_kelurahan;
    private TextView tv_telpon;

    private String nik;
    private String id;
    private String nama;
    private String kelurahan;
    private String telpon;
    private String foto;
    private Bitmap bitmap_foto;

    // Data petugas
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

    private SweetAlertDialog pDialog;
    private SweetAlertDialog pDialog1;
    private static final String TAG = AkunFragment.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;

    private ImageView img_copy_telpon;
    private ImageView img_copy_nik;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_akun_petugas, container, false);
        mPreferences = getActivity().getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, getActivity().MODE_PRIVATE);

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        img_profile = view.findViewById(R.id.img_profile);
        tv_nama = view.findViewById(R.id.tv_nama);
        tv_nik = view.findViewById(R.id.tv_nik);
        tv_kelurahan = view.findViewById(R.id.tv_kelurahan);
        tv_telpon = view.findViewById(R.id.tv_telpon);
        img_copy_telpon = view.findViewById(R.id.img_copy_telpon);
        img_copy_nik = view.findViewById(R.id.img_copy_nik);

        img_profile.setOnClickListener(this::clickFoto);

        rl_edit = view.findViewById(R.id.rl_edit);
        rl_edit.setOnClickListener(this::clickEdit);

        rl_password = view.findViewById(R.id.rl_password);
        rl_password.setOnClickListener(this::clickUbahPassword);

        rl_logout = view.findViewById(R.id.rl_logout);
        rl_logout.setOnClickListener(this::clickLogout);

        loadData();

        img_copy_nik.setOnClickListener(this::clickCopyNik);
        tv_nik.setOnClickListener(this::clickCopyNik);
        img_copy_telpon.setOnClickListener(this::clickCopyTelpon);
        tv_telpon.setOnClickListener(this::clickCopyTelpon);

        return view;

    }

    private void clickLogout(View view) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Logout")
                .setContentText("Ingin Keluar Dari Akun ?")
                .setCancelButton("Tidak", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.apply();
                        editor.clear();
                        editor.commit();
                        getActivity().finish();
                    }
                })
                .show();
    }

    private void clickCopyNik(View view) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("NIK", nik);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copied NIK",
                Toast.LENGTH_SHORT).show();

    }

    private void clickCopyTelpon(View view) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Telpon", telpon);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copied Telpon",
                Toast.LENGTH_SHORT).show();
    }

    private void loadData() {

        id = mPreferences.getString(Constanta.SESSION_ID_PETUGAS, "");
        nik = mPreferences.getString(Constanta.SESSION_NIK_PETUGAS, "");
        nama = mPreferences.getString(Constanta.SESSION_NAMA_PETUGAS, "");
        kelurahan = mPreferences.getString(Constanta.SESSION_KELURAHAN_PETUGAS, "");
        telpon = mPreferences.getString(Constanta.SESSION_TELPON_PETUGAS, "");
        foto = mPreferences.getString(Constanta.SESSION_FOTO_PETUGAS, "");

//        Toast.makeText(getActivity(), mPreferences.getString(Constanta.SESSION_ALAMAT_MASYARAKAT, ""), Toast.LENGTH_SHORT).show();

        tv_nik.setText(nik);
        tv_nama.setText(nama);
        tv_kelurahan.setText(kelurahan);
        tv_telpon.setText(telpon);
        Glide.with(getActivity())
                .load(Constanta.URL_IMG_PETUGAS + foto)
                .into(img_profile);
        pDialog.dismiss();
    }

    private void clickUbahPassword(View view) {
        startActivity(new Intent(getActivity(), UbahPasswordPetugasActivity.class));
    }

    private void clickEdit(View view) {
        startActivity(new Intent(getActivity(), EditAkunPetugasActivity.class));
    }

    private void clickFoto(View view) {

        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }

            @Override
            public void onViewImage() {
                launchViewImage();
            }
        });
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchViewImage() {
//        Toast.makeText(getActivity(), "Lihat Gambar!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ViewImageActivity.class);
        intent.putExtra("role", "petugas");
        intent.putExtra("foto", foto);
        getActivity().startActivity(intent);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    bitmap_foto = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    startUpdatePhoto(bitmap_foto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startUpdatePhoto(Bitmap bitmap_foto) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap_foto.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        String foto_send = Base64.encodeToString(imgByte, Base64.DEFAULT);

        SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponsePetugas> masyarakatCall = apiInterface.editFotoPetugas(id, foto_send);
        masyarakatCall.enqueue(new Callback<ResponsePetugas>() {
            @Override
            public void onResponse(Call<ResponsePetugas> call, Response<ResponsePetugas> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = String.valueOf(response.body().getKode());
                    if (kode.equals("1")) {
                        SweetAlertDialog success = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                        success.setTitleText("Success..");
                        success.setCancelable(false);
                        success.setContentText("Edit Foto Berhasil");
                        success.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                loadDataProfile(id);
                            }
                        });
                        success.show();
                    } else {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Uups..")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Uups..")
                            .setContentText("Terjadi kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePetugas> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Uups..")
                        .setContentText("Terjadi kesalahan!")
                        .show();

            }
        });

    }

    private void loadDataProfile(String id) {

        pDialog1 = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog1.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog1.setTitleText("Menyiapkan Data..");
        pDialog1.setCancelable(true);
        pDialog1.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponsePetugas> responseMasyarakatCall = apiInterface.getPetugasId(id);
        responseMasyarakatCall.enqueue(new Callback<ResponsePetugas>() {
            @Override
            public void onResponse(Call<ResponsePetugas> call, Response<ResponsePetugas> response) {
                if (response.isSuccessful()) {
                    petugas = response.body().getPetugas();
                    initData(petugas);
                }
            }

            @Override
            public void onFailure(Call<ResponsePetugas> call, Throwable t) {
                pDialog1.dismiss();
            }
        });
    }

    private void initData(Petugas petugas) {

        id_pekerja = petugas.getIdPekerja();
        nik_pekerja = petugas.getNikPekerja();
        nama_pekerja = petugas.getNamaPekerja();
        alamat_pekerja = petugas.getAlamatPekerja();
        area_pekerja = petugas.getAreaPekerja();
        telpon_pekerja = petugas.getTelponPekerja();
        usia_pekerja = petugas.getUsiaPekerja();
        kelurahan_pekerja = petugas.getKelurahanPekerja();
        latitude_pekerja = petugas.getLatitudePekerja();
        longitude_pekerja = petugas.getLongitudePekerja();
        password = petugas.getPassword();
        foto_pekerja = petugas.getFotoPekerja();
        status_pekerja = petugas.getStatusPekerja();
        jenis_kelamin_pekerja = petugas.getJenisKelaminPekerja();
        kendaraan_pekerja = petugas.getKendaraanPekerja();
        status_kerja_pekerja = petugas.getStatusKerjaPekerja();
        role_pekerja = petugas.getRolePekerja();

        editor = mPreferences.edit();
        // data
        editor.putString(Constanta.SESSION_ID_PETUGAS, id_pekerja);
        editor.putString(Constanta.SESSION_NIK_PETUGAS, nik_pekerja);
        editor.putString(Constanta.SESSION_NAMA_PETUGAS, nama_pekerja);
        editor.putString(Constanta.SESSION_ALAMAT_PETUGAS, alamat_pekerja);
        editor.putString(Constanta.SESSION_AREA_PETUGAS, area_pekerja);
        editor.putString(Constanta.SESSION_TELPON_PETUGAS, telpon_pekerja);
        editor.putString(Constanta.SESSION_USIA_PETUGAS, usia_pekerja);
        editor.putString(Constanta.SESSION_KELURAHAN_PETUGAS, kelurahan_pekerja);
        editor.putString(Constanta.SESSION_LATITUDE_PETUGAS, latitude_pekerja);
        editor.putString(Constanta.SESSION_LONGITUDE_PETUGAS, longitude_pekerja);
        editor.putString(Constanta.SESSION_PASSWORD_PETUGAS, password);
        editor.putString(Constanta.SESSION_FOTO_PETUGAS, foto_pekerja);
        editor.putString(Constanta.SESSION_STATUS_PETUGAS, status_pekerja);
        editor.putString(Constanta.SESSION_JEKEL_PETUGAS, jenis_kelamin_pekerja);
        editor.putString(Constanta.SESSION_KENDARAAN_PETUGAS, kendaraan_pekerja);
        editor.putString(Constanta.SESSION_STATUS_KERJA_PETUGAS, status_kerja_pekerja);
        editor.putString(Constanta.SESSION_ROLE_PETUGAS, role_pekerja);
        editor.apply();
        pDialog1.dismiss();
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        loadData();
    }
}
