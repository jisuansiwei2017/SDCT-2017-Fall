// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.
    @R0
	D=M
	@a
	M=D
	@R1
	D=M
	@b
	M=D
	@sum
	M=0
	
(LOOP)
    @b
	D=M
	@END
	D;JLE
	@a
	D=M
	@sum
	M=M+D
	@b
	M=M-1
	@LOOP
	0;JMP
(END)
    @sum
	D=M
	@R2
	M=D
(LOOP2)
    @LOOP2
	0;JMP



