package com.app.happyshop.dbhelper;

import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;


public class BaseType implements Serializable {

    public static final String INT_TY = "int";
    public static final String STRING_TY = "String";
    public static final String BOOLEAN_TY = "boolean";


    public String getFieldType() {
        return null;
    }

    boolean pkValueDb = false;


    public Object getFieldDb() {
        return "";
    }

    public boolean isPkValueDb() {
        return pkValueDb;
    }

    public String getClassName() {
        return this.getClass().getSimpleName();
    }

    public String toString() {
        StringBuilder str = new StringBuilder("" + getClassName() + " [");
        String separator = "";
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldStr = field.toString();
            if (fieldStr.contains(getClassName())) {
                String fieldName = fieldStr.split("." + getClassName() + ".")[1];
                Log.e("BaseType fieldName ", "" + fieldName);
                if (fieldName.contains("Db")) {
                    try {
                        str.append(separator + fieldName + " = " + field.get(this));
                        separator = ",";
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        str.append(", pkValueDb = " + this.isPkValueDb());
        str.append("]");
        return str.toString();
    }

}





/*

public class BaseType {

    boolean pkValue =false;

    public BaseType() {
    }

    public BaseType(boolean pkValue) {
        this.pkValue = pkValue;
    }

    public boolean isPkValue() {
        return pkValue;
    }

    public String getFieldType(){
        return null;
    }


    public Object getField() {
        return "";
    }
}

* */

