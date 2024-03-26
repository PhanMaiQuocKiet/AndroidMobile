package com.example.banhang.View.RecyclerViewProduct;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.banhang.View.RecyclerViewCategory.*;
import com.example.banhang.database.*;
import com.example.banhang.View.RecycleViewDonHang.*;

public class Utils {

    public static Bitmap convertToBitmapFromAssets(Context context, String nameImage) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open("images/" + nameImage);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<ProductsCategory> loadCategoriesFromDatabase(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        ArrayList<ProductsCategory> categoryList = new ArrayList<>();
        SQLiteDatabase db = createDatabase.getReadableDatabase();

        String query = "SELECT " + CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID + ", " + CreateDatabase.CL_TEN_LOAI_SAN_PHAM +
                " FROM " + CreateDatabase.TB_LOAI_SAN_PHAM;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String categoryId = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID));
                @SuppressLint("Range")String categoryName = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_LOAI_SAN_PHAM));

                ProductsCategory category = new ProductsCategory(categoryId, categoryName);
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categoryList;
    }
    @SuppressLint("Range")
    public static ArrayList<Products> LoadProductInCategoryFromDatabase(Context context, String categoryId) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        ArrayList<Products> productList = new ArrayList<>();
        SQLiteDatabase db = createDatabase.getReadableDatabase();

        // Câu truy vấn đã được sửa để kết hợp với mã danh mục sản phẩm đầu vào
        String query = "SELECT " + CreateDatabase.CL_TEN_SAN_PHAM + ", " + CreateDatabase.CL_GIA_BAN + ", " + CreateDatabase.CL_ANH_SAN_PHAM +
                " FROM " + CreateDatabase.TB_LOAI_SAN_PHAM +
                " INNER JOIN " + CreateDatabase.TB_SAN_PHAM +
                " ON " + CreateDatabase.TB_LOAI_SAN_PHAM + "." + CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID +
                " = " + CreateDatabase.TB_SAN_PHAM + "." + CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID +
                " WHERE " + CreateDatabase.TB_SAN_PHAM + "." + CreateDatabase.CL_LOAI_SAN_PHAM_ID + " = ?";

        // Tham số của câu truy vấn
        String[] selectionArgs = {categoryId};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                String productName = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_SAN_PHAM));
                String productPrice = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_GIA_BAN));
                String productImage = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_ANH_SAN_PHAM));

                Products product = new Products(context, productName, productPrice, productImage);
                productList.add(product);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return productList;
    }

    public static ArrayList<Products> LoadDataProductInCategory(Context context, String categoryId) {
        ArrayList<Products> lstUser = new ArrayList<>();
        ArrayList<Products> products = Utils.LoadProductInCategoryFromDatabase(context,categoryId);

        for (Products item : products) {
            lstUser.add(new Products(context, item.getName(),item.getPrice(), item.getImage()));
        }

        return lstUser;
    }
    public static ArrayList<ProductsCategory> LoadDataCategory(Context context) {
        ArrayList<ProductsCategory> lstUser = new ArrayList<>();
        ArrayList<ProductsCategory> categoryList = Utils.loadCategoriesFromDatabase(context);

        for (ProductsCategory category : categoryList) {
            lstUser.add(new ProductsCategory(category.getID(), category.getName()));
        }

        return lstUser;
    }
    @SuppressLint("Range")
    public static ArrayList<DonHang> LoadOrderFromDatabase(Context context, String tennguoidung) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        ArrayList<DonHang> listDonHang = new ArrayList<>();
        SQLiteDatabase db = createDatabase.getReadableDatabase();

        String query = "SELECT * FROM " + CreateDatabase.TB_DON_HANG +
                " WHERE " + CreateDatabase.CL_TEN_NGUOI_DUNG + " = ?" +
                " AND " + CreateDatabase.CL_DON_HANG_ID + " IN " +
                " (SELECT " + CreateDatabase.CL_DON_HANG_ID + " FROM " + CreateDatabase.TB_DON_HANG +
                " GROUP BY " + CreateDatabase.CL_NGAY_DAT_HANG + ")" +
                " ORDER BY " + CreateDatabase.CL_NGAY_DAT_HANG + " DESC ";

        String[] selectionArgs = {tennguoidung};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                 String MaDonHang = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_DON_HANG_ID));
                String NgayDatHang = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_NGAY_DAT_HANG));
                 Double TongTien = Double.parseDouble(cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TOTAL)));
                 int TrangThai = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TRANG_THAI)));
                String MaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_SAN_PHAM_ID_Donhang));
                DonHang newDonHang = new DonHang(MaDonHang, NgayDatHang, tennguoidung, TongTien, TrangThai, MaSanPham);
                listDonHang.add(newDonHang);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listDonHang;
    }
    public static ArrayList<DonHang> LoadDataOrder(Context context , String tenNguoiDung) {
        ArrayList<DonHang> listDonHang = new ArrayList<>();
        ArrayList<DonHang> donHangArrayList = Utils.LoadOrderFromDatabase(context,tenNguoiDung);

        for (DonHang donHang : donHangArrayList) {
            if (donHang != null) {
                listDonHang.add(new DonHang(context,donHang.getMaDonHang(),donHang.getNgayDatHang(),donHang.getTenNguoiDung(),donHang.getTongTien(),donHang.getTrangThai(),donHang.getIdSanPham()));
            }
        }

        return listDonHang;
    }
    public static ArrayList<DonHang> LoadOrderFromDatabase(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        ArrayList<DonHang> listDonHang = new ArrayList<>();
        SQLiteDatabase db = createDatabase.getReadableDatabase();

        String query = "SELECT * FROM " + CreateDatabase.TB_DON_HANG +
                " WHERE " + CreateDatabase.CL_DON_HANG_ID + " IN " +
                " (SELECT " + CreateDatabase.CL_DON_HANG_ID + " FROM " + CreateDatabase.TB_DON_HANG +
                " GROUP BY " + CreateDatabase.CL_NGAY_DAT_HANG + ")" +
                " ORDER BY " + CreateDatabase.CL_NGAY_DAT_HANG + " DESC ";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String MaDonHang = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_DON_HANG_ID));
                @SuppressLint("Range") String NgayDatHang = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_NGAY_DAT_HANG));
                @SuppressLint("Range") String TenDangNhap = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_NGUOI_DUNG));
                @SuppressLint("Range") Double TongTien = Double.parseDouble(cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TOTAL)));
                @SuppressLint("Range") int TrangThai = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TRANG_THAI)));
                @SuppressLint("Range") String MaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_SAN_PHAM_ID_Donhang));
                DonHang newDonHang = new DonHang(MaDonHang, NgayDatHang, TenDangNhap, TongTien, TrangThai, MaSanPham);
                listDonHang.add(newDonHang);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listDonHang;
    }
    public static ArrayList<DonHang> LoadDataOrder(Context context) {
        ArrayList<DonHang> listDonHang = new ArrayList<>();
        ArrayList<DonHang> donHangArrayList = Utils.LoadOrderFromDatabase(context);

        for (DonHang donHang : donHangArrayList) {
            if (donHang != null) {
                listDonHang.add(new DonHang(context,donHang.getMaDonHang(),donHang.getNgayDatHang(),donHang.getTenNguoiDung(),donHang.getTongTien(),donHang.getTrangThai(),donHang.getIdSanPham()));
            }
        }

        return listDonHang;
    }
    public static ArrayList<Products> LoadProductsFromDatabase(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        ArrayList<Products> listProducts = new ArrayList<>();
        SQLiteDatabase db = createDatabase.getReadableDatabase();

        String query = "SELECT * FROM " + CreateDatabase.TB_SAN_PHAM;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String tenSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_SAN_PHAM));
                @SuppressLint("Range") String giaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_GIA_BAN));
                @SuppressLint("Range") String moTaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_MO_TA));
                @SuppressLint("Range") String anhSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_ANH_SAN_PHAM));
                @SuppressLint("Range")  String idTheLoaiSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_LOAI_SAN_PHAM_ID));

                Products newProducts1 = new Products(context, tenSanPham, giaSanPham, moTaSanPham, anhSanPham, idTheLoaiSanPham);
                listProducts.add(newProducts1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listProducts;
    }

    public static ArrayList<Products> LoadDaTaProducts(Context context) {
        ArrayList<Products> lstProducts = new ArrayList<>();
        ArrayList<Products> productsArrayList = Utils.LoadProductsFromDatabase(context);

        for (Products products : productsArrayList) {
            if (products != null) {
                lstProducts.add(new Products(context, products.getName(), products.getPrice(), products.getDes(), products.getImage(), products.getCategoryId()));
            }
        }

        return lstProducts;
    }
    public static ArrayList<Products> LoadProductsFavoriteFromDatabase(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        ArrayList<Products> listProducts = new ArrayList<>();
        SQLiteDatabase db = createDatabase.getReadableDatabase();

        String query = "SELECT " + CreateDatabase.CL_TEN_SAN_PHAM + ", " + CreateDatabase.CL_GIA_BAN + ", " + CreateDatabase.CL_ANH_SAN_PHAM + ", " + CreateDatabase.CL_MO_TA + ", " + CreateDatabase.CL_THE_LOAI_SAN_PHAM_ID +
                " From " + CreateDatabase.TB_YEU_THICH +
                " inner  join " + CreateDatabase.TB_SAN_PHAM +
                " on " + CreateDatabase.TB_YEU_THICH + "." + CreateDatabase.CL_SAN_PHAM_YEU_THICH_ID +
                " = " + CreateDatabase.TB_SAN_PHAM + "." + CreateDatabase.CL_SAN_PHAM_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String tenSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_SAN_PHAM));
                @SuppressLint("Range") String giaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_GIA_BAN));
                @SuppressLint("Range") String moTaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_MO_TA));
                @SuppressLint("Range") String anhSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_ANH_SAN_PHAM));
                @SuppressLint("Range")  String idTheLoaiSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_LOAI_SAN_PHAM_ID));

                Products newProducts1 = new Products(context, tenSanPham, giaSanPham, moTaSanPham, anhSanPham, idTheLoaiSanPham);
                listProducts.add(newProducts1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listProducts;
    }
    public static ArrayList<Products> LoadProductsCartFromDatabase(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        ArrayList<Products> listProducts = new ArrayList<>();
        SQLiteDatabase db = createDatabase.getReadableDatabase();

        String query = "SELECT " + CreateDatabase.CL_TEN_SAN_PHAM + ", " + CreateDatabase.CL_GIA_BAN + ", " + CreateDatabase.CL_ANH_SAN_PHAM +
                " From " + CreateDatabase.TB_GIO_HANG +
                " inner  join " + CreateDatabase.TB_SAN_PHAM +
                " on " + CreateDatabase.TB_GIO_HANG + "." + CreateDatabase.CL_GIO_HANG_SAN_PHAM_ID +
                " = " + CreateDatabase.TB_SAN_PHAM + "." + CreateDatabase.CL_SAN_PHAM_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String tenSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_SAN_PHAM));
                @SuppressLint("Range") String giaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_GIA_BAN));
                @SuppressLint("Range") String anhSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_ANH_SAN_PHAM));

                Products newProducts1 = new Products(context, tenSanPham, giaSanPham, anhSanPham);
                listProducts.add(newProducts1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listProducts;
    }

    public static ArrayList<Products> LoadDaTaProductsCart(Context context) {
        ArrayList<Products> lstProducts = new ArrayList<>();
        ArrayList<Products> productsArrayList = Utils.LoadProductsCartFromDatabase(context);
        int soLuong = 0 ;

        for (Products products : productsArrayList) {
            if (products != null) {
                lstProducts.add(new Products(context, products.getName(), products.getPrice(), products.getDes(), products.getImage(), products.getCategoryId()));
            }
        }


        return lstProducts;
    }
    public static void ThongBao(Context context, String thongBao){
        Toast.makeText(context,thongBao,Toast.LENGTH_SHORT).show();
    }
    public static ArrayList<Products> LoadDetailFromDatabase(Context context, String ngayDatHang, String tongTien) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        ArrayList<Products> listProducts = new ArrayList<>();
        SQLiteDatabase db = createDatabase.getReadableDatabase();

        String query = "SELECT " +
                CreateDatabase.TB_SAN_PHAM + "." + CreateDatabase.CL_TEN_SAN_PHAM + ", " +
                CreateDatabase.TB_SAN_PHAM + "." + CreateDatabase.CL_GIA_BAN + ", " +
                CreateDatabase.TB_SAN_PHAM + "." + CreateDatabase.CL_ANH_SAN_PHAM + ", " +
                CreateDatabase.TB_SAN_PHAM + "." + CreateDatabase.CL_MO_TA + ", " +
                CreateDatabase.TB_DON_HANG + "." + CreateDatabase.CL_SO_LUONG +
                " FROM " + CreateDatabase.TB_DON_HANG +
                " INNER JOIN " + CreateDatabase.TB_SAN_PHAM +
                " ON " + CreateDatabase.TB_DON_HANG + "." + CreateDatabase.CL_SAN_PHAM_ID_Donhang +
                " = " + CreateDatabase.TB_SAN_PHAM + "." + CreateDatabase.CL_SAN_PHAM_ID +
                " WHERE " + CreateDatabase.TB_DON_HANG + "." + CreateDatabase.CL_NGAY_DAT_HANG + " = '" + ngayDatHang + "'" +
                " AND " + CreateDatabase.TB_DON_HANG + "." + CreateDatabase.CL_TOTAL + " = " + tongTien;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String tenSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_SAN_PHAM));
                @SuppressLint("Range") String giaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_GIA_BAN));
                @SuppressLint("Range") String moTaSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_MO_TA));
                @SuppressLint("Range") String anhSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_ANH_SAN_PHAM));
                @SuppressLint("Range") int soLuong = cursor.getInt(cursor.getColumnIndex(CreateDatabase.CL_SO_LUONG));

                Products newProduct = new Products(context, tenSanPham, giaSanPham, moTaSanPham, anhSanPham, soLuong);
                listProducts.add(newProduct);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listProducts;
    }
    public static ArrayList<Products> LoadDataOrderDetails(Context context,String ngayDatHang, String tongTien) {
        ArrayList<Products> lstProducts = new ArrayList<>();
        ArrayList<Products> productsArrayList = Utils.LoadDetailFromDatabase(context,ngayDatHang,tongTien);

        for (Products products : productsArrayList) {
            if (products != null) {
                lstProducts.add(new Products(context, products.getName(), products.getPrice(), products.getDes(), products.getImage(),products.getSoLuong()));
            }
        }


        return lstProducts;
    }


}
