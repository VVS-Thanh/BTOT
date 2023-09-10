package com.example.btot;

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

// Thêm phòng ban 2
        myDatabase.addPhongBan("PB002", "Phòng Ban B", 15);

// Thêm phòng ban 3
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
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                loadNhanVien();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không có phòng ban nào được chọn, hiển thị toàn bộ danh sách nhân viên
                loadAllNhanVien();
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
                // Thực hiện mở Activity khác (nếu có)
            }
        });
    }

    private void loadPhongBanData() {
        List<String> phongBanNames = myDatabase.getAllPhongBanNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phongBanNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhongBan.setAdapter(adapter);
    }

    private void loadNhanVien() {
        String selectedPhongBan = spinnerPhongBan.getSelectedItem().toString();
        List<String> nhanVienInfoList = myDatabase.getNhanVienInfoByPhongBan(selectedPhongBan);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nhanVienInfoList);
        lvDanhSach.setAdapter(adapter);
    }

    private void loadAllNhanVien() {
        List<String> nhanVienInfoList = myDatabase.getNhanVienInfoByPhongBan("");
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
            loadNhanVien();
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
        loadNhanVien();
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
