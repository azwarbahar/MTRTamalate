package com.skripsi.mtrtamalate.ui.petugas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.skripsi.mtrtamalate.LoginActivity;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.utils.Constanta;

public class AkunFragmentPetugas extends Fragment {

    private RelativeLayout rl_logout;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_akun_petugas, container,false);
        mPreferences = getActivity().getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, getActivity().MODE_PRIVATE);


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

        return view;

    }
}
