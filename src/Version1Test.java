import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Version 1 test
 * @author Yufang Sun, Boren Li
 *
 */
public class Version1Test {
	private final static int TIMEOUT = 2000;
	private final static double EPS = 0.0001;
	private final static int PERCENT = 100;
	protected static Version test;
	protected static CensusData data;
	protected static Version standard;
	protected static CensusData census;
	private int[] input1 = new int[] {1, 1, 10, 20};
	private int[] input2 = new int[] {1, 1, 5, 5};
	private int[] input3 = new int[] {1, 1, 20, 25};
	private int[] input4 = new int[] {1, 1, 5, 4};
	private int[] input5 = new int[] {1, 1, 20, 4};
	private int[] input6 = new int[] {1, 12, 9, 25};

	@BeforeClass
	public static void setUpBeforeClass() {
		data = PopulationQuery.parse("test.txt");
		test = new Version1(10, 20, data);
		census = PopulationQuery.parse("CenPop2010.txt");
		standard = new Version1(20, 25, census);
	}
	
	/**
	 * Test for Grid method
	 */
	@Test(timeout = TIMEOUT)
	public void testGridWest() {
		assertEquals("West should be -86.486916", Float.compare(test.map.left,
				(float)-86.486916), 0);
	}
	@Test(timeout = TIMEOUT)
	public void testGridEast() {
		assertEquals("East should be -86.42121", Float.compare(test.map.right, 
				(float)-86.42121), 0);
	}
	
	@Test(timeout = TIMEOUT)
	public void testGridTop() {
		assertEquals("North should be 0.5999871", Float.compare(test.map.top, 
				(float)0.5999871), 0);
	}
	
	@Test(timeout = TIMEOUT)
	public void testGridBottom() {
		assertEquals("South should be 0.5990094", Float.compare(test.map.bottom, 
				(float)0.5990094), 0);
	}
	
	@Test(timeout = TIMEOUT)
	public void testGridPopulation() {
		assertEquals("Population: 26275", test.map.population, 26275);
	}
	
	/**
	 * Test for US map correctness
	 */
	@Test(timeout = TIMEOUT)
	public void testMapWest() {
		assertEquals("West should be -173.033", Float.compare(standard.map.left,
				(float)-173.033), 0);
	}
	@Test(timeout = TIMEOUT)
	public void testMapEast() {
		assertEquals("East should be -65.30086", Float.compare(standard.map.right, 
				(float)-65.30086), 0);
	}
	
	@Test(timeout = TIMEOUT)
	public void testMapTop() {
		assertEquals("North should be 1.8039697", Float.compare(standard.map.top, 
				(float)1.8039697), 0);
	}
	
	@Test(timeout = TIMEOUT)
	public void testMapBottom() {
		assertEquals("South should be 0.31838202", Float.compare(standard.map.bottom, 
				(float)0.31838202), 0);
	}
	
	@Test(timeout = TIMEOUT)
	public void testMapPopulation() {
		assertEquals("Population: 312471327", standard.map.population, 312471327);
	}
	
	/**
	 * Test the population method and percentage
	 */
	@Test(timeout = TIMEOUT)
	public void testTotalPopulation() {
		assertEquals("Population is 26275", test.population(input1), test.map.population);
	}
	
	@Test//(timeout = TIMEOUT)
	public void testTotalPercentage() {
		test.population(input1);
		assertEquals("It should be 100", test.percentage(), 100, 0);
	}
	
	@Test(timeout = TIMEOUT)
	public void testPartialPopulation() {
		assertEquals("Population is 3668", test.population(input2), 3668);
	}

	@Test(timeout = TIMEOUT)
	public void testPartialPercentage() {
		test.population(input2);
		assertEquals("It should be 13.96", test.percentage(), (float)PERCENT*3668/26275, 0);
	}
	
	/**
	 * Test for standard given output
	 */
	@Test(timeout = TIMEOUT)
	public void standardSpecTotal() {
		assertEquals("Population is 312471327", standard.population(input3), 312471327);	
	}
	
	@Test(timeout = TIMEOUT)
	public void standardSpecTotalPercentage() {
		standard.population(input3);
		assertEquals("Percentage should be 100", standard.percentage(), 
				(float)PERCENT*312471327/312471327, EPS);	
	}
	
	@Test(timeout = TIMEOUT)
	public void standardSpecHawaii() {
		assertEquals("Population is 1360301", standard.population(input4), 1360301);
	}
	
	@Test(timeout = TIMEOUT)
	public void standardSpecHawaiiPercentage() {
		standard.population(input4);
		assertEquals("Population is 0.44", standard.percentage(), (float)PERCENT*1360301/312471327, EPS);
	}
	
	@Test(timeout = TIMEOUT)
	public void standardSpecBottom4() {
		assertEquals("Population is 36493611", standard.population(input5), 36493611);
	}
	
	@Test(timeout = TIMEOUT)
	public void standardSpecBottom4Percentage() {
		standard.population(input5);
		assertEquals("Population is 11.68", standard.percentage(), (float)PERCENT*36493611/312471327, EPS);
	}
	
	@Test(timeout = TIMEOUT)
	public void standardSpecAlaska() {
		assertEquals("Population is 710231", standard.population(input6), 710231);
	}
	
	@Test(timeout = TIMEOUT)
	public void standardSpecAlaskaPercentage() {
		standard.population(input6);
		assertEquals("Percentage is 0.23", standard.percentage(), (float)PERCENT*710231/312471327, EPS);
	}
	
	/**
	 * Test for illegal argument in constructor
	 */
	@Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
	public void testNegativeArgOne() {
		test.illegalArg(-1, 5);
	}
	
	@Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
	public void testNegativeArgTwo() {
		test.illegalArg(-1, 0);
	}
	
	@Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
	public void testCheckInputWest() {
		test.checkInput(new int[]{0, 1, 5, 5});
	}
	
	@Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
	public void testCheckInputSouth() {
		test.checkInput(new int[]{1, 0, 5, 5});
	}
	
	@Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
	public void testCheckInputEast() {
		test.checkInput(new int[]{1, 1, 0, 5});
	}
	
	@Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
	public void testCheckInputNorth() {
		test.checkInput(new int[]{1, 1, 5, 0});
	}
}