package com.skripsi.mtrtamalate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.edukasi.SliderEdukasiItem;

import java.util.List;

public class EdukasiAdapter extends RecyclerView.Adapter<EdukasiAdapter.MyHolderView> {

    private List<SliderEdukasiItem> sliderEdukasiItems;
    private ViewPager2 viewPager2;

    public EdukasiAdapter(List<SliderEdukasiItem> sliderEdukasiItems, ViewPager2 viewPager2) {
        this.sliderEdukasiItems = sliderEdukasiItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public EdukasiAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_edukasi_cotainer, parent, false);
        EdukasiAdapter.MyHolderView myHolderView = new EdukasiAdapter.MyHolderView(view);

        return myHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull EdukasiAdapter.MyHolderView holder, int position) {
        holder.setImage(sliderEdukasiItems.get(position));
        if (position == sliderEdukasiItems.size() - 2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderEdukasiItems.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private RoundedImageView roundedImageView;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            roundedImageView = itemView.findViewById(R.id.edukasiSlide);

        }

        void setImage(SliderEdukasiItem sliderEdukasiItem) {
            roundedImageView.setImageResource(sliderEdukasiItem.getImage());
        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderEdukasiItems.addAll(sliderEdukasiItems);
            notifyDataSetChanged();
        }
    };

}
