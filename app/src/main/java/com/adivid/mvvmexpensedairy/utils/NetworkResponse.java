package com.adivid.mvvmexpensedairy.utils;

import javax.annotation.Nullable;

public class NetworkResponse<T> {

    public AuthStatus authStatus = null;
    public T data;
    public String msg = null;

    public NetworkResponse(AuthStatus authStatus, T data, String msg) {
        this.authStatus = authStatus;
        this.data = data;
        this.msg = msg;
    }

    public <T> NetworkResponse<T> success(@Nullable T data) {
        return new NetworkResponse(
                AuthStatus.Success,
                data,
                null
        );
    }

    public enum AuthStatus {
        Success, Error, Loading
    }

}
