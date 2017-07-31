package com.app.happyshop.dbhelper;

public class DBPKint extends BaseType {
    int fieldDb;

    public DBPKint() {
        this.pkValueDb = true;
    }

    public DBPKint(int field) {
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
