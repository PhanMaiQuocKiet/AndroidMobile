package com.example.banhang.View.RecycleViewDonHangTheoTheLoai;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang.View.RecycleViewDonHang.DonHang;
import com.example.banhang.View.RecyclerViewProduct.*;
import com.example.banhang.R;
import com.example.banhang.database.*;

import java.util.ArrayList;

public class ProductsInCategoryAdapter extends RecyclerView.Adapter<ProductsInCategoryAdapter.ProductsInCategoryViewHolder>{
    ArrayList<Products> lstProducts ;
    Context context;
    CreateDatabase databaseHelper;
   UserCallBack userCallBack;
    public ProductsInCategoryAdapter(ArrayList<Products> lstProducts, CreateDatabase databaseHelper, UserCallBack userCallBack){
        this.lstProducts = lstProducts;
        this.databaseHelper =databaseHelper;
        this.userCallBack = userCallBack;

    }
    @NonNull
    @Override
    public ProductsInCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_item_product_category,parent,false);
        ProductsInCategoryAdapter.ProductsInCategoryViewHolder viewHolder = new  ProductsInCategoryAdapter.ProductsInCategoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsInCategoryViewHolder holder, int position) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        //Lấy từng item của dữ liệu
        Products item = lstProducts.get(position);

        //gán vào item của view
        holder.tvName.setText(item.getName());
        holder.tvGia.setText(item.getPrice() + " VND");
        holder.imgAnhSanPham.setImageBitmap(Utils.convertToBitmapFromAssets(context,item.getImage()));
        // Set trạng thái của checkbox
        holder.cbYeuThich.setChecked(createDatabase.isFavorite(item.getName(),createDatabase.GetIdSanPham(item.getName())));
        //Tạo sự kiện cho nút tim
        holder.cbYeuThich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String tenSanPham = item.getName();
                String idSanPham = createDatabase.GetIdSanPham(tenSanPham);
                if(isChecked){
                    ThemSanPhamYeuThich(tenSanPham,idSanPham,context);
                }
                else {
                    XoaSanPhamYeuThich(tenSanPham,idSanPham,context);
                }
            }
        });
        //Lấy sự kiện
        holder.itemView.setOnClickListener(view -> userCallBack.onItemClick(item.getName(),item.getPrice(),item.getDes(),item.getImage()));

    }

    @Override
    public int getItemCount() {
        return lstProducts.size();
    }

    static class ProductsInCategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAnhSanPham;
        TextView tvName,tvGia;
        CheckBox cbYeuThich;
        public ProductsInCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTenProducts_Category);
            tvGia = itemView.findViewById(R.id.tvGiaSanPham_Category);
            cbYeuThich = itemView.findViewById(R.id.cbYeuThich_Category);
            imgAnhSanPham = itemView.findViewById(R.id.imgAnhSanPham_Category);
        }
    }
    public void ThemSanPhamYeuThich(String tenSanPham,String idSanPham,Context context){
        CreateDatabase createDatabase = new CreateDatabase(context);

        SQLiteDatabase sqLiteDatabase = createDatabase.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CreateDatabase.CL_TEN_KHACH_HANG_YEU_THICH,tenSanPham);
        cv.put(CreateDatabase.CL_SAN_PHAM_YEU_THICH_ID,idSanPham);

        sqLiteDatabase.insert(CreateDatabase.TB_YEU_THICH,null,cv);
        Toast.makeText(context, "Sản phẩm đã được thêm vao danh sách yêu thích", Toast.LENGTH_SHORT).show();

        sqLiteDatabase.close();

    }
    public void XoaSanPhamYeuThich(String tenSanPham, String idSanPham, Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);

        SQLiteDatabase sqLiteDatabase = createDatabase.getWritableDatabase();

        // Xác định điều kiện xóa - dựa trên tên sản phẩm và ID sản phẩm
        String selection = CreateDatabase.CL_TEN_KHACH_HANG_YEU_THICH + "=? AND " +
                CreateDatabase.CL_SAN_PHAM_YEU_THICH_ID + "=?";
        String[] selectionArgs = {tenSanPham, idSanPham};

        // Thực hiện xóa
        int deletedRows = sqLiteDatabase.delete(CreateDatabase.TB_YEU_THICH, selection, selectionArgs);

        if (deletedRows > 0) {
            Toast.makeText(context, "Sản phẩm đã được xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Không thể xóa sản phẩm khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
        }
        sqLiteDatabase.close();
    }
    public interface UserCallBack{
        void onItemClick(String tenSanPham , String GiaTien, String moTa , String srcAnh);
    }
}
