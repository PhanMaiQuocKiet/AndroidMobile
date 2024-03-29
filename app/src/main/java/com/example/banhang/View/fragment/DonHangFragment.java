package com.example.banhang.View.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.banhang.R;
import com.example.banhang.View.RecycleViewDonHang.*;
import com.example.banhang.database.CreateDatabase;
import com.example.banhang.View.RecyclerViewProduct.*;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DonHangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonHangFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DonHangFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DonHangFragment newInstance(String param1, String param2) {
        DonHangFragment fragment = new DonHangFragment();
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
    DonHangAdapter donHangAdapter ;
    ArrayList<DonHang> listDonHang;
    RecyclerView rcvDonHang;
    CreateDatabase databaseHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donhang, container, false);
        AnhXa(view);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("tk_mk login", Context.MODE_PRIVATE);
        String tenDangNhap =  sharedPreferences.getString("Username",null);
        LoadData(getContext(),tenDangNhap);
        donHangAdapter = new DonHangAdapter(listDonHang);

        //set kieu de xuat view theo dnag linear
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //set adapter cho rcv
        rcvDonHang.setAdapter(donHangAdapter);
        rcvDonHang.setLayoutManager(linearLayoutManager);
        return view;
    }
    void AnhXa(View view){
        rcvDonHang = view.findViewById(R.id.rcvDonHang);
    }
    void LoadData(Context context , String tenNguoiDung){
        listDonHang = Utils.LoadDataOrder(context,tenNguoiDung);
    }
}