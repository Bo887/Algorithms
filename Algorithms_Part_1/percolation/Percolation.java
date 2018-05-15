import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation{

	//top = 0, bottom = n+1, 1 based indexing
	private WeightedQuickUnionUF qu;
	//1 based indexing
	private boolean[][] opened;
	private int n;
	private int top, bottom;
	private int numOpenSites;

	public Percolation(int n){
		this.n = n;
		qu = new WeightedQuickUnionUF(n*n+2);
		opened = new boolean[n+1][n+1];
		for(int i=1; i<=n; i++){
			for(int j=1; j<=n; j++){
				opened[i][j] = false;
			}	
		}
		top = 0;
		bottom = n+1;
		numOpenSites = 0;
	}

	public void open(int row, int col){
		if (row > n || row < 1 || col > n || col < 1) throw new IllegalArgumentException();
		if (isOpen(row, col)) return;

		opened[row][col] = true;		
		numOpenSites++;

		if (row == 1){
			qu.union(doubleToSingle(row, col), top);
		}
		else if (row == n){
			qu.union(doubleToSingle(row, col), bottom);
		}

		if (col > 1 && isOpen(row, col-1)){
			qu.union(doubleToSingle(row, col), doubleToSingle(row, col-1));
		}
		if (col < n && isOpen(row, col+1)){
			qu.union(doubleToSingle(row, col), doubleToSingle(row, col+1));
		}
		if (row > 1 && isOpen(row-1, col)){
			qu.union(doubleToSingle(row, col), doubleToSingle(row-1, col));	
		}
		if (row < n && isOpen(row+1, col)){
			qu.union(doubleToSingle(row, col), doubleToSingle(row+1, col));
		}
	}

	public boolean isOpen(int row, int col){
		if (row > n || row < 1 || col > n || col < 1) throw new IllegalArgumentException();
		return opened[row][col];
	}

	public boolean isFull(int row, int col){
		if (row > n || row < 1 || col > n || col < 1) throw new IllegalArgumentException();
		return qu.connected(doubleToSingle(row, col), top);
	}
	
	public int numberOfOpenSites(){
		return numOpenSites;
	}
	
	public boolean percolates(){
		return qu.connected(top, bottom);
	}
	
	private int doubleToSingle(int row, int col){
		if (row > n || row < 1 || col > n || col < 1) throw new IllegalArgumentException();
		return row*n-(n-col); 
	}

	public static void main(String[] args){
		Percolation p = new Percolation(4);
		System.out.println(p.doubleToSingle(1, 1));
	}
}
