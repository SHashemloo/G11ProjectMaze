package com.company;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            System.out.println("File to be read is " + args[0] + "\n");
            MyMaze mazeRunner = new MyMaze(args[0]); // args [0] if you want filename from command line
            for (int i = 0; i++ < mazeRunner.trapCount;) {
                System.out.print("Solving for " + mazeRunner.newMazFile[i] + " starting from " + mazeRunner.rowStart +
                        " writing in " + mazeRunner.colStart +  "\n");
                mazeRunner.stepsTaken = 1;
                mazeRunner.solve(mazeRunner.newMazFile[i], mazeRunner.rowStart, mazeRunner.colStart);
                System.out.println("solved the maze for file: " + mazeRunner.newMazFile[i]);
            }
            System.out.println("File has been output!");
        } catch (Exception e) {
            System.out.println("ERROR : "+e.getMessage());
            e.printStackTrace();
        }
    }
}
