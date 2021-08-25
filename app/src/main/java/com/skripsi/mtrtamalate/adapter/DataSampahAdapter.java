package com.skripsi.mtrtamalate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.sampah.ResponSampah;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSampahAdapter extends RecyclerView.Adapter<DataSampahAdapter.MyHolderView> {

    private Context context;
    private ArrayList<String> kelurahans = new ArrayList<String>();

    public DataSampahAdapter(Context context, ArrayList<String> kelurahans) {
        this.context = context;
        this.kelurahans = kelurahans;
    }

    @NonNull
    @Override
    public DataSampahAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_kelurahan_data_sampah, parent, false);
        DataSampahAdapter.MyHolderView myHolderView = new DataSampahAdapter.MyHolderView(view);
        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull DataSampahAdapter.MyHolderView holder, int position) {

        String kelurahan = kelurahans.get(position);
        holder.tv_kelurahan.setText(kelurahan);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponSampah> responSampahCall = apiInterface.getTotalSampahKelurahan(kelurahan);
        responSampahCall.enqueue(new Callback<ResponSampah>() {
            @Override
            public void onResponse(Call<ResponSampah> call, Response<ResponSampah> response) {
                String kode = response.body().getKode();
                if (kode.equals("1")) {
                    String nilai = response.body().getTotal_berat_kelurahan();
                    if (nilai==null){
                        holder.tv_value.setText("0 Kg");
                    } else {
                        holder.tv_value.setText(nilai + " Kg");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponSampah> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return kelurahans.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private TextView tv_kelurahan;
        private TextView tv_value;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            tv_kelurahan = itemView.findViewById(R.id.tv_kelurahan);
            tv_value = itemView.findViewById(R.id.tv_value);

        }
    }
}
