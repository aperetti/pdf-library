package org.composi;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.eclipse.jgit.util.IO;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Extract {
    @Argument(required = true)
    private String homePath;

    @Option(name="-o", aliases = "--output")
    private String outputPath;

    void run(){
        verifyHomePath();
        try {
            PDFWrapper pdfWrapper;
            CSVPrinter csvPrinter = getCSVPrinter();
            GitFiles gitFiles = new GitFiles(homePath + ".git");
            List<String> fileList = gitFiles.getFiles();
            for(String filePath : fileList){
                try {
                    pdfWrapper = new PDFWrapper(homePath + filePath);
                    for (Map.Entry<String, String> entry : Pud.map.entrySet()) {
                        String[] matches = pdfWrapper.matchText(entry.getValue(),true," ");
                        if (matches.length > 0) {
                            for (String match : matches) {
                                List<String> csvLine = new ArrayList<String>();
                                csvLine.add(filePath);
                                csvLine.add(entry.getKey());
                                csvLine.add(match);
                                csvPrinter.printRecord(csvLine);
                            }
                        }
                    }
                }catch (IOException e){
                    System.out.println(filePath);
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e){
            System.err.println(e.getMessage());
            //TODO: Catch Invalid directory
        }
    }

    private CSVPrinter getCSVPrinter() throws IOException {
        File file;
        if(outputPath == null) {
            file = new File(homePath + "output.csv");
        }else{
            String fileType = outputPath.split("\\.")[outputPath.split("\\.").length - 1];
            if (!fileType.equals("csv") && !fileType.equals("txt")){
                System.err.println("Ouput file must be *.csv or *.txt");
            }
            file = new File(outputPath);
        }
        file.delete();
        FileOutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter output= new OutputStreamWriter(outputStream);
        return new CSVPrinter(output, CSVFormat.EXCEL);
    }

    private void verifyHomePath() {
        homePath = homePath.lastIndexOf("\\") == homePath.length() ? homePath : homePath.concat("\\");
        if (!new File(homePath).exists()) {
            System.err.println("Directory does not exists");
        }
    }
}
