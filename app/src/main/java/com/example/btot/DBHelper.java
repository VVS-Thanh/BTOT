package com.example.btot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static String TAG = "DatabaseHelper";
    private static final String DB_Name ="bt2.db";
    private static final int DB_Version = 1;
    private SQLiteDatabase mDefaultWritableDatabase;
    private String databasePath;

    public DBHelper(Context context) {
        super(context, DB_Name, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.mDefaultWritableDatabase = sqLiteDatabase;
        String sql1 = "CREATE TABLE PhongBans (" +
                "maPhongBan String PRIMARY KEY NOT NULL," +
                "tenPhongBan text NOT NULL,"+
                "soPhongban int default null)";
        sqLiteDatabase.execSQL(sql1);

        String sql2 = "CREATE TABLE NhanViens(" +
                "maNhanVien String Primary key not null," +
                "tenNhanVien String not null UNIQUE," +
                "tuoiNhanVien INTEGER not null,"+
                "maPhongBan String NOT NULL," +
                "FOREIGN KEY (maPhongBan) REFERENCES PhongBans (maPhongBan))";
        sqLiteDatabase.execSQL(sql2);

        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        this.mDefaultWritableDatabase = sqLiteDatabase;
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PhongBans");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NhanViens");
    }
    //Kiem tra neu chua co db thi tao moi con co roi thi copy db vao asset.
    public void createDatabase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
//            copyDataBase();
        }
    }
    //Kiem tra thu db co ton tai chua
    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY);
            Log.e(TAG, "Database is exist.");
        } catch (Exception e) {
            Log.e(TAG, "Database does not exist.");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }
    @Override
    public SQLiteDatabase getWritableDatabase() {
        final SQLiteDatabase db;
        if(mDefaultWritableDatabase != null){
            db = mDefaultWritableDatabase;
        }else{
            db = super.getWritableDatabase();
        }
        return db;
    }
    @Override
    public synchronized void close(){
        if(mDefaultWritableDatabase!=null){
            mDefaultWritableDatabase.close();
        }
        super.close();
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
