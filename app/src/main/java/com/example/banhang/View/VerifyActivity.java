package com.example.banhang.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewProduct.Utils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyActivity extends AppCompatActivity {
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    ProgressBar progressBar;
    TextView tvGuiLaiOTp, tvThongBao;
    Button btnXacNhan;
    EditText edtNhapOTP;
    ImageView imgAnhVerify;
    Long time = 60L;
    String trangThai = null;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this); // Khởi tạo FirebaseApp
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_verify);

        AnhXa();

        SharedPreferences sharedPreferences = getSharedPreferences("ThongTinNguoiDung", Context.MODE_PRIVATE);
        String soDienThoai = sharedPreferences.getString("soDienThoai", "");
        sendOtp(soDienThoai, false);
    }

    void AnhXa() {
        tvGuiLaiOTp = findViewById(R.id.tvGuilaiOTP);
        tvThongBao = findViewById(R.id.tvThongbao_Verify);
        btnXacNhan = findViewById(R.id.btnXacNhan_Verify);
        edtNhapOTP = findViewById(R.id.edtNhapOTP_Verify);
        imgAnhVerify = findViewById(R.id.imgAnhVerify);
        progressBar = findViewById(R.id.progressbar_Verify);
    }

    void sendOtp(String soDienThoai, boolean isResend) {
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(soDienThoai)
                        .setTimeout(time, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                verify(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Utils.ThongBao(getApplicationContext(), "Xảy ra lỗi khi gửi OTP");
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                Utils.ThongBao(getApplicationContext(), "Đã gửi lại OTP");
                                setInProgress(false);
                            }
                        });

        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    private void verify(PhoneAuthCredential phoneAuthCredential) {
        trangThai = "ok";
        SharedPreferences sharedPreferences = getSharedPreferences("TrangThai", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("flag", trangThai);
        editor.apply();
        Utils.ThongBao(getApplicationContext(), "Xác thực thành công");
        Intent i = new Intent(VerifyActivity.this, HomeActivity.class);
        startActivity(i);
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            btnXacNhan.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnXacNhan.setVisibility(View.VISIBLE);
        }
    }
}
