package comp3350.g16.inventorytracking.persistence;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import comp3350.g16.inventorytracking.objects.Item;
import comp3350.g16.inventorytracking.objects.Order;
import comp3350.g16.inventorytracking.objects.Status;

//--------------------------------------------------------------------
//	OrderQueue
//      A list of all orders
//--------------------------------------------------------------------
public class OrderQueue implements OrderInterface
{
    private LinkedList<Order> orders;
    private int size;
    
    
    // TODO: 2017-07-17 need to change methods to account for this
    // a counter for a UNIQUE order number
    private static int orderNumber = 99999;
    //--------------------------------------------------------------------
    //	Constructor
    //--------------------------------------------------------------------
    public OrderQueue()
    {
        orders = new LinkedList<Order>();
        size = 0;

        initialize();
    }

    //--------------------------------------------------------------------
    //	addOrder:		adds an Order to the queue
    //					Returns true if successfully added, false if not
    //--------------------------------------------------------------------
    public boolean addOrder(Order newOrder)
    {
        boolean orderAdded = false;

        if( newOrder != null )
        {
            orderAdded = orders.add(newOrder);

            if( orderAdded)
                size++;
        }

        return orderAdded;
    }

    //--------------------------------------------------------------------
    //	removeOrder:		removes an Order from the queue
    //						Returns item if removednull if (!found || isEmpty)
    //--------------------------------------------------------------------
    public Order removeOrder()
    {
        Order orderRemoved = orders.poll();

        if( orderRemoved != null )
            size--;

        return orderRemoved;
    }

      //--------------------------------------------------------------------
    //	removeOrder:		Removes an order by position
    //						Returns item if removed, null if (index < 0 || index >= size)
    //--------------------------------------------------------------------
    public boolean removeOrder(int orderNum)
    {
        boolean orderRemoved = false;
        Order orderFound = search(orderNum);

        if(orderFound != null)
        {
            orders.remove(orderFound);
            orderRemoved = true;
            size--;
        }

        return orderRemoved;
    }

    //--------------------------------------------------------------------
    //	search:		searches for an order by order number
    //--------------------------------------------------------------------
    public Order search(int orderNum)
    {
        ListIterator<Order> orderIter = getOrderPosn(orderNum);
        Order orderFound = null;

        if( orderIter != null && orderIter.hasNext() )
            orderFound = orderIter.next();

        return orderFound;
    }

    //--------------------------------------------------------------------
    //	changeOrderStatus	Given the order number, this will change the
    //						status of the order in the queue
    //--------------------------------------------------------------------
    public void changeOrderStatus(int orderNum, Status newStatus)
    {
        ListIterator<Order> orderIter = getOrderPosn(orderNum);
        Order currOrder;

        if( orderIter.hasNext() )
        {
            currOrder = orderIter.next();
            currOrder.setOrderStatus(newStatus);
        }
    }

    public void getList(List<Order> orderList)
    {
        if( orderList != null )
        {
            orderList.clear();
            orderList.addAll(this.orders);
        }
    }
    //--------------------------------------------------------------------
    //	getOrderPosn	Searches for an order in the queue by orderNum
    //
    //					Returns a ListIterator where next() will return
    //					the searched item.
    //					If item is not in list, then hasNext() is false.
    //--------------------------------------------------------------------
    private ListIterator<Order> getOrderPosn(int orderNum)
    {
        ListIterator<Order> orderIter = orders.listIterator();
        Order currOrder;
        boolean orderFound = false;

        while( orderIter.hasNext() && !orderFound )
        {
            currOrder = orderIter.next();

            if( orderNum == currOrder.getOrderNumber() )
            {
                orderIter.previous();
                orderFound = true;
            }
        }

        return orderIter;
    }

    //	TODO	remove on hand in
    public String toString()
    {
        ListIterator<Order> ordersIter = orders.listIterator();
        String orderListString = "LIST OF ORDERS";

        while( ordersIter.hasNext() )
        {
            orderListString += "\n"+ordersIter.next().orderDetails();
        }

        return orderListString;
    }

    public int size()
    {
        return size;
    }
    public void clear()
    {
        orders.clear();
    }
    public boolean isEmpty()
    {
        return orders.isEmpty();
    }

