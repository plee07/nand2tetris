package com.nand2tetris;

import com.nand2tetris.impl.CodeWriter;
import com.nand2tetris.impl.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException{
        if(args.length != 2) throw new IllegalArgumentException("Two arguments need to be provided");
        String input = args[0];
        String outputFilename = args[1];
        CodeWriter writer = new CodeWriter(outputFilename, 13, 5, 3);
        writer.writeInit();
        writer.writeCall("Sys.init", 0, "// call Sys.init 0");
        if(input.substring(input.length() - 3).equals(".vm")){
            runVm(input, writer);
        }
        else{
            Files.walk(Paths.get(input))
                .filter(Files::isRegularFile)
                .forEach(path -> { // TODO write a function to handle this exception elsewhere, llambda func should be short and simple
                    try{
                        runVm(path.toString(), writer);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                });
        }
        writer.close();
    }

    public static void runVm(String inputFilename, CodeWriter writer) throws IOException{
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
                case "C_CALL":
                    writer.writeCall(parser.getArg1(), parser.getArg2(), parser.getFullCommand());
                    break;
            }

            parser.advance();
        }
    }
}
