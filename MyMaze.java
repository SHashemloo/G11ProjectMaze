package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class MyMaze {

    private char[][] mapText;
    private char[][] mapTextTemp;
    private int colStart = 1;
    private int rowStart = 1;
    private int rows, cols;
    private String outputFilename;

    public MyMaze (String filename) throws IOException {
        try {
            this.outputFilename = filename + ".solved";
            File map = new java.io.File(filename);
            Scanner myFileScanner = new Scanner(map);
            int mapSide = 0;
            String currentLine;
            currentLine = myFileScanner.nextLine();
            mapSide = currentLine.length();
            this.rows = this.cols = mapSide;
            mapText = new char[mapSide][mapSide];
            mapText[0] = currentLine.toCharArray();
            System.out.println("currentline at 0 is " +  currentLine);
            int linecounter = 1;
            while (myFileScanner.hasNextLine()) {
                currentLine = myFileScanner.nextLine();
                System.out.println("currentline at " + linecounter + " is " +  currentLine);
                mapText[linecounter] = currentLine.toCharArray();
                for (int i = 0; i < mapSide; i++) {
                    System.out.println("mapText at " + linecounter + " and " + i + " is " +  mapText[linecounter][i]);
                }
                linecounter++;
            }
            System.out.print("Done filling the Array \n");
            // make sure our array is correct;
            for (int i = 0; i < mapSide; i++) {
                for (int j = 0; j < mapSide; j++) {
                    System.out.print(mapText[i][j]);
                }
                System.out.print("\n");
            }
            solve(this.rowStart, this.colStart);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("ERROR : " + e.getMessage());
        }
    }

    //assuming all maps are square
    //' ' is where the monster can move
    // '+', '-'. '|' are obstacles
    //'N' is start position
    //'F' is food
    //'P' is potential trap location
    //x is column, y is row
    //mark the path in the maze via symbol 'S'
    //if x = < 0, or y < 0 or x or y > mapSide (out of bounds) return false

    private boolean solve(int row, int col) {
        System.out.print("Solving for " + row + " and " + col + " \n");
        char right = this.mapText[row][col + 1];
        char left = this.mapText[row][col - 1];
        char up = this.mapText[row - 1][col];
        char down = this.mapText[row + 1][col];
        // System.out.print("Solving for " + row + " and " + col + " \n");
        if (right == 'F' || left == 'F' || up == 'F' || down == 'F') { // we are beside the Exit
            this.mapText[row][col] = 'S';
            try {
                File file = new File(this.outputFilename); // creating the output filename
                System.out.print("writing the file " + this.outputFilename + " for the maze " + this.rows + " by "
                        + this.cols + " \n");
                PrintWriter writer = new PrintWriter(file);
                for (int i = 0; i < this.rows; i++) {
                    for (int j = 0; j < this.cols; j++) {
                        System.out.print("writing the char value of " + this.mapText[i][j] + "\n");
                        writer.print(this.mapText[i][j]);
                    }
                    writer.println();
                }
                writer.close();
            } catch (FileNotFoundException e) {
                System.out.println("ERROR : " + e.getMessage());
            }
            return true; // return true once we reach the destination.
        } else {
            System.out.print("Stepping in " + row + " and " + col + " \n");
            this.mapText[row][col] = 'S';
        }

        boolean solved = false;
/* 
 * if (this.mapText[row][col] != 'P') {
   this.mapText[row][col] = 'S'; // we want to keep the potential traps value of 'P' 
  }
  */

        if ((right == 'P' || right == ' ') && !solved) {
            solved = solve(row, col + 1);
        }
        if ((down == ' ' || down == 'P') && !solved) {
            solved = solve(row + 1, col);
        }
        if ((left == ' ' || left == 'P') && !solved) {
            solved = solve(row, col - 1);
        }
        if ((up == ' ' || up == 'P') && !solved) {
            solved = solve(row - 1, col);
        }
        if (!solved) {
            this.mapText[row][col] = ' '; // unsolved path are no good. so lets clear our mark.
        }
        return solved;   // return false if nothing matches.(execute in one branch)
        // I suppose all solved: boolean will return false and the solve() function will finally return false when 
    }       // no path exists. Eventually every path will lead to dead end.
}
