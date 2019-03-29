import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String args[]){
        File f = new File("assets/maza.txt");
        Parser p = new Parser();
        try {
           int[][] maze =  p.parseFile(f);
           printMaze(maze);

            BuscaCaminho busca = new BuscaCaminho(maze,0,0);

            Agent champ = new Agent(36);
            do {
                champ = busca.buscaGenetica(21, 10000);
                System.out.println("Champ:");
                printAgent(champ);
                System.out.println(" Score: " + champ.getScore());
            }while(!verify(champ,maze));

            printSteps(champ,maze);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void printAgent(Agent agent){
        for (int i = 0; i < agent.getTrajSize(); i++) {
            System.out.print(agent.getCommand(i));
        }
    }

    public static void printSteps(Agent agent, int[][] maze){
        int posX = 0;
        int posY = 0;

        for (int i = 0; i < agent.getTrajSize(); i++) {
            maze[posY][posX] = 9;
            switch (agent.getCommand(i)) {
                case 0:
                    posX--;
                    break;
                case 1:
                    posY--;
                    break;
                case 2:
                    posX++;
                    break;
                case 3:
                    posY++;
                    break;
            }


            System.out.println("_-_-__-_-__-_-__-_-__-_-__-_-__-_-__-_-__-_-__-_-__-_-_");
            printMaze(maze);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(maze[posY][posX] == 3){
                System.out.println("WIN");
                break;
            }

        }
    }

    public static boolean verify(Agent agent, int[][] maze) {
        int posX = 0;
        int posY = 0;

        for (int i = 0; i < agent.getTrajSize(); i++) {
            switch (agent.getCommand(i)) {
                case 0:
                    posX--;
                    break;
                case 1:
                    posY--;
                    break;
                case 2:
                    posX++;
                    break;
                case 3:
                    posY++;
                    break;

            }
            if (posX < 0 || posX >= maze[0].length || posY < 0 || posY >= maze.length) {
                return false;
            } else if (maze[posY][posX] == 1) {
                return false;
            } else if (maze[posY][posX] == 3) {
                return true;
            }
        }
        return false;
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
