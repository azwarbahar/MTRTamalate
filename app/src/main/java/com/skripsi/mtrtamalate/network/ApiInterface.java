package com.skripsi.mtrtamalate.network;

import com.skripsi.mtrtamalate.models.berita.ResponseBerita;
import com.skripsi.mtrtamalate.models.laporan.ResponLaporan;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    // LPORAN
    @GET("laporan/getLaporanMasarakat.php")
    Call<ResponLaporan> laporanMasyarakatStatus(@Query("masyarakat_id") String masyarakat_id);

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


    // MASYARAKAT
    @GET("masyarakat/auth_masyarakat.php")
    Call<ResponseMasyarakat> authMasyarakat(@Query("nik_masyarakat") String nik_masyarakat,
                                            @Query("password_masyarakat") String password_masyarakat);
    @GET("masyarakat/getMasyarakatId.php")
    Call<ResponseMasyarakat> getMasyarakatId(@Query("id_masyarakat") String id_masyarakat);

    @FormUrlEncoded
    @POST("masyarakat/editProfilMasyarakat.php")
    Call<ResponseMasyarakat> editProfil(@Field("id_masyarakat") String id_masyarakat,
                                   @Field("nama_masyarakat") String nama_masyarakat,
                                   @Field("alamat_masyarakat") String alamat_masyarakat,
                                   @Field("telpon_masyarakat") String telpon_masyarakat,
                                   @Field("usia_masyarakat") String usia_masyarakat);
}
