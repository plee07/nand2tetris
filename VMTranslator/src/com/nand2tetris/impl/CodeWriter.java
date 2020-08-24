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
    public void writePush(String arg1, int arg2) throws IOException{
        StringBuilder sb = new StringBuilder();
        switch(arg1){
            case "constant":
                this.writeData(sb
                    .append("// push constant ").append(arg2).append("\n")
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
                    .append("// push ").append(arg1).append(" ").append(arg2).append("\n")
                    .append("@R").append(arg2+temp).append("\n")
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
                    .append("// push ").append(arg1).append(" ").append(arg2).append("\n")
                    .append("@").append(pointer+arg2).append("\n")
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
                    .append("//push static ").append(arg2).append("\n")
                    .append("@").append(name).append("\n")
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
                    .append("// push ").append(arg1).append(" ").append(arg2).append("\n")
                    .append("@").append(arg2).append("\n")
                    .append("D=A\n")
                    .append(this.segments.get(arg1)).append("\n")
                    .append("D=D+M\n")
                    .append("@R").append(this.register).append("\n")
                    .append("M=D\n")
                    .append("@R").append(this.register).append("\n")
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
                    .append("// pop ").append(arg1).append(" ").append(arg2).append("\n")
                    .append("@SP\n")
                    .append("A=M-1\n")
                    .append("D=M\n")
                    .append("@R").append(arg2 + temp).append("\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M-1")
                    .toString());
                break;
            case "pointer":
                this.writeData(sb
                    .append("// pop ").append(arg1).append(" ").append(arg2).append("\n")
                    .append("@SP\n")
                    .append("A=M-1\n")
                    .append("D=M\n")
                    .append("@").append(pointer + arg2).append("\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M-1")
                    .toString());
                break;
            case "static":
                String name = String.format("%s.%d", this.filename, arg2);
                this.writeData(sb
                    .append("//pop static ").append(arg2).append("\n")
                    .append("@SP\n")
                    .append("A=M-1\n")
                    .append("D=M\n")
                    .append("@").append(name).append("\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("M=M-1")
                    .toString());
                break;
            default:
                this.writeData(sb
                    .append("// pop ").append(arg1).append(" ").append(arg2).append("\n")
                    .append(this.segments.get(arg1)).append("\n")
                    .append("D=M\n")
                    .append("@").append(arg2).append("\n")
                    .append("D=D+A\n")
                    .append("@").append(this.register).append("\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("A=M-1\n")
                    .append("D=M\n")
                    .append("@").append(this.register).append("\n")
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

    public void writeLabel(String label, String fullCommand) throws IOException{
        StringBuilder sb = new StringBuilder();
        this.writeData(sb
            .append("// ").append(fullCommand).append("\n")
            .append("(").append(label).append(")")
            .toString());
    }

    public void writeIf(String label, String fullCommand) throws IOException{
        StringBuilder sb = new StringBuilder();
        this.writeData(sb
            .append("// ").append(fullCommand).append("\n")
            .append("@SP\n")
            .append("AM=M-1\n")
            .append("D=M\n")
            .append("@").append(label).append("\n")
            .append("D;JGT")
            .toString());
    }

    public void writeGoto(String label, String fullCommand) throws IOException{
        StringBuilder sb = new StringBuilder();
        this.writeData(sb
            .append("// ").append(fullCommand).append("\n")
            .append("@").append(label).append("\n")
            .append("0;JMP")
            .toString());
    }

    public void writeFunc(String label, int nArgs, String fullCommand) throws IOException{
        String function = "(" + label + ")";
        StringBuilder sb = new StringBuilder();
        this.writeData(sb
            .append("// ").append(fullCommand).append("\n")
            .append(function)
            .toString());

        for(int i = 0; i < nArgs; i++){
            this.writePush("constant", 0);
        }
    }

    public void writeReturn(String fullCommand) throws IOException{
        StringBuilder sb = new StringBuilder();
        this.writeData(sb.append("// ").append(fullCommand).toString());
        this.setFrame();
        this.writePop("argument", 0);
        this.incrArg();
        this.getPrevStack("THAT", 1);
        this.getPrevStack("THIS", 2);
        this.getPrevStack("ARG", 3);
        this.getPrevStack("LCL", 4);
        this.returnCaller();
    }

    private void negNotBuilder(String command, String type, StringBuilder sb) throws IOException{
        this.writeData(sb
            .append("// ").append(command).append("\n")
            .append("@SP\n")
            .append("AM=M-1\n")
            .append("D=").append(type).append("M\n")
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
            .append("// ").append(command).append("\n")
            .append("@SP\n")
            .append("M=M-1\n")
            .append("AM=M\n")
            .append("D=M\n")
            .append("@SP\n")
            .append("M=M-1\n")
            .append("AM=M\n")
            .append("D=M-D\n")
            .append(truth).append("\n")
            .append("D;").append(type).append("\n")
            .append("@SP\n")
            .append("AM=M\n")
            .append("M=0\n")
            .append(end).append("\n")
            .append("0; JMP\n")
            .append(truthAddr).append("\n")
            .append("@SP\n")
            .append("AM=M\n")
            .append("M=-1\n")
            .append(endAddr).append("\n")
            .append("@SP\n")
            .append("M=M+1")
            .toString());
    }

    private void operators(String command, String type, StringBuilder sb) throws IOException{
        this.writeData(sb
            .append("// ").append(command).append("\n")
            .append("@SP\n")
            .append("M=M-1\n")
            .append("AM=M\n")
            .append("D=M\n")
            .append("@SP\n")
            .append("M=M-1\n")
            .append("AM=M\n")
            .append("D=M").append(type).append("D\n")
            .append("@SP\n")
            .append("AM=M\n")
            .append("M=D\n")
            .append("@SP\n")
            .append("M=M+1")
            .toString());
    }

    private void setFrame() throws IOException{
        StringBuilder sb = new StringBuilder();
        this.writeData(sb
            .append("@LCL\n")
            .append("D=M\n")
            .append("@5\n")
            .append("D=D-A\n")
            .append("A=D\n")
            .append("D=M\n")
            .append("@R14\n")
            .append("M=D")
            .toString());
    }

    private void incrArg() throws IOException{
        StringBuilder sb = new StringBuilder();
        this.writeData(sb
            .append("@ARG\n")
            .append("D=M+1\n")
            .append("@SP\n")
            .append("M=D")
            .toString());
    }

    private void getPrevStack(String type, int count) throws IOException{
        StringBuilder sb = new StringBuilder();
        this.writeData(sb
            .append("// ").append(type).append("\n")
            .append("@LCL\n")
            .append("D=M\n")
            .append("@").append(count).append("\n")
            .append("D=D-A\n")
            .append("A=D\n")
            .append("D=M\n")
            .append("@").append(type).append("\n")
            .append("M=D")
            .toString());
    }

    private void returnCaller() throws IOException{
    StringBuilder sb = new StringBuilder();
        this.writeData(sb
            .append("// Return to caller\n")
            .append("@R14\n")
            .append("A=M\n")
            .append("0;JMP")
            .toString());
    }
}
