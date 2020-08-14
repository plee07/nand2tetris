package com.nand2tetris.impl;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {
    BufferedWriter writer;

    public CodeWriter(String filename) throws IOException {
        writer = new BufferedWriter(new FileWriter(filename));
    }

    public void writeSomething(String data) throws IOException {
        writer.write(data);
    }

    public void close() throws IOException {
        writer.close();
    }

}
