## This is the folder for TOKENIZER

### Contents

- Test examples from nand2tetris, which are named like "Main.jack", "MainT.xml" ...
- My tokenizer's output, which are named like "Maintokens.xml" ...
- The tokenizer program, which is "tokenizer.l" and its head file "mytokenizer.h"
- And you may find there are many unnecessary functions in the head file because I just copy it from part of the parser head file.  

### Implementation

- I run the program on Mac. I believe you can easily find the environment requirements online. 
- I have Xcode pre-installed (and maybe some Terminal Tools), which is supposed to be sufficient since Lex is basically supported by C. 

And here is what I type in terminal to run the tokenizer (cd to this folder of course):

```
lex tokenizer.l
cc lex.yy.c -ll
./a.out < Inputfile > Outputfile
```

