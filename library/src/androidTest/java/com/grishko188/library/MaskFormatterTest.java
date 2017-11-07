package com.grishko188.library;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class MaskFormatterTest {

    private static final String SIMPLE_NUMBERS = "123456789";
    private static final String SIMPLE_PHONE = "9307920000";
    private static final String FULL_PHONE = "79307920000";

    @Test
    public void test_SimpleFormat() {
        MaskFormatter formatter = MaskFormatter.get().mask("### ### ###");
        assertEquals("123 456 789", formatter.format(SIMPLE_NUMBERS));
    }

    @Test
    public void test_SimplePhoneFormat() {
        MaskFormatter formatter = MaskFormatter.get().mask("+7 (###) ###-##-##");
        assertEquals("+7 (930) 792-00-00", formatter.format(SIMPLE_PHONE));
    }

    @Test
    public void test_IgnoreInputPrefixes() {
        MaskFormatter formatter = MaskFormatter.get().mask("+7 (###) ###-##-##")
                .ignoreInputPrefixes("+7", "7");
        assertEquals("+7 (930) 792-00-00", formatter.format(FULL_PHONE));
    }

    @Test
    public void test_maskPrefix() {
        MaskFormatter formatter = MaskFormatter.get().mask("(###) ###-##-##")
                .ignoreInputPrefixes("+7", "7")
                .maskPrefix("+7 ");
        assertEquals("+7 (930) 792-00-00", formatter.format(FULL_PHONE));
    }

    @Test
    public void test_maskPrefixWithDifferentNumbers() {
        MaskFormatter formatter = MaskFormatter.get().mask("(###) ###-##-##")
                .ignoreInputPrefixes("+7", "7", "+7 ", "7 ", "8")
                .maskPrefix("+7 ");

        assertEquals("+7 (930) 792-00-00", formatter.format(FULL_PHONE));
        assertEquals("+7 (930) 792-00-00", formatter.format("9307920000"));
        assertEquals("+7 (930) 792-00-00", formatter.format("+79307920000"));
        assertEquals("+7 (930) 792-00-00", formatter.format("79307920000"));
        assertEquals("+7 (930) 792-00-00", formatter.format("+9307920000"));
        assertEquals("+7 (930) 792-00-00", formatter.format("7(930)7920000"));
        assertEquals("+7 (930) 792-00-00", formatter.format("7 930 792 00 00"));
        assertEquals("+7 (930) 792-00-00", formatter.format("89307920000"));
    }

    @Test
    public void test_maskPrefixWithClear() {
        MaskFormatter formatter = MaskFormatter.get().mask("(###) ###-##-##")
                .ignoreInputPrefixes("+7", "7")
                .maskPrefix("+7 ");

        String formatted = formatter.format(FULL_PHONE);

        assertEquals("9307920000", formatter.clear(formatted));
        assertEquals("9307920000", formatter.clearStatic(formatted));
    }
}