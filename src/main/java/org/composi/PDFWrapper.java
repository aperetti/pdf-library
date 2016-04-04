package org.composi;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PDFWrapper {
    private String pdfPath;
    private String pdfText;

    /* Constructor.
     * Sets up the PDF and extracts the text.
     * @param pdfPath A string representing the path to the PDF file.
     * @throws IOException If there is an error getting/parsing the document.
     * */
    PDFWrapper(String pdfPath) throws IOException {
        this.pdfPath = pdfPath;
        File file = new File(pdfPath);
        PDFParser parser = new PDFParser(new RandomAccessBufferedFileInputStream(file));
        parser.parse();
        COSDocument cosDocument = parser.getDocument();
        PDDocument pdDocument = new PDDocument(cosDocument);
        this.pdfText = new PDFTextStripper().getText(pdDocument);
        pdDocument.close();
        cosDocument.close();
    }

    /**
     * wrapper for getMatch
     *
     * @param regex a regular expresssion string used to return matches.
     * @param unique a boolean expression used to determine if the result should return only unique values.
     * @param removeFromMatch a regex string to remove from the match. (e.g match = "1234" and removeFromMatch = "2" then result would be "134". Pass null if functionality not needed.
     * @return a string array of the matched regex.
     * */
    String[] matchText(String regex, boolean unique, String removeFromMatch) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pdfText);
        return getMatch(matcher, unique, removeFromMatch);
    }

    /**
     *  Static wrapper function for getMatch
     *
     * @param regex a regular expresssion string used to return matches.
     * @param unique a boolean expression used to determine if the result should return only unique values.
     * @param removeFromMatch a regex string to remove from the match. (e.g match = "1234" and removeFromMatch = "2" then result would be "134". Pass null if functionality not needed.
     * @return a string array of the matched regex.
     * */
    static String[] matchText(String content, String regex, boolean unique, String removeFromMatch) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        return getMatch(matcher, unique, removeFromMatch);
    }

    /**
     * A helper function to return cycle through matches and create a string array.
     * @param matcher a matcher with a compiled pattern.
     * @param unique a boolean expression used to determine if the result should return only unique values.
     * @param removeFromMatch the matched string will be split using the removeFromMatch string and rejoined.
     * @return string array converted from an AbstractCollection
     */
    private static String[] getMatch(Matcher matcher, boolean unique, String  removeFromMatch){
        Collection<String> list = unique ? new HashSet<String>() : new ArrayList<String>();
        while(matcher.find()){
            String match;
            if (removeFromMatch != null){
                match = StringUtils.join(matcher.group().split(removeFromMatch),"");
            } else{
                match = matcher.group();
            }
            list.add(match);
        }
        String[] extractedStrings = new String[list.size()];
        list.toArray(extractedStrings);
        return extractedStrings;
    }

}
