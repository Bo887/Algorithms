import java.util.ArrayList;
public class Board{

	private int[][] blocks;
	private int N;
	private int emptyI;
	private int emptyJ;
	
	public Board(int[][] blocks){
		this.blocks = blocks;
		N = blocks.length;
		this.blocks = copy();
		for(int i=0; i<N; i++){
			for(int j=0; j<N; j++){
				if (blocks[i][j] == 0){
					emptyI = i;
					emptyJ = j;
				}
			}
		}
	}

	public int dimension(){
		return N;
	}

	public int hamming(){
		int dist = 0;
		for(int i=0; i<N; i++){ 
			for(int j=0; j<N; j++){
				//make sure to skip the empty square, which is represented by 0
				if (blocks[i][j] == 0) continue;
				//hamming dist is incr by 1 whenever the piece is not in its correct position
				if (blocks[i][j] != i*N+j+1){
					dist++;
					//System.out.println(i + "\t" + j);
				}
			}
		}
		return dist;
	}

	public int manhattan(){
		int dist = 0;
		for(int i=0; i<N; i++){
			for(int j=0; j<N; j++){
				//skip empty square
				if (blocks[i][j] == 0) continue;
				//current val in blocks[i][j]
				int val = blocks[i][j];	
				//calculate the indicies where this value should be
				int expectedValI = val%N==0 ? val/N-1 : val/N;
				//If val%N == 0, then this is the rightmost value in the block, which is N-1
				int expectedValJ = val%N==0 ? N-1 : val%N-1;
				/*
				System.out.println("I: " + i + "\tExpected I: " + expectedValI + "\tJ: " + j
					+ "\tExpected J: " + expectedValJ);
				*/
				//Manhattan dist = the dist horizontally and vertically between the expected and actual positions of the piece
				dist += Math.abs(expectedValI-i) + Math.abs(expectedValJ-j);
			}
		}
		return dist;
	}

	public boolean isGoal(){
		return hamming() == 0;
	}

	public Board twin(){
		int[][] copy = copy();
		boolean fail = false;
		for(int i=0; i<N;){
			for(int j=0; j<N; j++){
				//if any element in this row is 0 (empty), then we fail and move on to the next row to be safe
				if (copy[i][j] == 0){
					fail = true;
				}
			}
			//if we fail, move on to next row
			if (fail){
				i++;
			}
			//if we don't fail, we can swap safely
			else{
				//swap
				copy[i][0] = blocks[i][1];
				copy[i][1] = blocks[i][0];
				break;	
			}
			fail = false;
		}
		return new Board(copy);
	}

	private int[][] copy(){
		int[][] copy = new int[N][N];
		for(int i=0; i<N; i++){
			for(int j=0; j<N; j++){
				copy[i][j] = blocks[i][j];
			}
		}
		return copy;
	}

	@Override
	public boolean equals(Object y){
		if (!(y instanceof Board)) return false;
		Board other = (Board)y;
		if (this.N != other.N) return false;
		boolean pass = true;
		for(int i=0; i<N; i++){
			for(int j=0; j<N; j++){
				if (this.blocks[i][j] != other.blocks[i][j]) pass = false;
			}
		}
		return pass;
	}

	public Iterable<Board> neighbors(){
		ArrayList<Board> list = new ArrayList<Board>();
		//swap with upper neighbor
		if (emptyI > 0){
			list.add(neighbor(emptyI-1, emptyJ));
		}
		//swap with lower neighbor
		if (emptyI < N-1){
			list.add(neighbor(emptyI+1, emptyJ));
		}
		//swap with left neighbor
		if (emptyJ > 0){
			list.add(neighbor(emptyI, emptyJ-1));
		}
		//swap with right neighbor
		if (emptyJ < N-1){
			list.add(neighbor(emptyI, emptyJ+1));
		}
		return list;
	}

	private Board neighbor(int newI, int newJ){
		int[][] copy = copy();
		copy[emptyI][emptyJ] = copy[newI][newJ];
		copy[newI][newJ] = 0;
		return new Board(copy);
	}

	@Override
	public String toString(){
		String rv = N + "\n ";
		for(int i=0; i<N; i++){
			for(int j=0; j<N; j++){
				rv += blocks[i][j] + " ";	
			}
			rv += "\n ";
		}
		return rv;
	}

	public static void main(String[] args){
		int[][] blocks = {
			{8,1,3},
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
		Board board = new Board(test);
		//constructor + toString
		System.out.println(board);
		//hamming dist
		//System.out.println("HAMMING: " + board.hamming());
		//manhattan dist
		//System.out.println("MANHATTAN: " + board.manhattan());
		//twin board
		System.out.println("TWIN: " + board.twin());
		//equals method
		//System.out.println(board.equals(new Board(blocks)));
		//test neighbors method
		//System.out.println(board.neighbors());
	}
}
