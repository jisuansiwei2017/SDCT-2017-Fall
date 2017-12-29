# JackCompiler

This project is my implementation of a compiler that compiles the `Jack` language into `Jack VM` bytecodes. See [Nand2Tetris](http://nand2tetris.org/) for details.

## `JackTokenizer` Module

Run the unit test without IntelliJ IDEA:

```bash
cd <this git repo>
javac -d ./out/shell/ ./src/JackCompiler/*.java
cd ./out/shell/
java JackCompiler.JackTokenizer ../../test/Main.jack
java JackCompiler.JackTokenizer ../../test/Square.jack
java JackCompiler.JackTokenizer ../../test/SquareGame.jack
cd ../../
```

This module implements the `JackTokenizer` in textbook on p.214. It reads a `.jack` file and tokenizes it.

## `JackAnalyzer` Module

Developed using top-down `LL(0)` parser...

Run the unit test without IntelliJ IDEA:

```bash
cd <this git repo>
javac -d ./out/shell/ ./src/JackCompiler/*.java
cd ./out/shell/
java JackCompiler.JackAnalyzer ../../test/Main.jack ../../test/Main.xml
java JackCompiler.JackAnalyzer ../../test/Square.jack ../../test/Square.xml
java JackCompiler.JackAnalyzer ../../test/SquareGame.jack ../../test/SquareGame.xml
cd ../../
```

This module implements the `JackAnalyzer` in textbook on pp.208-209. It reads a `.jack` file, parses it, and output an `.xml` file.

This module is finished, but is still under test.

## `JackCodeGenerator` Module

Developed using recursive-descent. It should be able to generate a `.vm` file from an input `.xml` file.

Run the unit test without IntelliJ IDEA:

```bash
cd <this git repo>
javac -d ./out/shell/ ./src/JackCompiler/*.java
cd ./out/shell/
java JackCompiler.JackCodeGenerator ../../test/Main.jack ../../test/Main.xml ../../test/Main.vm
java JackCompiler.JackCodeGenerator ../../test/Square.jack ../../test/Square.xml ../../test/Square.vm
java JackCompiler.JackCodeGenerator ../../test/SquareGame.jack ../../test/SquareGame.xml ../../test/SquareGame.vm
cd ../../
```

This module is finished, but is still under test.

## `HttpWebApi` Module

You can send your `Jack` code by sending POST to endpoint `/compile` at port `args[0]`. The POST data is the code in text format.

Run the Http Web API server (it listens to port `args[0]`):

```bash
cd <this git repo>
javac -d ./out/shell/ ./src/JackCompiler/*.java
cd ./out/shell/
java JackCompiler.HttpWebApi 3247
cd ../../
```

Alternatively, using the HTTP POST tool at [http://coolaf.com/tool/post](http://coolaf.com/tool/post) (select `raw` -> `text(test/plain)`), you can send your Jack code as POST data:

- to `http://119.28.44.116:3247/compile` to compile it into Jack VM bytecodes, or

- to `http://119.28.44.116:3247/analyze` to parse it into a syntax tree.

## References

[Nand2Tetris Project 10](http://nand2tetris.org/10.php)
[Nand2Tetris Project 11](http://nand2tetris.org/11.php)