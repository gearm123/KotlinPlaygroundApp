package com.test.funfactsapp.network

data class FunFactApiState<out T>(var status: Status, val data: T?, val message: String?) {

    companion object {

        // In case of Success,set status as
        // Success and data as the response
        fun <T> success(data: T?): FunFactApiState<T> {
            return FunFactApiState(Status.SUCCESS, data, null)
        }

        // In case of failure ,set state to Error ,
        // add the error message,set data to null
        fun <T> error(msg: String): FunFactApiState<T> {
            return FunFactApiState(Status.ERROR, null, msg)
        }

        // When the call is loading set the state
        // as Loading and rest as null
        fun <T> loading(): FunFactApiState<T> {
            return FunFactApiState(Status.LOADING, null, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}