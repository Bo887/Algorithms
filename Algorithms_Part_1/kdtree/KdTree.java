import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Stack;

public class KdTree{

	private static final boolean HORIZONTAL = true;
	private static final boolean VERTICAL = false;
	
	private class Node{

		Node left, right; //children
		Point2D point; //point represented in this node
		RectHV rect; //rectangle represented by this node
		boolean type;
		int size;
		
		public Node(Point2D point, RectHV rect, int size, boolean type){
			this.point = point;
			this.rect = rect;
			this.size = size;
			this.type = type;
		}

		private int compare(double val, double otherVal){
			double cmp = val - otherVal;
			if (cmp < 0) return -1;
			else if (cmp > 0) return 1;
			else return 0;
		}

		public int compareTo(Point2D other){
			//vertical, so compare by x values primarily, then use y values to tiebreak
			if (this.type == VERTICAL){
				int cmp = compare(this.point.x(), other.x());
				return cmp != 0 ? cmp : compare(this.point.y(), other.y());
			}
			//horizontal, so compare by y values primarily, then use x values to tiebreak
			else{
				int cmp = compare(this.point.y(), other.y());
				return cmp != 0 ? cmp : compare(this.point.x(), other.x());
			}
		}
	}

	private Node root = null;

	private Point2D closest;
	private double min_dist;

	public KdTree(){
	}

	public boolean isEmpty(){
		return root == null;
	}

	public int size(){
		return size(root);
	}

	public void insert(Point2D p){
		//root is vertical, and the rectangle contains the entire unit square
		root = insert(root, p, VERTICAL, null);
	}

	private Node insert(Node node, Point2D p, boolean type, Node parent){
		//we have reached bottom of tree, so time to insert
		if (node == null){
			RectHV rect = null;
			//this is first node inserted, so the rectangle is the unit square
			if (parent == null){
				rect = new RectHV(0,0,1,1);
			}
			else{
				double xmin=0, xmax=0, ymin=0, ymax=0;
				if (type == HORIZONTAL){
					//since this node is horizontal, the y boundaries are the same for either case
					ymin = parent.rect.ymin();
					ymax = parent.rect.ymax();
					//the point being inserted is horizontal and on the right of the parent node
					if (parent.compareTo(p) < 0){
						xmin = parent.point.x();
						xmax = parent.rect.xmax();
					}
					//point being inserted is horizontal and left of the parent node
					else{
						xmin = parent.rect.xmin();
						xmax = parent.point.x();
					}
				}
				//same as above but vice versa
				else{
					xmin = parent.rect.xmin();
					xmax = parent.rect.xmax();
					if (parent.compareTo(p) < 0){
						ymin = parent.point.y();
						ymax = parent.rect.ymax();
					}
					else{
						ymin = parent.rect.ymin();
						ymax = parent.point.y();
					}
				}
				rect = new RectHV(xmin, ymin, xmax, ymax);
			}
			return new Node(p, rect, 1, type);
		}
		int cmp = node.compareTo(p);
		if (cmp < 0) node.left = insert(node.left, p, !type, node);
		else if (cmp > 0) node.right = insert(node.right, p, !type, node);
		else{
			node.point = p;
		}
		node.size = 1 + size(node.left) + size(node.right);
		return node;
	}

	private int size(Node node){
		if (node == null) return 0;
		return node.size;
	}

	public boolean contains(Point2D p){
		//start at the root
		Node curr = root;
		//and traverse down until we reach a null node (the end), or until we hit the point we are looking for
		while (curr != null){
			int cmp = curr.compareTo(p); 
			if (cmp < 0){
				curr = curr.left;
			}
			else if (cmp > 0){
				curr = curr.right;
			}
			//cmp == 0
			else{
				return true;
			}
		}
		return false;
	}

	public void draw(){
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		StdDraw.line(0,0,1,0);
		StdDraw.line(0,0,0,1);
		StdDraw.line(1,0,1,1);
		StdDraw.line(0,1,1,1);
		draw(root);
	}

	private void draw(Node node){
		if (node == null) return;
		StdDraw.setPenColor(StdDraw.BLACK);	
		StdDraw.setPenRadius(0.01);
		node.point.draw();
		if (node.type == HORIZONTAL){
			StdDraw.setPenColor(StdDraw.BLUE);	
			StdDraw.setPenRadius(0.005);
			StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
		}
		else{
			StdDraw.setPenColor(StdDraw.RED);	
			StdDraw.setPenRadius(0.005);
			StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
		}
		draw(node.left);
		draw(node.right);
	}

	public Iterable<Point2D> range(RectHV rect){
		Stack<Point2D> stack = new Stack<Point2D>();
		rangesearch(root, rect, stack);
		return stack;
	}

	private void rangesearch(Node node, RectHV query, Stack<Point2D> stack){
		//if we hit bottom of tree, return
		if (node == null) return;	
		//if this node's rectangle intersects the query, process this tree, otherwise do nothing
		if (node.rect.intersects(query)){
			//if this node's point is in the rectangle, store this point
			if (query.contains(node.point)){
				stack.push(node.point);
			}
			//and continue searching the subtree
			rangesearch(node.left, query, stack);
			rangesearch(node.right, query, stack);
		}
	}

	public Point2D nearest(Point2D p){
		closest = null;
		min_dist = 1e9;
		pointsearch(root, p);
		return closest;
	}

	private void pointsearch(Node node, Point2D point){
		if (node == null) return;
		//if the dist between this node and the point is > than the min dist already, skip
		double squared_dist = node.point.distanceSquaredTo(point);
		if (squared_dist >= min_dist) return;
		else{
			closest = node.point;
			min_dist = squared_dist;
			pointsearch(node.left, point);
			pointsearch(node.right, point);
		}
	}

	public static void main(String[] args){
		KdTree tree = new KdTree();
		Point2D zero = new Point2D(0.5,0.5);
		Point2D one = new Point2D(0.5,0.75);
		tree.insert(zero);
		tree.insert(one);
		//tree.insert(new Point2D(0.5, 0.25));
		//tree.insert(new Point2D(0.5, 0.7));
		//tree.insert(new Point2D(0.5, 0.3));
		System.out.println(tree.nearest(new Point2D(1,1)));
		/*
		System.out.println(tree.contains(zero));
		System.out.println(tree.contains(one));
		System.out.println(tree.size());
		System.out.println(tree.contains(one));
		*/
		tree.draw();
	}
}
