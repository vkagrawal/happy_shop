package com.app.happyshop.model;

import android.database.Cursor;
import android.os.Parcelable;

import com.app.happyshop.dbhelper.BaseModel;

public abstract class AppDbBaseModel extends BaseModel implements Parcelable {

    public AppDbBaseModel() {
    }

    public AppDbBaseModel(Cursor cursor) {
        super(cursor);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
