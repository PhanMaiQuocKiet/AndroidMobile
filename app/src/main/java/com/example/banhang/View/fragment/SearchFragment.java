package com.example.banhang.View.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.banhang.R;
import com.example.banhang.View.RecycleViewSearch.SearchAdapter;
import com.example.banhang.View.RecyclerViewProduct.ProductAdapter;
import com.example.banhang.View.RecyclerViewProduct.Products;
import com.example.banhang.View.RecycleViewSearch.SearchAdapter;
import com.example.banhang.View.RecyclerViewProduct.Utils;
import com.example.banhang.database.CreateDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    SearchView searchView;
    SearchAdapter searchAdapter;
    ArrayList<Products> listProducts;
    RecyclerView rvListProduct,rcvSearch;
    CreateDatabase databaseHelper;
    ProductAdapter productAdapter;


    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        AnhXa(view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LoadDataProductsByName(getContext(), query);
                searchAdapter = new SearchAdapter(listProducts, databaseHelper);
                rcvSearch.setAdapter(searchAdapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                rcvSearch.setLayoutManager(gridLayoutManager);
                rcvSearch.setVisibility(View.VISIBLE);
                rvListProduct.setVisibility(View.GONE);
                return true; // Trả về true nếu sự kiện được xử lý
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    // Nếu SearchView trống, hiển thị lại danh sách sản phẩm ban đầu
                    rcvSearch.setVisibility(View.GONE);
                    rvListProduct.setVisibility(View.VISIBLE);
                    LoadDataProducts(getContext());
                    productAdapter.notifyDataSetChanged();
                } else {
                    // Nếu có dữ liệu trong SearchView, thực hiện tìm kiếm và hiển thị kết quả
                    LoadDataProductsByName(getContext(), newText);
                    // Khởi tạo SearchAdapter và gắn nó với RecyclerView
                    searchAdapter = new SearchAdapter(listProducts, databaseHelper);
                    rcvSearch.setAdapter(searchAdapter);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                    rcvSearch.setLayoutManager(gridLayoutManager);
                    rcvSearch.setVisibility(View.VISIBLE);
                    rvListProduct.setVisibility(View.GONE);
                }
                return  false;
            }
        });
        return view;
    }
    void AnhXa(View view){
        rvListProduct = view.findViewById(R.id.rvListProduct);

        rcvSearch=view.findViewById(R.id.rcv_search);
        searchView = view.findViewById(R.id.search_user);
    }
    void LoadDataProductsByName(Context context, String productName){
        // Xóa dữ liệu hiện có của listProducts
        listProducts.clear();

        // Mở kết nối đến cơ sở dữ liệu
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Câu truy vấn SQL để lấy danh sách sản phẩm dựa trên tên sản phẩm
        String query = "SELECT * FROM " + CreateDatabase.TB_SAN_PHAM +
                " WHERE " + CreateDatabase.CL_TEN_SAN_PHAM + " LIKE '%" + productName + "%'";

        // Thực thi câu truy vấn
        Cursor cursor = db.rawQuery(query, null);

        // Duyệt qua các dòng kết quả và thêm vào danh sách sản phẩm
        if(cursor != null && cursor.getCount() > 0){
            while(cursor.moveToNext()){
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_TEN_SAN_PHAM));
                @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_GIA_BAN));
                @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(CreateDatabase.CL_ANH_SAN_PHAM));
                // Tạo đối tượng sản phẩm và thêm vào danh sách
                Products product = new Products(context, name, price, image);
                listProducts.add(product);
            }
            // Đóng con trỏ sau khi sử dụng
            cursor.close();
        }
        // Đóng kết nối đến cơ sở dữ liệu
        db.close();

        // Cập nhật RecyclerView sau khi tìm kiếm
        if(searchAdapter != null) {
            searchAdapter.notifyDataSetChanged();
        }
    }
    void LoadDataProducts(Context context){
        listProducts = Utils.LoadDaTaProducts(context);
    }

}