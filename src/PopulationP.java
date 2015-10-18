import java.util.concurrent.RecursiveTask;

/**
 * Computes and return the desired population.
 * Process the data in parallel
 * @author Yufang Sun, Boren Li
 *
 */
@SuppressWarnings("serial")
public class PopulationP extends RecursiveTask<Integer>{
	private int[] grid;
	private int hi;
	private int lo;
	private CensusData array;
	private int[] xOrY;
	private Rectangle map;
	private static final int CUTOFF = 100000;
	
	/**
	 * It takes following paramenter
	 * @param census data
	 * @param user input
	 * @param x and y
	 * @param map
	 * @param high
	 * @param low
	 */
	public PopulationP (CensusData data, int[] input, int[] needed, Rectangle rect, int high, int low) {
		grid = input;
		hi = high;
		lo = low;
		array = data;
		xOrY = needed;
		map = rect;
	}
	
	/**
	 * Compute and return the population in desired area.
	 */
	public Integer compute() {
		if(hi - lo <= CUTOFF) {
			return CommonMethod.population(map, array, grid, xOrY, hi, lo);
		} else {
			PopulationP left = new PopulationP(array, grid, xOrY, map, (hi + lo)/2, lo);
			PopulationP right = new PopulationP(array, grid, xOrY, map, hi, (hi + lo)/2);
			left.fork();
			int rightAns = right.compute();
			int leftAns = left.join();
			return leftAns + rightAns;
		}
	}

}
