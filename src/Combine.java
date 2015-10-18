import java.util.concurrent.RecursiveAction;
/** Combines two grids into one in parallel.
 * * @author Yufang Sun, Boren Li
 */
@SuppressWarnings("serial")
public class Combine extends RecursiveAction{
	private int xlo;
	private int xhi;
	private int ylo;
	private int yhi;
	private int[][] left;
	private int[][] right;
	private static final int SEQUENTIAL_CUTOFF = 10000;
	
	/**
	 * It takes following parameters
	 * @param xlow
	 * @param xhigh
	 * @param ylow
	 * @param yhigh
	 * @param leftAns
	 * @param rightAns
	 */
	public Combine(int xlow, int xhigh, int ylow, int yhigh, int[][]leftAns, int[][]rightAns) {
		xlo = xlow;
		ylo = ylow;
		xhi = xhigh;
		yhi = yhigh;
		left = leftAns;
		right = rightAns;
		
	}
	
	/**
	 * Take two grids and combine them into one.
	 */
	public void compute() {
		if( (xhi-xlo)*(yhi-ylo) <= SEQUENTIAL_CUTOFF) {
			for(int i = ylo; i < yhi; i++) {
				for(int j = xlo; j < xhi; j++) {
					left[i][j] += right[i][j];
				}
			}
		}else {
			Combine leftup = new Combine(xlo, (xlo + xhi) / 2, ylo, (ylo + yhi) / 2,  left, right);
			Combine rightup = new Combine((xlo + xhi) / 2,  xhi, ylo, (ylo + yhi) / 2, left, right);
			Combine leftdown = new Combine(xlo, (xlo + xhi) / 2, (ylo + yhi) / 2, yhi, left, right);
			Combine rightdown = new Combine((xlo + xhi) / 2, xhi, (ylo + yhi) / 2, yhi, left, right);
			leftup.fork();
			rightup.fork();
			leftdown.fork();
			rightdown.compute();
			leftup.join();
			rightup.join();
			leftdown.join();
		}
	}


}
