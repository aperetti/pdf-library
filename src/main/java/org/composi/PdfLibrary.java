package org.composi;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class PdfLibrary {
    public static void main(String[] args) {
        Extract extract = new Extract();
        CmdLineParser parser = new CmdLineParser(extract);
        try {
            parser.parseArgument(args);
            extract.run();
        } catch (CmdLineException e){
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
    }
}
