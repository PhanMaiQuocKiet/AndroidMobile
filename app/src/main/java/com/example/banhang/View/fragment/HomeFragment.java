package com.example.banhang.View.fragment;


import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.example.banhang.View.RecycleViewGioHang.*;
import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewCategory.CategoryAdapter;
import com.example.banhang.View.RecyclerViewCategory.ProductsCategory;
import com.example.banhang.View.*;
import com.example.banhang.database.CreateDatabase;
import com.example.banhang.View.RecyclerViewProduct.*;
import java.util.ArrayList;
import com.example.banhang.View.RecycleViewDonHangTheoTheLoai.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */

public class HomeFragment extends Fragment implements ProductAdapter.UserCallBack,ProductsInCategoryAdapter.UserCallBack{
    CreateDatabase databaseHelper;
    RecyclerView rvListProduct,rvListProductCategory;
    ArrayList<Products> listProducts,listProductsCategory;
     ProductAdapter productAdapter;
     ProductAdapterAdmin productAdapterAdmin;
     ImageView imgCart;
     TextView tvCartItemCount;
     SwipeRefreshLayout swipeRefreshLayout;
    ProductsInCategoryAdapter productsInCategoryAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    ViewFlipper viewFlipper;
    ArrayList<Products> lstProductCart = new ArrayList<>();
    private int currentImage = 0;
    private static final int FLIP_INTERVAL = 3000;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        AnhXa(view);

        databaseHelper = new CreateDatabase(getActivity());

        // Lấy số lượng sản phẩm trong giỏ hàng từ SharedPreferences
        SharedPreferences cartPreferences = getContext().getSharedPreferences("DuLieu", MODE_PRIVATE);
        String cartItemCount = cartPreferences.getString("soLuong", null);
        lstProductCart = Utils.LoadDaTaProductsCart(getContext());
            // Tính tổng số lượng sản phẩm trong giỏ hàng
        int totalQuantity = 0;
        for (Products product : lstProductCart) {
            // Kiểm tra sản phẩm đã được chọn trong giỏ hàng
            if (product  != null ) {
                // Tăng số lượng sản phẩm trong giỏ hàng
                totalQuantity += 1; // Hoặc sử dụng một số thuộc tính khác để lấy số lượng, phụ thuộc vào cách bạn đã cài đặt nó trong lớp Products
            }
        }

        // Tìm và cập nhật TextView
        if (totalQuantity > 0) {
            tvCartItemCount.setVisibility(View.VISIBLE);
            tvCartItemCount.setText(String.valueOf(totalQuantity));
        } else {
            tvCartItemCount.setVisibility(View.GONE);
        }
        String thongtinluu = "tk_mk login";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(thongtinluu, MODE_PRIVATE);
        String tenDangNhap =  sharedPreferences.getString("Username","");

        String role  = databaseHelper.GetCLVaitro(tenDangNhap);
        if(role.equals("admin")){
            Toast.makeText(getContext(),"Product Admin",Toast.LENGTH_SHORT).show();

            LoadDataProducts(getActivity());
            productAdapterAdmin = new ProductAdapterAdmin(listProducts,databaseHelper);
            rvListProduct.setAdapter(productAdapterAdmin);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            rvListProduct.setLayoutManager(gridLayoutManager);
        }
        else {
            // Load dữ liệu sản phẩm cho người dùng thường
            // Load danh sách sản phẩm trong một danh mục cụ thể
            LoadDataProductsInCategory(getActivity(), "1"); // Thay "1" bằng mã danh mục cụ thể mong muốn
            productsInCategoryAdapter = new ProductsInCategoryAdapter(listProductsCategory, databaseHelper,this);
            rvListProductCategory.setAdapter(productsInCategoryAdapter);
            GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2);
            rvListProductCategory.setLayoutManager(gridLayoutManager2);

            // Load tất cả sản phẩm cho RecyclerView chính
            LoadDataProducts(getActivity());
            productAdapter = new ProductAdapter(listProducts, databaseHelper, this);
            rvListProduct.setAdapter(productAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            rvListProduct.setLayoutManager(gridLayoutManager);
        }
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),CartActivity.class);
                startActivity(i);
            }
        });
        return view;


    }
    // Phương thức reload lại trang
    public void reloadFragment() {
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        if (currentFragment instanceof HomeFragment) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
            swipeRefreshLayout.setRefreshing(false); // Dừng hiệu ứng refresh
        }
    }
    void AnhXa(View view){
        rvListProduct = view.findViewById(R.id.rvListProduct);
        viewFlipper = view.findViewById(R.id.viewlipper);
        imgCart = view.findViewById(R.id.imgCart);
        tvCartItemCount = view.findViewById(R.id.tvCartItemCount);
        rvListProductCategory = view.findViewById(R.id.rvListProductCategory);
    }
    void LoadDataProducts(Context context){
        listProducts = Utils.LoadDaTaProducts(context);
    }
    @SuppressLint("HandlerLeak")
    private final Handler flipHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (currentImage < viewFlipper.getChildCount() - 1) {
                    currentImage++;
                } else {
                    currentImage = 0;
                }
                viewFlipper.setDisplayedChild(currentImage);
                sendEmptyMessageDelayed(0, FLIP_INTERVAL);
            }
        }
    };
    void LoadDataProductsInCategory(Context context, String categoryId){
        listProductsCategory = Utils.LoadDataProductInCategory(context, categoryId);
    }

    @Override
    public void onItemClick(String tenSanPham, String GiaTien, String moTa, String srcAnh) {
        Intent i = new Intent(getActivity(), DetailActivity.class);
        i.putExtra("tenSanPham", tenSanPham);
        i.putExtra("giaTien", GiaTien);
        i.putExtra("mota", moTa);
        i.putExtra("srcAnh", srcAnh);
        startActivity(i);
    }

}
