import java.util.Arrays;
import java.util.Comparator;
public class FastCollinearPoints{
	
	private LineSegment[] segments;
	private int cnt = 0;	

	public FastCollinearPoints(Point[] points){
		//Check for illegal arguments
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
		//Start processing, loop for every single point, calculating the slope to every other point
		for(int c=0; c<N; c++){
			Point curr = points[c];
			//copy is now sorted with respect to slope with curr
			Arrays.sort(copy, this.slopeOrder(curr));
			/*
			for(int i=1; i<N; i++){
				System.out.println("CURR: " + curr + "\tCOPY[i]: " + copy[i] + "\tSLOPE: " + curr.slopeTo(copy[i]));
			}
			*/
			int currSegmentStartIndex = 0;
			for(int i=1; i<N; i++){
				//System.out.println(curr + "\t" + copy[i]);
				Point currSegmentStartPoint = copy[currSegmentStartIndex];
				if (curr.slopeTo(copy[i]) != curr.slopeTo(currSegmentStartPoint)){
					int pointsOnSegment = i-currSegmentStartIndex;
					//if there are more than three more points on this segment
					if (pointsOnSegment >= 3 && currSegmentStartPoint.compareTo(curr)>0){
				//		System.out.println("ADDING: " + curr + "\tCOPY: " + copy[i-1]);
						//add to segments list
						segments[cnt++] = new LineSegment(curr, copy[i-1]);
					}
					//reset counter
					currSegmentStartIndex = i;
				}
				//System.out.println("POINTS ON SEGMENT: " + pointsOnSegment);
			}
			//System.out.println();
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
		
	private Comparator<Point> slopeOrder(Point p) {
        return new Comparator<Point>(){
                @Override
                public int compare(Point p1, Point p2){
                        double slope1 = p.slopeTo(p1);
                        double slope2 = p.slopeTo(p2);
                        double val = slope1-slope2;
                        if (val > 0) return 1;
                        else if (val < 0) return -1;
                        else return p1.compareTo(p2);
                }
        };
    }

}
