package com.nand2tetris;

import com.nand2tetris.impl.CodeWriter;
import com.nand2tetris.impl.Parser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	    if(args.length != 2) throw new IllegalArgumentException("No arguments were provided");
        String inputFilename = args[0];
        String outputFilename = args[1];
        CodeWriter writer = new CodeWriter(outputFilename);
        Parser parser = new Parser(inputFilename);

	    while(parser.hasNext()){
	        String commandType = parser.commandType();
            switch(commandType){
                case "C_PUSH":
                    writer.writePush(parser.getArg1(), parser.getArg2());
                    break;
            }

	        parser.advance();
        }
        writer.close();
    }
}
