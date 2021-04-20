package com.skripsi.mtrtamalate.ui.petugas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.adapter.LaporanPetugasAdapter;
import com.skripsi.mtrtamalate.models.laporan.Laporan;
import com.skripsi.mtrtamalate.models.laporan.ResponLaporan;
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.models.petugas.ResponsePetugas;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.ui.masyarakat.FormLaporActivity;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.EditAkunActivity;
import com.skripsi.mtrtamalate.ui.masyarakat.akun.TitikLokasiActivity;
import com.skripsi.mtrtamalate.utils.Constanta;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragmentPetugas extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private LatLng latLng;
    private LatLng latLng_petugas;

    private GoogleMap map;
    private GoogleMap mapLaporan;
    View view;
    private Drawable vectorDrawble;
    private Bitmap bitmap;
    private MarkerOptions markerOptionsPesanan;
    private CardView cv_laporan;
    private CardView cv_masyarakat;

    private SlidingUpPanelLayout sliding_layout;

    private Location currentLocation;
    private LocationManager mLocationManager;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;

    Location mLastLocation;
    Marker mCurrLocationMarker;

    private GoogleApiClient mGoogleApiClient;
    private boolean isPermission;
    private long UPDATE_INTERVAL = 2000;
    private long FASTES_INTERVAL = 5000;

    private boolean first_time = true;
    private View dialogView;
    private Geocoder geocoder;

    // Data petugas
    private String id_pekerja;
    private String nik_pekerja;
    private String nama_pekerja;
    private String jenis_kelamin_pekerja;
    private String usia_pekerja;
    private String alamat_pekerja;
    private String latitude_pekerja;
    private String longitude_pekerja;
    private String telpon_pekerja;
    private String kelurahan_pekerja;
    private String password;
    private String area_pekerja;
    private String kendaraan_pekerja;
    private String foto_pekerja;
    private String status_pekerja;
    private String status_kerja_pekerja;
    private String role_pekerja;

    private String addres_latlig_petugas;


    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private ImageView btn_jenis_map;
    private SweetAlertDialog pDialog;

    private ArrayList<Masayarkat> masayarkats;
    private ArrayList<Laporan> laporans;

    private RecyclerView rv_laporan;
    private LaporanPetugasAdapter laporanPetugasAdapter;
    private TextView tv_jumlah_laporan;
    private ImageView img_kosong;

    HashMap<String, Masayarkat> markerMapMasyarakat = new HashMap<String, Masayarkat>();
    HashMap<String, Laporan> markerMapLaporan = new HashMap<String, Laporan>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        geocoder = new Geocoder(getActivity(), Locale.getDefault());
//        startLocationUpdate();
        buildGoogleApiClient();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_petugas, container, false);

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

        mPreferences = getActivity().getSharedPreferences(Constanta.MY_SHARED_PREFERENCES,
                getActivity().MODE_PRIVATE);

        sliding_layout = view.findViewById(R.id.sliding_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        tv_jumlah_laporan = view.findViewById(R.id.tv_jumlah_laporan);
        rv_laporan = view.findViewById(R.id.rv_laporan);
        img_kosong = view.findViewById(R.id.img_kosong);

        cv_laporan = view.findViewById(R.id.cv_laporan);
        cv_laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPanel();
            }
        });

        cv_masyarakat = view.findViewById(R.id.cv_masyarakat);
        cv_masyarakat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DataMasyarakatActivity.class));
            }
        });

        btn_jenis_map = view.findViewById(R.id.btn_jenis_map);
        ImageView img_my_location = view.findViewById(R.id.img_my_location);
        img_my_location.setVisibility(View.GONE);
