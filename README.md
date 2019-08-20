# Text File Compare Tool

Java command line tool to compare text file line by line.

August 6, 2019 Hitoshi Ozawa

## Description

Text File Compare Tool is a Java 1.6 tool to compare text files in 2 directories and output differences to specified output file.

It can be used to compare and verify generated files in test environment with those in production environment to make sure they are all the same.

## Requirements

   JDK 1.6 or above

## Setup

1. Depending on whether you're using MS Windows or Linux, execute
    "filecompare.bat" or "filecompare.sh" with appropriate arguments.
     Refer to section 3 for explanation on arguments.
    e.g. ./filecompare.sh -f file1.txt -g file2.txt -o output.txt
    
2. Arguments may be set in a configuration file. file".
     Refer to section 4 for explanation on configuration settings.

## Syntax


```bash
 filecompare [-a] [-b <arg>] [-c <arg>] [-d <arg>] [-e <arg>] [-f
       <arg>] [-g <arg>] [-h] [-i <arg>] [-j <arg>] [-o <arg>] [-q] [-r
       <arg>] [-s <arg>] [-t] [-v]
 -a,--append            append to existing output file
 -b,--buffer <arg>      read ahead buffer size
 -c,--conf <arg>        configuration file to use
 -d,--diffCnt <arg>     max number of differing lines per file
 -e,--encoding <arg>    file encoding
 -f,--file1 <arg>       input file 1
 -g,--file2 <arg>       input file 2
 -h,--help              display this help
 -i,--dir1 <arg>        input dir 1
 -j,--dir2 <arg>        input dir 2
 -o,--output <arg>      output log full path
 -q,--quiet             hide console messages
 -r,--readahead <arg>   number of rows to read ahead to find matching line
 -s,--separator <arg>   output column separator character
 -t,--title             output column header row
 -v,--version           version
```

## Configuration file

Default arguments may be set in properties file.

Values specified by arguments will override values specified in configuration file.

Property Name           | Value
----------------------- | ------------------------------------------------------
append_flg              | append to existing output file
include_header_flg      | output column header row
file1_dir               | input file 1
file2_dir               | input file 2
file1_name              | input file 1
file2_name              | input file 2
output_filename         | output log full path
encoding                | file encoding
read_ahead_line_cnt     | number of rows to read ahead to find matching line
buffer_size             | read ahead buffer size
max_diff_line_cnt       | max number of differing lines per file
column_separator        | output column separator character
max_column_diff_length  | max characters of difference in a line to output
quiet                   | hide console messages

### Example:
```
append_flg=false
include_header_flg=false
#file1_dir=
#file2_dir=
#file1_name=
#file2_name=
#output_filename=
encoding=Shift-JIS
read_ahead_line_cnt=10
buffer_size=1000
max_diff_line_cnt=10
column_separator=,
max_column_diff_length=10
quiet=false
```

## Build locally
Maven2 is required to build and execute tests.
```bash
$ mvn clean package

# To run tests
$ mvn test
```

## Included Files

File name                      | Content
------------------------------ | --------------------------------------------
filecompare.jar                | file compare Java program
imagecropper_linux.properties  | sample configuration file for linux
filecompare_example.properties | sample configuration file
filecompare.sh                 | linux script
filecompare.bat                | MS Windows script
README.md                      | this file
LICENSE                        | license file - Apache License, Version 2.0
NOTICE                         | copyright notice
sample/                        | directory with sample test images 
   
## License

Apache License, Version 2.0
   
## Releases

2019/08/06 v1.0  initial release
   
END
