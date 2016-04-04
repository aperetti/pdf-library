package org.composi;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PDFWrapperTest {
    private PDFWrapper pdfWrapper;

    @org.junit.Before
    public void setUp() throws Exception {
        pdfWrapper = new PDFWrapper("C:\\Users\\peretti\\projects\\standards_directory\\CU\\D\\D0505SC\\D0505SC.pdf");
    }

    @org.junit.After
    public void tearDown() throws Exception {
        pdfWrapper = null;
    }

    @org.junit.Test
    public void matchTextCU() throws Exception {
        String[] string = pdfWrapper.matchText("[A-Za-z]{2}\\d{2}[A-Za-z]+|[A-Za-z]{1,2}\\d{4}[A-Za-z]{0,3}",true," ");
        assertEquals(string.length, 6);
        assertTrue(Arrays.asList(string).contains("D0505INT"));
        assertTrue(Arrays.asList(string).contains("D0505SC"));
        assertTrue(Arrays.asList(string).contains("D0505"));
        assertTrue(Arrays.asList(string).contains("D0506SC"));
        assertTrue(Arrays.asList(string).contains("N0110"));
        assertTrue(Arrays.asList(string).contains("N0110"));
    }

    @org.junit.Test
    public void matchTextAS() throws Exception {
        String[] string = pdfWrapper.matchText("(?:12[FVU]|1U|1L|1J|115[FHPVW])\\d{3}(?:A|STL)?",true," ");
        assertEquals(string.length, 1);
        assertTrue(Arrays.asList(string).contains("12F713"));
    }

    @org.junit.Test
    public void matchTextTD() throws Exception {
        String[] string = pdfWrapper.matchText("4 ?- ?\\d+ ?- ?\\d+\\.\\d+",true," ");
        assertEquals(string.length, 0);
    }

    @org.junit.Test
    public void matchTextMat() throws Exception {
        String[] string = pdfWrapper.matchText("(?<=\\s)\\d{6,7}(?=[\\s\\.])",true," ");
        assertEquals(string.length, 9);
        assertTrue(Arrays.asList(string).contains("124214"));
        assertTrue(Arrays.asList(string).contains("124222"));
        assertTrue(Arrays.asList(string).contains("124602"));
        assertTrue(Arrays.asList(string).contains("211524"));
        assertTrue(Arrays.asList(string).contains("412693"));
        assertTrue(Arrays.asList(string).contains("506743"));
        assertTrue(Arrays.asList(string).contains("910019"));
        assertTrue(Arrays.asList(string).contains("1001443"));
        assertTrue(Arrays.asList(string).contains("1001445"));
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