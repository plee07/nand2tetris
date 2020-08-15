package com.nand2tetris.impl;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodeWriter {

    private BufferedWriter writer;
    private Map<String, String> segments;
    private int register;
    private int temp;

    public CodeWriter(String filename, int register, int temp) throws IOException {
        writer = new BufferedWriter(new FileWriter(filename));
        this.register = register;
        this.temp = temp;
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
        } else if(arg1.equals("temp")){
            String output = String.format(
                    "// push %s %d\n" +
                            "@R%d\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "A=M\n" +
                            "M=D\n" +
                            "@SP\n" +
                            "M=M+1"
                    ,arg1, arg2, arg2+temp);
            this.writeData(output);
        } else {
          String output = String.format(
                  "// push %s %d\n" +
                          "@%d\n" +
                          "D=A\n" +
                          "%s\n" +
                          "D=D+M\n" +
                          "@R%d\n" +
                          "M=D\n" +
                          "@R%d\n" +
                          "A=M\n" +
                          "D=M\n" +
                          "@SP\n" +
                          "A=M\n" +
                          "M=D\n" +
                          "@SP\n" +
                          "M=M+1"
          ,arg1, arg2, arg2, this.segments.get(arg1),this.register, this.register);
          this.writeData(output);
        }
    }

    public void writePop(String arg1, int arg2) throws IOException {
        if(arg1.equals("temp")){
            String output = String.format(
                    "// pop %s %d\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "D=M\n" +
                            "@R%d\n" +
                            "M=D\n" +
                            "@SP\n" +
                            "M=M-1"
                    ,arg1, arg2, arg2+temp);
            this.writeData(output);
        } else{
            String output = String.format(
                    "// pop %s %d\n" +
                            "%s\n" +
                            "D=M\n" +
                            "@%d\n" +
                            "D=D+A\n" +
                            "@R%d\n" +
                            "M=D\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "D=M\n" +
                            "@R%d\n" +
                            "A=M\n" +
                            "M=D\n" +
                            "@SP\n" +
                            "M=M-1"
                    ,arg1, arg2, this.segments.get(arg1), arg2, this.register, this.register,this.segments.get(arg1));
            this.writeData(output);
        }

    }

    public void writeArithemetic(String command) throws IOException {
        switch(command){
            case "add":
                this.writeData(String.format(
                       "// %s\n" +
                               "@SP\n" +
                               "M=M-1\n" +
                               "AM=M\n" +
                               "D=M\n" +
                               "@SP\n" +
                               "M=M-1\n" +
                               "AM=M\n" +
                               "D=D+M\n" +
                               "@SP\n" +
                               "AM=M\n" +
                               "M=D\n" +
                               "@SP\n" +
                               "M=M+1"
                ,command));
                break;
            case "sub":
                this.writeData(String.format(
                        "// %s\n" +
                                "@SP\n" +
                                "M=M-1\n" +
                                "AM=M\n" +
                                "D=M\n" +
                                "@SP\n" +
                                "M=M-1\n" +
                                "AM=M\n" +
                                "D=M-D\n" +
                                "@SP\n" +
                                "AM=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1"
                ,command));
                break;
        }
    }

}
