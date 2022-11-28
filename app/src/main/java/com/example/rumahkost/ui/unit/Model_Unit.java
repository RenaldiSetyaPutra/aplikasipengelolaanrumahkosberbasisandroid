package com.example.rumahkost.ui.unit;

public class Model_Unit {
    private String id;
    private String nama;
    private String fasilitas;
    private String harga;
    private String status;

    public Model_Unit(){

    }
    public Model_Unit(String id, String nama, String status, String fasilitas, String harga) {
        this.id = id;
        this.nama = nama;
        this.status = status;
        this.fasilitas = fasilitas;
        this.harga = harga;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
