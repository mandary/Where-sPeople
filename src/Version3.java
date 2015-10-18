/**
 * This preprocess the data and return user desired population and its
 * percentage against total population.
 * (Note: Sequential preprocessing, with O(1) time answering query.)
 * @author Yufang Sun, Boren Li
 *
 */
public class Version3 extends Version1{
	private int[][]pinpoint;
	
	/**
	 * Constructor throw illegal argument exception if any argument
	 * is less than 1
	 * Takes in data and preprocess it into desired columns and rows
	 * on a rectangle/map, each grid within contains its population.
	 * @param column
	 * @param row
	 * @param census data
	 */
	public Version3(int column, int row, CensusData data) {
		super(column, row, data);
		pinpoint = CommonMethod.gridPop(data, map, new int[]{x,y}, 0, data.data_size);
		CommonMethod.UpdatePop(data, pinpoint, new int[]{x, y});
	}
	
	@Override
	/**
	 * Use the preprocessing method to calculate user demanded population,
	 * wrong input, result in IllegalArgumentException. (Detail in V1)
	 */
	public int population(int[] input) {
		checkInput(input);
		return wanted = CommonMethod.populationForV345(input, pinpoint,  new int[]{x, y});
		
	}
}
