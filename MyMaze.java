package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class MyMaze {

    private char[][] mapText;
    char workingnMap[][];
    String [] newMazFile = new String[100];
    private int[][] trapsAndLocations = new int[2][100];
    private int round, tStep = 0;
    int trapCounter= 1;
    int trapCount = 0;
    int stepsTaken = 1;
    int rowStart = 1, colStart = 1;
    private int rows, cols;
    private String outputFilename;
    private String inputFilename;

    public MyMaze (String filename) throws IOException {
        try {
            this.outputFilename = filename + ".solved";
            this.inputFilename = filename;
            File map = new java.io.File(filename);
            Scanner myFileScanner = new Scanner(map);
            int mapSide = 0;
            String currentLine;
            currentLine = myFileScanner.nextLine();
            mapSide = currentLine.length();
            this.rows = this.cols = mapSide;
            mapText = new char[mapSide][mapSide];
            mapText[0] = currentLine.toCharArray();
            int linecounter = 1;
            while (myFileScanner.hasNextLine()) {
                currentLine = myFileScanner.nextLine();
                mapText[linecounter] = currentLine.toCharArray();
                linecounter++;
            }
            System.out.print("Done filling the Array \n total of " + this.trapCount + " traps ");
            // Write the map into working map and count the traps
            for (int i = 0; i < mapSide; i++) {
                for (int j = 0; j < mapSide; j++) {
                    if (mapText[i][j] == 'P') {
                        // Hold the position of this round's potential Trap
                        // we will add the step counted of this potential Trap in tStep
                        this.trapsAndLocations[0][this.trapCount] = i*10+j;
                        System.out.println("found a trap " + this.trapsAndLocations[0][this.trapCount]);
                        this.trapCount++;
                    }
                    System.out.print(mapText[i][j]);
                }
                System.out.print("\n");
            }

            System.out.println("found total of " + this.trapCount + " traps ");
            System.out.println("trapCounter is " + this.trapCounter + " before the loop ");
            for (int i = 0; i++ < this.trapCount;) {
                System.out.println("writing the input file #" + i + "!! ");
                System.out.println("trapCounter is " + this.trapCounter + "!! ");
                this.newMazFile[i] = createInputFile(i);
                // this.trapCounter++;
            }
            System.out.println("total trapCount is " + this.trapCount + ":) ");
            for (int i = 0; i++ < this.trapCount;) {
                System.out.println("file name #" + i + " is " + this.newMazFile[i]);
            }
            /* for (int i = 0; i++ < this.trapCount;) {
                this.stepsTaken = 1;
                solve(this.newMazFile[i], this.rowStart, this.colStart);
            } */

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("ERROR : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR : " + e.getMessage());
        }
    }
    private String createInputFile(int trapIndex) throws Exception {

        String filename = this.inputFilename + '_' + trapIndex;
        int tCounter = 0;
        try {
            File file = new File(filename); // creating the output filename
            System.out.print("Writing the file " + filename + " \n");
            PrintWriter writer = new PrintWriter(file);
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.cols; j++) {
                    // System.out.print("writing the char value of " + this.mapText[i][j] + "\n");
                    if (this.mapText[i][j] == 'P') {
                        tCounter++;
                        System.out.print("we have trap #" + tCounter + " in hand.  for a file #" + trapIndex + " \n");
                        if (tCounter == trapIndex) {
                            System.out.print("replacing the char value of " + this.mapText[i][j] + " with space \n");
                            writer.print(' ');
                        } else {
                            System.out.print("replacing the char value of " + this.mapText[i][j] + " with bar \n");
                            writer.print('|');
                        }
                        this.trapCounter++;
                    } else {
                        writer.print(this.mapText[i][j]);
                    }
                }
                writer.println();
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR : " + e.getMessage());
        }
        return filename;
    }
    //assuming all maps are square
    //' ' is where the monster can move
    // '+', '-'. '|' are obstacles
    // we skip outer walls
    //'N' is start position
    //'F' is food
    //'P' is potential trap location
    //x is column, y is row
    //mark the path in the maze via symbol 'S'
    //if x = < 0, or y < 0 or x or y > mapSide (out of bounds) return false

    boolean solveChildMaze(String mazeName, char [][] mapText, int row, int col) {

        char right = mapText[row][col + 1];
        char left = mapText[row][col - 1];
        char up = mapText[row - 1][col];
        char down = mapText[row + 1][col];
        int cstepsTaken = 1;
        String outMaze = new String (mazeName + ".out");
        // System.out.print("Solving for " + mazeName + " starting from " + row + " and " + col + " writing in " +
        //                outMaze +  "\n");
        // System.out.print("Solving for " + row + " and " + col + " \n");
        if (right == 'F' || left == 'F' || up == 'F' || down == 'F') { // we are beside the Exit
            mapText[row][col] = 'S';
            cstepsTaken++;
            System.out.println("Step #" + cstepsTaken);
            return true; // return true once we reach the destination.
        } else {
            System.out.print("Stepping in " + row + " and " + col + " \n");
            mapText[row][col] = 'S';
            cstepsTaken++;
        }

        boolean solved = false;

        if ((right == 'P' || right == ' ') && !solved) {
            solved = solveChildMaze(mazeName, mapText, row, col + 1);
        }
        if ((down == ' ' || down == 'P') && !solved) {
            solved = solveChildMaze(mazeName, mapText,row + 1, col);
        }
        if ((left == ' ' || left == 'P') && !solved) {
            solved = solveChildMaze(mazeName, mapText, row, col - 1);
        }
        if ((up == ' ' || up == 'P') && !solved) {
            solved = solveChildMaze(mazeName, mapText,row - 1, col);
        }
        if (!solved) {
            mapText[row][col] = ' '; // unsolved path are no good. so lets clear our mark.
        }
        return solved;

    }


    boolean solve(String mazeName, int row, int col) {

        char right = this.mapText[row][col + 1];
        char left = this.mapText[row][col - 1];
        char up = this.mapText[row - 1][col];
        char down = this.mapText[row + 1][col];
        String outMaze = new String (mazeName + ".out");
        // System.out.print("Solving for " + mazeName + " starting from " + row + " and " + col + " writing in " +
        //                outMaze +  "\n");
        // System.out.print("Solving for " + row + " and " + col + " \n");
        if (right == 'F' || left == 'F' || up == 'F' || down == 'F') { // we are beside the Exit
            this.mapText[row][col] = 'S';
            this.stepsTaken++;
            System.out.println("Step #" + this.stepsTaken);
            try {
                File file = new File(outMaze); // creating the output filename
                System.out.print("writing the file " + outMaze + " for the maze " + mazeName + " by taking "
                        + this.stepsTaken + " \n");
                PrintWriter writer = new PrintWriter(file);
                for (int i = 0; i < this.rows; i++) {
                    for (int j = 0; j < this.cols; j++) {
                        // System.out.print("writing the char value of " + this.mapText[i][j] + "\n");
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
            this.stepsTaken++;
        }

        boolean solved = false;
/*
 * if (this.mapText[row][col] != 'P') {
   this.mapText[row][col] = 'S'; // we want to keep the potential traps value of 'P'
  }
  */

        if ((right == 'P' || right == ' ') && !solved) {
            solved = solve(mazeName, row, col + 1);
        }
        if ((down == ' ' || down == 'P') && !solved) {
            solved = solve(mazeName, row + 1, col);
        }
        if ((left == ' ' || left == 'P') && !solved) {
            solved = solve(mazeName, row, col - 1);
        }
        if ((up == ' ' || up == 'P') && !solved) {
            solved = solve(mazeName, row - 1, col);
        }
        if (!solved) {
            this.mapText[row][col] = ' '; // unsolved path are no good. so lets clear our mark.
        }
        return solved;   // return false if nothing matches.(execute in one branch)
        // I suppose all solved: boolean will return false and the solve() function will finally return false when
    }       // no path exists. Eventually every path will lead to dead end.

}
