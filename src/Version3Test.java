import org.junit.BeforeClass;

/**
 * Extends Version1Test but with Version3 initialization
 * @author Yufang Sun, Boren Li
 *
 */

public class Version3Test extends Version1Test{

	@BeforeClass
	public static void setUpBeforeClass() {
		data = PopulationQuery.parse("test.txt");
		test = new Version3(10, 20, data);
		census = PopulationQuery.parse("CenPop2010.txt");
		standard = new Version3(20, 25, census);
	}

}
