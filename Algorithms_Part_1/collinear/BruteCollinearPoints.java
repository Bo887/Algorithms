import java.util.Arrays;
public class BruteCollinearPoints{

	private LineSegment[] segments;
	private int cnt = 0;

	public BruteCollinearPoints(Point[] points){
		if (points == null) throw new IllegalArgumentException();
		int N = points.length;	
		if (N == 1 && points[0] == null) throw new IllegalArgumentException();
		for(int i=0; i<N; i++){
			if (points[i] == null) throw new IllegalArgumentException();
		}
		Point[] copy = Arrays.copyOf(points, points.length);
		Arrays.sort(copy);
		for(int i=1, j=0; i<N; i++, j++){
			if (copy[i].compareTo(copy[j]) == 0) throw new IllegalArgumentException();
		}
		segments = new LineSegment[N];
		for(int i=0; i<N; i++){
			for(int j=i+1; j<N; j++){
				for(int k=j+1; k<N; k++){
					for(int l=k+1; l<N; l++){
						double slope_ij = copy[i].slopeTo(copy[j]); 
						double slope_ik = copy[i].slopeTo(copy[k]);
						double slope_il = copy[i].slopeTo(copy[l]);
						if (slope_ij == slope_ik && slope_ij == slope_il){
							Point[] temp = {copy[i], copy[j], copy[k], copy[l]};
							Arrays.sort(temp);
							segments[cnt++] = new LineSegment(temp[0], temp[3]);
						}
					}
				}
			}
		}
		LineSegment[] temp = new LineSegment[cnt];
		for(int i=0; i<cnt; i++){
			temp[i] = segments[i];
		}
		segments = temp;
	}

	public int numberOfSegments(){
		return cnt;
	}

	public LineSegment[] segments(){
		return Arrays.copyOf(segments, cnt);
	}

}
