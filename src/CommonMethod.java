/**
 * This stores common used methods.
 * @author Yufang Sun, Boren Li
 *
 */
public class CommonMethod {

	/**
	 * 
	 * Computes the whole data's map coordinates and population
	 * @param data
	 * @param hi
	 * @param lo
	 * @return the rectangle computed
	 */
	public static Rectangle grid(CensusData data, int hi, int lo) {
		float lon = data.data[lo].longitude;
		float lat = data.data[lo].latitude;
		int pop = data.data[lo].population;
		Rectangle map = new Rectangle(lon, lon, lat, lat, pop);
		
		for(int i = lo + 1; i < hi; i++) {
			lon = data.data[i].longitude;
			lat = data.data[i].latitude;
			pop = data.data[i].population;
			map = map.encompass(new Rectangle(lon, lon, lat, lat, pop));
		}
		
		return map;
	}
	
	/**
	 * Return the population if it belongs to the map area.
	 * @param map
	 * @param census
	 * @param user input
	 * @param xOrY
	 * @param hi
	 * @param lo
	 * @return total population
	 */
	public static int population(Rectangle map, CensusData census, int input[], int[] xOrY, int hi, int lo){
		float xdis = (map.right - map.left) / xOrY[0];
		float ydis = (map.top - map.bottom) / xOrY[1];
		int pop = 0;
		for(int i = lo; i < hi; i++) {
			CensusGroup group = census.data[i];
			int lon = process(group.longitude, map.left, xdis, xOrY[0]); // lon means which grid it goes into
			int lat = process(group.latitude, map.bottom, ydis, xOrY[1]);
			if(lon >= input[0] && lon <= input[2] 
					&& lat >= input[1] && lat <= input[3]) {
				pop += group.population;
			}
		}
		return pop;
	}
	
	/**
	 * Preprocess the longitude and latitude to fit in the user given grid
	 * static for parallelism
	 * @param number
	 * @param bound
	 * @param dis
	 * @param grid
	 * @return the processed longitude or latitude
	 */
	public static int process(float number, float bound, float dis, int grid) {
		return Math.min((int)((number - bound)/dis) + 1, grid);

	}
	
	
	/**
	 * Return the population grid. Each grid contains its correct population
	 * @param data
	 * @param map
	 * @param xOrY
	 * @param lo
	 * @param hi
	 * @return the grid
	 */
	public static int[][] gridPop(CensusData data, Rectangle map, int[]xOrY, int lo, int hi) {
		int[][] result = new int[xOrY[1] + 1][xOrY[0] + 1];
		float xdis = (map.right - map.left) / xOrY[0];
		float ydis = (map.top - map.bottom) / xOrY[1];
		for(int i = lo; i < hi; i++) { 
			CensusGroup group = data.data[i];
			int colGrid = process(group.longitude, map.left, xdis, xOrY[0]);
	   	    int rowGrid = process(map.top, group.latitude, ydis, xOrY[1]);
			
			result[rowGrid][colGrid] += group.population;	
		}
		return result;
	}
	
	/**
	 * Update the grid and prepare it for answering query
	 * @param data
	 * @param pinpoint
	 * @param xOrY
	 */
	public static void UpdatePop(CensusData data, int[][]pinpoint, int[] xOrY) {
		for(int i = 1; i <= xOrY[1]; i++) {   
			for(int j = 1; j <= xOrY[0]; j++) {
				int left = (j != 1) ? pinpoint[i][j - 1] : 0; 
				int above = (i != 1) ? pinpoint[i - 1][j] : 0; 
				int diagonal = (i != 1 && j != 1) ?  pinpoint[i - 1][j - 1] : 0;
				pinpoint[i][j] = pinpoint[i][j] + left + above - diagonal;
			}
		}	
		
	}
	/**
	 * Return the correct population that user demands
	 * @param input
	 * @param pinpoint
	 * @param xOrY
	 * @return population
	 */
	public static int populationForV345(int[] input, int[][]pinpoint, int[] xOrY) {
		int west = input[0];
		int south = xOrY[1] - input[1] + 1;
		int east = input[2];
		int north = xOrY[1] - input[3] + 1;
		int left = (west != 1) ? pinpoint[south][west - 1] : 0;
		int top = (north != 1) ? pinpoint[north - 1][east] : 0;
		int diagonal = (west != 1 && north != 1) ? pinpoint[north - 1][west - 1] : 0;
		return pinpoint[south][east] - left - top + diagonal;
	}
	
	
}
