package com.bflarsen.convert;

import java.util.HashMap;
import java.util.Map;

public class AutoConvert {
    private static Map<String, IAutoConverter> converters;

    public static void init() {
        converters = new HashMap<>();
        addConverter(String.class, Long.class, AutoConvert::String_To_Long);
        addConverter(String.class, Integer.class, AutoConvert::String_To_Integer);
        addConverter(String.class, Boolean.class, AutoConvert::String_To_Boolean);
        addConverter(Integer.class, Boolean.class, AutoConvert::Number_To_Boolean);
        addConverter(Long.class, Boolean.class, AutoConvert::Number_To_Boolean);
        addConverter(Integer.class, Long.class, AutoConvert::Number_To_Long);
        addConverter(Long.class, Integer.class, AutoConvert::Number_To_Integer);
    }

    public static void term() {
        converters = null;
    }

    public static <T> T convert(Object value, Class<T> cls) throws Exception {
        if (value == null) {
            return null;
        }

        Class sourceClass = value.getClass();
        if (cls == value.getClass()) {
            return (T)value;
        }

        String targetClassName = cls.getName();
        String sourceClassName = sourceClass.getName();
        String autoConvertKey = sourceClassName + " to " + targetClassName;
        if (converters.containsKey(autoConvertKey)) {
            return (T)converters.get(autoConvertKey).convert(value);
        }
        else if (cls == String.class) {
            return (T)value.toString();
        }
        else {
            throw new Exception("No AutoConverter defined for " + autoConvertKey);
        }
    }

    public static void addConverter(Class<?> fromClass, Class<?> toClass, IAutoConverter converter) {
        converters.put(fromClass.getName() + " to " + toClass.getName(), converter);
    }

    public static Object String_To_Long(Object value) throws Exception {
        return Long.parseLong((String)value);
    }

    public static Object Number_To_Long(Object value) throws Exception {
        return ((Number)value).longValue();
    }

    public static Object String_To_Integer(Object value) throws Exception {
        return Integer.parseInt((String)value);
    }

    public static Object Number_To_Integer(Object value) throws Exception {
        return ((Number)value).intValue();
    }

    public static Object String_To_Boolean(Object value) throws Exception {
        switch ((String)value) {
            case "true":
            case "TRUE":
            case "True":
                return true;
            case "false":
            case "False":
            case "FALSE":
            case "":
                return false;
            default:
                throw  new Exception ("Unable to convert String_To_Boolean('" + value + "')");
        }
    }

    public static Object Number_To_Boolean(Object value) throws Exception {
        return value.equals(1);
    }
}