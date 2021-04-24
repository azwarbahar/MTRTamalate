package com.skripsi.mtrtamalate.ui.koordinator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.skripsi.mtrtamalate.R;

public class LaporDataSampahActivity extends AppCompatActivity {

    private CardView cv_tambah;
    private CardView cv_tambah2;
    private CardView cv_kurang;
    private CardView cv_kurang2;

    private TextView tv_jumlah_mobil;
    private TextView tv_jumlah_motor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor_data_sampah);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        cv_kurang = findViewById(R.id.cv_kurang);
        cv_kurang2 = findViewById(R.id.cv_kurang2);
        cv_tambah = findViewById(R.id.cv_tambah);
        cv_tambah2 = findViewById(R.id.cv_tambah2);
        tv_jumlah_mobil = findViewById(R.id.tv_jumlah_mobil);
        tv_jumlah_motor = findViewById(R.id.tv_jumlah_motor);

        cv_kurang.setOnClickListener(this::clickKurangMobil);
        cv_kurang2.setOnClickListener(this::clickKurangMotor);
        cv_tambah.setOnClickListener(this::clickTambahMobil);
        cv_tambah2.setOnClickListener(this::clickTambahMotor);

    }

    private void clickTambahMotor(View view) {
        int awal = Integer.parseInt(tv_jumlah_motor.getText().toString());
        String hasil = String.valueOf(awal + 1);
        tv_jumlah_motor.setText(hasil);
    }

    private void clickTambahMobil(View view) {
        int awal = Integer.parseInt(tv_jumlah_mobil.getText().toString());
        String hasil = String.valueOf(awal + 1);
        tv_jumlah_mobil.setText(hasil);
    }

    private void clickKurangMotor(View view) {
        int awal = Integer.parseInt(tv_jumlah_motor.getText().toString());
        int hasil = awal - 1;
        if (hasil<1){
            tv_jumlah_motor.setText("0");
        } else {
            tv_jumlah_motor.setText(String.valueOf(hasil));
        }
    }

    private void clickKurangMobil(View view) {
        int awal = Integer.parseInt(tv_jumlah_mobil.getText().toString());
        int hasil = awal - 1;
        if (hasil<1){
            tv_jumlah_mobil.setText("0");
        } else {
            tv_jumlah_mobil.setText(String.valueOf(hasil));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}