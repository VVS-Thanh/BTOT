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

    public long addNhanVien(String maNhanVien, String tenNhanVien, int tuoi, String maPhongBan) {
        // Kiểm tra xem mã nhân viên đã tồn tại trong cơ sở dữ liệu chưa
        if (isNhanVienExists(maNhanVien)) {
            // Mã nhân viên đã tồn tại, trả về -1 hoặc một giá trị thông báo lỗi khác (tùy theo ý muốn của bạn)
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put("maNhanVien", maNhanVien);
        values.put("tenNhanVien", tenNhanVien);
        values.put("tuoi", tuoi);
        values.put("maPhongBan", maPhongBan);

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
    public List<String> getNhanVienInfoByPhongBan(String tenPhongBan) {
        List<String> nhanVienInfoList = new ArrayList<>();
        String query = "SELECT PhongBans.tenPhongBan, NhanViens.maNhanVien, NhanViens.tenNhanVien, NhanViens.tuoi " +
                "FROM PhongBans " +
                "INNER JOIN NhanViens ON PhongBans.maPhongBan = NhanViens.maPhongBan " +
                "WHERE PhongBans.tenPhongBan = ?";
        Cursor cursor = database.rawQuery(query, new String[]{tenPhongBan});
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
}
