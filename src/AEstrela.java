import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AEstrela {
	private int[][] maze;
	private int size;
	
	private Nodo start, end;
	private List<Nodo> path, visitados;

	private class Nodo {
		public int x, y;
		Nodo(int y, int x) throws Exception {
			if(x < 0 || x >= size || y < 0 || y >= size)
				throw new Exception("Nodo invalido: x = " +x+ "\n y = "+y);
			this.x = x;
			this.y = y;
		}
		
		@Override
	    public boolean equals(Object obj) {
	        return this.x == ((Nodo) obj).x && this.y == ((Nodo) obj).y;
	    }
	}
	
	public AEstrela(int[][] maze, int posX, int posY, int posXf, int posYf, int size) throws Exception {
		this.maze = maze;
		this.size = size;
		this.start =  new Nodo(posY, posX);
		this.end =  new Nodo(posYf, posXf);
		this.path = new ArrayList<>();
		this.visitados = new ArrayList<>();
	}
	
	public void findPath() throws Exception {
		this.path.add(this.start);
		this.visitados.add(this.start);

		while (!path.get(path.size() - 1).equals(end)) {			
			Nodo next = findNext();
			if (next == null) {
				path.remove(path.size() - 1);
				
				if(path.isEmpty())
					throw new Exception("Caminho impossível");
			} else {
				visitados.add(next);
				path.add(next);
			}
		}
	}
	
	private Nodo findNext() throws Exception {
		Nodo aux = path.get(path.size() - 1);
		
		return getPossibilidades(aux).stream()
				.sorted((n1, n2) -> heuristica(n1.x, n1.y, end.x, end.y) - heuristica(n2.x, n2.y, end.x, end.y))
				.findFirst()
				.orElse(null);
	};
	
	private List<Nodo> getPossibilidades(Nodo atual) throws Exception {
		List<Nodo> possibilidades = new ArrayList<>();
		
		if(atual.x-1 >= 0 && maze[atual.y][atual.x-1] != 1) {//esquerda
			possibilidades.add(new Nodo(atual.y, atual.x-1));
		}
		if(atual.y+1 < size && maze[atual.y+1][atual.x] != 1) {//baixo
			possibilidades.add(new Nodo(atual.y+1, atual.x));
		}
		if(atual.x+1 < size && maze[atual.y][atual.x+1] != 1) {//direita
			possibilidades.add(new Nodo(atual.y, atual.x+1));
		}
		if(atual.y-1 >= 0 && maze[atual.y-1][atual.x] != 1) {//cima
			possibilidades.add(new Nodo(atual.y-1, atual.x));
		}
		
		return possibilidades.stream().filter(p -> !visitados.contains(p)).collect(Collectors.toList());
	}
	
	public int heuristica(int xI,int  yI,int  xF,int  yF) {
		return  Math.abs(yI - yF) + Math.abs(xI - xF);
	}
	
	public void printMazeWithShortestPath() throws Exception{
		for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if(path.contains(new Nodo(i, j))) {
                    System.out.print("X");
                }else if (maze[i][j] == 0 ){
                	System.out.print(" ");
                } else {
                    System.out.print(maze[i][j]);
                }
            }
            System.out.println(" ");
        }
	}
}
