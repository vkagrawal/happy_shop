package com.app.happyshop.apicalls;


import com.app.happyshop.base.App;

public class Requests {

    public static final String SUB_DOMAIN = "subdomain";
    public static final String ACCESS_TOKEN="access_token";
    private static Requests _instance = null;

    public static Requests getInstance(){
        if(_instance== null){
            _instance = new Requests();
        }
        return _instance;
    }

    public static String baseUrl  = "";

    public static String subDomain  = "";

    public static String middleUrl  = "";

    public static String token = "";


    public static String getSubDomain(){
        return App.pref.getString(SUB_DOMAIN,"");
    }

    public static void setSubDomain(String subDomain) {
        Requests.subDomain = subDomain;
    }

    public static void setMiddleUrl(String middleUrl){
            Requests.middleUrl = middleUrl;
    }

    public static void setBaseUrl(String baseUrl){
        Requests.baseUrl = baseUrl;
    }

    public static void setUserToker(String token){
        Requests.token = token;
    }

    public static String getUserToker(){
        String token = App.getAppPreferences().getString(ACCESS_TOKEN, "");
        return token;
    }

    public final ApiRequest data =
            new ApiRequest.Builder("itunes.apple.com/us/rss/topfreeapplications/limit=20/json",ApiNames.Data)
                    .setMethod(ServiceHandler.RequestMethod.GET)
                    .setAuthunticate(false)
                    .setShowProgressBar(true)
                    .setJsonRequest(false)
                    .build();

}
