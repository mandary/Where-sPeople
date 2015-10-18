/**
 * This computes the user required population and its percentage against
 * total population.
 * (Note: This is sequential processing, with O(n) time answering query.)
 * @author Yufang Sun, Boren Li
 *
 */
public class Version1 extends Version{
	
	/**
	 * Constructor takes the column and row as its arguments
	 * Cannot be less than 1, otherwise IllegalArgumentException
	 * @param column
	 * @param row
	 * @param census data
	 */

	public Version1(int column, int row, CensusData data) {
		super(column, row, data);
		map = CommonMethod.grid(data, data.data_size, 0);
	}
	
	@Override
	/**
	 * Calculate the desired population
	 * If user input is illegal, IllegalArgumentException and exit.
	 * The Western-most grid column Exception if this is less than 1 or greater than column.
	 * The Southern-most grid row Exception if this is less than 1 or greater than row.
	 * The Eastern-most grid column Exception if this is less than the Western-most column (equal is okay) or greater than column.
	 * The Northern-most grid row Exception if this is less than the Southern-most column (equal is okay) or greater than row.
	 * @param CensusData data
	 * @param user input
	 * @return the population
	 */
	public int population(int[] input) {
		checkInput(input);
		return wanted = CommonMethod.population(map, census, input, new int[]{x,y}, census.data_size, 0);
		
	}
	
}
