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

@16384
D=A
@i
M=D   //i=16384

(LOOP)

@i
D=M
@24575
D=D-A
@END
D;JEQ    //i>24575?

@24576   
D=A
@RAM
A=D+A
D=M      //D=RAM[24576]
@LOOP2
D;JEQ   //if keyboard==0

@i
D=M      //D=i
@RAM
A=D+A    //RAM[i]=1
M=-1    // blacken

@i
M=M+1    //i=i+1

@LOOP
0;JMP

(LOOP2)





(END)

@LOOP   //circle infinite
0;JMP
