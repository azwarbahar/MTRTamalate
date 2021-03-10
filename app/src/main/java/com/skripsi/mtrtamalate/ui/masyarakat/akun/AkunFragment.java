package com.skripsi.mtrtamalate.ui.masyarakat.akun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.skripsi.mtrtamalate.LoginActivity;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.utils.Constanta;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AkunFragment extends Fragment {

    private SharedPreferences mPreferences;
    private RelativeLayout rl_edit;
    private RelativeLayout rl_location;
    private RelativeLayout rl_password;
    private RelativeLayout rl_iuran;
    private RelativeLayout rl_logout;
    View view;


    private CircularImageView img_profile;
    private TextView tv_nama;
    private TextView tv_nik;
    private TextView tv_kelurahan;
    private TextView tv_telpon;

    private String nik;
    private String nama;
    private String kelurahan;
    private String telpon;
    private String foto;

    private SweetAlertDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_akun, container, false);
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
        loadData();

        rl_edit = view.findViewById(R.id.rl_edit);
        rl_edit.setOnClickListener(this::clickEdit);

        rl_password = view.findViewById(R.id.rl_password);
        rl_password.setOnClickListener(this::clickUbahPassword);

        rl_location = view.findViewById(R.id.rl_location);
        rl_location.setOnClickListener(this::clickLokasi);

        rl_logout = view.findViewById(R.id.rl_logout);
        rl_logout.setOnClickListener(this::clickLogout);

        return view;
    }

    private void clickLokasi(View view) {
        startActivity(new Intent(getActivity(), TitikLokasiActivity.class));
    }

    private void clickUbahPassword(View view) {
        startActivity(new Intent(getActivity(), UbahPasswordActivity.class));

    }

    private void clickEdit(View view) {
        startActivity(new Intent(getActivity(), EditAkunActivity.class));
    }

    private void loadData() {

        nik = mPreferences.getString(Constanta.SESSION_NIK_MASYARAKAT, "");
        nama = mPreferences.getString(Constanta.SESSION_NAMA_MASYARAKAT, "");
        kelurahan = mPreferences.getString(Constanta.SESSION_KELURAHAN_MASYARAKAT, "");
        telpon = mPreferences.getString(Constanta.SESSION_TELPON_MASYARAKAT, "");
        foto = mPreferences.getString(Constanta.SESSION_FOTO_MASYARAKAT, "");

        tv_nik.setText(nik);
        tv_nama.setText(nama);
        tv_kelurahan.setText(kelurahan);
        tv_telpon.setText(telpon);
        Glide.with(getActivity())
                .load(Constanta.URL_IMG_MASYARAKAT + foto)
                .into(img_profile);
        pDialog.dismiss();
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
                        mPreferences = getActivity().getSharedPreferences(Constanta.MY_SHARED_PREFERENCES,
                                getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.apply();
                        editor.clear();
                        editor.commit();
                        getActivity().finish();
                    }
                })
                .show();
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
