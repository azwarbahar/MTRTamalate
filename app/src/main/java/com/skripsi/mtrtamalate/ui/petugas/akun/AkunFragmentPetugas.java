package com.skripsi.mtrtamalate.ui.petugas.akun;

import android.Manifest;
import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
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
    private Masayarkat masayarkat;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_akun_petugas, container,false);
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

        img_profile.setOnClickListener(this::clickFoto);

        rl_edit = view.findViewById(R.id.rl_edit);
        rl_edit.setOnClickListener(this::clickEdit);

        rl_password = view.findViewById(R.id.rl_password);
        rl_password.setOnClickListener(this::clickUbahPassword);


        rl_logout = view.findViewById(R.id.rl_logout);
        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.apply();
                editor.clear();
                editor.commit();
                getActivity().finish();
            }
        });

        loadData();

        return view;

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

    }

    private void clickEdit(View view) {

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
//                    startUpdatePhoto(bitmap_foto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
