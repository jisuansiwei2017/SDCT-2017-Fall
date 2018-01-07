// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

(MAIN_LOOP)

@24576
D = M
@WHITE
D; JEQ
@BLACK
0; JMP

(WHITE)
@16384
D = A
@addr
M = D
(SCREEN_LOOP_WHITE)
@1
D = A
@addr
A = M
M = D
@addr
D = M
M = D + 1
@24576
D = D - A
@LOOP_END
D; JEQ
@SCREEN_LOOP_WHITE
0; JMP

(BLACK)
@16384
D = A
@addr
M = D
(SCREEN_LOOP_WHITE)
@0
D = A
@addr
A = M
M = D
@addr
D = M
M = D + 1
@24576
D = D - A
@LOOP_END
D; JEQ
@SCREEN_LOOP_WHITE
0; JMP


(LOOP_END)
@MAIN_LOOP
0; JMP