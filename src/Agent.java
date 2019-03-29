public class Agent {

    private int[] traj;
    private int score;

    public Agent(int size){
        traj = new int[size];
    }

    public int getTrajSize(){
        return traj.length;
    }

    public void sliceTraj(int pos){
        int[] aux = new int[pos];
        for(int i = 0; i <= pos; i++){
            aux[i] = traj[i];
        }
        traj = aux;
    }

    public void addCommand(int pos, int command){
        traj[pos] = command;
    }

    public int getCommand(int pos){
        return traj[pos];
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return score;
    }

}
