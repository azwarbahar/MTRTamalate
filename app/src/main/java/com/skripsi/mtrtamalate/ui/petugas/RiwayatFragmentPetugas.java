package com.skripsi.mtrtamalate.ui.petugas;

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
import com.skripsi.mtrtamalate.adapter.RiwayatAdapter;

public class RiwayatFragmentPetugas extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_riwayat_petugas, container,false);

        RecyclerView rv_riwayat = view.findViewById(R.id.rv_riwayat);
//        rv_riwayat.setLayoutManager(new LinearLayoutManager(getActivity()));
//        RiwayatAdapter riwayatAdapter = new RiwayatAdapter(getActivity());
//        rv_riwayat.setAdapter(riwayatAdapter);

        return view;
    }
}
