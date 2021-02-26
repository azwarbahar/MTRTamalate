package com.skripsi.mtrtamalate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.mtrtamalate.R;

public class DataSampahAdapter extends RecyclerView.Adapter<DataSampahAdapter.MyHolderView> {

    private Context context;

    public DataSampahAdapter(Context context) {
        this.context = context;
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

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        public MyHolderView(@NonNull View itemView) {
            super(itemView);
        }
    }
}
