package com.skripsi.mtrtamalate.ui.masyarakat.akun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.utils.Constanta;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        PhotoView photoView = (PhotoView) findViewById(R.id.img_zoom);
        ImageView img_close = findViewById(R.id.img_close);
        Bundle bundle = getIntent().getExtras();
        String foto = bundle.getString("foto");
        Glide.with(this)
                .load(Constanta.URL_IMG_MASYARAKAT + foto)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(photoView);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}