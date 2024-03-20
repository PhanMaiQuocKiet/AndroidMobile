package com.example.banhang.View.RecycleViewSearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banhang.View.RecycleViewDonHang.DonHang;
import com.example.banhang.R;
import com.example.banhang.View.RecyclerViewProduct.ProductAdapter;
import com.example.banhang.View.RecyclerViewProduct.Products;
import com.example.banhang.View.RecyclerViewProduct.Utils;
import com.example.banhang.database.CreateDatabase;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    ArrayList<Products> listProduct;
    Context context;
    CreateDatabase createDatabase;
    public SearchAdapter(ArrayList<Products> List,CreateDatabase createDatabase){
        this.listProduct=List;
        this.createDatabase=createDatabase;
    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //Nạp layout cho view biểu diễn phần từ User
        View userView = inflater.inflate(R.layout.layout_item_search,parent,false);
        //
        SearchAdapter.SearchViewHolder viewHolder = new    SearchAdapter.SearchViewHolder(userView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            Products item=listProduct.get(position);

            holder.tvName.setText(item.getName());
            holder.tvGia.setText(item.getPrice());
            holder.imgAnhSanPham.setImageBitmap(Utils.convertToBitmapFromAssets(context,item.getImage()));
            if(createDatabase.isFavorite(item.getName(),item.getId())){

               holder.cbYeuThich.setChecked(true);
        }

    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }
    static class SearchViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAnhSanPham;
        TextView tvName,tvGia;
        CheckBox cbYeuThich;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnhSanPham=itemView.findViewById(R.id.imgAnhSanPham_Search);
            tvGia=itemView.findViewById(R.id.tvGiaSanPham_Search);
            tvName=itemView.findViewById(R.id.tvTenProducts_Search);
            cbYeuThich=itemView.findViewById(R.id.cbYeuThich_Search);

        }
    }
}
