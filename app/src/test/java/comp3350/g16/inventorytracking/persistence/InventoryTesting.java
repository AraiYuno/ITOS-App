package comp3350.g16.inventorytracking.tests.persistence;

/**
 * Created by vaibhavarora on 2017-06-28.
 */
;


import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Test;

import comp3350.g16.inventorytracking.persistence.Inventory;
import comp3350.g16.inventorytracking.objects.*;

public class InventoryTesting {

    private Inventory inventory;
    

    @Before
    public void init()
    {
        inventory=new Inventory();
        inventory.clear();
        
    }

    @Test
    public void testIsEmptyOnEmpty()
    {
        System.out.println("\n=========================== \nNow testing isEmptyOnEmpty\n");
        assertTrue(inventory.size()==0 );
    }



    @Test
    public void testIsEmptyOnNotEmpty()
    {
        System.out.println("\n=========================== \nNow testing isEmptyOnNotEmpty\n");
        inventory.addItem( new Item("VideoGame", "Witcher3", "1", 1, 60.00 ) );
        assertTrue( !inventory.isEmpty() );
        inventory.clear();
    }

    @Test
    public void testAddItem() {

        System.out.println("\n=========================== \nNow testing addItem\n");
        Item newItem1= new Item("Game","GTA","xyz-123",1, 100.00);
        Item newItem2= new Item("books", "fudu" , "abc-321" ,1,  50.00);
        Item newItem3= new Item("books", "harry potter" , "def-789" ,1,  55.00);

        inventory.addItem(newItem1);

        assertTrue(!(inventory.isEmpty()));

        inventory.addItem(newItem2);
        inventory.addItem(newItem3);

        System.out.println("\n=========================== \nNow testing size of inventory \n");
        int size= inventory.size();

        assertTrue(size==3);

        System.out.println("\n=========================== \nNow testing size of inventory by adding 100000 items \n");

        for( int i = 0; i < 10000; i++ )
        {
            inventory.addItem(new Item( "VideoGame", "FallOut4", Integer.toString(i),1,  69.99));
        }

        assertTrue( inventory.size() == 10003 );

        System.out.println("\n=========================== \nNow testing items at position 2 \n");

        assertTrue((inventory.search("def-789").compareTo(newItem3))==0);

        inventory.clear();

        assertTrue((inventory.isEmpty()));


    }

    @Test
    public void testRemoveItem() {

        System.out.println("\n=========================== \n add 1000 items to inventory \n");

        for( int i = 0; i < 1000; i++ )
        {
            inventory.addItem(new Item( "VideoGame", "FallOut4", Integer.toString(i), 1, 69.99));
        }

        assertTrue( inventory.size() == 1000 );


        System.out.println("\n=========================== \n removing all the items from the inventory  \n");

        for( int i = 0; i < 1000; i++ )
        {
            inventory.removeItem(Integer.toString(i)); // checking removing adding item

        }



        assertTrue( inventory.size() == 0 );
        assertTrue((inventory.isEmpty()));

    }

    @Test
    public void testUpdateItem() {
        System.out.println("\n=========================== \nNow testing updateItem\n");
        System.out.println("1. Test when the item update is successful. < True case >");
        inventory.addItem(new Item("VideoGame", "Witcher3", "32",1, 55.59));
        inventory.updateItem("32", 2);
        inventory.toString();
        inventory.clear();

        System.out.println("2. Test when he item update is unsuccessful due to wrong productID < False case >");
        inventory.addItem(new Item("VideoGame", "Witcher3", "33", 1, 55.59));
        inventory.updateItem("35", 3);
        inventory.toString();

        System.out.println("3. Test for negative quantity value. < Should be still 0 >");
        inventory.updateItem("33", -3);
        inventory.toString();

        System.out.println("4. Test for decreased quantity. < 5 - 2 = 3 > ");
        inventory.updateItem("33", 5);
        inventory.toString();
        inventory.updateItem("33", -2);
        inventory.toString();

        inventory.clear();

        assertTrue((inventory.isEmpty()));
    }


    @Test
    public void testSearch() {

        System.out.println("\n=========================== \nNow testing Now testing Search Item\n");
        Item newItem1= new Item("Game","GTA","xyz-123",1, 100.00);
        Item newItem2= new Item("books", "fudu" , "abc-321" , 1, 50.00);
        Item newItem3= new Item("books", "harry potter" , "def-789" , 1, 55.00);

        inventory.addItem(newItem1);
        inventory.addItem(newItem2);
        inventory.addItem(newItem3);

        assertTrue((inventory.search("xyz-123").compareTo(newItem1))==0);


        assertTrue((inventory.search("def-789").compareTo(newItem3))==0);


        inventory.clear();

        assertTrue((inventory.isEmpty()));

    }



}


