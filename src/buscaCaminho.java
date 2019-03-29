import java.util.Random;

public class buscaCaminho {

    private Agent[] pop;
    private Agent[] popInt;
    private int[][] maze;
    private int posXIni, posYIni;

    public buscaCaminho(int[][] maze,int posX, int posY){
        this.maze = maze;
        posXIni = posX;
        posYIni = posY;
    }



    public void initPop(){
        Random r = new Random();
        for(int i = 0; i < pop.length; i++){
            Agent aux = new Agent(36);
            for(int j = 0; j < pop[i].getTrajSize(); j++){
                pop[i].addCommand(j,r.nextInt(4));
            }
        }
    }

    public void calculateAllScore(Agent[] p){
        for (Agent a:p) {
            calculateScore(a);
        }
    }

    public void calculateScore(Agent agent){
        int score = 0;
        int posX = posXIni;
        int posY = posYIni;
        int chegou = -1;

        for (int i = 0; i < agent.getTrajSize(); i++){
            switch (agent.getCommand(i)){
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
            if(posX<0 || posX>=maze[0].length || posY<0 || posY>maze.length){
                score += 200;
            } else if(maze[posX][posY] == 1){
                score += 200;
            } else if(maze[posX][posY] == 0){
                score += 1;
            } else {
                chegou = i;
                break;
            }
        }

        /*if(chegou > 0){
            agent.sliceTraj(chegou);
        }*/

        agent.setScore(score);

    }

    public void getHighlander(){
        int highlander = 0;
        int menor = pop[0].getScore();

        for(int i = 1; i<pop.length; i++){
            if(pop[i].getScore() < menor){
                menor = pop[i].getScore();
                highlander = i;
            }
        }

        for(int i = 0; i < pop[highlander].getTrajSize(); i++){
            popInt[0] = pop[highlander];
        }
    }

    public Agent torneio(){
        Random r = new Random();
        int l1 = r.nextInt(pop.length);
        int l2 = r.nextInt(pop.length);

        if(pop[l1].getScore() < pop[l2].getScore()){
            return pop[l1];
        }else{
            return pop[l2];
        }
    }

    public void crossOver(){
        Agent dad;
        Agent mom;
        int corte = 18;

        for(int i = 1; i < popInt.length; i = i + 2){
            do{
                dad = torneio();
                mom = torneio();
            }while(dad.equals(mom));

            for (int j = 0; j < corte; j++) {
                popInt[i].addCommand(j,dad.getCommand(j));
                popInt[i+1].addCommand(j,mom.getCommand(j));
            }
            for (int j = corte; j < popInt.length; j++) {
                popInt[i].addCommand(j,mom.getCommand(j));
                popInt[i+1].addCommand(j,dad.getCommand(j));
            }
        }
    }

    public void mutate(){
        Random r = new Random();

        int l = r.nextInt(popInt.length);
        int c = r.nextInt(popInt[l].getTrajSize());

        switch (popInt[l].getCommand(c)){
            case 0:
                popInt[l].addCommand(c,1);
                break;
            case 1:
                popInt[l].addCommand(c,2);
                break;
            case 2:
                popInt[l].addCommand(c,3);
                break;
            case 3:
                popInt[l].addCommand(c,0);
                break;
        }
    }



}
