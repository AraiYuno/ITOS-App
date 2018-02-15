package comp3350.g16.inventorytracking.tests.persistence;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Test;

import comp3350.g16.inventorytracking.objects.Item;
import comp3350.g16.inventorytracking.objects.Order;
import comp3350.g16.inventorytracking.objects.Status;
import comp3350.g16.inventorytracking.persistence.OrderQueue;


public class OrderQueueTest {
    private OrderQueue orderQueue;
    private Order order1 = new Order();
    private Order order2 = new Order();
    private Order order3 = new Order();

    @Before
    public void init() throws Exception {

        // the way an OrderQueue instance is initialized, it contain a default of 15 orders
        orderQueue = new OrderQueue();

        // then we create 3 more orders on top of that for testing
        order1.placeOrder(new Item("game", "name", "123-abc", 5, 59.99));
        order1.placeOrder(new Item("game", "video", "456-cvb", 3, 69.99));
        order2.placeOrder(new Item("book", "wilde", "123475-8800", 1, 20.99));
        order3.placeOrder(new Item("console", "some", "789-abc", 2, 59.99));
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.out.println("<Start of OrderQueueTest>\n");
    }

    @After
    public void tearDown() throws Exception {
        orderQueue.clear();
        order1 = null;
        order2 = null;
        order3 = null;
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("\n<End of OrderQueueTest>");
    }

    @Test
    public void testAddOrder()
    {
        //default size of orderQueue is 15 from stub database
        System.out.println("<Testing addOrder>");
        System.out.println("3 orders to add");

        System.out.println("Addition of order1");
        assertTrue(orderQueue.addOrder(order1));
        System.out.println("\tSUCCESS!");

        System.out.println("Addition of order2 and order3");
        orderQueue.addOrder(order2);
        orderQueue.addOrder(order3);
        assertEquals(18, orderQueue.size());
        System.out.println("\tSUCCESS!");

        System.out.println("Addition of 10000 order1's");
        for (int count = 0; count <10000 ; count ++ ){
            orderQueue.addOrder(order1);
        }
        assertEquals(10018, orderQueue.size());

        System.out.println("<Done testing testAddOrder>\n");

    }


    @Test
    public void testRemoveOrder()
    {
        int countRemoved = 0;
        boolean removedOrder;
        orderQueue.addOrder(order3);
        orderQueue.addOrder(order1);
        orderQueue.addOrder(order2);

        System.out.println("<Testing removeOrder>");
        System.out.println("Adding 3 orders - order3, order1, order2, respectively");
        System.out.println("Removing by order1");
        removedOrder = orderQueue.removeOrder(order1.getOrderNumber());
        assertTrue(removedOrder);
        System.out.println("\tSUCCESS");

        System.out.println("Removing by order2");
        removedOrder = orderQueue.removeOrder(order2.getOrderNumber());
        assertTrue(removedOrder);
        System.out.println("\tSUCCESS");

        System.out.println("Removing by order3");
        removedOrder = orderQueue.removeOrder(order3.getOrderNumber());
        assertTrue(removedOrder);
        System.out.println("\tSUCCESS");

        System.out.println("Attempt to remove order1 again (SHOULD FAIL)");
        removedOrder = orderQueue.removeOrder(order1.getOrderNumber());
        assertFalse(removedOrder);
        System.out.println("\tFAILED!");

        System.out.println("Removing all orders from stub database (expecting 15 to be removed)");
        while (orderQueue.size() > 0) {
            orderQueue.removeOrder();
            countRemoved++;
        }
        assertEquals(0, orderQueue.size());
        assertEquals(15, countRemoved);
        System.out.println("\tSUCCESS!");

        System.out.println("Removing from empty list (SHOULD FAIL)");
        orderQueue.removeOrder();
        assertEquals(0, orderQueue.size());
        System.out.println("\tFAILED!");

        System.out.println("<Done testing removeOrder>\n");
    }

    @Test
    public void testChangeOrderStatus() {
        System.out.println("<Testing changeOrderStatus>");
        orderQueue.addOrder(order1);

        System.out.println("Given (order1 number, Status.NEW), expected result: no change first order");
        orderQueue.changeOrderStatus(order1.getOrderNumber(), Status.NEW);
        assertEquals(Status.NEW, order1.getStatus());
        System.out.println("\tSUCCESS!");

        System.out.println("Given (order1, Status.APPROVED), expected result: order1 with APPROVED status");
        orderQueue.changeOrderStatus(order1.getOrderNumber(), Status.APPROVED);
        assertEquals(Status.APPROVED, order1.getStatus());
        System.out.println("\tSUCCESS!");

        System.out.println("Given (order1, Status.REJECTED), expected result: order1 with REJECTED status");
        orderQueue.changeOrderStatus(order1.getOrderNumber(), Status.REJECTED);
        assertEquals(Status.REJECTED, order1.getStatus());
        System.out.println("\tSUCCESS!");

        System.out.println("Given (order1, Status.COMPLETED), expected result: order1 with COMPLETED status");
        orderQueue.changeOrderStatus(order1.getOrderNumber(), Status.COMPLETED);
        assertEquals(Status.COMPLETED, order1.getStatus());
        System.out.println("\tSUCCESS!");

        System.out.println("Given (order1, Status.CANCELLED), expected result: order1 with CANCELLED status");
        orderQueue.changeOrderStatus(order1.getOrderNumber(), Status.CANCELLED);
        assertEquals(Status.CANCELLED, order1.getStatus());
        System.out.println("\tSUCCESS!");

        System.out.println("Given (order1, Status.NEW), expected result order1 back to NEW status");
        orderQueue.changeOrderStatus(order1.getOrderNumber(), Status.NEW);
        assertEquals(Status.NEW, order1.getStatus());
        System.out.println("\tSUCCESS!");

        System.out.println("<Done testing changeOrderStatus>\n");
    }
}
