package com.example.rumahkost.ui.pembayaran;

public class Model_Pembayaran {
    private String id;
    private String penghuni;
    private String unit;
    private String tglbyr;
    private String harga;
    private String status;
    private  long tgl;

    public Model_Pembayaran(){

    }

    public Model_Pembayaran(String id, String penghuni, String unit, String  tglbyr, String harga, String status, long tgl){

        this.id = id;
        this.penghuni = penghuni;
        this.unit = unit;
        this.tglbyr = tglbyr;
        this.harga = harga;
        this.status = status;
        this.tgl = tgl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPenghuni() {
        return penghuni;
    }

    public void setPenghuni(String penghuni) {
        this.penghuni = penghuni;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTglbyr() {return tglbyr;
    }

    public void setTglbyr(String tglbyr) {this.tglbyr = tglbyr;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTgl() {
        return tgl;
    }

    public void setTgl(long tgl) {
        this.tgl = tgl;
    }
}
