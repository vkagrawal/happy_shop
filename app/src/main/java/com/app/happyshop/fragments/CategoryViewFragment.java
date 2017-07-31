package com.app.happyshop.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Transition;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class CategoryViewFragment extends ListBaseClass {

    protected int page = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    String category = "Category View";

    private String title;

    public CategoryViewFragment() {
    }

    public static CategoryViewFragment create(String category) {
        final Bundle args = new Bundle();
        args.putInt(EXTRA_DATA_LAYOUT, R.layout.category_view_fragment);
        args.putString(EXTRA_DATA_TAG, category);
        CategoryViewFragment f = new CategoryViewFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCallback.setCurrentFrag(CategoryViewFragment.this);
        mCallback.setActionBarTitle(getTitle());
    }

    @Override
    public void initOnCreateView(View v) {
        super.initOnCreateView(v);
        productsRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        productsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener
                (getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        try {
                            if (mActivity instanceof MainActivity) {
                                ((MainActivity) mActivity).pushFragments(MainActivity.CATEGORIES,
                                        CategoryDetailsFragment.create(productArrayList.get(position).getId()), true, true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
        Bundle bundle = getArguments();
        if (bundle.containsKey(EXTRA_DATA_TAG)) {
            category = bundle.getString(EXTRA_DATA_TAG);
            if (category != null && category.length() > 0) {
                callApi(getItemDetails(true), getCategoryRequest(category, ("" + page)));
            }
        }
    }

    private ApiRequest getItemDetails(boolean isShowProgressBar) {
        ApiRequest categoryRequest =
                new ApiRequest.Builder("http://sephora-mobile-takehome-apple.herokuapp.com/api/v1/products.json", ApiNames.CategoryView)
                        .setMethod(ServiceHandler.RequestMethod.GET)
                        .setAuthunticate(false)
                        .setShowProgressBar(isShowProgressBar)
                        .setJsonRequest(true)
                        .setTakeOnlyPath(true)
                        .build();
        return categoryRequest;
    }

    private Map<String, String> getCategoryRequest(String category, String pageNo) {
        Map<String, String> params = new HashMap<>();
        params.put("category", category);
        params.put("page", pageNo);
        return params;
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastvisibleitemposition = mLayoutManager.findLastVisibleItemPosition();
            if (lastvisibleitemposition == productsAdapter.getItemCount() - 1 && !isLoading && !isLastPage && dy > 0) {
                isLoading = true;
                isShowProgressBar(isLoading);
                ++page;
                callApi(getItemDetails(false), getCategoryRequest("Makeup", ("" + page)));
            }
        }
    };


    @Override
    public void errorResponseHandler(JSONObject jsonObject, ApiNames serviceTaskType) {
        isLoading = false;
        isShowProgressBar(isLoading);
        if (serviceTaskType == ApiNames.CategoryDetail) {
            App.printToast(R.string.server_side_error);
        }
    }

    @Override
    public void responseHandler(JSONObject jsonObject, ApiNames serviceTaskType) {
        isLoading = false;
        isShowProgressBar(isLoading);
        if (serviceTaskType == ApiNames.CategoryView) {
            updateProductListArray(jsonObject);
        }
    }

    private void updateProductListArray(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("products");
            Product product;
            for (int i = 0; i < jsonArray.length(); i++) {
                product = new Product(jsonArray.getJSONObject(i));
                productArrayList.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        productsAdapter.notifyDataSetChanged();
        checkIfItmeNotAvailable();
    }

    @Override
    public void notifyNetworkStateChange(boolean isNetworkAvailable) {
        if (isNetworkAvailable && productArrayList.size() == 0) {
            callApi(getItemDetails(true), getCategoryRequest("Makeup", ("" + page)));
        } else {
            App.printToast(R.string.api_network_not_available);
            getActivity().finish();
        }
    }

    public String getTitle() {
        if (category == "Tools") {
            return "Tools & Brushes";
        } else if (category == "SkinCare") {
            return "Skin Care";
        }
        return category;
    }
}
