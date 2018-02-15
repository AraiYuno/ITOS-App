package comp3350.g16.inventorytracking.tests.persistence;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Test;

import comp3350.g16.inventorytracking.objects.Item;
import comp3350.g16.inventorytracking.persistence.InventorySQL;



public class InventorySQLTest {

    private InventorySQL inventorySQL;
    private Item item1;
    private Item item2;
    private Item item3;

    @Before
    public void init() throws Exception
    {
        inventorySQL = new InventorySQL();
        item1 = new Item("game", "name", "126-abcfd", 5, 59.99);
        item2 = new Item("book", "wilde", "123475-8800", 1, 20.99);
        item3 = new Item("console", "some", "789-abc", 2, 59.99);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        System.out.println("<Start of InventorySQLTest>\n");
    }

    @After
    public void tearDown() throws Exception
    {
        inventorySQL = null;
        item1 = null;
        item2 = null;
        item3 = null;
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        System.out.println("\n<End of InventorySQLTest>");
    }

    @Test
    public void testAddItem()
    {
        //not testing for null entries because Item
        //will be validated before it is inserted into the database
        //given that the stub database has 12 default items as its content
        System.out.println("<Testing addItem>");
        System.out.println("3 items to add");

        System.out.println("Addition of item1");
        assertTrue(inventorySQL.addItem(item1));
        System.out.println("\tSUCCESS!");

        System.out.println("Addition of item2 and item3");
        inventorySQL.addItem(item2);
        inventorySQL.addItem(item3);
        assertEquals(15, inventorySQL.size());
        System.out.println("\tSUCCESS!");

        System.out.println("Addition of 10000 items with uniqueIDs");
        for( int i = 0; i < 10000; i++ )
        {
            inventorySQL.addItem(new Item( "VideoGame", "FallOut4", Integer.toString(i),1,  69.99));
        }
        assertEquals(10015, inventorySQL.size());
        System.out.println("\tSUCCESS!");

        System.out.println("<Done testing testAddOrder>\n");
    }

    @Test
    public void testRemoveItem()
    {
        Item removed;

        System.out.println("<Testing addItem>");

        System.out.println("Setup to add item2, item3 and item1, respectively");
        inventorySQL.addItem(item2);
        inventorySQL.addItem(item3);
        inventorySQL.addItem(item1);

        System.out.println("Remove item3");
        removed = inventorySQL.removeItem(item3.getProductID());
        assertEquals(item3.getProductID(), removed.getProductID());
        System.out.println("\tSUCCESS!");

        System.out.println("Remove item1 (last item to be added)");
        removed = inventorySQL.removeItem(item1.getProductID());
        assertEquals(item1.getProductID(), removed.getProductID());
        System.out.println("\tSUCCESS!");

        System.out.println("Remove item2");
        removed = inventorySQL.removeItem(item2.getProductID());
        assertEquals(item2.getProductID(), removed.getProductID());
        System.out.println("\tSUCCESS!");

        System.out.println("Remove very first item in the stub database");
        removed = inventorySQL.removeItem("123-abc");
        assertNotNull(removed);
        System.out.println("\tSUCCESS!");

        System.out.println ("Try to remove the same first item (SHOULD FAIL).");
        removed = inventorySQL.removeItem("123-abc");
        assertNull(removed.getProductID());
        System.out.println("\tFAILED!");

        System.out.println("<Done testing testAddOrder>\n");
    }

    @Test
    public void testSearch()
    {
        Item found;

        System.out.println("<Testing addItem>");

        System.out.println("Testing search for empty string (SHOULD FAIL)");
        found = inventorySQL.search("");
        assertNull(found);
        System.out.println("\tFAILED!");

        System.out.println("Testing for an item hit");
        found = inventorySQL.search("7854-a");
        assertNotNull(found.getProductID());
        System.out.println("\tSUCCESS!");

        System.out.println("Testing for partial hit (SHOULD FAIL)");
        found = inventorySQL.search("785");
        assertNull(found);
        System.out.println("\tFAILED!");

        System.out.println("<Done testing testAddOrder>\n");
    }

    @Test
    public void testUpdateItem()
    {
        boolean updated;

        System.out.println("<Testing updateItem>");

        System.out.println("Setup to add item1");
        inventorySQL.addItem(item1);

//        System.out.println("Given an invalid product id");
//        updated = inventorySQL.updateItem("gh", 10);
//        System.out.println(updated);
//        assertFalse(updated);
//        System.out.println("\tSUCCESS!");
//
        System.out.println("Given value of 0 for update (quantity should stay the same)");
        inventorySQL.updateItem(item1.getProductID(), 0);
        assertEquals(5, item1.getQuantity());
        System.out.println("\tSUCCESS!");

//        System.out.println("Given value of < 0 for update (should decrease or minimum at 0");
//        inventorySQL.updateItem(item1.getProductID(), -3);
//        assertEquals(2, item1.getQuantity());
//        inventorySQL.updateItem(item1.getProductID(), -3);
//        assertEquals(0, item1.getQuantity());
//        System.out.println("\tSUCCESS!");

//        System.out.println("Update with valid ID and valid quantity");
//        updated = inventorySQL.updateItem(item1.getProductID(), 10);
//        assertTrue(updated);
//        assertEquals(15, item1.getQuantity());
//        System.out.println("\tSUCCESS!");
    }
}