package com.example.btot;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public MyDatabase(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addPhongBan(String maPhongBan, String tenPhongBan, int soPhongBan) {
        ContentValues values = new ContentValues();
        values.put("maPhongBan", maPhongBan);
        values.put("tenPhongBan", tenPhongBan);
        values.put("soPhongban", soPhongBan);

        return database.insert("PhongBans", null, values);
    }

    public boolean isNhanVienExists(String maNhanVien) {
        String query = "SELECT maNhanVien FROM NhanViens WHERE maNhanVien = ?";
        Cursor cursor = database.rawQuery(query, new String[]{maNhanVien});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public long addNhanVien(String maNhanVien, String tenNhanVien, int tuoi, String tenPhongBan) {
        // Kiểm tra xem mã nhân viên đã tồn tại trong cơ sở dữ liệu chưa
        if (isNhanVienExists(maNhanVien)) {
            // Mã nhân viên đã tồn tại, trả về -1 hoặc một giá trị thông báo lỗi khác (tùy theo ý muốn của bạn)
            return -1;
        }

        // Lấy mã phòng ban dựa vào tên phòng ban
        String maPhongBan = getMaPhongBanByTenPhongBan(tenPhongBan);

        ContentValues values = new ContentValues();
        values.put("maNhanVien", maNhanVien);
        values.put("tenNhanVien", tenNhanVien);
        values.put("tuoi", tuoi);
        values.put("maPhongBan", maPhongBan); // Lưu mã phòng ban

        return database.insert("NhanViens", null, values);
    }

    public void deleteNhanVien(String maNhanVien) {
        database.delete("NhanViens", "maNhanVien = ?", new String[]{maNhanVien});
    }

    @SuppressLint("Range")
    public List<String> getAllPhongBanNames() {
        List<String> phongBanNames = new ArrayList<>();
        Cursor cursor = database.query("PhongBans", new String[]{"tenPhongBan"}, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String tenPhongBan = cursor.getString(cursor.getColumnIndex("tenPhongBan"));
                phongBanNames.add(tenPhongBan);
            }
            cursor.close();
        }

        return phongBanNames;
    }

    @SuppressLint("Range")
    public List<String> getNhanVienInfoByPhongBan(String maPhongBan) {
        List<String> nhanVienInfoList = new ArrayList<>();
        String query = "SELECT PhongBans.tenPhongBan, NhanViens.maNhanVien, NhanViens.tenNhanVien, NhanViens.tuoi " +
                "FROM PhongBans " +
                "INNER JOIN NhanViens ON PhongBans.maPhongBan = NhanViens.maPhongBan " +
                "WHERE NhanViens.maPhongBan = ?"; // Sử dụng mã phòng ban để lọc

        Cursor cursor = database.rawQuery(query, new String[]{maPhongBan});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String tenPhongBanResult = cursor.getString(cursor.getColumnIndex("tenPhongBan"));
                String maNhanVien = cursor.getString(cursor.getColumnIndex("maNhanVien"));
                String tenNhanVien = cursor.getString(cursor.getColumnIndex("tenNhanVien"));
                int tuoi = cursor.getInt(cursor.getColumnIndex("tuoi"));

                String nhanVienInfo = tenPhongBanResult + ": " + maNhanVien + ":" + tenNhanVien + ": " + tuoi;
                nhanVienInfoList.add(nhanVienInfo);
            }
            cursor.close();
        }
        return nhanVienInfoList;
    }

    @SuppressLint("Range")
    public List<String> getAllNhanVienInfo() {
        List<String> nhanVienInfoList = new ArrayList<>();
        String query = "SELECT PhongBans.tenPhongBan, NhanViens.maNhanVien, NhanViens.tenNhanVien, NhanViens.tuoi " +
                "FROM PhongBans " +
                "INNER JOIN NhanViens ON PhongBans.maPhongBan = NhanViens.maPhongBan";

        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String tenPhongBanResult = cursor.getString(cursor.getColumnIndex("tenPhongBan"));
                String maNhanVien = cursor.getString(cursor.getColumnIndex("maNhanVien"));
                String tenNhanVien = cursor.getString(cursor.getColumnIndex("tenNhanVien"));
                int tuoi = cursor.getInt(cursor.getColumnIndex("tuoi"));

                String nhanVienInfo = tenPhongBanResult + ": " + maNhanVien + ":" + tenNhanVien + ": " + tuoi;
                nhanVienInfoList.add(nhanVienInfo);
            }
            cursor.close();
        }
        return nhanVienInfoList;

    }

    @SuppressLint("Range")
    public String getMaPhongBanByTenPhongBan(String tenPhongBan) {
        String maPhongBan = null;
        String query = "SELECT maPhongBan FROM PhongBans WHERE tenPhongBan = ?";
        Cursor cursor = database.rawQuery(query, new String[]{tenPhongBan});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                maPhongBan = cursor.getString(cursor.getColumnIndex("maPhongBan"));
            }
            cursor.close();
        }

        return maPhongBan;
    }

    public int getSoLuongNhanVienTrongPhongBan(String maPhongBan) {
        String query = "SELECT COUNT(*) FROM NhanViens WHERE maPhongBan = ?";
        Cursor cursor = database.rawQuery(query, new String[]{maPhongBan});
        int soLuongNhanVien = 0;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                soLuongNhanVien = cursor.getInt(0);
            }
            cursor.close();
        }

        return soLuongNhanVien;
    }
}
