import java.io.*;

public class Parser {

    public int[][] parseFile(File file) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));

        String st;
        int j = 0;
        int tam = Integer.parseInt(in.readLine());
        int[][] maze = new int[tam][tam];
        while ((st = in.readLine()) != null) {
            int l = 0;
            for(int i=0; i<st.length(); i++){
                Character aux = st.charAt(i);
                if(aux == 'E'){
                    maze[j][l] = 2;
                    l++;
                }else if(aux == 'S'){
                    maze[j][l] = 3;
                    l++;
                }else if(aux == '0'){
                    maze[j][l] = 0;
                    l++;
                } else if(aux == '1'){
                    maze[j][l] = 1;
                    l++;
                }
            }
            j++;
        }



        return maze;
    }
}
