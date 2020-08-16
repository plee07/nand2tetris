package com.nand2tetris.impl;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodeWriter{

    private BufferedWriter writer;
    private Map<String, String> segments;
    private String filename;
    private int register;
    private int temp;
    private int pointer;
    private int counter;

    public CodeWriter(String filename, int register, int temp, int pointer) throws IOException{
        writer = new BufferedWriter(new FileWriter(filename));
        this.filename = filename.substring(0, filename.indexOf('.'));
        this.register = register;
        this.temp = temp;
        this.pointer = pointer;
        this.counter = 0;
        createSegmentMap();
    }

    public void writeData(String data) throws IOException{
        writer.write(data);
        writer.newLine();
    }

    public void close() throws IOException{
        writer.close();
    }

    private void createSegmentMap(){
        this.segments = new HashMap<>();
        this.segments.put("argument", "@ARG");
        this.segments.put("local", "@LCL");
        this.segments.put("this", "@THIS");
        this.segments.put("that", "@THAT");
    }

    // Push the value of segment[index] onto the stack
    // TODO convert String.format to StringBuilder
    public void writePush(String arg1, int arg2) throws IOException{
        StringBuilder sb = new StringBuilder();
        switch(arg1){
            case "constant":
                this.writeData(sb
                    .append("// push constant " + arg2 + "\n")
                    .append("@" + arg2 + "\n")
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("AM=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1")
                    .toString());
                break;
            case "temp":
                this.writeData(sb
                    .append("// push " + arg1 + " " + arg2 + "\n")
                    .append("@R" + (arg2+temp) + "\n")
                    .append("D=M\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1")
                    .toString());
                break;
            case "pointer":
                this.writeData(sb
                    .append("// push " + arg1 + " " +  arg2 + "\n")
                    .append("@" + (pointer+arg2) + "\n")
                    .append("D=M\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1")
                    .toString());
                break;
            case "static":
                String name = this.filename + "." + arg2;
                this.writeData(sb
                    .append("//push static " + arg2 + "\n")
                    .append("@" + name +"\n")
                    .append("D=M\n")
                    .append("@SP\n")
                    .append("AM=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1")
                    .toString());
                break;
            default:
                this.writeData(sb
                    .append("// push " + arg1 + " " + arg2 +"\n")
                    .append("@" + arg2 + "\n")
                    .append("D=A\n")
                    .append(this.segments.get(arg1) + "\n")
                    .append("D=D+M\n")
                    .append("@R" + this.register +"\n")
                    .append("M=D\n")
                    .append("@R" + this.register +"\n")
                    .append("A=M\n")
                    .append("D=M\n")
                    .append("@SP\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M+1")
                    .toString());
        }
    }

    public void writePop(String arg1, int arg2) throws IOException{
        StringBuilder sb = new StringBuilder();
        switch(arg1){
            case "temp":
                this.writeData(sb
                    .append("// pop " + arg1 + " " + arg2 +"\n")
                    .append("@SP\n")
                    .append("A=M-1\n")
                    .append("D=M\n")
                    .append("@R" + (arg2 + temp) + "\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M-1")
                    .toString());
                break;
            case "pointer":
                this.writeData(sb
                    .append("// pop " + arg1 + " " + arg2 +"\n")
                    .append("@SP\n")
                    .append("A=M-1\n")
                    .append("D=M\n")
                    .append("@" + (pointer + arg2) +"\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M-1")
                    .toString());
                break;
            case "static":
                String name = String.format("%s.%d", this.filename, arg2);
                this.writeData(sb
                    .append("//pop static " + arg2 + "\n")
                    .append("@SP\n")
                    .append("A=M-1\n")
                    .append("D=M\n")
                    .append("@" + name + "\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M-1")
                    .toString());
                break;
            default:
                this.writeData(sb
                    .append("// pop " + arg1 + " " + arg2 +"\n")
                    .append(this.segments.get(arg1) + "\n")
                    .append("D=M\n")
                    .append("@" + arg2 + "\n")
                    .append("D=D+A\n")
                    .append("@" + this.register + "\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("A=M-1\n")
                    .append("D=M\n")
                    .append("@" + this.register + "\n")
                    .append("A=M\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M-1")
                    .toString());
        }
    }

    public void writeArithmetic(String command) throws IOException{
        StringBuilder sb = new StringBuilder();
        switch(command){
            case "add":
                operators(command, "+", sb);
                break;
            case "sub":
                operators(command, "-", sb);
                break;
            case "and":
                operators(command, "&", sb);
                break;
            case "or":
                operators(command, "|", sb);
                break;
            case "eq":
                comparisonOperatorBuilder(command, "JEQ", sb);
                break;
            case "gt":
                comparisonOperatorBuilder(command, "JGT", sb);
                break;
            case "lt":
                comparisonOperatorBuilder(command, "JLT", sb);
                break;
            case "neg":
                negNotBuilder(command, "-", sb);
                break;
            case "not":
                negNotBuilder(command, "!", sb);
                break;
        }
    }

    private void negNotBuilder(String command, String type, StringBuilder sb) throws IOException{
        this.writeData(sb
            .append("// " + command + "\n")
            .append("@SP\n")
            .append("AM=M-1\n")
            .append("D=" + type +"M\n")
            .append("M=D\n")
            .append("@SP\n")
            .append("M=M+1")
            .toString());
    }

    private void comparisonOperatorBuilder(String command, String type, StringBuilder sb) throws IOException{
        String truth = "@TRUTH" + counter;
        String truthAddr = "(TRUTH" + counter + ")";
        String end = "@END" + counter;
        String endAddr = "(END" + counter + ")";
        counter++;
        this.writeData(sb
            .append("// " + command + "\n")
            .append("@SP\n")
            .append("M=M-1\n")
            .append("AM=M\n")
            .append("D=M\n")
            .append("@SP\n")
            .append("M=M-1\n")
            .append("AM=M\n")
            .append("D=M-D\n")
            .append(truth + "\n")
            .append("D; " + type + "\n")
            .append("@SP\n")
            .append("AM=M\n")
            .append("M=0\n")
            .append(end + "\n")
            .append("0; JMP\n")
            .append(truthAddr + "\n")
            .append("@SP\n")
            .append("AM=M\n")
            .append("M=-1\n")
            .append(endAddr + "\n")
            .append("@SP\n")
            .append("M=M+1")
            .toString());
    }

    private void operators(String command, String type, StringBuilder sb) throws IOException{
        this.writeData(sb
            .append("// " + command + "\n")
            .append("@SP\n")
            .append("M=M-1\n")
            .append("AM=M\n")
            .append("D=M\n")
            .append("@SP\n")
            .append("M=M-1\n")
            .append("AM=M\n")
            .append("D=M" + type + "D\n")
            .append("@SP\n")
            .append("AM=M\n")
            .append("M=D\n")
            .append("@SP\n")
            .append("M=M+1")
            .toString());
    }
}
