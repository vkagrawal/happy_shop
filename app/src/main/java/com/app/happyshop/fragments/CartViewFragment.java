package com.app.happyshop.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.happyshop.MainActivity;
import com.app.happyshop.R;
import com.app.happyshop.adapters.ProductsAdapter;
import com.app.happyshop.apicalls.ApiNames;
import com.app.happyshop.apicalls.ApiRequest;
import com.app.happyshop.apicalls.ServiceHandler;
import com.app.happyshop.base.App;
import com.app.happyshop.base.BaseFragmentWithApi;
import com.app.happyshop.model.Product;
import com.app.happyshop.recycler.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartViewFragment extends ListBaseClass {

    public CartViewFragment() {
    }

    public static CartViewFragment create() {
        final Bundle args = new Bundle();
        args.putInt(EXTRA_DATA_LAYOUT, R.layout.category_view_fragment);
        CartViewFragment f = new CartViewFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCallback.isShowBackHome(false);
        mCallback.setCurrentFrag(CartViewFragment.this);
        mCallback.setActionBarTitle("Add Items");
        productArrayList.clear();
        new RetriveProducts().execute();
    }

    @Override
    public void initOnCreateView(View v) {
        super.initOnCreateView(v);
        productsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener
                (getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (mActivity instanceof MainActivity) {
                            ((MainActivity) mActivity).pushFragments(MainActivity.CART,
                                    CategoryDetailsFragment.create(productArrayList.get(position).getId()), true, true);
                        }
                    }
                }));
    }


    class RetriveProducts extends AsyncTask<Void, Void, ArrayList<Product>> {

        @Override
        protected ArrayList<Product> doInBackground(Void... voids) {
            try {
                productArrayList = (ArrayList<Product>) Product.getInstance().findAllData();
                return productArrayList;
            } catch (Exception e) {
                e.printStackTrace();
                App.printToast(R.string.error_to_retrive);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> productArrayList) {
            super.onPostExecute(productArrayList);
            if (productArrayList != null && productArrayList.size() > 0) {
                productsAdapter = new ProductsAdapter(productArrayList, getActivity());
                productsRecyclerView.setAdapter(productsAdapter);
            }
            checkIfItmeNotAvailable();
        }
    }
}
