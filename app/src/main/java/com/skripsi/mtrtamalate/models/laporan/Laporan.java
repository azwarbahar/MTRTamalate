package com.skripsi.mtrtamalate.models.laporan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Laporan implements Parcelable {

	@SerializedName("petugas_id")
	private String petugasId;

	@SerializedName("id_laporan")
	private String idLaporan;

	@SerializedName("foto_laporan")
	private String fotoLaporan;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("latitude_laporan")
	private String latitudeLaporan;

	@SerializedName("staus_laporan")
	private String stausLaporan;

	@SerializedName("longitude_laporan")
	private String longitudeLaporan;

	@SerializedName("nik_laporan")
	private String nikLaporan;

	@SerializedName("masyarakat_id")
	private String masyarakatId;

	@SerializedName("kelurahan_laporan")
	private String kelurahanLaporan;

	@SerializedName("keterangan_laporan")
	private String keteranganLaporan;

	@SerializedName("update_at")
	private String updateAt;

	@SerializedName("alamat_laporan")
	private String alamatLaporan;

	@SerializedName("area_laporan")
	private String areaLaporan;

	@SerializedName("foto_tindakan_laporan")
	private String fotoTindakanLaporan;

	protected Laporan(Parcel in) {
		petugasId = in.readString();
		idLaporan = in.readString();
		fotoLaporan = in.readString();
		createdAt = in.readString();
		latitudeLaporan = in.readString();
		stausLaporan = in.readString();
		longitudeLaporan = in.readString();
		nikLaporan = in.readString();
		masyarakatId = in.readString();
		kelurahanLaporan = in.readString();
		keteranganLaporan = in.readString();
		updateAt = in.readString();
		alamatLaporan = in.readString();
		areaLaporan = in.readString();
		fotoTindakanLaporan = in.readString();
	}

	public static final Creator<Laporan> CREATOR = new Creator<Laporan>() {
		@Override
		public Laporan createFromParcel(Parcel in) {
			return new Laporan(in);
		}

		@Override
		public Laporan[] newArray(int size) {
			return new Laporan[size];
		}
	};

	public String getPetugasId() {
		return petugasId;
	}

	public String getIdLaporan() {
		return idLaporan;
	}

	public String getFotoLaporan() {
		return fotoLaporan;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getLatitudeLaporan() {
		return latitudeLaporan;
	}

	public String getStausLaporan() {
		return stausLaporan;
	}

	public String getLongitudeLaporan() {
		return longitudeLaporan;
	}

	public String getNikLaporan() {
		return nikLaporan;
	}

	public String getMasyarakatId() {
		return masyarakatId;
	}

	public String getKelurahanLaporan() {
		return kelurahanLaporan;
	}

	public String getKeteranganLaporan() {
		return keteranganLaporan;
	}

	public String getUpdateAt() {
		return updateAt;
	}

	public String getAlamatLaporan() {
		return alamatLaporan;
	}

	public String getAreaLaporan() {
		return areaLaporan;
	}

	public String getFotoTindakanLaporan() {
		return fotoTindakanLaporan;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(petugasId);
		dest.writeString(idLaporan);
		dest.writeString(fotoLaporan);
		dest.writeString(createdAt);
		dest.writeString(latitudeLaporan);
		dest.writeString(stausLaporan);
		dest.writeString(longitudeLaporan);
		dest.writeString(nikLaporan);
		dest.writeString(masyarakatId);
		dest.writeString(kelurahanLaporan);
		dest.writeString(keteranganLaporan);
		dest.writeString(updateAt);
		dest.writeString(alamatLaporan);
		dest.writeString(areaLaporan);
		dest.writeString(fotoTindakanLaporan);
	}
}