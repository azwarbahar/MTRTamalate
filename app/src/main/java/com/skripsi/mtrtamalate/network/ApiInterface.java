package com.skripsi.mtrtamalate.network;

import com.skripsi.mtrtamalate.models.berita.ResponseBerita;
import com.skripsi.mtrtamalate.models.laporan.ResponLaporan;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("laporan/addLaporan.php")
    Call<ResponLaporan> AddLaporan(@Field("keterangan_laporan") String keterangan_laporan,
                                         @Field("foto_laporan") String foto_laporan,
                                         @Field("latitude_laporan") String latitude_laporan,
                                         @Field("longitude_laporan") String longitude_laporan,
                                         @Field("alamat_laporan") String alamat_laporan,
                                         @Field("nik_laporan") String nik_laporan,
                                         @Field("kelurahan_laporan") String kelurahan_laporan,
                                         @Field("area_laporan") String area_laporan,
                                         @Field("masyarakat_id") String masyarakat_id);


    // BERITA
    @GET("berita/readBerita.php")
    Call<ResponseBerita> getBerita();
}
