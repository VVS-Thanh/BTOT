package com.example.btot;

public class PhongBan {
    private String maPhongBan;
    private String tenPhongBan;
    private int soPhongBan;

    public PhongBan(String maPhongBan, String tenPhongBan, int soPhongBan) {
        this.maPhongBan = maPhongBan;
        this.tenPhongBan = tenPhongBan;
        this.soPhongBan = soPhongBan;
    }

    public String getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(String maPhongBan) {
        this.maPhongBan = maPhongBan;
    }

    public String getTenPhongBan() {
        return tenPhongBan;
    }

    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }

    public int getSoPhongBan() {
        return soPhongBan;
    }

    public void setSoPhongBan(int soPhongBan) {
        this.soPhongBan = soPhongBan;
    }
}
