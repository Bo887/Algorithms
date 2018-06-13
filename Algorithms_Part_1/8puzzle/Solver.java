import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;
import java.util.Comparator;

public class Solver{

	private class SearchNode{
		
		Board board;
		int moves;
		SearchNode prev;

		public SearchNode(Board board, int moves, SearchNode prev){
			this.board = board;
			this.moves = moves;
			this.prev = prev;
		}
	}

	private class Hamming implements Comparator<SearchNode>{
		
		@Override
		public int compare(SearchNode n1, SearchNode n2){
			int n1dist = n1.board.hamming() + n1.moves;
			int n2dist = n2.board.hamming() + n2.moves;
			return n1dist-n2dist;
		}
	}

	private class Manhattan implements Comparator<SearchNode>{

		@Override
		public int compare(SearchNode n1, SearchNode n2){
			int n1dist = n1.board.manhattan() + n1.moves;
			int n2dist = n2.board.manhattan() + n2.moves;
			return n1dist-n2dist;
		}
	}

	private MinPQ<SearchNode> pq;
	private MinPQ<SearchNode> twin;
	private boolean solvable;
	private SearchNode finalBoard;
	private int moves;
	
	public Solver(Board initial){
		
		if (initial == null) throw new IllegalArgumentException();

		pq = new MinPQ<SearchNode>(new Manhattan());
		twin = new MinPQ<SearchNode>(new Manhattan());

		pq.insert(new SearchNode(initial, 0, null));
		twin.insert(new SearchNode(initial.twin(), 0, null));

		while(!pq.isEmpty() && !twin.isEmpty()){

			SearchNode min = pq.delMin();	
			if (min.board.isGoal()){
				solvable = true;
				finalBoard = min;
				moves = finalBoard.moves;
				break;
			}

			SearchNode twinMin = twin.delMin();
			if (twinMin.board.isGoal()){
				solvable = false;
				break;
			}

			Iterable<Board> neighbors = min.board.neighbors();
			for(Board b : neighbors){
				if (min.prev == null || !b.equals(min.prev.board)){
					pq.insert(new SearchNode(b, min.moves+1, min));
				}
			}
			
			Iterable<Board> twinNeighbors = twinMin.board.neighbors();
			for(Board b : twinNeighbors){
				if (twinMin.prev == null || !b.equals(twinMin.prev.board)){
					twin.insert(new SearchNode(b, twinMin.moves+1, twinMin));
				}
			}
		}
	}

	public boolean isSolvable(){
		return solvable;
	}

	public int moves(){
		return isSolvable() ? this.moves : -1;
	}

	public Iterable<Board> solution(){
		if (!isSolvable()) return null;
		ArrayList<Board> moves = new ArrayList<Board>();
		moves.add(finalBoard.board);
		while(finalBoard.prev != null){
			moves.add(finalBoard.prev.board);
			finalBoard = finalBoard.prev;
		}
		return moves;
	}

	public static void main(String[] args){
		int[][] blocks = {
			{1,8,3},
			{4,0,2},
			{7,6,5},
		};
		int[][] solved = {
			{1,2,3},
			{4,5,6},
			{7,8,0},
		};
		int[][] test = {
			{1, 0},
			{2, 3},
		};
		Solver solver = new Solver(new Board(test));
		System.out.println(solver.solution());
		System.out.println("MOVES: " + solver.moves());
	}
}
