package com.example.btot;

public class NhanVien {
    private String maNhanVien;
    private String tenNhanVien;
    private int tuoiNhanVien;
    private String maPhongBan;

    public NhanVien(String maNhanVien, String tenNhanVien, int tuoiNhanVien, String maPhongBan) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.tuoiNhanVien = tuoiNhanVien;
        this.maPhongBan = maPhongBan;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public int getTuoiNhanVien() {
        return tuoiNhanVien;
    }

    public void setTuoiNhanVien(int tuoiNhanVien) {
        this.tuoiNhanVien = tuoiNhanVien;
    }

    public String getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(String maPhongBan) {
        this.maPhongBan = maPhongBan;
    }
}
