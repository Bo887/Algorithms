import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP{

	private final Digraph digraph;
	
	public SAP(Digraph G){
		if (G == null) throw new IllegalArgumentException();
		digraph = new Digraph(G);
	}

	public int length(int v, int w){
		if (v < 0 || v >= digraph.V() || w < 0 || w >= digraph.V()) throw new IllegalArgumentException();
		BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(digraph, w);
		int min_dist = Integer.MAX_VALUE;
		for(int i=0; i<digraph.V(); i++){
			if (!fromV.hasPathTo(i) || !fromW.hasPathTo(i)) continue;	
			int dist = fromV.distTo(i) + fromW.distTo(i);	
			min_dist = Math.min(dist, min_dist);
		}
		return min_dist == Integer.MAX_VALUE ? -1 : min_dist;
	}

	public int ancestor(int v, int w){
		if (v < 0 || v >= digraph.V() || w < 0 || w >= digraph.V()) throw new IllegalArgumentException();
		BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(digraph, w);
		int min_dist = Integer.MAX_VALUE;
	 	int min_id = -1;
		for(int i=0; i<digraph.V(); i++){
			if (!fromV.hasPathTo(i) || !fromW.hasPathTo(i)) continue;	
			int dist = fromV.distTo(i) + fromW.distTo(i);	
			if (dist < min_dist){
				min_dist = dist;
				min_id = i;
			}
		}
		return min_id;
	}

	public int length(Iterable<Integer> v, Iterable<Integer> w){
		if (v == null || w == null) throw new IllegalArgumentException();
		BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(digraph, w);
		int min_dist = Integer.MAX_VALUE;
		for(int i=0; i<digraph.V(); i++){
			if (!fromV.hasPathTo(i) || !fromW.hasPathTo(i)) continue;	
			int dist = fromV.distTo(i) + fromW.distTo(i);	
			min_dist = Math.min(dist, min_dist);
		}
		return min_dist == Integer.MAX_VALUE ? -1 : min_dist;
	}

	public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
		if (v == null || w == null) throw new IllegalArgumentException();
		BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(digraph, w);
		int min_dist = Integer.MAX_VALUE;
	 	int min_id = -1;
		for(int i=0; i<digraph.V(); i++){
			if (!fromV.hasPathTo(i) || !fromW.hasPathTo(i)) continue;	
			int dist = fromV.distTo(i) + fromW.distTo(i);	
			if (dist < min_dist){
				min_dist = dist;
				min_id = i;
			}
		}
		return min_id;
	}

	public static void main(String[] args){
		SAP sap = new SAP(new Digraph(new In("digraph1.txt")));
		int a = 1, b = 2;
		System.out.println(sap.length(a, b));
		System.out.println(sap.ancestor(a, b));
	}

}
