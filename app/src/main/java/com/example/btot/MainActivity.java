package com.example.btot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerPhongBan;
    private ListView lvDanhSach;
    private Button btnThemNhanVien, btnThem, btnXoa, btnOpenAct;
    private LinearLayout layoutThemNhanVien;
    private EditText edtMaNV, edtTenNV, edtTuoi;
    private MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabase = new MyDatabase(this);
        myDatabase.open();
        myDatabase.addPhongBan("PB001", "Phòng Ban A", 10);
        myDatabase.addPhongBan("PB002", "Phòng Ban B", 15);
        myDatabase.addPhongBan("PB003", "Phòng Ban C", 20);

        spinnerPhongBan = findViewById(R.id.spinnerPhongBan);
        lvDanhSach = findViewById(R.id.lvDanhSach);
        btnThemNhanVien = findViewById(R.id.btnThemNhanVien);
        btnThem = findViewById(R.id.btnThem);
        btnXoa = findViewById(R.id.btnXoa);
        btnOpenAct = findViewById(R.id.btnOpenAct);
        layoutThemNhanVien = findViewById(R.id.layoutThemNhanVien);
        edtMaNV = findViewById(R.id.edtMaNV);
        edtTenNV = findViewById(R.id.edtTenNV);
        edtTuoi = findViewById(R.id.edtTuoi);

        loadPhongBanData();
        loadAllNhanVien();

        spinnerPhongBan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedMaPhongBan = myDatabase.getMaPhongBanByTenPhongBan(adapterView.getSelectedItem().toString());
                loadNhanVien(selectedMaPhongBan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Không cần xử lý
            }
        });

        btnThemNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutThemNhanVien.setVisibility(View.VISIBLE);
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themNhanVien();
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xoaNhanVien();
            }
        });

        btnOpenAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy mã phòng ban đang được chọn
                String selectedMaPhongBan = myDatabase.getMaPhongBanByTenPhongBan(spinnerPhongBan.getSelectedItem().toString());
                // Lấy số lượng nhân viên của phòng ban đó
                int soLuongNhanVien = myDatabase.getSoLuongNhanVienTrongPhongBan(selectedMaPhongBan);

                // Tạo Intent để mở Activity_2
                Intent intent = new Intent(MainActivity.this, Activity_2.class);
                // Đưa tên phòng ban và số lượng nhân viên vào Intent
                intent.putExtra("phongBan", spinnerPhongBan.getSelectedItem().toString());
                intent.putExtra("soLuongNhanVien", soLuongNhanVien);

                // Mở Activity_2
                startActivity(intent);
            }
        });
    }

    private void loadPhongBanData() {
        List<String> phongBanNames = myDatabase.getAllPhongBanNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phongBanNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhongBan.setAdapter(adapter);
    }

    private void loadNhanVien(String maPhongBan) {
        List<String> nhanVienInfoList = myDatabase.getNhanVienInfoByPhongBan(maPhongBan);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nhanVienInfoList);
        lvDanhSach.setAdapter(adapter);
    }

    private void loadAllNhanVien() {
        List<String> nhanVienInfoList = myDatabase.getAllNhanVienInfo();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nhanVienInfoList);
        lvDanhSach.setAdapter(adapter);
    }

    private void themNhanVien() {
        String maNV = edtMaNV.getText().toString().trim();
        String tenNV = edtTenNV.getText().toString().trim();
        String tuoi = edtTuoi.getText().toString().trim();
        String phongBan = spinnerPhongBan.getSelectedItem().toString();

        if (maNV.isEmpty() || tenNV.isEmpty() || tuoi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int tuoiInt;
        try {
            tuoiInt = Integer.parseInt(tuoi);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Tuổi phải là số nguyên", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = myDatabase.addNhanVien(maNV, tenNV, tuoiInt, phongBan);

        if (result == -1) {
            Toast.makeText(this, "Mã nhân viên đã tồn tại hoặc có lỗi xảy ra", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
            layoutThemNhanVien.setVisibility(View.GONE);
            clearEditTextFields();
            loadNhanVien(phongBan);
        }
    }

    private void xoaNhanVien() {
        String maNV = edtMaNV.getText().toString().trim();

        if (maNV.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã nhân viên", Toast.LENGTH_SHORT).show();
            return;
        }

        myDatabase.deleteNhanVien(maNV);
        Toast.makeText(this, "Xóa nhân viên thành công", Toast.LENGTH_SHORT).show();
        clearEditTextFields();
        String selectedMaPhongBan = spinnerPhongBan.getSelectedItem().toString();
        loadNhanVien(selectedMaPhongBan);
    }

    private void clearEditTextFields() {
        edtMaNV.setText("");
        edtTenNV.setText("");
        edtTuoi.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDatabase.close();
    }
}
