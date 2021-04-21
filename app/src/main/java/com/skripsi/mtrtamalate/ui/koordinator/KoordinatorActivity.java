package com.skripsi.mtrtamalate.ui.koordinator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skripsi.mtrtamalate.R;

public class KoordinatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koordinator);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewKoordinator);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_koordinator);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }
}