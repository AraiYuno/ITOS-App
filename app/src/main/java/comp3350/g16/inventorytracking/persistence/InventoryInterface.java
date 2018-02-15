package comp3350.g16.inventorytracking.persistence;

import java.util.List;

import comp3350.g16.inventorytracking.objects.Item;

public interface InventoryInterface {
    boolean addItem (Item item);
    Item removeItem (String itemID);
    void getList(List<Item> list);
    boolean updateItem(String productID, int value);
    Item search(String itemID);
    int size();
    
    List<Item> searchByName( String name );
}
