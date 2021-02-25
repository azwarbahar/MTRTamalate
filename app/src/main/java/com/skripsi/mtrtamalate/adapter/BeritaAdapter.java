package com.skripsi.mtrtamalate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skripsi.mtrtamalate.R;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.MyHolderView> {

    private Context context;

    public BeritaAdapter(Context context) {
        this.context = context;
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
