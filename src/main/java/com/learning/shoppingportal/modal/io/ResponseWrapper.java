package com.learning.shoppingportal.modal.io;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseWrapper<T> {

    private boolean success;
    private T data;

    private String error;

    public ResponseWrapper(boolean success, T data, String error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }
}
