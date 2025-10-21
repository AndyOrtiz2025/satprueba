// ApiResponse.java
package com.gestor.util;

public class ApiResponse<T> {
    private boolean ok;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(T data){
        ApiResponse<T> r = new ApiResponse<>();
        r.ok = true; r.data = data; r.message = "OK";
        return r;
    }
    public static <T> ApiResponse<T> ok(String msg, T data){
        ApiResponse<T> r = new ApiResponse<>();
        r.ok = true; r.data = data; r.message = msg;
        return r;
    }
    public static <T> ApiResponse<T> error(String msg){
        ApiResponse<T> r = new ApiResponse<>();
        r.ok = false; r.message = msg;
        return r;
    }

    public boolean isOk(){ return ok; }
    public void setOk(boolean ok){ this.ok = ok; }
    public String getMessage(){ return message; }
    public void setMessage(String message){ this.message = message; }
    public T getData(){ return data; }
    public void setData(T data){ this.data = data; }
}
