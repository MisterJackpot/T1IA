import java.io.File;
import java.io.IOException;

public class Main {
    public static final int posx = 0;
    public static final int posy = 0;

    public static void main(String args[]){
        File f = new File("assets/maza.txt");
        Parser p = new Parser();
        try {
           int[][] maze =  p.parseFile(f);
           printMaze(maze);

           int numberZeros = 0;

            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    if(maze[i][j] == 0) numberZeros++;
                }

            }

            BuscaCaminho busca = new BuscaCaminho(maze,posx,posy, 20, 80,numberZeros);

            Agent champ = new Agent(36);
            do {
                champ = busca.buscaGenetica(51, 100000);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("Champ:");
                printAgent(champ);
                System.out.println(" Score: " + champ.getScore());
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
        int posX = posx;
        int posY = posy;

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
            System.out.println("Passo: " + i);
            printMaze(maze);
            try {
                Thread.sleep(1500);
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
        int posX = posx;
        int posY = posy;

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
                if(maze[i][j] == 0) {
                    System.out.print(" ");
                }else{
                        System.out.print(maze[i][j]);
                }
            }
            System.out.println(" ");
        }
    }
}
