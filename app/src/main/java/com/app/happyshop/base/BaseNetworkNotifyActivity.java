package com.app.happyshop.base;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.app.happyshop.R;
import com.app.happyshop.apicalls.CheckNetwork;

public abstract class BaseNetworkNotifyActivity extends BaseActivity {


    protected static BaseNetworkNotifyActivity activity = null;
    private final int ACCESS_NETWORK_STATE_CODE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        activity = this;
        setContentView(R.layout.fragments_container);
        resources = getResources();
    }

    protected TextView networkMessageTextView;
    public static final String NETWORK_DISABLE_ACTION = (App.getCurrentPackageName() + ".action.network_disbale");
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            BaseNetworkNotifyActivity.this.showNetworkMessageAndNotify(!CheckNetwork.getInstance().isNetworkAvailable(BaseNetworkNotifyActivity.this));
        }
    };

    protected void showNetworkMessageAndNotify(final boolean isShow) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkMessageTextView.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }
        });
        notifyOnNetworkStateChange(isShow);
    }

    protected abstract void notifyOnNetworkStateChange(boolean isNetworkAvailable);

    protected abstract void initNetworkMessageTextView();


    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isAccessNetworkStateAllowed()) {
                initNetworkMessageTextView();
                callRegisterReceiver();
            } else {
                requestAccessNetworkStatePermission();
            }
        } else {
            initNetworkMessageTextView();
            callRegisterReceiver();
        }
    }


    //Requesting permission
    private void requestAccessNetworkStatePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE)) {
            App.printToastLong(R.string.permission_needed);
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, ACCESS_NETWORK_STATE_CODE);
    }

    //We are calling this method to check the permission status
    private boolean isAccessNetworkStateAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == ACCESS_NETWORK_STATE_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                initNetworkMessageTextView();
                callRegisterReceiver();
            } else {
                //Displaying another toast if permission is not granted
                App.printToastLong(R.string.permission_needed);
                finish();
            }
        }
    }

    protected void callRegisterReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NETWORK_DISABLE_ACTION);
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        // Put whatever message you want to receive as the action
        this.registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mBroadcastReceiver);
    }

}
