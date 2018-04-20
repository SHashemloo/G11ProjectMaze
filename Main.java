package soroush.hashemloo;;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            System.out.println("File to be read is " + args[0] + "\n");
            MyMaze mazeRunner = new MyMaze(args[0]); // args [0] if you want filename from command line
            System.out.print("Solving map for " + mazeRunner.inputFilename + " starting from " + mazeRunner.rowStart +
                    " writing in " + mazeRunner.colStart +  "\n");
            boolean foundaway = mazeRunner.solve(mazeRunner.inputFilename, mazeRunner.rowStart, mazeRunner.colStart);
            if (foundaway) {
                System.out.println("solved the maze for file: " + mazeRunner.inputFilename);
                System.out.println("File has been output!");
            } else {
                System.out.println("no solution was found for the maze for file: " + mazeRunner.inputFilename);
            }

            /* for (int i = 0; i++ < mazeRunner.trapCount;) {
                System.out.print("Building map for " + mazeRunner.inputFilename + " starting from " + mazeRunner.rowStart +
                        " writing in " + mazeRunner.colStart +  "\n");

               mazeRunner.workingnMap = mazeRunner.fillWorkingMap(mazeRunner.mapSize, mazeRunner.newMazFile[i]);
                mazeRunner.stepsTaken = 1;

               mazeRunner.solveChildMaze(mazeRunner.newMazFile[i], mazeRunner.workingnMap,
                    mazeRunner.rowStart, mazeRunner.colStart);
            */

        } catch (Exception e) {
            System.out.println("ERROR : "+e.getMessage());
            e.printStackTrace();
        }
    }
}
