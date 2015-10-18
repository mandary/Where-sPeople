import java.util.concurrent.RecursiveTask;

/**
 * Computes and return the rectangle of the whole map and its total population
 * Process the whole data in parallel.
 * @author Yufang Sun, Boren Li
 *
 */
@SuppressWarnings("serial")
public class GridP extends RecursiveTask<Rectangle>{
	private static final int CUTOFF = 100000;
	private CensusData array;
	private int hi;
	private int lo;
	
	/**
	 * Takes in census data and its begining index and ending index
	 * @param census data
	 * @param high
	 * @param low
	 */
	public GridP (CensusData data, int high, int low) {
		array = data;
		hi = high;
		lo = low;
	}
	
	/**
	 * Computes and return the rectangle of the whole map and its population
	 */
	public Rectangle compute(){
		if(hi - lo <= CUTOFF) {
			return CommonMethod.grid(array, hi, lo);
		} else {
			GridP left = new GridP(array, (hi + lo)/2, lo);
			GridP right = new GridP(array, hi, (hi + lo)/2);
			left.fork();
			Rectangle rightAns = right.compute();
			Rectangle leftAns = left.join();
			return leftAns.encompass(rightAns);
		}
		
	}

}
