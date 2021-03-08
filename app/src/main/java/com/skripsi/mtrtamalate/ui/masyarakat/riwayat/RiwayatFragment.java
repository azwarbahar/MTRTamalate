package com.skripsi.mtrtamalate.ui.masyarakat.riwayat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.adapter.RiwayatAdapter;
import com.skripsi.mtrtamalate.models.laporan.Laporan;
import com.skripsi.mtrtamalate.models.laporan.ResponLaporan;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    View view;
    private RecyclerView rv_riwayat;
    private RiwayatAdapter riwayatAdapter;
    private SharedPreferences mPreferences;

    private ImageView img_kosong;
    private CardView cv_search;
    private CardView cv_progressBar;
    private EditText et_cari;
    private SwipeRefreshLayout swipe_continer;

    private ArrayList<Laporan> laporans;
    private String session_id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_riwayat, container,false);
        mPreferences = getActivity().getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, getActivity().MODE_PRIVATE);
        session_id = mPreferences.getString(Constanta.SESSION_ID_MASYARAKAT, "");

        swipe_continer = view.findViewById(R.id.swipe_continer);
        cv_progressBar = view.findViewById(R.id.cv_progressBar);
        img_kosong = view.findViewById(R.id.img_kosong);
        cv_search = view.findViewById(R.id.cv_search);
        et_cari = view.findViewById(R.id.et_cari);
        rv_riwayat = view.findViewById(R.id.rv_riwayat);

        swipe_continer = view.findViewById(R.id.swipe_continer);
        swipe_continer.setOnRefreshListener(this);
        swipe_continer.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark);
        swipe_continer.post(new Runnable() {
            @Override
            public void run() {
                loadData(session_id);
            }
        });



        return view;
    }

    private void loadData(String session_id) {
        onLoadData();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponLaporan> responLaporanCall = apiInterface.laporanMasyarakatStatus(session_id);
        responLaporanCall.enqueue(new Callback<ResponLaporan>() {
            @Override
            public void onResponse(Call<ResponLaporan> call, Response<ResponLaporan> response) {
                swipe_continer.setRefreshing(false);
                if (response.isSuccessful()){
                    String kode = response.body().getKode();
                    if (kode.equals("1")){
                        laporans = (ArrayList<Laporan>) response.body().getResult_laporan_masyarakat();
                        if (laporans.size()<1){
                            onEmptyData();
                        } else {
                            onGetData();
                            rv_riwayat.setLayoutManager(new LinearLayoutManager(getActivity()));
                            riwayatAdapter = new RiwayatAdapter(getActivity(), laporans);
                            rv_riwayat.setAdapter(riwayatAdapter);
                        }

                    } else {
                        onEmptyData();
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Mohon Maaf...")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    onEmptyData();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Mohon Maaf...")
                            .setContentText("Terjadi Kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponLaporan> call, Throwable t) {
                onEmptyData();
                swipe_continer.setRefreshing(false);
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Mohon Maaf...")
                        .setContentText("Terjadi Kesalahan System!")
                        .show();

            }
        });

    }

    @Override
    public void onRefresh() {
        loadData(session_id);
    }

    private void onLoadData() {
        et_cari.setEnabled(false);
        cv_progressBar.setVisibility(View.VISIBLE);
        img_kosong.setVisibility(View.GONE);
        rv_riwayat.setVisibility(View.GONE);
    }

    private void onGetData(){
        et_cari.setEnabled(true);
        cv_progressBar.setVisibility(View.GONE);
        img_kosong.setVisibility(View.GONE);
        rv_riwayat.setVisibility(View.VISIBLE);
    }

    private void onEmptyData(){
        et_cari.setEnabled(false);
        cv_progressBar.setVisibility(View.GONE);
        img_kosong.setVisibility(View.VISIBLE);
        rv_riwayat.setVisibility(View.GONE);
    }
}
