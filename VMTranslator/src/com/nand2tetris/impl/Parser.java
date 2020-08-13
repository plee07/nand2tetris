package com.nand2tetris.impl;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
Implementation of this class will be a bit different than what was defined in the book
We will read in a .vm file, parse it line by line and add it to the List
 */
public class Parser {
    private List<String> commands;
    int tracker;

    public Parser(String filename)  {
        this.tracker = 0;
        this.commands = new ArrayList<>();
        this.readData(filename);
    }

    // Read file and add to commands
    private void readData(String filename){
        try{
            File file = new File(filename);
            Scanner reader = new Scanner(file);

            while(reader.hasNextLine()){
                String command = reader.nextLine();
                this.commands.add(command);
            }
            reader.close();
        } catch (FileNotFoundException e){
            System.out.println("File was not found");
        }
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public boolean hasNext(){
        return tracker != commands.size();
    }

    public void advance(){
        tracker++;
    }
    public String getCurrent(){
        return commands.get(tracker);
    }

    /*
    The first String will always be the command
     */
    public String commandType(){
        String current = commands.get(tracker);
        int index = current.indexOf(' ');
        String command = current.substring(0, index);

        switch(command){
            case "add":
            case "sub":
            case "neg":
            case "eq":
            case "gt":
            case "lt":
            case "and":
            case "or":
            case "not":
                return "C_ARITHMETIC";
            case "pop":
                return "C_POP";
            case "push":
                return "C_PUSH";
            default:
                return command;
        }
    }

    public void reset(){
        this.tracker = 0;
    }
}
