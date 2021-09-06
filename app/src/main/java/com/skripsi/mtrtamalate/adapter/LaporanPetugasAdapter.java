package com.skripsi.mtrtamalate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.laporan.Laporan;
import com.skripsi.mtrtamalate.ui.koordinator.DetailLaporanActivity;
import com.skripsi.mtrtamalate.ui.petugas.DetailLaporanPetugasActivity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LaporanPetugasAdapter extends RecyclerView.Adapter<LaporanPetugasAdapter.MyHolderView> {

    private Context context;
    private ArrayList<Laporan> laporans;
    private String role;
    private boolean ready_tindaki;

    public LaporanPetugasAdapter(Context context, ArrayList<Laporan> laporans, String role, boolean ready_tindaki) {
        this.context = context;
        this.laporans = laporans;
        this.role = role;
        this.ready_tindaki = ready_tindaki;
    }

    @NonNull
    @Override
    public LaporanPetugasAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_laporan_pekerja, parent, false);
        LaporanPetugasAdapter.MyHolderView myHolderView = new LaporanPetugasAdapter.MyHolderView(view);
        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanPetugasAdapter.MyHolderView holder, int position) {

        holder.tv_alamat.setText(laporans.get(position).getAlamatLaporan());
        holder.tv_keterangan.setText(laporans.get(position).getKeteranganLaporan());
        holder.tv_tanggal.setText(laporans.get(position).getCreatedAt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (role.equals("petugas")) {
                    if (ready_tindaki){
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Maaf")
                                .setContentText("Terdapat laporan yang belum terselesaikan!")
                                .show();
                    } else {
                        Intent intent = new Intent(context, DetailLaporanPetugasActivity.class);
                        intent.putExtra("extra_data", laporans.get(position));
                        context.startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(context, DetailLaporanActivity.class);
                    intent.putExtra("extra_data", laporans.get(position));
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return laporans.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private TextView tv_alamat;
        private TextView tv_keterangan;
        private TextView tv_tanggal;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            tv_alamat = itemView.findViewById(R.id.tv_alamat);
            tv_keterangan = itemView.findViewById(R.id.tv_keterangan);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);

        }
    }
}
