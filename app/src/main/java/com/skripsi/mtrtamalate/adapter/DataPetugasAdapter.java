package com.skripsi.mtrtamalate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;
import com.skripsi.mtrtamalate.models.petugas.Petugas;
import com.skripsi.mtrtamalate.ui.koordinator.DetailPetugasActivity;
import com.skripsi.mtrtamalate.ui.koordinator.LaporDataSampahActivity;
import com.skripsi.mtrtamalate.ui.koordinator.LaporanPetugasActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.util.ArrayList;

public class DataPetugasAdapter extends RecyclerView.Adapter<DataPetugasAdapter.MyHolderView> {

    private Context context;
    private ArrayList<Petugas> petugases;

    public DataPetugasAdapter(Context context, ArrayList<Petugas> petugases) {
        this.context = context;
        this.petugases = petugases;
    }

    @NonNull
    @Override
    public DataPetugasAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_data_petugas, parent, false);
        DataPetugasAdapter.MyHolderView myHolderView = new DataPetugasAdapter.MyHolderView(view);
        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull DataPetugasAdapter.MyHolderView holder, int position) {

        holder.tv_nama.setText(petugases.get(position).getNamaPekerja());
        holder.tv_status.setText("Sedang : "+petugases.get(position).getStatusKerjaPekerja());
        Glide.with(context)
                .load(Constanta.URL_IMG_PETUGAS + petugases.get(position).getFotoPekerja())
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(holder.img_foto);

        holder.tv_lapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, LaporanPetugasActivity.class));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, DetailPetugasActivity.class));
            }
        });



    }

    @Override
    public int getItemCount() {
        return petugases.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private ImageView img_foto;
        private TextView tv_nama;
        private TextView tv_status;
        private TextView tv_lapor;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            img_foto = itemView.findViewById(R.id.img_foto);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_lapor = itemView.findViewById(R.id.tv_lapor);
        }
    }

    public void filterList(ArrayList<Petugas> petugases1) {
        petugases = petugases1;
        notifyDataSetChanged();
    }
}
