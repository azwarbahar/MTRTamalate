package com.skripsi.mtrtamalate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.mtrtamalate.R;

public class EdukasiBacaanAdapter extends RecyclerView.Adapter<EdukasiBacaanAdapter.MyHolderView> {

    private Context context;

    public EdukasiBacaanAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public EdukasiBacaanAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_bacaan_edukasi, parent, false);
        EdukasiBacaanAdapter.MyHolderView myHolderView = new EdukasiBacaanAdapter.MyHolderView(view);
        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull EdukasiBacaanAdapter.MyHolderView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        public MyHolderView(@NonNull View itemView) {
            super(itemView);
        }
    }
}