    private void initialize()
    {
        Order order1 = new Order();
        order1.placeOrder(new Item("CATEGORY1", "NAME1", "123456789", 10, 58.9));
        order1.placeOrder(new Item("CATEGORY2", "NAME2", "345678912", 5, 5.9));
        order1.placeOrder(new Item("CATEGORY4", "NAME3", "567891234", 1, 580.9));
        order1.placeOrder(new Item("CATEGORY1", "NAME4", "456789987", 100, 0.99));
        order1.placeOrder(new Item("CATEGORY2", "NAME5", "678912644", 5, 5.9));
        order1.placeOrder(new Item("CATEGORY2", "NAME6", "951235689", 1, 4.96));
        addOrder(order1);
        
        Order order2 = new Order();
        order2.placeOrder(new Item("CATEGORY1", "NAME1", "1", 1, 1));
        order2.placeOrder(new Item("CATEGORY1", "NAME2", "2", 2, 2));
        order2.placeOrder(new Item("CATEGORY1", "NAME3", "3", 3, 3));
        order2.placeOrder(new Item("CATEGORY1", "NAME4", "4", 4, 4));
        order2.placeOrder(new Item("CATEGORY1", "NAME5", "5", 5, 5));
        order2.placeOrder(new Item("CATEGORY1", "NAME6", "6", 6, 6));
        order2.placeOrder(new Item("CATEGORY1", "NAME7", "7", 7, 7));
        order2.placeOrder(new Item("CATEGORY1", "NAME8", "8", 8, 8));
        order2.placeOrder(new Item("CATEGORY1", "NAME9", "9", 9, 9));
        order2.placeOrder(new Item("CATEGORY1", "NAME10", "10", 10, 10));
        order2.placeOrder(new Item("CATEGORY1", "NAME11", "11", 11, 11));
        order2.placeOrder(new Item("CATEGORY1", "NAME12", "12", 12, 12));
        order2.placeOrder(new Item("CATEGORY1", "NAME13", "13", 13, 13));
        order2.placeOrder(new Item("CATEGORY1", "NAME14", "14", 14, 14));
        order2.placeOrder(new Item("CATEGORY1", "NAME15", "15", 15, 15));
        order2.placeOrder(new Item("CATEGORY1", "NAME16", "16", 16, 16));
        order2.placeOrder(new Item("CATEGORY1", "NAME17", "17", 17, 17));
        order2.placeOrder(new Item("CATEGORY1", "NAME18", "18", 18, 18));
        addOrder(order2);

        Order order3 = new Order();
        order3.placeOrder(new Item("CATEGORY1", "NAME1", "123456789", 10, 58.9));
        order3.placeOrder(new Item("CATEGORY2", "NAME2", "345678912", 5, 5.9));
        order3.placeOrder(new Item("CATEGORY2", "NAME2", "567891234", 1, 580.9));
        addOrder(order3);
    
        Order order = new Order();
        order.placeOrder(new Item("CATEGORY1", "NAME1", "123456789", 10, 58.9));
        order.placeOrder(new Item("CATEGORY2", "NAME2", "345678912", 5, 5.9));
        order.placeOrder(new Item("CATEGORY2", "NAME2", "567891234", 1, 580.9));
        addOrder(order);
        
        order = new Order();
        order.placeOrder(new Item("CATEGORY1", "NAME1", "989712277", 10, 58.9));
        order.placeOrder(new Item("CATEGORY2", "NAME2", "211232564", 5, 5.9));
        order.placeOrder(new Item("CATEGORY2", "NAME2", "ajsdhbajf", 1, 580.9));
        order.setOrderStatus(Status.CANCELLED);
        addOrder(order);
    
        order = new Order();
        order.placeOrder(new Item("CATEGORY1", "NAME1", "989712277", 10, 70.1));
        order.placeOrder(new Item("CATEGORY2", "NAME2", "211232564", 5, 5.9));
        order.placeOrder(new Item("CATEGORY2", "NAME2", "ajsdhbajf", 1, 58.9));
        addOrder(order);
    
        order = new Order();
        order.placeOrder(new Item("CATEGORY1", "NAME1", "989712277", 10, 7.77));
        order.placeOrder(new Item("CATEGORY2", "NAME2", "211232564", 5, 7.77));
        order.placeOrder(new Item("CATEGORY2", "NAME2", "ajsdhbajf", 1, 7.77));
        order.setOrderStatus(Status.CANCELLED);
        addOrder(order);
    
        order = new Order();
        order.placeOrder(new Item("CATEGORY1", "NAME1", "989712277", 6, 6.66));
        order.placeOrder(new Item("CATEGORY2", "NAME2", "211232564", 5, 7.66));
        order.placeOrder(new Item("CATEGORY2", "NAME2", "ajsdhbajf", 1, 76.6));
        order.placeOrder(new Item("CATEGORY2", "NAME2", "djannjahc", 1, 76.6));
        addOrder(order);
    
        order = new Order();
        order.placeOrder(new Item("CATEGORY1", "NAME1", "989712277", 500, 0.66));
        addOrder(order);
    
        order = new Order();
        order.placeOrder(new Item("CATEGORY1", "NAME1", "989712277", 60, 0.99));
        order.placeOrder(new Item("CATEGORY2", "NAME2", "211232564", 51, 0.66));
        addOrder(order);
    }
}
