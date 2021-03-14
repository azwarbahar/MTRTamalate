package com.skripsi.mtrtamalate.ui.masyarakat.akun;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skripsi.mtrtamalate.R;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.network.ApiClient;
import com.skripsi.mtrtamalate.network.ApiInterface;
import com.skripsi.mtrtamalate.utils.Constanta;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TitikLokasiActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private Drawable vectorDrawble;
    private Bitmap bitmap;

    GoogleMap map;
    String latitud, longitud;
    private String alamat_latlig;
    private EditText et_alamat;
    private TextView tv_alamat;
    private CardView cv_tap;
    private TextView tv_lokasi_sekarang;
    private TextView tv_info;
    private TextView tv_send;
    private ImageView img_btn_send;
    private ImageView btn_jenis_map;
    private LatLng latLng;
    private String latitud2, longitud2;
    private RelativeLayout rl_edit;
    private String latitude_send;
    private String longitude_send;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private View dialogView;

    private boolean latlingReady;
    private boolean firstTime = true;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private String id_session;
    private String alamat_session;
    private String latitude_session;
    private String longitude_session;
    private String data_intent;
    private boolean clickMaps = false;
    MarkerOptions markerOptionsSession;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titik_lokasi);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mPreferences = getSharedPreferences(Constanta.MY_SHARED_PREFERENCES, MODE_PRIVATE);


        rl_edit = findViewById(R.id.rl_edit);
        tv_send = findViewById(R.id.tv_send);
        img_btn_send = findViewById(R.id.img_btn_send);
        cv_tap = findViewById(R.id.cv_tap);
        tv_info = findViewById(R.id.tv_info);
        btn_jenis_map = findViewById(R.id.btn_jenis_map);
        et_alamat = findViewById(R.id.et_alamat);
        tv_alamat = findViewById(R.id.tv_alamat);
        tv_lokasi_sekarang = findViewById(R.id.tv_lokasi_sekarang);
        tv_lokasi_sekarang.setOnClickListener(this::clickLokasiSekarang);
        btn_jenis_map.setOnClickListener(this::clickjenisMap);
        tv_info.setOnClickListener(this::clickInfo);

        tv_send.setOnClickListener(this::clickSend);

        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            data_intent = bundle.getString("extra_data");
        }

        setDataSession();
        if (data_intent.equals("akun")) {
            setModeView();
        } else {
            setModeUpdate();
        }
    }

    private void clickSend(View view) {

        if (clickMaps) {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(true);
            pDialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (alamat_latlig.isEmpty()) {
                        new SweetAlertDialog(TitikLokasiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Uups..")
                                .setContentText("Alamat Belum dilengkapi!")
                                .show();
                    } else if (latitude_send.isEmpty() || longitude_send.isEmpty()) {
                        new SweetAlertDialog(TitikLokasiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Uups..")
                                .setContentText("Titik lokasi Belum ditentukan!")
                                .show();
                    } else {
                        sendData();
                    }
                }
            }, 500);
        } else {
            setModeUpdate();
        }

    }

    private void sendData() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseMasyarakat> responseMasyarakatCall = apiInterface.editLokasi(id_session, alamat_latlig,
                latitude_send, longitude_send);
        responseMasyarakatCall.enqueue(new Callback<ResponseMasyarakat>() {
            @Override
            public void onResponse(Call<ResponseMasyarakat> call, Response<ResponseMasyarakat> response) {
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    String kode = String.valueOf(response.body().getKode());
                    if (kode.equals("1")) {
                        SweetAlertDialog sweetAlertDialogsuccess = new SweetAlertDialog(TitikLokasiActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialogsuccess.setTitleText("Success..");
                        sweetAlertDialogsuccess.setContentText(response.body().getPesan());
                        sweetAlertDialogsuccess.setCancelable(false);
                        sweetAlertDialogsuccess.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                setModeView();
                                updateDataSession(alamat_latlig,
                                        latitude_send, longitude_send);
                                configIntent();
                            }
                        });
                        sweetAlertDialogsuccess.show();
                    } else {
                        new SweetAlertDialog(TitikLokasiActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Uups..")
                                .setContentText(response.body().getPesan())
                                .show();
                    }
                } else {
                    new SweetAlertDialog(TitikLokasiActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Gagal..")
                            .setContentText("Terjadi kesalahan!")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMasyarakat> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(TitikLokasiActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal..")
                        .setContentText("Terjadi kesalahan!")
                        .show();

            }
        });

    }

    private void configIntent() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                pDialog = new SweetAlertDialog(TitikLokasiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Menyimpan Data..");
                pDialog.setCancelable(true);
                pDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.dismiss();
                        finish();
                    }
                }, 2500);
            }
        }, 500);


    }

    private void updateDataSession(String alamat_latlig, String latitude_send, String longitude_send) {

        editor = mPreferences.edit();
        editor.putString(Constanta.SESSION_ALAMAT_MASYARAKAT, alamat_latlig);
        editor.putString(Constanta.SESSION_LATITUDE_MASYARAKAT, latitude_send);
        editor.putString(Constanta.SESSION_LONGITUDE_MASYARAKAT, longitude_send);
        editor.apply();
    }

    private void setDataSession() {
        id_session = mPreferences.getString(Constanta.SESSION_ID_MASYARAKAT, "");
        alamat_session = mPreferences.getString(Constanta.SESSION_ALAMAT_MASYARAKAT, "");
        latitude_session = mPreferences.getString(Constanta.SESSION_LATITUDE_MASYARAKAT, "");
        longitude_session = mPreferences.getString(Constanta.SESSION_LONGITUDE_MASYARAKAT, "");

        if (!alamat_session.isEmpty()) {
            et_alamat.setText(alamat_session);
            tv_alamat.setText(alamat_session);
            alamat_latlig = alamat_session;
        } else {
            et_alamat.setText("Kosong");
            tv_alamat.setText("Kosong");
        }
        if (!latitude_session.equals("-")) {
            latitude_send = latitude_session;
            longitude_send = longitude_session;
            LatLng latLng1 = new LatLng(Double.valueOf(latitude_session), Double.valueOf(longitude_session));
            markerOptionsSession = new MarkerOptions();
            markerOptionsSession.position(latLng1);
            markerOptionsSession.title("Titik Lokasi");
            markerOptionsSession.icon(bitmapDescriptor(this));
            latlingReady = true;
        } else {
            latlingReady = false;
        }
    }

    private void setModeUpdate() {
        cv_tap.setVisibility(View.VISIBLE);
        clickMaps = true;
        tv_lokasi_sekarang.setVisibility(View.VISIBLE);
        tv_send.setText("Simpan");
        rl_edit.setBackground(ContextCompat.getDrawable(TitikLokasiActivity.this, R.color.colorPrimaryDark));
        img_btn_send.setImageResource(R.drawable.ic_baseline_send_24);
        et_alamat.setVisibility(View.VISIBLE);
        tv_alamat.setVisibility(View.GONE);
    }

    private void setModeView() {
        cv_tap.setVisibility(View.GONE);
        clickMaps = false;
        tv_lokasi_sekarang.setVisibility(View.GONE);
        tv_send.setText("Edit");
        rl_edit.setBackground(ContextCompat.getDrawable(TitikLokasiActivity.this, R.color.grey));
        img_btn_send.setImageResource(R.drawable.ic_baseline_edit_24);
        et_alamat.setVisibility(View.GONE);
        tv_alamat.setVisibility(View.VISIBLE);
    }

    private void clickInfo(View view) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(TitikLokasiActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        dialog.setIcon(R.drawable.ic_baseline_info_24);
        dialog.setCancelable(true);
        dialog.setTitle("Info Koordinat");
        dialog.setMessage("Titik koordinat yang anda tentukan akan digunakan petugas untuk menindaki " +
                "laporan yang anda ajukan, dan juga mengetahui titik sampah yang terdaftar pada sistem");
        dialog.show();

    }

    private void clickjenisMap(View view) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(TitikLokasiActivity.this);
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

            img_maps_satelit.setBackground(ContextCompat.getDrawable(TitikLokasiActivity.this, R.drawable.bg_trans_merah));
            img_maps_satelit.setPadding(6, 6, 6, 6);
            tv_maps_satelit.setTextColor(ContextCompat.getColor(TitikLokasiActivity.this, R.color.colorPrimaryDark));

            tv_maps_default.setBackground(null);
            tv_maps_default.setPadding(0, 0, 0, 0);
            tv_maps_default.setTextColor(ContextCompat.getColor(TitikLokasiActivity.this, R.color.grey));
        } else {
            img_maps_default.setBackground(ContextCompat.getDrawable(TitikLokasiActivity.this, R.drawable.bg_trans_merah));
            img_maps_default.setPadding(6, 6, 6, 6);
            tv_maps_default.setTextColor(ContextCompat.getColor(TitikLokasiActivity.this, R.color.colorPrimaryDark));

            img_maps_satelit.setBackground(null);
            img_maps_satelit.setPadding(0, 0, 0, 0);
            tv_maps_satelit.setTextColor(ContextCompat.getColor(TitikLokasiActivity.this, R.color.grey));
        }

        ll_maps_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setMapType(map.MAP_TYPE_NORMAL);
                img_maps_default.setBackground(ContextCompat.getDrawable(TitikLokasiActivity.this, R.drawable.bg_trans_merah));
                img_maps_default.setPadding(6, 6, 6, 6);
                tv_maps_default.setTextColor(ContextCompat.getColor(TitikLokasiActivity.this, R.color.colorPrimaryDark));

                img_maps_satelit.setBackground(null);
                img_maps_satelit.setPadding(0, 0, 0, 0);
                tv_maps_satelit.setTextColor(ContextCompat.getColor(TitikLokasiActivity.this, R.color.grey));

            }
        });

        ll_maps_satelit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setMapType(map.MAP_TYPE_SATELLITE);
                img_maps_satelit.setBackground(ContextCompat.getDrawable(TitikLokasiActivity.this, R.drawable.bg_trans_merah));
                img_maps_satelit.setPadding(6, 6, 6, 6);
                tv_maps_satelit.setTextColor(ContextCompat.getColor(TitikLokasiActivity.this, R.color.colorPrimaryDark));

                img_maps_default.setBackground(null);
                img_maps_default.setPadding(0, 0, 0, 0);
                tv_maps_default.setTextColor(ContextCompat.getColor(TitikLokasiActivity.this, R.color.grey));

            }
        });

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

    private void clickLokasiSekarang(View view) {
        map.clear();
        latitude_send = latitud2;
        longitude_send = longitud2;
        latLng = new LatLng(Double.valueOf(latitud2), Double.valueOf(longitud2));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Titik Lokasi");
        markerOptions.icon(bitmapDescriptor(this));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
//        latitudKirim = latitud2;
//        longitudKirim = longitud2;
        map.addMarker(markerOptions);
        getAddress(Double.valueOf(latitud2), Double.valueOf(longitud2));
    }

    private void clear() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
        et_alamat.setText("");
    }

    private void getAddress(double latitud, double longitud) {

        Geocoder geocoder;
        List<Address> addressList;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addressList = geocoder.getFromLocation(latitud, longitud, 1);
            String address = addressList.get(0).getAddressLine(0);
            alamat_latlig = address;
            et_alamat.setText(address);
            tv_alamat.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        if (!latitude_session.equals("-")) {
            map.addMarker(markerOptionsSession);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (clickMaps) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Titik Lokasi");
                    markerOptions.icon(bitmapDescriptor(TitikLokasiActivity.this));
//                latitudKirim = String.valueOf(latLng.latitude);
//                longitudKirim = String.valueOf(latLng.longitude);
                    getAddress(latLng.latitude, latLng.longitude);
                    latitude_send = String.valueOf(latLng.latitude);
                    longitude_send = String.valueOf(latLng.longitude);
                    map.clear();
                    map.addMarker(markerOptions);
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
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

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        latitud2 = String.valueOf(location.getLatitude());
        longitud2 = String.valueOf(location.getLongitude());

        //move map camera
        if (!latlingReady) {
            if (firstTime) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                firstTime = false;
            }
        } else {
            LatLng latLng1 = new LatLng(Double.valueOf(latitude_session), Double.valueOf(longitude_session));
            if (firstTime) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 17));
                firstTime = false;
            }
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(TitikLokasiActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }
}