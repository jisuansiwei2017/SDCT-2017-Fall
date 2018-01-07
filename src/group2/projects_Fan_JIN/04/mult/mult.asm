// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.

@2
M = 0

// let R3 = R0
@0
D = M
@3
M = D


// for L0
// let D = R1
@1
D = M
// let D = R1[0]
@1
D = D & A
@L0
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L0)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L1
// let D = R1
@1
D = M
// let D = R1[1]
@2
D = D & A
@L1
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L1)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L2
// let D = R1
@1
D = M
// let D = R1[2]
@4
D = D & A
@L2
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L2)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L3
// let D = R1
@1
D = M
// let D = R1[3]
@8
D = D & A
@L3
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L3)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L4
// let D = R1
@1
D = M
// let D = R1[4]
@16
D = D & A
@L4
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L4)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L5
// let D = R1
@1
D = M
// let D = R1[5]
@32
D = D & A
@L5
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L5)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L6
// let D = R1
@1
D = M
// let D = R1[6]
@64
D = D & A
@L6
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L6)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L7
// let D = R1
@1
D = M
// let D = R1[7]
@128
D = D & A
@L7
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L7)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L8
// let D = R1
@1
D = M
// let D = R1[8]
@256
D = D & A
@L8
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L8)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L9
// let D = R1
@1
D = M
// let D = R1[9]
@512
D = D & A
@L9
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L9)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L10
// let D = R1
@1
D = M
// let D = R1[10]
@1024
D = D & A
@L10
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L10)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L11
// let D = R1
@1
D = M
// let D = R1[11]
@2048
D = D & A
@L11
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L11)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L12
// let D = R1
@1
D = M
// let D = R1[12]
@4096
D = D & A
@L12
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L12)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L13
// let D = R1
@1
D = M
// let D = R1[13]
@8192
D = D & A
@L13
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L13)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L14
// let D = R1
@1
D = M
// let D = R1[14]
@16384
D = D & A
@L14
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L14)
// let R3 *= 2
@3
D = M
@3
M = D + M


// for L15
// let D = R1
@1
D = M
// let D = R1[15]
@32768
D = D & A
@L15
D; JEQ
// let R2 += R3
@3
D = M
@2
M = M + D
(L15)
// let R3 *= 2
@3
D = M
@3
M = D + M


