import java.util.concurrent.ForkJoinPool;

/**
 * This is written for recommended testing, but note we did not use
 * this for the timing experiment. We did not warm up in the for loop,
 * instead, we just insert begin and ending time during run time in the 
 * actual method.
 * @author Yufang Sun
 *
 */
public class Test {
	public static final int NUM_TEST = 40;
	public static final int NUM_WARMUP = 20;
	private static ForkJoinPool fjPool = new ForkJoinPool();
	private static final int XDIM = 100;
	private static final int YDIM = 500;
	
	public static void main(String[] args) {
		CensusData data = PopulationQuery.parse("CenPop2010.txt");
		Rectangle map = CommonMethod.grid(data, data.data_size, 0);
		double test1A = V1FindCorner(data);
		double test1B = V2FindCorner(data);
		double test2A = V3GridBuilding(data, map, XDIM, YDIM);
		double test2B = V4GridBuildingMerging(data, map, XDIM, YDIM);
		double test3 = Query(data, XDIM, YDIM);
		System.out.println(test1A);
		System.out.println(test1B);
		System.out.println(test2A);
		System.out.println(test2B);
		System.out.println(test3);
	}
	
	private static double V1FindCorner(CensusData data) {
	    double totalTime = 0;
	    for (int i=0; i<NUM_TEST; i++) {
	      long startTime = System.currentTimeMillis();
	      CommonMethod.grid(data, data.data_size, 0);
	      long endTime = System.currentTimeMillis();
	      if (NUM_WARMUP <= i) {                    // Throw away first NUM_WARMUP runs to exclude JVM warmup
	        totalTime += (endTime - startTime);
	      }
	    }
	    return totalTime / (NUM_TEST - NUM_WARMUP);  // Return average runtime.
	}
	
	private static double V2FindCorner(CensusData data) {
	    double totalTime = 0;
	    for (int i=0; i<NUM_TEST; i++) {
	      long startTime = System.currentTimeMillis();
	      fjPool.invoke(new GridP(data, data.data_size, 0));
	      long endTime = System.currentTimeMillis();
	      if (NUM_WARMUP <= i) {                    // Throw away first NUM_WARMUP runs to exclude JVM warmup
	        totalTime += (endTime - startTime);
	      }
	    }
	    return totalTime / (NUM_TEST - NUM_WARMUP);  // Return average runtime.
	}
	
	private static double V3GridBuilding(CensusData data, Rectangle map, int x, int y) {
	    double totalTime = 0;
	    for (int i=0; i<NUM_TEST; i++) {
	      long startTime = System.currentTimeMillis();
	      CommonMethod.gridPop(data, map, new int[]{x,y}, 0, data.data_size);
	      long endTime = System.currentTimeMillis();
	      if (NUM_WARMUP <= i) {                    // Throw away first NUM_WARMUP runs to exclude JVM warmup
	        totalTime += (endTime - startTime);
	      }
	    }
	    return totalTime / (NUM_TEST - NUM_WARMUP);  // Return average runtime.
	}
	
	private static double V4GridBuildingMerging(CensusData data, Rectangle map, int x, int y) {
		Version4 test = new Version4(x, y, data);
	    double totalTime = 0;
	    for (int i=0; i<NUM_TEST; i++) {
	      long startTime = System.currentTimeMillis();
	      test.fjPool.invoke(new GridResolve(0, data.data_size, data, map, new int[]{x, y}));
	      long endTime = System.currentTimeMillis();
	      if (NUM_WARMUP <= i) {                    // Throw away first NUM_WARMUP runs to exclude JVM warmup
	        totalTime += (endTime - startTime);
	      }
	    }
	    return totalTime / (NUM_TEST - NUM_WARMUP);  // Return average runtime.
	}
	
	private static double Query(CensusData data, int x, int y) {
		Version4 test = new Version4(x, y, data); //Here version is interchangeable
	    double totalTime = 0;
	    for (int i=0; i<NUM_TEST; i++) {
	      long startTime = System.currentTimeMillis();
	      test.population(new int[] {1, 1, 100, 500});
	      test.percentage();
	      long endTime = System.currentTimeMillis();
	      if (NUM_WARMUP <= i) {                    // Throw away first NUM_WARMUP runs to exclude JVM warmup
	        totalTime += (endTime - startTime);
	      }
	    }
	    return totalTime / (NUM_TEST - NUM_WARMUP);  // Return average runtime.
	}
	
}
