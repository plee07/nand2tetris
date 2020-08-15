// push constant 7
@7
D=A
@SP
AM=M
M=D
@SP
M=M+1
// push constant 8
@8
D=A
@SP
AM=M
M=D
@SP
M=M+1
// add
@SP
M=M-1
AM=M
D=M
@SP
M=M-1
AM=M
D=D+M
@SP
AM=M
M=D
@SP
M=M+1
