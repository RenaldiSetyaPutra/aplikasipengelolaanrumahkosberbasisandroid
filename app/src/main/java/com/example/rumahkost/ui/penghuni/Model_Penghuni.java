package com.example.rumahkost.ui.penghuni;

public class Model_Penghuni {
    private String id;
    private String nama;
    private String jk;
    private String asal;
    private String no_hp;
    private String tgl_msk;
    private String unit;

    public Model_Penghuni(){

    }

    public Model_Penghuni(String id, String nama, String jk, String asal, String no_hp, String tgl_msk, String unit) {
        this.id = id;
        this.nama = nama;
        this.jk = jk;
        this.asal = asal;
        this.no_hp = no_hp;
        this.tgl_msk = tgl_msk;
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getAsal() {
        return asal;
    }

    public void setAsal(String asal) {
        this.asal = asal;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getTgl_msk() {return tgl_msk;
    }

    public void setTgl_msk(String tgl_msk) {
        this.tgl_msk = tgl_msk;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
