package com.example.rumahkost.ui.pengaduan;

public class Model_Pengaduan {
    private String id;
    private String namapenghuni;
    private String unitpenghuni;
    private String pengaduan;
    private String tglpengaduan;

    public Model_Pengaduan() {

    }

    public Model_Pengaduan(String id, String namapenghuni, String unitpenghuni, String pengaduan, String tglpengaduan) {
        this.id = id;
        this.namapenghuni = namapenghuni;
        this.unitpenghuni = unitpenghuni;
        this.pengaduan = pengaduan;
        this.tglpengaduan = tglpengaduan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamapenghuni() {
        return namapenghuni;
    }

    public void setNamapenghuni(String namapenghuni) {
        this.namapenghuni = namapenghuni;
    }

    public String getUnitpenghuni() {
        return unitpenghuni;
    }

    public void setUnitpenghuni(String unitpenghuni) {
        this.unitpenghuni = unitpenghuni;
    }

    public String getPengaduan() {
        return pengaduan;
    }

    public void setPengaduan(String pengaduan) {
        this.pengaduan = pengaduan;
    }

    public String getTglpengaduan() {
        return tglpengaduan;
    }

    public void setTglpengaduan(String tglpengaduan) {
        this.tglpengaduan = tglpengaduan;
    }
}