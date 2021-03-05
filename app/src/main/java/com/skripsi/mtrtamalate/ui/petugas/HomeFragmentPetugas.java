package com.skripsi.mtrtamalate.ui.petugas;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skripsi.mtrtamalate.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

public class HomeFragmentPetugas extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    View view;
    private Drawable vectorDrawble;
    private Bitmap bitmap;
    private MarkerOptions markerOptionsPesanan;
    private CardView cv_laporan;

    private SlidingUpPanelLayout sliding_layout;

    LatLng latLng;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_petugas, container, false);

        sliding_layout = view.findViewById(R.id.sliding_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        cv_laporan = view.findViewById(R.id.cv_laporan);
        cv_laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPanel();
            }
        });

        ImageView img_my_location = view.findViewById(R.id.img_my_location);
        img_my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLngzoom = new LatLng(-5.181629, 119.431791);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngzoom,13));
            }
        });


        return view;

    }

    private void showPanel() {

        if (sliding_layout != null &&
                (sliding_layout.getPanelState() == PanelState.EXPANDED || sliding_layout.getPanelState() == PanelState.ANCHORED)) {
            sliding_layout.setPanelState(PanelState.COLLAPSED);
        } else {
            sliding_layout.setPanelState(PanelState.ANCHORED);
        }

    }

    private BitmapDescriptor bitmapDescriptor(Context context) {
        int height = 70;
        int width = 70;
        vectorDrawble = ContextCompat.getDrawable(context, R.drawable.icon_mylocation);
        assert vectorDrawble != null;
        vectorDrawble.setBounds(0, 0, width, height);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawble.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        latLng = new LatLng(-5.181629, 119.431791);
        markerOptionsPesanan = new MarkerOptions().title("My Location")
                .icon(bitmapDescriptor(getActivity()))
                .position(latLng);
        map.addMarker(markerOptionsPesanan);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.map_style_grey));

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }

    }
}
