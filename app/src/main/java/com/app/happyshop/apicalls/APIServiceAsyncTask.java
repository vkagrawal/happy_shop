package com.app.happyshop.apicalls;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.app.happyshop.R;

import org.json.JSONObject;

import java.util.Map;


public class APIServiceAsyncTask extends AsyncTask<Void, Integer, Object> {

    private Map<String, String> serviceParamsMap = null;
    private Object serviceParamsObject = null;

    private ApiRequest apiRequest = null;
    private ProgressDialog mDialog = null;
    Context mContext;

    public APIServiceAsyncTask(Context mContext) {
        this.mContext = mContext;
    }

    public APIServiceAsyncTask(Context mContext, ApiRequest apiRequest, Map<String, String> serviceParamsMap) {
        this.mContext = mContext;
        this.apiRequest = apiRequest;
        this.serviceParamsMap = serviceParamsMap;
    }

    public APIServiceAsyncTask(Context mContext, ApiRequest apiRequest, Object serviceParams) {
        this.mContext = mContext;
        this.apiRequest = apiRequest;
        this.serviceParamsObject = serviceParams;
    }

    public void showProgress(boolean isShow) {
        showProgress(isShow,null);
    }
    public void showProgress(boolean isShow,String pDialogMsg) {
            if (isShow && (mDialog == null || (!mDialog.isShowing()))) {
                mDialog = new ProgressDialog(mContext);
                mDialog.setMessage((pDialogMsg != null && pDialogMsg.length()>0)?pDialogMsg:"Please Wait...");
                mDialog.setCancelable(false);
                if(!((Activity) mContext).isFinishing()) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.show();
                        }
                    });
                }
            } else {
                if(mDialog != null && mDialog.isShowing())
                    mDialog.dismiss();
            }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(CheckNetwork.getInstance().isNetworkAvailable(mContext)) {
            if(apiRequest.isShowProgressBar())
                try {
                    showProgress(true,apiRequest.getpDialogMsg());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }else {
            CheckNetwork.getInstance().ShowDialog(mContext);
            APIServiceAsyncTask.this.cancel(true);
        }
    }

    @Override
    protected Object doInBackground(Void... params) {

            try {
                if (apiRequest != null) {
                    ServiceHandler sh = new ServiceHandler();
                    ResponseModel responseModel = null;
                    //JSONObject jsonObj = null;
                    // Making a request to url and getting response
                    if (apiRequest.isDownload()) {
                        //jsonStr = sh.makeDownloadCall(MediaDownloadURLKey, MediaSavePathKey,serviceParamsMap).toString();
                    //} else if (apiRequest.isUpload) {
                        //jsonStr = sh.makeUploadCall();
                    } else {
                        if(serviceParamsMap != null) {
                            responseModel = sh.makeServiceCall(apiRequest, serviceParamsMap);
                        }else {
                            responseModel = sh.makeServiceCall(apiRequest, serviceParamsObject);
                        }
                    }
                    if (responseModel != null && responseModel.getResponse().toString().length()>0) {
                        Log.e("Working", "Json Object not null");
                        return responseModel;
                    } else {
                        //Shadow.alertToast("Exception : Couldn't get any data from the url");
                        Log.e("Exception", "Couldn't get any data from the url");
                        failure("Couldn't get any data from the url");
                        return null;
                    }
                } else {
                    //Shadow.alertToast("Exception : Something went wrong");
                    Log.e("Exception", "Something went wrong");
                    failure("Something went wrong");
                    return null;
                }

            } catch (Exception e) {
                //Shadow.alertToast("Exception" + e.getMessage());
                //Log.e("Exception", e.getMessage());
                e.printStackTrace();
                return null;
            }
    }

    @Override
    protected void onPostExecute(Object o) {
        //super.onPostExecute(o);
        showProgress(false);
        if(o instanceof ResponseModel){
            Log.e("print instanceof ", "json");
            ResponseModel rModel = (ResponseModel) o;
            JSONObject jsonObj = rModel.getResponse();
            if((rModel.getResponseCode()/100) == 2){
                this.success(jsonObj,apiRequest.getReqID());
                Log.e("Success", "Goto success block");
            }else if(rModel.getResponseCode() == -10){
                showErrorMessage(mContext.getString(R.string.api_network_issue),mContext.getString(R.string.api_network_issue_messgae));
            }else {
                Log.i("Failure", "Goto failure block");
                this.failure("FAILURE");
                this.failure(jsonObj, apiRequest.getReqID());
            }
        }else if(o == null){
            this.failure("FAILURE");
        }
        onFinished(apiRequest.getReqID());
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        showProgress(false);
        Toast.makeText(mContext,"Please retry", Toast.LENGTH_LONG).show();
    }

    protected void success(JSONObject jsonObj, ApiNames serviceTaskType) {

    }

    protected void failure(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    protected void failure(JSONObject jsonObj, ApiNames serviceTaskType) {

    }

    private void showErrorMessage(String message){
        showErrorMessage(mContext.getString(R.string.api_error),message);
    }

    private void showErrorMessage(String title, String message){
            try {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext)
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(true)
                        .setPositiveButton(mContext.getResources().getString(R.string.ok)
                                ,new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        arg0.dismiss();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    protected void onFinished(ApiNames serviceTaskType) {

    }


}