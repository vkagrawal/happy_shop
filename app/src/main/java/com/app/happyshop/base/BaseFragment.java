package com.app.happyshop.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    public static final String EXTRA_DATA_LAYOUT = "sc_extra_layout";
    public static final String EXTRA_DATA_TAG = "sc_extra_tag";
    public static final String EXTRA_DATA_PARCELABLE = "sc_extra_Parcelable";

    public BaseActivity mActivity;
    public Resources resources;
    int _layout = -1;
    public String _extraTag = null;
    public FragmentSelectControl mCallback;

    public interface FragmentSelectControl {

        public void setCurrentFrag(BaseFragment frag);

        public BaseFragment getCurrentFrag();

        public void setActionBarTitle(String title);

        public void isShowBackHome(boolean isShow);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCallback.setCurrentFrag(this);
        mCallback.isShowBackHome(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) this.getActivity();
        resources = getResources();
        Bundle args = this.getArguments();
        if (args != null) {
            this._layout = args.getInt(EXTRA_DATA_LAYOUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(_layout, container, false);

        initOnCreateView(view);
        return view;
    }

    public abstract void initOnCreateView(View v);


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentSelectControl) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement Interface");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (mCallback == null)
                mCallback = (FragmentSelectControl) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Interface");
        }
    }

    public abstract void notifyNetworkStateChange(boolean isNetworkAvailable);

}
