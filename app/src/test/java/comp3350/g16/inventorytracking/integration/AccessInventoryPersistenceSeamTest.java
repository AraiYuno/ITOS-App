package comp3350.g16.inventorytracking.tests.integration;

import junit.framework.TestCase;

import java.util.List;

import comp3350.g16.inventorytracking.application.Services;
import comp3350.g16.inventorytracking.business.AccessInventory;
import comp3350.g16.inventorytracking.business.AccessOrders;
import comp3350.g16.inventorytracking.objects.Item;
import comp3350.g16.inventorytracking.objects.Order;

public class AccessInventoryPersistenceSeamTest extends TestCase
{
	
	public AccessInventoryPersistenceSeamTest( String arg0)
	{
		super(arg0);
	}
	
	public void testAccessInventory()
	{
		AccessInventory ai;
		Item item;
		List<Item> list;
		
		int defaultSize = 22;
		
		Services.createDatabaseAccess();
		
		
		// CRUD -- Create, Read, Update, Delete
		
		System.out.println("Starting Integration test of AccessInventory to Persistence");
		
		ai = new AccessInventory();
		
		// items should already be in the default database
		item = ai.search("80361141");
		assertNotNull(item);
		assertEquals(defaultSize, ai.size() );
		
		
		// ===== adding items ===== //
		assertFalse( ai.addItem(null) );		// can't add null
		assertFalse( ai.addItem(new Item("c", "n", "80361141", 10,10)) );	// this item exists
		
		assertTrue ( ai.addItem(new Item("cat", "nom", "jksajkhdhjsdhj", 0,0)) );
		assertFalse( ai.addItem(new Item("cat", "nom", "jksajkhdhjsdhj", 0,0)) ); // adding duplicate
		
		assertFalse( ai.addItem(new Item("", "", "", 0, 0)) );		// adding empty items
		assertFalse( ai.addItem(new Item("c", "n", "i", -1, -1)) );	// invalid parameters
		
		// ===== search items ===== //
		assertNull(ai.search(null));
		assertNull(ai.search(""));
		
			// just added this item
		item = ai.search("jksajkhdhjsdhj");
		assertNotNull(item);
		assertEquals(item.getProductID(), "jksajkhdhjsdhj");
		assertNotNull( ai.search(item.getProductID().toUpperCase()) );
		
		
		// ===== remove items ===== //
		assertNull( ai.removeItem(null) );
		assertNull( ai.removeItem("") );
		
			// remove and search for item
		item = ai.removeItem("jksajkhdhjsdhj");
		assertNotNull( item );
		assertNull( ai.search("jksajkhdhjsdhj") );
		assertEquals( defaultSize, ai.size() );	 // item added, item removed == no size change
		
		// ===== list of items ===== //
			// testing the search by name function
		list = ai.searchByName(null);
		assertNull(list);
		
		list = ai.searchByName("item name does not exist");
		assertNull(list);
		
		list = ai.searchByName("jane eyre");
		assertNotNull(list);
		assertEquals( 2, list.size() );
		list = ai.searchByName("JANE EYRE");
		assertNotNull(list);
		assertEquals( 2, list.size() );
		
			// getting a list of items
		ai.getList(null);	// this shouldn't break lol
		int currListSize = list.size();
		ai.getList(list);
		assertNotSame( list.size()+currListSize, list.size() );	// assert new list was made ie list was not appended
		assertEquals(defaultSize, list.size() );
		
		System.out.println("Finished Integration test of AccessInventory to Persistence\n");
	}
	
	/*		Inventory Methods
    public Item search(String productID)
    public void getList(List<Item> list )
    public boolean addItem(Item newItem)
    public Item removeItem(String productID)
    public List<Item> searchByName(String name)
    public int size()
	
	 */
	
}
