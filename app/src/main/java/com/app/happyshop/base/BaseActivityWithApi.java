package com.app.happyshop.base;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.app.happyshop.apicalls.APIServiceAsyncTask;
import com.app.happyshop.apicalls.ApiNames;
import com.app.happyshop.apicalls.ApiRequest;

import org.json.JSONObject;

import java.util.Map;


public abstract class BaseActivityWithApi extends BaseActivity implements View.OnClickListener {

    @Override
    public void onClick(View view) {

    }

    protected void callApi(ApiRequest call) {
        callApi(call, null);
    }

    protected void callApi(ApiRequest call, Object map) {
        ApiTask mTask = new ApiTask(this, call, map);
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
            hideSoftKeyboard();
            responseHandler(jsonObj, serviceTaskType);
        }

        @Override
        protected void failure(JSONObject jsonObj, ApiNames serviceTaskType) {
            super.failure(jsonObj, serviceTaskType);
            errorResponseHandler(jsonObj, serviceTaskType);
            hideSoftKeyboard();
        }

        @Override
        protected void failure(String message) {
            // super.failure(message);
            hideSoftKeyboard();
            Toast.makeText(BaseActivityWithApi.this, "Unknown error in API response", Toast.LENGTH_LONG).show();
        }


    }

   /* private Map<String, String> getParams(String code){
        Map<String, String> params = new HashMap<>();
        switch (code){
            case Test:
                params.put("access_token", getToken() );
                params.put("client_id", getUserId() );
            break;
        }
        return params;
    }*/

    public abstract void errorResponseHandler(JSONObject jsonObject, ApiNames serviceTaskType);

    public abstract void responseHandler(JSONObject jsonObject, ApiNames serviceTaskType);

}
