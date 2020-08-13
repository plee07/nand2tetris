package com.nand2tetris;

import com.nand2tetris.impl.Parser;

public class Main {

    public static void main(String[] args) {
	    if(args.length == 0) throw new IllegalArgumentException("No arguments were provided");
        String filename = args[0];

        Parser parser = new Parser(filename);
	    while(parser.hasNext()){
	        System.out.println(parser.getCurrent());
	        System.out.println(parser.commandType());
	        parser.advance();
        }
    }
}
