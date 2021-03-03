package com.example.androiddevchallenge.model

/**
 * @author: xuzhiyong
 * @date: 2021/3/2  下午4:27
 * @Email: 18971269648@163.com
 * @description:
 */
sealed class NetResult<out T>{
    data class Success<out T>(val value: T) : NetResult<T>()

    data class Failure(val throwable: Throwable?) : NetResult<Nothing>()
}
inline fun <reified T> NetResult<T>.doSuccess(success: (T) -> Unit) {
    if (this is NetResult.Success) {
        success(value)
    }
}

inline fun <reified T> NetResult<T>.doFailure(failure: (Throwable?) -> Unit) {
    if (this is NetResult.Failure) {
        failure(throwable)
    }
}