package comp3350.g16.inventorytracking.acceptance;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AcceptanceTests
{
	public static TestSuite suite;
	
	public static Test suite()
	{
		suite = new TestSuite("Acceptance tests");
		suite.addTestSuite(ItemsTest.class);
		suite.addTestSuite(OrdersTest.class);
		suite.addTestSuite(NewOrdersTest.class);
		return suite;
	}
}
