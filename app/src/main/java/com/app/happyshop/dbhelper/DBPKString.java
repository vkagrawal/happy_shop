package com.app.happyshop.dbhelper;


public class DBPKString extends BaseType{
      String fieldDb;

    public DBPKString() {
        this.pkValueDb = true;
    }

    public DBPKString(String field) {
        this.fieldDb = field;
        this.pkValueDb = true;
    }

    public void setFieldDb(String fieldDb) {
        this.fieldDb = fieldDb;
    }

    @Override
    public Object getFieldDb() {
        return fieldDb;
    }

    @Override
    public String getFieldType(){
        return STRING_TY;
    }
}