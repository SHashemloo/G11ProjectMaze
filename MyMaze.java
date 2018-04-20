package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class MyMaze {

    private char[][] mapText;
    char workingnMap[][];
    char solution[][];

    String [] newMazFile = new String[100];
    private int[][] trapsAndLocations = new int[2][100];
    private int round, tStep = 0;
    int trapCounter= 1;
    int trapCount = 0;
    int stepsTaken = 1;
    int rowStart = 1, colStart = 1;
    private int rows, cols;
    int mapSize = 0;
    String outputFilename;
    String inputFilename;

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
            solution = new char[mapSide][mapSide];
            mapText[0] = currentLine.toCharArray();
            solution[0] = currentLine.toCharArray();
            int linecounter = 1;

            // fill the internal 2d array with the original file

            while (myFileScanner.hasNextLine()) {
                currentLine = myFileScanner.nextLine();
                mapText[linecounter] = currentLine.toCharArray();
                solution[linecounter] = currentLine.toCharArray();

                linecounter++;
            }
            System.out.print("Done filling the Array \n total of " + this.trapCount + " traps ");
            // Write the map into main map and count the traps
            for (int i = 0; i < mapSide; i++) {
                for (int j = 0; j < mapSide; j++) {
                    if (mapText[i][j] == 'P') {
                        // Hold the position of this round's potential Trap
                        // we will add the step counted of this potential Trap in tStep
                        this.trapsAndLocations[0][this.trapCount] = i*10000+j;
                        System.out.println("found a trap " + this.trapsAndLocations[0][this.trapCount]);
                        this.trapCount++;
                    }
                    System.out.print(mapText[i][j]);
                }
                System.out.print("\n");
            }

            this.mapSize = mapSide;

            System.out.println("found total of " + this.trapCount + " traps ");
            System.out.println("trapCounter is " + this.trapCounter + " before the loop ");
        
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

    char [][] fillWorkingMap (int mapSide, String filename) {
        try {
            File map = new java.io.File(filename);
            Scanner myFileScanner = new Scanner(map);
            String currentLine;
            currentLine = myFileScanner.nextLine();
            workingnMap = new char[mapSide][mapSide];
            workingnMap[0] = currentLine.toCharArray();
            int lineno = 1;
        
            // Write the map into main map and count the traps
            while (myFileScanner.hasNextLine()) {
                currentLine = myFileScanner.nextLine();
                workingnMap[lineno] = currentLine.toCharArray();
                lineno++;
            }
            System.out.print("dumping the working Map\n");
            for (int i = 0; i < mapSide; i++) {
                for (int j = 0; j < mapSide; j++) {
                    System.out.print(mapText[i][j]);
                }
                System.out.print("\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("ERROR : " + e.getMessage());
        }
        return workingnMap;
    }

    boolean solve(String mazeName, int row, int col) {

        char right = this.mapText[row][col + 1];
        char left = this.mapText[row][col - 1];
        char up = this.mapText[row - 1][col];
        char down = this.mapText[row + 1][col];
        String outMaze = new String();
        String solutionFile = new String();
        solutionFile =  "solution.txt";
        // File fileSol;
        // PrintWriter writerSol;
        outMaze = mazeName + ".out";
        // System.out.print("Solving for " + mazeName + " starting from " + row + " and " + col + " writing in " +
        //             outMaze +  "\n");
        // System.out.print("Solving for " + row + " and " + col + " \n");
        if (right == 'F' || left == 'F' || up == 'F' || down == 'F') { // we are beside the Exit
            this.mapText[row][col] = 'S';
            this.solution[row][col] = ' ';
            this.stepsTaken++;
            System.out.println("Step #" + this.stepsTaken);
            try {
                File file = new File(outMaze); // creating the output filename
                System.out.print("writing the file " + outMaze + " for the maze " + mazeName + " by taking "
                        + this.stepsTaken + " \n");
                PrintWriter writer = new PrintWriter(file);
                for (int i = 0; i < this.rows; i++) {
                    for (int j = 0; j < this.cols; j++) {
                        writer.print(this.mapText[i][j]);
                    }
                    writer.println();
                }
                writer.close();

                File fileSol = new File(solutionFile);  // creating the output filename
                System.out.print("writing the file " + solutionFile + " for the maze " + mazeName + " by taking "
                        + this.stepsTaken + " \n");
                PrintWriter writerSol = new PrintWriter(fileSol);
                for (int i = 0; i < this.rows; i++) {
                    for (int j = 0; j < this.cols; j++) {
                        System.out.print(this.solution[i][j]);
                        writerSol.print(this.solution[i][j]);
                    }
                    System.out.println();
                    writerSol.println();
                }
                writerSol.close();
            } catch (FileNotFoundException e) {
                System.out.println("ERROR : " + e.getMessage());
            }
            return true; // return true once we reach the destination.
        } else {
            // System.out.print("Stepping in " + row + " and " + col + " \n");
            if (this.mapText[row][col] == 'P') {
                this.mapText[row][col] = 'T';
                this.solution[row][col] = 'T';
            } else {
                this.mapText[row][col] = 'S';
                this.solution[row][col] = ' ';
            }
            this.stepsTaken++;
        }

        boolean solved = false;

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
            this.solution[row][col] = ' ';
        }
        return solved;
    }
}
