package com.app.happyshop.model;

import android.database.Cursor;
import android.os.Parcel;

import com.app.happyshop.dbhelper.DBPKint;
import com.app.happyshop.dbhelper.DBString;
import com.app.happyshop.dbhelper.DBboolean;

import org.json.JSONException;
import org.json.JSONObject;

public class Product extends AppDbBaseModel {

    DBPKint id;
    DBString name;
    DBString category;
    DBString price;
    DBString imgUrl;
    DBboolean underSale;
    DBString description = makeStr("");

    public Product(JSONObject jsonObject) {
        try {
            this.id = makePKInt(jsonObject.getInt("id"));
            this.name = makeStr(jsonObject.getString("name"));
            this.category = makeStr(jsonObject.getString("category"));
            this.price = makeStr(jsonObject.getString("price"));
            this.imgUrl = makeStr(jsonObject.getString("img_url"));
            this.underSale = makeBoolean(jsonObject.getBoolean("under_sale"));
            if(jsonObject.has("description")) {
                this.description = makeStr(jsonObject.getString("description"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Product() {}

    public int getId() {
        return (int) id.getFieldDb();
    }

    public String getName() {
        return name.getFieldDb().toString();
    }

    public String getCategory() {
        return category.getFieldDb().toString();
    }

    public String getPrice() {
        return price.getFieldDb().toString();
    }

    public String getImgUrl() {
        return imgUrl.getFieldDb().toString();
    }

    public boolean isUnderSale() {
        return underSale.getFieldValue();
    }

    public String getDescription() {
        return description.getFieldDb().toString();
    }

    public void setId(int id) {
        this.id = makePKInt(id);
    }

    public void setName(String name) {
        this.name = makeStr(name);
    }

    public void setCategory(String category) {
        this.category = makeStr(category);
    }

    public void setPrice(String price) {
        this.price = makeStr(price);
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = makeStr(imgUrl);
    }

    public void setUnderSale(boolean underSale) {
        this.underSale = makeBoolean(underSale);
    }


    public void setDescription(String description) {
        this.description = makeStr(description);
    }

    /*Parcelable section*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getId());
        dest.writeString(this.getName());
        dest.writeString(this.getCategory());
        dest.writeString(this.getPrice());
        dest.writeString(this.getImgUrl());
        dest.writeByte(this.isUnderSale() ? (byte) 1 : (byte) 0);
        dest.writeString(this.getDescription());
    }

    protected Product(Parcel in) {
        this.setId(in.readInt());
        this.setName(in.readString());
        this.setCategory(in.readString());
        this.setPrice(in.readString());
        this.setImgUrl(in.readString());
        this.setUnderSale(in.readByte() != 0);
        this.setDescription(in.readString());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product(Cursor cursor) {
        super(cursor);
    }

    @Override
    protected Product getInstanse(Cursor cursor) {
        return new Product(cursor);
    }

    public static Product _instance = new Product();

    public static Product getInstance(){
        return _instance;
    }
}
