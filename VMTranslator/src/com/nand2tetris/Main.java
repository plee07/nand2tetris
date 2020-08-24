package com.nand2tetris;

import com.nand2tetris.impl.CodeWriter;
import com.nand2tetris.impl.Parser;

import java.io.IOException;

public class Main{

    public static void main(String[] args) throws IOException{
        if(args.length != 2) throw new IllegalArgumentException("No arguments were provided");
        String inputFilename = args[0];
        String outputFilename = args[1];
        CodeWriter writer = new CodeWriter(outputFilename, 13, 5, 3);
        Parser parser = new Parser(inputFilename);

        while(parser.hasNext()){
            String commandType = parser.commandType();
            System.out.println(commandType);
            switch(commandType){
                case "C_PUSH":
                    writer.writePush(parser.getArg1(), parser.getArg2());
                    break;
                case "C_POP":
                    writer.writePop(parser.getArg1(), parser.getArg2());
                    break;
                case "C_LABEL":
                    writer.writeLabel(parser.getArg1(), parser.getFullCommand());
                    break;
                case "C_IF":
                    writer.writeIf(parser.getArg1(), parser.getFullCommand());
                    break;
                case "C_GOTO":
                    writer.writeGoto(parser.getArg1(), parser.getFullCommand());
                    break;
                case "C_ARITHMETIC":
                    writer.writeArithmetic(parser.getArg0());
                    break;
                case "C_FUNCTION":
                    writer.writeFunc(parser.getArg1(), parser.getArg2(), parser.getFullCommand());
                    break;
                case "C_RETURN":
                    writer.writeReturn(parser.getFullCommand());
                    break;
            }

            parser.advance();
        }
        writer.close();
    }
}
