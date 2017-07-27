package com.app.happyshop.dbhelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseModel implements Serializable {

    private static final String SORT_TYPE_ASC  = "ASC";
    private static final String SORT_TYPE_DESC  = "DESC";
    private static final String INTEGER_PRIMARY_KEY  = "INTEGER PRIMARY KEY";
    private static final String INTEGER_PRIMARY_KEY_AUTO  = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String TEXT_PRIMARY_KEY = "TEXT PRIMARY KEY";
    private static final String TEXT  = "TEXT";
    private static final String INTEGER  = "INTEGER";

    private static final String GET_DB_FIELDS = "GET_DB_FIELDS";
    private static final String GET_PK_FIELDS = "GET_PK_FIELDS";
    private static final String GET_PK_VALUE = "GET_PK_VALUE";
    private static final String GET_TABLES_FIELDS = "GET_TABLES_FIELDS";
    private static final String GET_CONTENT_VALUES = "GET_CONTENT_VALUES";

    public BaseModel() {
       getAllDBFileds();
        Log.e("ALL DB FILED  ", "is RUN0000");
    }

    public String getTableName(){
        return getClassName();
    }
    public String getClassName(){
        return this.getClass().getSimpleName();
    }

 /*   public Map<String, HashMap<String,String>> getDBScheme() {
        Map<String, HashMap<String,String>> dbSchema = new HashMap<String, HashMap<String,String>>();
        dbSchema.put(etTableName(),getAllDBFileds());
        return dbSchema;
    }*/

    public String getPKField() {
        return getCommonType(GET_PK_FIELDS).toString();
    }

    public Object getPKValue(){
        return getCommonType(GET_PK_VALUE);
    }


    public Object getCommonType(String retrieveType ) {
        Log.e("ALL DB FILED  ", "is RUN1111");
        HashMap<String,String> scheme = new HashMap<String,String>();
        ArrayList<String> keyList = new ArrayList<>();
        ContentValues values = new ContentValues();
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldStr = field.toString();
            Type fieldType = field.getGenericType();
         /*   try {
                DBString dbString =((DBString) field.get(this));
                Log.e("dbString ",""+dbString.get Field());
                Log.e("dbString pkValue",""+dbString.is PkValue());
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            //Log.e("fields name", "is " + fieldStr +" type is "+fieldType);
            if(fieldStr.contains(this.getClassName()) && (!fieldStr.contains("static"))){
                String fieldName = fieldStr.split("."+ this.getClassName()+".")[1];
                if(retrieveType.equals(GET_DB_FIELDS)) {
                    if(fieldType.toString().contains("DB")) {
                        scheme.put(fieldName, getColumnType(field));
                        Log.e("fields ame", "is " + fieldName);
                    }
                } else if(retrieveType.equals(GET_PK_FIELDS) || retrieveType.equals(GET_PK_VALUE)){
                    String fieldTypeStr = getColumnType(field).toString();
                    if(fieldTypeStr.contains("PRIMARY KEY")){
                        if(retrieveType.equals(GET_PK_VALUE)){
                            try {
                                return ((BaseType)field.get(this)).getFieldDb();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }else
                            return fieldName;
                    }
                } else if(retrieveType.equals(GET_CONTENT_VALUES)){
                    try {
                        if (fieldType.toString().contains("int")) {
                            //values.put(fieldName, (Integer.parseInt((String) ((BaseType) field.get(this)).getFieldDb())));
                            values.put(fieldName, ((int) ((BaseType) field.get(this)).getFieldDb()));
                        } else if (fieldType.toString().contains("String")) {
                            String dbStringFld;
                            if (fieldType.toString().contains("PK")) {
                                dbStringFld = (String) ((DBPKString) field.get(this)).getFieldDb();
                            } else {
                                dbStringFld = (String) ((DBString) field.get(this)).getFieldDb();
                            }
                            Log.e("dbStringFld",""+dbStringFld);
                            Log.e("fieldName",""+fieldName);
                            values.put(fieldName, dbStringFld);
                        } else if (fieldType.toString().contains("boolean")) {
                            values.put(fieldName, ((boolean)(((BaseType)field.get(this)).getFieldDb())) ? 1 : 0);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else if(retrieveType.equals(GET_TABLES_FIELDS)){
                    if(fieldType.toString().contains("DB")) {
                        keyList.add(fieldName);
                    }
                }
            }
        }
        if(retrieveType.equals(GET_TABLES_FIELDS)){
            return keyList;
        }else if(retrieveType.equals(GET_CONTENT_VALUES)){
            return values;
        }else
            return scheme;
    }
    public HashMap<String, String> getAllDBFileds() {
        return ((HashMap<String, String>)getCommonType(GET_DB_FIELDS));
    }

    private String getColumnType(Field field){
        //TODO
        Type fieldType = field.getGenericType();
        Log.e("getColumnType",""+fieldType.toString());
        try {
            if (fieldType.toString().contains("int")) {
                if(fieldType.toString().contains("Auto")){
                    return INTEGER_PRIMARY_KEY_AUTO;
                }else {
                    return (fieldType.toString().contains("PK"))
                            ? INTEGER_PRIMARY_KEY : INTEGER;
                }
            }else if (fieldType.toString().contains("String")) {
                return (fieldType.toString().contains("PK"))?TEXT_PRIMARY_KEY:TEXT;
                // return TEXT;
            }else if (fieldType.toString().contains("boolean")) {
                return INTEGER;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TEXT;
    }

    // Reference http://tutorials.jenkov.com/java-reflection/fields.html
    public BaseModel(Cursor cursor) {
        this();
        HashMap<String,String> scheme = new HashMap<String,String>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String fieldStr = fields[i].toString();
            if(fieldStr.contains(this.getClassName()) && (!fieldStr.contains("static"))){
                String fieldName = fieldStr.split("."+ getClassName()+".")[1];
                if(fieldName.contains("DB")) {
                    Field field = fields[i];
                    field.setAccessible(true);
                    Type fieldType = field.getGenericType();
                    try {
                        if(fieldType.toString().contains("int")){
                            if (fieldType.toString().contains("PK")) {
                                ((DBPKint)fieldType).setFieldDb(cursor.getInt(i));
                            } else {
                                ((DBint)fieldType).setFieldDb(cursor.getInt(i));
                            }
                        }else if(fieldType.toString().contains("String")){
                            if (fieldType.toString().contains("PK")) {
                                ((DBPKString)fieldType).setFieldDb(cursor.getString(i));
                            } else {
                                ((DBString)fieldType).setFieldDb(cursor.getString(i));
                            }
                        }else if(fieldType.toString().contains("boolean")){
                            ((DBboolean)fieldType).setFieldDb((cursor.getInt(i) == 1) ? true : false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Reference http://tutorials.jenkov.com/java-reflection/fields.html
    // Reference http://www.coderzheaven.com/2012/07/25/serialization-android-simple-example/
  /*  public String toString() {
        //HashMap<String,Object> dMap = getDataMap();
        StringBuilder str = new StringBuilder(""+getClassName()+" [");
        String separator = "";
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String fieldStr = fields[i].toString();
            if(fieldStr.contains(this.getClassName()) && (!fieldStr.contains("static"))){
                String fieldName = fieldStr.split("."+ getClassName()+".")[1];
                if(fieldName.contains("db")) {
                    Field field = fields[i];
                    field.setAccessible(true);
                    try {
                        str.append(separator+fieldName+" ="+field.get(this));
                        separator = ",";
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
                str.append("]");
        return str.toString();
    }*/

    // Deleting single obj
    public static int clearAllTables() {
        try {
            int j=0;
            SQLiteDatabase db =  DatabaseHandler.dbHandlerObj.getWritableDatabase();
            int length = DatabaseHandler.dbSchema.size();
            for(int i=0;i<length;i++) {
                int numOfRows = db.delete(DatabaseHandler.dbSchema.keySet().toString(), "1", null);
                if(numOfRows>0){
                    j++;
                }
            }
            db.close();
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int deleteAll() {
        try {
            SQLiteDatabase db =  DatabaseHandler.dbHandlerObj.getWritableDatabase();
            int numOfRows= db.delete(getTableName(), "1",null);
            db.close();
            return numOfRows;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public boolean delete() {
        Field[] fields = this.getClass().getDeclaredFields();
        String clause = "";
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldStr = field.toString();
            String fieldName = fieldStr.split("."+ getClassName()+".")[1];
            Type fieldType = field.getGenericType();
            if(fieldType.toString().contains("PK")){
                try {
                    String idStr = "";
                    int id = -10;
                    if(fieldType.toString().contains("int")){
                        id =  (int) ((DBPKint) field.get(this)).getFieldDb();
                    }else if(fieldType.toString().contains("String")){
                        idStr = (String) ((DBPKString) field.get(this)).getFieldDb();
                    }else {
                        Log.e("Error","Please enter your on clause");
                    }
                    if(id == -10 && idStr.length()<1) {
                        Log.e("Error", "Please enter your on clause");
                    }else if(id != -10){
                        clause = "" + fieldName + "='" + id + "'";
                    }else {
                        clause = "" + fieldName + "='" + idStr + "'";
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.e("Error", "Please enter your on clause");
                }
            }
        }
        Log.e("clause", clause);
        return delete(clause);
    }

    public boolean delete(String whereClause) {
        try {
            SQLiteDatabase db =  DatabaseHandler.dbHandlerObj.getWritableDatabase();
            boolean isDeleted = db.delete(getTableName(), whereClause, null) > 0;
            db.close();
            return isDeleted;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean delete(Object key) {
        try {
            SQLiteDatabase db =  DatabaseHandler.dbHandlerObj.getWritableDatabase();
            //db.delete(getTableName(), getPKField() + " = ?",new String[]{String.valueOf(key)});
            boolean isDeleted = db.delete(getTableName(), getPKField() + "=" + key.toString(), null) > 0;
            db.close();
            return isDeleted;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String[] getTableFieldsString(){
        ArrayList<String> keyList = getTableFields();
        String[] fields = new String[keyList.size()];
        for(int i=0;i<keyList.size();i++){
            fields[i] = keyList.get(i);
        }
        return fields;
    }

    public ArrayList<String> getTableFields(){
        return ((ArrayList<String>)getCommonType(GET_TABLES_FIELDS));
    }

    // Getting objs Count
    public int count() {
        return count(getTableName());
    }

    public static int count(String tableName) {
        String countQuery = "SELECT  * FROM " + tableName;//ASC  DESC
        SQLiteDatabase db =  DatabaseHandler.dbHandlerObj.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int countValue = cursor.getCount();
        cursor.close();
        // return count
        return countValue;
    }

    // Updating single obj
    public int update() {
        Log.e("update start", "1");
        return update(this.getContentValues());
    }
    public int update(ContentValues values) {
        SQLiteDatabase db =  DatabaseHandler.dbHandlerObj.getWritableDatabase();
        // updating row
        Log.e("update start","2 pk field "+getPKField());
        int rtnVal = db.update(getTableName(), values, getPKField() + " = ?",
                new String[]{String.valueOf(this.getPKValue())});
        Log.e("update start","3 update rows"+rtnVal);
        db.close();
        Log.e("update start", "4 pk value " + this.getPKValue());
        return rtnVal;
    }

    public  void save(){
        //TODO
        //if(update()<1){
            SQLiteDatabase db = DatabaseHandler.dbHandlerObj.getWritableDatabase();
            db.insert(getTableName(), null, this.getContentValues());
            db.close();
        //}
    }

    protected ContentValues getContentValues(){
        return ((ContentValues)getCommonType(GET_CONTENT_VALUES));
    }

    protected BaseModel find(int id){
        return find(id, this);
    }

    protected BaseModel getCursorObj(Cursor cursor){
        // return new SubModel(cursor);
        //SubModel subNew2 =new SubModel(cursor);
        BaseModel currentIns = this.getInstanse(cursor);
        Field[] fields = this.getClass().getDeclaredFields();
        String pkField = getPKField();
        for (int i = 0; i < fields.length; i++) {
            boolean isPkField = false;
            Field field = fields[i];
            field.setAccessible(true);
            String fieldStr = field.toString();
            Type fieldType = field.getGenericType();
            if(fieldStr.contains(this.getClassName()) && (!fieldStr.contains("static"))){
                String fieldName = fieldStr.split("."+ getClassName()+".")[1];
                Log.e("print all vaule test33", "is " + fieldName+"  "+pkField+" value "+isPkField);
                                  if(fieldName.equals(pkField)){
                                          isPkField = true;
                                  }
                Log.e("print all vaule test33", "is " + fieldName+"  "+pkField+" value2 "+isPkField);
                if(fieldType.toString().contains("DB")) {
                    try {
                        Field field11;
                        try {
                            field11 = currentIns.getClass().getDeclaredField(fieldName);
                        }catch (Exception e){
                            e.printStackTrace();
                            field11 = currentIns.getClass().getField(fieldName);
                        }
                        field11.setAccessible(true);
                        if (fieldType.toString().contains("int")) {
                            int value = cursor.getInt(cursor.getColumnIndex(fieldName));
                            Log.e("print all vaule", "" + value);
                           if(fieldType.toString().contains("PK")){
                                if(fieldType.toString().contains("Auto")){
                                    field11.set(currentIns, new DBPKAutoint(value));
                                }else
                                    field11.set(currentIns, new DBPKint(value));
                            }else{
                                field11.set(currentIns, new DBint(value));
                            }
                        } else if (fieldType.toString().contains("String")) {
                            String value = cursor.getString(cursor.getColumnIndex(fieldName));
                            Log.e("print all vaule", "" + value);

                            if (fieldType.toString().contains("PK")) {
                                field11.set(currentIns, new DBPKString(value));
                            } else {
                                field11.set(currentIns, new DBString(value));
                            }
                        } else if (fieldType.toString().contains("boolean")) {
                            boolean value = (cursor.getInt(cursor.getColumnIndex(fieldName)))==1;
                            Log.e("print all vaule",""+value);
                            field11.set(currentIns, new DBboolean(value));
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    Log.e("print all vaule test2",""+fieldName);
                }
            }
        }

        return currentIns;
    }

    protected abstract BaseModel getInstanse(Cursor cursor);

    // Getting single obj
    public static BaseModel find(Object id, BaseModel con) {
        SQLiteDatabase db =  DatabaseHandler.dbHandlerObj.getReadableDatabase();
        Cursor cursor = db.query(con.getTableName(), con.getTableFieldsString(), con.getPKField() + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        BaseModel obj;
        if (cursor != null && cursor.moveToFirst()){
            obj = con.getCursorObj(cursor);
        }else{
            return null;
        }
        // return obj
        return obj;
    }

    public List findAllData() {
        return findAllData(this.getTableName(), this);
    }

    public List findDataById(Object id) {
        return findDataById(id, getTableName(), this);
    }
    public static List findDataById(Object id, String tableName, BaseModel con) {
        String selectQuery;
        if (id instanceof Integer) {
            selectQuery = "SELECT  * FROM " + tableName+" where "+con.getPKField()+" = '"+id+"'";
        } else {
            selectQuery = "SELECT  * FROM " + tableName+" where "+con.getPKField()+" = "+id;
        }
        return findAllData(con, selectQuery);
    }
    public List findAllData(String query) {
        return findAllData(this, query);
    }

    // Getting All objs
    public static List findAllData(String tableName, BaseModel con) {
        String selectQuery = "SELECT  * FROM " + tableName ;
        return findAllData(con,selectQuery);
    }

    public static List findAllData(BaseModel con, String selectQuery) {
        List objList = new ArrayList<>();
        // Select All Query

        SQLiteDatabase db =  DatabaseHandler.dbHandlerObj.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                objList.add(con.getCursorObj(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return obj list
        return objList;
    }

    public String toString() {
        //HashMap<String,Object> dMap = getDataMap();
        StringBuilder str = new StringBuilder(""+getClassName()+" [");
        String separator = "";
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldStr = field.toString();
            if(fieldStr.contains(this.getClassName()) && (!fieldStr.contains("static"))){
                Log.e("print fieldData ", " " + fieldStr);
                String fieldName = fieldStr.split("."+ getClassName()+".")[1];
                String fieldType = field.getGenericType().toString();
                Log.e("print fieldData ame", " " + fieldName);
                //Log.e("print fieldData type", " " + fieldType);
                if(fieldType.contains("db") ) {

                    try {
                        //str.append(separator+fieldName+" ="+field.get(this));
                        BaseType childObj = null;
                        if(fieldStr.contains("DBString")) {
                            childObj = ((DBString)field.get(this));
                        }else if(fieldStr.contains("DBPKString")) {
                            childObj = ((DBPKString)field.get(this));
                        }else if(fieldStr.contains("DBPKint")) {
                            childObj = ((DBPKint)field.get(this));
                        }else if(fieldStr.contains("DBint")) {
                            childObj = ((DBint)field.get(this));
                        }else if(fieldStr.contains("DBboolean")) {
                            childObj = ((DBboolean)field.get(this));
                        }
                        if(childObj!= null) {
                            Log.e("print fieldData ", "is " + (childObj).toString());
                            str.append(separator + fieldName + " = " + (childObj).toString());
                            separator = ",";
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }catch (StackOverflowError e){
                        e.printStackTrace();
                    }

                }
            }
        }
        str.append("]");
        Log.e("print full String ", "si " + str.toString());
        return str.toString();
    }

    public DBint makeInt(){
        return new DBint();
    }

    public DBPKint makePKInt(){
        return new DBPKint();
    }

    public DBPKAutoint makePKAutoInt(){
        return new DBPKAutoint();
    }

    public DBint makeInt(int field){
        return new DBint(field);
    }

    public DBPKint makePKInt(int field){
        return new DBPKint(field);
    }

    public DBPKAutoint makePKAutoInt(int field){
        return new DBPKAutoint(field);
    }

    public DBString makeStr(){
        return new DBString();
    }

    public DBPKString makePKStr(){
        return new DBPKString();
    }

    public DBString makeStr(String field){
        return new DBString(field);
    }

    public DBPKString makePKStr(String field){
            return new DBPKString(field);
        }

    public DBboolean makeBoolean(){
        return new DBboolean();
    }

    public DBboolean makeBoolean(boolean field){
        return new DBboolean(field);
    }

}
