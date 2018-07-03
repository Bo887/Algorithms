import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast{

	private WordNet wordnet;

	public Outcast(WordNet wordnet){
		this.wordnet = wordnet;
	}

	public String outcast(String[] nouns){
		int max_dist = Integer.MIN_VALUE;
		String max_string = "";
		for(int i=0; i<nouns.length; i++){
			int dist = 0;
			for(int j=0; j<nouns.length; j++){
				//if (i==j) continue;
				dist += wordnet.distance(nouns[i], nouns[j]);
			}
			if (dist > max_dist){
				max_dist = dist;
				max_string = nouns[i];
			}
		}
		return max_string;
	}

	public static void main(String[] args){
	    WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
		In in = new In(args[t]);
		String[] nouns = in.readAllStrings();
		StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
	}
}
