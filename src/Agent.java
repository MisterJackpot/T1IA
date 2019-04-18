public class Agent {

    private int[] traj;
    private int score;
    private boolean invalido;
    private boolean chegou;
    private int chegada;
    private int batida;

    public Agent(int size){
        traj = new int[size];
        chegada = -1;
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

    public boolean isInvalido() {
        return invalido;
    }

    public void setInvalido(boolean invalido) {
        this.invalido = invalido;
    }

    public boolean isChegou() {
        return chegou;
    }

    public void setChegou(boolean chegou) {
        this.chegou = chegou;
    }

    public int getChegada() {
        return chegada;
    }

    public void setChegada(int chegada) {
        this.chegada = chegada;
    }

    public int getBatida() {
        return batida;
    }

    public void setBatida(int batida) {
        this.batida = batida;
    }

    public int[] getTraj(){
        return traj;
    }
}
