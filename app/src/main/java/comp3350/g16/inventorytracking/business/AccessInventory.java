package comp3350.g16.inventorytracking.business;

import java.util.List;

import comp3350.g16.inventorytracking.application.Services;
import comp3350.g16.inventorytracking.objects.Item;
import comp3350.g16.inventorytracking.persistence.InventoryInterface;

//-----------------------------------------------------------------------------
//  AccessInventory
//      Midpoint between Presentation and Persistence.
//-----------------------------------------------------------------------------
public class AccessInventory
{
    private InventoryInterface inventoryAccess;

    public AccessInventory()
    {
        inventoryAccess = Services.getItemAccess();
    }
    
    public Item search(String productID)
    {
        Item searchItem = null;
        
        if( productID != null )
            searchItem = inventoryAccess.search( productID.toLowerCase() );
    
        return searchItem;
    }
    
    public void getList(List<Item> list )
    {
        if( list != null )
            inventoryAccess.getList(list);
    }
    
    public boolean addItem(Item newItem)
    {
        boolean added = false;
        
        if( ItemValidator.validItem(newItem) )
           added = inventoryAccess.addItem(newItem);
        
        return added;
    }
    
    public Item removeItem(String productID)
    {
        Item removedItem = null;
        
        if( productID != null && productID.length() > 0 )
            removedItem = inventoryAccess.removeItem(productID);
        
        return removedItem;
    }
    
    public List<Item> searchByName(String name)
    {
        List<Item> list = null;
        
        if( name != null && name.length() > 0 )
            list = inventoryAccess.searchByName( name.toLowerCase() );
        
        return list;
    }
    
    public int size() {
        return inventoryAccess.size();
    }
}
