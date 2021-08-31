package com.skripsi.mtrtamalate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.laporan.Laporan;
import com.skripsi.mtrtamalate.ui.masyarakat.riwayat.DetailRiwayatActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.util.ArrayList;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.MyHolderView> {

    private Context context;
    private ArrayList<Laporan> laporans;

    public RiwayatAdapter(Context context, ArrayList<Laporan> laporans) {
        this.context = context;
        this.laporans = laporans;
    }

    @NonNull
    @Override
    public RiwayatAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_laporan, parent, false);
        RiwayatAdapter.MyHolderView myHolderView = new RiwayatAdapter.MyHolderView(view);
        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatAdapter.MyHolderView holder, int position) {

        String id_laporan = laporans.get(position).getIdLaporan();
        String status = laporans.get(position).getStausLaporan();
        String tanggal = laporans.get(position).getCreatedAt();

        holder.tv_tanggal.setText("Waktu : " + tanggal);


        switch (status) {
            case "New":
                holder.tv_status.setText("Terbaru");
                Glide.with(context)
                        .load(R.drawable.icon_hourglass)
                        .into(holder.img_status);
                break;
            case "Proccess":
                holder.tv_status.setText("Diproses");
                Glide.with(context)
                        .load(R.drawable.icon_hourglass)
                        .into(holder.img_status);
                break;
            case "Done":
                holder.tv_status.setText("Selesai");
                Glide.with(context)
                        .load(R.drawable.icon_success)
                        .into(holder.img_status);
                break;
            case "Cancel":
                holder.tv_status.setText("Dibatalkan");
                Glide.with(context)
                        .load(R.drawable.icon_cancel)
                        .into(holder.img_status);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailRiwayatActivity.class);
                intent.putExtra("extra_data_riwayat", laporans.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return laporans.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private TextView tv_status;
        private ImageView img_status;
        private TextView tv_tanggal;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            tv_status = itemView.findViewById(R.id.tv_status);
            img_status = itemView.findViewById(R.id.img_status);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);

        }
    }

    public void filterList(ArrayList<Laporan> laporans1) {
        laporans = laporans1;
        notifyDataSetChanged();
    }
}
