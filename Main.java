import java.io.IOException;

public class Main {
 public static void main(String[] args) throws IOException{
  try {
   new MyMaze("map1.txt"); // args [0] if you want filename from command line
   System.out.println("File has been output!");
  } catch (Exception e) {
   System.out.println("ERROR : "+e.getMessage());
   e.printStackTrace();
  }
 }
}