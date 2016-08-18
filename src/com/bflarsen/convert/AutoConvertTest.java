package com.bflarsen.convert;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoConvertTest extends junit.framework.TestCase {
    public static class Bean {
        public String StrValue;
        public Long LongValue;
        public Boolean BoolValue;
        public Integer IntValue;
    }

    AutoConvert converter;

    @Before
    public void setUp() {
        converter = new AutoConvert();
    }

    @After
    public void tearDown() {
        converter = null;
    }

    public void test_convert() throws Exception {
        assertEquals((Integer)1, converter.convert("1", Integer.class));
        assertEquals((Boolean)true, converter.convert(1, Boolean.class));
    }

    public void test_fill() throws Exception {
        Bean bean = new Bean();
        Map<String, Object> values = new HashMap<>();
        values.put("StrValue", true);  // convert bool to string
        values.put("LongValue", 987612345); // convert integer to long
        values.put("IntValue", "3"); // convert string to int
        values.put("BoolValue", true); // convert bool to bool
        values.put("noise", "junk");  // it should ignore extra junk

        converter.fill(bean, values);

        assertEquals("true", bean.StrValue);
        assertEquals(987612345L, (long)bean.LongValue);
        assertEquals(3, (int)bean.IntValue);
        assertEquals(true, (boolean)bean.BoolValue);

        // lets try some other combinations
        bean = new Bean();
        bean.IntValue = 5; // make sure this gets overwritten with null
        values = new HashMap<>();
        values.put("StrValue", 5.6D);  // convert bool to string
        values.put("LongValue", "-1"); // convert integer to long
        values.put("IntValue", null); // convert string to int
        values.put("BoolValue", "1"); // convert string to boolean

        converter.fill(bean, values);

        assertEquals("5.6", bean.StrValue);
        assertEquals(-1, (long)bean.LongValue);
        assertEquals(null, bean.IntValue);
        assertEquals(true, (boolean)bean.BoolValue);
    }

    public void test_exceptionHandler() throws Exception {
        final List<Exception> exceptionsThrown = new ArrayList<Exception>();
        converter.ExceptionHandler = (ex) -> exceptionsThrown.add(ex);

        // do something that would throw an exception
        converter.convert("Not a bool", Boolean.class);
        converter.convert("1K2jj2l", Integer.class);

        // verify our handler was called, and the exception didn't bubble up (if it had bubbled, we would have died on that last line.
        assertEquals(2, exceptionsThrown.size());
        assertEquals("Unable to convert 'Not a bool' to instance of 'java.lang.Boolean' in 'AutoConvert.String_To_Boolean'", exceptionsThrown.get(0).getMessage());
        assertEquals("Unable to convert '1K2jj2l' to instance of 'java.lang.Integer' in 'AutoConvert.String_To_Integer' java.lang.NumberFormatException: For input string: \"1K2jj2l\"", exceptionsThrown.get(1).getMessage());
    }
}