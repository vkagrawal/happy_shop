package com.app.happyshop.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.happyshop.R;
import com.app.happyshop.apicalls.ApiNames;
import com.app.happyshop.apicalls.ApiRequest;
import com.app.happyshop.apicalls.ServiceHandler;
import com.app.happyshop.base.App;
import com.app.happyshop.base.BaseFragmentWithApi;
import com.app.happyshop.model.Product;
import com.loopj.android.image.SmartImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CategoryDetailsFragment extends BaseFragmentWithApi {

    TextView productName, productPrice, productOnSale, productDescription;
    SmartImageView productImage;
    Button addToCart;
    int id = -1;
    Product product = null;
    boolean isAlreadyExist = false;
    public CategoryDetailsFragment() {
    }

    public static CategoryDetailsFragment create(int id) {
        final Bundle args = new Bundle();
        args.putInt(EXTRA_DATA_LAYOUT, R.layout.category_details_fragment);
        args.putInt(EXTRA_DATA_TAG, id);
        CategoryDetailsFragment f = new CategoryDetailsFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCallback.setCurrentFrag(CategoryDetailsFragment.this);
        setTitle();
    }

    private void setTitle() {
        if (product != null && product.getName() != null && product.getName().length() > 0) {
            mCallback.setActionBarTitle(product.getName());
        } else {
            mCallback.setActionBarTitle("Item Details");
        }
    }

    @Override
    public void initOnCreateView(View v) {
        Bundle bundle = getArguments();
        if (bundle.containsKey(EXTRA_DATA_TAG)) {
            addToCart = (Button) v.findViewById(R.id.addToCart);
            addToCart.setOnClickListener(this);
            productName = (TextView) v.findViewById(R.id.productName);
            productPrice = (TextView) v.findViewById(R.id.productPrice);
            productOnSale = (TextView) v.findViewById(R.id.productOnSale);
            productDescription = (TextView) v.findViewById(R.id.productDescription);
            productImage = (SmartImageView) v.findViewById(R.id.productImage);
            id = bundle.getInt(EXTRA_DATA_TAG);
            mActivity.setAnimation(R.anim.scale, productImage);
            mActivity.setAnimation(R.anim.scale, addToCart);
            new CheckStatus().execute(id);
        } else {
            App.printToast("Item id not exist. Please try again.");
            mActivity.onBackPressed();
        }

    }

    private ApiRequest getItemDetails(int id) {
        ApiRequest itemDetails =
                new ApiRequest.Builder("http://sephora-mobile-takehome-apple.herokuapp.com/api/v1/products/" + id, ApiNames.CategoryDetail)
                        .setMethod(ServiceHandler.RequestMethod.GET)
                        .setAuthunticate(false)
                        .setShowProgressBar(true)
                        .setJsonRequest(true)
                        .setTakeOnlyPath(true)
                        .build();
        return itemDetails;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.addToCart:
                if(isAlreadyExist){
                    if(product.delete()) {
                        mActivity.onBackPressed();
                        App.printToast(R.string.removed);
                    } else {
                        App.printToast(R.string.error_while_removing);
                    }
                } else {
                    product.save();
                    App.printToast(R.string.item_added);
                }
                break;
        }
    }

    class CheckStatus extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... item) {
            try {
                List lst =  product.getInstance().findDataById(item[0]);
                if(lst != null && lst.size() > 0){
                    product = (Product) lst.get(0);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isExist) {
            super.onPostExecute(isExist);
            if (isExist && product != null) {
                isAlreadyExist = true;
                updateView(product);
                addToCart.setText(R.string.remove_from_cart);
            } else {
                callApi(getItemDetails(id));
            }
        }
    }

    @Override
    public void errorResponseHandler(JSONObject jsonObject, ApiNames serviceTaskType) {
        if (serviceTaskType == ApiNames.CategoryDetail) {
            App.printToast(R.string.server_side_error);
        }
    }

    @Override
    public void responseHandler(JSONObject jsonObject, ApiNames serviceTaskType) {
        if (serviceTaskType == ApiNames.CategoryDetail) {
            retriveJsonData(jsonObject);
        }
    }

    private void retriveJsonData(JSONObject jsonObject) {
        try {
            JSONObject procuteJson = jsonObject.getJSONObject("product");
            product = new Product(procuteJson);
            setTitle();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (product != null) {
            updateView(product);
        }
    }

    private void updateView(Product product) {
        productName.setText(product.getName());
        productPrice.setText(product.getPrice());
        productOnSale.setVisibility(product.isUnderSale() ? View.VISIBLE : View.INVISIBLE);
        if (product.getDescription() != null && product.getDescription().length() > 0) {
            productDescription.setText(product.getDescription());
        }
        productImage.setImageUrl(product.getImgUrl());
    }

    @Override
    public void notifyNetworkStateChange(boolean isNetworkAvailable) {
        if (isNetworkAvailable && id == -1 && product == null) {
            callApi(getItemDetails(id));
        }
    }

}
