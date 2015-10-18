
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
/**
 * 
 * @author Yufang Sun, Boren Li.
 * Take 4 arguments legal format census txt filename, divide map into x columns
 * and y rows, and select a version to compute with(5 versions in total).
 * Usage: file, x-dimension, y-dimension, (-v)version#
 * Computes the desired population within the grid and its percentage against
 * total population.
 */
public class PopulationQuery {
	// next four constants are relevant to parsing
	public static final int TOKENS_PER_LINE  = 7;
	public static final int POPULATION_INDEX = 4; // zero-based indices
	public static final int LATITUDE_INDEX   = 5;
	public static final int LONGITUDE_INDEX  = 6;
	public static final int INPUT_INT_NUMBER = 4;
	private static CensusData data;
	private static Version version;

	/** parse the input file into a large array held in a CensusData object
	 * Skip the first line of the file
	 * After that each line has 7 comma-separated numbers (see constants above)
	 * We want to skip the first 4, the 5th is the population (an int)
	 * and the 6th and 7th are latitude and longitude (floats)
	 * If the population is 0, then the line has latitude and longitude of +.,-.
	 * which cannot be parsed as floats, so that's a special case
	 * (we could fix this, but noisy data is a fact of life, more fun
	 * to process the real data as provided by the government)
	 * @param filename
	 * @return CensusData
	 */
	// parse the input file into a large array held in a CensusData object
	public static CensusData parse(String filename) {
		CensusData result = new CensusData();
		
        try {
            @SuppressWarnings("resource")
			BufferedReader fileIn = new BufferedReader(new FileReader(filename));
            
            // Skip the first line of the file
            // After that each line has 7 comma-separated numbers (see constants above)
            // We want to skip the first 4, the 5th is the population (an int)
            // and the 6th and 7th are latitude and longitude (floats)
            // If the population is 0, then the line has latitude and longitude of +.,-.
            // which cannot be parsed as floats, so that's a special case
            //   (we could fix this, but noisy data is a fact of life, more fun
            //    to process the real data as provided by the government)
            
            String oneLine = fileIn.readLine(); // skip the first line

            // read each subsequent line and add relevant data to a big array
            while ((oneLine = fileIn.readLine()) != null) {
                String[] tokens = oneLine.split(",");
                if(tokens.length != TOKENS_PER_LINE)
                	throw new NumberFormatException();
                int population = Integer.parseInt(tokens[POPULATION_INDEX]);
                if(population != 0) {
                	result.add(population,
                			   Float.parseFloat(tokens[LATITUDE_INDEX]),
                		       Float.parseFloat(tokens[LONGITUDE_INDEX]));
                }
            }

            fileIn.close();
        } catch(IOException ioe) {
            System.err.println("Error opening/reading/writing input or output file.");
            System.exit(1);
        } catch(NumberFormatException nfe) {
            System.err.println(nfe.toString());
            System.err.println("Error in file format");
            System.exit(1);
        }
        return result;
	}
	/**
	 * Error if not 4 arguments, or file not found, or wrong file format
	 * If version number does not match, use default version 1.
	 * If x or y is less than 1, throw Illegal Argument Exception
	 * @param filename, x-dimension, y-dimension, version#(-v1/-v2/-v3/-v4/-v5)
	 * Prompt user input for the 4 point grid for the query rectangle.
	 * The Western-most grid column error if this is less than 1 or greater than x.
	 * The Southern-most grid row error if this is less than 1 or greater than y.
	 * The Eastern-most grid column error if less than the West (equal is okay) or greater than x.
	 * The Northern-most grid row error if less than the South (equal is okay) or greater than y.
	 * If not 4 legal numbers for user input, exit the program automatically.
	 */
	
	public static void main(String[] args) {
		if(args.length != INPUT_INT_NUMBER) {
			System.err.println("Usage: file, x-dimension, y-dimension, version#");
			System.exit(1);
		} else {
			data = parse(args[0]);
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);
			version = control(args[3], x, y, data);
			boolean stop = false;
			Scanner console = new Scanner(System.in);
			while(!stop) {
				int[] input = input(console);
				System.out.println("population of rectangle: " + 
									version.population(input)); 
				System.out.printf("percent of total population: " + "%.2f%%\n", 
									version.percentage());
			}
		}
	}
	
	/**
	 * handles and initialized the version choice
	 * @param version number
	 * @param x column
	 * @param y row
	 * @return the version
	 * @throws InterruptedException 
	 */
	public static Version control (String version, int x, int y, CensusData data) {
		switch(version) {
		case "-v1": return new Version1(x, y, data);
		case "-v2": return new Version2(x, y, data);
		case "-v3": return new Version3(x, y, data);
		case "-v4": return new Version4(x, y, data);
		case "-v5": return new Version5(x, y, data);
		default: return new Version1(x, y, data);
		}
	}
	
	/**
	 * handles the user input for west, south, east and north coordinates
	 * and check to see if user want to exit. return the user input back.
	 * @return an array of user input
	 */
	public static int[] input(Scanner console) {
		System.out.println("Please give west, south, east, "
				+ "north coordinates of your query rectangle:");
		String[] input = console.nextLine().split("\\s+");
		if(input.length != INPUT_INT_NUMBER) {
			System.exit(0);
		} 
		return new int[] {Integer.parseInt(input[0]), Integer.parseInt(input[1]),
				Integer.parseInt(input[2]), Integer.parseInt(input[3])};
	}
	
	
	/**
	 * For USMaps interaction purposes
	 * @param filename
	 * @param x
	 * @param y
	 * @param versionNum
	 */
	public static void preprocess(String filename, int x, int y, int versionNum){
		data = parse(filename);
		version = control("-v"+versionNum, x, y, data);
		
	}
	
	/**
	 * For USMaps interaction purposes
	 * @param west
	 * @param south
	 * @param east
	 * @param north
	 * @return Pair of population and percentage
	 */
	public static Pair<Integer, Float> singleInteraction(int w, int s, int e, int n){
		int[] coordinates = {w,s,e,n};
		return new Pair<Integer, Float>(version.population(coordinates), version.percentage());
	}
}
