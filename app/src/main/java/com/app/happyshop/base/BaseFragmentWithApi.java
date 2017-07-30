package com.app.happyshop.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.app.happyshop.apicalls.APIServiceAsyncTask;
import com.app.happyshop.apicalls.ApiNames;
import com.app.happyshop.apicalls.ApiRequest;

import org.json.JSONObject;

import java.util.Map;

public abstract class BaseFragmentWithApi extends BaseFragment implements View.OnClickListener {

    @Override
    public void onClick(View view) {

    }

    protected void callApi(ApiRequest call) {
        callApi(call, null);
    }

    protected void callApi(ApiRequest call, Object map) {
        ApiTask mTask = new ApiTask(getActivity(), call, map);
        mTask.execute((Void) null);
    }

    class ApiTask extends APIServiceAsyncTask {

        public ApiTask(Context mContext) {
            super(mContext);
        }

        public ApiTask(Context mContext, ApiRequest apiRequest, Map<String, String> serviceParamsMap) {
            super(mContext, apiRequest, serviceParamsMap);
        }

        public ApiTask(Context mContext, ApiRequest apiRequest, Object serviceParamsMap) {
            super(mContext, apiRequest, serviceParamsMap);
        }

        @Override
        protected void success(JSONObject jsonObj, ApiNames serviceTaskType) {
            super.success(jsonObj, serviceTaskType);
            mActivity.hideSoftKeyboard();
            responseHandler(jsonObj, serviceTaskType);
        }

        @Override
        protected void failure(JSONObject jsonObj, ApiNames serviceTaskType) {
            super.failure(jsonObj, serviceTaskType);
            errorResponseHandler(jsonObj, serviceTaskType);
            mActivity.hideSoftKeyboard();
        }

        @Override
        protected void failure(String message) {
            // super.failure(message);
            mActivity.hideSoftKeyboard();
            Toast.makeText(getActivity(), "Unknown error in API response", Toast.LENGTH_LONG).show();
        }


    }

    public abstract void errorResponseHandler(JSONObject jsonObject, ApiNames serviceTaskType);

    public abstract void responseHandler(JSONObject jsonObject, ApiNames serviceTaskType);

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

}
