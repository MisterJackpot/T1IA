import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String args[]){
        File f = new File("assets/maza.txt");
        Parser p = new Parser();
        try {
           int[][] maze =  p.parseFile(f);
           printMaze(maze);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void printMaze(int[][] maze){
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println(" ");
        }
    }
}
