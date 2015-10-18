import org.junit.BeforeClass;


/**
 * Extends Version1Test but with Version2 initialization
 * @author Yufang Sun, Boren Li
 *
 */

public class Version2Test extends Version1Test{
	
	@BeforeClass
	public static void setUpBeforeClass() {
		data = PopulationQuery.parse("test.txt");
		test = new Version2(10, 20, data);
		census = PopulationQuery.parse("CenPop2010.txt");
		standard = new Version2(20, 25, census);
	}
}
