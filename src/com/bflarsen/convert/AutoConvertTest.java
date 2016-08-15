package com.bflarsen.convert;

import org.junit.After;
import org.junit.Before;

public class AutoConvertTest extends junit.framework.TestCase {
    @Before
    public void setUp() {
        AutoConvert.init();
    }

    @After
    public void tearDown() {
        AutoConvert.term();
    }

    public void test() throws Exception {
        assertEquals((Integer)1, AutoConvert.convert("1", Integer.class));
        assertEquals((Boolean)true, AutoConvert.convert(1, Boolean.class));
    }
}