package com.example.vigoshorts.search;

public interface ResponseListener<T> {
    void onSuccess(T response);
    void onError(String errorMessage);
}

