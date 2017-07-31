package com.app.happyshop.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.happyshop.MainActivity;
import com.app.happyshop.R;
import com.app.happyshop.adapters.ProductsAdapter;
import com.app.happyshop.apicalls.ApiNames;
import com.app.happyshop.base.BaseFragmentWithApi;
import com.app.happyshop.model.Product;
import com.app.happyshop.recycler.RecyclerItemClickListener;

import org.json.JSONObject;

import java.util.ArrayList;

abstract public class ListBaseClass extends BaseFragmentWithApi {

    protected ArrayList<Product> productArrayList = new ArrayList<>();
    protected RecyclerView productsRecyclerView;
    protected ProductsAdapter productsAdapter;
    protected GridLayoutManager mLayoutManager;
    protected TextView noItem;
    ProgressBar loadMoreBar;

    @Override
    public void initOnCreateView(View v) {
        noItem = (TextView) v.findViewById(R.id.noItem);
        loadMoreBar = (ProgressBar) v.findViewById(R.id.loadMoreBar);
        productsRecyclerView = (RecyclerView) v.findViewById(R.id.rList);
        productsAdapter = new ProductsAdapter(productArrayList, getActivity());
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        productsRecyclerView.setLayoutManager(mLayoutManager);
        productsRecyclerView.setItemAnimator(null);
        // productsRecyclerView.getItemAnimator().setAddDuration(0);
        productsRecyclerView.setAdapter(productsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkIfItmeNotAvailable();
    }


    protected void isShowProgressBar(boolean isLoading) {
        loadMoreBar.setVisibility(isLoading?View.VISIBLE:View.GONE);
    }

    protected void checkIfItmeNotAvailable() {
        if (productArrayList == null || productArrayList.size() == 0) {
            noItem.setVisibility(View.VISIBLE);
            productsRecyclerView.setVisibility(View.GONE);
        } else {
            noItem.setVisibility(View.GONE);
            productsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void notifyNetworkStateChange(boolean isNetworkAvailable) {

    }


    @Override
    public void errorResponseHandler(JSONObject jsonObject, ApiNames serviceTaskType) {

    }

    @Override
    public void responseHandler(JSONObject jsonObject, ApiNames serviceTaskType) {

    }
}
