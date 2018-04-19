package com.company;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            System.out.println("File to be read is " + args[0] + "\n");
            new MyMaze(args[0]); // args [0] if you want filename from command line
            System.out.println("File has been output!");
        } catch (Exception e) {
            System.out.println("ERROR : "+e.getMessage());
            e.printStackTrace();
        }
    }
}
