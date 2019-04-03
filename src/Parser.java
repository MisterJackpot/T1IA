import java.io.*;

public class Parser {

    public int[][] parseFile(File file) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));

        String st;
        int j = 0;
        int[][] maze = new int[12][12];
        while ((st = in.readLine()) != null) {
            for(int i=0; i<st.length(); i++){
                maze[j][i] = Character.getNumericValue(st.charAt(i));
            }
            j++;
        }



        return maze;
    }
}
