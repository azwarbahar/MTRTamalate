package com.skripsi.mtrtamalate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.berita.Berita;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.util.ArrayList;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.MyHolderView> {

    private Context context;
    private ArrayList<Berita> beritas;

    public BeritaAdapter(Context context, ArrayList<Berita> beritas) {
        this.context = context;
        this.beritas = beritas;
    }

    @NonNull
    @Override
    public BeritaAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_berita, parent, false);
        BeritaAdapter.MyHolderView myHolderView = new BeritaAdapter.MyHolderView(view);
        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull BeritaAdapter.MyHolderView holder, int position) {

        holder.tv_judul.setText(beritas.get(position).getJudulBerita());
        holder.tv_penulis.setText("Oleh : " + beritas.get(position).getPenulisBerita());
        holder.tv_deskripsi.setText(beritas.get(position).getIsiBerita());
        holder.tv_tanggal.setText(beritas.get(position).getCreatedAt());
        Glide.with(context)
                .load(Constanta.URL_IMG_BERITA + beritas.get(position).getFotoBerita())
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(holder.img_foto);

    }

    @Override
    public int getItemCount() {
        return beritas.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private TextView tv_judul;
        private TextView tv_penulis;
        private TextView tv_deskripsi;
        private TextView tv_tanggal;
        private ImageView img_foto;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            img_foto = itemView.findViewById(R.id.img_foto);
            tv_judul = itemView.findViewById(R.id.tv_judul);
            tv_penulis = itemView.findViewById(R.id.tv_penulis);
            tv_deskripsi = itemView.findViewById(R.id.tv_deskripsi);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);

        }
    }
}
