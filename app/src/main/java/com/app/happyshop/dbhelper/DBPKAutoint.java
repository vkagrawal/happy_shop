package com.app.happyshop.dbhelper;

public class DBPKAutoint extends BaseType {
    int fieldDb;

    public DBPKAutoint() {
        this.pkValueDb = true;
    }

    public DBPKAutoint(int field) {
        this.fieldDb = field;
        this.pkValueDb = true;
    }

    public void setFieldDb(int fieldDb) {
        this.fieldDb = fieldDb;
    }

    @Override
    public Object getFieldDb() {
        return fieldDb;
    }


    @Override
    public String getFieldType() {
        return INT_TY;
    }
}
