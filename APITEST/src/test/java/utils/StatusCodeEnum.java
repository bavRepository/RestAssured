package utils;

public enum StatusCodeEnum {

    OK(200);
    // ... (404 etc.)

    private final int value;

    StatusCodeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}