package com.example.banhang.View.RecycleViewGioHang;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewProduct.*;
import com.example.banhang.View.RecyclerViewProductFavorite.ProductsFavoriteAdapter;
import com.example.banhang.database.CreateDatabase;
import com.example.banhang.View.RecycleViewGioHang.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.example.banhang.View.fragment.*;
import com.example.banhang.View.RecycleViewDonHang.*;

public class CartActivity extends AppCompatActivity implements CartAdapter.TotalAmountListener {
    // Khai báo biến
    ArrayList<Products> lstProductsCart;
    CartAdapter cartAdapter;
    CreateDatabase databaseHelper;
    SQLiteDatabase database;
    RecyclerView rcvCart;
    CheckBox cbChonTatCa;
    TextView tvTongTien;
    Button btnMuaHang;
    ImageView imgBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cart);
        AnhXa();
        LoadDataProducts(CartActivity.this);
        databaseHelper = new CreateDatabase(CartActivity.this);
        database  = databaseHelper.getWritableDatabase();
        cartAdapter = new CartAdapter(lstProductsCart,databaseHelper, this);
        rcvCart.setAdapter(cartAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvCart.setLayoutManager(linearLayoutManager);

        cbChonTatCa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Chọn hoặc bỏ chọn tất cả các sản phẩm
                cartAdapter.selectAll(isChecked);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một đơn hàng mới
                DonHang newOrder = createNewOrder();
                // Thêm đơn hàng vào cơ sở dữ liệu
                addToOrderTable(newOrder);
                // Lặp qua danh sách sản phẩm trong giỏ hàng
                for (Products product : lstProductsCart) {
                    // Kiểm tra nếu sản phẩm đã được đánh dấu chọn
                    if (product.isChecked()) {
                        // Lấy ID của sản phẩm dựa vào tên
                        String productName = product.getName();
                        String productId = databaseHelper.GetIdSanPham(productName);

                        // Thêm các xử lý khác ở đây...
                        // Xóa các sản phẩm đã chọn từ giỏ hàng
                        cartAdapter.deleteSelectedProductsFromCart(productId,lstProductsCart,database);
                        tvTongTien.setText("");

                    }
                }

                // Cập nhật lại danh sách sản phẩm trong Adapter
                cartAdapter.setData(Utils.LoadDaTaProductsCart(CartActivity.this));
            }
        });

    }
    void AnhXa(){
        rcvCart = findViewById(R.id.rcvGiohang);
        cbChonTatCa = findViewById(R.id.checkboxSelectAll);
        tvTongTien = findViewById(R.id.tvTongTien);
        btnMuaHang = findViewById(R.id.btnMuaHang);
        imgBack = findViewById(R.id.imgBack_Cart);
    }
    void LoadDataProducts(Context context){
        lstProductsCart = Utils.LoadDaTaProductsCart(context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTotalAmountChanged(double totalAmount) {
        tvTongTien.setText("Tổng Tiền: " + totalAmount + " VND");
    }
    // Phương thức để thêm đơn hàng vào cơ sở dữ liệu
    private DonHang createNewOrder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        // Tính tổng tiền
        double totalAmount = cartAdapter.calculateAndNotifyTotalAmount();

        // Tạo một đơn hàng mới với thông tin từ giỏ hàng
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("tk_mk login", Context.MODE_PRIVATE);
        String tenDangNhap = sharedPreferences.getString("hovaten","");
        DonHang newOrder = new DonHang( currentDateAndTime, tenDangNhap, totalAmount, 0);
        return newOrder;
    }
    // Phương thức để thêm đơn hàng vào cơ sở dữ liệu
    private void addToOrderTable(DonHang order) {
        ContentValues values = order.toContentValues();
        long newRowId = database.insert(CreateDatabase.TB_DON_HANG, null, values);
        if (newRowId == -1) {
            // Nếu thêm không thành công
            Toast.makeText(this, "Thêm đơn hàng không thành công", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Thêm đơn hàng  thành công", Toast.LENGTH_SHORT).show();

        }
    }


}
