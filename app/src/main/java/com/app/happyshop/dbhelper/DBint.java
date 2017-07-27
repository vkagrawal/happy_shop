package com.app.happyshop.dbhelper;

public class DBint extends BaseType{
      int fieldDb;

    public DBint() {
        this.pkValueDb = false;
    }

    public DBint(int field) {
        this.fieldDb = field;
        this.pkValueDb = false;
    }

    public void setFieldDb(int fieldDb) {
        this.fieldDb = fieldDb;
    }

    @Override
    public Object getFieldDb() {
        return fieldDb;
    }

    @Override
    public String getFieldType(){
        return INT_TY;
    }
}
