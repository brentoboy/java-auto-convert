package com.bflarsen.convert.exceptions;

public class AutoConverterNotRegisteredException extends RuntimeException {
    public AutoConverterNotRegisteredException(Class<?> fromClass, Class<?> toClass) {
        super("No AutoConverter registered for converting from '" + fromClass.getName() + "' to '" + toClass.getName() + "'");
    }
}
