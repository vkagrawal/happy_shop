package com.app.happyshop.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.app.happyshop.dbhelper.DatabaseHandler;

public class App extends Application {

    public static Context _instance;

    public static String getAppNameAsStr() {
        return App.getAppContext().getClass().getSimpleName().toString();
    }

    public static final String TAG = App.class.getSimpleName();

    /*Shared Prefrence*/
    public static final String AppPref = TAG;
    public static SharedPreferences pref = null;
    public static SharedPreferences.Editor editor = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    //public static DatabaseHandler dbHandlerObj;
    @Override
    public void onCreate() {
        super.onCreate();
        App._instance = getApplicationContext();
        DatabaseHandler.initDB();
        /*Shared Prefrence*/
        getAppPreferences();
        getAppPreferencesEditor();
    }

    /*Shared Prefrence*/
    public static SharedPreferences getAppPreferences() {
        if (pref == null)
            pref = App._instance.getSharedPreferences(TAG, MODE_PRIVATE);

        return pref;
    }

    public static SharedPreferences.Editor getAppPreferencesEditor() {
        if (editor == null) {
            SharedPreferences pref = App.getAppPreferences();
            editor = pref.edit();
        }
        return editor;
    }

    public static Context getAppContext() {
        return App._instance;
    }

    public static void printToastLong(int id) {
        printToastLong(getAppContext().getResources().getString(id));
    }

    public static void printToastLong(String message) {
        Toast.makeText(App.getAppContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void printToast(int id) {
        printToast(getAppContext().getResources().getString(id));
    }

    public static void printToast(String message) {
        Toast.makeText(App.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static String getCurrentPackageName() {
        return getAppContext().getPackageName();
    }
}
