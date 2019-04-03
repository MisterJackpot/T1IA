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

    public Agent buscaGenetica(int numeroAgentes, int geracoes){
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
            //System.out.println("Geraçao:" + geracao);

            calculateAllScore(pop);

            getHighlander();
            crossOver();

            if(r.nextInt(101) < percentMutation) {
                if(pop[0].getChegada() > 0){
                    mutate(pop[0].getChegada());
                    this.corte = pop[0].getChegada()/2;
                }
                else mutate(agentSize/2);
            }
            calculateAllScore(popInt);


            //System.out.println("popInt:");
            //printAgents(popInt);

            pop = popInt.clone();

            /*System.out.println("pop:");
            printAgents(pop);*/

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

    public void calculateScore(Agent agent){
        int score = 0;
        int posX = posXIni;
        int posY = posYIni;
        agent.setInvalido(false);
        agent.setChegou(false);

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
            if(posX<0 || posX>=maze[0].length || posY<0 || posY>=maze.length){
                score += agentSize * 10;
                agent.setInvalido(true);
            } else if(maze[posY][posX] == 1){
                score += agentSize * 5;
                agent.setInvalido(true);
            } else if(maze[posY][posX] == 0 || maze[posY][posX] == 2){
                score += 1;
            } else {
                agent.setChegou(true);
                agent.setChegada(i);
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

        for(int i = 1; i < popInt.length; i = i + 2){
            do{
                dad = torneio();
                mom = torneio();
            }while(dad.equals(mom));
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
        for (int i = 0; i < 10; i++) {
            int l = r.nextInt(popInt.length-1)+1;
            for (int j = 0; j < 2; j++) {
                int c = r.nextInt(chegada);

                popInt[l].addCommand(c, r.nextInt(4));
            }

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
