package com.skripsi.mtrtamalate.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;


//import static org.junit.Assert.*;


@RunWith(JUnit4.class)
public class ValidasiLaporanTest {

    //  set up before
    public ValidasiLaporan validasiLaporan = new ValidasiLaporan();


    @Test
    public void whenPhotoIsReady(){

        String img_url = "https://mtrtamalate.id/photo/laporan/img_00001.jpg";

        assertThat(validasiLaporan).isEqualTo(true);
    }

    @Test
    public void validasiKoordinat() {

        String latitude = "-5.170858";
        String longitude = "119.430527";

//        assertThat(validasiLaporan.validasiKoordinat(latitude,latitude)).isEquals


    }
}