package com.skripsi.mtrtamalate.ui.petugas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skripsi.mtrtamalate.R;

public class PetugasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewPetugas);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_petugas);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

    }
}