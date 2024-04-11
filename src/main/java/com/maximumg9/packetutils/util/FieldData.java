package com.maximumg9.packetutils.util;

public record FieldData(Class<?> clazz, Object data, String unfriendlyName) {
    @Override
    public String toString() {
        return data + "(" + clazz.getSimpleName() + ")";
    }
}
