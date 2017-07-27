package com.app.happyshop.apicalls;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;


public class ServiceHandler {

    static String response = null;
    public static final String AUTHORIZATION = "Authorization";

    public static enum RequestMethod {
        GET, POST, PUT, PATCH, DELETE;
    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     */

    private String getURlFormPath(String path, boolean isTakeOnlyPath) {
        if (isTakeOnlyPath) {
            return path;
        }
        return "http://" + Requests.getSubDomain() + Requests.baseUrl + Requests.middleUrl + path;
        //return "http://requestb.in/1b3ocol1";
    }


    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     */

    public ResponseModel makeServiceCall(ApiRequest apiRequest,
                                         Map<String, String> params) throws JSONException {
        String paramsStr = null;
        try {
            if (params != null)
                paramsStr = getQuery(params, ((apiRequest.getMethod() == RequestMethod.GET) ? false : apiRequest.isJsonRequest()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return makeServiceCallBase(apiRequest, paramsStr);
    }

    public ResponseModel makeServiceCall(ApiRequest apiRequest,
                                         Object params) throws JSONException {
        Object paramsStr = null;
        try {
            if (params != null) {
                if (params instanceof Map) {
                    paramsStr = getQuery((Map<String, String>) params, ((apiRequest.getMethod() == RequestMethod.DELETE || apiRequest.getMethod() == RequestMethod.GET) ? false : apiRequest.isJsonRequest()));
                } else if (params instanceof String) {
                    paramsStr = (String) params;
                } else if (params instanceof JSONObject) {
                    //paramsStr = getPostDataStringBilder((JSONObject) params).toString();
                    paramsStr = params;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return makeServiceCallBase(apiRequest, paramsStr);
    }


    public ResponseModel makeServiceCallBase(ApiRequest apiRequest, Object paramsStr) {
        HttpURLConnection urlConnection = null;

        URL url = null;
        ResponseModel responseModel;
        JSONObject object = null;
        InputStream inStream = null;
        String temp, response = "";
        int status = 0;
        try {
            String urlStr = this.getURlFormPath(apiRequest.getPath(), apiRequest.isTakeOnlyPath());
            if ((apiRequest.getMethod() == RequestMethod.DELETE || apiRequest.getMethod() == RequestMethod.GET) && paramsStr != null) {
                urlStr = urlStr + "?" + paramsStr;
            }
            url = new URL(urlStr);

            Log.e("URL is ", "" + this.getURlFormPath(apiRequest.getPath(), apiRequest.isTakeOnlyPath()));
            Log.e("Method is ", "" + apiRequest.getMethod());
            Log.e("peramers is ", "" + paramsStr);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept",
                    apiRequest.isJsonRequest() ? "application/json" : "application/x-www-form-urlencoded");
            if (apiRequest.getMethod() == RequestMethod.POST) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type",
                        apiRequest.isJsonRequest() ? "application/json" : "application/x-www-form-urlencoded");
            } else if (apiRequest.getMethod() == RequestMethod.GET) {
                urlConnection.setRequestMethod("GET");
            } else if (apiRequest.getMethod() == RequestMethod.PUT) {
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type",
                        apiRequest.isJsonRequest() ? "application/json" : "application/x-www-form-urlencoded");
            } else if (apiRequest.getMethod() == RequestMethod.PATCH) {
                urlConnection.setRequestMethod("PATCH");
                urlConnection.setRequestProperty("Content-Type",
                        apiRequest.isJsonRequest() ? "application/json" : "application/x-www-form-urlencoded");
            } else if (apiRequest.getMethod() == RequestMethod.DELETE) {
                urlConnection.setDoInput(true);
                urlConnection.setInstanceFollowRedirects(false);
                if (apiRequest.isAuthunticate()) {
                    urlConnection.setRequestProperty(AUTHORIZATION, "Bearer " + Requests.getUserToker());
                }
                urlConnection.setRequestProperty("Connection", "Keep-Alive");
                urlConnection.setRequestProperty("Content-Type",
                        apiRequest.isJsonRequest() ? "application/json" : "application/x-www-form-urlencoded");//  charset=UTF-8
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setRequestProperty("charset", "utf-8");
                urlConnection.setUseCaches(false);
                urlConnection.connect();
            }

            if (apiRequest.isAuthunticate() && apiRequest.getMethod() != RequestMethod.DELETE) {
                urlConnection.setRequestProperty(AUTHORIZATION, "Bearer " + Requests.getUserToker());
            }
            if (apiRequest.getMethod() != RequestMethod.DELETE && apiRequest.getMethod() != RequestMethod.GET) {
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                if (apiRequest.getMethod() == RequestMethod.PUT) {
                    OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
                    Writer bw = new BufferedWriter(osw);
                    if (paramsStr != null && paramsStr.toString().length() > 0)
                        bw.write(paramsStr.toString());
                    bw.flush();
                    bw.close();
                    System.err.println(urlConnection.getResponseCode());
                } else {
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(paramsStr.toString());

                    //Log.e("paramsStr", "is " + URLEncoder.encode(paramsStr.toString(), "UTF-8"));
                    writer.flush();
                    writer.close();
                    os.close();
                    urlConnection.connect();
                }
            }
            try {
                try {
                    status = urlConnection.getResponseCode();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    status = -10;
                } catch (IOException e) {
                    e.printStackTrace();
                    status = -10;
                }
                Log.e("Print Response Code", "" + status);
                //if((status%100) != HttpStatus.SC_BAD_REQUEST)
                if ((status / 100) != 2)
                    inStream = urlConnection.getErrorStream();
                else
                    inStream = urlConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                inStream = urlConnection.getErrorStream();
                if (inStream == null) {
                    inStream = urlConnection.getInputStream();
                }
            }

            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }

            Log.e("Response is ", "" + response);
            object = (JSONObject) new JSONTokener(response).nextValue();

            //object= new JSONObject(getStringFromInputStream(inStream));

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        try {
            Log.e("JsonObject", "" + object.toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return new ResponseModel(status, object);
    }


    public JSONObject makeDownloadCall(String downloadURLKey, String savePathKey, Map<String, String> params) {
        String downloadUrl = params.get(downloadURLKey);
        String saveFilePath = params.get(savePathKey);
        if (downloadUrl == null || saveFilePath == null) {
            return null;
        }
        JSONObject object = null;
        final int BYTE = 1024;
        final int MEGABYTE = BYTE * 1024;
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL(downloadUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setDoInput(true);
            //urlConnection.setDoOutput(true);

            String afterSaveFilePath = saveFilePath;
            String lastStr = saveFilePath.substring(saveFilePath.lastIndexOf('/') + 1);
            saveFilePath = saveFilePath.replace(lastStr, "tempData__" + lastStr);
            Log.e("FileName: ", afterSaveFilePath);
            Log.e("FileName: ", saveFilePath);


            urlConnection.connect();
            File tmpFile = new File(saveFilePath);
            FileOutputStream fileOutput = new FileOutputStream(tmpFile);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            float presentSize = totalSize / 100;
            byte[] buffer = new byte[MEGABYTE];
            int bufferSize = 0;
            while ((bufferSize = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferSize);
                downloadedSize += bufferSize;
                //Log.e("Progress", "Downloaded : "+  Math.round(downloadedSize/presentSize)+" %");
            }
            fileOutput.close();

            File file = new File(afterSaveFilePath);
            if (tmpFile.exists())
                tmpFile.renameTo(file);

            object = (JSONObject) new JSONTokener("{\"success\":true}").nextValue();
        } catch (Exception e) {
            //Log.e("Progress", "Error : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return object;
    }

    public static String getQuery(Map<String, String> params,
                                  boolean isJsonRequest)
            throws Exception {
        StringBuffer requestParams = new StringBuffer();
        JSONObject jsonParam = new JSONObject();
        if (params != null && params.size() > 0) {
            Iterator<String> paramIterator = params.keySet().iterator();
            while (paramIterator.hasNext()) {
                String key = paramIterator.next();
                String value = params.get(key);
                if (isJsonRequest) {
                    jsonParam.put(key, value);
                } else {
                    requestParams.append(URLEncoder.encode(key, "UTF-8"));
                    requestParams.append("=").append(URLEncoder.encode(value, "UTF-8"));
                    requestParams.append("&");
                }
            }
            if (isJsonRequest) {
                //requestParams.append(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                //requestParams.append(getPostDataStringBilder(jsonParam));
                requestParams.append(jsonParam);
            }
        }
        int lenght = requestParams.toString().length();
        if (lenght > 0) {
            if (isJsonRequest) {
                return requestParams.toString();
            } else {
                return requestParams.toString().substring(0, (lenght - 1));
            }
        } else
            return requestParams.toString();
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
