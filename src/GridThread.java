
/** Computes and return the rectangle of the whole map and its population within
 * (Process the whole data with 4 threads and all fill in one grid with lock control.)
 * @author Yufang Sun, Boren Li
 */
public class GridThread extends java.lang.Thread{
	private int lo; 
	private int hi;
	private int[][] grid;
	private CensusData data;
	private String[][] lock;
	Rectangle map;
	int[] xOrY;
	
	/**
	 * It takes 
	 * @param low
	 * @param high
	 * @param lock
	 * @param result
	 * @param map
	 * @param x and y
	 * @param census
	 */
	public GridThread(int low, int high, String[][] lk, int[][] result, Rectangle rect, int[] needed, CensusData census) {
		lo = low;
		hi = high;
		grid = result;
		xOrY = needed;
		map = rect;
		data = census;
		lock = lk;
	}
	
	/**
	 * Compute the grid for population.
	 */
	public void run() {
		float xdis = (map.right - map.left) / xOrY[0];
		float ydis = (map.top - map.bottom) / xOrY[1];
		for(int i = lo; i < hi; i++) { 
			CensusGroup group = data.data[i];   
			int colGrid = CommonMethod.process(group.longitude, map.left, xdis, xOrY[0]);
		    int rowGrid = CommonMethod.process(map.top, group.latitude, ydis, xOrY[1]);
	   	    synchronized(lock[rowGrid][colGrid]) {
				grid[rowGrid][colGrid] += group.population;	
			}
		}
			
	}

}
