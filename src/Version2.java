import java.util.concurrent.ForkJoinPool; 
/**
 * This process the data to return user desired population and percentage
 * against its total population
 * (Note: This is parallel processing, with O(n) time answering query.)
 * 
 * @author Yufang Sun, Boren Li
 *
 */
public class Version2 extends Version {
	protected static ForkJoinPool fjPool;

	/**
	 * Constructor throw illegal argument exception if any argument
	 * is less than 1
	 * @param column
	 * @param row
	 */
	public Version2(int column, int row, CensusData data) {
		super(column, row, data);
		fjPool = new ForkJoinPool();
		//final long startTime = System.currentTimeMillis();
		map = fjPool.invoke(new GridP(data, data.data_size, 0));

	}
	
	@Override
	/**
	 * Computes the desired area's population, again checking user
	 * input, illegalArgumentException. return the population.
	 * (Detail in V1)
	 */
	public int population(int[] input) {
		checkInput(input);
		int[] xOrY = new int[]{x, y};
		return wanted = fjPool.invoke(new PopulationP(census, input, xOrY, map, census.data_size, 0));
	}
	
}
