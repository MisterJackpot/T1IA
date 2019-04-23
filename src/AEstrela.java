import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AEstrela {
	private int[][] maze;
	private int size;

	private Nodo start, end;
	private List<Nodo> path;

	private Set<Nodo> closedSet, openSet;
	private HashMap<Nodo, Nodo> cameFrom;
	private HashMap<Nodo, Integer> gScore, fScore;

	private class Nodo {
		public int x, y;

		Nodo(int y, int x) throws Exception {
			if (x < 0 || x >= size || y < 0 || y >= size)
				throw new Exception("Nodo invalido: x = " + x + "\n y = " + y);
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
		this.start = new Nodo(posY, posX);
		this.end = new Nodo(posYf, posXf);
		this.path = new LinkedList<>();

		this.closedSet = new HashSet<>();
		this.openSet = new HashSet<>();
		openSet.add(start);
		this.cameFrom = new HashMap<>();
		this.gScore = new HashMap<>();
		this.gScore.put(start, 0);
		this.fScore = new HashMap<>();
		this.fScore.put(start, heuristica(start));
	}

	public void executa() throws Exception {
		Optional<Nodo> _atual = Optional.empty();
		while (!openSet.isEmpty()) {
			_atual = openSet.stream().sorted((n1, n2) -> fScore.get(n1) - fScore.get(n2)).findFirst();
			Nodo atual = _atual.get();

			Thread.sleep(250);
			System.out.println("_-_-__-_-__-_-__-_-__-_-__-_-__-_-__-_-__-_-__-_-__-_-_");
			try {
				printMazeWithAStarPath(atual);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (atual.equals(end))
				break;

			openSet.remove(atual);
			closedSet.add(atual);

			getVizinhos(atual).stream()
			.filter(v -> !closedSet.stream().anyMatch(c -> c.equals(v)))
			.forEach(v -> {
				Integer tentativeScore = gScore.get(atual) + 1;
				
				if (openSet.stream().noneMatch(o -> o.equals(v))) {
					openSet.add(v);
					
					if(gScore.keySet().stream().noneMatch(k -> k.equals(v)))
						fScore.put(v, Integer.MAX_VALUE);
					
					if(gScore.keySet().stream().noneMatch(k -> k.equals(v)))
						gScore.put(v, Integer.MAX_VALUE);
					
				} else if ( tentativeScore >= gScore.keySet().stream()
						.filter(k -> k.equals(v))
						.map(k -> gScore.get(k))
						.findFirst().orElse(Integer.MAX_VALUE)) {					
					return;
				}
				cameFrom.put(v, atual);
				gScore.put(v, tentativeScore);
				fScore.put(v, tentativeScore + heuristica(v));
			});
		}
		_atual.ifPresent(this::reconstroePassos);
	}

	public void reconstroePassos(Nodo nodo) {
		path.add(nodo);
		while (cameFrom.containsKey(nodo)) {
			nodo = cameFrom.get(nodo);
			path.add(0, nodo);
		}
	}

	private List<Nodo> getVizinhos(Nodo atual) throws Exception {
		List<Nodo> vizinhos = new ArrayList<>();

		if (atual.x - 1 >= 0 && maze[atual.y][atual.x - 1] != 1) {// esquerda
			vizinhos.add(new Nodo(atual.y, atual.x - 1));
		}
		if (atual.y + 1 < size && maze[atual.y + 1][atual.x] != 1) {// baixo
			vizinhos.add(new Nodo(atual.y + 1, atual.x));
		}
		if (atual.x + 1 < size && maze[atual.y][atual.x + 1] != 1) {// direita
			vizinhos.add(new Nodo(atual.y, atual.x + 1));
		}
		if (atual.y - 1 >= 0 && maze[atual.y - 1][atual.x] != 1) {// cima
			vizinhos.add(new Nodo(atual.y - 1, atual.x));
		}

		return vizinhos;
	}

	public int heuristica(Nodo nodo) {
		return Math.abs(nodo.y - end.y) + Math.abs(nodo.x - end.x);
	}

	public void printMazeWithShortestPath() throws Exception {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (path.contains(new Nodo(i, j))) {
					System.out.print("X");
				} else if (maze[i][j] == 0) {
					System.out.print(" ");
				} else {
					System.out.print(maze[i][j]);
				}
			}
			System.out.println(" ");
		}
	}
	public void printMazeWithAStarPath(Nodo n) throws Exception {
		reconstroePassos(n);
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (path.contains(new Nodo(i, j))) {
					System.out.print("X");
				} else if (maze[i][j] == 0) {
					System.out.print(" ");
				} else {
					System.out.print(maze[i][j]);
				}
			}
			System.out.println(" ");
		}
		path = new LinkedList<>();
	}
}
