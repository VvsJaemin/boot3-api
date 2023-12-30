package org.jm.apiserver.util;

public class CustomJWTException extends RuntimeException{

    public CustomJWTException(String msg) {
        super(msg);

    }
}
