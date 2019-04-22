import java.util.ArrayList;
import java.util.Random;

public class BuscaCaminho {
    private int agentSize;
    private Agent[] pop;
    private Agent[] popInt;
    private int[][] maze;
    private int posXIni, posYIni;
    private int corte;
    private int percentMutation;

    public BuscaCaminho(int[][] maze, int posX, int posY, int corte, int percentMutation, int size){
        this.maze = maze;
        posXIni = posX;
        posYIni = posY;
        this.corte = corte;
        agentSize = size;
        this.percentMutation = percentMutation;
    }

    public Agent buscaGenetica(int numeroAgentes, int geracoes, boolean print){
        pop = new Agent[numeroAgentes];
        popInt = new Agent[numeroAgentes];
        Random r = new Random();

        initPop();

        /*System.out.println("______________________________________________");
        System.out.println("Primeiros Agentes:");
        printAgents(pop);
        System.out.println("______________________________________________");*/

        for (int geracao = 0; geracao <= geracoes; geracao++) {
            for (int i = 0; i < popInt.length; i++) {
                popInt[i] = new Agent(agentSize);
            }


            calculateAllScore(pop);

            getHighlander();
            crossOver();

            if(r.nextInt(101) < percentMutation) {
                if(pop[0].getBatida() != -1){
                    mutate(pop[0].getBatida());
                }
                else mutate(agentSize-1);
            }


            //System.out.println("popInt:");
            //printAgents(popInt);

            pop = popInt.clone();

            if(print) {
                System.out.println("Geraçao:" + geracao);


                System.out.println("pop:");
                System.out.println("Batida: " + pop[0].getBatida());
                System.out.println(" Score: " + pop[0].getScore());
                printAgent(pop[0]);
                System.out.println(" ");
            }

            if(pop[0].isChegou() && !pop[0].isInvalido()){
                break;
            }
        }

        return pop[0];
    }

    public static void printAgent(Agent agent){
        for (int i = 0; i < agent.getTrajSize(); i++) {
            System.out.print(agent.getCommand(i));
        }
    }


    public void printAgents(Agent[] a){
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].getTrajSize(); j++) {
                System.out.print(a[i].getCommand(j));
            }
            System.out.print(" Score: " + a[i].getScore());
            System.out.println(" ");
        }
    }

    public void initPop(){
        Random r = new Random();
        for(int i = 0; i < pop.length; i++){
            Agent aux = new Agent(agentSize);
            pop[i] = aux;
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

    private int calculateScore(Agent test) {
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
                            score += (dup.visitas * 5);
                        }
                    }
                    score += 1;
                }
            } else if(maze[posY][posX] == 3){
                if(!test.isInvalido()) {
                    Geo pos = new Geo();
                    pos.posX = posX;
                    pos.posY = posY;
                    test.setPos(pos);
                    break;
                }
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

        for(int i = 1; i < popInt.length; i = i + 2){
            do{
                dad = torneio();
                mom = torneio();
            }while(dad.getTraj() == mom.getTraj());
            for (int j = 0; j < corte; j++) {
                popInt[i].addCommand(j,dad.getCommand(j));
                popInt[i+1].addCommand(j,mom.getCommand(j));
            }
            for (int j = corte; j < popInt[i].getTrajSize(); j++) {
                popInt[i].addCommand(j,mom.getCommand(j));
                popInt[i+1].addCommand(j,dad.getCommand(j));
            }
        }
    }

    public void mutate(int chegada){
        Random r = new Random();
        for (int i = 0; i < pop.length/2; i++) {
            int l = r.nextInt(popInt.length-1)+1;
            int j = r.nextInt(popInt[l].getTrajSize());
            popInt[l].addCommand(j, r.nextInt(4));


            /*switch (popInt[l].getCommand(c)) {
                case 0:
                    popInt[l].addCommand(c, 1);
                    break;
                case 1:
                    popInt[l].addCommand(c, 2);
                    break;
                case 2:
                    popInt[l].addCommand(c, 3);
                    break;
                case 3:
                    popInt[l].addCommand(c, 0);
                    break;
            }*/
        }
    }


}
