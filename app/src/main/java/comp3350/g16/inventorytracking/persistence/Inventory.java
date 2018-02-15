package comp3350.g16.inventorytracking.persistence;

import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import comp3350.g16.inventorytracking.objects.Item;

//-----------------------------------------------------------------------------
//  Inventory
//      A stub list of all Items
//-----------------------------------------------------------------------------
public class Inventory implements InventoryInterface
{
    private Hashtable<String, Item> inventory;
    private int size;

    public Inventory()
    {
        this.inventory = new Hashtable<String, Item>();
        this.size = 0;

//        initialize();
        moreInitalize();
    }

    //----------------------------------------------------------
    //	addItem:	adds a new Item to the inventory if it is
    //				not already in the inventory.
    //	Returns:	false, if it has been added
    //---------------------------------------------------------=
    public boolean addItem( Item newItem )
    {
        boolean added = false;

        if( newItem != null )
        {
            String productID = newItem.getProductID();

//            if( inventory.putIfAbsent(productID, newItem) == null )
            if( !inventory.containsKey(productID) )
            {
                inventory.put(productID, newItem);
                added = true;
                size++;
            }
        }

        return added;
    }

    //----------------------------------------------------------
    //	removeItem:	removes an item from the inventory
    //	Returns:	the removed item, null if not found
    //----------------------------------------------------------
    public Item removeItem( String itemID )
    {
        Item removed = null;

        if( itemID != null )
        {
            removed = inventory.remove(itemID);

            if( removed != null )
                size--;
        }

        return removed;
    }

    //----------------------------------------------------------
    //	getList:	returns the inventory in the form of a list
    //----------------------------------------------------------
    public void getList(List<Item> itemList)
    {
		// create a linked list of items from hash table
		LinkedList<Item> values = new LinkedList<>(inventory.values());
	
		// sorts according to product ID
		Collections.sort(values);
		
		// fills parameter list with these values
        itemList.clear();
        itemList.addAll( values );
    }

    public String toString()
    {
        LinkedList<Item> values = new LinkedList<>();
        String returnString = null;
		
		getList(values);

        for( Item currItem : values )
            returnString += currItem.toString() + "\n";

        return returnString;
    }

    //----------------------------------------------------------
    //	updateItem:	update quantity of item in inventory
    //	Returns:	true if item quantity was updated
    //----------------------------------------------------------
    public boolean updateItem(String productID, int value)
    {
        boolean updated = false;

        if( productID != null )
        {
            Item currItem = inventory.get(productID);

            if( currItem != null )
                updated = currItem.updateQuantity(value);
        }

        return updated;
    }

    //----------------------------------------------------------
    //	search:		searches for an item in the inventory
    //	Returns:	item if found, null if not
    //----------------------------------------------------------
    public Item search( String itemID )
    {
        Item getItem = null;

        if( itemID != null )
            getItem = inventory.get(itemID);

        return getItem;
    }

    public int size()
    {
        return this.size;
    }
    public void clear()
    {
        size = 0;
        inventory.clear();
    }
    public boolean isEmpty()
    {
        return inventory.isEmpty();
    }
    
    public List<Item> searchByName(String name)
    {
        // returns a list of items matching a name
        LinkedList<Item> values = new LinkedList<Item>(inventory.values());
        LinkedList<Item> searchList = new LinkedList<>();
        ListIterator<Item> listIter = values.listIterator();
        
        Item currentItem;
        
        while( listIter.hasNext() )
        {
            currentItem = listIter.next();
            
            if( name.equalsIgnoreCase((currentItem.getName())) )
                searchList.add(currentItem);
        }
        
        if( searchList.size() == 0 )
            searchList = null;
        
        return searchList;
    }

    private void initialize()
    {
        addItem( new Item("book", "catcher", "123-abc", 0, 20.99));
        addItem( new Item("book", "in the rye", "124-abc", 10, 20.99));
        addItem( new Item("book", "a really long and pretentious title because we r mart", "777-ooo", 15, 11.99));
        addItem( new Item("book", "jane", "456-efg", 0, 10.99));
        addItem( new Item("book", "eyre", "789-hij", 19, 8.99));
        addItem( new Item("book", "catcher", "123-acc", 0, 20.99));

        addItem( new Item("game", "ck2", "769-ck2", 0, 59.99));
        addItem( new Item("game", "stellaris", "7854-a", 6, 79.99));
        addItem( new Item("game", "mass effect", "1107-n", 10, 40.99));
        addItem( new Item("game", "some other video game", "789-qwe", 0, 79.99));
        addItem( new Item("game", "super deluxe edition", "345-asdc", 19, 129.99));
        addItem( new Item("game", "indie", "465-gjs", 0, 1.99));

    }
    
    // initializes better titles and ids for the list
    private void moreInitalize()
    {
        addItem ( new Item("Video Game", "Stellaris", "82125680", 7, 69.99) );
        addItem ( new Item("Video Game", "Grand Theft Auto V", "22010663", 5, 69.99) );
        addItem ( new Item("Video Game", "Mount & Blade: Warband", "81871678", 3, 49.99) );
        addItem ( new Item("Video Game", "Some Indie Game No One Has Heard Of", "01380038", 20, 0.99) );
        addItem ( new Item("Video Game", "Super Deluxe Edition", "44250583", 1, 129.99) );
        addItem ( new Item("Video Game", "Mass Effect", "75285866", 8, 19.99) );
        addItem ( new Item("Video Game", "The Wolf Among Us", "05503457", 6, 27.99) );
        addItem ( new Item("Video Game", "Mass Effect: Andromeda", "76750220", 2, 79.99) );
        addItem ( new Item("Video Game", "Tomb Raider", "33306682", 100, 69.99) );
    
        addItem ( new Item("Book", "Jane Eyre", "48820132", 3, 10.99) );
        addItem ( new Item("Book", "A Song of Ice and Fire: Box Set", "87856233", 10, 64.67) );
        addItem ( new Item("Book", "A Really long and pretentious title that the author came up because they think they're better than you (censored version)", "72460260", 9, 19.99) );
        addItem ( new Item("Book", "The Very Hungry Caterpillar", "52101274", 1, 5.99) );
        addItem ( new Item("Book", "For Dummies: Learn Java", "80361141", 10, 20.99 ) );
        addItem ( new Item("Book", "The Catcher in the Rye", "36025341", 5, 5.99) );
        addItem ( new Item("Book", "Fierce Kingdom: A novel", "80883013", 5, 19.99) );
        addItem ( new Item("Book", "War and Peace", "13024523", 6, 30.99) );
        addItem ( new Item("Book", "Overpriced Textbook", "38771568", 7, 299.99) );
    
        addItem ( new Item("Movie/TV", "Jane Eyre", "48713705", 5, 20.99) );
        addItem ( new Item("Movie/TV", "Game of Thrones: Box Set", "52581355", 2, 90.99) );
        addItem ( new Item("Movie/TV", "Movie Version of the Book", "68780043", 10 , 7.99) );
        addItem ( new Item("Movie/TV", "The Very Hungry Caterpillar", "70178864", 7, 29.99) );
    }
    
}
