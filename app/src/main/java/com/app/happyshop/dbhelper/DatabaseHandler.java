package com.app.happyshop.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.happyshop.base.App;
import com.app.happyshop.model.Product;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DataBs.db";

    // Database table Schema

    public static DatabaseHandler dbHandlerObj;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void initDB() {
        //TODO
        dbHandlerObj = new DatabaseHandler(App.getAppContext());
        SQLiteDatabase db = dbHandlerObj.getReadableDatabase();
        db.close();
    }


    public static final Map<String, HashMap<String, String>> dbSchema = Collections.unmodifiableMap(
            new HashMap<String, HashMap<String, String>>() {{
                //TODO add tables
                put(Product.getInstance().getTableName(), Product.getInstance().getAllDBFileds());
            }});


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Map.Entry<String, HashMap<String, String>> entry : DatabaseHandler.dbSchema.entrySet()) {
            String tableName = entry.getKey();
            HashMap<String, String> tableFields = entry.getValue();
            String createTable = "CREATE TABLE " + tableName + "(";
            String separator = "";

            for (Map.Entry<String, String> entry1 : tableFields.entrySet()) {
                String key = entry1.getKey();
                Object value = entry1.getValue();
                createTable += (separator + key + " " + value);
                separator = " , ";
            }

            createTable += ")";
            db.execSQL(createTable);
        }
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Map.Entry<String, HashMap<String, String>> entry : dbSchema.entrySet()) {
            String tableName = entry.getKey();
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
        }
        // Create tables again
        onCreate(db);
    }

}
