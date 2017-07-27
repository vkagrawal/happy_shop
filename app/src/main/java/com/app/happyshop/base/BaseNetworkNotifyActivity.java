package com.app.happyshop.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import com.app.happyshop.R;
import com.app.happyshop.apicalls.CheckNetwork;

public abstract class BaseNetworkNotifyActivity extends BaseActivity {


    protected static BaseNetworkNotifyActivity activity = null;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        activity = this;
        setContentView(R.layout.fragments_container);
        resources = getResources();
    }

    protected TextView networkMessageTextView;
    public static final String NETWORK_DISABLE_ACTION = (activity.getPackageName()+".action.network_disbale");
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            BaseNetworkNotifyActivity.this.showNetworkMessage(!CheckNetwork.getInstance().isNetworkAvailable(BaseNetworkNotifyActivity.this));
        }
    };

    protected void showNetworkMessage(final boolean isShow){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkMessageTextView.setVisibility(isShow? View.VISIBLE: View.GONE);
            }
        });
    }

    protected abstract void initNetworkMessageTextView();

    @Override
    protected void onResume() {
        super.onResume();
        initNetworkMessageTextView();
        callRegisterReceiver();
    }

    protected void callRegisterReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NETWORK_DISABLE_ACTION);
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        // Put whatever message you want to receive as the action
        this.registerReceiver(this.mBroadcastReceiver,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mBroadcastReceiver);
    }

}
