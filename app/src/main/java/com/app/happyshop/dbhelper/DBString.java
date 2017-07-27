package com.app.happyshop.dbhelper;


public class DBString extends BaseType{
      String fieldDb;

    public DBString() {
        this.pkValueDb = false;
    }

    public DBString(String field) {
        this.fieldDb = field;
        this.pkValueDb = false;
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
