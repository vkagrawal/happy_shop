package com.app.happyshop.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.happyshop.R;
import com.app.happyshop.model.Product;
import com.loopj.android.image.SmartImageView;

import java.util.List;


public class ProductsAdapter extends BaseRecyclerViewAdapter<Product, ProductsAdapter.ArtifactHolder> {

    Context context;

    public ProductsAdapter(List<Product> itemList, Context context) {
        super(itemList, context);
        this.context = context;
    }

    @Override
    protected ArtifactHolder getHolderView(View itemView) {
        return new ArtifactHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArtifactHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Product item = itemList.get(position);
        holder.productName.setText(item.getName());
        holder.productPrice.setText(item.getPrice());
        holder.productOnSale.setVisibility(item.isUnderSale() ? View.VISIBLE : View.INVISIBLE);
        holder.productImage.setImageUrl(item.getImgUrl());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ArtifactHolder extends BaseRecyclerViewAdapter.BaseRecyclerViewHolder {
        TextView productName, productPrice, productOnSale;
        SmartImageView productImage;

        public ArtifactHolder(View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.productName);
            productPrice = (TextView) view.findViewById(R.id.productPrice);
            productOnSale = (TextView) view.findViewById(R.id.productOnSale);
            productImage = (SmartImageView) view.findViewById(R.id.productImage);
        }
    }

}
