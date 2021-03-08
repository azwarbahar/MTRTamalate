package com.skripsi.mtrtamalate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.skripsi.mtrtamalate.ui.masyarakat.MasyarakatActivity;
import com.skripsi.mtrtamalate.ui.petugas.PetugasActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Button btn_masyarakat = findViewById(R.id.btn_masyarakat);
//        Button btn_petugas = findViewById(R.id.btn_petugas);
//
//        btn_masyarakat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, MasyarakatActivity.class));
//            }
//        });
//
//        btn_petugas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, PetugasActivity.class));
//            }
//        });
    }
}