import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class WordNet{

	private HashMap<String, ArrayList<Integer>> synsets;
	private HashMap<Integer, ArrayList<String>> ids;
	private Digraph digraph;
	private SAP sap;
	private int V = 0;

	public WordNet(String synsetsFile, String hypernyms){
		if (synsetsFile == null || hypernyms == null) throw new IllegalArgumentException();
		//symbol table mapping from synset id to synset
		synsets = new HashMap<String, ArrayList<Integer>>();
		ids = new HashMap<Integer, ArrayList<String>>();
		readSynsets(synsetsFile);
		//V is calculated in readSynsets
		digraph = new Digraph(V);
		readHypernyms(hypernyms);
		//digraph is filled in readHypernyms
		sap = new SAP(digraph);

		checkCycle();
	}

	private void checkCycle(){
		DirectedCycle cycle = new DirectedCycle(digraph);
		if (cycle.hasCycle()) throw new IllegalArgumentException();
	}

	private void checkRoot(){

	}

	private void readSynsets(String filename){
		In in = new In(filename);
		while(in.hasNextLine()){
			String[] line = in.readLine().split(",");
			int id = Integer.parseInt(line[0]);
			String synset = line[1];
			String[] split = synset.split(" ");
			for(String s : split){
				if (synsets.containsKey(s)){
					synsets.get(s).add(id);
				}
				else{
					synsets.put(s, new ArrayList<Integer>());
					synsets.get(s).add(id);
				}
			}
			ids.put(id, new ArrayList<>(Arrays.asList(split)));
			V++;
		}
	}

	private void readHypernyms(String filename){
		In in = new In(filename);
		while(in.hasNextLine()){
			String[] line = in.readLine().split(",");
			int synset = Integer.parseInt(line[0]);
			for(int i=1; i<line.length; i++){
				digraph.addEdge(synset, Integer.parseInt(line[i]));
			}
		}
	}

	private void printST(HashMap<Integer,String> st){
		for(Integer i : st.keySet()){
			System.out.println(i + " " + st.get(i));
		}
	}

	public Iterable<String> nouns(){
		return synsets.keySet();
	}

	public boolean isNoun(String word){
		if (word == null) throw new IllegalArgumentException();
		return synsets.keySet().contains(word);
	}

	public int distance(String nounA, String nounB){
		if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
		return sap.length(synsets.get(nounA), synsets.get(nounB));
	}

	public String sap(String nounA, String nounB){
		if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
		ArrayList<String> strings = ids.get(sap.ancestor(synsets.get(nounA), synsets.get(nounB)));
		String rv = "";	
		for(String s : strings){
			rv += s + " ";
		}
		return rv;
	}

	public static void main(String[] args){
		WordNet test = new WordNet("synsets.txt", "hypernyms.txt");
		//System.out.println(test.nouns());
	}
}
