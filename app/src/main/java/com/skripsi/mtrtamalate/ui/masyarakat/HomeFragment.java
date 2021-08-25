package com.skripsi.mtrtamalate.ui.masyarakat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.adapter.BeritaAdapter;
import com.skripsi.mtrtamalate.adapter.EdukasiAdapter;
import com.skripsi.mtrtamalate.models.berita.Berita;
import com.skripsi.mtrtamalate.models.berita.ResponseBerita;
import com.skripsi.mtrtamalate.models.edukasi.SliderEdukasiItem;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.TitikLokasiActivity;
import com.skripsi.mtrtamalate.ui.masyarakat.bacaan.BacaanActivity;
import com.skripsi.mtrtamalate.ui.masyarakat.sampah.DataSampahActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    View view;
    private Handler slideEdukasiHandler = new Handler();

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private String latitude_masyarakat;
    private String longitude_masyarakat;
    private String pembayaran_masyarakat;

    private LinearLayout menu_bacaan;
    private LinearLayout menu_data_sampah;
    private LinearLayout menu_info;
    private RelativeLayout rl_btn_lapor;

    private TextView tv_show_all;
    private ArrayList<Berita> beritas;

    private ViewPager2 viewPagerHomeEdukasi;
    private RecyclerView rv_berita;
    private BeritaAdapter beritaAdapter;
    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container,false);

        viewPagerHomeEdukasi = view.findViewById(R.id.viewPagerHomeEdukasi);
        rv_berita = view.findViewById(R.id.rv_berita);
        tv_show_all = view.findViewById(R.id.tv_show_all);
        rl_btn_lapor = view.findViewById(R.id.rl_btn_lapor);
        menu_bacaan = view.findViewById(R.id.menu_bacaan);
        menu_data_sampah = view.findViewById(R.id.menu_data_sampah);
        menu_info = view.findViewById(R.id.menu_info);
        mPreferences = getActivity().getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, getActivity().MODE_PRIVATE);

        tv_show_all.setOnClickListener(this::showAll);
        rl_btn_lapor.setOnClickListener(this::menuLapor);
        menu_bacaan.setOnClickListener(this::menuBacaan);
        menu_data_sampah.setOnClickListener(this::menuDataSampah);
        menu_info.setOnClickListener(this::menuInfo);


        List<SliderEdukasiItem> sliderEdukasiItems = new ArrayList<>();
        sliderEdukasiItems.add(new SliderEdukasiItem(R.drawable.edukasi1));
        sliderEdukasiItems.add(new SliderEdukasiItem(R.drawable.edukasi2));
        sliderEdukasiItems.add(new SliderEdukasiItem(R.drawable.edukasi3));

        viewPagerHomeEdukasi.setAdapter(new EdukasiAdapter(sliderEdukasiItems,viewPagerHomeEdukasi));

        viewPagerHomeEdukasi.setClipToPadding(false);
        viewPagerHomeEdukasi.setClipChildren(false);
        viewPagerHomeEdukasi.setOffscreenPageLimit(3);
        viewPagerHomeEdukasi.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPagerHomeEdukasi.setPageTransformer(compositePageTransformer);
        viewPagerHomeEdukasi.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideEdukasiHandler.removeCallbacks(runnable);
                slideEdukasiHandler.postDelayed(runnable, 2000);
            }
        });

        loadBerita();

        return view;
    }

    private void loadBerita() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBerita> responseBeritaCall = apiInterface.getBerita();
        responseBeritaCall.enqueue(new Callback<ResponseBerita>() {
            @Override
            public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {

                beritas = (ArrayList<Berita>) response.body().getBerita();

                rv_berita.setLayoutManager(new LinearLayoutManager(getActivity()));
                beritaAdapter = new BeritaAdapter(getActivity(), beritas);
                rv_berita.setAdapter(beritaAdapter);
            }

            @Override
            public void onFailure(Call<ResponseBerita> call, Throwable t) {

            }
        });

    }

    private void menuLapor(View view) {

        pembayaran_masyarakat = mPreferences.getString(Constanta.SESSION_PEMBAYARAN_MASYARAKAT, "");
        latitude_masyarakat = mPreferences.getString(Constanta.SESSION_LATITUDE_MASYARAKAT, "");
        longitude_masyarakat = mPreferences.getString(Constanta.SESSION_LATITUDE_MASYARAKAT, "");
        if (latitude_masyarakat.equals("-")||longitude_masyarakat.equals("-")||latitude_masyarakat.isEmpty()){
            SweetAlertDialog sweetAlertDialogError = new SweetAlertDialog(getActivity(),
                    SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialogError.setTitleText("Maaf..");
            sweetAlertDialogError.setCancelable(false);
            sweetAlertDialogError.setContentText("Atur Titik Lokasi Terlebih dahulu sebelum mengajukan laporan!");
            sweetAlertDialogError.setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    Intent intent = new Intent(getActivity(), TitikLokasiActivity.class);
                    intent.putExtra("extra_data", "Home");
                    startActivity(intent);
                }
            });
            sweetAlertDialogError.setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                }
            });
            sweetAlertDialogError.show();
        } else {
            if (pembayaran_masyarakat.equals("Sudah")){
                startActivity(new Intent(getActivity(), FormLaporActivity.class));
            } else {

                SweetAlertDialog sweetAlertDialogError = new SweetAlertDialog(getActivity(),
                        SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialogError.setTitleText("Tidak Dapat Melapor");
                sweetAlertDialogError.setCancelable(false);
                sweetAlertDialogError.setContentText("Anda belum melakukan pembayaran");
                sweetAlertDialogError.setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
                sweetAlertDialogError.show();
            }
        }
    }

    private void menuBacaan(View view) {
        startActivity(new Intent(getActivity(), BacaanActivity.class));
    }

    private void menuDataSampah(View view) {
        startActivity(new Intent(getActivity(), DataSampahActivity.class));
    }

    private void menuInfo(View view) {
        Toast.makeText(getActivity(), "Klik Info", Toast.LENGTH_SHORT).show();
    }

    private void showAll(View view) {
        startActivity(new Intent(getActivity(), BacaanActivity.class));
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPagerHomeEdukasi.setCurrentItem(viewPagerHomeEdukasi.getCurrentItem() +1);
        }
    };

}
