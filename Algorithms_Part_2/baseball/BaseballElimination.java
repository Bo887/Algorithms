import java.util.HashMap;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination{

	private final int N;
	private final HashMap<String, Integer> teamToInt;
	private final String[] intToTeam;
	private final int[] wins, losses, remaining;
	private final int[][] games;
	private final ArrayList<String>[] certificates;

	public BaseballElimination(String filename){
		In in = new In(filename);
		N = Integer.parseInt(in.readLine());		
		teamToInt = new HashMap<String, Integer>();
		wins = new int[N];
		losses = new int[N];
		remaining = new int[N];
		games = new int[N][N];
		certificates = new ArrayList[N];
		intToTeam = new String[N];
		for(int i=0; i<N; i++){
			String name = in.readString();
			intToTeam[i] = name;
			teamToInt.put(name, i);
			wins[i] = in.readInt();
			losses[i] = in.readInt();
			remaining[i] = in.readInt();
			for(int j=0; j<N; j++){
				games[i][j] = in.readInt();
			}
		}
	}

	public int numberOfTeams(){
		return N;
	}

	public Iterable<String> teams(){
		return teamToInt.keySet();	
	}

	public int wins(String team){
		checkTeam(team);
		return wins[teamToInt.get(team)];
	}

	public int losses(String team){
		checkTeam(team);
		return losses[teamToInt.get(team)];
	}

	public int remaining(String team){
		checkTeam(team);
		return remaining[teamToInt.get(team)];
	}

	public int against(String team1, String team2){
		checkTeam(team1);
		checkTeam(team2);
		return games[teamToInt.get(team1)][teamToInt.get(team2)];
	}

	public boolean isEliminated(String team){
		checkTeam(team);
		if (isTriviallyEliminated(team) || isNonTriviallyEliminated(team)){
			return true;
		}
		return false;
	}

	public Iterable<String> certificateOfElimination(String team){
		checkTeam(team);
		if (!isEliminated(team)) return null;
		int index = teamToInt.get(team);
		return certificates[index];
	}
	
	private void checkTeam(String team){
		if (!teamToInt.containsKey(team)) throw new IllegalArgumentException();
	}

	private boolean isTriviallyEliminated(String team){
		int maxWins = Integer.MIN_VALUE;
		int maxWinsIndex = 0;
		for(int i=0; i<N; i++){
			if (wins[i] > maxWins){
				maxWins = wins[i];
				maxWinsIndex = i;
			}
		}
		int i = teamToInt.get(team);
		if (wins[i] + remaining[i] < maxWins){
			certificates[i] = new ArrayList<String>();
			certificates[i].add(intToTeam[maxWinsIndex]);
			return true;
		}
		return false;
	}

	private boolean isNonTriviallyEliminated(String team){
		int teamIndex = teamToInt.get(team);
		int firstLayer = (N*(N-1))/2;
		int lastLayer = (N);
		int V = firstLayer + lastLayer + 2;
		FlowNetwork f = new FlowNetwork(V);
		//let s be vertex # 0, and t be vertex # V-1
		//first, add the edges from s to the first layer
		int source = 0, sink = V-1;
		int counter = 1;
		for(int i=0; i<N; i++){
			for(int j=i+1; j<N; j++){
				if (i==j || teamIndex == i || teamIndex == j) continue;
				FlowEdge edge = new FlowEdge(source, counter, games[i][j]);
				f.addEdge(edge);
				edge = new FlowEdge(counter, firstLayer+1+i, Double.POSITIVE_INFINITY);
				f.addEdge(edge);
				edge = new FlowEdge(counter, firstLayer+1+j, Double.POSITIVE_INFINITY);
				f.addEdge(edge);
				counter++;
			}
			int capacity = wins[teamIndex] + remaining[teamIndex] - wins[i];
			if (i == teamIndex) continue;
			capacity = Math.max(0, capacity);
			FlowEdge edge = new FlowEdge(firstLayer+i+1, sink, capacity);
			f.addEdge(edge);
				
		}
		//System.out.println(counter);
		//System.out.println(f);
		//add edges from first layer to second layer
		FordFulkerson ff = new FordFulkerson(f, source, sink);
		boolean rv = false;
		for(FlowEdge edge : f.adj(source)){
			if (edge.capacity() != edge.flow()){
				rv = true;
			}
		}
		if (rv){
			ArrayList<String> temp = new ArrayList<String>();
			for(int i=0; i<N; i++){
				if (ff.inCut(i+firstLayer+1) && i != teamIndex){
					temp.add(intToTeam[i]);
				}
			}
			certificates[teamIndex] = temp;
		}
		return rv;
	}

	public static void main(String[] args){
		BaseballElimination test = new BaseballElimination(args[0]);
		for(String team : test.teams()){
			if (test.isEliminated(team)){
				StdOut.print(team + " is eliminated by the subset R = { ");
				for(String t : test.certificateOfElimination(team)){
					StdOut.print(t + " ");
				}
				StdOut.println("}");
			}
			else{
				StdOut.println(team + " is not eliminated");
			}
		}
	}
}
