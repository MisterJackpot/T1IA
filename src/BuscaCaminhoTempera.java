import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class BuscaCaminhoTempera {
    private int agentSize;
    private Agent agent;
    private int[][] maze;
    private int posXIni, posYIni;
    private double temperatura;
    private double k;
    private Random r;


    public BuscaCaminhoTempera(int[][] maze, int posX, int posY, int size, double k){
        this.maze = maze;
        posXIni = posX;
        posYIni = posY;
        agentSize = size;
        this.k = k;
        r = new Random();
    }

    public Agent buscaTempera(int geracoes, double temp){
        agent = new Agent(agentSize);
        initAgent();
        int dE;

        for (int i = 0; i < geracoes; i++) {

            temp = k * temp;

            if(temp == 0) break;

            Agent proxAgent = gerarAgent();

            dE = funcaoAptidao(proxAgent) - funcaoAptidao(agent);

            System.out.println("-_-_-_-_-_-_-_");
            System.out.println("Geração: " + i);
            System.out.println("Temperatura: " + temp);
            System.out.println("dE: " + dE);
            System.out.println("Batida: " + agent.getBatida());
            printAgent(agent);
            System.out.println(" Score: " + agent.getScore());
            printAgent(proxAgent);
            System.out.println(" Score: " + proxAgent.getScore());


            if(dE < 0) agent.setTraj(proxAgent.getTraj());
            else if(r.nextDouble() < Math.exp((-dE)/temp)) agent.setTraj(proxAgent.getTraj());
        }

        return agent;
    }

    public static void printAgent(Agent a){
        for (int i = 0; i < a.getTrajSize(); i++) {
            System.out.print(a.getCommand(i));
        }
    }

    private int funcaoAptidao(Agent test) {
        int score = 0;
        test.setScore(score);
        test.setBatida(-1);
        test.setInvalido(false);
        int posX = posXIni;
        int posY = posYIni;
        ArrayList<Geo> aux = test.getPosicoes();

        for (int i = 0; i < test.getTrajSize(); i++) {
            switch (test.getCommand(i)) {
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
                score += 100;
                test.setInvalido(true);
                if(test.getBatida() == -1) test.setBatida(i);
            } else if (maze[posY][posX] == 1) {
                score += 50;
                test.setInvalido(true);
                if(test.getBatida() == -1) test.setBatida(i);
            } else if (maze[posY][posX] == 0 || maze[posY][posX] == 2) {
                if(test.isInvalido()){
                    score += 100;
                }else {
                    if (i > 0) {
                        Geo dup = checkPos(aux,posX,posY);
                        if(dup != null && dup.visitas > 2){
                            score += 50;
                        }
                    }
                    score += 1;
                }
            } else if(maze[posY][posX] == 3){
                Geo pos = new Geo();
                pos.posX = posX;
                pos.posY = posY;
                test.setPos(pos);
                break;
            }

            Geo pos = new Geo();
            pos.posX = posX;
            pos.posY = posY;
            aux.add(pos);
        }

        test.setPosicoes(aux);
        test.setScore(score);

        return score;
    }

    private Geo checkPos(ArrayList<Geo> pos, int posX, int posY){
        for (Geo g: pos) {
            if(g.itsEqual(posX,posY)){
                g.visitas += 1;
                return g;
            }
        }
        return null;
    }

    private Agent gerarAgent() {
        Agent aux = new Agent(agentSize);
        aux.setTraj(agent.getTraj().clone());
        int pos;
        if(agent.getBatida() >= 0){
            pos = agent.getBatida();
        }else {
            pos = r.nextInt(agent.getTrajSize());
        }
        boolean saida = true;
        do {
            int command = r.nextInt(4);
            if (aux.getCommand(pos) != command){
                saida = false;
                aux.addCommand(pos, command);
            }
        }while(saida);
        return aux;
    }

    private void initAgent() {
        Random r = new Random();
        for(int j = 0; j < agent.getTrajSize(); j++){
            agent.addCommand(j,r.nextInt(4));
        }
    }

}
