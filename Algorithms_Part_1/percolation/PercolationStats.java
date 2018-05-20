import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats{

	private double[] storage;
	private int trials;

	public PercolationStats(int n, int trials){
		if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
		storage = new double[trials];
		this.trials = trials;
		for(int i=0; i<trials; i++){
			Percolation p = new Percolation(n);		
			while(!p.percolates()){
				int x = StdRandom.uniform(1, n+1);
				int y = StdRandom.uniform(1, n+1);
				p.open(x, y);
			}	
			storage[i] = (double)p.numberOfOpenSites()/(n*n);
		}	
	}
	
	public double mean(){
		return StdStats.mean(storage);
	}

	public double stddev(){
		return StdStats.stddev(storage);
	}
	
	public double confidenceLo(){
		return mean() - 1.96*stddev()/Math.sqrt(trials);
	}

	public double confidenceHi(){
		return mean() + 1.96*stddev()/Math.sqrt(trials);
	}

	public static void main(String[] args){
		int n = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[1]);
		PercolationStats ps = new PercolationStats(n, trials);
	
		System.out.println("mean                    = " + ps.mean());
		System.out.println("stddev                  = " + ps.stddev());
		System.out.println("95% confidence interval = " + "[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
	}
}
