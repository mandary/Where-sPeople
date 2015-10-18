/**
 * This preprocess the data and return user desired population and its
 * percentage against total population.
 * (Note: Lock based 4 thread preprocessing, with O(1) time answering query.)
 * @author Yufang Sun, Boren Li
 *
 */

public class Version5 extends Version2 {
	private int[][] pinpoint;
	private static final int THREAD_NUM = 4;
	
	/** Constructor throw illegal argument exception if any argument
	 * is less than 1
	 * Takes in data and preprocess it into desired columns and rows
	 * on a rectangle/map, each grid within contains its population.
	 * @param column
	 * @param row
	 * @param census data
	 */
	public Version5(int column, int row, CensusData data)  {
		super(column, row, data);
		pinpoint = calGrid(data);
		CommonMethod.UpdatePop(data, pinpoint, new int[]{x, y});
	}
	
	/**
	 * (Four thread lock-based processing)
	 * @param data
	 * @return the calculated grid
	 */
	public int[][] calGrid(CensusData data){
		GridThread[] gt = new GridThread[THREAD_NUM];
		int[][] result = new int[y + 1][x + 1];
		int[] xOrY =  new int[]{x, y};
		String[][] lock = new String[y + 1][x + 1];
		for(int i = 0; i < y + 1; i++) {
			for(int j = 0; j < x + 1; j++) {
				lock[i][j] = i + "," + j;
			}
		}
		for(int i = 0; i < THREAD_NUM - 1; i++) {
			int lo = i * (data.data_size / THREAD_NUM);
			int hi = (i + 1) * (data.data_size / THREAD_NUM);
			gt[i] = new GridThread(lo, hi, lock, result, map, xOrY, data);
			gt[i].start();
		}
		int lo = (THREAD_NUM -1 ) * (data.data_size / THREAD_NUM);
		gt[THREAD_NUM - 1] = new GridThread(lo, data.data_size, lock, result, map, xOrY, data);
		gt[THREAD_NUM - 1].start();
		for(int i = 0; i < THREAD_NUM; i++) {
			try {
				gt[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		return result;
	}
	
	@Override
	/**
	 * Use the preprocessing method to calculate user demanded population
	 * Illegal input, illegalArgumentException. (Detail in V1)
	 */
	public int population(int[] input) {
		checkInput(input);
		return wanted = CommonMethod.populationForV345(input, pinpoint, new int[]{x, y});
	}

}
