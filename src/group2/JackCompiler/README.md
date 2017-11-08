# JackCompiler

## ```JackTokenizer``` Module

Run the unit test without IntelliJ IDEA:

```bash
cd <this git repo>
javac -d ./out/production/ ./src/JackCompiler/*.java
cd ./out/production/
java JackCompiler.JackTokenizer ../../test/test.jack
cd ../../
```

This module implements the ```JackTokenizer``` in textbook on p.214. It reads a ```.jack``` file and tokenizes it. 

## ```JackAnalyzer``` Module

Developed using top-down ```LL(0)``` parser...  

Run the unit test without IntelliJ IDEA:

```bash
cd <this git repo>
javac -d ./out/production/ ./src/JackCompiler/*.java
cd ./out/production/
java JackCompiler.JackAnalyzer ../../test/test.jack ../../test/test.xml
cd ../../
```

This module implements the ```JackAnalyzer``` in textbook on pp.208-209. It reads a ```.jack``` file, parses it, and output an ```.xml``` file. 
