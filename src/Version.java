/**
 * Abstract Version base for all versions. Contain the method to 
 * check standard input and calculating the percentage and
 * constructor initialization.
 * @author Yufang Sun, Boren Li
 *
 */
public abstract class Version {
	protected Rectangle map; // since it is protected, so other class have the access to its field
	protected int x;
	protected int y;
	protected int wanted;
	protected CensusData census;
	public static final int ONE = 1;
	public static final int PERCENT = 100;
	
	
	/**
	 * Constructor. if column and row less than 1, illegalArgumentException else wise
	 * @param column
	 * @param row
	 * @param data
	 */
	protected Version(int column, int row, CensusData data) {
		illegalArg(column, row);
		x = column;
		y = row;
		census = data;
	}
	
	/**
	 * Calculate the percentage of the desired population against
	 * total population
	 * @return percentage
	 */
	protected float percentage() {
		return (float) PERCENT * wanted / map.population;
	}
	
	/**
	 * This handles the input checking specified in PopulationQuery
	 * @param user input
	 */
	protected void checkInput(int[] input) {
		boolean c1 = ONE <= input[0] & input[0] <= x;	
		errorMessage(c1, "Wrong west coordinates");
		boolean r1 = ONE <= input[1] & input[1] <= y;
		errorMessage(r1, "Wrong south coordinates");
		boolean c2 = input[0] <= input[2] & input[2] <= x;
		errorMessage(c2, "Wrong east coordinates");
		boolean r2 = input[1] <= input[3] & input[3] <= y;
		errorMessage(r2, "Wrong north coordinates");
	}
	
	/**
	 * Handles the user input error message and exit the program.
	 */
	private void errorMessage(boolean boo, String output) {
		if(!boo) {
			throw new IllegalArgumentException(output);
		}
	}
	
	protected void illegalArg(int column, int row) {
		if(column < ONE || row < ONE) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Standard method to return user desired population;
	 * @param input
	 * @return population
	 */
	public abstract int population(int[] input);
	

}