//        img_my_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng_petugas,13));
//            }
//        });
        loadDataSession();

        btn_jenis_map.setOnClickListener(this::clickjenisMap);

        return view;

    }

    private void loadDataLaporan() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponLaporan> laporanCall = apiInterface.getLaporanMapPetugas(kelurahan_pekerja, area_pekerja, "Proccess");
        laporanCall.enqueue(new Callback<ResponLaporan>() {
            @Override
            public void onResponse(Call<ResponLaporan> call, Response<ResponLaporan> response) {
                if (response.isSuccessful()) {
                    String kode = response.body().getKode();
                    if (kode.equals("1")) {
                        laporans = (ArrayList<Laporan>) response.body().getResult_marker_laporan();
                        if (laporans.size() > 0){

                            img_kosong.setVisibility(View.GONE);
                            rv_laporan.setVisibility(View.VISIBLE);
                            laporanPetugasAdapter = new LaporanPetugasAdapter(getActivity(),laporans);
                            rv_laporan.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rv_laporan.setAdapter(laporanPetugasAdapter);
                            tv_jumlah_laporan.setText(String.valueOf(laporans.size()));
//                        initMarkerLaporan(laporans);
                        } else {
                            img_kosong.setVisibility(View.VISIBLE);
                            rv_laporan.setVisibility(View.GONE);
                            tv_jumlah_laporan.setText(String.valueOf(laporans.size()));
                        }
                    } else {
                        img_kosong.setVisibility(View.VISIBLE);
                        rv_laporan.setVisibility(View.GONE);
                        showSnackMessage(response.body().getPesan());
                        tv_jumlah_laporan.setText(String.valueOf(laporans.size()));
                    }
                } else {
                    img_kosong.setVisibility(View.VISIBLE);
                    rv_laporan.setVisibility(View.GONE);
                    showSnackMessage("Terjadi Kesalahan Saat Memproses Data Laporan!");
                    tv_jumlah_laporan.setText(String.valueOf(laporans.size()));
                }
            }

            @Override
            public void onFailure(Call<ResponLaporan> call, Throwable t) {
                img_kosong.setVisibility(View.VISIBLE);
                rv_laporan.setVisibility(View.GONE);
                showSnackMessage("Terjadi Kesalahan Sistem Saat Memproses Data Laporan!");
                tv_jumlah_laporan.setText(String.valueOf(laporans.size()));
            }
        });

    }

    private void initMarkerLaporan(ArrayList<Laporan> laporans) {
        tv_jumlah_laporan.setText(String.valueOf(laporans.size()));
        for (int i = 0; i < laporans.size(); i++) {
            double latitude_masyarakat = Double.parseDouble(laporans.get(i).getLatitudeLaporan());
            double longitude_masyarakat = Double.parseDouble(laporans.get(i).getLongitudeLaporan());
            Marker marker = mapLaporan.addMarker(new MarkerOptions().title("Lokasi Laporan")
                    .icon(bitmapDescriptorLaporan(getActivity()))
                    .position(new LatLng(latitude_masyarakat, longitude_masyarakat)));
            String idmark = marker.getId();
            markerMapLaporan.put(idmark, laporans.get(i));

            SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            assert supportMapFragment != null;
            supportMapFragment.getMapAsync(HomeFragmentPetugas.this);
        }
    }

    private void loadDataMasyarakat() {

        pDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseMasyarakat> masyarakatCall = apiInterface.getAllLokasiMasyarakat(kelurahan_pekerja, area_pekerja);
        masyarakatCall.enqueue(new Callback<ResponseMasyarakat>() {
            @Override
            public void onResponse(Call<ResponseMasyarakat> call, Response<ResponseMasyarakat> response) {
                pDialog.dismiss();

                if (response.isSuccessful()) {
                    String kode = String.valueOf(response.body().getKode());
                    if (kode.equals("1")) {
                        masayarkats = (ArrayList<Masayarkat>) response.body().getMasyarakat_marker();
                        initMarkerMasyarakat(masayarkats);
                    } else {
                        showSnackMessage(response.body().getPesan());
                    }
                } else {
                    showSnackMessage("Maaf, terjadi kesalahan saat memproses data masyarakat!");
                }

            }

            @Override
            public void onFailure(Call<ResponseMasyarakat> call, Throwable t) {
                pDialog.dismiss();
                showSnackMessage("Terjadi kesalahan sistem saat memproses data masyarakat!");
            }
        });

    }

    private void initMarkerMasyarakat(ArrayList<Masayarkat> masayarkats) {

        for (int i = 0; i < masayarkats.size(); i++) {
            double latitude_masyarakat = Double.parseDouble(masayarkats.get(i).getLatitudeMasyarakat());
            double longitude_masyarakat = Double.parseDouble(masayarkats.get(i).getLongitudeMasyarakat());
            Marker marker = map.addMarker(new MarkerOptions().title("Lokasi Sampah")
                    .icon(bitmapDescriptor(getActivity()))
                    .position(new LatLng(latitude_masyarakat, longitude_masyarakat)));

            String idmark = marker.getId();
            markerMapMasyarakat.put(idmark, masayarkats.get(i));

            SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            assert supportMapFragment != null;
            supportMapFragment.getMapAsync(HomeFragmentPetugas.this);
        }

    }

    private void showSnackMessage(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .setAction("Close", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                    }
                })
                .show();
    }

    private void loadDataSession() {

        id_pekerja = mPreferences.getString(Constanta.SESSION_ID_PETUGAS, "");
        nik_pekerja = mPreferences.getString(Constanta.SESSION_NIK_PETUGAS, "");
        nama_pekerja = mPreferences.getString(Constanta.SESSION_NAMA_PETUGAS, "");
        jenis_kelamin_pekerja = mPreferences.getString(Constanta.SESSION_JEKEL_PETUGAS, "");
        usia_pekerja = mPreferences.getString(Constanta.SESSION_USIA_PETUGAS, "");
        alamat_pekerja = mPreferences.getString(Constanta.SESSION_ALAMAT_PETUGAS, "");
        latitude_pekerja = mPreferences.getString(Constanta.SESSION_LATITUDE_PETUGAS, "");
        longitude_pekerja = mPreferences.getString(Constanta.SESSION_LONGITUDE_PETUGAS, "");
        telpon_pekerja = mPreferences.getString(Constanta.SESSION_TELPON_PETUGAS, "");
        kelurahan_pekerja = mPreferences.getString(Constanta.SESSION_KELURAHAN_PETUGAS, "");
        password = mPreferences.getString(Constanta.SESSION_PASSWORD_PETUGAS, "");
        area_pekerja = mPreferences.getString(Constanta.SESSION_AREA_PETUGAS, "");
        kendaraan_pekerja = mPreferences.getString(Constanta.SESSION_KENDARAAN_PETUGAS, "");
        foto_pekerja = mPreferences.getString(Constanta.SESSION_FOTO_PETUGAS, "");
        status_pekerja = mPreferences.getString(Constanta.SESSION_STATUS_PETUGAS, "");
        status_kerja_pekerja = mPreferences.getString(Constanta.SESSION_STATUS_KERJA_PETUGAS, "");
        role_pekerja = mPreferences.getString(Constanta.SESSION_ROLE_PETUGAS, "");
        role_pekerja = mPreferences.getString(Constanta.SESSION_ROLE, "");

        loadDataLaporan();
        loadDataMasyarakat();
    }

    private void clickjenisMap(View view) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_jenis_maps, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Jenis Maps");
        dialog.show();

        LinearLayout ll_maps_default = dialogView.findViewById(R.id.ll_maps_default);
        LinearLayout ll_maps_satelit = dialogView.findViewById(R.id.ll_maps_satelit);

        ImageView img_maps_default = dialogView.findViewById(R.id.img_maps_default);
        ImageView img_maps_satelit = dialogView.findViewById(R.id.img_maps_satelit);

        TextView tv_maps_default = dialogView.findViewById(R.id.tv_maps_default);
        TextView tv_maps_satelit = dialogView.findViewById(R.id.tv_maps_satelit);

        if (map.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {

            img_maps_satelit.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_trans_merah));
            img_maps_satelit.setPadding(6, 6, 6, 6);
            tv_maps_satelit.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

            tv_maps_default.setBackground(null);
            tv_maps_default.setPadding(0, 0, 0, 0);
            tv_maps_default.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
        } else {
            img_maps_default.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_trans_merah));
            img_maps_default.setPadding(6, 6, 6, 6);
            tv_maps_default.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

            img_maps_satelit.setBackground(null);
            img_maps_satelit.setPadding(0, 0, 0, 0);
            tv_maps_satelit.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
        }

        ll_maps_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setMapType(map.MAP_TYPE_NORMAL);
                img_maps_default.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_trans_merah));
                img_maps_default.setPadding(6, 6, 6, 6);
                tv_maps_default.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

                img_maps_satelit.setBackground(null);
                img_maps_satelit.setPadding(0, 0, 0, 0);
                tv_maps_satelit.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));

            }
        });

        ll_maps_satelit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setMapType(map.MAP_TYPE_SATELLITE);
                img_maps_satelit.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_trans_merah));
                img_maps_satelit.setPadding(6, 6, 6, 6);
                tv_maps_satelit.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

                img_maps_default.setBackground(null);
                img_maps_default.setPadding(0, 0, 0, 0);
                tv_maps_default.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));

            }
        });

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
        int width = 55;
        vectorDrawble = ContextCompat.getDrawable(context, R.drawable.marker_lokasi_sampah);
        assert vectorDrawble != null;
        vectorDrawble.setBounds(0, 0, width, height);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawble.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor bitmapDescriptorLaporan(Context context) {
        int height = 70;
        int width = 55;
        vectorDrawble = ContextCompat.getDrawable(context, R.drawable.marker_laporan);
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
        mapLaporan = googleMap;


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
//            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }

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

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showDialogMarker(marker);
                return false;
            }
        });

    }

    private void showDialogMarker(Marker marker) {
        if (markerMapMasyarakat.get(marker.getId()) == null) {

            Laporan laporan = markerMapLaporan.get(marker.getId());
            Toast.makeText(getActivity(), "Pesan : " + laporan.getKeteranganLaporan(), Toast.LENGTH_SHORT).show();

        } else {
            Masayarkat masayarkat = markerMapMasyarakat.get(marker.getId());
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Angkut Sampah ?")
                    .setCancelButton("Detail", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            pDialog.dismiss();
                            Toast.makeText(getActivity(), "Show Detail " + masayarkat.getNamaMasyarakat(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setConfirmButton("Selesai", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            pDialog.show();

                            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                            assert masayarkat != null;
                            Call<ResponseMasyarakat> responseMasyarakatCall = apiInterface.editStatusMarker(
                                    masayarkat.getIdMasyarakat());
                            responseMasyarakatCall.enqueue(new Callback<ResponseMasyarakat>() {
                                @Override
                                public void onResponse(Call<ResponseMasyarakat> call, Response<ResponseMasyarakat> response) {
                                    pDialog.dismiss();
                                    if (response.isSuccessful()) {
                                        String kode = String.valueOf(response.body().getKode());
                                        if (kode.equals("1")) {
                                            Toast.makeText(getActivity(), "Mengambil Sampah " + masayarkat.getNamaMasyarakat() +
                                                    " Berhasil!", Toast.LENGTH_SHORT).show();
                                            marker.remove();
                                        } else {
                                            Toast.makeText(getActivity(), "Maaf, Proses Gagal! Kode Kesalahan 001", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "Maaf, Proses Gagal! Kode Kesalahan 002", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseMasyarakat> call, Throwable t) {
                                    pDialog.dismiss();
                                    Toast.makeText(getActivity(), "Maaf, Proses Gagal! Kode Kesalahan 003", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .show();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startLocationUpdate();
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (currentLocation == null) {
            startLocationUpdate();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        getAddress(location.getLatitude(), location.getLongitude());
        latLng_petugas = new LatLng(location.getLatitude(), location.getLongitude());
        //Place current location marker
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng_petugas);

        updateLatlingSession(location);

        if (first_time) {
            updateLatlingDatabase(location);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_petugas, 19));
            first_time = false;
        }


    }

    private void updateLatlingDatabase(Location location) {

        String la = String.valueOf(location.getLatitude());
        String lo = String.valueOf(location.getLongitude());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponsePetugas> responsePetugasCall = apiInterface.editLocationPetugas(id_pekerja, alamat_pekerja, la, lo);
        responsePetugasCall.enqueue(new Callback<ResponsePetugas>() {
            @Override
            public void onResponse(Call<ResponsePetugas> call, Response<ResponsePetugas> response) {

                if (response.isSuccessful()) {
//                    Toast.makeText(getActivity(), "Sukses Simpan", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponsePetugas> call, Throwable t) {
//                Toast.makeText(getActivity(), "gagal", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void updateLatlingSession(Location location) {
        latitude_pekerja = String.valueOf(location.getLatitude());
        longitude_pekerja = String.valueOf(location.getLongitude());
        editor = mPreferences.edit();
        editor.putString(Constanta.SESSION_ALAMAT_PETUGAS, alamat_pekerja);
        editor.putString(Constanta.SESSION_LATITUDE_PETUGAS, latitude_pekerja);
        editor.putString(Constanta.SESSION_LONGITUDE_PETUGAS, longitude_pekerja);
        editor.apply();
    }

    private void getAddress(double latitud, double longitud) {

//        geocoder = null;
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocation(latitud, longitud, 1);
            String address = addressList.get(0).getAddressLine(0);
            alamat_pekerja = address;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startLocationUpdate() {

//        progressBar.setVisibility(View.GONE);
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTES_INTERVAL);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }
}
