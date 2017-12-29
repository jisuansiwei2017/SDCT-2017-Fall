## This is the folder for Parser

### Contents

- There are test examples here, with Lex program, which is my parser. 
- And the way to execute the parser is just like that with tokenizer.

### Implementation

*Remember that you may want to make a copy of the provided test examples and do parsing on the copy. Because I find that there may be some problems with the encoding format of the original file.*

### Problem

Sadly, I found that there is a huge problem with implementing the whole using just Lex. I mean, it's possible to pass all the homework because the cases in the homework is finite and we can always adjust our source code to kind of cater to the standard output. But when we are handling more complex and unpredicted input, it will be a disaster. 

Remember you can not handle all the situations unless you actually write all the situations in regular expressions if you are just using Lex. A brute-force way to do it is that you write some very arbitrary regular expressions, so that you get some raw-input as a string and do those subtle distinguishing afterwards. But it's just the same idea with ANTLR and Yacc. 

**So I would rather leave my half-failed work here, so that you can see how it passes test examples but fail when input gets complicated. **

