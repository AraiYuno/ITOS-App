package comp3350.g16.inventorytracking.application;

import comp3350.g16.inventorytracking.persistence.Inventory;
import comp3350.g16.inventorytracking.persistence.InventoryInterface;
import comp3350.g16.inventorytracking.persistence.InventorySQL;
import comp3350.g16.inventorytracking.persistence.OrderInterface;
import comp3350.g16.inventorytracking.persistence.OrderQueue;
import comp3350.g16.inventorytracking.persistence.OrderQueueSQL;

//-----------------------------------------------------------------------------
//  Services
//      Keeps a reference to the database.
//      Right now it's a stub database
//-----------------------------------------------------------------------------
public class Services
{
    private static OrderInterface orderAccess = null;
    private static InventoryInterface itemAccess = null;
    

    public static void createDatabaseAccess()
    {
        if( orderAccess == null )
        {
            orderAccess = new OrderQueue();
//            orderAccess = new OrderQueueSQL();
        }
        
        if( itemAccess == null ) {
            itemAccess = new Inventory();
//            itemAccess = new InventorySQL();
        }
    }

    public static OrderInterface getOrderAccess()
    {
        return orderAccess;
    }
    
    public static InventoryInterface getItemAccess()
    {
        return itemAccess;
    }
}
