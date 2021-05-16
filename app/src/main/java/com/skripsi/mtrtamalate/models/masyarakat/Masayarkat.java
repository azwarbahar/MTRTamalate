package com.skripsi.mtrtamalate.models.masyarakat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Masayarkat implements Parcelable {

	@SerializedName("foto_masyarakat")
	private String fotoMasyarakat;

	@SerializedName("usia_masyarakat")
	private String usiaMasyarakat;

	@SerializedName("id_masyarakat")
	private String idMasyarakat;

	@SerializedName("status_masyarakat")
	private String statusMasyarakat;

	@SerializedName("longitude_masyarakat")
	private String longitudeMasyarakat;

	@SerializedName("nama_masyarakat")
	private String namaMasyarakat;

	@SerializedName("status_marker")
	private String statusMarker;

	@SerializedName("area_masyarakat")
	private String areaMasyarakat;

	@SerializedName("nik_masyarakat")
	private String nikMasyarakat;

	@SerializedName("kelurahan_masyarakat")
	private String kelurahanMasyarakat;

	@SerializedName("pembayaran_masyarakat")
	private String pembayaranMasyarakat;

	@SerializedName("telpon_masyarakat")
	private String telponMasyarakat;

	@SerializedName("latitude_masyarakat")
	private String latitudeMasyarakat;

	@SerializedName("password_masyarakat")
	private String passwordMasyarakat;

	@SerializedName("alamat_masyarakat")
	private String alamatMasyarakat;

	protected Masayarkat(Parcel in) {
		fotoMasyarakat = in.readString();
		usiaMasyarakat = in.readString();
		idMasyarakat = in.readString();
		statusMasyarakat = in.readString();
		longitudeMasyarakat = in.readString();
		namaMasyarakat = in.readString();
		statusMarker = in.readString();
		areaMasyarakat = in.readString();
		nikMasyarakat = in.readString();
		kelurahanMasyarakat = in.readString();
		pembayaranMasyarakat = in.readString();
		telponMasyarakat = in.readString();
		latitudeMasyarakat = in.readString();
		passwordMasyarakat = in.readString();
		alamatMasyarakat = in.readString();
	}

	public static final Creator<Masayarkat> CREATOR = new Creator<Masayarkat>() {
		@Override
		public Masayarkat createFromParcel(Parcel in) {
			return new Masayarkat(in);
		}

		@Override
		public Masayarkat[] newArray(int size) {
			return new Masayarkat[size];
		}
	};

	public String getFotoMasyarakat(){
		return fotoMasyarakat;
	}

	public String getUsiaMasyarakat(){
		return usiaMasyarakat;
	}

	public String getIdMasyarakat(){
		return idMasyarakat;
	}

	public String getStatusMasyarakat(){
		return statusMasyarakat;
	}

	public String getLongitudeMasyarakat(){
		return longitudeMasyarakat;
	}

	public String getNamaMasyarakat(){
		return namaMasyarakat;
	}

	public String getStatusMarker(){
		return statusMarker;
	}

	public String getAreaMasyarakat(){
		return areaMasyarakat;
	}

	public String getNikMasyarakat(){
		return nikMasyarakat;
	}

	public String getKelurahanMasyarakat(){
		return kelurahanMasyarakat;
	}

	public String getPembayaranMasyarakat(){
		return pembayaranMasyarakat;
	}

	public String getTelponMasyarakat(){
		return telponMasyarakat;
	}

	public String getLatitudeMasyarakat(){
		return latitudeMasyarakat;
	}

	public String getPasswordMasyarakat(){
		return passwordMasyarakat;
	}

	public String getAlamatMasyarakat(){
		return alamatMasyarakat;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(fotoMasyarakat);
		parcel.writeString(usiaMasyarakat);
		parcel.writeString(idMasyarakat);
		parcel.writeString(statusMasyarakat);
		parcel.writeString(longitudeMasyarakat);
		parcel.writeString(namaMasyarakat);
		parcel.writeString(statusMarker);
		parcel.writeString(areaMasyarakat);
		parcel.writeString(nikMasyarakat);
		parcel.writeString(kelurahanMasyarakat);
		parcel.writeString(pembayaranMasyarakat);
		parcel.writeString(telponMasyarakat);
		parcel.writeString(latitudeMasyarakat);
		parcel.writeString(passwordMasyarakat);
		parcel.writeString(alamatMasyarakat);
	}
}