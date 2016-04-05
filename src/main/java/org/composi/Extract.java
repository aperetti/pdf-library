package org.composi;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Extract {
    @Argument(required = true, usage = "Specify the parent of a .git repository or pass a txt file with a list (carriage return separated) of PDF's absolute paths." )
    private String path;

    @Option(name="-o", aliases = "--output", usage = "Specify output file. File must be [csv] or [txt]. Defaults to specified *homepath*\\output.csv")
    private String outputPath;

    @Option(name="-m", aliases = "--map", usage = "Point to a file which contains a CSV list of name and regex values. Format should be \"name\", \"regex\"." )
    private String mapPath;

    private List<String> fileList;

    boolean pdfFromGit = true;
    private String homePath;

    void run(){
        verifyHomePath();
        try {

            PDFWrapper pdfWrapper;
            CSVPrinter csvPrinter = getCSVPrinter();
            constructFileList();

            for(String filePath : fileList){
                try {
                    pdfWrapper = pdfFromGit ? new PDFWrapper(homePath + filePath) : new PDFWrapper(filePath);
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
            outputPath = homePath + "output.csv";
        }else{
            String fileType = outputPath.split("\\.")[outputPath.split("\\.").length - 1];
            if (!fileType.equals("csv") && !fileType.equals("txt")){
                System.err.println("Ouput file must be *.csv or *.txt");
            }
        }
        file = new File(outputPath);
        file.delete();
        FileOutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter output= new OutputStreamWriter(outputStream);
        return new CSVPrinter(output, CSVFormat.EXCEL);
    }

    private void verifyHomePath() {
        File inputFile = new File(path);
        if (inputFile.isFile()) {
            pdfFromGit = false;
            homePath = inputFile.getParent();
        } else {
            homePath = path.lastIndexOf("\\") == path.length() ? path : path.concat("\\");
            if (!inputFile.exists()) {
                System.err.println("Directory does not exists");
            }
        }
    }

    private void constructFileList() {
        if (pdfFromGit) {
            GitFiles gitFiles = new GitFiles(homePath + ".git");
            fileList = gitFiles.getFiles();
        } else {
            File inputFile = new File(path);
            try {
                InputStream inputStream = new FileInputStream(inputFile);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                CSVParser csvParser = new CSVParser(inputStreamReader,CSVFormat.DEFAULT);
                List<CSVRecord> list = csvParser.getRecords();
                fileList = new ArrayList<String>();
                for (CSVRecord record : list){
                    fileList.add(record.get(0));
                }
            }catch(FileNotFoundException e){
                System.err.println(e.getMessage());
            }catch (IOException e){
                System.err.println(e.getMessage());
            }
        }
    }

    private Map<String, String> getRegexMap(){
        File inputFile = new File(mapPath);
        Map<String, String> map = new HashMap<String, String>();
        try {
            InputStream inputStream = new FileInputStream(inputFile);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            CSVParser csvParser = new CSVParser(inputStreamReader,CSVFormat.DEFAULT);

            fileList = new ArrayList<String>();
            for (CSVRecord record : csvParser){
                map.put(record.get(0),record.get(1));
            }
        }catch(FileNotFoundException e){
            System.err.println(e.getMessage());
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        return map;
    }
}
