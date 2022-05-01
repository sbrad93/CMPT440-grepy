### @author Shannon Brady
### @course CMPT440 - Formal Languages & Computability
<br>

# Grepy
An application that searches files for regular expression pattern matches and produces DOT graph file output for the automata used in the matching computation.

## Usage
```
~/grepy$ java -jar target/grepy-0.0.2-SNAPSHOT-jar-with-dependencies.jar -h
grepy
Version: 0.0.2
usage: grepy
 -h   Display this help text
 -v   Verbose mode
```

## Building
[Apache Maven](https://maven.apache.org/) is the build tool of choice.  Simply issue `mvn install package` to install dependencies and build the jar.  

Use `mvn clean install compile verify test package site` to clean up existing targets, install dependencies, verify, run unit tests, build a jar, and create the documentation and reports.

## Running
Issue a `java -jar target/grepy-0.0.2-SNAPSHOT-jar-with-dependencies.jar` to run the target directly.

## Testing
[JUnit](https://junit.org/junit5/) is used as the Test Framework. To execute unit tests run `mvn test`.  
To skip tests, add `-Dmaven.test.skip=true` as an argument to your Maven command.

## Overview
1. Regular Expression Manipulation
    * In order to complete automata calculations, the input regex must be prepared accordingly. The following functions achieve this aim:
    <br>

        * RegexReader.read: reads first line of input file and builds alphabet
        * RegexReader.addConcat: inserts '.' to indicate concatenation
        * RegexReader.orderRegex: reorders regex string to match operator precedence
    <br>
    <br>
2. Regular expression to NFA conversion
    * The modified regex is then converted into an NFA. This is acheived with the following functions:
    <br>

        * RegexReader.createNFA: foundational function used to convert regex to NFA, utilizes several helper methods, including:
            * RegexReader.addSymbol: creates NFA transition for input symbols
            * RegexReader.compute: switch statement based on regex operation (kleene, union, or concatenation)
            * RegexReader.union: union operation in Thompson's algorithm
            * RegexReader.concat: concatenation operation in Thompson's algorithm
            * RegexReader.kleene: kleene star operation in Thompson's algorithm
    <br>
    <br>

3. NFA to DFA conversion
    * The previously created NFA is then converted into a DFA. This aim is achieved with the following functions:
    <br>

        * RegexReader.createDFA: foundational function that converts and NFA into a DFA
    <br>
    <br>

4. Expression validation using DFA
    * The previously created DFA is then used to validate various strings included in the initial input file. Accepted strings are printed in the terminal as well as to standard file output. The associated file name is accepted_strings.txt. Such is achieved by the following functions:
    <br>

        * RegexReader.validate: checks if a given string is accepted by the DFA

    <br>
    <br>


5. DOT Output of NFA and DFA
    * DOT graph file output is produced for both the NFA and DFA. They can be found in files nfa.txt and dfa.txt.
    
## Implementation Notes
1. Grouping symbols "()" are not supported in this version. Precendence is solely configured by RegexReader.orderRegex, which takes on the following format: [kleene operations][individual input symbols][union operations]
    * ex. a\*b\*ababa+b
    * ex. a*ba+b
<br>
<br>

2. Union operations require two input symbols. 
    * ex. a+b, not a+ or b+
<br>
<br>

3. Input Files must take on the following format:
    1. target regex
    2. test string
    3. test string
    4. test string
    5. etc...