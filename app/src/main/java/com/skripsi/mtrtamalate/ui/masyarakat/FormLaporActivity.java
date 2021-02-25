package com.skripsi.mtrtamalate.ui.masyarakat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skripsi.mtrtamalate.R;

public class FormLaporActivity extends AppCompatActivity {

    private RelativeLayout rl_foto;
    private RelativeLayout rl_send;
    private EditText et_keterangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_lapor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rl_foto = findViewById(R.id.rl_foto);
        et_keterangan = findViewById(R.id.et_keterangan);
        rl_send = findViewById(R.id.rl_send);

        rl_foto.setOnClickListener(this::clickFoto);
        rl_send.setOnClickListener(this::clickSend);


    }

    private void clickSend(View view) {
        String keterangan = et_keterangan.getText().toString();
        Toast.makeText(this, "Kirim : "+keterangan, Toast.LENGTH_SHORT).show();
    }

    private void clickFoto(View view) {
        Toast.makeText(this, "klik Foto..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}