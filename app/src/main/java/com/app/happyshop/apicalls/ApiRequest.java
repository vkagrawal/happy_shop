package com.app.happyshop.apicalls;


public class ApiRequest {

    Builder builder;

    public ApiRequest(Builder builder) {
        this.builder = builder;
    }

    public String getPath() {
        return builder.path;
    }

    public ApiNames getReqID() {
        return builder.reqID;
    }

    public ServiceHandler.RequestMethod getMethod() {
        return builder.method;
    }

    public boolean isAuthunticate() {
        return builder.authunticate;
    }

    public boolean isShowProgressBar() {
        return builder.isShowProgressBar;
    }

    public boolean isDownload() {
        return builder.isDownload;
    }

    public boolean isShowErrorDialog() {
        return builder.isShowErrorDialog;
    }

    public String getpDialogMsg() {
        return builder.pDialogMsg;
    }

    public boolean isJsonRequest() {
        return builder.isJsonRequest;
    }

    public boolean isTakeOnlyPath() {
        return builder.isTakeOnlyPath;
    }

    public static class Builder {

        public String path = "";
        public ApiNames reqID;
        public ServiceHandler.RequestMethod method = ServiceHandler.RequestMethod.GET;
        public boolean authunticate = true;
        public boolean isShowProgressBar = true;
        public boolean isDownload = false;
        public boolean isShowErrorDialog = false;
        public String pDialogMsg = "";
        public boolean isJsonRequest = false;
        public boolean isTakeOnlyPath = false;

        public Builder(String path, ApiNames reqID) {
            this.path = path;
            this.reqID = reqID;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setReqID(ApiNames reqID) {
            this.reqID = reqID;
            return this;
        }

        public Builder setMethod(ServiceHandler.RequestMethod method) {
            this.method = method;
            return this;
        }

        public Builder setAuthunticate(boolean authunticate) {
            this.authunticate = authunticate;
            return this;
        }

        public Builder setShowProgressBar(boolean showProgressBar) {
            isShowProgressBar = showProgressBar;
            return this;
        }

        public Builder setDownload(boolean download) {
            isDownload = download;
            return this;
        }

        public Builder setShowErrorDialog(boolean showErrorDialog) {
            isShowErrorDialog = showErrorDialog;
            return this;
        }

        public Builder setpDialogMsg(String pDialogMsg) {
            this.pDialogMsg = pDialogMsg;
            return this;
        }

        public Builder setJsonRequest(boolean jsonRequest) {
            isJsonRequest = jsonRequest;
            return this;
        }

        public Builder setTakeOnlyPath(boolean takeOnlyPath) {
            isTakeOnlyPath = takeOnlyPath;
            return this;
        }

        public ApiRequest build() {
            return new ApiRequest(this);
        }

    }

}