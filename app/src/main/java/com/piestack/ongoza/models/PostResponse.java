package com.piestack.ongoza.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeffrey Nyauke on 6/15/2017.
 */

public class PostResponse {

        @SerializedName("error")
        @Expose
        private Boolean error;
        @SerializedName("error_msg")
        @Expose
        private String errorMsg;

        public Boolean getError() {
            return error;
        }

        public void setError(Boolean error) {
            this.error = error;
        }


        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }


}
