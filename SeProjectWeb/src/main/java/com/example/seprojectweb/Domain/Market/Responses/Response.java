package com.example.seprojectweb.Domain.Market.Responses;

public class Response<T> {
    private final String ErrorMessage;
    private T value;

    public Response(String errorMessage) {
        this.ErrorMessage = errorMessage;
    }

    public Response(T value) {
        this.value = value;
        ErrorMessage = null;
    }

    public T getValue() {
        return value;
    }

    public boolean isErrorOccurred() {
        return ErrorMessage != null;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }
}
