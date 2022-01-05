package com.example.ecommerceapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.Interface.ItemClickListener;
import com.example.ecommerceapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductPrice, txtProductDescription;
    public ImageView imageView;
    private ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
    }

    public void  setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
