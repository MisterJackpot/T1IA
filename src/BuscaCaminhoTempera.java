import java.util.Random;

public class BuscaCaminhoTempera {
    private int agentSize;
    private Agent agent;
    private int[][] maze;
    private int posXIni, posYIni;
    private double temperatura;


    public BuscaCaminhoTempera(int[][] maze, int posX, int posY, int size){
        this.maze = maze;
        posXIni = posX;
        posYIni = posY;
        agentSize = size;
    }

    public Agent buscaTempera(){
        agent = new Agent(agentSize);
        initAgent();

        return agent;
    }

    private void initAgent() {
        Random r = new Random();
        for(int j = 0; j < agent.getTrajSize(); j++){
            agent.addCommand(j,r.nextInt(4));
        }
    }

}
