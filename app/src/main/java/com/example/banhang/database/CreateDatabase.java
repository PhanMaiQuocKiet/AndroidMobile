package com.example.banhang.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CreateDatabase extends SQLiteOpenHelper {
    //*****Cơ Sở Dữ Liệu*****
    //Tên Database
    private final static String DATABASE_NAME = "ShoppingSouvenir";

    //Version
    private final static int VERSION = 16;


    // Bảng Người dùng
    public static final String TB_DANG_NHAP_KHACH_HANG = "DangNhapKhachHang";
    public static final String CL_TEN_DANGNHAP = "TenDangNhap";
    public static final String CL_MAT_KHAU = "MatKhau";

    public static final String CL_NGAYSINH= "NGAYSINH";
    public static final String CL_CMND= "CMND";
    public static final String CL_GIOITINH= "GIOITINH";
    public static final String CL_VAI_TRO = "VaiTro";
    public static final String CL_TEN_KHACH_HANG = "TenKhachHang";
    public static final String CL_DIA_CHI = "DiaChi";
    public static final String CL_SO_DIEN_THOAI = "SoDienThoai";
    public static final String CL_EMAIL = "email";
    //bảng GioHang
    public static final String TB_GIO_HANG = "GioHang";
    public static final String CL_GIO_HANG_SAN_PHAM_ID ="maSanPham";
    public static final String Cl_GIO_HANG_TEN_NGUOI_DUNG ="TenDangNhap";

    // Bảng DonHang
    public static final String TB_DON_HANG = "DonHang";
    public static final String CL_DON_HANG_ID = "MaDonHang";

    public static final String CL_NGAY_DAT_HANG = "NgayDatHang";
    public static final String CL_TEN_NGUOI_DUNG = "TenDangNhap";

    public static final String CL_TOTAL = "TongTien";
    public static final String CL_TRANG_THAI = "TrangThai";

    public static final String CL_SAN_PHAM_ID_Donhang = "MaSanPham";



    // Bảng SanPham
    public static final String TB_SAN_PHAM = "SanPham";
    public static final String CL_SAN_PHAM_ID = "MaSanPham";
    public static final String CL_TEN_SAN_PHAM = "TenSanPham";
    public static final String CL_GIA_BAN = "GiaBan";
    public static final String CL_ANH_SAN_PHAM = "AnhSanPham";
    public static final String CL_MO_TA = "MoTa";
    public static final String CL_LOAI_SAN_PHAM_ID = "MaLoaiSanPham";


    // Bảng DanhGiaKhachHang
    private static final String TB_DANH_GIA_KHACH_HANG = "DanhGiaKhachHang";
    private static final String  CL_Ma_DANH_GIA = "MADANHGIA";
    private static final String  CL_MA_KHACH_HANG_DANH_GIA = "MaKhachHangDanhGia";
    private static final String CL_DANHGIA_KHACH_HANG_ID = "MaKhachHang";
    // Bảng YeuThich
    public static final String TB_YEU_THICH = "YeuThich";
    public static final String CL_YEU_THICH_ID = "MaYeuThich";
    public static final String CL_TEN_KHACH_HANG_YEU_THICH = "TenKhachHangYeuThich";
    public static final String CL_SAN_PHAM_YEU_THICH_ID = "MaSanPhamYeuThich";
    // Bảng LoaiSanPham
    public static final String TB_LOAI_SAN_PHAM = "LoaiSanPham";
    public static final String CL_THE_LOAI_SAN_PHAM_ID = "MaLoaiSanPham";
    public static  final String CL_TEN_LOAI_SAN_PHAM= "TENLOAISANPHAM";


    public CreateDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        setWriteAheadLoggingEnabled(true); // Đảm bảo hỗ trợ UTF-8

    }

    //Tạo Bảng
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tạo Bảng GioHang
        String CREATE_TABLE_GIO_HANG = "CREATE TABLE " + TB_GIO_HANG + "("
                + CL_GIO_HANG_SAN_PHAM_ID + " TEXT,"
                + Cl_GIO_HANG_TEN_NGUOI_DUNG + " TEXT,"
                + "FOREIGN KEY (" + CL_GIO_HANG_SAN_PHAM_ID + ") REFERENCES " + TB_SAN_PHAM + "(" + CL_SAN_PHAM_ID + "),"
                + "FOREIGN KEY (" + Cl_GIO_HANG_TEN_NGUOI_DUNG + ") REFERENCES " + TB_DANG_NHAP_KHACH_HANG + "(" + CL_TEN_KHACH_HANG + ")"
                + ")";
            db.execSQL(CREATE_TABLE_GIO_HANG);

        // Tạo bảng DangNhapKhachHang với username là khóa chính
        String CREATE_TABLE_DANG_NHAP_KHACH_HANG = "CREATE TABLE " + TB_DANG_NHAP_KHACH_HANG + "("
                + CL_TEN_DANGNHAP + " TEXT PRIMARY KEY,"
                + CL_MAT_KHAU + " TEXT,"
                + CL_NGAYSINH + " DATE,"
                + CL_CMND + " INTEGER ,"
                + CL_GIOITINH + " TEXT,"
                + CL_VAI_TRO + " TEXT,"
                + CL_TEN_KHACH_HANG + " TEXT,"
                + CL_DIA_CHI + " TEXT,"
                + CL_SO_DIEN_THOAI + " TEXT,"
                + CL_EMAIL + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_DANG_NHAP_KHACH_HANG);
        // Tạo bảng YeuThich
        String CREATE_TABLE_YEU_THICH = "CREATE TABLE " + TB_YEU_THICH + "("
                + CL_YEU_THICH_ID + " INTEGER PRIMARY KEY,"
                + CL_TEN_KHACH_HANG_YEU_THICH + " TEXT," // Change to TEXT, as it references CL_TEN_DANGNHAP
                + CL_SAN_PHAM_YEU_THICH_ID + " INTEGER,"
                + "FOREIGN KEY(" + CL_TEN_KHACH_HANG_YEU_THICH + ") REFERENCES " + TB_DANG_NHAP_KHACH_HANG + "(" + CL_TEN_DANGNHAP + "),"
                + "FOREIGN KEY(" + CL_SAN_PHAM_YEU_THICH_ID + ") REFERENCES " + TB_SAN_PHAM + "(" + CL_SAN_PHAM_ID + ")"
                + ")";
        db.execSQL(CREATE_TABLE_YEU_THICH);
        // Tạo bảng SanPham
        String CREATE_TABLE_SAN_PHAM = "CREATE TABLE " + TB_SAN_PHAM + "("
                + CL_SAN_PHAM_ID + " INTEGER PRIMARY KEY,"
                + CL_TEN_SAN_PHAM + " TEXT,"
                + CL_GIA_BAN + " REAL,"
                + CL_LOAI_SAN_PHAM_ID + " INTEGER,"
                + CL_MO_TA + " TEXT,"
                + CL_ANH_SAN_PHAM + " TEXT,"  // Thêm cột ảnh sản phẩm
                + "FOREIGN KEY(" + CL_LOAI_SAN_PHAM_ID + ") REFERENCES " + TB_LOAI_SAN_PHAM + "(" + CL_THE_LOAI_SAN_PHAM_ID + ")"
                + ")";
        db.execSQL(CREATE_TABLE_SAN_PHAM);
        // Tạo bảng DanhGiaKhachHang
        String CREATE_TABLE_DANH_GIA_KHACH_HANG = "CREATE TABLE " + TB_DANH_GIA_KHACH_HANG + "("
                + CL_Ma_DANH_GIA + " INTEGER PRIMARY KEY,"
                + CL_MA_KHACH_HANG_DANH_GIA + " INTEGER,"
                + CL_DANHGIA_KHACH_HANG_ID + " INTEGER,"
                + "FOREIGN KEY(" + CL_MA_KHACH_HANG_DANH_GIA + ") REFERENCES " + TB_DANG_NHAP_KHACH_HANG + "(" + CL_TEN_DANGNHAP + ")"
                + ")";
        db.execSQL(CREATE_TABLE_DANH_GIA_KHACH_HANG);

        // Tạo bảng LoaiSanPham
        String CREATE_TABLE_LOAI_SAN_PHAM = "CREATE TABLE " + TB_LOAI_SAN_PHAM + "("
                + CL_LOAI_SAN_PHAM_ID + " INTEGER PRIMARY KEY,"
                + CL_TEN_LOAI_SAN_PHAM + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_LOAI_SAN_PHAM);

        // Tạo bảng DonHang
        String CREATE_TABLE_DON_HANG = "CREATE TABLE " + TB_DON_HANG + "("
                + CL_DON_HANG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CL_NGAY_DAT_HANG + " TEXT,"
                + CL_TEN_NGUOI_DUNG + " TEXT," // Giả sử TenDangNhap là tên người dùng đặt hàng
                + CL_TOTAL + " REAL,"
                + CL_TRANG_THAI + " INTEGER,"
                + CL_SAN_PHAM_ID_Donhang + " INTEGER," // Thêm cột ID sản phẩm
                + "FOREIGN KEY(" + CL_TEN_NGUOI_DUNG + ") REFERENCES " + TB_DANG_NHAP_KHACH_HANG + "(" + CL_TEN_DANGNHAP +"),"
                + "FOREIGN KEY(" + CL_SAN_PHAM_ID + ") REFERENCES " + TB_SAN_PHAM + "(" + CL_SAN_PHAM_ID + ")"
                + ")";
        db.execSQL(CREATE_TABLE_DON_HANG);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa và tạo lại bảng nếu có bất kỳ thay đổi cấu trúc nào
        db.execSQL("DROP TABLE IF EXISTS " + TB_DANG_NHAP_KHACH_HANG);
        db.execSQL("DROP TABLE IF EXISTS " + TB_DON_HANG);
        db.execSQL("DROP TABLE IF EXISTS " + TB_SAN_PHAM);
        db.execSQL("DROP TABLE IF EXISTS " + TB_DANH_GIA_KHACH_HANG);
        db.execSQL("DROP TABLE IF EXISTS " + TB_LOAI_SAN_PHAM);
        db.execSQL("DROP TABLE IF EXISTS " + TB_YEU_THICH);
        db.execSQL("DROP TABLE IF EXISTS " + TB_GIO_HANG);

        // Tạo lại bảng mới
        onCreate(db);
    }
 //method
    @SuppressLint("Range")
    public String getMatKhauDatabas(String tenDangNhap) {
        String matKhau = null;

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            // Truy vấn để lấy mật khẩu từ cơ sở dữ liệu
            String query = "SELECT " + CreateDatabase.CL_MAT_KHAU + " FROM " + CreateDatabase.TB_DANG_NHAP_KHACH_HANG +
                    " WHERE " + CreateDatabase.CL_TEN_DANGNHAP + " = ?";

            try (Cursor cursor = db.rawQuery(query, new String[]{tenDangNhap})) {
                // Kiểm tra xem có dữ liệu hay không
                if (cursor.moveToFirst()) {
                    matKhau = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_MAT_KHAU));
                }
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu có
            e.printStackTrace();
        }

        return matKhau;
    }

    @SuppressLint("Range")
    public String getClTenDangnhapDatabase(String tenDangNhap) {
        String tendangnhap = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Query để lấy Tài Khoản từ cơ sở dữ liệu
        String query = "SELECT " + CreateDatabase.CL_TEN_DANGNHAP + " FROM " + CreateDatabase.TB_DANG_NHAP_KHACH_HANG +
                " WHERE " + CreateDatabase.CL_TEN_DANGNHAP + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{tenDangNhap});

        // Kiểm tra xem có dữ liệu hay không
        if (cursor.moveToFirst()) {
            tendangnhap = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_DANGNHAP));
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return tendangnhap;
    }

    public void setAdminRoleForUser(String tenDangNhap) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Sử dụng ContentValues để chuẩn bị dữ liệu cần cập nhật
        ContentValues values = new ContentValues();
        values.put(CreateDatabase.CL_VAI_TRO, "admin");

        // Cập nhật vai trò cho người dùng trong bảng DangNhapKhachHang
        db.update(
                TB_DANG_NHAP_KHACH_HANG,
                values,
                CL_TEN_DANGNHAP + " = ?",
                new String[]{tenDangNhap}
        );

        // Đóng kết nối database
        db.close();
    }
    public void setCustomerRoleForUser(String tenDangNhap) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Sử dụng ContentValues để chuẩn bị dữ liệu cần cập nhật
        ContentValues values = new ContentValues();
        values.put(CreateDatabase.CL_VAI_TRO, "customer");

        // Cập nhật vai trò cho người dùng trong bảng DangNhapKhachHang
        db.update(
                TB_DANG_NHAP_KHACH_HANG,
                values,
                CL_TEN_DANGNHAP + " = ?",
                new String[]{tenDangNhap}
        );

        // Đóng kết nối database
        db.close();
    }
    //phuong thuc lay ra chuc vu cua nguoi dung
    @SuppressLint("Range")
    public String GetCLVaitro(String tendangnhap){
        String vaiTro = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Query để lấy chức vụ từ cơ sở dữ liệu
        String query = "SELECT " + CreateDatabase. CL_VAI_TRO  + " FROM " + CreateDatabase.TB_DANG_NHAP_KHACH_HANG +
                " WHERE " + CreateDatabase.CL_TEN_DANGNHAP + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{tendangnhap});

        // Kiểm tra xem có dữ liệu hay không
        if (cursor.moveToFirst()) {
            vaiTro = cursor.getString(cursor.getColumnIndex(CreateDatabase. CL_VAI_TRO ));
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return vaiTro;
    }
    //Cập nhật ảnh
    public void updateAnhSanPham(int maSanPham, String duongDanAnh) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CL_ANH_SAN_PHAM, duongDanAnh);

        db.update(
                TB_SAN_PHAM,
                values,
                CL_SAN_PHAM_ID + " = ?",
                new String[]{String.valueOf(maSanPham)}
        );

        db.close();
    }

    //getCL
    @SuppressLint("Range")
    public String GetCLHoVaTenKhachHang(String cmnd){
        String hoVaTen = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Query để lấy chức vụ từ cơ sở dữ liệu
        String query = "SELECT " + CreateDatabase. CL_TEN_KHACH_HANG  + " FROM " + CreateDatabase.TB_DANG_NHAP_KHACH_HANG +
                " WHERE " + CreateDatabase.CL_CMND + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cmnd});

        // Kiểm tra xem có dữ liệu hay không
        if (cursor.moveToFirst()) {
            hoVaTen = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_KHACH_HANG ));
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return hoVaTen;
    }
    //Get Column Email
    @SuppressLint("Range")
    public String GetCLEmailKhachHang(String cmnd){
        String email = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Query để lấy email từ khách hàng
        String query = "SELECT " + CreateDatabase. CL_EMAIL  + " FROM " + CreateDatabase.TB_DANG_NHAP_KHACH_HANG +
                " WHERE " + CreateDatabase.CL_CMND + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cmnd});

        // Kiểm tra xem có dữ liệu hay không
        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_EMAIL ));
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return email;
    }
    //Get Column SDT
    @SuppressLint("Range")
    public String GetCLSDTKhachHang(String cmnd){
        String soDienThoai = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Query để lấy Số điện thoại từ bảng khách hàng
        String query = "SELECT " + CreateDatabase. CL_SO_DIEN_THOAI  + " FROM " + CreateDatabase.TB_DANG_NHAP_KHACH_HANG +
                " WHERE " + CreateDatabase.CL_CMND + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cmnd});

        // Kiểm tra xem có dữ liệu hay không
        if (cursor.moveToFirst()) {
            soDienThoai = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_SO_DIEN_THOAI ));
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return soDienThoai;
    }
    //Get Column Dia Ch
    @SuppressLint("Range")
    public String GetCLDiaChiKhachHang(String cmnd){
        String diaChi = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Query để lấy Dịa Chỉ Từ bảng Khách Hàng
        String query = "SELECT " + CreateDatabase. CL_DIA_CHI  + " FROM " + CreateDatabase.TB_DANG_NHAP_KHACH_HANG +
                " WHERE " + CreateDatabase.CL_CMND + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cmnd});

        // Kiểm tra xem có dữ liệu hay không
        if (cursor.moveToFirst()) {
            diaChi = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_DIA_CHI ));
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return diaChi;
    }
    //Get CLNgaySinh
    @SuppressLint("Range")
    public String GetCLNgaySinh(String cmnd){
        String birthDay = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Query để lấy Dịa Chỉ Từ bảng Khách Hàng
        String query = "SELECT " + CreateDatabase. CL_NGAYSINH  + " FROM " + CreateDatabase.TB_DANG_NHAP_KHACH_HANG +
                " WHERE " + CreateDatabase.CL_CMND + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cmnd});

        // Kiểm tra xem có dữ liệu hay không
        if (cursor.moveToFirst()) {
            birthDay = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_NGAYSINH ));
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return birthDay;
    }

    //Get CMND

    @SuppressLint("Range")
    public String GetCLCMND(String cmnd){
        String birthDay = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Query để lấy Dịa Chỉ Từ bảng Khách Hàng
        String query = "SELECT " + CreateDatabase. CL_CMND  +
                " FROM " + CreateDatabase.TB_DANG_NHAP_KHACH_HANG +
                " WHERE " + CreateDatabase.CL_CMND + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cmnd});

        // Kiểm tra xem có dữ liệu hay không
        if (cursor.moveToFirst()) {
            birthDay = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_CMND ));
        }

        // Đóng cursor và database
        cursor.close();
        db.close();

        return birthDay;
    }
    //Get id Sản Phẩm

    @SuppressLint("Range")
    public String GetIdSanPham(String name){
        String idSanPham = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //Query để lấy dữ liệu từ sản phẩm
        String query = "SELECT " + CL_SAN_PHAM_ID +
                " FROM " + TB_SAN_PHAM +
                " WHERE " + CreateDatabase.CL_TEN_SAN_PHAM + " = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query,new String[]{name});

        //Kiểm tra xem có dữ liệu hay không
        if(cursor.moveToFirst()){
            idSanPham = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_SAN_PHAM_ID));
        }
        // Đóng cursor và database
        cursor.close();

        return idSanPham;
    }
    public boolean isFavorite(String tenSanPham, String idSanPham) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean isFavorite = false;

        try {
            String query = "SELECT * FROM " + TB_YEU_THICH +
                    " WHERE " + CL_TEN_KHACH_HANG_YEU_THICH + " = ?" +
                    " AND " + CL_SAN_PHAM_YEU_THICH_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{tenSanPham, idSanPham});

            if (cursor != null && cursor.moveToFirst()) {
                isFavorite = true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return isFavorite;
    }

}


