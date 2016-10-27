package com.soloask.android.common.network;

import com.soloask.android.common.network.response.BaseResponse;
import com.soloask.android.util.Constant;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by lebron on 16-10-27.
 */

public class ApiResponseHandler<T> {
    private CustomHandler<T> handler;

    public ApiResponseHandler(CustomHandler<T> handler) {
        this.handler = handler;
    }

    public void onCompleted() {
        release();
    }

    public void onError(Throwable e) {
        resetLoadingStatus();
        e.printStackTrace();
        if (!handler.error(e)) {
            handleException(e);
        }
        release();
    }

    public void onNext(T t) {
        resetLoadingStatus();
        BaseResponse data;
        if (t instanceof BaseResponse) {
            data = (BaseResponse) t;
            if (data.getCode() == 1) {
                handler.success(t);
            } else {
                if (!handler.operationError(t, data.getCode(), data.getMessage())) {
                    handleOperationError(data.getMessage());
                }
            }
        } else {
            handler.success(t);
        }
        release();
    }

    public void resetLoadingStatus() {

    }

    public void release() {
        handler = null;
    }

    public void handleException(Throwable e) {
        if (e instanceof ConnectException) {
            Constant.showToastMessage("network error");
        } else if (e instanceof HttpException) {
            Constant.showToastMessage("network_server_error");
        } else if (e instanceof SocketTimeoutException) {
            Constant.showToastMessage("network_timeout");
        } else {
            Constant.showToastMessage("network_other");
        }
    }

    public void handleOperationError(String message) {
        Constant.showToastMessage(message);
    }

    public interface CustomHandler<T> {
        /**
         * 请求成功同时业务成功的情况下会调用此函数
         */
        void success(T t);

        /**
         * 请求成功但业务失败的情况下会调用此函数.
         *
         * @return 是否需要自行处理业务错误.
         */
        boolean operationError(T t, int status, String message);

        /**
         * 请求失败的情况下会调用此函数
         *
         * @return 是否需要自行处理系统错误.
         */
        boolean error(Throwable e);
    }
}
