package com.example.banhang.Login;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.banhang.R;
import com.example.banhang.database.CreateDatabase;
import com.example.banhang.View.HeaderMenu.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KhungDangKyActivity extends AppCompatActivity {
    //Khai Báo Các Biên Giao Diện
    EditText edtTenDangNhap,edtMatKhau,edtNhapLaiMatKhau,edtNgaySinh,edtCMND;
    Button btDongY,btThoat;
    RadioButton rdoNam,rdoNu;
    RadioGroup rdogrGioiTinh;
    CreateDatabase databaseHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangky);
            databaseHelper = new CreateDatabase(this);
            db = databaseHelper.getWritableDatabase();
            AnhXa();
            btDongY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String TenDangNhapDK = edtTenDangNhap.getText().toString();
                    String MatKhauDK = edtMatKhau.getText().toString();
                    String NhapLaiMatKhauDK = edtNhapLaiMatKhau.getText().toString();
                    String NgaySinhDK = edtNgaySinh.getText().toString();
                    String CMNDK = edtCMND.getText().toString();
                    int selectedRadioButtonId = rdogrGioiTinh.getCheckedRadioButtonId();
                    String gioiTinh;
                    if (selectedRadioButtonId != -1) {
                        // Tìm RadioButton dựa trên ID
                        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

                        // Lấy văn bản của RadioButton
                        gioiTinh = selectedRadioButton.getText().toString();

                    } else {
                        gioiTinh = "";
                    }
                    if(TenDangNhapDK.equals("")||TenDangNhapDK.length()  < 5){
                        edtTenDangNhap.setError("Ten Dang Nhap Dang Rong Va Phai Lon Hon 4 Ký Tự");
                    } else if (!isPasswordValid(MatKhauDK)) {
                        edtMatKhau.setError("Mật Khẩu phải hơn 8 ký tự , có chữ hoa và số ");

                    } else if (NhapLaiMatKhauDK.equals("")) {
                        edtNhapLaiMatKhau.setError("Bạn Chưa Nhập Lại Mật Khẩu");

                    } else if (!NhapLaiMatKhauDK.equals(edtMatKhau.getText().toString())) {
                        edtNhapLaiMatKhau.setError("Mật Khẩu Bạn Nhập Không Trùng Khớp");
                    } else if (rdogrGioiTinh.getCheckedRadioButtonId() == -1) {
                        Toast toast = Toast.makeText(getApplicationContext(),"Hãy Chọn Giới Tính Của Bạn",Toast.LENGTH_SHORT);toast.show();
                    } else if(!isValidDateOfBirth(NgaySinhDK)){
                        edtNgaySinh.setError("bạn Chưa Nhập Ngày Sinh");
                    }else if(!isValidCMND(CMNDK) ){
                        Toast toast = Toast.makeText(getApplicationContext(),"CMND không hợp lệ",Toast.LENGTH_SHORT);toast.show();
                    }else {
                        try {
                            DangKyNguoiDung(TenDangNhapDK,MatKhauDK,NgaySinhDK,CMNDK,gioiTinh,edtCMND,edtTenDangNhap);
                            Intent intent = new Intent(KhungDangKyActivity.this, KhungDangNhapActivity.class);
                            // Lưu dữ liệu
                            SharedPreferences.Editor editor = getSharedPreferences("ShareData", MODE_PRIVATE).edit();
                            editor.putString("ngaySinh", NgaySinhDK);
                            editor.putString("cmnd", CMNDK);
                            editor.putString("hovaten",TenDangNhapDK);
                            editor.apply();
                            startActivity(intent);
                        }catch (Exception e){
                            Toast toast = Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);toast.show();
                        }

                    }

                }
            });
            findViewById(R.id.btThoat).setOnClickListener(v -> {
                Intent intent  = new Intent(KhungDangKyActivity.this,KhungDangNhapActivity.class);
                startActivity(intent);
            });

    }
    public  void AnhXa(){
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtNhapLaiMatKhau = findViewById(R.id.edtNhapLaiMatKhau);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        edtCMND = findViewById(R.id.edtCMND);
        btDongY = findViewById(R.id.btDongY);
        btThoat = findViewById(R.id.btThoat);
        rdoNam = findViewById(R.id.rdoNam);
        rdoNu = findViewById(R.id.rdoNu);
        rdogrGioiTinh = findViewById(R.id.rdogrGioiTinh);
    }
    //Method
    private void DangKyNguoiDung(String tenDangNhap, String matKhau,String ngaysinh,String cmnd,String gioitinh, EditText eErorrCmnd,EditText eErorrName) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        /*
        ContentValues ContentValues là một lớp trong Android được sử dụng để đại diện cho một bộ giá trị, nơi mà mỗi giá trị được liên kết với một tên cột. Nó thường được sử dụng để chuẩn bị dữ liệu để chèn hoặc cập nhật vào cơ sở dữ liệu.

        +Một số phương thức quan trọng của lớp ContentValues bao gồm:

        +put(String key, String value): Thêm một cặp khóa/giá trị vào ContentValues. Loại giá trị được xác định bởi kiểu dữ liệu của giá trị được chuyển vào.

        +putAll(ContentValues other): Thêm tất cả các cặp khóa/giá trị từ một đối tượng ContentValues khác vào đối tượng hiện tại.

        +remove(String key): Loại bỏ cặp khóa/giá trị với khóa được chỉ định từ ContentValues.

        +Các phương thức put khác cho các kiểu dữ liệu khác nhau như put(String key, Integer value), put(String key, Long value), put(String key, Double value), vv.
        */
        try {
            if(!isTaiKhoanTonTai(tenDangNhap, cmnd) && db != null && db.isOpen()){
                db.beginTransaction();

                // Thêm dữ liệu vào bảng TB_DANG_NHAP_KHACH_HANG
                ContentValues valuesDangNhap = new ContentValues();
                valuesDangNhap.put(CreateDatabase.CL_TEN_DANGNHAP, tenDangNhap);
                valuesDangNhap.put(CreateDatabase.CL_MAT_KHAU, matKhau);
                valuesDangNhap.put(CreateDatabase.CL_NGAYSINH, ngaysinh);
                valuesDangNhap.put(CreateDatabase.CL_CMND, cmnd);
                valuesDangNhap.put(CreateDatabase.CL_GIOITINH, gioitinh);
                db.insert(CreateDatabase.TB_DANG_NHAP_KHACH_HANG, null, valuesDangNhap);
                db.setTransactionSuccessful();
                Toast toast = Toast.makeText(getApplicationContext(), "Đăng Ký Thành Công Nhé ~~", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "Tên Đăng Nhặp hoặc CMND đã tồn tại", Toast.LENGTH_SHORT);
                eErorrName.setError("Tên Đăng Nhặp hoặc CMND đã tồn tại");
                eErorrCmnd.setError("Tên Đăng Nhặp hoặc CMND đã tồn tại");
                toast.show();
            }

        } catch (Exception e) {
            Log.e("DangKyNguoiDung", "Error during registration", e);
        } finally {
            if(db != null && db.isOpen()){
                db.endTransaction();
                db.close();
            }
        }

    }
    // Phương thức kiểm tra xem tài khoản hoặc số điện thoại đã tồn tại chưa
    private boolean isTaiKhoanTonTai(String tenDangNhap, String cmnd) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + CreateDatabase.TB_DANG_NHAP_KHACH_HANG +
                " WHERE " + CreateDatabase.CL_TEN_DANGNHAP + "=? OR " + CreateDatabase.CL_CMND + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{tenDangNhap, cmnd});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đảm bảo kiểm tra null trước khi đóng
        if (db != null && db.isOpen()) {
            db.close();
        }

        // Đóng đối tượng SQLiteDatabase khi không cần sử dụng nữa
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
    public static boolean isValidCMND(String cmnd) {
        String cmndRegex = "^[0-9]{12}$";
        Pattern pattern = Pattern.compile(cmndRegex);
        Matcher matcher = pattern.matcher(cmnd);
        return matcher.matches();
    }
    public static boolean isPasswordValid(String password) {
        // Ít nhất 8 ký tự
        // Ít nhất một chữ cái viết hoa
        // Ít nhất một chữ cái viết thường
        // Ít nhất một số
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

        // Kiểm tra mật khẩu với biểu thức chính quy
        return password.matches(passwordRegex);
    }
    public static boolean isValidDateOfBirth(String dateOfBirth) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            Date parsedDate = sdf.parse(dateOfBirth);

            if (!dateOfBirth.equals(sdf.format(parsedDate))) {
                return false;
            }

            Date currentDate = new Date();
            if (parsedDate.after(currentDate)) {
                return false;
            }

        } catch (ParseException e) {
            return false;
        }

        return true;
    }


}