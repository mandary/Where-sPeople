/**
 * Computes and return the grids of the whole map and its population within
 * Process the whole data in parallel.
 * @author Yufang Sun, Boren Li
 */
import java.util.concurrent.RecursiveTask;
@SuppressWarnings("serial")
public class GridResolve extends RecursiveTask<int[][]> {
	private int lo;
	private int hi;
	private CensusData data;
	private Rectangle map;
	private int[] xOrY;
	private static final int SEQUENTIAL_CUTOFF = 100000;
	
	/**
	 * It takes following parameters.
	 * @param low
	 * @param high
	 * @param array
	 * @param rec
	 * @param needed
	 */
	public GridResolve(int low, int high, CensusData array, Rectangle rec, int[] needed)  {
		lo = low;
		hi = high;
		data = array;
		map = rec;
		xOrY = needed;
	}
	
	/**
	 * return the computed grid with its population filled in.
	 */
	public int[][] compute() {
		if(hi - lo <= SEQUENTIAL_CUTOFF) { // every time it returns a whole grid!
			return CommonMethod.gridPop(data, map, xOrY, lo, hi);
			
		}else {
			GridResolve left = new GridResolve(lo, (lo + hi) / 2, data, map, xOrY);
			GridResolve right = new GridResolve((lo + hi) / 2, hi, data, map, xOrY);
			left.fork();
			int[][] rightAns = right.compute();
			int[][] leftAns = left.join();
			Version4.fjPool.invoke(new Combine(1, xOrY[0] + 1, 1, xOrY[1] + 1, leftAns, rightAns));
			return leftAns;
		}

	}

}
