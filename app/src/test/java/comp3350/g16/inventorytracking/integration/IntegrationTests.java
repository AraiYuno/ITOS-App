package comp3350.g16.inventorytracking.tests.integration;

import junit.framework.Test;
import junit.framework.TestSuite;

public class IntegrationTests
{
	public static TestSuite suite;
	
	public static Test suite()
	{
		suite = new TestSuite("Integration tests");
		suite.addTestSuite(AccessInventoryPersistenceSeamTest.class);
		suite.addTestSuite(AccessOrderPersistenceSeamTest.class);
		
		return suite;
	}
}
