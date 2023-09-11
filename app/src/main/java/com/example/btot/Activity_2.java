package com.example.btot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_2 extends AppCompatActivity {
    private TextView tvPhongBan, tvSoLuongNhanVien;
    private ImageView btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity2);

        tvPhongBan = findViewById(R.id.tvTenPhongBan);
        tvSoLuongNhanVien = findViewById(R.id.tvSoLuongNV);
        btn_back = findViewById(R.id.btnBack);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String phongBan = intent.getStringExtra("phongBan");
        int soLuongNhanVien = intent.getIntExtra("soLuongNhanVien", 0);

        // Hiển thị dữ liệu lên TextView
        tvPhongBan.setText("Tên phòng ban: " + phongBan);
        tvSoLuongNhanVien.setText("Số lượng nhân viên: " + soLuongNhanVien);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Activity_2.this, MainActivity.class);
                startActivity(intent2);
            }
        });
    }
}
