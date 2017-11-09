D// This file is part of www.nand2tetris.org
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


(LOOP1)

    @SCREEN
	D=A
	@n
	M=D
    @KBD
	D=M
	@LOOP3
	D;JEQ

(LOOP2)

    @n
	D=M
	@KBD
	D=D-A
	@END
	D;JGE
    @n
	A=M
	M=-1
	@n
	M=M+1
	@LOOP2
	0;JMP

(LOOP3)

    @n
	D=M
	@KBD
	D=D-A
	@END
	D;JGE
    @n
	A=M
	M=0
	@n
	M=M+1
	@LOOP3
	0;JMP

(END)
    @LOOP1
	0;JMP
	