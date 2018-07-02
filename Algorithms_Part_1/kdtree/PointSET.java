import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;
import java.util.Stack;

public class PointSET{

	//treeset uses a red-black bst
	private TreeSet<Point2D> bst;
	
	public PointSET(){
		bst = new TreeSet<Point2D>();	
	}

	public boolean isEmpty(){
		return bst.isEmpty();
	}

	public int size(){
		return bst.size();
	}

	public void insert(Point2D p){
		bst.add(p);
	}

	public boolean contains(Point2D p){
		return bst.contains(p);
	}

	public void draw(){
		for(Point2D p : bst){
			p.draw();
		}
	}

	public Iterable<Point2D> range(RectHV rect){
		Stack<Point2D> stack = new Stack<Point2D>();
		for(Point2D p : bst){
			if(rect.contains(p)){
				stack.push(p);
			}
		}
		return stack;
	}

	public Point2D nearest(Point2D p){
		Point2D min = null;
		double minDist = 1E9;
		for(Point2D curr : bst){
			if (p.distanceTo(curr) < minDist){
				min = curr;
				minDist = p.distanceTo(curr);
			}
		}
		return min;
	}

	public static void main(String[] args){
		PointSET test = new PointSET();	
		test.insert(new Point2D(10, 10));
		System.out.println(test.nearest(new Point2D(4, 4)));
	}
}
