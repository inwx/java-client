package com.inwx.domrobot.model;

import com.google.gson.annotations.SerializedName;

/**
 * Class which can be used to create custom response models.
 *
 * @param <ResDataImpl>
 */
public class GenericResponse<ResDataImpl> {

    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("resData")
    private ResDataImpl resData;

    public GenericResponse(int code, String msg, ResDataImpl resData) {
        this.code = code;
        this.msg = msg;
        this.resData = resData;
    }

    public GenericResponse() {

    }

    public boolean wasSuccessful() {
        return getCode() == 1000 || getCode() == 1001;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ResDataImpl getResData() {
        return resData;
    }
}
