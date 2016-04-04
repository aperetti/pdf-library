package org.composi;

import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PDFWrapperTest {
    private PDFWrapper pdfWrapper;

     String file;

    @org.junit.Before
    public void setUp() throws Exception {
        file = PDFWrapperTest.class.getResource("/fw9.pdf").getPath();
        pdfWrapper = new PDFWrapper(file);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        pdfWrapper = null;
    }

    @org.junit.Test
    public void matchTextW9() throws Exception {
        String[] string = pdfWrapper.matchText("Form \\d{4}",true," ");
        assertEquals(string.length, 7);
        assertTrue(Arrays.asList(string).contains("Form2553"));
        assertTrue(Arrays.asList(string).contains("Form1099"));
        assertTrue(Arrays.asList(string).contains("Form8233"));
        assertTrue(Arrays.asList(string).contains("Form1098"));
        assertTrue(Arrays.asList(string).contains("Form1040"));
        assertTrue(Arrays.asList(string).contains("Form8832"));
        assertTrue(Arrays.asList(string).contains("Form1403"));
    }

    @Test
    public void matchTextUnique() throws Exception {
        String[] test = PDFWrapper.matchText("THIS IS A TEST TEST","TEST",true,null);
        assertArrayEquals(new String[]{"TEST"},test);
    }

    @Test
    public void matchTextAll() throws Exception {
        String[] test = PDFWrapper.matchText("THIS IS A TEST TEST","TEST",false,null);
        assertArrayEquals(new String[]{"TEST","TEST"},test);
    }


}