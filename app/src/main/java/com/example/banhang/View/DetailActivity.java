package com.example.banhang.View;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewProduct.*;
import com.example.banhang.database.*;
import com.example.banhang.View.fragment.*;
import com.example.banhang.View.RecycleViewGioHang.*;
public class DetailActivity extends AppCompatActivity {
    TextView tvGiaTien, tvNoiDungMoTa,tvTenSanPham;
    Button btnThemSanPham,btnBack;
    ImageView imgAnhSanPham;
    CreateDatabase createDatabase;
    FrameLayout mainFragment_Details;
    CartAdapter cartAdapte;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_detail);
        // Khởi tạo đối tượng CreateDatabase
        createDatabase = new CreateDatabase(this);
        //Ánh xạ view
        AnhXa();

        //Lấy dự liệu được intent qua từ ProductsAdapter
        String tenSanPham = getIntent().getStringExtra("tenSanPham");
        String anhSanPham = getIntent().getStringExtra("srcAnh");
        String moTa = getIntent().getStringExtra("mota");
        String giaTien = getIntent().getStringExtra("giaTien");

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("tk_mk login", Context.MODE_PRIVATE);
        String tenDangNhap = sharedPreferences.getString("Username","");
        //Gan Dữ liêu cho detail
        imgAnhSanPham.setImageBitmap(Utils.convertToBitmapFromAssets(getApplication(),anhSanPham));
        tvTenSanPham.setText(tenSanPham);
        tvNoiDungMoTa.setText(moTa);
        tvGiaTien.setText(giaTien);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //Khi Mua San Pham Can phai kiem tra thong tin khach hang da cap nhat thong tin day du chua
        //Neu Chua : chuyen sang trang cap nhat thong tin khach hang
        //Neu Roi : hien thi man hinh dialog Xac Nhan Mua
        // => luu du lieu vao database donHang va ...
        String tenKhachHang =createDatabase.GetCLHoVaTenKhachHang_Detail(tenDangNhap);



        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Kiểm tra nếu người dùng đã nhập tên khách hàng
                if(tenKhachHang == null){
                        Toast.makeText(getApplication(),"Bạn Cần Cập Nhật thông tin tài khoản đẻ được thêm vào giỏ hàng",Toast.LENGTH_SHORT).show();
                        Fragment fm ;
                        fm = new AboutFragment();
                        loadFragment(fm);
                } else {
                    ThemSanPhamVaoGioHang(tenSanPham, tenDangNhap);
                }
            }
        });
    }
    void loadFragment(Fragment fmNew){
        mainFragment_Details.setVisibility(View.VISIBLE);
        FragmentTransaction fmTran = getSupportFragmentManager().beginTransaction();
        fmTran.replace(R.id.mainFragment_Detail, fmNew);
        fmTran.addToBackStack(null);
        fmTran.commit();


    }
    void AnhXa(){
        tvGiaTien = findViewById(R.id.tvGiaTien_Detail);
        tvNoiDungMoTa = findViewById(R.id.tvNoiDung_Detail);
        btnThemSanPham  = findViewById(R.id.btnThemSanPham_Detail);
        tvTenSanPham = findViewById(R.id.tvTenSanPham_Detail);
        imgAnhSanPham = findViewById(R.id.imgAnhSanPham_Detail);
        btnBack = findViewById(R.id.btnBack);
        mainFragment_Details = findViewById(R.id.mainFragment_Detail);
    }


    boolean isTonTaiSanPhamTrongGioHang(String tenSanPham, String tenDangNhap) {
        SQLiteDatabase db = createDatabase.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + CreateDatabase.TB_GIO_HANG +
                    " WHERE " + CreateDatabase.CL_GIO_HANG_SAN_PHAM_ID + " = ?" +
                    " AND " + CreateDatabase.Cl_GIO_HANG_TEN_NGUOI_DUNG + " = ?";
            cursor = db.rawQuery(query, new String[]{createDatabase.GetIdSanPham(tenSanPham), tenDangNhap});

            return cursor != null && cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    void ThemSanPhamVaoGioHang(String tenSanPham, String tenDangNhap) {
        SQLiteDatabase db = createDatabase.getWritableDatabase();
        try {
            if(!isTonTaiSanPhamTrongGioHang(tenSanPham,tenDangNhap)){
                ContentValues values = new ContentValues();
                values.put(CreateDatabase.CL_GIO_HANG_SAN_PHAM_ID, createDatabase.GetIdSanPham(tenSanPham));
                values.put(CreateDatabase.Cl_GIO_HANG_TEN_NGUOI_DUNG, tenDangNhap);

                db.insert(CreateDatabase.TB_GIO_HANG, null, values);
                Toast.makeText(getApplication(),"Thêm Giỏ Hàng thành công",Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(getApplication(),"Sản Phẩm đã tồn tại trong giỏ hàng",Toast.LENGTH_SHORT).show();

            }

        } finally {
            db.close();
        }
    }

}
