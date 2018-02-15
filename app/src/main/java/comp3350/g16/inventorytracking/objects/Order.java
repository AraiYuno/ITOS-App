package comp3350.g16.inventorytracking.objects;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;       // to create randon numbers, KYLE.
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;


//--------------------------------------------------------------------
// Order: A list of items that compose an order
//--------------------------------------------------------------------

public class Order {
    private LinkedList<Item> items;
    private double orderPrice;
    private int size;
    private int orderNumber;
    private Calendar origDate;
    private Calendar arrivalDate;
    private Status orderStatus;

    // each order has a unique order number, so this counter increases for each order
    private static int count = 99999;

    //--------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------
    public Order() {
        count++;

        this.items = new LinkedList<Item>();
        this.orderPrice = 0;
        this.size = 0;
        this.orderNumber = count;
        this.orderStatus = Status.NEW;
    
    
        this.origDate = Calendar.getInstance();
        this.arrivalDate = null;
    }

    // for possible testing
    public Order(double price) {
        this();
        this.orderPrice = price;
    }

    //--------------------------------------------------------------------
    // placeOrder:  Adds an item to the order list.
    //
    //     Checks if item is in the list, and if it is
    //     not then it is added to the order list.
    //     If it is, then the item quantity is updated
    //--------------------------------------------------------------------
    public void placeOrder(Item newItem)
    {
        if( newItem != null )
        {
            String id = newItem.getProductID();
            int quantity = newItem.getQuantity();
            double price = newItem.getPrice();
    
            ListIterator<Item> listIter = getItemPosn(id);
            Item foundItem;
            
            if( listIter.hasNext() )
            {
                foundItem = listIter.next();
                foundItem.updateQuantity( newItem.getQuantity() );
                
                orderPrice += quantity*( foundItem.getPrice() );
            }
            else
            {
                listIter.add(newItem);
                size++;
                orderPrice += quantity*price;
            }
            
        }
    }
    
    
    //--------------------------------------------------------------------
    // removeOrderItem: Removes an item from the list
    //--------------------------------------------------------------------
    public Item removeOrderItem(String id) {
        ListIterator<Item> listIter = getItemPosn(id);
        Item removedItem = null;

        if (listIter.hasNext()) {
            removedItem = listIter.next();

            // reset list pointer and remove
            listIter.previous();
            listIter.remove();

            orderPrice -= (removedItem.getQuantity()) * (removedItem.getPrice());
            size--;
        }
        
        return removedItem;
    }

    //--------------------------------------------------------------------
    // orderDetails:  Returns a string with order/item details
    //--------------------------------------------------------------------
    public String orderDetails() {
        ListIterator<Item> listIter = items.listIterator();
        String listString = null;
        Item currItem = null;
        int itemNumber = 1;

        listString = "ORDER NO. " + orderNumber + "\t\tSTATUS: " + orderStatus.toString();

        while (listIter.hasNext()) {
            currItem = listIter.next();
            listString += "\n" + itemNumber + ") " + currItem.toString();
            itemNumber++;
        }

        listString += "\nOrder Total:\t$ " + orderPrice;

        return listString;
    }

    //--------------------------------------------------------------------
    // toString:  Order details (minus full item list) as a string
    //--------------------------------------------------------------------
    public String toString() {
        return "ORDER NO. " + orderNumber + "\tSTATUS: " + orderStatus.toString() + "\tTOTAL: $" + orderPrice;
    }

    //--------------------------------------------------------------------
    // placeOrder:  Searches for an item by product id, and returns
    //     item if found, or null if (NOT found || id == null)
    //--------------------------------------------------------------------
    public Item search(String id) {
        ListIterator<Item> itemsIter = getItemPosn(id);
        Item currItem = null;

        if (itemsIter != null && itemsIter.hasNext())
            currItem = itemsIter.next();

        return currItem;
    }

    //--------------------------------------------------------------------
    // getItemPosn  Searches for an item in the list by product id
    //
    //     Returns a ListIterator where next() will return
    //     the searched item.
    //     If item is not in list, then hasNext() is false.
    //     Returns null if given string is null;
    //--------------------------------------------------------------------
    private ListIterator<Item> getItemPosn(String id) {
        ListIterator<Item> itemsIter = items.listIterator(); // traversing list
        boolean itemFound = false;
        Item currItem = null;
        String currID;

        // search for item in list
        if (id != null) {
            while (itemsIter.hasNext() && !itemFound) {
                currItem = itemsIter.next();
                currID = currItem.getProductID();

                if (currID.equalsIgnoreCase(id)) {
                    itemsIter.previous();
                    itemFound = true;
                }
            }
        } else
            itemsIter = null;

        return itemsIter;
    }

    //--------------------------------------------------------------------
    // SETTERS
    //--------------------------------------------------------------------
    public void setOrderStatus(Status newStatus)
    {
        this.orderStatus = newStatus;
        
        // once a status is approved, it should get the estimated order arrival date
        if ( newStatus == Status.APPROVED )
            calculateArrivalDate();
        else
            this.arrivalDate = null;
    }
    
    public void changeItemQuantity(Item currentItem, int newQuantity) {
        if( newQuantity >= 0 )
        {
            int quantity = currentItem.getQuantity();
            double unitPrice = currentItem.getPrice();
            currentItem.setQuantity(newQuantity);
            
            if( quantity > newQuantity )
                orderPrice -= (quantity-newQuantity)*unitPrice; // decreasing quantity
            else if( newQuantity > quantity )
                orderPrice += (newQuantity-quantity)*unitPrice; // increasing quantity
            
            currentItem.setQuantity( newQuantity );
        }
    }
    
    // If a user creates an order, but doesn't save it, the counter will need be decremented so
    // so that the order number can be re-used for an order that will be saved
    public static void decrementCounter()
    {
        if( count > 0 )
            count--;
    }
    
    private void calculateArrivalDate()
    {
        Random random = new Random();
        int randomNumber = random.nextInt(14) + 1;
        int origDayOfMonth = origDate.get(Calendar.DAY_OF_MONTH);
        
        arrivalDate = Calendar.getInstance();
        arrivalDate.set(Calendar.DAY_OF_MONTH, randomNumber + origDayOfMonth);
    }

    //--------------------------------------------------------------------
    // GETTERS
    //--------------------------------------------------------------------
    public int getOrderNumber() {
        return orderNumber;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public int getSize() {
        return size;
    }

    public Status getStatus() {
        return orderStatus;
    }
    
    public Calendar getArrivalDate() {
        return this.arrivalDate;
    }
    
    public Calendar getOrderDate()
    {
        return this.origDate;
    }


    public List<Item> getList() {
        return this.items;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setItems(LinkedList<Item> items) {
        this.items = items;
    }


}
