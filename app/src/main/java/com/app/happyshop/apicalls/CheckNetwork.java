package com.app.happyshop.apicalls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckNetwork {

    private static CheckNetwork _instance = null;

    public static CheckNetwork getInstance() {
        if (_instance == null) {
            _instance = new CheckNetwork();
        }
        return _instance;
    }

    private boolean isNetwokAvailable;

    /**
     * check wheather network available or not either wi-fi of mobile network
     * @param mContext
     * @return
     */
    public boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectionManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        isNetwokAvailable = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Network[] wifiNetworkes = connectionManager.getAllNetworks();
            if (wifiNetworkes.length > 0) {
                for (Network network : wifiNetworkes) {
                    NetworkInfo wifiInfo = connectionManager.getNetworkInfo(network);
                    if (!isNetwokAvailable) {
                        if ((wifiInfo != null && wifiInfo.isConnected())) {
                            isNetwokAvailable = true;
                        }
                    }
                }
            }
        } else {
            NetworkInfo wifiInfo = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobileInfo = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
                isNetwokAvailable = true;
            }
        }
        Log.e("isNetwokAvailable: ", "" + isNetwokAvailable);
        return isNetwokAvailable;
    }

    public void ShowDialog(final Context mContext) {
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        mAlertDialog.setTitle("Networke is not available")
                .setMessage("No internet connectivity found")
                .setPositiveButton("Open WIFI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                        mContext.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }
}
