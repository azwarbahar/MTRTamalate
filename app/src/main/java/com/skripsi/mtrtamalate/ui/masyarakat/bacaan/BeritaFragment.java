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

public class BeritaFragment extends Fragment {

    View view;
    private RecyclerView rv_bacaan;
    private BeritaAdapter beritaAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_bacaan, container, false);

        rv_bacaan = view.findViewById(R.id.rv_bacaan);

        rv_bacaan.setLayoutManager(new LinearLayoutManager(getActivity()));
        beritaAdapter = new BeritaAdapter(getActivity());
        rv_bacaan.setAdapter(beritaAdapter);

        return  view;
    }
}
