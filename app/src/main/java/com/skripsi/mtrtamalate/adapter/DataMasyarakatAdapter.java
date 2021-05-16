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
import com.skripsi.mtrtamalate.models.laporan.Laporan;
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;
import com.skripsi.mtrtamalate.ui.koordinator.DetailMasyarakatActivity;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.util.ArrayList;

public class DataMasyarakatAdapter extends RecyclerView.Adapter<DataMasyarakatAdapter.MyHolderview> {

    private Context context;
    private ArrayList<Masayarkat> masayarkats;

    public DataMasyarakatAdapter(Context context, ArrayList<Masayarkat> masayarkats) {
        this.context = context;
        this.masayarkats = masayarkats;
    }

    @NonNull
    @Override
    public DataMasyarakatAdapter.MyHolderview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_data_masyarakat, parent, false);
        DataMasyarakatAdapter.MyHolderview myHolderView = new DataMasyarakatAdapter.MyHolderview(view);
        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull DataMasyarakatAdapter.MyHolderview holder, int position) {

        holder.tv_nama.setText(masayarkats.get(position).getNamaMasyarakat());
        holder.tv_nik.setText("Akun : "+masayarkats.get(position).getStatusMasyarakat());
        holder.tv_status.setText(masayarkats.get(position).getPembayaranMasyarakat());

        Glide.with(context)
                .load(Constanta.URL_IMG_MASYARAKAT + masayarkats.get(position).getFotoMasyarakat())
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(holder.img_foto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailMasyarakatActivity.class);
                intent.putExtra("id_masyarakat", masayarkats.get(position).getIdMasyarakat());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return masayarkats.size();
    }

    public class MyHolderview extends RecyclerView.ViewHolder {

        private ImageView img_foto;
        private TextView tv_nama;
        private TextView tv_nik;
        private TextView tv_status;

        public MyHolderview(@NonNull View itemView) {
            super(itemView);

            img_foto = itemView.findViewById(R.id.img_foto);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_nik = itemView.findViewById(R.id.tv_nik);
            tv_status = itemView.findViewById(R.id.tv_status);

        }
    }

    public void filterList(ArrayList<Masayarkat> masayarkats1) {
        masayarkats = masayarkats1;
        notifyDataSetChanged();
    }
}
