package com.nand2tetris.impl;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodeWriter {

    private BufferedWriter writer;
    private Map<String, String> segments;
    public CodeWriter(String filename) throws IOException {
        writer = new BufferedWriter(new FileWriter(filename));
        createSegmentMap();
    }

    public void writeData(String data) throws IOException {
        writer.write(data);
        writer.newLine();
    }

    public void close() throws IOException {
        writer.close();
    }

    private void createSegmentMap(){
        this.segments = new HashMap<>();
        this.segments.put("argument", "@ARG");
        this.segments.put("local", "@LCL");
        this.segments.put("this", "@THIS");
        this.segments.put("that", "@THAT");
        this.segments.put("temp", "@TEMP");
    }

    // Push the value of segment[index] onto the stack
    public void writePush(String arg1, int arg2) throws IOException {
        if(arg1.equals("constant")){
            String output = String.format(
                    "// push constant %d\n" +
                            "@%d\n" +
                            "D=A\n" +
                            "@SP\n" +
                            "AM=M\n" +
                            "M=D\n" +
                            "@SP\n" +
                            "M=M+1"
            , arg2, arg2);
            this.writeData(output);
        } else {
          String output = String.format(
                  "// push %s %d\n" +
                          "@%d\n" +
                          "D=A\n" +
                          "%s\n" +
                          "D=D+M\n" +
                          "@R13\n" +
                          "M=D\n" +
                          "@R13\n" +
                          "A=M\n" +
                          "D=M\n" +
                          "@SP\n" +
                          "A=M\n" +
                          "M=D\n" +
                          "@SP\n" +
                          "M=M+1"
          ,arg1, arg2, arg2, this.segments.get(arg1));
          this.writeData(output);
        }
    }


}


/*
// pop local 0
@LCL
D = M
@0
D = D + A
@R13
M = D
@SP
A = M - 1
D=M
@R13
A=M
M=D
 */