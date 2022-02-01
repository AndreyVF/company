package com.company.dto.error;

public class ErrorDetailsDto {

    private String fieldName;

    private Object rejectedValue;

    private String errorCode;

    private ErrorDetailsDto() {
    }

    public ErrorDetailsDto(String fieldName, Object rejectedValue, String errorMessage) {
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
        this.errorCode = errorMessage;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
