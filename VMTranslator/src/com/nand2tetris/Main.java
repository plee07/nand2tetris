package com.nand2tetris;

import com.nand2tetris.impl.CodeWriter;
import com.nand2tetris.impl.Parser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	    if(args.length == 0) throw new IllegalArgumentException("No arguments were provided");
        String filename = args[0];
        CodeWriter test = new CodeWriter("test3.asm");
        test.writeSomething("Some value here\n");
        test.writeSomething("Another value here\n");
        test.close();
        Parser parser = new Parser(filename);
	    while(parser.hasNext()){
	        parser.advance();
	        System.out.println(parser.getCurrent());
        }
    }
}
