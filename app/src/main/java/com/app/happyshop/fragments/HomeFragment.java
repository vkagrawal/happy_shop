package com.app.happyshop.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.happyshop.MainActivity;
import com.app.happyshop.R;
import com.app.happyshop.apicalls.ApiNames;
import com.app.happyshop.base.BaseFragmentWithApi;

import org.json.JSONObject;

public class HomeFragment extends BaseFragmentWithApi {

    public HomeFragment() {
    }

    public static HomeFragment create() {
        final Bundle args = new Bundle();
        args.putInt(EXTRA_DATA_LAYOUT, R.layout.home_fragment);
        HomeFragment f = new HomeFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCallback.setCurrentFrag(HomeFragment.this);
        mCallback.setActionBarTitle("Home");
        mCallback.isShowBackHome(false);
    }

    @Override
    public void initOnCreateView(View v) {
        v.findViewById(R.id.makeup).setOnClickListener(this);
        v.findViewById(R.id.skinCare).setOnClickListener(this);
        v.findViewById(R.id.hair).setOnClickListener(this);
        v.findViewById(R.id.toolsBrushes).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.makeup:
                goToCategoryView("Makeup");
                break;
            case R.id.skinCare:
                goToCategoryView("SkinCare");
                break;
            case R.id.hair:
                goToCategoryView("Hair");
                break;
            case R.id.toolsBrushes:
                goToCategoryView("Tools");
                break;

        }
    }

    private void goToCategoryView(String category) {
        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).pushFragments(MainActivity.CATEGORIES,
                    CategoryViewFragment.create(category), true, true);
        }
    }

    @Override
    public void errorResponseHandler(JSONObject jsonObject, ApiNames serviceTaskType) {

    }

    @Override
    public void responseHandler(JSONObject jsonObject, ApiNames serviceTaskType) {

    }

    @Override
    public void notifyNetworkStateChange(boolean isNetworkAvailable) {

    }

}
