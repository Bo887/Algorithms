import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver{

	private Picture picture;
	private boolean transposed = false; 
	private double[][] energy;
	private int W;
	private int H;
	
	public SeamCarver(Picture picture){
		if (picture == null) throw new IllegalArgumentException();
		this.picture = new Picture(picture);
		W = picture.width();
		H = picture.height();
		energy = new double[H][W];
		for(int i=0; i<H; i++){
			for(int j=0; j<W; j++){
				energy[i][j] = energy(j, i);
			}
		}
	}

	public Picture picture(){
		if (transposed) transpose();
		return new Picture(picture);
	}

	public int width(){
		return W;
	}

	public int height(){
		return H;
	}

	public double energy(int x, int y){
		if (x < 0 || y < 0 || x >= W || y >= H) throw new IllegalArgumentException();
		if (x == 0 || y == 0 || x == W-1 || y == H-1) return 1000;
		return Math.sqrt(calculateRGBGradient2(picture.get(x-1, y), picture.get(x+1, y)) + 
			calculateRGBGradient2(picture.get(x, y-1), picture.get(x, y+1)));
	}

	private double calculateRGBGradient2(Color curr, Color other){
		int deltaR = curr.getRed()-other.getRed();
		int deltaG = curr.getGreen()-other.getGreen();
		int deltaB = curr.getBlue()-other.getBlue();
		return Math.pow(deltaR, 2) + Math.pow(deltaG, 2) + Math.pow(deltaB, 2);
	}

	public int[] findHorizontalSeam(){
		//what is the total distance to point (x, y)
		double [][] distTo = new double[H][W];
		//what was the x value of the previous point to (x, y)
		int[][] pointTo = new int[H][W];
		for(int i=0; i<H; i++){
			for(int j=0; j<W; j++){
				distTo[i][j] = Double.MAX_VALUE;
			}
		}
		//initialize distances of first row
		for(int i=0; i<H; i++){
			distTo[i][0] = energy[i][0];
		}

		for(int col=0; col<W-1; col++){
			for(int row=0; row<H; row++){
				relaxHorizontal(row, col, pointTo, distTo);
			}
		}
		/*
		for(int i=0; i<H; i++){
			for(int j=0; j<W; j++){
				System.out.print(distTo[i][j] + " ");
			}
			System.out.println();
		}
		*/
		int minEndRow = -1;
		double min = Double.MAX_VALUE;
		for(int i=0; i<H; i++){
			double curr = distTo[i][W-1];
			if (curr < min){
				min = curr;
				minEndRow = i;
			}
		}
		int[] rv = new int[W];
		for(int i=W-1, j=minEndRow; i>=0; i--){
			rv[i] = j;
			j -= pointTo[j][i];
		}
		return rv;
	}

	private void relaxHorizontal(int row, int col, int[][] pointTo, double[][] distTo){
		int nextCol = col + 1;	
		for(int i=-1; i<=1; i++){
			int nextRow = row + i;
			if (nextRow < 0 || nextRow >= H) continue;
			if (distTo[row][col] + energy[nextRow][nextCol] < distTo[nextRow][nextCol]){
				distTo[nextRow][nextCol] = distTo[row][col] + energy[nextRow][nextCol];
				pointTo[nextRow][nextCol] = i;
			}
		}
	}

	private void transpose(){
		transposed = !transposed;
		Picture picture = new Picture(H, W);
		double[][] energyCopy = new double[W][H];
		for(int i=0; i<H; i++){
			for(int j=0; j<W; j++){
				picture.set(i, j, this.picture.get(j, i));
				energyCopy[j][i] = energy[i][j];
			}
		}
		this.picture = picture;
		int copyW = W;
		W = H;
		H = copyW;
		energy = energyCopy;
	}
	
	public int[] findVerticalSeam(){
		if (!transposed){
			transpose();
		}
		int[] rv = findHorizontalSeam();
		transpose();
		return rv;
	}

	public void removeHorizontalSeam(int[] seam){
		if (seam == null || picture.height() == 1) throw new IllegalArgumentException();
		checkHorizontalSeam(seam);
		H--;
		Picture newPic = new Picture(W, H);
		for(int i=0; i<W; i++){
			int rm = seam[i];
			for(int j=0; j<H; j++){
				if (j >= rm){
					newPic.set(i, j, picture.get(i, j+1));
				}
				else{
					newPic.set(i, j, picture.get(i, j));
				}
			}
		}
		picture = newPic;
	}

	public void removeVerticalSeam(int[] seam){
		if (seam == null || picture.width() == 1) throw new IllegalArgumentException();
		if (!transposed){
			transpose();
		}
		removeHorizontalSeam(seam);
		transpose();
	}

	private void checkHorizontalSeam(int[] seam){
		if (seam.length != W) throw new IllegalArgumentException();
		int prev = seam[0];
		for(int i=0; i<seam.length; i++){
			if (seam[i] < 0 || seam[i] >= H) throw new IllegalArgumentException();
			if (seam[i] < prev - 1 || seam[i] > prev + 1) throw new IllegalArgumentException();
			prev = seam[i];
		}
	}

	public static void main(String[] algs){
		SeamCarver test = new SeamCarver(new Picture("10x10.png"));
		int[] horizontalSeam = test.findHorizontalSeam();
		for(int i=0; i<horizontalSeam.length; i++){
			System.out.print(horizontalSeam[i] + " " );
		}
		System.out.println();
		System.out.println(test.picture());
		test.removeHorizontalSeam(horizontalSeam);
		System.out.println(test.picture());
	}
}
