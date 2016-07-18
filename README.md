# pdf-library
Extract text from PDF files tracked in a text file or git repository and export to a CSV. Text will be extracted specified by regular expression.

###Simple as simple.
The following command will by default extract all email addresses from the PDFs listed in the "pdffiles.txt" file.
```
java -jar pdf-library.jar "pdffiles.txt"
```
Given a text file "pdffiles.txt" with the following data:
```
\full\path\to1.pdf
\full\path\to2.pdf
\full\path\to3.pdf
```
The program will find all occurences of emails (using the regular expression "\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\\b") and output the data to a CSV file with following format.
output.csv
```
\full\path\to1.pdf, email, john.smith@eml.com
\full\path\to2.pdf, email, val1luvscats@hotmail.com
\full\path\to2.pdf, email, hal_2000@gmail.com
\full\path\to3.pdf, email, jjkk@nauk.com
```

###Options

####-o
Specify an outfile. Default is the current directory of the input+output.csv.

####-m
Specify your own mapping file. The mapping file is a comma separated list containing regular expression you would like to extract from the PDF.

For example, provided the file mapping.txt with the following information:
```
"myname", "Adam Peretti"
"phonenumber", "^(?:(?:\+?1\s*(?:[.-]\s*)?)?(?:\(\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\s*\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\s*(?:[.-]\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\s*(?:[.-]\s*)?([0-9]{4})(?:\s*(?:#|x\.?|ext\.?|extension)\s*(\d+))?$"
```

Running 
```
java -jar pdf-library.jar "\path\to\input\text\file.txt" -m "\path\to\mapping.txt"
```

Will extract all unique occurrences of "Adam Peretti" and phones numbers from each of the PDF files. The output will look something like this:
```
\full\path\to1.pdf, myname, Adam Peretti
\full\path\to2.pdf, myname, Adam Peretti
\full\path\to2.pdf, phonenumber, 555-555-5555
\full\path\to3.pdf, phonenumber, 555-5555
\full\path\to3.pdf, phonenumber, 555-5465
```


