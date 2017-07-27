package com.app.happyshop.apicalls;

import org.json.JSONObject;


public class ResponseModel {

    int responseCode;
    JSONObject response;

    public ResponseModel(int responseCode, JSONObject response) {
        this.responseCode = responseCode;
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public JSONObject getResponse() {
        return response;
    }
}
