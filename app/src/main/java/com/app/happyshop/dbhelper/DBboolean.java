package com.app.happyshop.dbhelper;


public class DBboolean extends BaseType {
    boolean fieldDb;

    public DBboolean() {
    }

    public void setFieldDb(boolean fieldDb) {
        this.fieldDb = fieldDb;
    }

    @Override
    public Object getFieldDb() {
        return fieldDb;
    }

    public DBboolean(boolean field) {
        this.fieldDb = field;
    }

    public boolean getFieldValue() {
        return fieldDb;
    }

    public String getFieldType() {
        return BOOLEAN_TY;
    }
}
