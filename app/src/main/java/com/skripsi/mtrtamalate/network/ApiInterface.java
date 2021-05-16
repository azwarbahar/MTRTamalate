package com.skripsi.mtrtamalate.network;

import com.skripsi.mtrtamalate.models.area.ResponseArea;
import com.skripsi.mtrtamalate.models.berita.ResponseBerita;
import com.skripsi.mtrtamalate.models.kendaraan.ResponseKendaraan;
import com.skripsi.mtrtamalate.models.laporan.ResponLaporan;
import com.skripsi.mtrtamalate.models.masyarakat.ResponseMasyarakat;
import com.skripsi.mtrtamalate.models.petugas.ResponsePetugas;
import com.skripsi.mtrtamalate.models.sampah.ResponSampah;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    // KENDARAAN
    @GET("kendaraan/getKendaraanPetugas.php")
    Call<ResponseKendaraan> getKendaraanPetugas(@Query("id_kendaraan") String id_kendaraan);


    // AREA
    @GET("area/getAreaId.php")
    Call<ResponseArea> getAreaId(@Query("id_area") String id_area,
                                 @Query("kelurahan") String kelurahan);



    // LPORAN
    @GET("laporan/getLaporanMasarakat.php")
    Call<ResponLaporan> laporanMasyarakatStatus(@Query("masyarakat_id") String masyarakat_id);

    @GET("laporan/getLaporanPetugas.php")
    Call<ResponLaporan> getLaporanPetugas(@Query("petugas_id") String petugas_id);

    @GET("laporan/getLaporanMapPetugas.php")
    Call<ResponLaporan> getLaporanMapPetugas(@Query("kelurahan_laporan") String kelurahan_laporan,
                                             @Query("area_laporan") String area_laporan,
                                             @Query("staus_laporan") String staus_laporan);

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


    // DATA SAMPAH
    @FormUrlEncoded
    @POST("sampah/addSampah.php")
    Call<ResponSampah> AddSampah(@Field("kelurahan") String kelurahan,
                                 @Field("id_koordinator") String id_koordinator,
                                 @Field("id_petugas") String id_petugas,
                                 @Field("berat_sampah") String berat_sampah,
                                 @Field("foto_bukti") String foto_bukti,
                                 @Field("latitude_sampah") String latitude_sampah,
                                 @Field("longitude_sampah") String longitude_sampah,
                                 @Field("alamat_sampah") String alamat_sampah,
                                 @Field("keterangan") String keterangan,
                                 @Field("tanggal") String tanggal);

    @GET("sampah/cekTodaySendPetugas.php")
    Call<ResponSampah> cekTodaySendPetugas(@Query("id_petugas") String id_petugas,
                                           @Query("tanggal") String tanggal);



    // MASYARAKAT
    @FormUrlEncoded
    @POST("masyarakat/editFotoMasyarakat.php")
    Call<ResponseMasyarakat> editFoto(@Field("id_masyarakat") String id_masyarakat,
                                      @Field("foto_masyarakat") String foto_masyarakat);

    @GET("masyarakat/auth_masyarakat.php")
    Call<ResponseMasyarakat> authMasyarakat(@Query("nik_masyarakat") String nik_masyarakat,
                                            @Query("password_masyarakat") String password_masyarakat);

    @GET("masyarakat/getAllLokasiMasyarakat.php")
    Call<ResponseMasyarakat> getAllLokasiMasyarakat(@Query("kelurahan_pekerja") String kelurahan_pekerja,
                                                    @Query("area_pekerja") String area_pekerja);

    @GET("masyarakat/getMasyarakatKelurahan.php")
    Call<ResponseMasyarakat> getMasyarakatKelurahan(@Query("kelurahan_pekerja") String kelurahan_pekerja);

    @GET("masyarakat/editPasswordMasyarakat.php")
    Call<ResponseMasyarakat> editPassword(@Query("id_masyarakat") String id_masyarakat,
                                          @Query("password_lama") String password_lama,
                                          @Query("password_baru") String password_baru);

    @GET("masyarakat/lokasiMasyarakat.php")
    Call<ResponseMasyarakat> editLokasi(@Query("id_masyarakat") String id_masyarakat,
                                          @Query("alamat_masyarakat") String alamat_masyarakat,
                                          @Query("latitude_masyarakat") String latitude_masyarakat,
                                          @Query("longitude_masyarakat") String longitude_masyarakat);

    @GET("masyarakat/getMasyarakatId.php")
    Call<ResponseMasyarakat> getMasyarakatId(@Query("id_masyarakat") String id_masyarakat);

    @GET("masyarakat/editPembayaran.php")
    Call<ResponseMasyarakat> editPembayaran(@Query("id_masyarakat") String id_masyarakat,
                                            @Query("isi") String isi);

    @FormUrlEncoded
    @POST("masyarakat/editProfilMasyarakat.php")
    Call<ResponseMasyarakat> editProfil(@Field("id_masyarakat") String id_masyarakat,
                                   @Field("nama_masyarakat") String nama_masyarakat,
                                   @Field("alamat_masyarakat") String alamat_masyarakat,
                                   @Field("telpon_masyarakat") String telpon_masyarakat,
                                   @Field("usia_masyarakat") String usia_masyarakat);

    @GET("masyarakat/editStatusMarker.php")
    Call<ResponseMasyarakat> editStatusMarker(@Query("id_masyarakat") String id_masyarakat);


    // PETUGAS
    @GET("petugas/getPetugasId.php")
    Call<ResponsePetugas> getPetugasId(@Query("id_petugas") String id_petugas);

    @GET("petugas/auth_petugas.php")
    Call<ResponsePetugas> authPetugas(@Query("nik_petugas") String nik_petugas,
                                      @Query("password_petugas") String password_petugas);

    @FormUrlEncoded
    @POST("petugas/editLocationPetugas.php")
    Call<ResponsePetugas> editLocationPetugas(@Field("id_petugas") String id_petugas,
                                              @Field("alamat_petugas") String alamat_petugas,
                                              @Field("latitude_petugas") String latitude_petugas,
                                              @Field("longitude_petugas") String longitude_petugas);

    @GET("petugas/getPetugasKelurahan.php")
    Call<ResponsePetugas> getPetugasKelurahan(@Query("kelurahan_pekerja") String kelurahan_pekerja);
}
