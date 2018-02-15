package comp3350.g16.inventorytracking.business;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import comp3350.g16.inventorytracking.application.Services;
import comp3350.g16.inventorytracking.objects.Order;
import comp3350.g16.inventorytracking.objects.Status;
import comp3350.g16.inventorytracking.persistence.OrderInterface;

//-----------------------------------------------------------------------------
//  AccessOrders
//      Midpoint between Presentation and Persistence.
//-----------------------------------------------------------------------------
public class AccessOrders
{
    private OrderInterface dataAccess;

    public AccessOrders()
    {
        dataAccess = Services.getOrderAccess();
    }
    
    public void getList(List<Order> orderList)
    {
        if( orderList != null )
            dataAccess.getList(orderList);
    }
    
    // gets a list of orders whose statuses exclude the parameter Status removedStatus
    public List<Order> getStatusOrdersWithout( Status removedStatus )
    {
        Order currentOrder;
        LinkedList<Order> statusList = new LinkedList<>();
        LinkedList<Order> list = new LinkedList<>();
        dataAccess.getList(list);
        
        Iterator<Order> listIter = list.listIterator();
        
        while( listIter.hasNext() )
        {
            currentOrder = listIter.next();
            
            if( currentOrder.getStatus() != removedStatus )
                statusList.add(currentOrder);
        }
        
        return new LinkedList<Order>(statusList);
    }

    public Order search(int orderNumber)
    {
        Order order = null;
        
        if (orderNumber > 0 )
            order = dataAccess.search(orderNumber);
        
        return order;
    }

    public boolean addOrder(Order newOrder)
    {
        boolean added = false;
        
        if( newOrder != null )
            added = dataAccess.addOrder(newOrder);
        
        return added;
    }
    
    public int size()
    {
        return dataAccess.size();
    }
}
