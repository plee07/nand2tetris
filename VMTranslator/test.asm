// push constant 3
@3
D=A
@SP
AM=M
M=D
@SP
M=M+1
// push this 6
@6
D=A
@THIS
D=D+M
@R13
M=D
@R13
A=M
D=M
@SP
A=M
M=D
@SP
M=M+1
// push local 6
@6
D=A
@LCL
D=D+M
@R13
M=D
@R13
A=M
D=M
@SP
A=M
M=D
@SP
M=M+1
