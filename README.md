# UML Parser

## Overview: 
a parser that converts a java source code into a class diagram.

## Tools Used:
**Java Parser:** It takes java file as input and creates token that uses Abstract Syntax Tree to match with the Java Grammar. The parsers main function is to read all the java source code or all the java files in the provided source path, and finally make a grammar that can be interpreted by the UML generator.

**yUML:** This handles the part which generates the final class diagram. The Java Parser creates grammar using which url is created and finally which can be used to create class diagram using yUML.

**Steps For Execution:**
1. Download the jar file
2. Within the terminal, go to the directory where the jar is located
3. Execute the following query:

        java -jar [Name_of_jar] class “Path_of_test_files” [Name_of_output_file]

For instance if we want to run "tests.jar" file and lets say test case location is “/user/lakshaysharma/desktop” and the name of the ouput file we want is “tests”

-> java -jar tests.jar class “/user/lakshaysharma/desktop” tests

Final class diagram will be created and the name of the file will be tests.png
