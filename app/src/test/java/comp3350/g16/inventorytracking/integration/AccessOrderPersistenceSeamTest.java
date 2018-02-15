package comp3350.g16.inventorytracking.tests.integration;

import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.List;

import comp3350.g16.inventorytracking.application.Services;
import comp3350.g16.inventorytracking.business.AccessOrders;
import comp3350.g16.inventorytracking.objects.Order;
import comp3350.g16.inventorytracking.objects.Status;

public class AccessOrderPersistenceSeamTest extends TestCase
{
	public AccessOrderPersistenceSeamTest( String arg0)
	{
		super(arg0);
	}
	
	public void testAccessOrder()
	{
		AccessOrders ao;
		Order order;
		List<Order> list;
		
		int defaultSize = 10;
		int newSize = defaultSize;
		int defaultOrderNumber = 99999;
		
		Services.createDatabaseAccess();
		ao = new AccessOrders();
		
		System.out.println("Starting Integration test of AccessOrder to Persistence");
		
		assertEquals(defaultSize, ao.size() );
		
		// === adding orders ===
		assertFalse( ao.addOrder(null) );
		
		assertTrue( ao.addOrder(new Order() ) );
		newSize++;
		assertEquals( newSize, ao.size() );	// order actually added
		
		// === searching for orders ===
		assertNull( ao.search(-1) );
		
		assertEquals(defaultOrderNumber+1, ao.search(defaultOrderNumber+1).getOrderNumber() );
		assertEquals( newSize, ao.size() );		// searching doesn't change the size
		
		// === getting lists ===
		list = new LinkedList<>();
		ao.getList(list);
		assertNotNull( list );
		assertEquals( newSize, list.size() );
		
			// testing that getting these orders does not mean the underlying order list is changed
		list = ao.getStatusOrdersWithout(Status.NEW);
		assertNotSame(ao.size(), list.size() );	// not all the orders are set to "new"
		
		System.out.println("Finished Integration test of AccessOrder to Persistence\n");
	}
	
	/*		Order methods
	void getList(List<Order> orderList)
    List<Order> getStatusOrdersWithout( Status removedStatus )
    Order search(int orderNumber)
    boolean addOrder(Order newOrder)
	 */
	
}
