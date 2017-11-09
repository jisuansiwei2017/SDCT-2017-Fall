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
(DRAWWHITE)
@16384
D=A
@i
M=D
(LOOPWHITE)
@24576
D=D-A
@ENDWHITE
D;JGE
@i
A=M
M=0
@i
MD=M+1
@LOOPWHITE
0;JMP
(ENDWHITE)
@scflag
M=0

(LISTENING)
@24576
D=M
@JUDGEFLAG
D;JNE
@scflag
D=M
@DRAWWHITE
D;JNE
@LISTENING
0;JMP

(DRAWBLACK)
@16384
D=A
@i
M=D
(LOOPBLACK)
@24576
D=D-A
@ENDBLACK
D;JGE
@i
A=M
M=-1
@i
MD=M+1
@LOOPBLACK
0;JMP
(ENDBLACK)
@scflag
M=1

(JUDGEFLAG)
@scflag
D=M
@DRAWBLACK
D;JEQ
@LISTENING
0;JMP