package com.skripsi.mtrtamalate.ui.masyarakat.bacaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.adapter.BacaanPagerAdapter;

public class BacaanActivity extends AppCompatActivity {

    private ViewPager bacaan_viewpager;
    private TabLayout bacaan_tablayout;
    private BacaanPagerAdapter bacaanPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bacaan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bacaan_viewpager = findViewById(R.id.bacaan_viewpager);
        bacaan_tablayout = findViewById(R.id.bacaan_tablayout);

        bacaanPagerAdapter = new BacaanPagerAdapter(getSupportFragmentManager());
        bacaanPagerAdapter.AddFragment(new EdukasiFragment(), "Edukasi");
        bacaanPagerAdapter.AddFragment(new BeritaFragment(), "Berita");
        bacaan_viewpager.setAdapter(bacaanPagerAdapter);
        bacaan_tablayout.setupWithViewPager(bacaan_viewpager);

        EdukasiFragment proccessOrderFragment = new EdukasiFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bacaan_viewpager, proccessOrderFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}