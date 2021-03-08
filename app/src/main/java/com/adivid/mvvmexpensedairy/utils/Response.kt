package com.adivid.mvvmexpensedairy.utils

import androidx.annotation.NonNull
import androidx.annotation.Nullable


class AuthResource<T>(
        var authStatus: AuthStatus? = null,
        var data: T,
        var msg: String? = null

)


    fun <T> success(@Nullable data: T): AuthResource<T> {
        return AuthResource(
                AuthStatus.Success,
                data,
                null
        )
    }

    fun <T> Error(@NonNull msg: String?, @Nullable data: T): AuthResource<T>? {
        return AuthResource(
                AuthStatus.ERROR,
                data,
                msg
        )
    }

    fun <T> loading(@Nullable data: T): AuthResource<T>? {
        return AuthResource(
                AuthStatus.LOADING,
                data,
                null
        )
    }


    enum class AuthStatus {
        Success, ERROR, LOADING
    }
