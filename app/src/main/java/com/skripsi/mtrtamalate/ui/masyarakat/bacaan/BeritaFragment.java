package com.skripsi.mtrtamalate.ui.masyarakat.bacaan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.adapter.BeritaAdapter;
import com.skripsi.mtrtamalate.models.berita.Berita;
import com.skripsi.mtrtamalate.models.berita.ResponseBerita;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BeritaFragment extends Fragment {

    View view;
    private RecyclerView rv_bacaan;
    private BeritaAdapter beritaAdapter;
    private ArrayList<Berita> beritas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_bacaan, container, false);

        rv_bacaan = view.findViewById(R.id.rv_bacaan);
//
//        rv_bacaan.setLayoutManager(new LinearLayoutManager(getActivity()));
//        beritaAdapter = new BeritaAdapter(getActivity());
//        rv_bacaan.setAdapter(beritaAdapter);
        loadBerita();
        return  view;
    }

    private void loadBerita() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBerita> responseBeritaCall = apiInterface.getBerita();
        responseBeritaCall.enqueue(new Callback<ResponseBerita>() {
            @Override
            public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {

                beritas = (ArrayList<Berita>) response.body().getBerita();

                rv_bacaan.setLayoutManager(new LinearLayoutManager(getActivity()));
                beritaAdapter = new BeritaAdapter(getActivity(), beritas);
                rv_bacaan.setAdapter(beritaAdapter);
            }

            @Override
            public void onFailure(Call<ResponseBerita> call, Throwable t) {

            }
        });

    }
}
