package com.Blog_Application.Payload;

public class ApiResponse {

    private String message;
    private boolean success;

    // No-args constructor
    public ApiResponse() {
    }

    // All-args constructor
    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    // getters & setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
