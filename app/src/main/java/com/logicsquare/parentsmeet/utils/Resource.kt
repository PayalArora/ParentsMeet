package com.logicsquare.parentsmeet.utils

data class Resource<out T>(val status: Status, val data: T?, val message: String?, val isRetryButton : Boolean, val isItSecondPage : Boolean) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null, false, isItSecondPage = false)
        }

        fun <T> error(message: String, data: T? = null, isRetryButton : Boolean = false, isItForSecondPage: Boolean = false): Resource<T> {
            return Resource(Status.ERROR, data, message, isRetryButton, isItForSecondPage)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null, false, isItSecondPage = false)
        }

    }
}